package com.google.android.finsky.config;

/* JADX INFO: loaded from: classes.dex */
public class G {
    public static GservicesValue<Boolean> debugOptionsEnabled = GservicesValue.value("finsky.debug_options_enabled", false);
    public static GservicesValue<Long> androidId = GservicesValue.value("android_id", (Long) 0L);
    public static GservicesValue<String> authTokenType = GservicesValue.value("finsky.auth_token_type", "androidmarket");
    public static GservicesValue<String> vendingAuthTokenType = GservicesValue.value("vending.auth_token_type", "android");
    public static GservicesValue<String> vendingSecureAuthTokenType = GservicesValue.value("vending.secure_auth_token_type", "androidsecure");
    public static GservicesValue<String> webViewUserAgent = GservicesValue.value("finsky.web_view_user_agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_5_7; en-us) AppleWebKit/530.17 (KHTML, like Gecko) Version/4.0 Safari/530.17");
    public static GservicesValue<String> appsRefundPolicyUrl = GservicesValue.value("finsky.apps_refund_policy_url", "http://market.android.com/support/bin/answer.py?answer=134336&hl=en");
    public static GservicesValue<String> booksRefundPolicyUrl = GservicesValue.value("finsky.books_refund_policy_url", "http://books.google.com/support/bin/answer.py?hl=en&answer=1062968");
    public static GservicesValue<String> moviesRefundPolicyUrl = GservicesValue.value("finsky.movies_refund_policy_url", "http://www.google.com/support/youtube/bin/answer.py?hl=en&answer=166672");
    public static GservicesValue<String> billingPrivacyPolicyUrl = GservicesValue.value("finsky.billing_privacy_policy_url", "https://checkout.google.com/files/privacy.html");
    public static GservicesValue<String> widgetUrl = GservicesValue.value("finsky.widget_url", "promo?c=3");
    public static GservicesValue<String> phoneDefaultContactUsUrl = GservicesValue.value("finsky.phone_default_contact_us_url", "http://www.google.com/support/androidmarket/bin/request.py?contact_type=xoom_apps");
    public static GservicesValue<String> phoneBooksContactUsUrl = GservicesValue.value("finsky.phone_books_contact_us_url", "http://books.google.com/support/bin/request.py?contact_type=eb_device_xoom");
    public static GservicesValue<String> phoneMoviesContactUsUrl = GservicesValue.value("finsky.phone_movies_contact_us_url", "http://www.google.com/support/youtube/bin/request.py?contact_type=yt_device_xoom");
    public static GservicesValue<String> tabletDefaultContactUsUrl = GservicesValue.value("finsky.tablet_default_contact_us_url", "http://www.google.com/support/androidmarket/bin/request.py?contact_type=xoom_apps");
    public static GservicesValue<String> tabletBooksContactUsUrl = GservicesValue.value("finsky.tablet_books_contact_us_url", "http://books.google.com/support/bin/request.py?contact_type=eb_device_xoom");
    public static GservicesValue<String> tabletMoviesContactUsUrl = GservicesValue.value("finsky.tablet_movies_contact_us_url", "http://www.google.com/support/youtube/bin/request.py?contact_type=yt_device_xoom");
    public static GservicesValue<String> defaultHelpUrl = GservicesValue.value("finsky.default_help_url", "http://market.android.com/support");
    public static GservicesValue<String> appsHelpUrl = GservicesValue.value("finsky.apps_help_url", "http://www.google.com/support/androidmarket/bin/answer.py?answer=136634");
    public static GservicesValue<String> booksHelpUrl = GservicesValue.value("finsky.books_help_url", "http://books.google.com/support/bin/topic.py?topic=28528");
    public static GservicesValue<String> moviesHelpUrl = GservicesValue.value("finsky.movies_help_url", "http://www.google.com/support/youtube/bin/answer.py?answer=1221183");
    public static GservicesValue<String> purchaseHistoryUrl = GservicesValue.value("finsky.purchase_history_url", "http://market.android.com/account");
    public static GservicesValue<String> myEBooksUrl = GservicesValue.value("finsky.my_ebooks_url", "http://books.google.com/ebooks");
    public static GservicesValue<String> readBookUrl = GservicesValue.value("finsky.read_book_url", "http://books.google.com/ebooks/reader");
    public static GservicesValue<String> myMoviesUrl = GservicesValue.value("finsky.my_movies_url", "http://www.youtube.com/my_purchases");
    public static GservicesValue<Integer> defaultContentFilterLevel = GservicesValue.value("finsky.default_content_filter_level", (Integer) 3);
    public static GservicesValue<String> contentFilterInfoUrl = GservicesValue.value("finsky.content_filter_info_url", "http://market.android.com/support/bin/answer.py?answer=1075738");
    public static GservicesValue<Boolean> analyticsEnabled = GservicesValue.value("finsky.analytics_enabled", true);
    public static GservicesValue<String> analyticsAccountId = GservicesValue.value("finsky.google_analytics_account_id", "UA-19761900-1");
    public static GservicesValue<Boolean> contentRatingEnabled = GservicesValue.value("finsky.content_rating_enabled", false);
    public static GservicesValue<String> loggingId = GservicesValue.partnerSetting("logging_id2", "");
    public static GservicesValue<String> clientId = GservicesValue.partnerSetting("market_client_id", "am-google");
    public static final GservicesValue<Boolean> vendingAlwaysSendConfig = GservicesValue.value("vending_always_send_config", false);
    public static final GservicesValue<Long> vendingSyncFrequencyMs = GservicesValue.value("vending_sync_frequency_ms", (Long) 86400000L);
}
