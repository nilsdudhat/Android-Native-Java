package com.cartoon2021.photo.editor.GlobalActivities;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import com.cartoon2021.photo.editor.AdUtils.AdUtils;
import com.cartoon2021.photo.editor.AdUtils.AppUtility;
import com.cartoon2021.photo.editor.AdUtils.BaseActivity;
import com.cartoon2021.photo.editor.AdUtils.Constant;
import com.cartoon2021.photo.editor.BuildConfig;
import com.cartoon2021.photo.editor.R;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class StartActivity extends BaseActivity implements View.OnClickListener {

    CardView card_start;
    CardView card_privacy;
    CardView card_share;
    CardView card_rate;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_start);

        showInterstitial(StartActivity.this);
        showBannerAd(StartActivity.this, findViewById(R.id.ad_banner));

        card_start = findViewById(R.id.card_start);
        card_start.setOnClickListener(this);

        card_privacy = findViewById(R.id.card_privacy);
        card_privacy.setOnClickListener(this);

        card_share = findViewById(R.id.card_share);
        card_share.setOnClickListener(this);

        card_rate = findViewById(R.id.card_rate);
        card_rate.setOnClickListener(this);

        YoYo.with(Techniques.Shake).duration(5000).repeat(1000).playOn(findViewById(R.id.title_app));

        if (AppUtility.getInt(StartActivity.this, Constant.FORCEFULLY_UPDATE, BuildConfig.VERSION_CODE) > BuildConfig.VERSION_CODE) {
            Dialog dialog = new Dialog(StartActivity.this);
            dialog.setContentView(R.layout.update_app_dlg);
            dialog.getWindow().setBackgroundDrawable(null);
            dialog.setCancelable(false);

            dialog.findViewById(R.id.btn_update).setOnClickListener(view -> {
                if (AppUtility.isNetworkAvailable(StartActivity.this)) {
                    dialog.dismiss();
                    finishAffinity();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + AppUtility.getString(StartActivity.this, Constant.FORCEFULLY_UPDATE_PKG, getPackageName()))));
                }
            });
            dialog.findViewById(R.id.btn_cancel).setOnClickListener(view -> dialog.dismiss());
            dialog.show();
        }
    }

    private void requestPermissions() {
        // below line is use to request permission in the current activity.
        Dexter.withContext(StartActivity.this)
                // below line is use to request the number of permissions which are required in our app.
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                // after adding permissions we are calling an with listener method.
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        // this method is called when all permissions are granted
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            // do you work now
//                            Toast.makeText(StartActivity.this, "All the permissions are granted..", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(StartActivity.this, DashBoardActivity.class));
                        }
                        // check for permanent denial of any permission
                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permanently, we will show user a dialog message.
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        // this method is called when user grants some permission and denies some of them.
                        permissionToken.continuePermissionRequest();
                    }
                }).withErrorListener(new PermissionRequestErrorListener() {
            // this method is use to handle error in runtime permissions
            @Override
            public void onError(DexterError error) {
                // we are displaying a toast message for error message.
                Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
            }
        })
                // below line is use to run the permissions on same thread and to check the permissions
                .onSameThread().check();
    }

    // below is the shoe setting dialog method which is use to display a dialogue message.
    private void showSettingsDialog() {
        // we are displaying an alert dialog for permissions
        AlertDialog.Builder builder = new AlertDialog.Builder(StartActivity.this);

        // below line is the title for our alert dialog.
        builder.setTitle("Need Permissions");

        // below line is our message for our dialog
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // on clicking button we are redirecting our user from our app to the settings page of our app.
                dialog.cancel();
                // below is the intent from which we are redirecting our user.
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // this method is called when user click on negative button.
                dialog.cancel();
            }
        });
        // below line is used to display our dialog
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onClick(View view) {
        if (view.getId() == R.id.card_start) {
            requestPermissions();
        } else if (view.getId() == R.id.card_privacy) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cartoon-photo-editor-1.flycricket.io/privacy.html"));
            startActivity(browserIntent);

        } else if (view.getId() == R.id.card_rate) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        } else if (view.getId() == R.id.card_share) {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
                String shareMessage = "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onBackPressed() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.exit_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        TextView textView = dialog.findViewById(R.id.txt_exit);
        AdUtils.showMiniNativeAds(this, dialog.findViewById(R.id.ad_mini_native));
        textView.setEnabled(false);
        textView.setOnClickListener(v -> finishAffinity());
        new CountDownTimer(6000, 1000) {
            public void onTick(long j) {
                textView.setText((j / 1000) + "");
            }

            public void onFinish() {
                textView.setText("Yes");
                textView.setEnabled(true);
            }
        }.start();
        dialog.findViewById(R.id.rl_no).setOnClickListener(view -> dialog.dismiss());
        dialog.show();
    }
}
