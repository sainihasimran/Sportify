package com.cegep.sportify;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.cegep.sportify.checkout.ShippingActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SettingsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.user_profile_text).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ProfileActivity.class);
            requireActivity().startActivity(intent);
        });

        view.findViewById(R.id.saved_address_text).setOnClickListener(v -> {
            Intent intent = ShippingActivity.getCallingIntent(requireContext(), false);
            requireActivity().startActivity(intent);
        });

        view.findViewById(R.id.saved_card_address).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SavedCardActivity.class);
            requireActivity().startActivity(intent);
        });

        view.findViewById(R.id.reset_password_text).setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), ResetPasswordActivity.class);
            requireActivity().startActivity(intent);
        });

        Button button = view.findViewById(R.id.sign_out_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(requireActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                requireActivity().finish();
            }
        });
    }
}