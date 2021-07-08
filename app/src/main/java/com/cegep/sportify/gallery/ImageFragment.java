package com.cegep.sportify.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.cegep.sportify.R;

public class ImageFragment extends Fragment {

    private static final String KEY_IMAGE = "KEY_IMAGE";

    private ImageView imageView;

    public static ImageFragment newInstance(String image) {
        ImageFragment imageFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_IMAGE, image);
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        imageView = view.findViewById(R.id.imageView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String image = getArguments().getString(KEY_IMAGE);

        Glide.with(this)
                .load(image)
                .into(imageView);
    }
}
