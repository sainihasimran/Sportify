package com.cegep.sportify.gallery;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.List;

public class ImageAdapter extends FragmentPagerAdapter {

    private final List<String> images;

    public ImageAdapter(@NonNull FragmentManager fm, List<String> images) {
        super(fm);
        this.images = images;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return ImageFragment.newInstance(images.get(position));
    }

    @Override
    public int getCount() {
        return images.size();
    }
}
