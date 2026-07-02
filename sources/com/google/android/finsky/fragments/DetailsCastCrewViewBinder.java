package com.google.android.finsky.fragments;

import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import com.google.android.finsky.R;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.remoting.protos.DeviceDoc;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DetailsCastCrewViewBinder extends TableLayoutViewBinder<DeviceDoc.VideoCredit> {
    private List<DeviceDoc.VideoCredit> mCreditsList;

    public void bind(View view, Document doc) {
        super.bind(view, doc, R.id.header, R.string.details_cast_crew);
        this.mCreditsList = doc.getCreditsList();
        if (this.mCreditsList == null || this.mCreditsList.size() == 0) {
            this.mLayout.setVisibility(8);
        } else {
            this.mLayout.setVisibility(0);
            populateTable();
        }
    }

    @Override // com.google.android.finsky.fragments.TableLayoutViewBinder
    protected int getTableColumnCount() {
        return 4;
    }

    @Override // com.google.android.finsky.fragments.TableLayoutViewBinder
    protected int getCellCount() {
        return this.mCreditsList.size();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.android.finsky.fragments.TableLayoutViewBinder
    public DeviceDoc.VideoCredit getData(int position) {
        return this.mCreditsList.get(position);
    }

    @Override // com.google.android.finsky.fragments.TableLayoutViewBinder
    protected void populateTable() {
        TableLayout tableLayout = (TableLayout) this.mLayout.findViewById(R.id.table_layout);
        tableLayout.removeAllViews();
        TableRow tableRow = new TableRow(this.mContext);
        for (DeviceDoc.VideoCredit credit : this.mCreditsList) {
            addCellToRow(credit, tableRow);
        }
        finishRowWithEmptyCells(tableRow);
        tableLayout.addView(tableRow);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.finsky.fragments.TableLayoutViewBinder
    public void setupTableCell(DeviceDoc.VideoCredit data, ViewGroup view) {
        if (data != null) {
            TextView titleView = (TextView) view.findViewById(R.id.role_title);
            titleView.setText(data.getCredit().toUpperCase());
            LinearLayout namesLayoutView = (LinearLayout) view;
            List<String> allNames = data.getNameList();
            for (String name : allNames) {
                TextView singleNameView = (TextView) this.mInflater.inflate(R.layout.cast_crew_item, (ViewGroup) namesLayoutView, false);
                singleNameView.setText(name);
                namesLayoutView.addView(singleNameView);
            }
        }
    }

    @Override // com.google.android.finsky.fragments.TableLayoutViewBinder
    protected int getTableCellLayoutId() {
        return R.layout.cast_crew_list;
    }
}
