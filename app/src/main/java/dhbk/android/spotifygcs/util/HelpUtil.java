package dhbk.android.spotifygcs.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.Html;
import android.text.Spanned;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by phongdth.ky on 7/13/2016.
 * contain help methods
 */
public class HelpUtil {
    @NonNull
    public static Spanned getSpannedText(@NonNull Context context, @StringRes int stringRes) {
        checkNotNull(context);
        context = context.getApplicationContext();
        Spanned formattedText;
        // check because in android N, Html.fromHtml is deprecated
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            formattedText = Html.fromHtml(context.getString(stringRes), Html.FROM_HTML_MODE_LEGACY);
        } else {
            formattedText = Html.fromHtml(context.getString(stringRes));
        }
        return formattedText;
    }

    // translate milisecond to format minute:second
    public static String transformMilisecond(int millis) {
        int minutes = (millis / 1000) / 60;
        int seconds = (millis / 1000) % 60;
        return minutes + ":" + seconds;
    }


    public static boolean isServiceRunning(Class<?> serviceClass,
                                           Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        boolean isServiceRunning = false;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                isServiceRunning = true;
            }
        }
        return isServiceRunning;
    }
}
