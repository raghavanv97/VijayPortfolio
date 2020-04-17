package com.GeekyRaghavan.vijayportfolio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.GeekyRaghavan.vijayportfolio.adapter.ProfilePagerAdapter;
import com.GeekyRaghavan.vijayportfolio.adapter.ProjectsRecyclerViewAdapter;
import com.GeekyRaghavan.vijayportfolio.fragment.EducationFragment;
import com.GeekyRaghavan.vijayportfolio.fragment.JobFragment;
import com.GeekyRaghavan.vijayportfolio.fragment.ProfileFragment;
import com.GeekyRaghavan.vijayportfolio.fragment.ProjectsFragment;
import com.GeekyRaghavan.vijayportfolio.viewpagertransformer.DepthPageTransformer;
import com.GeekyRaghavan.vijayportfolio.viewpagertransformer.ZoomOutPageTransformer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.FirebaseApp;

import static com.GeekyRaghavan.vijayportfolio.Constants.DAY_MODE;
import static com.GeekyRaghavan.vijayportfolio.Constants.DEFAULT_MODE;
import static com.GeekyRaghavan.vijayportfolio.Constants.NIGHT_MODE;
import static com.GeekyRaghavan.vijayportfolio.Constants.NIGHT_MODE_SP;
import static com.GeekyRaghavan.vijayportfolio.Constants.SHARED_PREFERENCE;

public class MainActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager;
    TabLayout tabLayout;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    FloatingActionButton fab;

    private static final String TAG_FRAGMENT_ONE = "fragment_one";
    private static final String TAG_FRAGMENT_TWO = "fragment_two";
    private static final String TAG_FRAGMENT_THREE = "fragment_three";
    private static final String TAG_FRAGMENT_FOUR = "fragment_four";
    private static final String TAG_SAVED_FRAGMENT = "saved_fragment";

    private Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        FirebaseApp.initializeApp(this);



        drawerLayout = findViewById(R.id.navigation_drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        int nightModeFlags =
                getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                fab.setImageDrawable(getDrawable(R.drawable.ic_menu_white));
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                fab.setImageDrawable(getDrawable(R.drawable.ic_menu_black));
                break;

        }


        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_setting:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                        startActivity(intent);
//                        Toast.makeText(MainActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }
        });


        /*viewPager = findViewById(R.id.profile_viewpager);
        viewPager.requestDisallowInterceptTouchEvent(true);
        ProfilePagerAdapter adapter = new ProfilePagerAdapter(this);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.tablayout);
        new TabLayoutMediator(tabLayout, viewPager,(tab, position)  -> tab.setText("")).attach();
*/


        bottomNavigationView = findViewById(R.id.bottom_navigation);
//        bottomNavigationView.inflateMenu(R.menu.bottom_navigation_menu);
        bottomNavigationView.setItemIconTintList(null);


        fragmentManager = getSupportFragmentManager();
        Fragment fragment;
        if (savedInstanceState!=null){
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, TAG_SAVED_FRAGMENT);
            currentFragment = fragment;
        }else {
            fragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT_ONE);
            if (fragment == null) {
                fragment = new ProfileFragment();
            }
            replaceFragment(fragment, TAG_FRAGMENT_ONE);
        }


        bottomNavigationView.setSelectedItemId(R.id.menu_profile);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.menu_profile: {
                        Fragment fragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT_ONE);
                        if (fragment == null) {
                            fragment = new ProfileFragment();
                        }
                        replaceFragment(fragment, TAG_FRAGMENT_ONE);

                    }
                        break;

                    case R.id.menu_education: {
                        Fragment fragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT_TWO);
                        if (fragment == null) {
                            fragment = new EducationFragment();
                        }
                        replaceFragment(fragment, TAG_FRAGMENT_TWO);
                        break;
                    }
                    case R.id.menu_job: {
                        Fragment fragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT_THREE);
                        if (fragment == null) {
                            fragment = new JobFragment();
                        }
                        replaceFragment(fragment, TAG_FRAGMENT_THREE);
                        break;
                    }
                    case R.id.menu_setting: {
                        Fragment fragment = fragmentManager.findFragmentByTag(TAG_FRAGMENT_FOUR);
                        if (fragment == null) {
                            fragment = new ProjectsFragment();
                        }
                        replaceFragment(fragment, TAG_FRAGMENT_FOUR);
                        break;
                    }
                }

                return true;
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (toggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        /*else if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }*/
        else {
            super.onBackPressed();
        }
    }


    private void replaceFragment(@NonNull Fragment fragment, @NonNull String tag) {
        if (!fragment.equals(currentFragment)) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.framelayout, fragment, tag)
//                    .addToBackStack(null)
                    .commit();
            currentFragment = fragment;
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, TAG_SAVED_FRAGMENT, currentFragment);
    }
}
