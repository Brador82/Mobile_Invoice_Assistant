package com.mobileinvoice.ocr.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.mobileinvoice.ocr.R;

/* loaded from: classes5.dex */
public final class ItemCompletedHeaderBinding implements ViewBinding {
    public final ImageView ivExpandArrow;
    private final LinearLayout rootView;
    public final TextView tvCompletedCount;
    public final TextView tvHeaderTitle;

    private ItemCompletedHeaderBinding(LinearLayout rootView, ImageView ivExpandArrow, TextView tvCompletedCount, TextView tvHeaderTitle) {
        this.rootView = rootView;
        this.ivExpandArrow = ivExpandArrow;
        this.tvCompletedCount = tvCompletedCount;
        this.tvHeaderTitle = tvHeaderTitle;
    }

    @Override // androidx.viewbinding.ViewBinding
    public LinearLayout getRoot() {
        return this.rootView;
    }

    public static ItemCompletedHeaderBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ItemCompletedHeaderBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.item_completed_header, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ItemCompletedHeaderBinding bind(View rootView) {
        int id = R.id.ivExpandArrow;
        ImageView ivExpandArrow = (ImageView) ViewBindings.findChildViewById(rootView, id);
        if (ivExpandArrow != null) {
            id = R.id.tvCompletedCount;
            TextView tvCompletedCount = (TextView) ViewBindings.findChildViewById(rootView, id);
            if (tvCompletedCount != null) {
                id = R.id.tvHeaderTitle;
                TextView tvHeaderTitle = (TextView) ViewBindings.findChildViewById(rootView, id);
                if (tvHeaderTitle != null) {
                    return new ItemCompletedHeaderBinding((LinearLayout) rootView, ivExpandArrow, tvCompletedCount, tvHeaderTitle);
                }
            }
        }
        String missingId = rootView.getResources().getResourceName(id);
        throw new NullPointerException("Missing required view with ID: ".concat(missingId));
    }
}
