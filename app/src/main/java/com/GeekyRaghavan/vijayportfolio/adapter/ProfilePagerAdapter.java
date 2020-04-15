package com.GeekyRaghavan.vijayportfolio.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.GeekyRaghavan.vijayportfolio.fragment.EducationFragment;
import com.GeekyRaghavan.vijayportfolio.fragment.JobFragment;
import com.GeekyRaghavan.vijayportfolio.fragment.ProfileFragment;
import com.GeekyRaghavan.vijayportfolio.fragment.ProjectsFragment;

public class ProfilePagerAdapter extends FragmentStateAdapter {


    public ProfilePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = ProfileFragment.newInstance(position);
                break;
            case 1:
                fragment = new EducationFragment();
                break;
            case 2:
                fragment = new JobFragment();
                break;
            case 3:
                fragment = new ProjectsFragment();
                break;

        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
