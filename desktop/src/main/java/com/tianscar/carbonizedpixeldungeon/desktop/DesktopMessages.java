package com.tianscar.carbonizedpixeldungeon.desktop;

import java.util.Locale;
import java.util.ResourceBundle;

public class DesktopMessages {

    public static String get(String key, Object... args) {
        Locale defaultLocale = Locale.getDefault();
        if (defaultLocale.getLanguage().equals("zh")) {
            if (defaultLocale.getCountry().equals("HK") || defaultLocale.getCountry().equals("MO") || defaultLocale.getCountry().equals("TW")) {
                defaultLocale = new Locale("tc");
            }
            else defaultLocale = new Locale("zh");
        }
        ResourceBundle bundle = ResourceBundle.getBundle("messages/desktop/desktop", defaultLocale, new ResourceBundle.Control() {
            @Override
            public Locale getFallbackLocale(String baseName, Locale locale) {
                if (baseName == null) {
                    throw new NullPointerException();
                }
                Locale fallbackLocale = Locale.ENGLISH;
                return locale.equals(fallbackLocale) ? null : fallbackLocale;
            }
        });
        return (bundle != null && bundle.containsKey(key)) ? String.format(bundle.getString(key), args) : "Text missing: " + key;
    }

    public static String get(Object object, String key, Object... args) {
        return get(object.getClass(), key, args);
    }

    public static String get(Class<?> clazz, String key, Object... args) {
        return get(clazz.getName().toLowerCase(Locale.ENGLISH).replace("com.tianscar.carbonizedpixeldungeon.", "") + "." +
                key.toLowerCase(Locale.ENGLISH), args);
    }

}