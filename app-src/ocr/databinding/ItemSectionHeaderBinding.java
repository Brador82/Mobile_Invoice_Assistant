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
public final class ItemSectionHeaderBinding implements ViewBinding {
    public final ImageView ivExpandIcon;
    private final LinearLayout rootView;
    public final TextView tvHeaderTitle;

    private ItemSectionHeaderBinding(LinearLayout rootView, ImageView ivExpandIcon, TextView tvHeaderTitle) {
        this.rootView = rootView;
        this.ivExpandIcon = ivExpandIcon;
        this.tvHeaderTitle = tvHeaderTitle;
    }

    @Override // androidx.viewbinding.ViewBinding
    public LinearLayout getRoot() {
        return this.rootView;
    }

    public static ItemSectionHeaderBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ItemSectionHeaderBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.item_section_header, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ItemSectionHeaderBinding bind(View rootView) {
        int id = R.id.ivExpandIcon;
        ImageView ivExpandIcon = (ImageView) ViewBindings.findChildViewById(rootView, id);
        if (ivExpandIcon != null) {
            id = R.id.tvHeaderTitle;
            TextView tvHeaderTitle = (TextView) ViewBindings.findChildViewById(rootView, id);
            if (tvHeaderTitle != null) {
                return new ItemSectionHeaderBinding((LinearLayout) rootView, ivExpandIcon, tvHeaderTitle);
            }
        }
        String missingId = rootView.getResources().getResourceName(id);
        throw new NullPointerException("Missing required view with ID: ".concat(missingId));
    }
}
