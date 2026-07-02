package com.google.android.finsky.providers;

import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import com.android.volley.Response;
import com.google.android.finsky.FinskyApp;
import com.google.android.finsky.analytics.Analytics;
import com.google.android.finsky.api.DfeApi;
import com.google.android.finsky.api.PaginatedDfeRequest;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.remoting.protos.DeviceDoc;
import com.google.android.finsky.remoting.protos.DocList;
import com.google.android.finsky.remoting.protos.SearchResponse;
import com.google.android.finsky.utils.CorpusMetadata;
import com.google.android.finsky.utils.Lists;
import java.util.List;
import java.util.concurrent.Semaphore;

/* JADX INFO: loaded from: classes.dex */
public class SuggestionProvider extends SearchRecentSuggestionsProvider {
    protected static final String[] COLUMNS = {"_id", "suggest_icon_1", "suggest_text_1", "suggest_text_2", "suggest_intent_data", "suggest_intent_extra_data"};
    private String mQuery;

    public SuggestionProvider() {
        setupSuggestions("com.google.android.finsky.SuggestionProvider", 1);
    }

    @Override // android.content.SearchRecentSuggestionsProvider, android.content.ContentProvider
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (selectionArgs == null || selectionArgs.length == 0) {
            throw new IllegalArgumentException("SelectionArgs must be provided for the Uri: " + uri);
        }
        final List<Document> listDocs = Lists.newArrayList();
        final Semaphore sem = new Semaphore(0);
        String query = selectionArgs[0];
        this.mQuery = query.toLowerCase();
        String url = DfeApi.formSearchUrl(this.mQuery, 0);
        DfeApi api = FinskyApp.get().getDfeApi();
        api.search(url, 0, 100, new PaginatedDfeRequest.PaginatedListener<SearchResponse>() { // from class: com.google.android.finsky.providers.SuggestionProvider.1
            @Override // com.google.android.finsky.api.PaginatedDfeRequest.PaginatedListener
            public void onResponse(SearchResponse response, int offset, int count) {
                for (DocList.Bucket bucket : response.getBucketList()) {
                    String cookie = bucket.getAnalyticsCookie();
                    FinskyApp.get().getAnalytics().reportVirtualPageView(Analytics.Event.SEARCH_SUGGESTION, cookie);
                    for (DeviceDoc.DeviceDocument doc : bucket.getDocumentList()) {
                        listDocs.add(new Document(doc, cookie));
                    }
                }
                sem.release();
            }
        }, new Response.ErrorListener() { // from class: com.google.android.finsky.providers.SuggestionProvider.2
            @Override // com.android.volley.Response.ErrorListener
            public void onErrorResponse(Response.ErrorCode error, String errorMessage) {
                sem.release();
            }
        });
        try {
            sem.acquire();
            if (listDocs.size() == 0) {
                return emptyCursor();
            }
            MatrixCursor matrixCursor = new MatrixCursor(COLUMNS);
            for (int i = 0; i < getMaxItemsToDisplay(listDocs); i++) {
                Document doc = listDocs.get(i);
                Object[] row = new Object[COLUMNS.length];
                row[0] = Integer.valueOf(doc.getDocId().hashCode());
                row[1] = Integer.valueOf(CorpusMetadata.getIconResource(doc.getBackend()));
                row[2] = removeEllipsis(doc.getTitle());
                row[3] = doc.getCreator();
                row[4] = doc.getDocId();
                String detailsUrl = doc.getDetailsUrl();
                row[5] = detailsUrl;
                matrixCursor.addRow(row);
            }
            addLastRowIfNeeded(matrixCursor);
            return matrixCursor;
        } catch (InterruptedException e) {
            return emptyCursor();
        }
    }

    protected String getQuery() {
        return this.mQuery;
    }

    protected int getMaxItemsToDisplay(List<Document> listDocs) {
        return listDocs.size();
    }

    protected void addLastRowIfNeeded(MatrixCursor matrixCursor) {
    }

    private MatrixCursor emptyCursor() {
        return new MatrixCursor(COLUMNS);
    }

    private String removeEllipsis(String originalString) {
        int index = originalString.indexOf("...");
        return index == -1 ? originalString : originalString.substring(0, index);
    }
}
