package com.google.android.finsky.api.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.TextUtils;
import android.widget.TextView;
import com.google.android.finsky.remoting.protos.BookInfo;
import com.google.android.finsky.remoting.protos.Common;
import com.google.android.finsky.remoting.protos.DeviceDoc;
import com.google.android.finsky.remoting.protos.Doc;
import com.google.android.finsky.utils.Lists;
import com.google.android.finsky.utils.Maps;
import com.google.android.finsky.utils.PackageInfoCache;
import com.google.android.finsky.utils.ParcelableProto;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class Document implements Parcelable {
    public static Parcelable.Creator<Document> CREATOR = new Parcelable.Creator<Document>() { // from class: com.google.android.finsky.api.model.Document.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Document createFromParcel(Parcel parcel) {
            return new Document((DeviceDoc.DeviceDocument) ParcelableProto.getProtoFromParcel(parcel, ParcelableProto.class.getClassLoader()), parcel.readString());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public Document[] newArray(int size) {
            return new Document[size];
        }
    };
    private final String mCookie;
    private final DeviceDoc.DeviceDocument mDeviceDoc;
    private final Doc.Document mFinskyDoc;
    private Map<Integer, List<Doc.Image>> mImageTypeMap;

    public Document(DeviceDoc.DeviceDocument deviceDoc, String cookie) {
        this.mDeviceDoc = deviceDoc;
        this.mFinskyDoc = deviceDoc.getFinskyDoc();
        this.mCookie = cookie;
    }

    public String getDocId() {
        return this.mDeviceDoc.getDocid();
    }

    public int getBackend() {
        return this.mFinskyDoc.getDocid().getBackend();
    }

    public String getBackendDocId() {
        return this.mFinskyDoc.getDocid().getBackendDocid();
    }

    public String getDetailsUrl() {
        return this.mDeviceDoc.getDetailsUrl();
    }

    public String getShareUrl() {
        return this.mDeviceDoc.getShareUrl();
    }

    public String getReviewsUrl() {
        return this.mDeviceDoc.getReviewsUrl();
    }

    public String getRelatedHeader() {
        return this.mDeviceDoc.getRelatedHeader();
    }

    public String getRelatedUrl() {
        return this.mDeviceDoc.getRelatedListUrl();
    }

    public String getRelatedBrowseUrl() {
        return this.mDeviceDoc.getRelatedBrowseUrl();
    }

    public boolean hasCreatorRelatedContent() {
        return !TextUtils.isEmpty(this.mDeviceDoc.getMoreByListUrl());
    }

    public String getMoreByHeader() {
        String header = this.mDeviceDoc.getMoreByHeader();
        return !TextUtils.isEmpty(header) ? header : getRelatedHeader();
    }

    public String getMoreByListUrl() {
        String moreByUrl = this.mDeviceDoc.getMoreByListUrl();
        return !TextUtils.isEmpty(moreByUrl) ? moreByUrl : getRelatedUrl();
    }

    public String getMoreByBrowseUrl() {
        String browseUrl = this.mDeviceDoc.getMoreByBrowseUrl();
        return !TextUtils.isEmpty(browseUrl) ? browseUrl : getRelatedBrowseUrl();
    }

    public String getCreator() {
        return this.mDeviceDoc.getCreator();
    }

    public String getTitle() {
        return this.mFinskyDoc.getTitle();
    }

    public CharSequence getDescription() {
        return Html.fromHtml(this.mDeviceDoc.getDescriptionHtml());
    }

    public CharSequence getWhatsNew() {
        String whatsNew = getAppDetails() != null ? getAppDetails().getRecentChangesHtml() : null;
        if (whatsNew != null) {
            return Html.fromHtml(whatsNew);
        }
        return "";
    }

    public boolean hasRating() {
        return this.mFinskyDoc.hasAggregateRating();
    }

    public float getStarRating() {
        return this.mFinskyDoc.getAggregateRating().getStarRating();
    }

    public long getRatingCount() {
        return this.mFinskyDoc.getAggregateRating().getRatingsCount();
    }

    public List<Doc.Image> getImages(int imageType) {
        return getImageTypeMap().get(Integer.valueOf(imageType));
    }

    public Map<Integer, List<Doc.Image>> getImageTypeMap() {
        if (this.mImageTypeMap == null) {
            List<Doc.Image> thumbnailImages = Lists.newArrayList();
            List<Doc.Image> previewImages = Lists.newArrayList();
            List<Doc.Image> promoImages = Lists.newArrayList();
            List<Doc.Image> videoImages = Lists.newArrayList();
            List<Doc.Image> hiResPreviewImages = Lists.newArrayList();
            this.mImageTypeMap = Maps.newHashMap();
            this.mImageTypeMap.put(0, thumbnailImages);
            this.mImageTypeMap.put(1, previewImages);
            this.mImageTypeMap.put(2, promoImages);
            this.mImageTypeMap.put(3, videoImages);
            this.mImageTypeMap.put(4, hiResPreviewImages);
            for (Doc.Image image : this.mFinskyDoc.getImageList()) {
                this.mImageTypeMap.get(Integer.valueOf(image.getImageType())).add(image);
            }
        }
        return this.mImageTypeMap;
    }

    public String getFirstImageUrl(int imageType) {
        List<Doc.Image> typedImages = getImages(imageType);
        if (typedImages.isEmpty()) {
            return null;
        }
        return typedImages.get(0).getImageUrl();
    }

    public String getBestThumbnailUrl() {
        String url = getFirstImageUrl(4);
        if (url == null) {
            return getFirstImageUrl(0);
        }
        return url;
    }

    public boolean hasDetails() {
        return this.mDeviceDoc.hasDetails();
    }

    public DeviceDoc.AppDetails getAppDetails() {
        if (this.mDeviceDoc.hasDetails()) {
            return this.mDeviceDoc.getDetails().getAppDetails();
        }
        return null;
    }

    public BookInfo.BookDetails getBookDetails() {
        if (this.mDeviceDoc.hasDetails()) {
            return this.mDeviceDoc.getDetails().getBookDetails();
        }
        return null;
    }

    public DeviceDoc.VideoDetails getVideoDetails() {
        if (this.mDeviceDoc.hasDetails()) {
            return this.mDeviceDoc.getDetails().getVideoDetails();
        }
        return null;
    }

    public int getNormalizedContentRating() {
        DeviceDoc.AppDetails appDetails = getAppDetails();
        if (appDetails == null) {
            return -1;
        }
        return appDetails.getContentRating() - 1;
    }

    public List<DeviceDoc.VideoRentalTerm> getMovieRentalTerms() {
        DeviceDoc.VideoDetails details = getVideoDetails();
        if (details == null) {
            return null;
        }
        return details.getRentalTermList();
    }

    public List<DeviceDoc.Trailer> getMovieTrailers() {
        DeviceDoc.VideoDetails details = getVideoDetails();
        if (details == null) {
            return null;
        }
        return details.getTrailerList();
    }

    public List<DeviceDoc.VideoCredit> getCreditsList() {
        DeviceDoc.VideoDetails videoDetails = getVideoDetails();
        if (videoDetails == null) {
            return null;
        }
        return videoDetails.getCreditList();
    }

    public String getYouTubeWatchUrl() {
        if (getBackend() == 4) {
            return this.mFinskyDoc.getUrl();
        }
        return null;
    }

    public List<String> getAppPermissionsList() {
        if ((this.mFinskyDoc.getDocid().getBackend() == 3 && this.mDeviceDoc.hasDetails()) || this.mDeviceDoc.getDetails().hasAppDetails()) {
            return this.mDeviceDoc.getDetails().getAppDetails().getPermissionList();
        }
        return null;
    }

    public List<Common.Offer> getAvailableOffers() {
        return this.mFinskyDoc.getOfferList();
    }

    public boolean ownedByUser(PackageInfoCache packageInfoCache) {
        if (isSystemApp(packageInfoCache)) {
            return true;
        }
        return offerTypeCheck(1) || (getBackend() == 4 && (offerTypeCheck(3) || offerTypeCheck(4)));
    }

    public boolean sampleOwnedByUser() {
        return offerTypeCheck(2);
    }

    private boolean offerTypeCheck(int offerType) {
        return this.mFinskyDoc.hasAvailability() && this.mFinskyDoc.getAvailability().hasOfferType() && this.mFinskyDoc.getAvailability().getOfferType() == offerType;
    }

    public boolean isLocallyAvailable(PackageInfoCache packageInfoCache) {
        return getBackend() != 3 ? ownedByUser(packageInfoCache) : isAppInstalled(packageInfoCache);
    }

    private boolean isAppInstalled(PackageInfoCache packageInfoCache) {
        DeviceDoc.AppDetails appDetails = getAppDetails();
        if (appDetails == null) {
            return false;
        }
        String packageName = appDetails.getPackageName();
        return packageInfoCache.isPackageInstalled(packageName);
    }

    private boolean isSystemApp(PackageInfoCache packageInfoCache) {
        DeviceDoc.AppDetails appDetails = getAppDetails();
        if (appDetails == null) {
            return false;
        }
        String packageName = appDetails.getPackageName();
        return packageInfoCache.isSystemPackage(packageName);
    }

    public boolean hasSample() {
        return this.mFinskyDoc.hasSampleDocid();
    }

    public boolean canManage(PackageInfoCache packageInfoCache) {
        if (getBackend() == 4) {
            return ownedByUser(packageInfoCache);
        }
        DeviceDoc.AppDetails appDetails = getAppDetails();
        if (appDetails != null && ownedByUser(packageInfoCache)) {
            int ourVersion = packageInfoCache.getPackageVersion(appDetails.getPackageName());
            int theirVersion = appDetails.getVersionCode();
            return ourVersion <= theirVersion;
        }
        return false;
    }

    public boolean canLaunch(PackageInfoCache packageInfoCache) {
        DeviceDoc.AppDetails appDetails = getAppDetails();
        if (appDetails == null) {
            return false;
        }
        return packageInfoCache.canLaunch(appDetails.getPackageName());
    }

    public boolean isUpdateAvailable(PackageInfoCache packageInfoCache) {
        DeviceDoc.AppDetails appDetails = getAppDetails();
        if (appDetails == null) {
            return false;
        }
        int ourVersion = packageInfoCache.getPackageVersion(appDetails.getPackageName());
        int theirVersion = appDetails.getVersionCode();
        return theirVersion > ourVersion;
    }

    public void setPrice(TextView priceView, TextView convertedPriceView) {
        priceView.setVisibility(0);
        if (this.mFinskyDoc.getPrice().hasFormattedAmount()) {
            String price = this.mFinskyDoc.getPrice().getFormattedAmount();
            String convertedPrice = null;
            if (price.contains("(")) {
                String[] parts = price.split("\\(|\\)");
                price = parts[0];
                convertedPrice = parts[1];
            }
            priceView.setText(price);
            if (convertedPrice != null) {
                convertedPriceView.setVisibility(0);
                convertedPriceView.setText(convertedPrice.toUpperCase());
                return;
            } else {
                convertedPriceView.setVisibility(4);
                return;
            }
        }
        priceView.setVisibility(4);
    }

    public String getFormattedPrice() {
        if (this.mFinskyDoc.getPrice().hasFormattedAmount()) {
            return this.mFinskyDoc.getPrice().getFormattedAmount();
        }
        return null;
    }

    public boolean needsCheckoutFlow() {
        return this.mFinskyDoc.getPrice().getCheckoutFlowRequired();
    }

    public boolean skipPurchaseDialog(int offerType) {
        return getBackend() == 1 && (offerType == 2 || !needsCheckoutFlow());
    }

    public boolean hasVideos() {
        List<Doc.Image> videoList = getImages(3);
        return (videoList == null || videoList.isEmpty() || TextUtils.isEmpty(videoList.get(0).getImageUrl())) ? false : true;
    }

    public boolean hasScreenshots() {
        List<Doc.Image> imageList = getImages(1);
        return (imageList == null || imageList.isEmpty() || 1 == getBackend()) ? false : true;
    }

    public String getCookie() {
        return this.mCookie;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(ParcelableProto.forProto(this.mDeviceDoc), 0);
        dest.writeString(this.mCookie);
    }

    public Common.Offer getDefaultOffer() {
        Common.Offer bestOffer = null;
        if (getBackend() == 4) {
            List<Common.Offer> offers = getAvailableOffers();
            Iterator<Common.Offer> it = offers.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Common.Offer o = it.next();
                int offerType = o.getOfferType();
                if (offerType == 4) {
                    bestOffer = o;
                    break;
                }
                if (offerType == 3 && bestOffer == null) {
                    bestOffer = o;
                }
            }
        } else if (getBackend() == 3 || getBackend() == 1) {
            Iterator<Common.Offer> it2 = this.mFinskyDoc.getOfferList().iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                }
                Common.Offer offer = it2.next();
                if (offer.hasOfferType()) {
                    if (offer.getOfferType() == 1) {
                        bestOffer = offer;
                        break;
                    }
                    bestOffer = offer;
                }
            }
        }
        return bestOffer == null ? this.mFinskyDoc.getPrice() : bestOffer;
    }
}
