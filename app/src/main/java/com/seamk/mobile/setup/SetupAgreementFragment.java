package com.seamk.mobile.setup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.appintro.SlidePolicy;
import com.seamk.mobile.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class SetupAgreementFragment extends Fragment implements SlidePolicy {

    @BindView(R.id.checkbox_accept_terms)
    CheckBox checkBox;
    @BindView(R.id.frame_layout_root)
    FrameLayout frameLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setup_agreement_fragment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public boolean isPolicyRespected() {
        return checkBox.isChecked();
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {
        // TODO turn into string res with translation
        Toasty.warning(requireContext(), "Accepts terms to continue", Toast.LENGTH_SHORT, true).show();
    }
}
