package com.google.android.finsky.activities;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import com.android.volley.AuthFailureException;
import com.android.volley.Response;
import com.android.volley.toolbox.AndroidAuthenticator;
import com.google.android.finsky.FinskyApp;
import com.google.android.finsky.R;
import com.google.android.finsky.adapters.PaymentListAdapter;
import com.google.android.finsky.analytics.Analytics;
import com.google.android.finsky.api.AccountHandler;
import com.google.android.finsky.api.DfeApi;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.config.G;
import com.google.android.finsky.layout.AppSecurityPermissions;
import com.google.android.finsky.layout.RentalTermsLayout;
import com.google.android.finsky.layout.ThumbnailListener;
import com.google.android.finsky.model.FormOfPayment;
import com.google.android.finsky.remoting.network.BrowserAuthRequest;
import com.google.android.finsky.remoting.protos.Buy;
import com.google.android.finsky.remoting.protos.DeviceDoc;
import com.google.android.finsky.utils.BitmapLoader;
import com.google.android.finsky.utils.ErrorStrings;
import com.google.android.finsky.utils.FinskyLog;
import com.google.android.finsky.utils.IntentUtils;
import com.google.android.finsky.utils.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class PurchaseActivity extends Activity implements Response.Listener<Buy.BuyResponse>, BrowserAuthRequest.Listener<Uri>, Response.ErrorListener {
    public static int MAX_WEB_VIEW_HEIGHT;
    private AnimatorSet mAnimatorSet;
    private int mBackend;
    private BitmapLoader mBitmapLoader;
    private ViewGroup mButtonGroup;
    private String mCheckoutServiceId;
    private String mCurrentAccountName;
    private DfeApi mDfeApi;
    private Document mDocument;
    private ProgressBar mFopLoadingIndicator;
    private Spinner mFormsOfPaymentSpinner;
    private TextView mFreeItemDetails;
    private int mOfferType;
    private ViewGroup mPaidItemDetails;
    private ViewGroup mPermissionsView;
    private TextView mPriceSpinner;
    private ViewGroup mPurchaseSummaryView;
    private ViewGroup mRootView;
    private String mSelectedCart;
    private String mSelectedInstrumentId;
    private WebView mWebView;
    private ViewGroup mWebViewHolder;
    private State mCurrentState = State.PURCHASE_SUMMARY;
    private boolean mShouldSendAuthToken = false;
    private boolean mHadValidFop = false;

    private enum State {
        PURCHASE_SUMMARY,
        PURCHASE_FOP_LOADING,
        WEBVIEW
    }

    @Override // android.app.Activity
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mCurrentAccountName = AccountHandler.getCurrentAccount();
        if (!AccountHandler.hasAccount(this.mCurrentAccountName, getApplicationContext())) {
            finishWithError();
        }
        this.mDfeApi = FinskyApp.get().getDfeApi();
        this.mBitmapLoader = FinskyApp.get().getBitmapLoader();
        Intent i = getIntent();
        this.mDocument = (Document) i.getParcelableExtra("doc");
        this.mBackend = this.mDocument.getBackend();
        this.mOfferType = i.getIntExtra("offerType", 1);
        requestWindowFeature(7);
        setContentView(R.layout.purchase_flow);
        getWindow().setFeatureInt(7, R.layout.purchase_title);
        this.mRootView = (ViewGroup) findViewById(R.id.purchase_dialog);
        this.mPurchaseSummaryView = (ViewGroup) findViewById(R.id.purchase_summary);
        this.mPermissionsView = (ViewGroup) findViewById(R.id.permissions_view);
        this.mWebViewHolder = (ViewGroup) findViewById(R.id.web_view_parent);
        this.mFopLoadingIndicator = (ProgressBar) findViewById(R.id.fop_progress_bar);
        this.mFormsOfPaymentSpinner = (Spinner) findViewById(R.id.fop_dropdown);
        this.mPriceSpinner = (TextView) findViewById(R.id.price_dropdown);
        this.mButtonGroup = (ViewGroup) findViewById(R.id.button_group);
        this.mFreeItemDetails = (TextView) findViewById(R.id.free_item_details);
        this.mPaidItemDetails = (ViewGroup) findViewById(R.id.paid_item_details);
        setupItemSummary();
        setupPurchaseDetailsView();
        setupTitle();
        if (!this.mDocument.needsCheckoutFlow() || this.mOfferType == 2) {
            startPurchaseFreeItem();
        } else {
            startPurchasePaidItem();
        }
    }

    private void startPurchaseFreeItem() {
        this.mFreeItemDetails.setText(this.mDocument.getFormattedPrice());
        this.mFreeItemDetails.setVisibility(0);
        setupActionButtons(new View.OnClickListener() { // from class: com.google.android.finsky.activities.PurchaseActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                PurchaseActivity.this.finishWithSuccess();
            }
        });
    }

    private void startPurchasePaidItem() {
        this.mDfeApi.getApiContext().invalidateAuthToken();
        if (this.mShouldSendAuthToken) {
            retrieveAuthTokenAndMakePurchase();
        } else {
            this.mDfeApi.makePurchase(this.mDocument.getDocId(), this.mOfferType, null, this, this);
            FinskyApp.get().getAnalytics().reportVirtualPageView(Analytics.Event.PURCHASE_STARTED, null);
        }
        setupActionButtons(new View.OnClickListener() { // from class: com.google.android.finsky.activities.PurchaseActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                FormOfPayment option = (FormOfPayment) PurchaseActivity.this.mFormsOfPaymentSpinner.getSelectedItem();
                PurchaseActivity.this.mSelectedCart = option.getCart();
                PurchaseActivity.this.finishWithSuccess();
            }
        });
        switchState(State.PURCHASE_FOP_LOADING);
    }

    private void retrieveAuthTokenAndMakePurchase() {
        AndroidAuthenticator googleCheckoutAuthenticator = new AndroidAuthenticator(getApplicationContext(), this.mDfeApi.getApiContext().getAccount(), this.mCheckoutServiceId);
        googleCheckoutAuthenticator.getAuthTokenAsync(new AndroidAuthenticator.AuthTokenListener() { // from class: com.google.android.finsky.activities.PurchaseActivity.3
            @Override // com.android.volley.toolbox.AndroidAuthenticator.AuthTokenListener
            public void onAuthTokenReceived(String authToken) {
                if (FinskyLog.DEBUG) {
                    FinskyLog.v("Sending auth token to make purchase", new Object[0]);
                }
                PurchaseActivity.this.mShouldSendAuthToken = false;
                PurchaseActivity.this.mDfeApi.makePurchase(PurchaseActivity.this.mDocument.getDocId(), PurchaseActivity.this.mOfferType, authToken, PurchaseActivity.this, PurchaseActivity.this);
                FinskyApp.get().getAnalytics().reportVirtualPageView(Analytics.Event.PURCHASE_STARTED, null);
            }

            @Override // com.android.volley.toolbox.AndroidAuthenticator.AuthTokenListener
            public void onErrorReceived(AuthFailureException exception) {
                String errorMessage = ErrorStrings.get(PurchaseActivity.this, Response.ErrorCode.AUTH, null);
                PurchaseActivity.this.finishWithError(errorMessage);
            }
        }, new Handler(Looper.getMainLooper()), true);
    }

    private void switchState(State newState) {
        if (this.mCurrentState != newState) {
            switch (newState) {
                case PURCHASE_FOP_LOADING:
                    if (this.mCurrentState == State.WEBVIEW) {
                        transitionToPurchaseLoadingState();
                    } else {
                        this.mPurchaseSummaryView.setVisibility(0);
                    }
                    this.mPaidItemDetails.setVisibility(0);
                    this.mFopLoadingIndicator.setVisibility(0);
                    this.mPriceSpinner.setVisibility(8);
                    this.mFormsOfPaymentSpinner.setVisibility(8);
                    this.mButtonGroup.setVisibility(4);
                    break;
                case PURCHASE_SUMMARY:
                    if (this.mCurrentState == State.WEBVIEW) {
                        transitionToPurchaseLoadingState();
                    }
                    this.mPaidItemDetails.setVisibility(0);
                    this.mPurchaseSummaryView.setVisibility(0);
                    this.mFopLoadingIndicator.setVisibility(8);
                    this.mPriceSpinner.setVisibility(0);
                    this.mFormsOfPaymentSpinner.setVisibility(0);
                    this.mButtonGroup.setVisibility(0);
                    this.mRootView.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
                    break;
                case WEBVIEW:
                    if (MAX_WEB_VIEW_HEIGHT == 0) {
                        int screenHeight = getResources().getDisplayMetrics().heightPixels;
                        MAX_WEB_VIEW_HEIGHT = (int) Math.round(0.75d * ((double) screenHeight));
                    }
                    transitionToWebViewState();
                    break;
                default:
                    throw new IllegalStateException("Invalid state in purchase dialog " + newState.toString());
            }
            this.mCurrentState = newState;
            setupTitle();
        }
    }

    private static class HeightAnimator implements ValueAnimator.AnimatorUpdateListener {
        View mView;

        private HeightAnimator(View view) {
            this.mView = view;
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public void onAnimationUpdate(ValueAnimator animator) {
            this.mView.getLayoutParams().height = ((Integer) animator.getAnimatedValue()).intValue();
            this.mView.requestLayout();
        }
    }

    private void transitionToWebViewState() {
        ValueAnimator heightAnimator = ValueAnimator.ofInt(this.mRootView.getHeight(), MAX_WEB_VIEW_HEIGHT);
        heightAnimator.addUpdateListener(new HeightAnimator(this.mRootView));
        heightAnimator.setDuration(250L);
        ObjectAnimator purchaseSummaryFadeAnimator = ObjectAnimator.ofFloat(this.mPurchaseSummaryView, "alpha", 1.0f, 0.0f).setDuration(250L);
        this.mAnimatorSet = new AnimatorSet();
        this.mAnimatorSet.playTogether(heightAnimator, purchaseSummaryFadeAnimator);
        this.mAnimatorSet.addListener(new Animator.AnimatorListener() { // from class: com.google.android.finsky.activities.PurchaseActivity.4
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                PurchaseActivity.this.mRootView.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
                PurchaseActivity.this.mPurchaseSummaryView.setVisibility(8);
                PurchaseActivity.this.mWebViewHolder.setVisibility(0);
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animation) {
                PurchaseActivity.this.mPurchaseSummaryView.setVisibility(8);
                PurchaseActivity.this.mWebViewHolder.setVisibility(0);
                PurchaseActivity.this.mRootView.getLayoutParams().height = PurchaseActivity.MAX_WEB_VIEW_HEIGHT;
                PurchaseActivity.this.mRootView.requestLayout();
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animation) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animation) {
            }
        });
        this.mAnimatorSet.start();
    }

    private void transitionToPurchaseLoadingState() {
        this.mPurchaseSummaryView.setVisibility(0);
        this.mPurchaseSummaryView.setAlpha(0.0f);
        this.mWebViewHolder.setVisibility(8);
        ValueAnimator heightAnimator = ValueAnimator.ofInt(this.mRootView.getHeight(), this.mPurchaseSummaryView.getHeight());
        heightAnimator.addUpdateListener(new HeightAnimator(this.mRootView));
        heightAnimator.setDuration(250L);
        ObjectAnimator purchaseSummaryFadeAnimator = ObjectAnimator.ofFloat(this.mPurchaseSummaryView, "alpha", 0.0f, 1.0f).setDuration(250L);
        this.mAnimatorSet = new AnimatorSet();
        this.mAnimatorSet.playTogether(heightAnimator, purchaseSummaryFadeAnimator);
        this.mAnimatorSet.addListener(new Animator.AnimatorListener() { // from class: com.google.android.finsky.activities.PurchaseActivity.5
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animation) {
                PurchaseActivity.this.mPurchaseSummaryView.setAlpha(1.0f);
                PurchaseActivity.this.mRootView.getLayoutParams().height = PurchaseActivity.this.mPurchaseSummaryView.getHeight();
                PurchaseActivity.this.mRootView.requestLayout();
                PurchaseActivity.this.resetWebView();
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animation) {
                PurchaseActivity.this.resetWebView();
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animation) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animation) {
            }
        });
        this.mAnimatorSet.start();
    }

    private void setupTitle() {
        int resourceId;
        ViewGroup titleView = (ViewGroup) getWindow().getDecorView();
        TextView title = (TextView) titleView.findViewById(R.id.title);
        TextView accountName = (TextView) titleView.findViewById(R.id.account);
        accountName.setText(this.mCurrentAccountName);
        if (this.mCurrentState == State.WEBVIEW) {
            title.setText(getResources().getString(R.string.purchase_signup_checkout_title));
            return;
        }
        boolean isFree = !this.mDocument.needsCheckoutFlow() || this.mOfferType == 2;
        switch (this.mBackend) {
            case 1:
                resourceId = !isFree ? R.string.purchase_title_paid_book : R.string.purchase_title_free_book;
                break;
            case 2:
            case 3:
            default:
                resourceId = !isFree ? R.string.purchase_title : R.string.purchase_free_title;
                break;
            case 4:
                resourceId = R.string.purchase_title_rent_movie;
                break;
        }
        title.setText(getResources().getString(resourceId));
    }

    private void setupItemSummary() {
        ((TextView) findViewById(R.id.item_title)).setText(this.mDocument.getTitle());
        ((TextView) findViewById(R.id.item_creator)).setText(this.mDocument.getCreator().toUpperCase());
        String thumbnailUrl = this.mDocument.getBestThumbnailUrl();
        if (thumbnailUrl != null) {
            ImageView thumbnailView = (ImageView) findViewById(R.id.item_thumbnail);
            this.mBitmapLoader.getOrBindImmediately(thumbnailUrl, thumbnailView, new ThumbnailListener(thumbnailView, true));
        }
    }

    private void setupPriceSummary(String total, List<Buy.LineItem> subItems) {
        this.mPriceSpinner.setText(total);
        if (subItems == null || subItems.size() == 0) {
            this.mPriceSpinner.setBackgroundDrawable(null);
            return;
        }
        final PopupWindow popupWindow = new PopupWindow(this, (AttributeSet) null, android.R.attr.listPopupWindowStyle);
        popupWindow.setWidth(-2);
        popupWindow.setHeight(-2);
        popupWindow.setFocusable(true);
        ViewGroup tablePopupContents = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.price_table, (ViewGroup) null);
        for (Buy.LineItem item : subItems) {
            TableRow lineItem = (TableRow) LayoutInflater.from(this).inflate(R.layout.price_info, (ViewGroup) null);
            ((TextView) lineItem.findViewById(R.id.label)).setText(item.getName());
            ((TextView) lineItem.findViewById(R.id.price)).setText(item.getPrice().getFormattedAmount());
            tablePopupContents.addView(lineItem);
        }
        popupWindow.setContentView(tablePopupContents);
        this.mPriceSpinner.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.finsky.activities.PurchaseActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                popupWindow.showAsDropDown(PurchaseActivity.this.mPriceSpinner);
            }
        });
    }

    private void setupFormsOfPaymentDropdown(final List<Buy.BuyResponse.CheckoutInfo.CheckoutOption> paymentOptions, final String addCreditCardUrl) {
        if (paymentOptions != null) {
            final ArrayList arrayListNewArrayList = Lists.newArrayList();
            String template = getString(R.string.pay_with_credit_card_number);
            for (Buy.BuyResponse.CheckoutInfo.CheckoutOption option : paymentOptions) {
                String optionName = String.format(template, option.getFormOfPayment());
                arrayListNewArrayList.add(new FormOfPayment(optionName, option.getInstrumentId(), option.hasAdjustedCart(), option.getAdjustedCart()));
            }
            FormOfPayment addCreditCard = new FormOfPayment(getString(R.string.add_credit_card), null, true, null);
            arrayListNewArrayList.add(addCreditCard);
            PaymentListAdapter spinnerAdapter = new PaymentListAdapter(this, R.layout.purchase_spinner_first_item);
            spinnerAdapter.setDropDownViewResource(R.layout.purchase_spinner_dropdown);
            spinnerAdapter.addAll(arrayListNewArrayList);
            this.mFormsOfPaymentSpinner.setAdapter((SpinnerAdapter) spinnerAdapter);
            this.mFormsOfPaymentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // from class: com.google.android.finsky.activities.PurchaseActivity.7
                @Override // android.widget.AdapterView.OnItemSelectedListener
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == paymentOptions.size()) {
                        PurchaseActivity.this.setupWebView(addCreditCardUrl);
                    } else if (((FormOfPayment) arrayListNewArrayList.get(position)).hasCart()) {
                        PurchaseActivity.this.findViewById(R.id.ok_button).setEnabled(true);
                    }
                }

                @Override // android.widget.AdapterView.OnItemSelectedListener
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            FormOfPayment firstFop = (FormOfPayment) this.mFormsOfPaymentSpinner.getSelectedItem();
            if (!firstFop.hasCart()) {
                findViewById(R.id.ok_button).setEnabled(false);
            }
            if (this.mSelectedInstrumentId != null) {
                int position = 0;
                int i = 0;
                while (true) {
                    if (i >= arrayListNewArrayList.size()) {
                        break;
                    }
                    if (!this.mSelectedInstrumentId.equals(((FormOfPayment) arrayListNewArrayList.get(i)).getInstrumentId())) {
                        i++;
                    } else {
                        position = i;
                        break;
                    }
                }
                this.mFormsOfPaymentSpinner.setSelection(position);
            }
            this.mHadValidFop = true;
        }
    }

    private void setupHelpDropdown() {
        View helpMenuButton = findViewById(R.id.help_dropdown);
        final PopupMenu helpPopup = new PopupMenu(this, helpMenuButton);
        helpPopup.getMenuInflater().inflate(R.menu.purchase_help_menu, helpPopup.getMenu());
        helpPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() { // from class: com.google.android.finsky.activities.PurchaseActivity.8
            @Override // android.widget.PopupMenu.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem item) {
                String uri = null;
                switch (item.getItemId()) {
                    case R.id.refund /* 2131296450 */:
                        uri = PurchaseActivity.this.getRefundPolicyUrl();
                        break;
                    case R.id.billing_privacy /* 2131296451 */:
                        String uri2 = G.billingPrivacyPolicyUrl.get();
                        uri = uri2;
                        break;
                }
                if (uri == null) {
                    return false;
                }
                Intent intent = IntentUtils.createViewIntentForUrl(Uri.parse(uri));
                PurchaseActivity.this.startActivity(intent);
                return true;
            }
        });
        helpMenuButton.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.finsky.activities.PurchaseActivity.9
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                helpPopup.show();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getRefundPolicyUrl() {
        switch (this.mBackend) {
            case 1:
                return G.booksRefundPolicyUrl.get();
            case 2:
            case 3:
            default:
                return G.appsRefundPolicyUrl.get();
            case 4:
                return G.moviesRefundPolicyUrl.get();
        }
    }

    private void setupPurchaseDetailsView() {
        switch (this.mDocument.getBackend()) {
            case 3:
                setupPermissionsView();
                break;
            case 4:
                setupMovieRentalTermsView();
                break;
        }
    }

    private void setupPermissionsView() {
        List<String> appPermissionsList = this.mDocument.getAppPermissionsList();
        if (appPermissionsList != null) {
            PackageManager packageManager = getPackageManager();
            ArrayList arrayListNewArrayList = Lists.newArrayList();
            Iterator<String> it = appPermissionsList.iterator();
            while (it.hasNext()) {
                try {
                    arrayListNewArrayList.add(packageManager.getPermissionInfo(it.next(), 0));
                } catch (PackageManager.NameNotFoundException e) {
                }
            }
            AppSecurityPermissions appSecurityPermissions = (AppSecurityPermissions) LayoutInflater.from(this).inflate(R.layout.permissions_layout, (ViewGroup) null);
            appSecurityPermissions.bindInfo(arrayListNewArrayList);
            this.mPermissionsView.addView(appSecurityPermissions);
            this.mPermissionsView.setBackgroundColor(getResources().getColor(R.color.permissions_background));
        }
    }

    private void setupMovieRentalTermsView() {
        DeviceDoc.VideoRentalTerm termsForOfferType = getTermsForOfferType(this.mOfferType);
        if (termsForOfferType != null) {
            String rentalHeader = termsForOfferType.getRentalHeader();
            List<DeviceDoc.VideoRentalTerm.Term> termList = termsForOfferType.getTermList();
            RentalTermsLayout rentalTermsLayout = (RentalTermsLayout) LayoutInflater.from(this).inflate(R.layout.rental_agreement_layout, (ViewGroup) null);
            rentalTermsLayout.bindInfo(rentalHeader, termList);
            this.mPermissionsView.addView(rentalTermsLayout);
            this.mPermissionsView.setBackgroundColor(getResources().getColor(R.color.permissions_background));
        }
    }

    private DeviceDoc.VideoRentalTerm getTermsForOfferType(int offerType) {
        List<DeviceDoc.VideoRentalTerm> videoRentalTermsList = this.mDocument.getMovieRentalTerms();
        for (DeviceDoc.VideoRentalTerm term : videoRentalTermsList) {
            if (offerType == term.getOfferType()) {
                return term;
            }
        }
        return null;
    }

    private void setupActionButtons(View.OnClickListener onClickListener) {
        ((Button) findViewById(R.id.ok_button)).setOnClickListener(onClickListener);
        ((Button) findViewById(R.id.cancel_button)).setOnClickListener(new View.OnClickListener() { // from class: com.google.android.finsky.activities.PurchaseActivity.10
            @Override // android.view.View.OnClickListener
            public void onClick(View v) {
                PurchaseActivity.this.finishWithError();
            }
        });
    }

    @Override // com.android.volley.Response.Listener
    public void onResponse(Buy.BuyResponse buyResponse) {
        this.mCheckoutServiceId = buyResponse.getCheckoutServiceId();
        if (buyResponse.hasContinueViaUrl()) {
            if (FinskyLog.DEBUG) {
                FinskyLog.v("Continue URL: %s", buyResponse.getContinueViaUrl());
            }
            setupWebView(buyResponse.getContinueViaUrl());
            return;
        }
        if (buyResponse.hasCheckoutInfo()) {
            Buy.BuyResponse.CheckoutInfo checkoutInfo = buyResponse.getCheckoutInfo();
            setupPriceSummary(checkoutInfo.getItem().getPrice().getFormattedAmount(), checkoutInfo.getSubItemList());
            setupFormsOfPaymentDropdown(checkoutInfo.getCheckoutOptionList(), checkoutInfo.getAddInstrumentUrl());
            setupHelpDropdown();
            switchState(State.PURCHASE_SUMMARY);
            FinskyApp.get().getAnalytics().reportVirtualPageView(Analytics.Event.PURCHASE_COMPLETE, null);
            return;
        }
        if (buyResponse.getCheckoutTokenRequired()) {
            if (FinskyLog.DEBUG) {
                FinskyLog.v("Checkout auth token required", new Object[0]);
            }
            this.mShouldSendAuthToken = true;
            startPurchasePaidItem();
            return;
        }
        FinskyLog.e("Unknown BuyResponse - purchase could not be completed", new Object[0]);
        finishWithError(ErrorStrings.get(this, Response.ErrorCode.SERVER, null));
    }

    @Override // com.android.volley.Response.ErrorListener
    public void onErrorResponse(Response.ErrorCode errorCode, String str) {
        if (FinskyLog.DEBUG) {
            FinskyLog.v("Error: %s", str);
        }
        finishWithError(str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setupWebView(String str) {
        if (str == null || str.equals("")) {
            finishWithError();
        }
        switchState(State.WEBVIEW);
        FinskyApp.get().getAnalytics().reportEvent(Analytics.Event.PURCHASE_WEBVIEW, str, null);
        this.mWebView = (WebView) findViewById(R.id.web_view);
        removeCookies();
        WebSettings settings = this.mWebView.getSettings();
        settings.setSavePassword(false);
        settings.setSaveFormData(false);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBlockNetworkImage(false);
        String str2 = G.webViewUserAgent.get();
        if (!TextUtils.isEmpty(str2)) {
            settings.setUserAgentString(str2);
        }
        this.mWebView.setScrollBarStyle(0);
        this.mWebView.setWebViewClient(new WebViewClient() { // from class: com.google.android.finsky.activities.PurchaseActivity.11
            @Override // android.webkit.WebViewClient
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (FinskyLog.DEBUG) {
                    FinskyLog.v("Override WebView URL: %s", url);
                }
                if (url.startsWith(DfeApi.BASE_URI.toString())) {
                    PurchaseActivity.this.handleDfeUrl(url);
                } else {
                    Intent i = IntentUtils.createViewIntentForUrl(Uri.parse(url));
                    PurchaseActivity.this.startActivity(i);
                }
                return true;
            }

            @Override // android.webkit.WebViewClient
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (FinskyLog.DEBUG) {
                    FinskyLog.v("Page Started WebView URL: %s", url);
                }
                if (url.startsWith(DfeApi.BASE_URI.toString())) {
                    PurchaseActivity.this.handleDfeUrl(url);
                }
            }

            @Override // android.webkit.WebViewClient
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                FinskyLog.e("Purchase failed with error %d and message %s", Integer.valueOf(errorCode), description);
                String errorMessage = ErrorStrings.get(PurchaseActivity.this, Response.ErrorCode.SERVER, null);
                PurchaseActivity.this.finishWithError(errorMessage);
            }
        });
        new BrowserAuthRequest(FinskyApp.get().getRequestQueue(), str, this.mCurrentAccountName, this.mCheckoutServiceId, this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleDfeUrl(String str) {
        this.mWebView.stopLoading();
        Uri uri = Uri.parse(str);
        String queryParameter = uri.getQueryParameter("status");
        String queryParameter2 = uri.getQueryParameter("instrumentId");
        FinskyLog.v("Status code result from Google Checkout flow: %s", queryParameter);
        if (queryParameter2 != null) {
            this.mSelectedInstrumentId = queryParameter2;
        }
        if (queryParameter == null || queryParameter.equals("SUCCESS") || queryParameter.equals("COMPLETED") || queryParameter.equals("REAUTH_REQUIRED")) {
            if (FinskyLog.DEBUG) {
                FinskyLog.v("Redirect and start purchase flow again", new Object[0]);
            }
            startPurchasePaidItem();
        } else {
            if (queryParameter.equals("ABORTED")) {
                if (FinskyLog.DEBUG) {
                    FinskyLog.v("Purchase process aborted from WebView - had valid fop " + this.mHadValidFop, new Object[0]);
                }
                if (this.mHadValidFop) {
                    switchState(State.PURCHASE_SUMMARY);
                    this.mFormsOfPaymentSpinner.setSelection(0);
                    return;
                } else {
                    finishWithError();
                    return;
                }
            }
            if (queryParameter.equals("MISSING_URL_PARAMETERS")) {
                throw new IllegalStateException("Google Checkout reports missing URL parameters");
            }
            if (FinskyLog.DEBUG) {
                FinskyLog.v("Unknown status from Google Checkout: %s", queryParameter);
            }
            finishWithError();
        }
    }

    @Override // com.google.android.finsky.remoting.network.BrowserAuthRequest.Listener
    public void onResponse(Uri uri, Response.ErrorCode error, String message) {
        if (uri != null) {
            this.mWebView.loadUrl(uri.toString());
        } else {
            finishWithError();
        }
    }

    @Override // com.google.android.finsky.remoting.network.BrowserAuthRequest.Listener
    public Activity getActivity() {
        return this;
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        finishWithError();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishWithSuccess() {
        destroyWebView();
        Intent intent = new Intent();
        intent.putExtra("doc", this.mDocument);
        intent.putExtra("offerType", this.mOfferType);
        intent.putExtra("cart", this.mSelectedCart);
        setResult(-1, intent);
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishWithError() {
        finishWithError(null);
    }

    private void sendError(String str) {
        destroyWebView();
        Intent intent = new Intent();
        intent.putExtra("doc", this.mDocument);
        intent.putExtra("error", str);
        intent.putExtra("offerType", this.mOfferType);
        setResult(0, intent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishWithError(String message) {
        sendError(message);
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void resetWebView() {
        if (this.mWebView != null) {
            this.mWebView.clearView();
            this.mWebView = null;
        }
    }

    private void destroyWebView() {
        if (this.mWebView != null) {
            this.mWebView.destroy();
            this.mWebView = null;
            removeCookies();
        }
    }

    private void removeCookies() {
        CookieManager.getInstance().removeAllCookie();
    }

    @Override // android.app.Activity
    protected void onPause() {
        super.onPause();
        if (this.mAnimatorSet != null && this.mAnimatorSet.isRunning()) {
            this.mAnimatorSet.cancel();
            this.mAnimatorSet = null;
        }
    }

    @Override // android.app.Activity
    public boolean onTouchEvent(MotionEvent event) {
        if (this.mCurrentState == State.WEBVIEW) {
            return false;
        }
        View decor = getWindow().getDecorView();
        int width = decor.getWidth();
        int height = decor.getHeight();
        float x = event.getX();
        float y = event.getY();
        if (x < 0.0f || x > width || y < 0.0f || y > height) {
            finishWithError();
            return true;
        }
        return super.onTouchEvent(event);
    }
}
