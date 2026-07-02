package com.google.android.finsky.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.provider.SearchRecentSuggestions;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import com.android.volley.Response;
import com.google.android.finsky.FinskyApp;
import com.google.android.finsky.R;
import com.google.android.finsky.analytics.Analytics;
import com.google.android.finsky.api.DfeApi;
import com.google.android.finsky.api.model.DfeToc;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.config.G;
import com.google.android.finsky.layout.FinskyActionBar;
import com.google.android.finsky.model.ChannelList;
import com.google.android.finsky.model.PurchaseStatusTracker;
import com.google.android.finsky.navigationmanager.NavigationManager;
import com.google.android.finsky.remoting.protos.Buy;
import com.google.android.finsky.utils.BitmapLoader;
import com.google.android.finsky.utils.CorpusMetadata;
import com.google.android.finsky.utils.ErrorStrings;
import com.google.android.finsky.utils.FinskyDebug;
import com.google.android.finsky.utils.FinskyLog;
import com.google.android.finsky.utils.IntentUtils;
import com.google.android.finsky.utils.NotificationSender;
import com.google.android.finsky.utils.UrlIntentFilter;

/* JADX INFO: loaded from: classes.dex */
public class MainActivity extends AuthenticatedActivity implements Response.ErrorListener {
    private FinskyActionBar mActionBar;
    private Analytics mAnalytics;
    private BitmapLoader mBitmapLoader;
    private DfeApi mDfeApi;
    private NavigationManager mNavigationManager;
    private String mQuery;
    private Bundle mSavedInstanceState;
    private boolean mShouldShowSearchMenu = false;
    private boolean mShouldShowShareMenu = false;
    private boolean mShouldShowMyCollectionsMenu = false;
    private boolean mStateSaved = false;
    DialogInterface.OnCancelListener cancelListener = new DialogInterface.OnCancelListener() { // from class: com.google.android.finsky.activities.MainActivity.13
        @Override // android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface dialog) {
            MainActivity.this.finish();
        }
    };

    public FinskyActionBar getFinskyActionBar() {
        return this.mActionBar;
    }

    public boolean isStateSaved() {
        return this.mStateSaved;
    }

    @Override // com.google.android.finsky.activities.AuthenticatedActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        this.mSavedInstanceState = savedInstanceState;
        this.mDfeApi = FinskyApp.get().getDfeApi();
        this.mBitmapLoader = FinskyApp.get().getBitmapLoader();
        this.mAnalytics = FinskyApp.get().getAnalytics();
        setContentView(R.layout.main_activity);
        this.mNavigationManager = new NavigationManager(this, this.mDfeApi, this.mAnalytics);
        this.mActionBar = new FinskyActionBar(this, this.mNavigationManager, getActionBar(), this);
        super.onCreate(savedInstanceState);
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mStateSaved = true;
        this.mNavigationManager.serialize(outState);
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        this.mActionBar.onPause();
    }

    @Override // com.google.android.finsky.activities.AuthenticatedActivity
    protected void onCleanup() {
        FinskyApp.get().drainAllRequests();
        FinskyApp.get().clearCacheAsync(null);
        this.mNavigationManager.clear();
        this.mActionBar.clear();
        PurchaseStatusTracker tracker = FinskyApp.get().getPurchaseStatusTracker();
        tracker.clearPendingPurchases();
    }

    @Override // android.app.Activity
    protected void onResume() {
        super.onResume();
        this.mStateSaved = false;
        if (getTosJustAccepted()) {
            setTosJustAccepted(false);
            setupSessionForCorrectUser(true);
        }
    }

    @Override // android.app.Activity
    protected void onRestart() {
        super.onRestart();
        this.mNavigationManager.refreshPage();
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
        this.mStateSaved = false;
    }

    @Override // android.app.Activity
    protected void onStop() {
        super.onStop();
        FinskyApp.get().drainAllRequests();
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        this.mNavigationManager.terminate();
        super.onDestroy();
    }

    @Override // com.google.android.finsky.activities.AuthenticatedActivity, android.app.Activity
    protected void onNewIntent(Intent intent) {
        this.mStateSaved = false;
        super.onNewIntent(intent);
    }

    @Override // com.google.android.finsky.activities.AuthenticatedActivity
    protected void onReady(boolean shouldHandleIntent) {
        if ((this.mSavedInstanceState == null || !this.mNavigationManager.deserialize(this.mSavedInstanceState)) && shouldHandleIntent) {
            handleIntent();
        }
        this.mSavedInstanceState = null;
    }

    private void handleSearchIntent(Intent intent) {
        String query = intent.getStringExtra("query");
        SearchRecentSuggestions suggestions = new SearchRecentSuggestions(this, "com.google.android.finsky.RecentSuggestionProvider", 1);
        suggestions.saveRecentQuery(query, null);
        this.mNavigationManager.search(query, this.mActionBar.getLastSearchChannelId(), true);
        FinskyApp.get().getAnalytics().reportVirtualPageView(Analytics.Event.SEARCH, query);
    }

    private void handleViewIntent(Intent intent) {
        String url = intent.getDataString();
        UrlIntentFilter.Result result = UrlIntentFilter.matchUri(url);
        if (result == null) {
            this.mNavigationManager.goToChannelHome();
            return;
        }
        if (result.corpus != 0 && !IntentUtils.isChannelEnabled(this, getPackageManager(), result.corpus)) {
            Intent forwardIntent = new Intent("android.intent.action.VIEW", intent.getData());
            startActivity(forwardIntent);
            finish();
            return;
        }
        FinskyApp.get().getAnalytics().reportPageView(url);
        if (result.type == 2) {
            String detailsUrl = DfeApi.createDetailsUrlFromId(result.corpus, result.extra);
            this.mNavigationManager.showDetails(detailsUrl);
            return;
        }
        if (result.type == 3) {
            this.mQuery = result.extra;
            this.mNavigationManager.search(this.mQuery, result.corpus);
        } else if (result.type == 1) {
            this.mActionBar.setChannelToLoad(result.corpus);
            this.mNavigationManager.goToChannelHome();
        } else if (result.type == 4) {
            String detailsUrl2 = DfeApi.createDetailsUrlFromId(result.corpus, result.extra);
            this.mNavigationManager.buyItem(detailsUrl2, url, 1);
        } else {
            FinskyLog.wtf("Unhandled URL %s", url);
            finish();
        }
    }

    @Override // com.google.android.finsky.activities.AuthenticatedActivity
    public void handleAuthenticationError(Response.ErrorCode error, String message) {
        String errorMessage = ErrorStrings.get(this, error, message);
        showErrorDialog(errorMessage, true);
    }

    @Override // com.google.android.finsky.activities.AuthenticatedActivity
    public void onApisChanged() {
        this.mDfeApi = FinskyApp.get().getDfeApi();
        this.mNavigationManager.init(this, this.mDfeApi, this.mAnalytics);
        this.mActionBar.init(this.mNavigationManager, this.mDfeApi, this);
    }

    @Override // com.google.android.finsky.activities.AuthenticatedActivity
    protected void onTocLoaded(DfeToc response) {
        ChannelList channelList = new ChannelList(response);
        this.mActionBar.setChannels(channelList);
        configureUrlInterceptorsInBg(channelList);
        if (!this.mShouldShowSearchMenu) {
            showSearchMenu(true);
            showMyCollectionsMenu(true);
            invalidateOptionsMenu();
        }
    }

    private void configureUrlInterceptorsInBg(final ChannelList channelList) {
        Runnable r = new Runnable() { // from class: com.google.android.finsky.activities.MainActivity.1
            @Override // java.lang.Runnable
            public void run() {
                IntentUtils.configureUrlInterceptors(MainActivity.this, MainActivity.this.getPackageManager(), channelList);
            }
        };
        new Thread(r).run();
    }

    private void handleIntent() {
        this.mNavigationManager.clear();
        Intent currentIntent = getIntent();
        String intentAction = currentIntent.getAction();
        if ("android.intent.action.SEARCH".equals(intentAction)) {
            handleSearchIntent(currentIntent);
        } else if ("android.intent.action.VIEW".equals(intentAction)) {
            handleViewIntent(currentIntent);
        } else {
            this.mNavigationManager.goToChannelHome();
        }
    }

    private void setupConsumptionAppMenuItem(MenuItem item, final int contentType) {
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() { // from class: com.google.android.finsky.activities.MainActivity.2
            @Override // android.view.MenuItem.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem item2) {
                Intent intent = IntentUtils.buildConsumptionAppLaunchIntent(MainActivity.this.getPackageManager(), contentType);
                if (intent != null) {
                    MainActivity.this.startActivity(intent);
                    return true;
                }
                return true;
            }
        });
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        ViewGroup searchParent = (ViewGroup) menu.findItem(R.id.search_item).getActionView();
        searchParent.setVisibility(this.mShouldShowSearchMenu ? 0 : 8);
        MenuItem share = menu.findItem(R.id.share_item);
        share.setVisible(this.mShouldShowShareMenu);
        MenuItem myCollection = menu.findItem(R.id.my_collection);
        myCollection.setVisible(this.mShouldShowMyCollectionsMenu);
        int channelId = 3;
        if (this.mActionBar != null) {
            myCollection.setTitle(this.mActionBar.getMyCollectionsName());
            channelId = this.mActionBar.getCurrentCollectionType();
        }
        setupConsumptionAppMenuItem(myCollection, channelId);
        return true;
    }

    private int getChannelId() {
        if (this.mActionBar == null) {
            return 3;
        }
        int channelId = this.mActionBar.getCurrentCollectionType();
        return channelId;
    }

    private void setupDebugMenu(Menu menu) {
        MenuItem clearCache = menu.findItem(R.id.clear_cache_item);
        clearCache.setVisible(true);
        clearCache.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() { // from class: com.google.android.finsky.activities.MainActivity.3
            @Override // android.view.MenuItem.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem item) {
                FinskyApp.get().clearCacheAsync(new Runnable() { // from class: com.google.android.finsky.activities.MainActivity.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Process.killProcess(Process.myPid());
                    }
                });
                return true;
            }
        });
        SubMenu serverMenu = menu.addSubMenu(0, 0, 0, "Select server");
        for (String name : FinskyDebug.SERVER_INSTANCES.keySet()) {
            MenuItem mi = serverMenu.add(name);
            mi.setCheckable(true);
            final String url = FinskyDebug.SERVER_INSTANCES.get(name);
            if (FinskyDebug.isServerSelected(this, url)) {
                mi.setChecked(true);
            }
            mi.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() { // from class: com.google.android.finsky.activities.MainActivity.4
                @Override // android.view.MenuItem.OnMenuItemClickListener
                public boolean onMenuItemClick(MenuItem item) {
                    FinskyDebug.selectServer(MainActivity.this, url);
                    MainActivity.this.restart(null);
                    return true;
                }
            });
        }
    }

    @Override // android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem myCollection = menu.findItem(R.id.my_collection);
        myCollection.setVisible(this.mShouldShowMyCollectionsMenu);
        if (this.mActionBar != null) {
            myCollection.setTitle(this.mActionBar.getMyCollectionsName());
        }
        setupConsumptionAppMenuItem(myCollection, getChannelId());
        MenuItem clearHistory = menu.findItem(R.id.clear_search_history_item);
        clearHistory.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() { // from class: com.google.android.finsky.activities.MainActivity.5
            @Override // android.view.MenuItem.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem item) {
                SearchRecentSuggestions suggestions = new SearchRecentSuggestions(MainActivity.this, "com.google.android.finsky.RecentSuggestionProvider", 1);
                suggestions.clearHistory();
                return true;
            }
        });
        MenuItem switchAccounts = menu.findItem(R.id.accounts_item);
        switchAccounts.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() { // from class: com.google.android.finsky.activities.MainActivity.6
            @Override // android.view.MenuItem.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem item) {
                MainActivity.this.showDialog(0);
                return true;
            }
        });
        ViewGroup searchParent = (ViewGroup) menu.findItem(R.id.search_item).getActionView();
        ViewGroup searchHolder = (ViewGroup) searchParent.findViewById(R.id.search_view_holder);
        Context menuContext = searchHolder.getContext();
        ContextThemeWrapper menuDarkContext = new ContextThemeWrapper(menuContext, R.style.SearchTheme);
        LayoutInflater darkThemeInflater = LayoutInflater.from(menuDarkContext);
        darkThemeInflater.inflate(R.layout.expanded_search_view, searchHolder);
        SearchView searchView = (SearchView) searchHolder.findViewById(R.id.search_view);
        searchView.setSubmitButtonEnabled(true);
        this.mActionBar.setSearchViewWidget(searchView);
        searchView.clearFocus();
        SearchManager searchManager = (SearchManager) getSystemService("search");
        if (searchManager != null) {
            SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
            searchView.setSearchableInfo(info);
        }
        if (this.mQuery != null) {
            searchView.setQuery(this.mQuery, false);
            this.mQuery = null;
        }
        MenuItem shareMenuItem = menu.findItem(R.id.share_item);
        shareMenuItem.setVisible(this.mShouldShowShareMenu);
        shareMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() { // from class: com.google.android.finsky.activities.MainActivity.7
            @Override // android.view.MenuItem.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem item) {
                MainActivity.this.shareDocument();
                return true;
            }
        });
        menu.findItem(R.id.purchase_history).setOnMenuItemClickListener(new LaunchUrlMenuListener(Uri.parse(G.purchaseHistoryUrl.get())));
        MenuItem contentFilterItem = menu.findItem(R.id.content_filter);
        contentFilterItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() { // from class: com.google.android.finsky.activities.MainActivity.8
            @Override // android.view.MenuItem.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity.this, (Class<?>) ContentFilterActivity.class);
                MainActivity.this.startActivityForResult(intent, 30);
                return true;
            }
        });
        contentFilterItem.setVisible(G.contentRatingEnabled.get().booleanValue());
        menu.findItem(R.id.help).setOnMenuItemClickListener(new LaunchUrlMenuListener(CorpusMetadata.getHelpUrl(getChannelId())));
        menu.findItem(R.id.contact_us).setOnMenuItemClickListener(new LaunchUrlMenuListener(CorpusMetadata.getContactUsUrl(getChannelId())));
        if (G.debugOptionsEnabled.get().booleanValue()) {
            setupDebugMenu(menu);
            return true;
        }
        return true;
    }

    private class LaunchUrlMenuListener implements MenuItem.OnMenuItemClickListener {
        private final Uri mUri;

        public LaunchUrlMenuListener(Uri uri) {
            this.mUri = uri;
        }

        @Override // android.view.MenuItem.OnMenuItemClickListener
        public boolean onMenuItemClick(MenuItem item) {
            MainActivity.this.startActivity(IntentUtils.createViewIntentForUrl(this.mUri));
            MainActivity.this.mAnalytics.reportPageView(this.mUri.toString());
            return true;
        }
    }

    @Override // android.app.Activity
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != 16908332) {
            return true;
        }
        this.mNavigationManager.goUp();
        return true;
    }

    public void showSearchMenu(boolean show) {
        this.mShouldShowSearchMenu = show;
    }

    public void showShareMenu(boolean show) {
        this.mShouldShowShareMenu = show;
    }

    public void showMyCollectionsMenu(boolean show) {
        this.mShouldShowMyCollectionsMenu = show;
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        if (!this.mNavigationManager.goBack()) {
            super.onBackPressed();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void shareDocument() {
        Document doc = this.mNavigationManager.getCurrentDocument();
        if (doc != null) {
            Intent intent = IntentUtils.buildShareIntent(getApplicationContext(), doc);
            startActivity(Intent.createChooser(intent, getString(R.string.share_dialog_title, new Object[]{doc.getTitle()})));
        } else {
            FinskyLog.w("Tried to share an item but there is no document active", new Object[0]);
        }
    }

    public void startPurchase(Document document, int i) {
        if (document.skipPurchaseDialog(i)) {
            String docId = document.getDocId();
            changePurchaseState(docId, i, PurchaseStatusTracker.PurchaseState.PURCHASE_INITIATED);
            this.mDfeApi.makePurchase(docId, i, null, createFreeItemPurchaseListener(docId, i), createPurchaseErrorListener(docId, document.getTitle(), i, document.getDetailsUrl()));
        } else {
            Intent intent = new Intent(this, (Class<?>) PurchaseActivity.class);
            intent.putExtra("doc", document);
            intent.putExtra("offerType", i);
            startActivityForResult(intent, 10);
        }
    }

    @Override // com.google.android.finsky.activities.AuthenticatedActivity, android.app.Activity
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        this.mStateSaved = false;
        if (requestCode == 10) {
            if (intent != null) {
                if (resultCode == 0) {
                    String errorMessage = intent.getStringExtra("error");
                    if (errorMessage != null) {
                        String detailsUrl = ((Document) intent.getParcelableExtra("doc")).getDetailsUrl();
                        int offerType = intent.getIntExtra("offerType", 1);
                        Bundle b = new Bundle();
                        b.putString("error", errorMessage);
                        b.putString("detailsUrl", detailsUrl);
                        b.putInt("offerType", offerType);
                        showDialog(3, b);
                        return;
                    }
                    return;
                }
                if (resultCode == -1) {
                    Document doc = (Document) intent.getParcelableExtra("doc");
                    int offerType2 = intent.getIntExtra("offerType", 1);
                    boolean needsCheckoutFlow = doc.needsCheckoutFlow();
                    String docTitle = doc.getTitle();
                    String detailsUrl2 = doc.getDetailsUrl();
                    String docId = doc.getDocId();
                    changePurchaseState(docId, offerType2, PurchaseStatusTracker.PurchaseState.PURCHASE_INITIATED);
                    if (!needsCheckoutFlow || offerType2 == 2) {
                        this.mDfeApi.makePurchase(docId, offerType2, null, createFreeItemPurchaseListener(docId, offerType2), createPurchaseErrorListener(docId, docTitle, offerType2, detailsUrl2));
                        return;
                    }
                    String cart = intent.getStringExtra("cart");
                    Response.ErrorListener errorListener = createPurchaseErrorListener(docId, docTitle, offerType2, detailsUrl2);
                    this.mDfeApi.completePurchase(docId, cart, createPaidItemPurchaseListener(docId, docTitle, offerType2, detailsUrl2, errorListener), errorListener);
                    return;
                }
                throw new IllegalStateException("Invalid PurchaseActivity result");
            }
            return;
        }
        if (requestCode == 30 && resultCode == -1) {
            restart(null);
        } else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void changePurchaseState(String docId, int offerType, PurchaseStatusTracker.PurchaseState stateChange) {
        PurchaseStatusTracker tracker = FinskyApp.get().getPurchaseStatusTracker();
        tracker.switchState(docId, offerType, stateChange);
    }

    private Response.Listener<Buy.BuyResponse> createFreeItemPurchaseListener(final String docId, final int offerType) {
        return new Response.Listener<Buy.BuyResponse>() { // from class: com.google.android.finsky.activities.MainActivity.9
            @Override // com.android.volley.Response.Listener
            public void onResponse(Buy.BuyResponse response) {
                MainActivity.this.changePurchaseState(docId, offerType, PurchaseStatusTracker.PurchaseState.PURCHASE_COMPLETED);
            }
        };
    }

    private Response.Listener<Buy.BuyResponse> createPaidItemPurchaseListener(final String docId, final String docTitle, final int offerType, final String detailsUrl, final Response.ErrorListener errorListener) {
        return new Response.Listener<Buy.BuyResponse>() { // from class: com.google.android.finsky.activities.MainActivity.10
            @Override // com.android.volley.Response.Listener
            public void onResponse(Buy.BuyResponse response) {
                if (response.hasPurchaseStatusUrl()) {
                    String url = response.getPurchaseStatusUrl();
                    MainActivity.this.mDfeApi.getPurchaseStatus(url, MainActivity.this.createPurchaseStatusListener(docId, docTitle, offerType, detailsUrl), errorListener);
                    return;
                }
                throw new IllegalStateException("Unknown response - purchase could not be completed");
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Response.Listener<Buy.PurchaseStatusResponse> createPurchaseStatusListener(final String docId, final String docTitle, final int offerType, final String detailsUrl) {
        return new Response.Listener<Buy.PurchaseStatusResponse>() { // from class: com.google.android.finsky.activities.MainActivity.11
            @Override // com.android.volley.Response.Listener
            public void onResponse(Buy.PurchaseStatusResponse response) {
                int status = response.getStatus();
                if (status == 1) {
                    MainActivity.this.changePurchaseState(docId, offerType, PurchaseStatusTracker.PurchaseState.PURCHASE_COMPLETED);
                    return;
                }
                MainActivity.this.changePurchaseState(docId, offerType, PurchaseStatusTracker.PurchaseState.PURCHASE_COMPLETED_WITH_ERROR);
                String fullMessage = response.getStatusMsg();
                String briefMessage = response.getBriefMessage();
                String title = response.getStatusTitle();
                String infoUrl = response.getInfoUrl();
                if (FinskyLog.DEBUG) {
                    FinskyLog.v("Purchase Status response has error code %d, title %s,message %s and info URL %s", Integer.valueOf(status), title, fullMessage, infoUrl);
                }
                MainActivity.this.handlePurchaseError(title, docTitle, fullMessage, briefMessage, infoUrl, detailsUrl, offerType, false);
            }
        };
    }

    private Response.ErrorListener createPurchaseErrorListener(final String docId, final String docTitle, final int offerType, final String detailsUrl) {
        return new Response.ErrorListener() { // from class: com.google.android.finsky.activities.MainActivity.12
            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(Response.ErrorCode error, String message) {
                MainActivity.this.changePurchaseState(docId, offerType, PurchaseStatusTracker.PurchaseState.PURCHASE_COMPLETED_WITH_ERROR);
                String title = MainActivity.this.getString(R.string.purchase_error_notification_title);
                String errorMessage = ErrorStrings.get(MainActivity.this, error, message);
                MainActivity.this.handlePurchaseError(title, docTitle, errorMessage, errorMessage, null, detailsUrl, offerType, true);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handlePurchaseError(String errorTitle, String docTitle, String fullMessage, String briefMessage, String additionalInfoUrl, String detailsUrl, int offerType, boolean showRetryButton) {
        String currentPageUrl = this.mNavigationManager.getCurrentPageUrl();
        if (detailsUrl != null && currentPageUrl != null && currentPageUrl.contains(detailsUrl)) {
            Bundle b = new Bundle();
            b.putString("dialogTitle", errorTitle);
            b.putString("error", fullMessage);
            b.putString("detailsUrl", detailsUrl);
            b.putInt("offerType", offerType);
            b.putSerializable("showRetry", Boolean.valueOf(showRetryButton));
            showDialog(3, b);
            return;
        }
        NotificationSender.send(getApplicationContext(), errorTitle, docTitle, briefMessage, additionalInfoUrl, detailsUrl);
    }

    @Override // com.google.android.finsky.activities.AuthenticatedActivity, android.app.Activity
    protected Dialog onCreateDialog(int i, Bundle bundle) {
        boolean z;
        String str;
        String str2;
        String string;
        boolean z2;
        int i2 = 1;
        switch (i) {
            case 1:
                if (bundle == null) {
                    string = "";
                    z2 = false;
                } else {
                    string = bundle.getString("error");
                    z2 = bundle.getBoolean("go_back");
                }
                return setupNavigationError(i, string, z2);
            case 2:
            default:
                return super.onCreateDialog(i, bundle);
            case 3:
                String string2 = "";
                if (bundle == null) {
                    z = false;
                    str = "";
                    str2 = "";
                } else {
                    String string3 = bundle.getString("error");
                    String string4 = bundle.getString("detailsUrl");
                    string2 = bundle.getString("dialogTitle");
                    z = bundle.getBoolean("showRetry");
                    i2 = bundle.getInt("offerType", 1);
                    str = string4;
                    str2 = string3;
                }
                return setupPurchaseErrorDialog(i, string2, str2, str, i2, z);
        }
    }

    private AlertDialog setupNavigationError(final int i, String str, final boolean z) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(str).setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() { // from class: com.google.android.finsky.activities.MainActivity.14
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int id) {
                MainActivity.this.removeDialog(i);
                if (z && !MainActivity.this.mNavigationManager.goBack()) {
                    MainActivity.this.onBackPressed();
                }
            }
        });
        return builder.create();
    }

    private AlertDialog setupPurchaseErrorDialog(int i, String str, String str2, final String str3, final int i2, boolean z) {
        TextView textView = (TextView) ((LayoutInflater) getSystemService("layout_inflater")).inflate(R.layout.purchase_error_dialog, (ViewGroup) null);
        textView.setText(new SpannableStringBuilder(Html.fromHtml(str2)));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(str).setView(textView).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.google.android.finsky.activities.MainActivity.15
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialog) {
                MainActivity.this.removeDialog(3);
            }
        });
        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.google.android.finsky.activities.MainActivity.16
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.removeDialog(3);
            }
        };
        if (z) {
            builder.setPositiveButton(R.string.network_retry, new DialogInterface.OnClickListener() { // from class: com.google.android.finsky.activities.MainActivity.17
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.mNavigationManager.buyItem(str3, null, i2);
                    MainActivity.this.removeDialog(3);
                }
            }).setNegativeButton(R.string.purchase_cancel, onClickListener);
        } else {
            builder.setPositiveButton(R.string.done, onClickListener);
        }
        return builder.create();
    }

    public void showErrorDialog(String str, boolean z) {
        if (!TextUtils.isEmpty(str)) {
            Bundle bundle = new Bundle();
            bundle.putString("error", str);
            bundle.putBoolean("go_back", z);
            showDialog(1, bundle);
            return;
        }
        FinskyLog.e("Unknown error with empty error message. Stack trace below:", new Object[0]);
        new Throwable().printStackTrace();
    }

    @Override // com.android.volley.Response.ErrorListener
    public void onErrorResponse(Response.ErrorCode error, String message) {
        String errorMessage = ErrorStrings.get(this, error, message);
        showErrorDialog(errorMessage, false);
    }

    public NavigationManager getNavigationManager() {
        return this.mNavigationManager;
    }

    public DfeApi getDfeApi() {
        return this.mDfeApi;
    }

    public BitmapLoader getBitmapLoader() {
        return this.mBitmapLoader;
    }
}
