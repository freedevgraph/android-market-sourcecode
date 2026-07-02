package com.google.android.finsky.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import com.google.android.finsky.R;
import com.google.android.finsky.activities.AppsUrlHandlerActivity;
import com.google.android.finsky.activities.BooksUrlHandlerActivity;
import com.google.android.finsky.activities.MainActivity;
import com.google.android.finsky.activities.MusicUrlHandlerActivity;
import com.google.android.finsky.activities.VideoUrlHandlerActivity;
import com.google.android.finsky.api.AccountHandler;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.config.G;
import com.google.android.finsky.model.ChannelList;
import com.google.android.finsky.model.ChannelTab;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class IntentUtils {
    private static final ConsumptionApp BOOKS_APP = new ConsumptionApp(BooksUrlHandlerActivity.class) { // from class: com.google.android.finsky.utils.IntentUtils.1
        @Override // com.google.android.finsky.utils.IntentUtils.ConsumptionApp
        public Intent buildViewCollectionIntent(PackageManager pm) {
            Intent intent = pm.getLaunchIntentForPackage("com.google.android.apps.books");
            intent.setAction("android.intent.action.MAIN");
            setDefaultFlags(intent);
            addAccountExtra(intent, "authAccount");
            return intent;
        }

        @Override // com.google.android.finsky.utils.IntentUtils.ConsumptionApp
        public Intent buildViewSampleIntent(PackageManager pm, Document doc) {
            Intent intent = buildViewItemIntent(pm, doc);
            intent.setFlags(268435456);
            return intent;
        }

        @Override // com.google.android.finsky.utils.IntentUtils.ConsumptionApp
        public Intent buildViewItemIntent(PackageManager pm, Document doc) {
            Uri.Builder uri = Uri.parse(G.readBookUrl.get()).buildUpon().appendQueryParameter("id", doc.getBackendDocId());
            Intent intent = new Intent("android.intent.action.VIEW", uri.build());
            intent.setPackage("com.google.android.apps.books");
            intent.setFlags(268451840);
            addAccountExtra(intent, "authAccount");
            intent.putExtra("books:addToMyEBooks", false);
            return intent;
        }
    };
    private static final ConsumptionApp BOOKS_WEB = new ConsumptionApp(BooksUrlHandlerActivity.class) { // from class: com.google.android.finsky.utils.IntentUtils.2
        private void addAccountParameter(Uri.Builder builder) {
            String account = AccountHandler.getCurrentAccount();
            if (account != null) {
                builder.appendQueryParameter("email", account);
            }
        }

        @Override // com.google.android.finsky.utils.IntentUtils.ConsumptionApp
        public Intent buildViewCollectionIntent(PackageManager pm) {
            Uri.Builder builder = Uri.parse(G.myEBooksUrl.get()).buildUpon();
            addAccountParameter(builder);
            Intent intent = new Intent("android.intent.action.VIEW", builder.build());
            setDefaultFlags(intent);
            return intent;
        }

        @Override // com.google.android.finsky.utils.IntentUtils.ConsumptionApp
        public Intent buildViewItemIntent(PackageManager pm, Document doc) {
            Uri.Builder builder = Uri.parse(G.readBookUrl.get()).buildUpon().appendQueryParameter("id", doc.getBackendDocId());
            addAccountParameter(builder);
            Intent intent = new Intent("android.intent.action.VIEW", builder.build());
            setDefaultFlags(intent);
            return intent;
        }
    };
    private static final ConsumptionApp MY_APPS = new ConsumptionApp(AppsUrlHandlerActivity.class) { // from class: com.google.android.finsky.utils.IntentUtils.3
        @Override // com.google.android.finsky.utils.IntentUtils.ConsumptionApp
        public Intent buildViewCollectionIntent(PackageManager pm) {
            Intent intent = pm.getLaunchIntentForPackage("com.android.vending");
            intent.setAction("android.intent.action.MAIN");
            setDefaultFlags(intent);
            addAccountExtra(intent, "account");
            return intent;
        }

        private Intent buildItemIntent(PackageManager pm, Document doc, String action) {
            Intent intent = pm.getLaunchIntentForPackage("com.android.vending");
            intent.setAction(action);
            setDefaultFlags(intent);
            addAccountExtra(intent, "account");
            intent.putExtra("asset_package", doc.getAppDetails().getPackageName());
            return intent;
        }

        @Override // com.google.android.finsky.utils.IntentUtils.ConsumptionApp
        public Intent buildViewItemIntent(PackageManager pm, Document doc) {
            Intent intent = pm.getLaunchIntentForPackage(doc.getAppDetails().getPackageName());
            if (intent == null) {
                intent = buildItemIntent(pm, doc, "android.intent.action.RUN");
            }
            setDefaultFlags(intent);
            return intent;
        }

        @Override // com.google.android.finsky.utils.IntentUtils.ConsumptionApp
        public Intent buildManageItemIntent(PackageManager pm, Document doc) {
            return buildItemIntent(pm, doc, "android.intent.action.VIEW");
        }
    };
    private static final ConsumptionApp VIDEOS_APP = new ConsumptionApp(VideoUrlHandlerActivity.class) { // from class: com.google.android.finsky.utils.IntentUtils.4
        @Override // com.google.android.finsky.utils.IntentUtils.ConsumptionApp
        public Intent buildViewCollectionIntent(PackageManager pm) {
            Intent intent = pm.getLaunchIntentForPackage("com.google.android.videos");
            intent.setAction("android.intent.action.MAIN");
            setDefaultFlags(intent);
            intent.setFlags(67108864);
            addAccountExtra(intent, "authAccount");
            return intent;
        }

        @Override // com.google.android.finsky.utils.IntentUtils.ConsumptionApp
        public Intent buildManageItemIntent(PackageManager pm, Document doc) {
            Intent intent = buildViewCollectionIntent(pm);
            intent.putExtra("download_video_id", doc.getBackendDocId());
            return intent;
        }

        @Override // com.google.android.finsky.utils.IntentUtils.ConsumptionApp
        public Intent buildViewItemIntent(PackageManager pm, Document doc) {
            Uri uri = Uri.parse(doc.getYouTubeWatchUrl());
            Intent intent = new Intent("com.google.android.videos.intent.action.VIEW", uri);
            intent.setFlags(268451840);
            addAccountExtra(intent, "authAccount");
            return intent;
        }
    };
    private static final ConsumptionApp VIDEOS_WEB = new ConsumptionApp(VideoUrlHandlerActivity.class) { // from class: com.google.android.finsky.utils.IntentUtils.5
        @Override // com.google.android.finsky.utils.IntentUtils.ConsumptionApp
        public Intent buildViewCollectionIntent(PackageManager pm) {
            Uri uri = Uri.parse(G.myMoviesUrl.get());
            Intent intent = new Intent("android.intent.action.VIEW", uri);
            setDefaultFlags(intent);
            return intent;
        }

        @Override // com.google.android.finsky.utils.IntentUtils.ConsumptionApp
        public Intent buildViewItemIntent(PackageManager pm, Document doc) {
            Uri uri = Uri.parse(doc.getYouTubeWatchUrl());
            Intent intent = new Intent("android.intent.action.VIEW", uri);
            setDefaultFlags(intent);
            return intent;
        }
    };
    private static final ConsumptionApp MUSIC_APP = new GenericConsumptionApp(MusicUrlHandlerActivity.class, "com.google.android.music");

    private static abstract class ConsumptionApp {
        private Class<?> mUrlHandlerClass;

        public abstract Intent buildViewCollectionIntent(PackageManager packageManager);

        public abstract Intent buildViewItemIntent(PackageManager packageManager, Document document);

        ConsumptionApp(Class<?> urlHandlerClass) {
            this.mUrlHandlerClass = urlHandlerClass;
        }

        public Intent buildViewSampleIntent(PackageManager pm, Document doc) {
            return buildViewItemIntent(pm, doc);
        }

        public Intent buildManageItemIntent(PackageManager pm, Document doc) {
            return buildViewItemIntent(pm, doc);
        }

        public String getUrlHandlerClassName() {
            return this.mUrlHandlerClass.getCanonicalName();
        }

        protected static final void addAccountExtra(Intent intent, String key) {
            String accountName = AccountHandler.getCurrentAccount();
            if (!TextUtils.isEmpty(accountName)) {
                intent.putExtra(key, accountName);
            }
        }

        protected static final void setDefaultFlags(Intent intent) {
            intent.setFlags(268435456);
        }
    }

    private static class GenericConsumptionApp extends ConsumptionApp {
        private final String mPackage;

        GenericConsumptionApp(Class<?> urlHandlerClass, String pkg) {
            super(urlHandlerClass);
            this.mPackage = pkg;
        }

        @Override // com.google.android.finsky.utils.IntentUtils.ConsumptionApp
        public Intent buildViewCollectionIntent(PackageManager pm) {
            Intent intent = pm.getLaunchIntentForPackage(this.mPackage);
            intent.setAction("android.intent.action.MAIN");
            setDefaultFlags(intent);
            return intent;
        }

        @Override // com.google.android.finsky.utils.IntentUtils.ConsumptionApp
        public Intent buildViewItemIntent(PackageManager pm, Document doc) {
            Intent intent = pm.getLaunchIntentForPackage(this.mPackage);
            intent.setAction("android.intent.action.VIEW");
            setDefaultFlags(intent);
            intent.putExtra("id", doc.getBackendDocId());
            return intent;
        }
    }

    private static boolean isBooksAppInstalled(PackageManager pm) {
        Intent booksIntent = pm.getLaunchIntentForPackage("com.google.android.apps.books");
        if (booksIntent == null) {
            return false;
        }
        List<ResolveInfo> resolveInfo = pm.queryIntentActivities(booksIntent, 65536);
        return resolveInfo != null && resolveInfo.size() > 0;
    }

    private static boolean isVideosAppInstalled(PackageManager pm) {
        Intent videosIntent = pm.getLaunchIntentForPackage("com.google.android.videos");
        if (videosIntent == null) {
            return false;
        }
        List<ResolveInfo> resolveInfo = pm.queryIntentActivities(videosIntent, 65536);
        return resolveInfo != null && resolveInfo.size() > 0;
    }

    private static boolean isYouTubeAppInstalled(PackageManager pm) {
        Intent youTubeIntent = pm.getLaunchIntentForPackage("com.google.android.youtube");
        if (youTubeIntent == null) {
            return false;
        }
        List<ResolveInfo> resolveInfo = pm.queryIntentActivities(youTubeIntent, 65536);
        return resolveInfo != null && resolveInfo.size() > 0;
    }

    private static ConsumptionApp getConsumptionApp(PackageManager pm, int contentType) {
        switch (contentType) {
            case 1:
                if (isBooksAppInstalled(pm)) {
                    return BOOKS_APP;
                }
                return BOOKS_WEB;
            case 2:
                return MUSIC_APP;
            case 3:
                return MY_APPS;
            case 4:
                if (isVideosAppInstalled(pm)) {
                    return VIDEOS_APP;
                }
                return VIDEOS_WEB;
            default:
                throw new IllegalStateException("Unknown content type " + contentType);
        }
    }

    public static Intent buildConsumptionAppLaunchIntent(PackageManager packageManager, int contentType) {
        return getConsumptionApp(packageManager, contentType).buildViewCollectionIntent(packageManager);
    }

    public static Intent buildConsumptionAppViewItemIntent(PackageManager pm, Document doc) {
        return getConsumptionApp(pm, doc.getBackend()).buildViewItemIntent(pm, doc);
    }

    public static Intent buildConsumptionAppViewSampleIntent(PackageManager pm, Document doc) {
        return getConsumptionApp(pm, doc.getBackend()).buildViewSampleIntent(pm, doc);
    }

    public static Intent buildConsumptionAppManageItemIntent(PackageManager pm, Document doc) {
        return getConsumptionApp(pm, doc.getBackend()).buildManageItemIntent(pm, doc);
    }

    public static Intent createViewIntentForUrl(Uri uri) {
        Intent intent = new Intent("android.intent.action.VIEW", uri);
        intent.setFlags(524288);
        return intent;
    }

    public static Intent createYouTubeIntentForUrl(PackageManager pm, Uri uri) {
        Intent intent = new Intent("android.intent.action.VIEW", uri);
        if (isYouTubeAppInstalled(pm)) {
            intent.setPackage("com.google.android.youtube");
            intent.putExtra("authAccount", AccountHandler.getCurrentAccount());
            intent.putExtra("force_fullscreen", true);
            intent.putExtra("finish_on_ended", true);
        }
        intent.setFlags(524288);
        return intent;
    }

    public static Intent createSendIntentForUrl(Uri uri) {
        Intent intent = new Intent("android.intent.action.SENDTO", uri);
        intent.setFlags(524288);
        return intent;
    }

    public static Intent buildShareIntent(Context context, Document doc) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.setFlags(524288);
        intent.putExtra("android.intent.extra.TEXT", doc.getShareUrl());
        intent.putExtra("android.intent.extra.SUBJECT", context.getString(R.string.share_subject, doc.getTitle()));
        return intent;
    }

    public static void forwardIntentToMainActivity(Context context, Intent intent) {
        Intent intent2 = new Intent(context, (Class<?>) MainActivity.class);
        intent2.setData(intent.getData());
        intent2.putExtra("authAccount", intent.getStringExtra("authAccount"));
        intent2.setAction("android.intent.action.VIEW");
        context.startActivity(intent2);
    }

    public static void enableDisableUrlHandler(Context context, PackageManager pm, int channel, boolean enable) {
        ConsumptionApp app = getConsumptionApp(pm, channel);
        if (app != null) {
            String className = app.getUrlHandlerClassName();
            int status = enable ? 1 : 2;
            pm.setComponentEnabledSetting(new ComponentName(context, className), status, 1);
        }
    }

    public static void configureUrlInterceptors(Context context, PackageManager pm, ChannelList channels) {
        int[] tabs = {3, 1, 4, 2};
        for (int channel : tabs) {
            boolean enable = true;
            ChannelTab tab = channels.getTab(channel);
            if (tab == null) {
                enable = false;
            }
            enableDisableUrlHandler(context, pm, channel, enable);
        }
    }

    public static boolean isChannelEnabled(Context context, PackageManager pm, int channel) {
        String className;
        ConsumptionApp app = getConsumptionApp(pm, channel);
        if (app != null && (className = app.getUrlHandlerClassName()) != null && pm.getComponentEnabledSetting(new ComponentName(context, className)) == 1) {
            return true;
        }
        return false;
    }
}
