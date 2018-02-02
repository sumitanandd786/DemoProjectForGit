package com.demoproject.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

public class BlankFragment extends Fragment {
    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.blank, container, false);
    }

    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        Animation anim = super.onCreateAnimation(transit, enter, nextAnim);
        if (anim == null && nextAnim != 0) {
            anim = AnimationUtils.loadAnimation(getActivity(), nextAnim);
        }
        if (anim != null) {
            anim.setAnimationListener(new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    BlankFragment.this.onAnimationStarted();
                }

                public void onAnimationEnd(Animation animation) {
                    BlankFragment.this.onAnimationEnded();
                }

                public void onAnimationRepeat(Animation animation) {
                    BlankFragment.this.onAnimationRepeated();
                }
            });
        }
        return anim;
    }

    protected void onAnimationStarted() {
    }

    protected void onAnimationEnded() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fm.findFragmentById(R.id.fragment));
        ft.commit();
    }

    protected void onAnimationRepeated() {
    }
}
