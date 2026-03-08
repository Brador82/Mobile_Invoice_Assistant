package com.mobileinvoice.ocr.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.card.MaterialCardView;
import com.mobileinvoice.ocr.R;

/* loaded from: classes5.dex */
public final class ItemInvoiceLibraryBinding implements ViewBinding {
    public final ImageView ivInvoiceImage;
    private final MaterialCardView rootView;
    public final TextView tvCustomerName;
    public final TextView tvInvoiceNumber;

    private ItemInvoiceLibraryBinding(MaterialCardView rootView, ImageView ivInvoiceImage, TextView tvCustomerName, TextView tvInvoiceNumber) {
        this.rootView = rootView;
        this.ivInvoiceImage = ivInvoiceImage;
        this.tvCustomerName = tvCustomerName;
        this.tvInvoiceNumber = tvInvoiceNumber;
    }

    @Override // androidx.viewbinding.ViewBinding
    public MaterialCardView getRoot() {
        return this.rootView;
    }

    public static ItemInvoiceLibraryBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ItemInvoiceLibraryBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.item_invoice_library, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ItemInvoiceLibraryBinding bind(View rootView) {
        int id = R.id.ivInvoiceImage;
        ImageView ivInvoiceImage = (ImageView) ViewBindings.findChildViewById(rootView, id);
        if (ivInvoiceImage != null) {
            id = R.id.tvCustomerName;
            TextView tvCustomerName = (TextView) ViewBindings.findChildViewById(rootView, id);
            if (tvCustomerName != null) {
                id = R.id.tvInvoiceNumber;
                TextView tvInvoiceNumber = (TextView) ViewBindings.findChildViewById(rootView, id);
                if (tvInvoiceNumber != null) {
                    return new ItemInvoiceLibraryBinding((MaterialCardView) rootView, ivInvoiceImage, tvCustomerName, tvInvoiceNumber);
                }
            }
        }
        String missingId = rootView.getResources().getResourceName(id);
        throw new NullPointerException("Missing required view with ID: ".concat(missingId));
    }
}
