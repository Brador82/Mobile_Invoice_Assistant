package com.mobileinvoice.ocr.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.mobileinvoice.ocr.R;

/* loaded from: classes5.dex */
public final class ItemImagePreviewBinding implements ViewBinding {
    public final ImageButton btnRemoveImage;
    public final ImageView imagePreview;
    private final FrameLayout rootView;

    private ItemImagePreviewBinding(FrameLayout rootView, ImageButton btnRemoveImage, ImageView imagePreview) {
        this.rootView = rootView;
        this.btnRemoveImage = btnRemoveImage;
        this.imagePreview = imagePreview;
    }

    @Override // androidx.viewbinding.ViewBinding
    public FrameLayout getRoot() {
        return this.rootView;
    }

    public static ItemImagePreviewBinding inflate(LayoutInflater inflater) {
        return inflate(inflater, null, false);
    }

    public static ItemImagePreviewBinding inflate(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        View root = inflater.inflate(R.layout.item_image_preview, parent, false);
        if (attachToParent) {
            parent.addView(root);
        }
        return bind(root);
    }

    public static ItemImagePreviewBinding bind(View rootView) {
        int id = R.id.btnRemoveImage;
        ImageButton btnRemoveImage = (ImageButton) ViewBindings.findChildViewById(rootView, id);
        if (btnRemoveImage != null) {
            id = R.id.imagePreview;
            ImageView imagePreview = (ImageView) ViewBindings.findChildViewById(rootView, id);
            if (imagePreview != null) {
                return new ItemImagePreviewBinding((FrameLayout) rootView, btnRemoveImage, imagePreview);
            }
        }
        String missingId = rootView.getResources().getResourceName(id);
        throw new NullPointerException("Missing required view with ID: ".concat(missingId));
    }
}
