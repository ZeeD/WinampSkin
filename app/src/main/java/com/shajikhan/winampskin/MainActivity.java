package com.shajikhan.winampskin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.shajikhan.winampskin.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Space;
;
public class MainActivity extends AppCompatActivity {

    // HJ Story
    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    MainActivity mainActivity ;
    Context context ;
    DisplayMetrics displayMetrics ;
    String TAG = "Winamp";
    // FIXME: 7/24/21 Determine the following automagically
    float UPSCALE_FACTOR = 1.43f ;
    float density ;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        density = displayMetrics.scaledDensity;

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mainActivity = this ;
        context = getApplicationContext();
        setup();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setup () {
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        Log.d(TAG, displayMetrics.toString());
        LinearLayout window = findViewById(R.id.window);

        LinearLayout mainWindow = findViewById(R.id.main_window);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, convertDpToPixel((int) (width / displayMetrics.scaledDensity  * 116f/275f))) ;
//        Log.d(TAG, "setup: " + width / displayMetrics.scaledDensity  * 116f/275f);
        mainWindow.setLayoutParams(layoutParams);

        setupEqualizer();
        setupPlaylist();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setupEqualizer () {
        LinearLayout equalizer = findViewById(R.id.equalizer);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, convertDpToPixel((int) (displayMetrics.widthPixels / displayMetrics.scaledDensity  * 116f/275f))) ;

