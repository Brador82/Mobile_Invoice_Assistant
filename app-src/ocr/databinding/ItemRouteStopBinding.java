package com.mobileinvoice.ocr.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.mobileinvoice.ocr.R;

/* loaded from: classes5.dex */
public final class ItemRouteStopBinding implements ViewBinding {
    public final ImageButton btnCall;
    public final ImageButton btnDecreaseTime;
    public final ImageButton btnExpand;
    public final ImageButton btnIncreaseTime;
    public final Button btnMakeFirst;
    public final Button btnMakeLast;
    public final ImageButton btnNavigate;
    public final Button btnTime120;
    public final Button btnTime15;
    public final Button btnTime30;
    public final Button btnTime45;
    public final Button btnTime60;
    public final Button btnTime90;
    public final CheckBox cbCompleted;
    public final ImageView dragHandle;
    public final LinearLayout expandablePanel;
    public final LinearLayout mainContent;
    private final CardView rootView;
    public final TextView tvAddress;
    public final TextView tvCustomerName;
    public final TextView tvETA;
    public final TextView tvPriorityBadge;
    public final TextView tvStopInfo;
    public final TextView tvStopNumber;
    public final TextView tvStopTime;
    public final TextView tvStopTimeValue;

    private ItemRouteStopBinding(CardView rootView, ImageButton btnCall, ImageButton btnDecreaseTime, ImageButton btnExpand, ImageButton btnIncreaseTime, Button btnMakeFirst, Button btnMakeLast, ImageButton btnNavigate, Button btnTime120, Button btnTime15, Button btnTime30, Button btnTime45, Button btnTime60, Button btnTime90, CheckBox cbCompleted, ImageView dragHandle, LinearLayout expandablePanel, LinearLayout mainContent, TextView tvAddress, TextView tvCustomerName, TextView tvETA, TextView tvPriorityBadge, TextView tvStopInfo, TextView tvStopNumber, TextView tvStopTime, TextView tvStopTimeValue) {
        this.rootView = rootView;
        this.btnCall = btnCall;
        this.btnDecreaseTime = btnDecreaseTime;
        this.btnExpand = btnExpand;
        this.btnIncreaseTime = btnIncreaseTime;
        this.btnMakeFirst = btnMakeFirst;
        this.btnMakeLast = btnMakeLast;
        this.btnNavigate = btnNavigate;
        this.btnTime120 = btnTime120;
        this.btnTime15 = btnTime15;
        this.btnTime30 = btnTime30;
        this.btnTime45 = btnTime45;
        this.btnTime60 = btnTime60;
        this.btnTime90 = btnTime90;
        this.cbCompleted = cbCompleted;
        this.dragHandle = dragHandle;
        this.expandablePanel = expandablePanel;
        this.mainContent = mainContent;
        this.tvAddress = tvAddress;
        this.tvCustomerName = tvCustomerName;
        this.tvETA = tvETA;
        this.tvPriorityBadge = tvPriorityBadge;
        this.tvStopInfo = tvStopInfo;
        this.tvStopNumber = tvStopNumber;
        this.tvStopTime = tvStopTime;
        this.tvStopTimeValue = tvStopTimeValue;
    }

    @Override // androidx.viewbinding.ViewBinding
    public CardView getRoot() {
        return this.rootView;
    }

