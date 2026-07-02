package com.google.android.finsky.providers;

import android.database.MatrixCursor;
import com.google.android.finsky.R;
import com.google.android.finsky.api.model.Document;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class LocalSuggestionProvider extends SuggestionProvider {
    public LocalSuggestionProvider() {
        setupSuggestions("com.google.android.finsky.LocalSuggestionProvider", 1);
    }

    @Override // com.google.android.finsky.providers.SuggestionProvider
    protected int getMaxItemsToDisplay(List<Document> listDocs) {
        return Math.min(listDocs.size(), 4);
    }

    @Override // com.google.android.finsky.providers.SuggestionProvider
    protected void addLastRowIfNeeded(MatrixCursor matrixCursor) {
        if (matrixCursor.getCount() != 0) {
            Object[] row = new Object[SuggestionProvider.COLUMNS.length];
            row[0] = 0;
            row[1] = Integer.valueOf(R.drawable.ic_apps);
            row[2] = getContext().getString(R.string.search_all_results);
            row[3] = null;
            row[4] = getQuery();
            row[5] = "allResults";
            matrixCursor.addRow(row);
        }
    }
}
