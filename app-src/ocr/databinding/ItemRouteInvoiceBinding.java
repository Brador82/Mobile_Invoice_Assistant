package com.mobileinvoice.ocr.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.card.MaterialCardView;
import com.mobileinvoice.ocr.R;

/* loaded from: classes5.dex */
public final class ItemRouteInvoiceBinding implements ViewBinding {
    public final ImageButton btnCall;
    public final ImageButton btnNavigate;
    public final MaterialCardView cardInvoice;
    public final ImageView ivDragHandle;
    private final MaterialCardView rootView;
    public final TextView tvAddress;
    public final TextView tvCustomerName;
    public final TextView tvDistance;
    public final TextView tvItems;
    public final TextView tvStopNumber;

    private ItemRouteInvoiceBinding(MaterialCardView rootView, ImageButton btnCall, ImageButton btnNavigate, MaterialCardView cardInvoice, ImageView ivDragHandle, TextView tvAddress, TextView tvCustomerName, TextView tvDistance, TextView tvItems, TextView tvStopNumber) {
        this.rootView = rootView;
        this.btnCall = btnCall;
        this.btnNavigate = btnNavigate;
        this.cardInvoice = cardInvoice;
        this.ivDragHandle = ivDragHandle;
        this.tvAddress = tvAddress;
        this.tvCustomerName = tvCustomerName;
        this.tvDistance = tvDistance;
        this.tvItems = tvItems;
        this.tvStopNumber = tvStopNumber;
    }

    @Override // androidx.viewbinding.ViewBinding
    public MaterialCardView getRoot() {
        return this.rootView;
    }

    public static ItemRouteInvoiceBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ItemRouteInvoiceBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.item_route_invoice, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ItemRouteInvoiceBinding bind(View rootView) {
        int id = R.id.btnCall;
        ImageButton btnCall = (ImageButton) ViewBindings.findChildViewById(rootView, id);
        if (btnCall != null) {
            id = R.id.btnNavigate;
            ImageButton btnNavigate = (ImageButton) ViewBindings.findChildViewById(rootView, id);
            if (btnNavigate != null) {
                MaterialCardView cardInvoice = (MaterialCardView) rootView;
                id = R.id.ivDragHandle;
                ImageView ivDragHandle = (ImageView) ViewBindings.findChildViewById(rootView, id);
                if (ivDragHandle != null) {
                    id = R.id.tvAddress;
                    TextView tvAddress = (TextView) ViewBindings.findChildViewById(rootView, id);
                    if (tvAddress != null) {
                        id = R.id.tvCustomerName;
                        TextView tvCustomerName = (TextView) ViewBindings.findChildViewById(rootView, id);
                        if (tvCustomerName != null) {
                            id = R.id.tvDistance;
                            TextView tvDistance = (TextView) ViewBindings.findChildViewById(rootView, id);
                            if (tvDistance != null) {
                                id = R.id.tvItems;
                                TextView tvItems = (TextView) ViewBindings.findChildViewById(rootView, id);
                                if (tvItems != null) {
                                    id = R.id.tvStopNumber;
                                    TextView tvStopNumber = (TextView) ViewBindings.findChildViewById(rootView, id);
                                    if (tvStopNumber != null) {
                                        return new ItemRouteInvoiceBinding((MaterialCardView) rootView, btnCall, btnNavigate, cardInvoice, ivDragHandle, tvAddress, tvCustomerName, tvDistance, tvItems, tvStopNumber);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        String missingId = rootView.getResources().getResourceName(id);
        throw new NullPointerException("Missing required view with ID: ".concat(missingId));
    }
}
