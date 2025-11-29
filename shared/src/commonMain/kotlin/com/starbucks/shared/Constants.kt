package com.starbucks.shared

object Constants {
    const val WEB_CLIENT_ID = "302361989840-rc8562obbtmh1c4m9dmubb17ui990r1v.apps.googleusercontent.com"
    const val GOOGLE_API = "AIzaSyDK1Bnel63ZoNCKfo4V2IHNRUoXcISUzHY"
    const val PAYPAL_CLIENT_ID = "AdUgb_Lb1eUE0E8rd8pK9SKwNouElU_QtIoskzA4tX1tRcQqwUBrRGL1Fh8qbA-YLIyHKm5VM42leSWX"
    const val PAYPAL_SECRET_ID = "EEiZF2PNirKAiLb7YRC9CAn8gA-K6Vu_Ck2ZBiLixXooKWt4ykOYO-t0HOtjh8gVs5oB0jm_3dKLMbaE"

    const val PAYPAL_AUTH_KEY = "$PAYPAL_CLIENT_ID:$PAYPAL_SECRET_ID"

    const val PAYPAL_AUTH_ENDPOINT = "https://api-m.sandbox.paypal.com/v1/oauth2/token"
    const val PAYPAL_CHECKOUT_ENDPOINT = "https://api-m.sandbox.paypal.com/v2/checkout/orders"

    const val RETURN_URL = "com.typhoon.starbucks://paypalpay?success=true"
    const val CANCEL_URL = "com.typhoon.starbucks://paypalpay?cancel=true"

    const val MAX_QUANTITY = 10
    const val MIN_QUANTITY = 1

}