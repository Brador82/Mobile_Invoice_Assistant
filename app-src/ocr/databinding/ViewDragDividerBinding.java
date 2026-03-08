package com.mobileinvoice.ocr.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.viewbinding.ViewBinding;
import com.mobileinvoice.ocr.R;

/* loaded from: classes5.dex */
public final class ViewDragDividerBinding implements ViewBinding {
    public final LinearLayout dragDivider;
    private final LinearLayout rootView;

    private ViewDragDividerBinding(LinearLayout rootView, LinearLayout dragDivider) {
        this.rootView = rootView;
        this.dragDivider = dragDivider;
    }

    @Override // androidx.viewbinding.ViewBinding
    public LinearLayout getRoot() {
        return this.rootView;
    }

    public static ViewDragDividerBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ViewDragDividerBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.view_drag_divider, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ViewDragDividerBinding bind(View rootView) {
        if (rootView == null) {
            throw new NullPointerException("rootView");
        }
        LinearLayout dragDivider = (LinearLayout) rootView;
        return new ViewDragDividerBinding((LinearLayout) rootView, dragDivider);
    }
}
