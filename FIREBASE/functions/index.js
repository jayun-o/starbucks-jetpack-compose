/**
 * Import function triggers from their respective submodules:
 *
 * const {onCall} = require("firebase-functions/v2/https");
 * const {onDocumentWritten} = require("firebase-functions/v2/firestore");
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

const {onDocumentCreated} = require("firebase-functions/v2/firestore");
const admin = require("firebase-admin");

// Initialize Firebase Admin
admin.initializeApp();
const db = admin.firestore();

/**
 * Firestore trigger that executes when a new order is created
 * Sends email notification with order details
 * Triggered on: /order/{orderId}
 */
exports.createEmailDocument = onDocumentCreated("order/{orderId}", async (event) => {
  const snapshot = event.data;
  const order = snapshot ? snapshot.data() : null;

  if (!order) {
    console.log("🔴 No order data found.");
    return null;
  }

  console.log("🟢 New order received:", order);

  try {
    // Fetch customer details
    const customerDoc = await db.collection("customer").doc(order.customerId).get();
    const customer = customerDoc.exists ? customerDoc.data() : null;

    if (!customer) {
      console.warn(`🔴 Customer with ID ${order.customerId} not found.`);
    }

    // Fetch product details for all items
    const productPromises = order.items.map(async (item) => {
      const productDoc = await db.collection("product").doc(item.productId).get();

      if (!productDoc.exists) {
        console.warn(`🔴 Product with ID ${item.productId} not found.`);
        return null;
      }
      return productDoc.data();
    });

    const productDetails = await Promise.all(productPromises);

    // Build cart items HTML with product details and customizations
    const cartItemsHtml = order.items
      .map((item, index) => {
        const product = productDetails[index];
        
        // Build customization details
        let customizations = [];
        if (item.size) customizations.push(`Size: ${item.size}`);
        if (item.shotCountEspresso > 0) customizations.push(`Espresso Shots: ${item.shotCountEspresso}`);
        if (item.shotCountHalfDecaf > 0) customizations.push(`Half-Decaf Shots: ${item.shotCountHalfDecaf}`);
        if (item.shotCountDecaf > 0) customizations.push(`Decaf Shots: ${item.shotCountDecaf}`);
        if (item.milk) customizations.push(`Milk: ${item.milk}`);
        if (item.sweetness) customizations.push(`Sweetness: ${item.sweetness}`);
        if (item.flavors && item.flavors.length > 0) customizations.push(`Flavors: ${item.flavors.join(", ")}`);
        if (item.toppings && item.toppings.length > 0) customizations.push(`Toppings: ${item.toppings.join(", ")}`);
        if (item.condiments && item.condiments.length > 0) customizations.push(`Condiments: ${item.condiments.join(", ")}`);
        if (item.cutlery) customizations.push(`Cutlery: Yes`);
        if (item.warmUp) customizations.push(`Warm Up: Yes`);

        const customizationHtml = customizations.length > 0 
          ? `<br><small style="color: #666;">${customizations.join(" | ")}</small>`
          : "";

        return `<li style="margin-bottom: 10px;">
          <strong>${product ? product.title : "Unknown Product"}</strong>
          ${customizationHtml}
          <br>${item.price.toFixed(2)} x ${item.quantity}
        </li>`;
      })
      .join("");

    const paymentMethod = order.token ? `PAY WITH PAYPAL (${order.token})` : "PAY ON DELIVERY";

    // Create email data
    const emailData = {
      to: ["typ.pharita@gmail.com"],
      message: {
        subject: `🎉 New Starbucks Order Received (${order.id.substring(0, 8)})`,
        html: `
          <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
            <h1 style="color: #00704A;">☕ New Order Received</h1>
            
            <h2>🛒 Order Items:</h2>
            <ul style="list-style-type: none; padding: 0;">
              ${cartItemsHtml}
            </ul>
            
            <h2>💰 Order Total:</h2>
            <p><strong style="font-size: 18px; color: #00704A;">${order.totalAmount.toFixed(2) + " THB"} </strong></p>
            
	    	    <h2>💳 Payment Method:</h2>
            <p><strong>${paymentMethod}</strong></p>

            <h2>👤 Customer Information:</h2>
            <table style="width: 100%; border-collapse: collapse;">
              <tr>
                <td style="padding: 5px;"><strong>Name:</strong></td>
                <td style="padding: 5px;">${customer ? `${customer.firstName} ${customer.lastName}` : "N/A"}</td>
              </tr>
              <tr>
                <td style="padding: 5px;"><strong>Email:</strong></td>
                <td style="padding: 5px;">${customer ? customer.email : "N/A"}</td>
              </tr>
              <tr>
                <td style="padding: 5px;"><strong>Phone:</strong></td>
                <td style="padding: 5px;">${customer && customer.phoneNumber ? customer.phoneNumber : "N/A"}</td>
              </tr>
              <tr>
                <td style="padding: 5px;"><strong>Location:</strong></td>
                <td style="padding: 5px;">${customer && customer.location ? customer.location : "N/A"}</td>
              </tr>
              <tr>
                <td style="padding: 5px;"><strong>Address:</strong></td>
                <td style="padding: 5px;">${customer && customer.address ? customer.address : "N/A"}</td>
              </tr>
              <tr>
                <td style="padding: 5px;"><strong>Postal Code:</strong></td>
                <td style="padding: 5px;">${customer && customer.postalCode ? customer.postalCode : "N/A"}</td>
              </tr>
            </table>
            
            <hr style="margin: 20px 0; border: none; border-top: 1px solid #ddd;">
            <p style="color: #666; font-size: 12px;">Order ID: ${order.id}</p>
          </div>
        `,
      },
    };

    // Add the email request to the mail collection
    await db.collection("mail").add(emailData);
    console.log("🟢 Mail document added to the collection successfully.");

  } catch (error) {
    console.error("🔴 Error while trying to create a new mail document:", error);
  }

  return null;
});