    public static ItemRouteStopBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ItemRouteStopBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.item_route_stop, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ItemRouteStopBinding bind(View rootView) {
        int id = R.id.btnCall;
        ImageButton btnCall = (ImageButton) ViewBindings.findChildViewById(rootView, id);
        if (btnCall != null) {
            id = R.id.btnDecreaseTime;
            ImageButton btnDecreaseTime = (ImageButton) ViewBindings.findChildViewById(rootView, id);
            if (btnDecreaseTime != null) {
                id = R.id.btnExpand;
                ImageButton btnExpand = (ImageButton) ViewBindings.findChildViewById(rootView, id);
                if (btnExpand != null) {
                    id = R.id.btnIncreaseTime;
                    ImageButton btnIncreaseTime = (ImageButton) ViewBindings.findChildViewById(rootView, id);
                    if (btnIncreaseTime != null) {
                        id = R.id.btnMakeFirst;
                        Button btnMakeFirst = (Button) ViewBindings.findChildViewById(rootView, id);
                        if (btnMakeFirst != null) {
                            id = R.id.btnMakeLast;
                            Button btnMakeLast = (Button) ViewBindings.findChildViewById(rootView, id);
                            if (btnMakeLast != null) {
                                id = R.id.btnNavigate;
                                ImageButton btnNavigate = (ImageButton) ViewBindings.findChildViewById(rootView, id);
                                if (btnNavigate != null) {
                                    id = R.id.btnTime120;
                                    Button btnTime120 = (Button) ViewBindings.findChildViewById(rootView, id);
                                    if (btnTime120 != null) {
                                        id = R.id.btnTime15;
                                        Button btnTime15 = (Button) ViewBindings.findChildViewById(rootView, id);
                                        if (btnTime15 != null) {
                                            id = R.id.btnTime30;
                                            Button btnTime30 = (Button) ViewBindings.findChildViewById(rootView, id);
                                            if (btnTime30 != null) {
                                                id = R.id.btnTime45;
                                                Button btnTime45 = (Button) ViewBindings.findChildViewById(rootView, id);
                                                if (btnTime45 != null) {
                                                    id = R.id.btnTime60;
                                                    Button btnTime60 = (Button) ViewBindings.findChildViewById(rootView, id);
                                                    if (btnTime60 != null) {
                                                        id = R.id.btnTime90;
                                                        Button btnTime90 = (Button) ViewBindings.findChildViewById(rootView, id);
                                                        if (btnTime90 != null) {
                                                            id = R.id.cbCompleted;
                                                            CheckBox cbCompleted = (CheckBox) ViewBindings.findChildViewById(rootView, id);
                                                            if (cbCompleted != null) {
                                                                id = R.id.dragHandle;
                                                                ImageView dragHandle = (ImageView) ViewBindings.findChildViewById(rootView, id);
                                                                if (dragHandle != null) {
                                                                    id = R.id.expandablePanel;
                                                                    LinearLayout expandablePanel = (LinearLayout) ViewBindings.findChildViewById(rootView, id);
                                                                    if (expandablePanel != null) {
                                                                        id = R.id.mainContent;
                                                                        LinearLayout mainContent = (LinearLayout) ViewBindings.findChildViewById(rootView, id);
                                                                        if (mainContent != null) {
                                                                            id = R.id.tvAddress;
                                                                            TextView tvAddress = (TextView) ViewBindings.findChildViewById(rootView, id);
                                                                            if (tvAddress != null) {
                                                                                id = R.id.tvCustomerName;
                                                                                TextView tvCustomerName = (TextView) ViewBindings.findChildViewById(rootView, id);
                                                                                if (tvCustomerName != null) {
                                                                                    id = R.id.tvETA;
                                                                                    TextView tvETA = (TextView) ViewBindings.findChildViewById(rootView, id);
                                                                                    if (tvETA != null) {
                                                                                        id = R.id.tvPriorityBadge;
                                                                                        TextView tvPriorityBadge = (TextView) ViewBindings.findChildViewById(rootView, id);
                                                                                        if (tvPriorityBadge != null) {
                                                                                            id = R.id.tvStopInfo;
                                                                                            TextView tvStopInfo = (TextView) ViewBindings.findChildViewById(rootView, id);
                                                                                            if (tvStopInfo != null) {
                                                                                                id = R.id.tvStopNumber;
                                                                                                TextView tvStopNumber = (TextView) ViewBindings.findChildViewById(rootView, id);
                                                                                                if (tvStopNumber != null) {
                                                                                                    id = R.id.tvStopTime;
                                                                                                    TextView tvStopTime = (TextView) ViewBindings.findChildViewById(rootView, id);
                                                                                                    if (tvStopTime != null) {
                                                                                                        id = R.id.tvStopTimeValue;
                                                                                                        TextView tvStopTimeValue = (TextView) ViewBindings.findChildViewById(rootView, id);
                                                                                                        if (tvStopTimeValue != null) {
                                                                                                            return new ItemRouteStopBinding((CardView) rootView, btnCall, btnDecreaseTime, btnExpand, btnIncreaseTime, btnMakeFirst, btnMakeLast, btnNavigate, btnTime120, btnTime15, btnTime30, btnTime45, btnTime60, btnTime90, cbCompleted, dragHandle, expandablePanel, mainContent, tvAddress, tvCustomerName, tvETA, tvPriorityBadge, tvStopInfo, tvStopNumber, tvStopTime, tvStopTimeValue);
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
                        }
                    }
                }
            }
        }
        String missingId = rootView.getResources().getResourceName(id);
        throw new NullPointerException("Missing required view with ID: ".concat(missingId));
    }
}
