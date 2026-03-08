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
public final class ItemDeliveryCardBinding implements ViewBinding {
    public final MaterialCardView cardDelivery;
    public final ImageView ivNavigate;
    public final ImageView ivPhone;
    public final ImageView ivPriority;
    private final MaterialCardView rootView;
    public final TextView tvAddress;
    public final TextView tvCustomerName;
    public final TextView tvPackageCount;
    public final TextView tvStatus;
    public final TextView tvTimeWindow;
    public final TextView tvTrackingNumber;
    public final View viewStatusIndicator;

    private ItemDeliveryCardBinding(MaterialCardView rootView, MaterialCardView cardDelivery, ImageView ivNavigate, ImageView ivPhone, ImageView ivPriority, TextView tvAddress, TextView tvCustomerName, TextView tvPackageCount, TextView tvStatus, TextView tvTimeWindow, TextView tvTrackingNumber, View viewStatusIndicator) {
        this.rootView = rootView;
        this.cardDelivery = cardDelivery;
        this.ivNavigate = ivNavigate;
        this.ivPhone = ivPhone;
        this.ivPriority = ivPriority;
        this.tvAddress = tvAddress;
        this.tvCustomerName = tvCustomerName;
        this.tvPackageCount = tvPackageCount;
        this.tvStatus = tvStatus;
        this.tvTimeWindow = tvTimeWindow;
        this.tvTrackingNumber = tvTrackingNumber;
        this.viewStatusIndicator = viewStatusIndicator;
    }

    @Override // androidx.viewbinding.ViewBinding
    public MaterialCardView getRoot() {
        return this.rootView;
    }

    public static ItemDeliveryCardBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ItemDeliveryCardBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.item_delivery_card, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ItemDeliveryCardBinding bind(View rootView) {
        MaterialCardView cardDelivery = (MaterialCardView) rootView;
        int id = R.id.iv_navigate;
        ImageView ivNavigate = (ImageView) ViewBindings.findChildViewById(rootView, id);
        if (ivNavigate != null) {
            id = R.id.iv_phone;
            ImageView ivPhone = (ImageView) ViewBindings.findChildViewById(rootView, id);
            if (ivPhone != null) {
                id = R.id.iv_priority;
                ImageView ivPriority = (ImageView) ViewBindings.findChildViewById(rootView, id);
                if (ivPriority != null) {
                    id = R.id.tv_address;
                    TextView tvAddress = (TextView) ViewBindings.findChildViewById(rootView, id);
                    if (tvAddress != null) {
                        id = R.id.tv_customer_name;
                        TextView tvCustomerName = (TextView) ViewBindings.findChildViewById(rootView, id);
                        if (tvCustomerName != null) {
                            id = R.id.tv_package_count;
                            TextView tvPackageCount = (TextView) ViewBindings.findChildViewById(rootView, id);
                            if (tvPackageCount != null) {
                                id = R.id.tv_status;
                                TextView tvStatus = (TextView) ViewBindings.findChildViewById(rootView, id);
                                if (tvStatus != null) {
                                    id = R.id.tv_time_window;
                                    TextView tvTimeWindow = (TextView) ViewBindings.findChildViewById(rootView, id);
                                    if (tvTimeWindow != null) {
                                        id = R.id.tv_tracking_number;
                                        TextView tvTrackingNumber = (TextView) ViewBindings.findChildViewById(rootView, id);
                                        if (tvTrackingNumber != null) {
                                            int id2 = R.id.view_status_indicator;
                                            View viewStatusIndicator = ViewBindings.findChildViewById(rootView, id2);
                                            if (viewStatusIndicator == null) {
                                                id = id2;
                                            } else {
                                                return new ItemDeliveryCardBinding((MaterialCardView) rootView, cardDelivery, ivNavigate, ivPhone, ivPriority, tvAddress, tvCustomerName, tvPackageCount, tvStatus, tvTimeWindow, tvTrackingNumber, viewStatusIndicator);
                                            }
                                        }
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
