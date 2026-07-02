package com.google.android.finsky.navigationmanager;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;
import com.android.volley.Response;
import com.google.android.finsky.FinskyApp;
import com.google.android.finsky.R;
import com.google.android.finsky.activities.MainActivity;
import com.google.android.finsky.analytics.Analytics;
import com.google.android.finsky.api.DfeApi;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.fragments.BrowseFragment;
import com.google.android.finsky.fragments.DetailsFragment;
import com.google.android.finsky.fragments.PageFragment;
import com.google.android.finsky.fragments.SearchFragment;
import com.google.android.finsky.layout.FinskyActionBar;
import com.google.android.finsky.remoting.protos.DetailsResponse;
import com.google.android.finsky.utils.ErrorStrings;
import com.google.android.finsky.utils.FinskyLog;
import com.google.android.finsky.utils.IntentUtils;
import com.google.android.finsky.utils.MainThreadStack;
import com.google.android.finsky.utils.PurchaseButtonWrapper;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.List;
import java.util.Stack;

/* JADX INFO: loaded from: classes.dex */
public class NavigationManager implements PurchaseButtonWrapper.PurchaseButtonHandler {
    private MainActivity mActivity;
    private Analytics mAnalytics;
    private DfeApi mDfeApi;
    private FragmentManager mFragmentManager;
    private final Stack<StackTuple> mBackStack = new MainThreadStack();
    private final Response.ErrorListener mErrorListener = new Response.ErrorListener() { // from class: com.google.android.finsky.navigationmanager.NavigationManager.1
        @Override // com.android.volley.Response.ErrorListener
        public void onErrorResponse(Response.ErrorCode error, String message) {
            if (NavigationManager.this.mActivity != null && !NavigationManager.this.mActivity.isStateSaved()) {
                String errorMessage = ErrorStrings.get(NavigationManager.this.mActivity, error, message);
                NavigationManager.this.mActivity.showErrorDialog(errorMessage, false);
            }
        }
    };

    public enum NavigationState {
        INITIAL,
        HOME,
        BROWSE,
        DETAILS,
        SEARCH;

        public static NavigationState valueOf(int value) {
            if (value == HOME.ordinal()) {
                return HOME;
            }
            if (value == BROWSE.ordinal()) {
                return BROWSE;
            }
            if (value == DETAILS.ordinal()) {
                return DETAILS;
            }
            if (value == SEARCH.ordinal()) {
                return SEARCH;
            }
            if (value == INITIAL.ordinal()) {
                return INITIAL;
            }
            throw new IllegalStateException("Invalid navigation state");
        }
    }

    public static class StackTuple implements Parcelable {
        public static final Parcelable.Creator<StackTuple> CREATOR = new Parcelable.Creator<StackTuple>() { // from class: com.google.android.finsky.navigationmanager.NavigationManager.StackTuple.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public StackTuple createFromParcel(Parcel in) {
                return new StackTuple(in);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            public StackTuple[] newArray(int size) {
                return new StackTuple[size];
            }
        };
        public final NavigationState state;
        public final String url;

        public StackTuple(NavigationState state, String url) {
            this.state = state;
            this.url = url;
        }

        public StackTuple(Parcel in) {
            this.state = NavigationState.valueOf(in.readInt());
            this.url = in.readString();
        }

