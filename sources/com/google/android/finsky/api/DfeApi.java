package com.google.android.finsky.api;

import android.net.Uri;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.google.android.finsky.api.PaginatedDfeRequest;
import com.google.android.finsky.remoting.protos.Browse;
import com.google.android.finsky.remoting.protos.Buy;
import com.google.android.finsky.remoting.protos.DetailsResponse;
import com.google.android.finsky.remoting.protos.DocList;
import com.google.android.finsky.remoting.protos.Rev;
import com.google.android.finsky.remoting.protos.SearchResponse;
import com.google.android.finsky.remoting.protos.Toc;
import com.google.android.finsky.utils.Maps;
import com.google.protobuf.micro.MessageMicro;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class DfeApi {
    private final DfeApiContext mApiContext;
    private final RequestQueue mQueue;
    public static final Uri BASE_URI = Uri.parse("https://android.clients.google.com/fdfe/");
    private static final Uri CHANNELS_URI = Uri.parse("toc");
    private static final Uri SEARCH_CHANNEL_URI = Uri.parse("search");
    private static final Uri PURCHASE_URI = Uri.parse("purchase");
    private static final Uri COMPLETE_PURCHASE_URI = Uri.parse("completePurchase");
    private static final Uri VIDEO_WATCH_URI = Uri.parse("http://www.youtube.com/watch");

    public DfeApi(RequestQueue queue, DfeApiContext apiContext) {
        this.mQueue = queue;
        this.mApiContext = apiContext;
    }

    public DfeApiContext getApiContext() {
        return this.mApiContext;
    }

    public Request<?> getChannels(Response.Listener<Toc.TocResponse> listener, Response.ErrorListener errorListener) {
        return this.mQueue.add(new DfeRequest(CHANNELS_URI.toString(), this.mApiContext, Toc.TocResponse.class, listener, errorListener));
    }

    public Request<?> getBrowseLayout(String str, Response.Listener<Browse.BrowseResponse> listener, Response.ErrorListener errorListener) {
        return this.mQueue.add(new DfeRequest(str, this.mApiContext, Browse.BrowseResponse.class, listener, errorListener));
    }

    public Request<?> getList(String str, int i, int i2, PaginatedDfeRequest.PaginatedListener<DocList.ListResponse> paginatedListener, Response.ErrorListener errorListener) {
        return this.mQueue.add(new PaginatedDfeRequest(str, i, i2, this.mApiContext, DocList.ListResponse.class, paginatedListener, errorListener));
    }

    public Request<?> search(String str, int i, int i2, PaginatedDfeRequest.PaginatedListener<SearchResponse> paginatedListener, Response.ErrorListener errorListener) {
        return this.mQueue.add(new PaginatedDfeRequest(str, i, i2, this.mApiContext, SearchResponse.class, paginatedListener, errorListener));
    }

    public Request<?> getReviews(String str, int i, int i2, PaginatedDfeRequest.PaginatedListener<Rev.ReviewResponse> paginatedListener, Response.ErrorListener errorListener) {
        return this.mQueue.add(new PaginatedDfeRequest(str, i, i2, this.mApiContext, Rev.ReviewResponse.class, paginatedListener, errorListener));
    }

    public Request<?> getDetails(String str, Response.Listener<DetailsResponse> listener, Response.ErrorListener errorListener) {
        return this.mQueue.add(new DfeRequest(str, this.mApiContext, DetailsResponse.class, listener, errorListener));
    }

    private static class DfePostRequest<T extends MessageMicro> extends DfeRequest<T> {
        private final Map<String, String> mPostParams;

        public DfePostRequest(String url, DfeApiContext apiContext, Class<T> responseClass, Response.Listener<T> listener, Response.ErrorListener errorListener) {
            super(url, apiContext, responseClass, listener, errorListener);
            this.mPostParams = Maps.newHashMap();
            setShouldCache(false);
            setDrainable(false);
        }

        public void addPostParam(String key, String value) {
            this.mPostParams.put(key, value);
        }

        @Override // com.android.volley.Request
        public Map<String, String> getPostParams() {
            return this.mPostParams;
        }
    }

    public Request<?> makePurchase(String str, int i, String str2, Response.Listener<Buy.BuyResponse> listener, Response.ErrorListener errorListener) {
        DfePostRequest dfePostRequest = new DfePostRequest(PURCHASE_URI.toString(), this.mApiContext, Buy.BuyResponse.class, listener, errorListener);
        dfePostRequest.addPostParam("doc", str);
        dfePostRequest.addPostParam("ot", Integer.toString(i));
        if (str2 != null) {
            dfePostRequest.addPostParam("ct", str2);
        }
        return this.mQueue.add(dfePostRequest);
    }

    private static Map<String, String> getQueryParameters(Uri uri) {
        Map<String, String> map = Maps.newHashMap();
        String encodedQuery = uri.getEncodedQuery();
        if (encodedQuery != null) {
            String[] params = encodedQuery.split("&");
            for (String param : params) {
                String[] pair = param.split("=");
                if (pair.length == 2) {
                    map.put(pair[0], pair[1]);
                }
            }
        }
        return map;
    }

    public Request<?> getPurchaseStatus(String str, Response.Listener<Buy.PurchaseStatusResponse> listener, Response.ErrorListener errorListener) {
        Uri uri = Uri.parse(str);
        DfePostRequest dfePostRequest = new DfePostRequest(uri.getLastPathSegment(), this.mApiContext, Buy.PurchaseStatusResponse.class, listener, errorListener);
        for (String str2 : getQueryParameters(uri).keySet()) {
            dfePostRequest.addPostParam(str2, uri.getQueryParameter(str2));
        }
        return this.mQueue.add(dfePostRequest);
    }

    public Request<?> completePurchase(String str, String str2, Response.Listener<Buy.BuyResponse> listener, Response.ErrorListener errorListener) {
        DfePostRequest dfePostRequest = new DfePostRequest(COMPLETE_PURCHASE_URI.toString(), this.mApiContext, Buy.BuyResponse.class, listener, errorListener);
        dfePostRequest.addPostParam("doc", str);
        dfePostRequest.addPostParam("cart", str2);
        return this.mQueue.add(dfePostRequest);
    }

    public static String formSearchUrl(String query, int channelId) {
        Uri.Builder builder = SEARCH_CHANNEL_URI.buildUpon().appendQueryParameter("c", Integer.toString(channelId)).appendQueryParameter("q", query);
        return builder.build().toString();
    }

    public static boolean isTopLevelUrl(String url) {
        return url.indexOf("?") == -1 || url.matches("\\?c=[0-9]*$");
    }

    public static String createDetailsUrlFromId(int corpusId, String itemId) {
        switch (corpusId) {
            case 1:
                return "details?doc=book-" + itemId;
            case 2:
            default:
                return null;
            case 3:
                return "details?doc=app-" + itemId;
            case 4:
                return "details?doc=movie-" + itemId;
        }
    }
}
