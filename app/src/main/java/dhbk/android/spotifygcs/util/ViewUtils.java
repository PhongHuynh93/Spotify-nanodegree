package dhbk.android.spotifygcs.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Property;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

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
        Picasso.with(context)
                .load(urlImage)
                .fit()
                .placeholder(R.drawable.no_artist)
                .into(imageView);
    }
}
