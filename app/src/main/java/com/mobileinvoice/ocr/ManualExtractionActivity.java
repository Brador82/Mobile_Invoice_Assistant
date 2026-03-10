package com.mobileinvoice.ocr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.mobileinvoice.ocr.PaddleOCREngine;
import com.mobileinvoice.ocr.SelectionOverlayView;
import com.mobileinvoice.ocr.database.Invoice;
import com.mobileinvoice.ocr.database.InvoiceDatabase;
import com.mobileinvoice.ocr.databinding.ActivityManualExtractionBinding;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* loaded from: classes7.dex */
public class ManualExtractionActivity extends BaseActivity {
    private ActivityManualExtractionBinding binding;
    private Invoice currentInvoice;
    private InvoiceDatabase database;
    private Bitmap fullBitmap;
    private TextRecognizer recognizer;
    private static final Pattern MODEL_PATTERN = Pattern
            .compile("(?i)(?:model|mdl|mod)\\s*(?:no\\.?|num\\.?|#|:)?\\s*[:#]?\\s*([A-Z0-9][A-Z0-9\\-]{3,19})");
    private static final Pattern SERIAL_PATTERN = Pattern
            .compile("(?i)(?:s/n|serial|ser\\.?|sn|a4l/serial)\\s*[#:]?\\s*([A-Z0-9][A-Z0-9\\-]{3,19})");
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\(?\\d{3}[)\\s.\\-]\\s*\\d{3}[\\s.\\-]\\d{4}");
    private static final Pattern INVOICE_PATTERN = Pattern.compile("(?i)(?:INV|invoice)[\\s#\\-]?[A-Z0-9]{4,}");
    // A4L serial number — always starts A4L: A4LSBYXWD0WX, A4LQ0Q2XL11
    private static final Pattern A4L_PATTERN = Pattern.compile("\\b(A4L[A-Z0-9]{4,14})\\b");
    // KY-style invoice number: 2 uppercase letters + 6-8 digits
    private static final Pattern KY_INVOICE_PATTERN = Pattern.compile("\\b([A-Z]{2}\\d{6,8})\\b");
    // Standalone model number without keyword prefix
    private static final Pattern BARE_MODEL_PATTERN = Pattern.compile("\\b(?!A4L)([A-Z]{2,5}\\d{1,7}[A-Z0-9]{0,7})\\b");
    private Map<FieldType, String> extractedValues = new EnumMap(FieldType.class);
    private Map<FieldType, Chip> fieldChips = new EnumMap(FieldType.class);
    private Map<FieldType, ChipState> chipStates = new EnumMap(FieldType.class);
    private Map<FieldType, ChipState> initialChipStates = new EnumMap(FieldType.class);
    private List<DeliveryItem> extractedItems = new ArrayList();
    private boolean isCropMode = false;
    private boolean imageModified = false;

    private enum ChipState {
        EMPTY,
        DETECTED,
        CONFIRMED
    }

    /* JADX INFO: Access modifiers changed from: private */
    enum FieldType {
        INVOICE_NUMBER("invoiceNumber", "Invoice #"),
        CUSTOMER_NAME("customerName", "Name"),
        ADDRESS("address", "Address"),
        PHONE("phone", "Phone"),
        NOTES("notes", "Notes"),
        ITEMS("items", "Items (Add)"),
        MODEL_NUMBER("modelNumber", "Model #"),
        SERIAL_NUMBER("serialNumber", "Serial #");

        final String key;
        final String label;

        FieldType(String key, String label) {
            this.key = key;
            this.label = label;
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity,
              // androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle savedInstanceState) {
        applyAppTheme();
        super.onCreate(savedInstanceState);
        this.binding = ActivityManualExtractionBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());
        this.database = InvoiceDatabase.getInstance(this);
        this.recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        int invoiceId = getIntent().getIntExtra("invoice_id", -1);
        if (invoiceId == -1) {
            Toast.makeText(this, "Invalid invoice", 0).show();
            finish();
            return;
        }
        for (FieldType f : FieldType.values()) {
            this.chipStates.put(f, ChipState.EMPTY);
        }
        loadInvoice(invoiceId);
        setupFieldChips();
        setupButtons();
        this.binding.selectionOverlay.setOnTextSelectedListener(new SelectionOverlayView.OnTextSelectedListener() { // from
                                                                                                                    // class:
                                                                                                                    // com.mobileinvoice.ocr.ManualExtractionActivity$$ExternalSyntheticLambda0
            @Override // com.mobileinvoice.ocr.SelectionOverlayView.OnTextSelectedListener
            public final void onTextSelected(String str, Rect rect) {
                ManualExtractionActivity.this.lambda$onCreate$0(str, rect);
            }
        });
        this.binding.tvHint.setText("Long-press & drag to select text · zoom/pan with two fingers");
        this.binding.layoutPreview.setVisibility(8);
    }

