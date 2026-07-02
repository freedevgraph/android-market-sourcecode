package com.google.android.finsky.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.finsky.R;
import com.google.android.finsky.adapters.PurchaseDetailsAdapter;
import com.google.android.finsky.remoting.protos.DeviceDoc;
import com.google.android.finsky.utils.Lists;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class RentalTermsLayout extends FrameLayout {
    public RentalTermsLayout(Context context) {
        super(context);
    }

    public RentalTermsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RentalTermsLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void bindInfo(String title, List<DeviceDoc.VideoRentalTerm.Term> terms) {
        ListView listView = (ListView) findViewById(R.id.agreement_list);
        RentalTermsAdapter rentalAgreementAdapter = new RentalTermsAdapter(getContext(), terms);
        TextView titleView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.purchase_details_title, (ViewGroup) null);
        titleView.setText(title);
        listView.addHeaderView(titleView);
        listView.setAdapter((ListAdapter) rentalAgreementAdapter);
    }

    private class RentalTermsAdapter extends PurchaseDetailsAdapter {
        private List<PurchaseDetailsAdapter.DetailsEntry> mTotalList;

        RentalTermsAdapter(Context context, List<DeviceDoc.VideoRentalTerm.Term> terms) {
            super(context);
            this.mTotalList = Lists.newArrayList();
            for (DeviceDoc.VideoRentalTerm.Term term : terms) {
                PurchaseDetailsAdapter.DetailsEntry newEntry = new PurchaseDetailsAdapter.DetailsEntry();
                newEntry.headerText = term.getHeader();
                newEntry.contentText = term.getBody();
                this.mTotalList.add(newEntry);
            }
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.mTotalList.size();
        }

        @Override // com.google.android.finsky.adapters.PurchaseDetailsAdapter, android.widget.Adapter
        public PurchaseDetailsAdapter.DetailsEntry getItem(int position) {
            return this.mTotalList.get(position);
        }
    }
}
