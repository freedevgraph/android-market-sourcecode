package com.google.android.finsky.fragments;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import com.google.android.finsky.R;
import com.google.android.finsky.api.model.Document;

/* JADX INFO: loaded from: classes.dex */
public class DetailsTextViewBinder extends DetailsViewBinder {
    private int mFullHeight;
    private int mTruncatedHeight;

    private int getDuration(int heightBefore, int heightAfter) {
        int minHeight = Math.min(heightBefore, heightAfter);
        float difference = Math.abs(heightBefore - heightAfter);
        int duration = (int) ((difference / (minHeight * 2)) * 250.0f);
        return Math.min(Math.max(duration, 250), 750);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void animateContentHeight(View view, int heightBefore, int heightAfter) {
        Animator animator = ObjectAnimator.ofInt(view, "maxHeight", heightBefore, heightAfter);
        int duration = getDuration(heightBefore, heightAfter);
        animator.setDuration(duration).start();
    }

    public void bind(View view, Document doc, int headerStringId, CharSequence content) {
        super.bind(view, doc, R.id.header, headerStringId);
        if (TextUtils.isEmpty(content)) {
            this.mLayout.setVisibility(8);
            return;
        }
        this.mLayout.setVisibility(0);
        final TextView contentView = (TextView) this.mLayout.findViewById(R.id.section_content);
        CharSequence oldContent = contentView.getText();
        if (oldContent == null || !content.toString().equals(oldContent.toString())) {
            contentView.setText(content);
            this.mFullHeight = -1;
            this.mTruncatedHeight = -1;
            setButtonVisibility(R.id.more_button, 8, 0);
            setButtonVisibility(R.id.less_button, 8, 0);
            contentView.setMaxLines(100);
            contentView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.google.android.finsky.fragments.DetailsTextViewBinder.1
                @Override // android.view.ViewTreeObserver.OnPreDrawListener
                public boolean onPreDraw() {
                    if (DetailsTextViewBinder.this.mFullHeight < 0) {
                        DetailsTextViewBinder.this.mFullHeight = contentView.getHeight();
                        contentView.setMaxLines(6);
                        return true;
                    }
                    DetailsTextViewBinder.this.mTruncatedHeight = contentView.getHeight();
                    contentView.getViewTreeObserver().removeOnPreDrawListener(this);
                    if (DetailsTextViewBinder.this.mFullHeight <= DetailsTextViewBinder.this.mTruncatedHeight) {
                        return true;
                    }
                    DetailsTextViewBinder.this.setButtonVisibility(R.id.more_button, 0, R.string.more_text);
                    DetailsTextViewBinder.this.setButtonClickListener(R.id.more_button, new View.OnClickListener() { // from class: com.google.android.finsky.fragments.DetailsTextViewBinder.1.1
                        @Override // android.view.View.OnClickListener
                        public void onClick(View v) {
                            DetailsTextViewBinder.this.setButtonVisibility(R.id.more_button, 8, 0);
                            DetailsTextViewBinder.this.setButtonVisibility(R.id.less_button, 0, R.string.less_text);
                            contentView.setMaxLines(100);
                            DetailsTextViewBinder.this.animateContentHeight(contentView, DetailsTextViewBinder.this.mTruncatedHeight, DetailsTextViewBinder.this.mFullHeight);
                        }
                    });
                    DetailsTextViewBinder.this.setButtonVisibility(R.id.less_button, 8, 0);
                    DetailsTextViewBinder.this.setButtonClickListener(R.id.less_button, new View.OnClickListener() { // from class: com.google.android.finsky.fragments.DetailsTextViewBinder.1.2
                        @Override // android.view.View.OnClickListener
                        public void onClick(View v) {
                            DetailsTextViewBinder.this.setButtonVisibility(R.id.more_button, 0, R.string.more_text);
                            DetailsTextViewBinder.this.setButtonVisibility(R.id.less_button, 8, 0);
                            contentView.getHeight();
                            contentView.setMaxLines(6);
                            DetailsTextViewBinder.this.animateContentHeight(contentView, DetailsTextViewBinder.this.mFullHeight, DetailsTextViewBinder.this.mTruncatedHeight);
                        }
                    });
                    return true;
                }
            });
        }
    }
}
