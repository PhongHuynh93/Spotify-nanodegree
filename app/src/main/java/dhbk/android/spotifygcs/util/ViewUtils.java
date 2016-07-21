package dhbk.android.spotifygcs.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Property;
import android.view.View;
import android.widget.ImageView;

import dhbk.android.spotifygcs.R;

/**
 * Created by phongdth.ky on 7/15/2016.
 */

/**
 * Utility methods for working with Views.
 */
public class ViewUtils {
    public static final Property<View, Integer> BACKGROUND_COLOR = new AnimUtils.IntProperty<View>("backgroundColor") {

        @Override
        public void setValue(View view, int value) {
            view.setBackgroundColor(value);
        }

        @Override
        public Integer get(View view) {
            Drawable d = view.getBackground();
            if (d instanceof ColorDrawable) {
                return ((ColorDrawable) d).getColor();
            }
            return Color.TRANSPARENT;
        }
    };

    // load image from url
    public static void setImagePicasso(Context context, String urlImage, ImageView imageView) {
        PicassoBigCache.INSTANCE.getPicassoBigCache(context).load(urlImage).fit().placeholder(R.drawable.no_artist).into(imageView);

//        Picasso.with(context)
//                .load(urlImage)
//                .fit()
//                .placeholder(R.drawable.no_artist)
//                .into(imageView);
    }


    /**
     * Determine if the navigation bar will be on the bottom of the screen, based on logic in
     * PhoneWindowManager.
     */
    public static boolean isNavBarOnBottom(@NonNull Context context) {
        final Resources res= context.getResources();
        final Configuration cfg = context.getResources().getConfiguration();
        final DisplayMetrics dm =res.getDisplayMetrics();
        boolean canMove = (dm.widthPixels != dm.heightPixels &&
                cfg.smallestScreenWidthDp < 600);
        return(!canMove || dm.widthPixels < dm.heightPixels);
    }

}
