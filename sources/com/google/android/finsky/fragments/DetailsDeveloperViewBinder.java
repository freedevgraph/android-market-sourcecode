package com.google.android.finsky.fragments;

import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.finsky.R;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.utils.IntentUtils;

/* JADX INFO: loaded from: classes.dex */
public class DetailsDeveloperViewBinder extends TableLayoutViewBinder<Integer> {
    private String mDeveloperEmail;
    private String mDeveloperWebsite;
    private int mFilledCells;

    public void bind(View view, Document doc) {
        super.bind(view, doc, R.id.header, R.string.details_developer);
        this.mFilledCells = 0;
        if (this.mDoc.getAppDetails() != null) {
            this.mDeveloperWebsite = this.mDoc.getAppDetails().getDeveloperWebsite();
            this.mDeveloperEmail = this.mDoc.getAppDetails().getDeveloperEmail();
        }
        if (!TextUtils.isEmpty(this.mDeveloperWebsite)) {
            this.mFilledCells++;
        }
        if (!TextUtils.isEmpty(this.mDeveloperEmail)) {
            this.mFilledCells++;
        }
        if (this.mFilledCells == 0) {
            this.mLayout.setVisibility(8);
        } else {
            this.mLayout.setVisibility(0);
            populateTable();
        }
    }

    @Override // com.google.android.finsky.fragments.TableLayoutViewBinder
    protected int getTableColumnCount() {
        return 2;
    }

    @Override // com.google.android.finsky.fragments.TableLayoutViewBinder
    protected int getCellCount() {
        return 2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.google.android.finsky.fragments.TableLayoutViewBinder
    public Integer getData(int position) {
        return Integer.valueOf(position);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.finsky.fragments.TableLayoutViewBinder
    public void setupTableCell(Integer data, ViewGroup view) {
        if (data.intValue() < this.mFilledCells) {
            ImageView iconView = (ImageView) view.findViewById(R.id.icon);
            TextView labelView = (TextView) view.findViewById(R.id.prompt);
            TextView infoView = (TextView) view.findViewById(R.id.info);
            switch (data.intValue()) {
                case 0:
                    if (!TextUtils.isEmpty(this.mDeveloperWebsite)) {
                        iconView.setImageResource(R.drawable.ic_developer_website);
                        labelView.setText(R.string.developer_website);
                        infoView.setText(this.mDeveloperWebsite);
                        view.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.finsky.fragments.DetailsDeveloperViewBinder.1
                            @Override // android.view.View.OnClickListener
                            public void onClick(View v) {
                                DetailsDeveloperViewBinder.this.mContext.startActivity(IntentUtils.createViewIntentForUrl(Uri.parse(DetailsDeveloperViewBinder.this.mDeveloperWebsite)));
                            }
                        });
                        return;
                    }
                    break;
                case 1:
                    break;
                default:
                    return;
            }
            iconView.setImageResource(R.drawable.ic_developer_email);
            labelView.setText(R.string.developer_email);
            infoView.setText(this.mDeveloperEmail);
            view.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.finsky.fragments.DetailsDeveloperViewBinder.2
                @Override // android.view.View.OnClickListener
                public void onClick(View v) {
                    DetailsDeveloperViewBinder.this.mContext.startActivity(IntentUtils.createSendIntentForUrl(Uri.parse("mailto:" + DetailsDeveloperViewBinder.this.mDeveloperEmail)));
                }
            });
        }
    }

    @Override // com.google.android.finsky.fragments.TableLayoutViewBinder
    protected int getTableCellLayoutId() {
        return R.layout.list_panel_developer_item;
    }
}
