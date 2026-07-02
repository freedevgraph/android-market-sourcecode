package com.google.android.finsky.fragments;

import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import com.google.android.finsky.R;

/* JADX INFO: loaded from: classes.dex */
public abstract class TableLayoutViewBinder<T> extends DetailsViewBinder {
    protected abstract int getCellCount();

    protected abstract T getData(int i);

    protected abstract int getTableCellLayoutId();

    protected abstract int getTableColumnCount();

    protected abstract void setupTableCell(T t, ViewGroup viewGroup);

    protected void populateTable() {
        TableLayout tableLayout = (TableLayout) this.mLayout.findViewById(R.id.table_layout);
        tableLayout.removeAllViews();
        int i = 0;
        while (i < getCellCount()) {
            TableRow tableRow = new TableRow(this.mContext);
            for (int j = 0; j < getTableColumnCount() && i + j < getCellCount(); j++) {
                T data = getData(i + j);
                if (data != null) {
                    addCellToRow(data, tableRow);
                }
            }
            finishRowWithEmptyCells(tableRow);
            tableLayout.addView(tableRow);
            i += getTableColumnCount();
        }
    }

    protected void addCellToRow(T data, ViewGroup tableRow) {
        ViewGroup cell = (ViewGroup) this.mInflater.inflate(getTableCellLayoutId(), tableRow, false);
        setupTableCell(data, cell);
        tableRow.addView(cell);
    }

    protected void finishRowWithEmptyCells(ViewGroup tableRow) {
        int childViewsCount = tableRow.getChildCount();
        int emptyViewsNeededCount = getTableColumnCount() - childViewsCount;
        for (int k = 0; k < emptyViewsNeededCount; k++) {
            ViewGroup itemView = (ViewGroup) this.mInflater.inflate(getTableCellLayoutId(), tableRow, false);
            itemView.setVisibility(4);
            tableRow.addView(itemView);
        }
    }

    protected int getTableLayoutId() {
        return R.id.table_layout;
    }
}
