package com.udemy.qrscanner.app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;

public class QRImageAnalyser implements ImageAnalysis.Analyzer {

    FragmentManager fragmentManager;
    Context context;

    BottomDialog bottomDialog;

    public QRImageAnalyser(Context context, FragmentManager supportFragmentManager) {
        this.fragmentManager = supportFragmentManager;
        this.context = context;
        bottomDialog = new BottomDialog();
    }

    @Override
    public void analyze(@NonNull ImageProxy image) {
        @SuppressLint("UnsafeOptInUsageError") Image image1 = image.getImage();

        if (image1 != null) {
            InputImage inputImage = InputImage.fromMediaImage(
                    image1, image.getImageInfo().getRotationDegrees());

            BarcodeScannerOptions barcodeScannerOptions = new BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(
                            Barcode.FORMAT_QR_CODE,
                            Barcode.FORMAT_AZTEC
                    ).build();

            BarcodeScanner barcodeScanner = BarcodeScanning.getClient(barcodeScannerOptions);

            Task<List<Barcode>> result = barcodeScanner.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {
                            readBarcode(barcodes);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failed to read Barcode", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnCompleteListener(new OnCompleteListener<List<Barcode>>() {
                        @Override
                        public void onComplete(@NonNull Task<List<Barcode>> task) {
                            image1.close();
                        }
                    });
        }
    }

    private void readBarcode(List<Barcode> barcodes) {
        for (Barcode barcode : barcodes) {
            Rect rect = barcode.getBoundingBox();
            Point[] corners = barcode.getCornerPoints();
            String rawValue = barcode.getRawValue();
            int valueType = barcode.getValueType();

            switch (valueType) {
                case Barcode.TYPE_WIFI:
                    if (barcode.getWifi() != null) {
                        String ssid = barcode.getWifi().getSsid();
                        String password = barcode.getWifi().getPassword();
                        int type = barcode.getWifi().getEncryptionType();
                    }
                    break;
                case Barcode.TYPE_URL:
                    if (barcode.getUrl() != null) {
                        String title = barcode.getUrl().getTitle();
                        String url = barcode.getUrl().getUrl();

                        if (bottomDialog.isAdded()) {
                            bottomDialog.show(fragmentManager, "");
                            bottomDialog.setData(title, url);
                        }
                    }
                    break;
            }
        }
    }
}