    private void loadInvoice(final int invoiceId) {
        new Thread(new Runnable() { // from class:
                                    // com.mobileinvoice.ocr.ManualExtractionActivity$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                ManualExtractionActivity.this.lambda$loadInvoice$3(invoiceId);
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadInvoice$3(int invoiceId) {
        this.currentInvoice = this.database.invoiceDao().getInvoiceByIdSync(invoiceId);
        if (this.currentInvoice == null) {
            runOnUiThread(new Runnable() { // from class:
                                           // com.mobileinvoice.ocr.ManualExtractionActivity$$ExternalSyntheticLambda9
                @Override // java.lang.Runnable
                public final void run() {
                    ManualExtractionActivity.this.lambda$loadInvoice$1();
                }
            });
            return;
        }
        if (this.currentInvoice.getInvoiceNumber() != null && !this.currentInvoice.getInvoiceNumber().isEmpty()) {
            this.extractedValues.put(FieldType.INVOICE_NUMBER, this.currentInvoice.getInvoiceNumber());
            this.chipStates.put(FieldType.INVOICE_NUMBER, ChipState.CONFIRMED);
        }
        if (this.currentInvoice.getCustomerName() != null && !this.currentInvoice.getCustomerName().isEmpty()
                && !"Unknown Customer".equals(this.currentInvoice.getCustomerName())) {
            this.extractedValues.put(FieldType.CUSTOMER_NAME, this.currentInvoice.getCustomerName());
            this.chipStates.put(FieldType.CUSTOMER_NAME, ChipState.CONFIRMED);
        }
        if (this.currentInvoice.getAddress() != null && !this.currentInvoice.getAddress().isEmpty()
                && !"No address found".equals(this.currentInvoice.getAddress())) {
            this.extractedValues.put(FieldType.ADDRESS, this.currentInvoice.getAddress());
            this.chipStates.put(FieldType.ADDRESS, ChipState.CONFIRMED);
        }
        if (this.currentInvoice.getPhone() != null && !this.currentInvoice.getPhone().isEmpty()
                && !"No phone".equals(this.currentInvoice.getPhone())) {
            this.extractedValues.put(FieldType.PHONE, this.currentInvoice.getPhone());
            this.chipStates.put(FieldType.PHONE, ChipState.CONFIRMED);
        }
        if (this.currentInvoice.getNotes() != null && !this.currentInvoice.getNotes().isEmpty()) {
            this.extractedValues.put(FieldType.NOTES, this.currentInvoice.getNotes());
            this.chipStates.put(FieldType.NOTES, ChipState.CONFIRMED);
        }
        if (this.currentInvoice.getItems() != null && !this.currentInvoice.getItems().isEmpty()) {
            this.extractedItems = ItemsHelper.fromJson(this.currentInvoice.getItems());
            if (!this.extractedItems.isEmpty()) {
                this.chipStates.put(FieldType.ITEMS, ChipState.CONFIRMED);
                // Populate model/serial chip states from the first item that has them
                for (DeliveryItem di : this.extractedItems) {
                    if (di.model != null && !di.model.isEmpty()) {
                        this.extractedValues.put(FieldType.MODEL_NUMBER, di.model);
                        this.chipStates.put(FieldType.MODEL_NUMBER, ChipState.CONFIRMED);
                        break;
                    }
                }
                for (DeliveryItem di : this.extractedItems) {
                    if (di.serial != null && !di.serial.isEmpty()) {
                        this.extractedValues.put(FieldType.SERIAL_NUMBER, di.serial);
                        this.chipStates.put(FieldType.SERIAL_NUMBER, ChipState.CONFIRMED);
                        break;
                    }
                }
            }
        }
        this.initialChipStates.putAll(this.chipStates);
        String imagePath = this.currentInvoice.getOriginalImagePath();
        if (imagePath != null && new File(imagePath).exists()) {
            this.fullBitmap = BitmapFactory.decodeFile(imagePath);
        }
        runOnUiThread(new Runnable() { // from class:
                                       // com.mobileinvoice.ocr.ManualExtractionActivity$$ExternalSyntheticLambda10
            @Override // java.lang.Runnable
            public final void run() {
                ManualExtractionActivity.this.lambda$loadInvoice$2();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadInvoice$1() {
        Toast.makeText(this, "Invoice not found", 0).show();
        finish();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$loadInvoice$2() {
        if (this.fullBitmap != null) {
            this.binding.selectionOverlay.setImage(this.fullBitmap);
            scanFullImageForRegions(this.fullBitmap);
        } else {
            Toast.makeText(this, "Could not load invoice image", 0).show();
            finish();
        }
        for (Map.Entry<FieldType, ChipState> entry : this.chipStates.entrySet()) {
            updateChipState(entry.getKey(), entry.getValue());
        }
    }

    private void setupFieldChips() {
        ChipGroup chipGroup = this.binding.chipGroupFields;
        for (FieldType field : FieldType.values()) {
            Chip chip = new Chip(this);
            chip.setText(field.label);
            chip.setCheckable(false);
            chip.setClickable(false);
            chip.setCheckedIconVisible(false);
            chip.setChipBackgroundColor(null);
            chip.setBackground(getResources().getDrawable(R.drawable.chip_gradient_bg, null));
            chip.setTextColor(-11184811);
            this.fieldChips.put(field, chip);
            chipGroup.addView(chip);
        }
    }

    private void updateChipState(FieldType field, ChipState state) {
        this.chipStates.put(field, state);
        Chip chip = this.fieldChips.get(field);
        if (chip == null) {
        }
        chip.setChipBackgroundColor(null);
        chip.setBackground(getResources().getDrawable(R.drawable.chip_gradient_bg, null));
        switch (state) {
            case EMPTY:
                chip.setTextColor(-11184811);
                chip.setChipIconVisible(false);
                break;
            case DETECTED:
                chip.setTextColor(-3355444);
                chip.setChipIconVisible(false);
                break;
            case CONFIRMED:
                chip.setTextColor(-2838729);
                chip.setChipIconResource(android.R.drawable.checkbox_on_background);
                chip.setChipIconVisible(true);
                chip.setChipIconTintResource(R.color.field_completed);
                break;
        }
    }

    private Set<FieldType> autoDetectFields(String text) {
        Set<FieldType> detected = new LinkedHashSet<>();
        if (PHONE_PATTERN.matcher(text).find()) {
            detected.add(FieldType.PHONE);
        }
        // A4L serial number takes priority — most specific identifier
        if (A4L_PATTERN.matcher(text).find()) {
            detected.add(FieldType.SERIAL_NUMBER);
        }
        if (INVOICE_PATTERN.matcher(text).find() || KY_INVOICE_PATTERN.matcher(text).find()) {
            detected.add(FieldType.INVOICE_NUMBER);
        }
        if (MODEL_PATTERN.matcher(text).find()) {
            detected.add(FieldType.MODEL_NUMBER);
        } else if (!detected.contains(FieldType.SERIAL_NUMBER) && !detected.contains(FieldType.INVOICE_NUMBER)) {
            // Heuristic: standalone alphanumeric 7-15 chars without keyword, has
            // letters+digits, not a phone
            Matcher mBare = BARE_MODEL_PATTERN.matcher(text.trim());
            if (mBare.find()) {
                String candidate = mBare.group(1);
                if (candidate.length() >= 7 && candidate.matches(".*[A-Z].*") && candidate.matches(".*\\d.*")
                        && !PHONE_PATTERN.matcher(text).find()) {
                    detected.add(FieldType.MODEL_NUMBER);
                }
            }
        }
        if (SERIAL_PATTERN.matcher(text).find()) {
            detected.add(FieldType.SERIAL_NUMBER);
        }
        if ((PHONE_PATTERN.matcher(text).find() || (text.matches("(?s).*\\d{1,5}\\s+[A-Za-z].*")
                && text.matches("(?si).*\\b(?:St|Ave|Blvd|Dr|Rd|Ln|Way|Ct|Pl|Apt|Suite|Hwy)\\b.*")))
                && text.matches("(?s).*\\d{1,5}\\s+[A-Za-z].*")
                && text.matches("(?si).*\\b(?:St|Ave|Blvd|Dr|Rd|Ln|Way|Ct|Pl|Apt|Suite|Hwy)\\b.*")) {
            detected.add(FieldType.ADDRESS);
        }
        String lower = text.toLowerCase();
        int i = 0;
        String[] strArr = { "washer", "dryer", "refrigerator", "dishwasher", "freezer", "range", "washtower",
                "microwave" };
        while (true) {
            if (i >= 8) {
                break;
            }
            String name = strArr[i];
            if (!lower.contains(name)) {
                i++;
            } else {
                detected.add(FieldType.ITEMS);
                break;
            }
        }
        return detected;
    }

    private void scanFullImageForRegions(Bitmap bitmap) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);
        this.recognizer.process(image).addOnSuccessListener(new OnSuccessListener() { // from class:
                                                                                      // com.mobileinvoice.ocr.ManualExtractionActivity$$ExternalSyntheticLambda7
            @Override // com.google.android.gms.tasks.OnSuccessListener
            public final void onSuccess(Object obj) {
                ManualExtractionActivity.this.lambda$scanFullImageForRegions$4((Text) obj);
            }
        }).addOnFailureListener(new OnFailureListener() { // from class:
                                                          // com.mobileinvoice.ocr.ManualExtractionActivity$$ExternalSyntheticLambda8
            @Override // com.google.android.gms.tasks.OnFailureListener
            public final void onFailure(Exception exc) {
                ManualExtractionActivity.lambda$scanFullImageForRegions$5(exc);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$scanFullImageForRegions$4(Text visionText) {
        // Line-level regions for blue visual overlay and tap-to-select
        List<PaddleOCREngine.TextRegion> lineRegions = new ArrayList<>();
        // Character-level regions for precise draw-selection
        List<PaddleOCREngine.TextRegion> charRegions = new ArrayList<>();
        for (Text.TextBlock block : visionText.getTextBlocks()) {
            for (Text.Line line : block.getLines()) {
                Rect lineBox = line.getBoundingBox();
                if (lineBox != null && !line.getText().trim().isEmpty()) {
                    lineRegions.add(new PaddleOCREngine.TextRegion(line.getText().trim(), lineBox));
                }
                for (Text.Element element : line.getElements()) {
                    for (Text.Symbol symbol : element.getSymbols()) {
                        Rect symBox = symbol.getBoundingBox();
                        String ch = symbol.getText();
                        if (symBox != null && ch != null && !ch.isEmpty()) {
                            charRegions.add(new PaddleOCREngine.TextRegion(ch, symBox));
                        }
                    }
                }
            }
        }
        this.binding.selectionOverlay.setTextRegions(lineRegions);
        this.binding.selectionOverlay.setCharRegions(charRegions);
    }

    static /* synthetic */ void lambda$scanFullImageForRegions$5(Exception e) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    /*
     * renamed from: showPreviewSheet, reason: merged with bridge method
     * [inline-methods]
     */
    public void lambda$onCreate$0(final String extractedText, Rect bitmapRect) {
        if (extractedText == null || extractedText.trim().isEmpty()) {
            return;
        }
        Set<FieldType> detected = autoDetectFields(extractedText);
        for (FieldType f : detected) {
            if (this.chipStates.get(f) != ChipState.CONFIRMED) {
                updateChipState(f, ChipState.DETECTED);
            }
        }
        this.binding.tvResultPreview.setText(extractedText.trim());
        this.binding.tvResultPreview.setSelection(this.binding.tvResultPreview.getText().length());
        this.binding.layoutPreview.setVisibility(0);
        showFieldAssignPopup(bitmapRect);
    }

    private void showFieldAssignPopup(final Rect bitmapRect) {
        PopupMenu popup = new PopupMenu(this, this.binding.btnAssignField);
        int menuId = 0;
        for (FieldType field : FieldType.values()) {
            popup.getMenu().add(0, menuId, menuId, field.label);
            menuId++;
        }
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(android.view.MenuItem item) {
                FieldType[] allFields = FieldType.values();
                int idx = item.getItemId();
                if (idx >= 0 && idx < allFields.length) {
                    FieldType selected = allFields[idx];
                    // Use whatever is currently in the EditText (user may have edited it)
                    String text = ManualExtractionActivity.this.binding.tvResultPreview
                            .getText().toString().trim();
                    if (!text.isEmpty()) {
                        applyTextToField(selected, text);
                        updateChipState(selected, ChipState.CONFIRMED);
                        if (bitmapRect != null) {
                            ManualExtractionActivity.this.binding.selectionOverlay.addCompletedSelection(
                                    selected.label, bitmapRect, 0xFFFFD700);
                        }
                        hapticFeedback();
                    }
                    ManualExtractionActivity.this.binding.layoutPreview.setVisibility(8);
                }
                return true;
            }
        });
        popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                // Keep preview open so user can edit and re-assign; only hide on outside tap
                // Actual hide is handled by the menu item click or background tap on
                // selectionOverlay
            }
        });
        popup.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupButtons$8(View v) {
        saveAndFinish();
    }

    private void setupButtons() {
        this.binding.btnDone.setOnClickListener(new View.OnClickListener() { // from class:
                                                                             // com.mobileinvoice.ocr.ManualExtractionActivity$$ExternalSyntheticLambda4
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ManualExtractionActivity.this.lambda$setupButtons$8(view);
            }
        });
        this.binding.btnReset.setOnClickListener(new View.OnClickListener() { // from class:
                                                                              // com.mobileinvoice.ocr.ManualExtractionActivity$$ExternalSyntheticLambda5
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ManualExtractionActivity.this.lambda$setupButtons$9(view);
            }
        });
        this.binding.btnRotateLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ManualExtractionActivity.this.rotateImage(-90f);
            }
        });
        this.binding.btnRotateRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ManualExtractionActivity.this.rotateImage(90f);
            }
        });
        this.binding.btnCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ManualExtractionActivity.this.toggleCropMode();
            }
        });
        this.binding.btnAssignField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Re-open field selection popup with current (possibly edited) text
                ManualExtractionActivity.this.showFieldAssignPopup(null);
            }
        });
        // Tapping the selectionOverlay area hides the preview panel
        this.binding.selectionOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ManualExtractionActivity.this.binding.layoutPreview.setVisibility(8);
            }
        });
    }

    private void rotateImage(float degrees) {
        if (this.fullBitmap == null || this.fullBitmap.isRecycled())
            return;
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        Bitmap rotated = Bitmap.createBitmap(this.fullBitmap, 0, 0,
                this.fullBitmap.getWidth(), this.fullBitmap.getHeight(), matrix, true);
        this.fullBitmap.recycle();
        this.fullBitmap = rotated;
        this.imageModified = true;
        this.binding.selectionOverlay.setImage(this.fullBitmap);
        this.binding.selectionOverlay.clearSelections();
        scanFullImageForRegions(this.fullBitmap);
    }

    private void toggleCropMode() {
        if (!this.isCropMode) {
            this.isCropMode = true;
            this.binding.selectionOverlay.setCropMode(true);
            this.binding.btnCrop.setText("Apply Crop");
            this.binding.tvHint.setText("Draw crop area · press Apply Crop to confirm");
        } else {
            this.isCropMode = false;
            Rect cropRect = this.binding.selectionOverlay.getCropBitmapRect();
            this.binding.selectionOverlay.setCropMode(false);
            if (cropRect != null && cropRect.width() > 20 && cropRect.height() > 20
                    && this.fullBitmap != null && !this.fullBitmap.isRecycled()) {
                cropRect.left = Math.max(0, cropRect.left);
                cropRect.top = Math.max(0, cropRect.top);
                cropRect.right = Math.min(this.fullBitmap.getWidth(), cropRect.right);
                cropRect.bottom = Math.min(this.fullBitmap.getHeight(), cropRect.bottom);
                if (cropRect.width() > 0 && cropRect.height() > 0) {
                    Bitmap cropped = Bitmap.createBitmap(this.fullBitmap,
                            cropRect.left, cropRect.top, cropRect.width(), cropRect.height());
                    this.fullBitmap.recycle();
                    this.fullBitmap = cropped;
                    this.imageModified = true;
                    this.binding.selectionOverlay.setImage(this.fullBitmap);
                    this.binding.selectionOverlay.clearSelections();
                    scanFullImageForRegions(this.fullBitmap);
                }
            }
            this.binding.btnCrop.setText("Crop");
            this.binding.tvHint.setText("Long-press & drag to select text · zoom/pan with two fingers");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$setupButtons$9(View v) {
        this.extractedValues.clear();
        this.extractedItems.clear();
        this.binding.selectionOverlay.clearSelections();
        for (FieldType field : FieldType.values()) {
            ChipState initial = this.initialChipStates.containsKey(field) ? this.initialChipStates.get(field)
                    : ChipState.EMPTY;
            updateChipState(field, initial);
        }
        this.binding.layoutPreview.setVisibility(8);
        this.binding.tvHint.setText("Zoom/pan with two fingers · Draw a box to extract text");
        Toast.makeText(this, "Extractions cleared", 0).show();
    }

    private void applyTextToField(FieldType field, String text) {
        switch (field.ordinal()) {
            case 5:
                this.extractedItems.clear();
                this.extractedItems.add(new DeliveryItem(text));
                break;
            case 6:
                if (this.extractedItems.isEmpty()) {
                    this.extractedItems.add(new DeliveryItem("Unknown", text, ""));
                } else {
                    this.extractedItems.get(this.extractedItems.size() - 1).model = text;
                }
                break;
            case 7:
                if (this.extractedItems.isEmpty()) {
                    this.extractedItems.add(new DeliveryItem("Unknown", "", text));
                } else {
                    this.extractedItems.get(this.extractedItems.size() - 1).serial = text;
                }
                break;
            default:
                this.extractedValues.put(field, text);
                break;
        }
    }

    private void hapticFeedback() {
        if (Build.VERSION.SDK_INT >= 30) {
            this.binding.selectionOverlay.performHapticFeedback(16);
        } else {
            this.binding.selectionOverlay.performHapticFeedback(0);
        }
    }

    private void saveAndFinish() {
        if (this.currentInvoice == null) {
            return;
        }
        String val = this.extractedValues.get(FieldType.INVOICE_NUMBER);
        if (val != null) {
            this.currentInvoice.setInvoiceNumber(val);
        }
        String val2 = this.extractedValues.get(FieldType.CUSTOMER_NAME);
        if (val2 != null) {
            this.currentInvoice.setCustomerName(val2);
        }
        String val3 = this.extractedValues.get(FieldType.ADDRESS);
        if (val3 != null) {
            this.currentInvoice.setAddress(val3);
        }
        String val4 = this.extractedValues.get(FieldType.PHONE);
        if (val4 != null) {
            this.currentInvoice.setPhone(val4);
        }
        String val5 = this.extractedValues.get(FieldType.NOTES);
        if (val5 != null) {
            this.currentInvoice.setNotes(val5);
        }
        if (!this.extractedItems.isEmpty()) {
            this.currentInvoice.setItems(ItemsHelper.toJson(this.extractedItems));
        }
        new Thread(new Runnable() { // from class:
                                    // com.mobileinvoice.ocr.ManualExtractionActivity$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                ManualExtractionActivity.this.lambda$saveAndFinish$11();
            }
        }).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveAndFinish$11() {
        if (this.imageModified && this.currentInvoice.getOriginalImagePath() != null
                && this.fullBitmap != null && !this.fullBitmap.isRecycled()) {
            try (FileOutputStream fos = new FileOutputStream(this.currentInvoice.getOriginalImagePath())) {
                this.fullBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            } catch (IOException e) {
                android.util.Log.e("ManualExtraction", "Failed to save modified image", e);
            }
        }
        this.database.invoiceDao().update(this.currentInvoice);
        runOnUiThread(new Runnable() { // from class:
                                       // com.mobileinvoice.ocr.ManualExtractionActivity$$ExternalSyntheticLambda11
            @Override // java.lang.Runnable
            public final void run() {
                ManualExtractionActivity.this.lambda$saveAndFinish$10();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$saveAndFinish$10() {
        Toast.makeText(this, R.string.extraction_saved, 0).show();
        setResult(-1);
        finish();
    }

    @Override // androidx.appcompat.app.AppCompatActivity,
              // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        if (this.recognizer != null) {
            this.recognizer.close();
        }
        if (this.fullBitmap == null || this.fullBitmap.isRecycled()) {
            return;
        }
        this.fullBitmap.recycle();
    }
}
