package com.GeekyRaghavan.vijayportfolio.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.GeekyRaghavan.vijayportfolio.NetworkDetector;
import com.GeekyRaghavan.vijayportfolio.R;
import com.GeekyRaghavan.vijayportfolio.custom.CustomTextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class EducationFragment extends Fragment {

    boolean treeOrStone = false;
    ObjectAnimator animator;
    CustomTextView tv_education;
    ImageView img_sun;
    Animation bottomUp;
    ImageView img_bus;
    ObjectAnimator cloudAnimator;
    private String educationRestCall;
    private final String SAVE_STATE = "savestate";

    public EducationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_education, container, false);
        final ImageView img_tree = view.findViewById(R.id.img_tree);
        ImageView img_cloud = view.findViewById(R.id.img_cloud);
        img_bus = view.findViewById(R.id.img_bus);
        img_sun = view.findViewById(R.id.img_sun);
        int nightModeFlags =
                getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                img_sun.setImageResource(R.drawable.ic_moon);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                img_sun.setImageResource(R.drawable.ic_sun);
                break;

        }
        tv_education = view.findViewById(R.id.customTextView_education);
        tv_education.setCharacterDelay(150);
        NetworkDetector detector = new NetworkDetector(getContext());
        if (detector.isNetworkAvailable()) {
            if (savedInstanceState == null) {
                setEducationTextView();
            }else {
                educationRestCall = savedInstanceState.getString(SAVE_STATE);
                tv_education.animateText(educationRestCall);
            }
        } else {
            tv_education.animateText(getString(R.string.internet_not_available));
        }
//        tv_education.animateText("I have completed my B.E in Sri SaiRam Institue Of Technology");

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        cloudAnimator = ObjectAnimator.ofFloat(img_cloud, "x", -150f, width);
        cloudAnimator.setDuration(8000);
        cloudAnimator.setRepeatCount(ValueAnimator.INFINITE);
        cloudAnimator.setRepeatMode(ValueAnimator.RESTART);


        /*ObjectAnimator busAnimator = ObjectAnimator.ofFloat(img_bus, "y", 402f, 407f);
        busAnimator.setDuration(500);
        busAnimator.setRepeatCount(ValueAnimator.INFINITE);
        busAnimator.setRepeatMode(ValueAnimator.REVERSE);
        busAnimator.start();*/

        bottomUp = AnimationUtils.loadAnimation(getContext(), R.anim.shake);


        animator = ObjectAnimator.ofFloat(img_tree, "x", -300f, width);
        animator.setDuration(5000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                if (!treeOrStone) {
                    img_tree.setImageResource(R.drawable.ic_sign);
                    img_tree.requestLayout();
                    img_tree.getLayoutParams().height = dpToPx(140);
                    img_tree.setElevation(1.0f);
                } else {
                    img_tree.setImageResource(R.drawable.ic_tree);
                    img_tree.requestLayout();
                    img_tree.getLayoutParams().height = dpToPx(200);
                    img_tree.setElevation(0.0f);
                }

                treeOrStone = !treeOrStone;
            }
        });


        return view;
    }

    public int dpToPx(int dp) {
        float density = getContext().getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    @Override
    public void onStop() {
        super.onStop();
        animator.removeAllListeners();
    }

    private void setEducationTextView() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("education")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.e("Vijay", document.getId() + " => " + document.getData());
                                Map<String, Object> keyValue = document.getData();
                                if (keyValue.containsKey("description")) {
                                    Log.e("raghavan", String.valueOf(keyValue.get("description")));
                                    educationRestCall = String.valueOf(keyValue.get("description"));
                                    tv_education.animateText(educationRestCall);
                                }
                            }
                        } else {
                            Log.e("Vijay", "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        img_bus.startAnimation(bottomUp);
        cloudAnimator.start();
        animator.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        bottomUp.cancel();
        cloudAnimator.cancel();
        animator.cancel();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (educationRestCall!=null)
            outState.putString(SAVE_STATE, educationRestCall);
        super.onSaveInstanceState(outState);
    }
}