        equalizer.setLayoutParams(layoutParams);
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.eqmain),
        mBitmap1 = Bitmap.createBitmap(mBitmap, 0, 0, convertDpToPixel(275), convertDpToPixel(116));
        equalizer.setBackground(new BitmapDrawable(getResources(), mBitmap1));
        Drawable drawable = new Drawable() {
            @Override
            public void draw(@NonNull Canvas canvas) {
                this.setBounds(0, 0, convertDpToPixel(275), convertDpToPixel(116));
                Paint paint = new Paint();
                // Because we have to change the drawable we apply to the layout, we have to draw the eq main
                canvas.drawBitmap(
                        upscaleBitmap(getBitmap(0, 0, 275, 116, R.drawable.eqmain)),
                        0,
                        0,
                        paint
                );
                // title bar
                canvas.drawBitmap(
                        upscaleBitmap(//getBitmap(0, 134f, 275, 13.5f, R.drawable.eqmain)),
                                Bitmap.createScaledBitmap(
                                        getBitmap(0, 134f, 275, 13.5f, R.drawable.eqmain),
                                        convertDpToPixel(275), convertDpToPixel(15), true
                                )
                        ),
                        0,
                        0,
                        paint
                );
                // that equalizer waveform thingy
                canvas.drawBitmap(
                        upscaleBitmap(
                                Bitmap.createScaledBitmap(
                                        getBitmap(0, 294f, 113, 19, R.drawable.eqmain),
                                        convertDpToPixel(113), convertDpToPixel(19), true
                                )
                        ),
                        convertDpToPixel(86 * UPSCALE_FACTOR),
                        convertDpToPixel(17 * UPSCALE_FACTOR),
                        paint
                );
            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(@Nullable ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return PixelFormat.OPAQUE;
            }
        } ;

        equalizer.setBackground(drawable);

        Button on = new WinampButton(
                context,
                mainActivity,
                R.drawable.eqmain,
                10, 119, 24, 12
        ), auto = new WinampButton(
                context,
                mainActivity,
                R.drawable.eqmain,
                35, 119, 33, 12
        ),  preset = new WinampButton(
                context,
                mainActivity,
                R.drawable.eqmain,
                224, 164, 44, 12
        );

        LinearLayout buttons = findViewById(R.id.equalizer_buttons);
        LinearLayout.LayoutParams layoutParamsButtons = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsButtons.setMargins(convertDpToPixel(14 * UPSCALE_FACTOR), convertDpToPixel(18 * UPSCALE_FACTOR), convertDpToPixel(4 * UPSCALE_FACTOR), 0);
        buttons.setLayoutParams(layoutParamsButtons);
        buttons.addView(on);
        buttons.addView(auto);

        LinearLayout.LayoutParams layoutParamsPreset = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParamsPreset.setMargins((int) (144 * UPSCALE_FACTOR * density), 0, 0, 0);

        preset.setLayoutParams(layoutParamsPreset);
        buttons.addView(preset);

        LinearLayout sliders = findViewById(R.id.equalizer_sliders);
        LinearLayout.LayoutParams layoutParamsSliders = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParamsSliders.setMargins(convertDpToPixel(21 * UPSCALE_FACTOR), convertDpToPixel(7 * UPSCALE_FACTOR), convertDpToPixel(4 * UPSCALE_FACTOR), convertDpToPixel(14 * UPSCALE_FACTOR));
        sliders.setLayoutParams(layoutParamsSliders);

        SeekBar gain = new WinampSlider(context, mainActivity, R.drawable.eqmain, 13, 164, 14, 64);
        sliders.addView(gain);
    }

    public int convertDpToPixel(float dp){
        double px = dp * (displayMetrics.densityDpi / 160D);
        return (int) Math.round(px);
    }

    public void setupPlaylist () {
        LinearLayout linearLayout = findViewById(R.id.playlist);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, convertDpToPixel((height / displayMetrics.scaledDensity) - (116+116)));
        //TODO: The following works but is unnecessary. But since I've already written it and it works perfectly,
        //      I've kept it in.
        linearLayout.setLayoutParams(layoutParams);

        Drawable drawable = new Drawable() {
            @Override
            public void draw(@NonNull Canvas canvas) {
                this.setBounds(0, 0, width, convertDpToPixel((height / displayMetrics.scaledDensity) - (116+116)));
                int canvasHeight = convertDpToPixel((height / displayMetrics.scaledDensity) - (116+116)) ;
                Paint paint = new Paint();
                Bitmap topLeft = getBitmap(0,0,24,20, R.drawable.pledit);
                canvas.drawBitmap(upscaleBitmap(topLeft), 0, 0, paint);
                Bitmap topLeftEx = upscaleBitmap(getBitmap(127.5f,0,24f,20, R.drawable.pledit));
                for (int i = 1 ; i < 6 ; i ++) {
                    canvas.drawBitmap(topLeftEx, convertDpToPixel(24) * i, 0, paint);
                }

                for (int i = 10 ; i < 16 ; i ++) {
                    canvas.drawBitmap(topLeftEx, convertDpToPixel(24) * i, 0, paint);
                }

                // top right
                canvas.drawBitmap(
                        upscaleBitmap(getBitmap(153.5f, 0, 24f, 20, R.drawable.pledit)),
//                        convertDpToPixel(24 * 15f),
                        displayMetrics.widthPixels - convertDpToPixel(24 * UPSCALE_FACTOR),
                        0,
                        paint
                );

                Bitmap left = upscaleBitmap(getBitmap(0,42.5f,12,29, R.drawable.pledit));
                canvas.drawBitmap(left, 0, convertDpToPixel(20.5f), paint);
                for (int i = 2 ; i < 30 ; i ++) {
                    canvas.drawBitmap(left, 0, i * convertDpToPixel(20 * UPSCALE_FACTOR), paint);
//                    Log.d(TAG, "draw: " + i * convertDpToPixel(20) + ", " + convertDpToPixel(272) +", " + displayMetrics.heightPixels);
//                    if ((i * convertDpToPixel(20) + convertDpToPixel(350)) > ((float)displayMetrics.heightPixels ))
                    if (i * convertDpToPixel(29 * UPSCALE_FACTOR) > displayMetrics.heightPixels - convertDpToPixel(212))
                        break ;
                }

                Bitmap right = upscaleBitmap(getBitmap(31f,42.5f,20f,29, R.drawable.pledit));
//                canvas.drawBitmap(right, displayMetrics.widthPixels - convertDpToPixel(21f), convertDpToPixel(20.5f), paint);
                canvas.drawBitmap(right, displayMetrics.widthPixels - convertDpToPixel(20 * UPSCALE_FACTOR), convertDpToPixel(20 * UPSCALE_FACTOR), paint);
                for (int i = 2 ; i < 30 ; i ++) {
//                    canvas.drawBitmap(right, displayMetrics.widthPixels - convertDpToPixel(21f), i * convertDpToPixel(20), paint);
//                    Log.d(TAG, "draw: " + i * convertDpToPixel(20) + ", " + convertDpToPixel(272) +", " + displayMetrics.heightPixels);
                    canvas.drawBitmap(right, displayMetrics.widthPixels - convertDpToPixel(20 * UPSCALE_FACTOR), i *convertDpToPixel(20 * UPSCALE_FACTOR), paint);
//                    if ((i * convertDpToPixel(20) + convertDpToPixel(350)) > ((float)displayMetrics.heightPixels ))
                    if (i * convertDpToPixel(29 * UPSCALE_FACTOR) > displayMetrics.heightPixels - convertDpToPixel(212))
                        break ;
                }

                // top winamp bar
                canvas.drawBitmap(
                        upscaleBitmap(getBitmap(26.5f, 0, 99f, 20, R.drawable.pledit)),
                        convertDpToPixel(24 * 6f),
                        0,
                        paint
                );

                // bottom bar
                for (int i = 0 ; i < 24 * 5 ; i = i + 24) {
                    canvas.drawBitmap(
                            upscaleBitmap(getBitmap(179.5f, 0.5f, 24, 37.5f, R.drawable.pledit)),
                            convertDpToPixel(i + 124f),
                            displayMetrics.heightPixels - convertDpToPixel(212)  - convertDpToPixel(38 * displayMetrics.scaledDensity * UPSCALE_FACTOR),
                            paint
                    );
                }

                // bottom left
                canvas.drawBitmap(
                        upscaleBitmap(getBitmap(0, 72.5f, 124.5f, 37.5f, R.drawable.pledit)),
                        0,
//                        displayMetrics.heightPixels - (convertDpToPixel(232) + convertDpToPixel(38 * UPSCALE_FACTOR)),
                        displayMetrics.heightPixels - convertDpToPixel(212)  - convertDpToPixel(38 * displayMetrics.scaledDensity * UPSCALE_FACTOR),
                        paint
                );
//                Log.d(TAG, "draw : %d x %d".format ("%d %d", canvasHeight, convertDpToPixel(40 * displayMetrics.scaledDensity))) ;

                //bottom right
                canvas.drawBitmap(
                        upscaleBitmap(getBitmap(126.5f, 72.5f, 149.5f, 37.5f, R.drawable.pledit)),
                        displayMetrics.widthPixels - convertDpToPixel(149.5f * UPSCALE_FACTOR),
                        displayMetrics.heightPixels - convertDpToPixel(212) - convertDpToPixel(38f * displayMetrics.scaledDensity * UPSCALE_FACTOR),
                        paint
                );


            }

            @Override
            public void setAlpha(int alpha) {

            }

            @Override
            public void setColorFilter(@Nullable ColorFilter colorFilter) {

            }

            @Override
            public int getOpacity() {
                return PixelFormat.OPAQUE;
            }
        } ;

        linearLayout.setBackground(drawable);

    }

    public Bitmap getBitmap (float x, float y, float width, float height, int resource) {
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), resource) ;
        mBitmap = Bitmap.createBitmap(mBitmap, convertDpToPixel(x), convertDpToPixel(y), convertDpToPixel(width), convertDpToPixel(height));

        return mBitmap ;
    }

    public Bitmap upscaleBitmap (Bitmap bitmap) {
        return Bitmap.createScaledBitmap(bitmap,
                (int)((float) bitmap.getWidth() * UPSCALE_FACTOR),
                (int)((float) bitmap.getHeight() * UPSCALE_FACTOR),
            true
        );
    }

    public Bitmap upscaleBitmapEx (Bitmap bitmap) {
        return Bitmap.createScaledBitmap(bitmap,
                (int)((float) bitmap.getWidth() * UPSCALE_FACTOR) + (int) density,
                (int)((float) bitmap.getHeight() * UPSCALE_FACTOR) + (int) density,
            true
        );
    }
}