        public String toString() {
            return "StackTuple[" + this.state + ", " + this.url + "]";
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.state.ordinal());
            dest.writeString(this.url);
        }
    }

    public NavigationManager(MainActivity activity, DfeApi api, Analytics analytics) {
        init(activity, api, analytics);
    }

    public void init(MainActivity activity, DfeApi api, Analytics analytics) {
        this.mActivity = activity;
        this.mDfeApi = api;
        this.mAnalytics = analytics;
        this.mFragmentManager = this.mActivity.getFragmentManager();
    }

    public void terminate() {
        this.mActivity = null;
    }

    public void goHome(String url) {
        if (this.mActivity != null && !this.mActivity.isStateSaved()) {
            clear();
            showPage(NavigationState.HOME, BrowseFragment.newInstance(url), url, true);
        }
    }

    public void goBrowse(String url) {
        if (this.mActivity != null && !this.mActivity.isStateSaved()) {
            if (DfeApi.isTopLevelUrl(url)) {
                goToChannelHome();
            } else {
                showPage(NavigationState.BROWSE, BrowseFragment.newInstance(url), url);
            }
        }
    }

    public void showDetails(String url) {
        showDetails(url, null);
    }

    public void showDetails(String url, Document doc) {
        if (this.mActivity != null && !this.mActivity.isStateSaved()) {
            showPage(NavigationState.DETAILS, DetailsFragment.newInstance(url, doc), url);
            this.mAnalytics.reportEvent(Analytics.Event.VIEW_ITEM, url, doc != null ? doc.getCookie() : null);
        }
    }

    public void switchDetailsPageState(DetailsFragment.DetailsPageState newState) {
        if (this.mActivity != null && !this.mActivity.isStateSaved()) {
            this.mBackStack.peek();
            PageFragment<?> active = getActivePage();
            if (active != null && (active instanceof DetailsFragment)) {
                ((DetailsFragment) active).switchLayout(newState);
            }
        }
    }

    public void search(String query, int channel) {
        search(query, channel, false);
    }

    public void search(String query, int channel, boolean resetStack) {
        if (this.mActivity != null && !this.mActivity.isStateSaved()) {
            boolean replaceTop = false;
            if (resetStack) {
                goToChannelHome();
            } else {
                PageFragment<?> active = getActivePage();
                replaceTop = active != null && (active instanceof SearchFragment);
            }
            String searchUrl = DfeApi.formSearchUrl(query, channel);
            showPage(NavigationState.SEARCH, SearchFragment.newInstance(searchUrl), searchUrl, replaceTop);
            FinskyActionBar actionBar = this.mActivity.getFinskyActionBar();
            actionBar.triggerQuery(query, channel);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void searchMore(String partialUrl, String query, int backendId) {
        if (this.mActivity != null && !this.mActivity.isStateSaved()) {
            if (this.mBackStack.isEmpty() || this.mBackStack.peek().state != NavigationState.SEARCH) {
                throw new IllegalStateException();
            }
            PageFragment<?> active = getActivePage();
            boolean replaceTop = active != null && (active instanceof SearchFragment);
            FinskyActionBar actionBar = this.mActivity.getFinskyActionBar();
            actionBar.setSearchQuery(query);
            actionBar.selectSpinnerCorpusNoRefresh(backendId);
            showPage(NavigationState.SEARCH, SearchFragment.newInstance(partialUrl), partialUrl, replaceTop);
        }
    }

    public boolean goBack() {
        if (this.mActivity == null || this.mActivity.isStateSaved()) {
            return false;
        }
        try {
            this.mBackStack.pop();
            this.mFragmentManager.popBackStack();
            StackTuple newStackEntry = this.mBackStack.peek();
            this.mActivity.getFinskyActionBar().switchState(newStackEntry.state);
            return true;
        } catch (EmptyStackException e) {
            return false;
        }
    }

    public void goUp() {
        if (this.mActivity != null && !this.mActivity.isStateSaved() && !this.mBackStack.isEmpty()) {
            NavigationState currentState = this.mBackStack.peek().state;
            if (currentState == NavigationState.DETAILS) {
                popAllDetailsOff();
                return;
            }
            if (currentState == NavigationState.BROWSE) {
                if (!goBack()) {
                    goToChannelHome();
                }
            } else if (currentState == NavigationState.SEARCH) {
                goToChannelHome();
            }
        }
    }

    private void popAllDetailsOff() {
        if (this.mActivity != null && !this.mActivity.isStateSaved() && !this.mBackStack.isEmpty() && this.mBackStack.peek().state == NavigationState.DETAILS) {
            this.mBackStack.peek();
            while (!this.mBackStack.isEmpty() && this.mBackStack.peek().state == NavigationState.DETAILS) {
                this.mBackStack.pop();
                this.mFragmentManager.popBackStack();
            }
            if (this.mBackStack.isEmpty()) {
                goToChannelHome();
            } else {
                StackTuple resultPage = this.mBackStack.peek();
                this.mActivity.getFinskyActionBar().switchState(resultPage.state);
            }
        }
    }

    public void serialize(Bundle savedInstanceState) {
        if (this.mBackStack != null && !this.mBackStack.isEmpty()) {
            ArrayList<StackTuple> asList = new ArrayList<>(this.mBackStack);
            savedInstanceState.putParcelableArrayList("nm_state", asList);
            savedInstanceState.putInt("nm_selected_channel", this.mActivity.getFinskyActionBar().getChannelToLoad());
        }
    }

    public boolean deserialize(Bundle savedInstanceState) {
        List<StackTuple> contents = savedInstanceState.getParcelableArrayList("nm_state");
        int previouslySelectedChannel = savedInstanceState.getInt("nm_selected_channel");
        if (contents == null || contents.size() == 0) {
            return false;
        }
        for (StackTuple st : contents) {
            this.mBackStack.push(st);
        }
        FinskyActionBar bar = this.mActivity.getFinskyActionBar();
        bar.setChannelToLoad(previouslySelectedChannel);
        StackTuple top = this.mBackStack.peek();
        this.mActivity.getFinskyActionBar().switchState(top.state);
        return true;
    }

    public Document getCurrentDocument() {
        PageFragment<?> active = getActivePage();
        if (active == null || !(active instanceof DetailsFragment)) {
            return null;
        }
        return ((DetailsFragment) active).getDocument();
    }

    public void buyItem(Document doc, int offerType) {
        if (!doc.ownedByUser(FinskyApp.get().getPackageInfoCache())) {
            int contentType = doc.getBackend();
            switch (contentType) {
                case 1:
                case 2:
                case 3:
                case 4:
                    MainActivity mainActivity = this.mActivity;
                    mainActivity.startPurchase(doc, offerType);
                    break;
                default:
                    Toast.makeText(this.mActivity, this.mActivity.getString(R.string.purchase_failed), 0).show();
                    break;
            }
            this.mAnalytics.reportEvent(Analytics.Event.BUY_ITEM, doc.getDetailsUrl(), doc.getCookie());
        }
    }

    public void buyItem(final String url, final String cookie, final int offerType) {
        this.mDfeApi.getDetails(url, new Response.Listener<DetailsResponse>() { // from class: com.google.android.finsky.navigationmanager.NavigationManager.2
            @Override // com.android.volley.Response.Listener
            public void onResponse(DetailsResponse response) {
                Document doc = new Document(response.getDoc(), cookie);
                if (response.getDoc() == null) {
                    NavigationManager.this.mActivity.showErrorDialog(NavigationManager.this.mActivity.getString(R.string.details_page_error), true);
                } else {
                    NavigationManager.this.showDetails(url, doc);
                    NavigationManager.this.buyItem(doc, offerType);
                }
            }
        }, this.mErrorListener);
    }

    public void sampleItem(Document doc) {
        Intent intent = getSampleIntent(this.mActivity, doc);
        if (intent != null) {
            this.mActivity.startActivity(intent);
        } else {
            Toast.makeText(this.mActivity, this.mActivity.getString(R.string.server_error), 0).show();
        }
    }

    public void openItem(Document doc) {
        Intent intent = getConsumptionIntent(this.mActivity, doc);
        if (intent != null) {
            this.mActivity.startActivity(intent);
        } else {
            Toast.makeText(this.mActivity, this.mActivity.getString(R.string.launch_error), 0).show();
        }
        this.mAnalytics.reportEvent(Analytics.Event.VIEW_ITEM, doc.getDetailsUrl(), doc.getCookie());
    }

    public void manageItem(Document doc) {
        Intent intent = getManageIntent(this.mActivity, doc);
        if (intent != null) {
            this.mActivity.startActivity(intent);
        } else {
            Toast.makeText(this.mActivity, this.mActivity.getString(R.string.server_error), 0).show();
        }
        this.mAnalytics.reportEvent(Analytics.Event.MANAGE_ITEM, doc.getDetailsUrl(), doc.getCookie());
    }

    public static Intent getConsumptionIntent(Context context, Document doc) {
        int contentType = doc.getBackend();
        String id = doc.getBackendDocId();
        if (id == null) {
            return null;
        }
        switch (contentType) {
            case 1:
                return IntentUtils.buildConsumptionAppViewItemIntent(context.getPackageManager(), doc);
            case 2:
                return null;
            case 3:
                return IntentUtils.buildConsumptionAppViewItemIntent(context.getPackageManager(), doc);
            case 4:
                return IntentUtils.buildConsumptionAppViewItemIntent(context.getPackageManager(), doc);
            default:
                throw new IllegalStateException("Cannot open an item from the corpus " + contentType);
        }
    }

    public static Intent getManageIntent(Context context, Document doc) {
        int contentType = doc.getBackend();
        String id = doc.getBackendDocId();
        if (id == null) {
            return null;
        }
        switch (contentType) {
            case 3:
            case 4:
                return IntentUtils.buildConsumptionAppManageItemIntent(context.getPackageManager(), doc);
            default:
                throw new IllegalStateException("Cannot open an item from the corpus " + contentType);
        }
    }

    public static Intent getSampleIntent(Context context, Document doc) {
        int contentType = doc.getBackend();
        String id = doc.getBackendDocId();
        if (id == null) {
            return null;
        }
        switch (contentType) {
            case 1:
                return IntentUtils.buildConsumptionAppViewSampleIntent(context.getPackageManager(), doc);
            default:
                throw new IllegalStateException("Cannot open an item from the corpus " + contentType);
        }
    }

    public void goToChannelHome() {
        FinskyActionBar actionBar = this.mActivity.getFinskyActionBar();
        goHome(actionBar.getHomeUrlForActiveChannel());
    }

    public void refreshPage() {
        if (!this.mBackStack.isEmpty()) {
            this.mBackStack.peek();
            PageFragment<?> active = getActivePage();
            if (active != null) {
                active.refresh();
            } else {
                FinskyLog.e("Called refresh but there was no active page", new Object[0]);
            }
        }
    }

    private PageFragment<?> getActivePage() {
        return (PageFragment) this.mFragmentManager.findFragmentById(R.id.content_frame);
    }

    public String getCurrentPageUrl() {
        if (this.mActivity == null || this.mActivity.isStateSaved()) {
            return null;
        }
        if (this.mBackStack.isEmpty()) {
            return null;
        }
        return this.mBackStack.peek().url;
    }

    private void showPage(NavigationState state, Fragment fragment, String url) {
        showPage(state, fragment, url, false);
    }

    private void showPage(NavigationState state, Fragment fragment, String url, boolean replaceTop) {
        this.mActivity.getFinskyActionBar().switchState(state);
        FragmentTransaction ft = this.mFragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        if (replaceTop) {
            if (!this.mBackStack.isEmpty()) {
                this.mBackStack.pop();
            }
            this.mFragmentManager.popBackStack();
        }
        ft.addToBackStack("unused name");
        ft.setTransition(4097);
        this.mBackStack.push(new StackTuple(state, url));
        ft.commit();
    }

    public void clear() {
        while (!this.mBackStack.isEmpty()) {
            this.mBackStack.pop();
            this.mFragmentManager.popBackStack();
        }
    }

    public View.OnClickListener createSearchMoreClickListener(final String url, final String query, final int backendId) {
        return new View.OnClickListener() { // from class: com.google.android.finsky.navigationmanager.NavigationManager.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                NavigationManager.this.searchMore(url, query, backendId);
            }
        };
    }

    public View.OnClickListener getDetailsClickListener(final Document doc) {
        return new View.OnClickListener() { // from class: com.google.android.finsky.navigationmanager.NavigationManager.6
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                NavigationManager.this.showDetails(doc.getDetailsUrl(), null);
            }
        };
    }

    public View.OnClickListener getBuyImmediateClickListener(final Document doc, final int offerType) {
        return new View.OnClickListener() { // from class: com.google.android.finsky.navigationmanager.NavigationManager.7
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                NavigationManager.this.buyItem(doc, offerType);
            }
        };
    }

    public View.OnClickListener getOpenClickListener(final Document doc) {
        return new View.OnClickListener() { // from class: com.google.android.finsky.navigationmanager.NavigationManager.8
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                NavigationManager.this.openItem(doc);
            }
        };
    }

    public View.OnClickListener getManageClickListener(final Document doc) {
        return new View.OnClickListener() { // from class: com.google.android.finsky.navigationmanager.NavigationManager.9
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                NavigationManager.this.manageItem(doc);
            }
        };
    }

    @Override // com.google.android.finsky.utils.PurchaseButtonWrapper.PurchaseButtonHandler
    public void buy(Document document, int offerType) {
        buyItem(document.getDetailsUrl(), document.getCookie(), offerType);
    }

    @Override // com.google.android.finsky.utils.PurchaseButtonWrapper.PurchaseButtonHandler
    public void manage(Document document) {
        manageItem(document);
    }

    @Override // com.google.android.finsky.utils.PurchaseButtonWrapper.PurchaseButtonHandler
    public void open(Document document) {
        openItem(document);
    }
}
