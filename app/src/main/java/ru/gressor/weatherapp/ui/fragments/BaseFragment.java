package ru.gressor.weatherapp.ui.fragments;

import android.content.Context;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

public class BaseFragment extends Fragment {
    public static final String DOWNLOAD_IMAGE_URL = "https://openweathermap.org/img/wn/%s@2x.png";

    public void setDrawableByFileName(ImageView imageView, Context context, String imageFileName) {
        if (imageFileName != null) {
            String drawableName = context.getPackageName() + ":drawable/w" + imageFileName;
            int iconId = context.getResources()
                    .getIdentifier(drawableName, null, null);

            if (iconId == 0)
                iconId = android.R.drawable.ic_menu_view;

            Picasso.get()
                    .load(String.format(DOWNLOAD_IMAGE_URL, imageFileName))
                    .placeholder(iconId)
                    .error(iconId)
                    .into(imageView);
        }
    }
}
