package com.cartoon2021.photo.editor.CartoonEditor.Activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.EmbossMaskFilter;
import android.graphics.Point;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.cartoon2021.photo.editor.AdUtils.BaseActivity;
import com.cartoon2021.photo.editor.CartoonEditor.TextDemo.FontFace;
import com.cartoon2021.photo.editor.CartoonEditor.TextDemo.FontList_Adapter;
import com.cartoon2021.photo.editor.CartoonEditor.TextDemo.GradientManager;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import com.cartoon2021.photo.editor.R;

public class AddTextActivity extends BaseActivity implements View.OnClickListener {
    public static String etData;
    public static Bitmap finalBitmapText;
    static TextView TV_Text;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    public GridView grid_font;

    public Context mContext;

    public GradientManager mGradientManager;

    public int mHeight;

    public Random mRandom = new Random();

    public int mWidth;

    public Shader shader;

    public String str;
    ArrayList<Typeface> fontList;
    Snackbar snackbar;
    int textSize = 25;
    TextView textView;
    private CardView CV_TEXT;
    private EditText ET_text;
    private FrameLayout FLText;
    private TextView btn;
    private final int currentBackgroundColor = -1;
    private ImageView fback;
    private TextView final_done;
    private LinearLayout gradient;
    private ImageView iv_DoneGradiont;
    private ImageView iv_DoneSize;
    private ImageView iv_Enter_text;
    private ImageView iv_color;
    private ImageView iv_done;
    private ImageView iv_gradiont;
    private ImageView iv_size;
    private ImageView iv_style;
    private LinearLayout llEnter_text;
    private LinearLayout llMic;
    private LinearLayout llSize;
    private LinearLayout llSizeSeek;
    private LinearLayout llcolor;
    private LinearLayout llstyle;
    private RadioGroup mRG;
    private FrameLayout mainFrame;
    private DiscreteSeekBar sizeseekBar;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_text_demo);

        showInterstitial(AddTextActivity.this);
        showBannerAd(AddTextActivity.this, findViewById(R.id.ad_mini_native));

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().addFlags(Integer.MIN_VALUE);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        Bind();
        etData = TV_Text.getText().toString();
        setFontListForGrid();
        this.grid_font.setAdapter(new FontList_Adapter(this, this.fontList));
        this.btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AddTextActivity.this.mWidth = AddTextActivity.TV_Text.getWidth();
                AddTextActivity.this.mHeight = AddTextActivity.TV_Text.getHeight();
                AddTextActivity textDemo_Activity = AddTextActivity.this;
                textDemo_Activity.mGradientManager = new GradientManager(textDemo_Activity.mContext, new Point(AddTextActivity.this.mWidth, AddTextActivity.this.mHeight));
                int nextInt = AddTextActivity.this.mRandom.nextInt(3);
                if (nextInt == 0) {
                    shader = mGradientManager.getRandomLinearGradient();
                    AddTextActivity.TV_Text.setText(AddTextActivity.this.str);
                } else if (nextInt == 1) {
                    shader = mGradientManager.getRandomRadialGradient();
                    AddTextActivity.TV_Text.setText(AddTextActivity.this.str);
                } else {
                    shader = mGradientManager.getRandomSweepGradient();
                    AddTextActivity.TV_Text.setText(AddTextActivity.this.str);
                }
                AddTextActivity.TV_Text.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
                AddTextActivity.TV_Text.getPaint().setShader(AddTextActivity.this.shader);
            }
        });
        this.mRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rb_normal) {
                    AddTextActivity.TV_Text.getPaint().setMaskFilter(null);
                } else if (i == R.id.rb_emboss) {
                    AddTextActivity.TV_Text.getPaint().setMaskFilter(new EmbossMaskFilter(new float[]{1.0f, 5.0f, 1.0f}, 0.8f, 8.0f, 7.0f));
                } else if (i == R.id.rb_deboss) {
                    AddTextActivity.TV_Text.getPaint().setMaskFilter(new EmbossMaskFilter(new float[]{0.0f, -1.0f, 0.5f}, 0.8f, 13.0f, 7.0f));
                }
            }
        });
        this.grid_font.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                if (i == 0) {
                    AddTextActivity.TV_Text.setTypeface(FontFace.f2(AddTextActivity.this.getApplicationContext()));
                } else if (i == 1) {
                    AddTextActivity.TV_Text.setTypeface(FontFace.f3(AddTextActivity.this.getApplicationContext()));
                } else if (i == 2) {
                    AddTextActivity.TV_Text.setTypeface(FontFace.f4(AddTextActivity.this.getApplicationContext()));
                } else if (i == 3) {
                    AddTextActivity.TV_Text.setTypeface(FontFace.f5(AddTextActivity.this.getApplicationContext()));
                } else if (i == 4) {
                    AddTextActivity.TV_Text.setTypeface(FontFace.f6(AddTextActivity.this.getApplicationContext()));
                } else if (i == 5) {
                    AddTextActivity.TV_Text.setTypeface(FontFace.f7(AddTextActivity.this.getApplicationContext()));
                } else if (i == 6) {
                    AddTextActivity.TV_Text.setTypeface(FontFace.f8(AddTextActivity.this.getApplicationContext()));
                } else if (i == 7) {
                    AddTextActivity.TV_Text.setTypeface(FontFace.f9(AddTextActivity.this.getApplicationContext()));
                } else if (i == 8) {
                    AddTextActivity.TV_Text.setTypeface(FontFace.f10(AddTextActivity.this.getApplicationContext()));
                } else if (i == 9) {
                    AddTextActivity.TV_Text.setTypeface(FontFace.f11(AddTextActivity.this.getApplicationContext()));
                } else if (i == 10) {
                    AddTextActivity.TV_Text.setTypeface(FontFace.f16(AddTextActivity.this.getApplicationContext()));
                } else if (i == 11) {
                    AddTextActivity.TV_Text.setTypeface(FontFace.f17(AddTextActivity.this.getApplicationContext()));
                } else if (i == 12) {
                    AddTextActivity.TV_Text.setTypeface(FontFace.f18(AddTextActivity.this.getApplicationContext()));
                } else if (i == 13) {
                    AddTextActivity.TV_Text.setTypeface(FontFace.f19(AddTextActivity.this.getApplicationContext()));
                } else if (i == 14) {
                    AddTextActivity.TV_Text.setTypeface(FontFace.f20(AddTextActivity.this.getApplicationContext()));
                } else if (i == 15) {
                    AddTextActivity.TV_Text.setTypeface(FontFace.f22(AddTextActivity.this.getApplicationContext()));
                } else if (i == 16) {
                    AddTextActivity.TV_Text.setTypeface(FontFace.f23(AddTextActivity.this.getApplicationContext()));
                } else if (i == 17) {
                    AddTextActivity.TV_Text.setTypeface(FontFace.f24(AddTextActivity.this.getApplicationContext()));
                } else if (i == 18) {
                    AddTextActivity.TV_Text.setTypeface(FontFace.f25(AddTextActivity.this.getApplicationContext()));
                } else if (i == 19) {
                    AddTextActivity.TV_Text.setTypeface(FontFace.f26(AddTextActivity.this.getApplicationContext()));
                } else if (i == 20) {
                    AddTextActivity.TV_Text.setTypeface(FontFace.f27(AddTextActivity.this.getApplicationContext()));
                } else if (i == 21) {
                    AddTextActivity.TV_Text.setTypeface(FontFace.f28(AddTextActivity.this.getApplicationContext()));
                } else if (i == 22) {
                    AddTextActivity.TV_Text.setTypeface(FontFace.f30(AddTextActivity.this.getApplicationContext()));
                } else if (!(i == 23 || i == 24 || i == 25 || i == 26 || i == 27 || i == 28 || i == 29)) {
                }
                AddTextActivity.this.grid_font.startAnimation(AnimationUtils.loadAnimation(AddTextActivity.this.getApplicationContext(), R.anim.slide_down));
                AddTextActivity.this.grid_font.setVisibility(View.INVISIBLE);
            }
        });
        this.sizeseekBar.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            public void onStartTrackingTouch(DiscreteSeekBar discreteSeekBar) {
            }

            public void onStopTrackingTouch(DiscreteSeekBar discreteSeekBar) {
            }

            public void onProgressChanged(DiscreteSeekBar discreteSeekBar, int i, boolean z) {
                AddTextActivity.this.textSize = i;
                AddTextActivity.TV_Text.setTextSize((float) AddTextActivity.this.textSize);
            }
        });
        this.sizeseekBar.setProgress(this.textSize);
    }

    private void setFontListForGrid() {
        this.fontList = new ArrayList<>();
        this.fontList.add(FontFace.f2(getApplicationContext()));
        this.fontList.add(FontFace.f3(getApplicationContext()));
        this.fontList.add(FontFace.f4(getApplicationContext()));
        this.fontList.add(FontFace.f5(getApplicationContext()));
        this.fontList.add(FontFace.f6(getApplicationContext()));
        this.fontList.add(FontFace.f7(getApplicationContext()));
        this.fontList.add(FontFace.f8(getApplicationContext()));
        this.fontList.add(FontFace.f9(getApplicationContext()));
        this.fontList.add(FontFace.f10(getApplicationContext()));
        this.fontList.add(FontFace.f11(getApplicationContext()));
        this.fontList.add(FontFace.f16(getApplicationContext()));
        this.fontList.add(FontFace.f17(getApplicationContext()));
        this.fontList.add(FontFace.f18(getApplicationContext()));
        this.fontList.add(FontFace.f19(getApplicationContext()));
        this.fontList.add(FontFace.f20(getApplicationContext()));
        this.fontList.add(FontFace.f22(getApplicationContext()));
        this.fontList.add(FontFace.f23(getApplicationContext()));
        this.fontList.add(FontFace.f24(getApplicationContext()));
        this.fontList.add(FontFace.f25(getApplicationContext()));
        this.fontList.add(FontFace.f26(getApplicationContext()));
        this.fontList.add(FontFace.f27(getApplicationContext()));
        this.fontList.add(FontFace.f28(getApplicationContext()));
        this.fontList.add(FontFace.f30(getApplicationContext()));
    }

    private void Bind() {
        this.ET_text = findViewById(R.id.ET_text);
        this.iv_done = findViewById(R.id.iv_done);
        this.iv_done.setOnClickListener(this);
        this.llEnter_text = findViewById(R.id.llEnter_text);
        this.iv_Enter_text = findViewById(R.id.iv_Enter_text);
        this.iv_Enter_text.setOnClickListener(this);
        this.llSize = findViewById(R.id.llSize);
        this.iv_size = findViewById(R.id.iv_size);
        this.iv_size.setOnClickListener(this);
        TV_Text = findViewById(R.id.TV_Text);
        this.CV_TEXT = findViewById(R.id.CV_TEXT);
        this.mainFrame = findViewById(R.id.mainFrame);
        this.llcolor = findViewById(R.id.llcolor);
        this.iv_color = findViewById(R.id.iv_color);
        this.iv_color.setOnClickListener(this);
        this.iv_DoneSize = findViewById(R.id.iv_DoneSize);
        this.iv_DoneSize.setOnClickListener(this);
        this.llSizeSeek = findViewById(R.id.llSizeSeek);
        this.sizeseekBar = findViewById(R.id.sizeseekBar);
        this.grid_font = findViewById(R.id.grid_font);
        this.iv_style = findViewById(R.id.iv_style);
        this.iv_style.setOnClickListener(this);
        this.llstyle = findViewById(R.id.llstyle);
        this.fback = findViewById(R.id.img_back);
        this.fback.setOnClickListener(this);
        this.final_done = findViewById(R.id.btn_done);
        this.final_done.setOnClickListener(this);
        this.FLText = findViewById(R.id.FLText);
        this.iv_gradiont = findViewById(R.id.iv_gradiont);
        this.iv_gradiont.setOnClickListener(this);
        this.btn = findViewById(R.id.btn);
        this.mRG = findViewById(R.id.rg);
        this.iv_DoneGradiont = findViewById(R.id.iv_DoneGradiont);
        this.iv_DoneGradiont.setOnClickListener(this);
        this.gradient = findViewById(R.id.gradient);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                this.llSizeSeek.setVisibility(View.INVISIBLE);
                this.CV_TEXT.setVisibility(View.INVISIBLE);
                this.grid_font.setVisibility(View.INVISIBLE);
                this.gradient.setVisibility(View.INVISIBLE);
                finish();
                return;
            case R.id.btn_done:
                if (TV_Text.getText().toString().isEmpty()) {
                    this.snackbar = Snackbar.make(this.mainFrame, "Text not found, Please insert text first.", BaseTransientBottomBar.LENGTH_SHORT);
                    this.textView = this.snackbar.getView().findViewById(R.id.snackbar_text);
                    this.textView.setTextColor(getResources().getColor(R.color.white));
                    this.textView.setTextSize(16.0f);
                    this.snackbar.show();
                    return;
                }
                this.llSizeSeek.setVisibility(View.INVISIBLE);
                this.CV_TEXT.setVisibility(View.INVISIBLE);
                this.grid_font.setVisibility(View.INVISIBLE);
                this.gradient.setVisibility(View.INVISIBLE);
                finalBitmapText = getMainFrameBitmap();
                setResult(-1);
                finish();
                return;
            case R.id.iv_DoneGradiont:
                this.gradient.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down));
                this.gradient.setVisibility(View.INVISIBLE);
                return;
            case R.id.iv_DoneSize:
                this.llSizeSeek.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down));
                this.llSizeSeek.setVisibility(View.INVISIBLE);
                this.gradient.setVisibility(View.INVISIBLE);
                return;
            case R.id.iv_Enter_text:
                this.CV_TEXT.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up));
                this.CV_TEXT.setVisibility(View.VISIBLE);
                this.llSizeSeek.setVisibility(View.INVISIBLE);
                this.grid_font.setVisibility(View.INVISIBLE);
                this.gradient.setVisibility(View.INVISIBLE);
                return;
            case R.id.iv_color:
                if (TV_Text.getText().toString().isEmpty()) {
                    this.snackbar = Snackbar.make(this.mainFrame, "Text not found, Please insert text first.", BaseTransientBottomBar.LENGTH_SHORT);
                    this.textView = this.snackbar.getView().findViewById(R.id.snackbar_text);
                    this.textView.setTextColor(getResources().getColor(R.color.white));
                    this.textView.setTextSize(16.0f);
                    this.snackbar.show();
                    return;
                }
                colordailog();
                this.llSizeSeek.setVisibility(View.INVISIBLE);
                this.CV_TEXT.setVisibility(View.INVISIBLE);
                this.grid_font.setVisibility(View.INVISIBLE);
                this.gradient.setVisibility(View.INVISIBLE);
                return;
            case R.id.iv_done:
                if (this.ET_text.getText().toString().isEmpty()) {
                    this.ET_text.setError("Please Enter Text");
                    return;
                }
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.iv_done.getWindowToken(), 0);
                CV_TEXT.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down));
                getDataText();
                CV_TEXT.setVisibility(View.INVISIBLE);
                return;
            case R.id.iv_gradiont:
                if (TV_Text.getText().toString().isEmpty()) {
                    this.snackbar = Snackbar.make(this.mainFrame, "Text not found, Please insert text first.", BaseTransientBottomBar.LENGTH_SHORT);
                    this.textView = this.snackbar.getView().findViewById(R.id.snackbar_text);
                    this.textView.setTextColor(getResources().getColor(R.color.white));
                    this.textView.setTextSize(16.0f);
                    this.snackbar.show();
                    return;
                }
                CONFORM();
                this.gradient.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up));
                this.gradient.setVisibility(View.VISIBLE);
                this.llSizeSeek.setVisibility(View.INVISIBLE);
                this.CV_TEXT.setVisibility(View.INVISIBLE);
                this.grid_font.setVisibility(View.INVISIBLE);
                return;
            case R.id.iv_size:
                if (TV_Text.getText().toString().isEmpty()) {
                    snackbar = Snackbar.make(this.mainFrame, "Text not found, Please insert text first.", BaseTransientBottomBar.LENGTH_SHORT);
                    textView = this.snackbar.getView().findViewById(R.id.snackbar_text);
                    textView.setTextColor(getResources().getColor(R.color.white));
                    textView.setTextSize(16.0f);
                    snackbar.show();
                    return;
                }
                this.llSizeSeek.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up));
                this.llSizeSeek.setVisibility(View.VISIBLE);
                this.CV_TEXT.setVisibility(View.INVISIBLE);
                this.grid_font.setVisibility(View.INVISIBLE);
                this.gradient.setVisibility(View.INVISIBLE);
                return;
            case R.id.iv_style:
                if (TV_Text.getText().toString().isEmpty()) {
                    this.snackbar = Snackbar.make(this.mainFrame, "Text not found, Please insert text first.", BaseTransientBottomBar.LENGTH_SHORT);
                    this.textView = this.snackbar.getView().findViewById(R.id.snackbar_text);
                    this.textView.setTextColor(getResources().getColor(R.color.white));
                    this.textView.setTextSize(16.0f);
                    this.snackbar.show();
                    return;
                }
                this.grid_font.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up));
                this.grid_font.setVisibility(View.VISIBLE);
                this.llSizeSeek.setVisibility(View.INVISIBLE);
                this.CV_TEXT.setVisibility(View.INVISIBLE);
                this.gradient.setVisibility(View.INVISIBLE);
                return;
            default:
        }
    }

    private void promptSpeechInput() {
        Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
        intent.putExtra("android.speech.extra.LANGUAGE", Locale.getDefault());
        intent.putExtra("android.speech.extra.PROMPT", getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, 100);
        } catch (ActivityNotFoundException unused) {
            Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    private void CONFORM() {
        final AlertDialog create = new AlertDialog.Builder(this).create();
        create.setMessage("Multiple Click to apply different gradient text");
        create.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                create.dismiss();
            }
        });
        create.show();
    }

    private void colordailog() {
        ColorPickerDialogBuilder.with(this).initialColor(this.currentBackgroundColor).wheelType(ColorPickerView.WHEEL_TYPE.FLOWER).density(12).setOnColorSelectedListener(new OnColorSelectedListener() {
            public void onColorSelected(int i) {
            }
        }).setPositiveButton("ok", new ColorPickerClickListener() {
            public void onClick(DialogInterface dialogInterface, int i, Integer[] numArr) {
                StringBuilder sb = null;
                AddTextActivity.TV_Text.getPaint().setMaskFilter(null);
                AddTextActivity.TV_Text.getPaint().setShader(null);
                AddTextActivity.TV_Text.setTextColor(i);
                if (numArr != null) {
                    for (Integer num : numArr) {
                        if (num != null) {
                            if (sb == null) {
                                sb = new StringBuilder("Color List:");
                            }
                            sb.append("\r\n#").append(Integer.toHexString(num).toUpperCase());
                        }
                    }
                }
            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).showColorEdit(true).setColorEditTextColor(getResources().getColor(R.color.black)).build().show();
    }

    private void getDataText() {
        this.str = this.ET_text.getText().toString();
        TV_Text.setText(this.ET_text.getText().toString());
        this.ET_text.getText().clear();
    }

    private Bitmap getMainFrameBitmap() {
        this.FLText.setDrawingCacheEnabled(true);
        Bitmap createBitmap = Bitmap.createBitmap(this.FLText.getDrawingCache());
        if (Build.VERSION.SDK_INT >= 19) {
            createBitmap.setConfig(Bitmap.Config.ARGB_8888);
        }
        this.FLText.setDrawingCacheEnabled(false);
        int height = createBitmap.getHeight();
        int width = createBitmap.getWidth();
        int i = height;
        int i2 = i;
        int i3 = width;
        int i4 = i3;
        int i5 = 0;
        while (i5 < width) {
            int i6 = i2;
            int i7 = i;
            int i8 = i4;
            int i9 = i3;
            for (int i10 = 0; i10 < height; i10++) {
                if (createBitmap.getPixel(i5, i10) != 0) {
                    int i11 = i5 + 0;
                    if (i11 < i9) {
                        i9 = i11;
                    }
                    int i12 = width - i5;
                    if (i12 < i8) {
                        i8 = i12;
                    }
                    int i13 = i10 + 0;
                    if (i13 < i7) {
                        i7 = i13;
                    }
                    int i14 = height - i10;
                    if (i14 < i6) {
                        i6 = i14;
                    }
                }
            }
            i5++;
            i3 = i9;
            i4 = i8;
            i = i7;
            i2 = i6;
        }
        Log.d("Trimed bitmap", "left:" + i3 + " right:" + i4 + " top:" + i + " bottom:" + i2);
        return Bitmap.createBitmap(createBitmap, i3, i, (width - i3) - i4, (height - i) - i2);
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 41 && i2 == -1 && intent != null) {
            this.str = intent.getStringArrayListExtra("android.speech.extra.RESULTS").get(0);
            TV_Text.setText(this.str);
        }
    }
}
