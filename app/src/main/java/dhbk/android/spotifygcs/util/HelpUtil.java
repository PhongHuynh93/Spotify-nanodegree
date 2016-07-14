package dhbk.android.spotifygcs.util;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.text.Html;
import android.text.Spanned;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by phongdth.ky on 7/13/2016.
 * constains static constain and static methods
 */
public class HelpUtil {
    public static final String TAG_FRAGMENT_SEARCH_ARTISTS = "search_artist_fragment";

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
}
