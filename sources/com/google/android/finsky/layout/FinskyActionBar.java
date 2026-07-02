package com.google.android.finsky.layout;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.android.volley.Response;
import com.google.android.finsky.FinskyApp;
import com.google.android.finsky.R;
import com.google.android.finsky.activities.MainActivity;
import com.google.android.finsky.api.DfeApi;
import com.google.android.finsky.model.ChannelList;
import com.google.android.finsky.model.ChannelTab;
import com.google.android.finsky.navigationmanager.NavigationManager;
import com.google.android.finsky.remoting.protos.Browse;
import com.google.android.finsky.utils.FinskyLog;
import java.util.HashMap;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class FinskyActionBar implements ActionBar.TabListener {
    private final MainActivity mActivity;
    private ViewGroup mBreadCrumbsView;
    private ChannelList mChannelList;
    private View mCustomSearchView;
    private final ActionBar mFrameworkActionBar;
    private String mLastQuery;
    private NavigationManager mNavigationManager;
    private boolean mProcessingChannels;
    private Spinner mSearchSpinner;
    private SearchView mSearchViewWidget;
    private int mLastSearchChannelId = -1;
    private int mCurrentCollectionType = 3;
    private int mChannelToLoad = 3;
    private View mDetailsView = null;
    private View mBrowseBarView = null;
    private NavigationManager.NavigationState mCurrentState = NavigationManager.NavigationState.INITIAL;
    private final HashMap<Integer, CharSequence> mMyCollectionNames = new HashMap<>();

    @Override // android.app.ActionBar.TabListener
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction arg1) {
        selectTab(tab);
    }

    @Override // android.app.ActionBar.TabListener
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction arg1) {
        selectTab(tab);
    }

    @Override // android.app.ActionBar.TabListener
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction arg1) {
    }

    private void selectTab(ActionBar.Tab tab) {
        if (!this.mProcessingChannels) {
            this.mChannelToLoad = ((Integer) tab.getTag()).intValue();
            ChannelTab channel = this.mChannelList.getTab(this.mChannelToLoad);
            processTabSelection(channel, tab);
        }
    }

    public FinskyActionBar(MainActivity activity, NavigationManager navManager, ActionBar abar, Response.ErrorListener errorListener) {
        this.mActivity = activity;
        this.mNavigationManager = navManager;
        this.mFrameworkActionBar = abar;
        Resources resources = this.mActivity.getApplicationContext().getResources();
        this.mMyCollectionNames.put(3, resources.getText(R.string.menu_my_apps));
        this.mMyCollectionNames.put(1, resources.getText(R.string.menu_my_books));
        this.mMyCollectionNames.put(2, resources.getText(R.string.menu_my_music));
        this.mMyCollectionNames.put(4, resources.getText(R.string.menu_my_movies));
    }

    public void init(NavigationManager navManager, DfeApi api, Response.ErrorListener errorListener) {
        this.mNavigationManager = navManager;
        this.mCurrentState = NavigationManager.NavigationState.INITIAL;
        setCurrentCollectionType(3);
        this.mChannelToLoad = 3;
    }

    public void clear() {
        this.mActivity.showSearchMenu(false);
        this.mActivity.showShareMenu(false);
        this.mActivity.showMyCollectionsMenu(false);
        this.mFrameworkActionBar.removeAllTabs();
        this.mFrameworkActionBar.setNavigationMode(2);
        this.mCustomSearchView = null;
        this.mDetailsView = null;
        this.mBrowseBarView = null;
        this.mSearchSpinner = null;
        this.mBreadCrumbsView = null;
        this.mNavigationManager = null;
        this.mChannelList = null;
        this.mLastQuery = "";
        this.mLastSearchChannelId = -1;
    }

    public void setChannels(ChannelList channels) {
        this.mChannelList = channels;
    }

    public ChannelList getChannels() {
        return this.mChannelList;
    }

    public void setCurrentCollectionType(int channelId) {
        this.mCurrentCollectionType = channelId;
        setCorpusSpecificHomeIcon(this.mCurrentCollectionType);
    }

    public int getCurrentCollectionType() {
        return this.mCurrentCollectionType;
    }

    public CharSequence getMyCollectionsName() {
        return this.mMyCollectionNames.get(Integer.valueOf(this.mCurrentCollectionType));
    }

    public void switchState(NavigationManager.NavigationState newState) {
        if (this.mChannelList != null && newState != this.mCurrentState) {
            switch (this.mCurrentState) {
                case HOME:
                    clearHomeBar();
                    break;
                case BROWSE:
                    clearBrowseBar();
                    break;
                case SEARCH:
                    clearSearchBar();
                    break;
                case DETAILS:
                    clearDetailsBar();
                    break;
            }
            this.mCurrentState = newState;
            switch (this.mCurrentState) {
                case HOME:
                    clearLastSearch();
                    switchToBrowseBar();
                    switchToHomeBar();
                    break;
                case BROWSE:
                    switchToBrowseBar();
                    break;
                case SEARCH:
                    switchToSearchBar();
                    break;
                case DETAILS:
                    switchToDetailsBar();
                    break;
            }
        }
    }

    private void switchToHomeBar() {
        this.mProcessingChannels = true;
        displayChannels();
        this.mFrameworkActionBar.setNavigationMode(2);
        this.mFrameworkActionBar.setDisplayHomeAsUpEnabled(false);
        this.mProcessingChannels = false;
    }

    private void displayChannels() {
        int selectedTabId = this.mChannelToLoad == 3 ? this.mChannelList.getSelectedTabId() : this.mChannelToLoad;
        for (ChannelTab channel : this.mChannelList.getTabs()) {
            if (channel.getActionBarTab() == null) {
                ActionBar.Tab newTab = this.mFrameworkActionBar.newTab();
                newTab.setText(channel.getName());
                newTab.setTabListener(this);
                newTab.setTag(Integer.valueOf(channel.getId()));
                channel.setActionBarTab(newTab);
            }
            boolean shouldSelectTab = false;
            if (channel.getId() == selectedTabId) {
                shouldSelectTab = true;
            }
            this.mFrameworkActionBar.addTab(channel.getActionBarTab(), shouldSelectTab);
        }
    }

    private void processTabSelection(ChannelTab channelTab, ActionBar.Tab actionBarTab) {
        if (channelTab.getDataUrl() == null) {
            throw new IllegalStateException("No url assigned to the channel. I can't retrieve the page");
        }
        FinskyApp.get().drainAllRequests();
        setCurrentCollectionType(channelTab.getId());
        this.mActivity.invalidateOptionsMenu();
        this.mChannelList.selectTabWithId(this.mChannelToLoad);
        this.mNavigationManager.goHome(channelTab.getDataUrl());
    }

    private void switchToBrowseBar() {
        if (this.mBrowseBarView == null) {
            this.mBrowseBarView = View.inflate(this.mActivity, R.layout.action_bar_browse, null);
            this.mBreadCrumbsView = (ViewGroup) this.mBrowseBarView.findViewById(R.id.bread_crumbs);
            BreadCrumbHolder holder = new BreadCrumbHolder();
            holder.parent = (TextView) this.mBreadCrumbsView.findViewById(R.id.parent);
            holder.child = (TextView) this.mBreadCrumbsView.findViewById(R.id.child);
            holder.divider = this.mBreadCrumbsView.findViewById(R.id.divider);
            this.mBreadCrumbsView.setTag(holder);
        }
        this.mFrameworkActionBar.setNavigationMode(0);
        this.mFrameworkActionBar.setCustomView(this.mBrowseBarView);
        this.mFrameworkActionBar.setDisplayHomeAsUpEnabled(true);
    }

    private final class BreadCrumbHolder {
        TextView child;
        View divider;
        TextView parent;

        private BreadCrumbHolder() {
        }
    }

    public void setupBreadcrumbs(List<Browse.BrowseLink> breadcrumbs) {
        if (breadcrumbs != null) {
            if (this.mBreadCrumbsView == null) {
                FinskyLog.e("Unable to set breadcrumbs when not in browse mode.", new Object[0]);
                return;
            }
            if (breadcrumbs.isEmpty()) {
                this.mBreadCrumbsView.setVisibility(8);
                return;
            }
            BreadCrumbHolder holder = (BreadCrumbHolder) this.mBreadCrumbsView.getTag();
            holder.parent.setText(breadcrumbs.get(0).getName());
            holder.parent.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.finsky.layout.FinskyActionBar.1
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    FinskyActionBar.this.mNavigationManager.goUp();
                }
            });
            String childName = null;
            if (breadcrumbs.size() > 1) {
                int childIndex = breadcrumbs.size() - 1;
                childName = breadcrumbs.get(childIndex).getName();
            }
            if (TextUtils.isEmpty(childName)) {
                holder.child.setVisibility(8);
                holder.divider.setVisibility(8);
            } else {
                holder.child.setText(childName);
                holder.child.setVisibility(0);
                holder.divider.setVisibility(0);
            }
            this.mBreadCrumbsView.setVisibility(0);
        }
    }

    private void switchToDetailsBar() {
        if (this.mDetailsView == null) {
            this.mDetailsView = View.inflate(this.mActivity, R.layout.action_bar_details, null);
        }
        this.mFrameworkActionBar.setCustomView(this.mDetailsView);
        this.mActivity.showShareMenu(true);
        this.mActivity.invalidateOptionsMenu();
        this.mFrameworkActionBar.setDisplayHomeAsUpEnabled(true);
    }

    public int getChannelToLoad() {
        return this.mChannelToLoad;
    }

    public void setChannelToLoad(int channelId) {
        this.mChannelToLoad = channelId;
    }

    public String getHomeUrlForActiveChannel() {
        if (this.mChannelList == null) {
            return null;
        }
        int selectedTabId = this.mChannelToLoad == 3 ? this.mChannelList.getSelectedTabId() : this.mChannelToLoad;
        return getChannels().getTab(selectedTabId).getDataUrl();
    }

    private void setLastQuery(String query, int channelId) {
        this.mLastQuery = query;
        this.mLastSearchChannelId = channelId;
    }

    public int getLastSearchChannelId() {
        if (this.mLastSearchChannelId < 0) {
            this.mLastSearchChannelId = this.mChannelToLoad == 3 ? this.mChannelList.getSelectedTabId() : this.mChannelToLoad;
        }
        return this.mLastSearchChannelId;
    }

    public void setSearchViewWidget(SearchView sv) {
        this.mSearchViewWidget = sv;
    }

    public boolean triggerQuery(String query, int channelId) {
        setLastQuery(query, channelId);
        setSearchQuery(query);
        if (this.mCustomSearchView == null) {
            return false;
        }
        this.mSearchViewWidget.clearFocus();
        selectSpinnerCorpusNoRefresh(this.mLastSearchChannelId);
        return true;
    }

    public void selectSpinnerCorpusNoRefresh(int channelId) {
        AdapterView.OnItemSelectedListener listener = this.mSearchSpinner.getOnItemSelectedListener();
        this.mSearchSpinner.setOnItemSelectedListener(null);
        selectSpinnerCorpus(channelId);
        this.mSearchSpinner.setOnItemSelectedListener(listener);
    }

    private void selectSpinnerCorpus(int channelId) {
        int positionInSpinner = this.mChannelList.getIndexForBackendId(channelId) + 1;
        this.mSearchSpinner.setSelection(positionInSpinner, false);
    }

    private void switchToSearchBar() {
        this.mActivity.showMyCollectionsMenu(false);
        this.mActivity.invalidateOptionsMenu();
        if (this.mCustomSearchView != null) {
            this.mFrameworkActionBar.setCustomView(this.mCustomSearchView);
            if (this.mSearchViewWidget != null && this.mLastQuery != null) {
                CursorAdapter adapter = this.mSearchViewWidget.getSuggestionsAdapter();
                this.mSearchViewWidget.setSuggestionsAdapter(null);
                this.mSearchViewWidget.setQuery(this.mLastQuery, false);
                this.mSearchViewWidget.setSuggestionsAdapter(adapter);
            }
            initSearchView();
            return;
        }
        initSearchView();
        this.mFrameworkActionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initSearchView() {
        final ArrayAdapter<ChannelTab> spinnerAdapter = new ArrayAdapter<>(this.mActivity, R.layout.search_spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.search_spinner_dropdown_item);
        String allString = this.mActivity.getResources().getString(R.string.search_all);
        ChannelTab multi = new ChannelTab(allString, 0, null, null, null);
        spinnerAdapter.add(multi);
        spinnerAdapter.addAll(this.mChannelList.getTabs());
        if (this.mCustomSearchView == null) {
            this.mCustomSearchView = View.inflate(this.mActivity, R.layout.action_bar_search, null);
        }
        this.mSearchSpinner = (Spinner) this.mCustomSearchView.findViewById(R.id.search_dropdown);
        this.mSearchSpinner.setAdapter((SpinnerAdapter) spinnerAdapter);
        selectSpinnerCorpus(this.mLastSearchChannelId);
        this.mSearchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.google.android.finsky.layout.FinskyActionBar.2
            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onNothingSelected(AdapterView<?> parent) {
            }

            @Override // android.widget.AdapterView.OnItemSelectedListener
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ChannelTab tab = (ChannelTab) spinnerAdapter.getItem(position);
                FinskyActionBar.this.mLastSearchChannelId = tab.getId();
                FinskyActionBar.this.mNavigationManager.search(FinskyActionBar.this.mLastQuery, FinskyActionBar.this.mLastSearchChannelId);
            }
        });
        this.mFrameworkActionBar.setCustomView(this.mCustomSearchView);
    }

    private void clearHomeBar() {
        this.mFrameworkActionBar.removeAllTabs();
    }

    private void clearSearchBar() {
        this.mFrameworkActionBar.setCustomView((View) null);
        this.mSearchViewWidget.setQuery(null, false);
        this.mSearchViewWidget.clearFocus();
        this.mActivity.showMyCollectionsMenu(true);
        this.mActivity.invalidateOptionsMenu();
        if (this.mSearchSpinner != null) {
            this.mSearchSpinner.setOnItemSelectedListener(null);
        }
    }

    private void clearLastSearch() {
        this.mLastSearchChannelId = -1;
    }

    public void setSearchQuery(String query) {
        this.mSearchViewWidget.setQuery(query, false);
    }

    private void clearBrowseBar() {
        this.mBreadCrumbsView.setVisibility(8);
    }

    private void clearDetailsBar() {
        this.mActivity.showShareMenu(false);
        this.mActivity.invalidateOptionsMenu();
    }

    public void onPause() {
        if (this.mSearchViewWidget != null) {
            this.mSearchViewWidget.clearFocus();
        }
    }

    private void setCorpusSpecificHomeIcon(int corpus) {
        int iconRes;
        View homeIcon = this.mActivity.getWindow().findViewById(android.R.id.home);
        if (homeIcon != null && (homeIcon instanceof ImageView)) {
            switch (corpus) {
                case 1:
                    iconRes = R.drawable.ic_apps_fi_books;
                    break;
                case 2:
                    iconRes = R.drawable.ic_apps_fi_music;
                    break;
                case 3:
                    iconRes = R.drawable.ic_apps_fi_apps;
                    break;
                case 4:
                    iconRes = R.drawable.ic_apps_fi_videos;
                    break;
                default:
                    iconRes = R.drawable.ic_apps_fi_shop;
                    break;
            }
            ((ImageView) homeIcon).setImageResource(iconRes);
        }
    }
}
