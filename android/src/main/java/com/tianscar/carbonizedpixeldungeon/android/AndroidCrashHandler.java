/*
 *
 *  * Copyright © 2018-19 Rohit Sahebrao Surwase.
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */
package com.tianscar.carbonizedpixeldungeon.android;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AndroidCrashHandler extends ContentProvider {

    static final String EXTRA_STACK_TRACE = "EXTRA_STACK_TRACE";

    private final static String TAG = "AndroidCrashHandler";

    private static final String CUSTOM_HANDLER_PACKAGE_NAME = "com.tianscar.carbonizedpixeldungeon.android";
    private static final String DEFAULT_HANDLER_PACKAGE_NAME = "com.android.internal.os";
    private static final int MAX_STACK_TRACE_SIZE = 131071; //128 KB - 1

    private static final String PREFERENCES_NAME = "crash_handler";
    private static final String KEY_LAST_CRASH_TIME = "last_crash_time";

    private static boolean inBackground = true;
    private static WeakReference<Activity> lastActivityCreated = new WeakReference<>(null);

    public static void install(Context context) {
        try {
            if (context != null) {
                Thread.UncaughtExceptionHandler oldHandler = Thread.getDefaultUncaughtExceptionHandler();
                if (oldHandler != null && oldHandler.getClass().getName().startsWith(CUSTOM_HANDLER_PACKAGE_NAME)) {
                    Log.e(TAG, "AndroidCrashHandler was already installed, doing nothing!");
                } else {
                    if (oldHandler != null && !oldHandler.getClass().getName().startsWith(DEFAULT_HANDLER_PACKAGE_NAME)) {
                        Log.e(TAG, "You already have an UncaughtExceptionHandler. If you use a custom UncaughtExceptionHandler, it should be initialized after AndroidCrashHandler! Installing anyway, but your original handler will not be called.");
                    }
                    Application application = (Application) context.getApplicationContext();
                    //Setup UCE Handler.
                    Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                        @Override
                        public void uncaughtException(Thread thread, Throwable throwable) {
                            Log.e(TAG, "App crashed, executing AndroidCrashHandler's UncaughtExceptionHandler", throwable);
                            if (hasCrashedInTheLastSeconds(application)) {
                                Log.e(TAG, "App already crashed recently, not starting custom error activity because we could enter a restart loop. Are you sure that your app does not crash directly on init?", throwable);
                                if (oldHandler != null) {
                                    oldHandler.uncaughtException(thread, throwable);
                                    return;
                                }
                            } else {
                                setLastCrashTime(application, new Date().getTime());
                                if (!inBackground) {
                                    final Intent intent = new Intent(application, AndroidCrashActivity.class);
                                    StringWriter sw = new StringWriter();
                                    PrintWriter pw = new PrintWriter(sw);
                                    throwable.printStackTrace(pw);
                                    String stackTraceString = sw.toString();
                                    if (stackTraceString.length() > MAX_STACK_TRACE_SIZE) {
                                        String disclaimer = " [stack trace too large]";
                                        stackTraceString = stackTraceString.substring(0, MAX_STACK_TRACE_SIZE - disclaimer.length()) + disclaimer;
                                    }
                                    intent.putExtra(EXTRA_STACK_TRACE, stackTraceString);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    application.startActivity(intent);
                                } else {
                                    if (oldHandler != null) {
                                        oldHandler.uncaughtException(thread, throwable);
                                        return;
                                    }
                                    //If it is null (should not be), we let it continue and kill the process or it will be stuck
                                }
                            }
                            Activity lastActivity = lastActivityCreated.get();
                            if (lastActivity != null) {
                                lastActivity.finish();
                                lastActivityCreated.clear();
                            }
                            killCurrentProcess();
                        }
                    });
                    application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                        int currentlyStartedActivities = 0;
                        @Override
                        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                            if (activity.getClass() != AndroidCrashActivity.class) {
                                lastActivityCreated = new WeakReference<>(activity);
                            }
                        }
                        @Override
                        public void onActivityStarted(Activity activity) {
                            currentlyStartedActivities ++;
                            inBackground = (currentlyStartedActivities == 0);
                        }
                        @Override
                        public void onActivityResumed(Activity activity) {}
                        @Override
                        public void onActivityPaused(Activity activity) {}
                        @Override
                        public void onActivityStopped(Activity activity) {
                            currentlyStartedActivities--;
                            inBackground = (currentlyStartedActivities == 0);
                        }
                        @Override
                        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                        }
                        @Override
                        public void onActivityDestroyed(Activity activity) {}
                    });
                }
                Log.i(TAG, "AndroidCrashHandler has been installed.");
            } else {
                Log.e(TAG, "Context can not be null");
            }
        } catch (Throwable throwable) {
            Log.e(TAG, "AndroidCrashHandler can not be initialized.", throwable);
        }
    }

    /**
     * INTERNAL method that tells if the app has crashed in the last seconds.
     * This is used to avoid restart loops.
     *
     * @return true if the app has crashed in the last seconds, false otherwise.
     */
    private static boolean hasCrashedInTheLastSeconds(Context context) {
        long lastTime = getLastCrashTime(context);
        long currentTime = new Date().getTime();
        return (lastTime <= currentTime && currentTime - lastTime < 3000);
    }

    @SuppressLint("ApplySharedPref")
    private static void setLastCrashTime(Context context, long time) {
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit().putLong(KEY_LAST_CRASH_TIME, time).commit();
    }

    private static void killCurrentProcess() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    private static long getLastCrashTime(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).getLong(KEY_LAST_CRASH_TIME, -1);
    }

    private static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        }
        catch (PackageManager.NameNotFoundException e) {
            return "???";
        }
    }

    private static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        }
        catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    /**
     * Given an Intent, returns several error details including the stack trace extra from the intent.
     *
     * @param context A valid context. Must not be null.
     * @param intent  The Intent. Must not be null.
     * @return The full error details.
     */
    public static String getErrorDetails(Context context, Intent intent) {

        //Added a space between line feeds to fix CustomActivityOnCrash#18.
        //Ideally, we should not use this method at all... It is only formatted this way because of coupling with the default error activity.
        //We should move it to a method that returns a bean, and let anyone format it as they wish.

        return context.getString(R.string.version_is) + "v" + getVersionName(context) + " (" + getVersionCode(context) + ") \n \n" +
                "Android " + Build.VERSION.RELEASE + " (SDK " + Build.VERSION.SDK_INT + ") \n \n" +
                intent.getStringExtra(EXTRA_STACK_TRACE);
    }

    static void closeApplication(Activity activity) {
        activity.finish();
        killCurrentProcess();
    }

    static void restartApplication(Activity activity, Class<? extends Activity> restart) {
        Intent intent = new Intent(activity, restart);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        if (intent.getComponent() != null) {
            //If the class name has been set, we force it to simulate a Launcher launch.
            //If we don't do this, if you restart from the error activity, then press home,
            //and then launch the activity from the launcher, the main activity appears twice on the backstack.
            //This will most likely not have any detrimental effect because if you set the Intent component,
            //if will always be launched regardless of the actions specified here.
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
        }
        activity.finish();
        activity.startActivity(intent);
        killCurrentProcess();
    }

    @Override
    public boolean onCreate() {
        install(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

}
