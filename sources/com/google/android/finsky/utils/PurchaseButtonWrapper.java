package com.google.android.finsky.utils;

import android.content.Context;
import com.google.android.finsky.R;
import com.google.android.finsky.api.model.Document;
import com.google.android.finsky.remoting.protos.Common;

/* JADX INFO: loaded from: classes.dex */
public class PurchaseButtonWrapper {
    private final ActionDescription mDescription;
    private final Document mDocument;
    private ActionIntent mIntent;
    private final Common.Offer mOffer;
    private int mVisibility;

    private enum ActionDescription {
        UPDATE,
        OPEN,
        INSTALL,
        PLAY,
        BUY,
        PRICE
    }

    private enum ActionIntent {
        MANAGE,
        OPEN,
        BUY
    }

    public interface PurchaseButtonHandler {
        void buy(Document document, int i);

        void manage(Document document);

        void open(Document document);
    }

    public PurchaseButtonWrapper(Document document, PackageInfoCache packageInfoCache) {
        this.mDocument = document;
        this.mOffer = document.getDefaultOffer();
        this.mVisibility = 0;
        if (document.ownedByUser(packageInfoCache)) {
            this.mIntent = ActionIntent.OPEN;
            switch (document.getBackend()) {
                case 3:
                    if (document.isLocallyAvailable(packageInfoCache)) {
                        if (document.isUpdateAvailable(packageInfoCache)) {
                            this.mDescription = ActionDescription.UPDATE;
                            this.mIntent = ActionIntent.MANAGE;
                        } else {
                            if (!document.canLaunch(packageInfoCache)) {
                                this.mVisibility = 8;
                            }
                            this.mDescription = ActionDescription.OPEN;
                        }
                    } else {
                        this.mDescription = ActionDescription.INSTALL;
                    }
                    break;
                case 4:
                    this.mDescription = ActionDescription.PLAY;
                    break;
                default:
                    this.mDescription = ActionDescription.OPEN;
                    break;
            }
            return;
        }
        if (document.getBackend() == 3 && document.isLocallyAvailable(packageInfoCache)) {
            this.mDescription = ActionDescription.OPEN;
            this.mIntent = ActionIntent.OPEN;
        } else {
            this.mDescription = ActionDescription.PRICE;
            this.mIntent = ActionIntent.BUY;
        }
    }

    public String getDisplayText(Context context) {
        switch (this.mDescription) {
            case UPDATE:
                return context.getString(R.string.update);
            case OPEN:
                return context.getString(R.string.open);
            case INSTALL:
                return context.getString(R.string.install);
            case PLAY:
                return context.getString(R.string.play);
            case BUY:
                return context.getString(R.string.buy);
            case PRICE:
                String price = this.mOffer.getFormattedAmount();
                if (this.mDocument.getBackend() == 4 && this.mOffer.getOfferType() == 4) {
                    price = context.getString(R.string.price_hd, price);
                }
                return price;
            default:
                return null;
        }
    }

    public void performDefaultAction(PurchaseButtonHandler nm) {
        switch (this.mIntent) {
            case BUY:
                nm.buy(this.mDocument, this.mOffer.getOfferType());
                break;
            case MANAGE:
                nm.manage(this.mDocument);
                break;
            case OPEN:
                nm.open(this.mDocument);
                break;
        }
    }

    public int getVisibility() {
        return this.mVisibility;
    }
}
