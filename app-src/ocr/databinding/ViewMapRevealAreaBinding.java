package com.mobileinvoice.ocr.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.mobileinvoice.ocr.R;

/* loaded from: classes5.dex */
public final class ViewMapRevealAreaBinding implements ViewBinding {
    public final LinearLayout mapRevealArea;
    private final LinearLayout rootView;
    public final TextView tvRevealHint;

    private ViewMapRevealAreaBinding(LinearLayout rootView, LinearLayout mapRevealArea, TextView tvRevealHint) {
        this.rootView = rootView;
        this.mapRevealArea = mapRevealArea;
        this.tvRevealHint = tvRevealHint;
    }

    @Override // androidx.viewbinding.ViewBinding
    public LinearLayout getRoot() {
        return this.rootView;
    }

    public static ViewMapRevealAreaBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ViewMapRevealAreaBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.view_map_reveal_area, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ViewMapRevealAreaBinding bind(View rootView) {
        LinearLayout mapRevealArea = (LinearLayout) rootView;
        int id = R.id.tvRevealHint;
        TextView tvRevealHint = (TextView) ViewBindings.findChildViewById(rootView, id);
        if (tvRevealHint != null) {
            return new ViewMapRevealAreaBinding((LinearLayout) rootView, mapRevealArea, tvRevealHint);
        }
        String missingId = rootView.getResources().getResourceName(id);
        throw new NullPointerException("Missing required view with ID: ".concat(missingId));
    }
}
