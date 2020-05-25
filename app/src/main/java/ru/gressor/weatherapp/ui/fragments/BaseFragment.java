package ru.gressor.weatherapp.ui.fragments;

import android.content.Context;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    public void setDrawableByFileName(ImageView imageView, Context context, String imageFileName) {
        if (imageFileName != null) {
            String drawableName = context.getPackageName() + ":drawable/w" + imageFileName;
            int iconId = context.getResources()
                    .getIdentifier(drawableName, null, null);

            if (iconId != 0) {
                imageView.setImageDrawable(context.getDrawable(iconId));
            }
        }
    }
}
