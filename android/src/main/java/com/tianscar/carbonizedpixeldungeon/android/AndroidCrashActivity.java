package com.tianscar.carbonizedpixeldungeon.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.tianscar.carbonizedpixeldungeon.PDSettings;

import java.util.Locale;

public class AndroidCrashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crash);

        final Typeface customTypeface;
        Typeface typeface;
        if (PDSettings.systemFont()) {
            typeface = null;
        }
        else try {
            String pixelFont;
            Locale defaultLocale = Locale.getDefault();
            if (defaultLocale.getLanguage().equals(Locale.JAPANESE.getLanguage())) pixelFont = "fusion_pixel_jp";
            else if (defaultLocale.getLanguage().equals(Locale.KOREA.getLanguage())) pixelFont = "fusion_pixel_kr";
            else if (defaultLocale.getLanguage().equals("zh")) {
                if (defaultLocale.getCountry().equals("HK") || defaultLocale.getCountry().equals("MO") || defaultLocale.getCountry().equals("TW")) {
                    pixelFont = "fusion_pixel_tc";
                }
                else pixelFont = "fusion_pixel_zh";
            }
            else pixelFont = "pixel_font_latin1";
            typeface = Typeface.createFromAsset(getAssets(), "fonts/" + pixelFont + ".ttf");
        } catch (RuntimeException e) {
            typeface = null;
        }
        customTypeface = typeface;

        TextView crashText = findViewById(R.id.crash_text);
        Button restartButton = findViewById(R.id.restart_button);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidCrashHandler.restartApplication(AndroidCrashActivity.this, AndroidLauncher.class);
            }
        });
        Button closeButton = findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidCrashHandler.closeApplication(AndroidCrashActivity.this);
            }
        });
        Button moreInfoButton = findViewById(R.id.more_info_button);
        moreInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(AndroidCrashActivity.this)
                        .setTitle(typeface(customTypeface, getString(R.string.error_details_title)))
                        .setMessage(typeface(customTypeface, AndroidCrashHandler.getErrorDetails(AndroidCrashActivity.this, getIntent())))
                        .setPositiveButton(typeface(customTypeface, getString(R.string.error_details_close)), null)
                        .setNeutralButton(typeface(customTypeface, getString(R.string.error_details_copy)), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        }).show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button copyButton = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                copyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String errorInformation = AndroidCrashHandler.getErrorDetails(AndroidCrashActivity.this, getIntent());
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        if (clipboard != null) {
                            ClipData clip = ClipData.newPlainText(getString(R.string.error_details_clipboard_label), errorInformation);
                            clipboard.setPrimaryClip(clip);
                            copyButton.setText(typeface(customTypeface, getString(R.string.error_details_copied)));
                        }
                    }
                });
                TextView messageText = dialog.findViewById(android.R.id.message);
                if (messageText != null) messageText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            }
        });
        if (customTypeface != null) {
            crashText.setTypeface(customTypeface);
            restartButton.setTypeface(customTypeface);
            closeButton.setTypeface(customTypeface);
            moreInfoButton.setTypeface(customTypeface);
        }
    }

    private static CharSequence typeface(Typeface typeface, CharSequence text) {
        if (typeface == null) return text;
        SpannableString string = new SpannableString(text);
        string.setSpan(new TypefaceSpan(typeface), 0, text.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return string;
    }

    private static class TypefaceSpan extends MetricAffectingSpan {
        private final String mFamily;
        private final Typeface mTypeface;
        public TypefaceSpan(String family) {
            this(family, null);
        }
        public TypefaceSpan(Typeface typeface) {
            this(null, typeface);
        }
        private TypefaceSpan(String family, Typeface typeface) {
            mFamily = family;
            mTypeface = typeface;
        }
        public String getFamily() {
            return mFamily;
        }
        public Typeface getTypeface() {
            return mTypeface;
        }
        @Override
        public void updateDrawState(TextPaint ds) {
            updateTypeface(ds);
        }
        @Override
        public void updateMeasureState(TextPaint paint) {
            updateTypeface(paint);
        }
        private void updateTypeface(Paint paint) {
            if (mTypeface != null) {
                paint.setTypeface(mTypeface);
            } else if (mFamily != null) {
                applyFontFamily(paint, mFamily);
            }
        }
        private void applyFontFamily(Paint paint, String family) {
            int style;
            Typeface old = paint.getTypeface();
            if (old == null) {
                style = Typeface.NORMAL;
            } else {
                style = old.getStyle();
            }
            final Typeface styledTypeface = Typeface.create(family, style);
            int fake = style & ~styledTypeface.getStyle();

            if ((fake & Typeface.BOLD) != 0) {
                paint.setFakeBoldText(true);
            }

            if ((fake & Typeface.ITALIC) != 0) {
                paint.setTextSkewX(-0.25f);
            }
            paint.setTypeface(styledTypeface);
        }
        @Override
        public String toString() {
            return "TypefaceSpan {"
                    + "family='" + getFamily() + '\''
                    + ", typeface=" + getTypeface()
                    + '}';
        }
    }

}
