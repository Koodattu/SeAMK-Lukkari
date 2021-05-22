package com.seamk.mobile;

import androidx.fragment.app.Fragment;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.seamk.mobile.util.FragmentUtils;

/**
 * Created by Jupe Danger on 29.3.2018.
 */

public class UtilityFragment extends Fragment {
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (FragmentUtils.sDisableFragmentAnimations && !enter) {
            return AnimationUtils.loadAnimation(getActivity(), R.anim.clear_stack_exit);
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }
}
