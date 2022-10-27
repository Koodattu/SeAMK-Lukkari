package com.seamk.mobile;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.seamk.mobile.restaurant.RootFragmentRestaurants;
import com.seamk.mobile.setup.SetupActivity;
import com.seamk.mobile.studybasket.FragmentStudyBasket;
import com.seamk.mobile.timetable.FragmentTimetableGrid;
import com.seamk.mobile.timetable.RootFragmentSummary;
import com.seamk.mobile.util.FragmentUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Juha Ala-Rantala on 10.2.2017.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.frame) FrameLayout frameLayout;
    @BindView(R.id.adView) AdView mAdView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.drawer_layout) DrawerLayout drawer;
    @BindView(R.id.nav_view) NavigationView navigationView;
    @BindView(R.id.MainContentConstraintLayout) ConstraintLayout adContainer;

    private SharedPreferences sharedPreferences;
    ActionBar actionbar;

    int lastMenuId = -1;
    DrawerArrowDrawable drawerArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("ApplicationPreferences", 0);

        setSupportActionBar(toolbar);
        actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        drawerArrow = new DrawerArrowDrawable(this);
        drawerArrow.setColor(ContextCompat.getColor(this, R.color.pureWhite));
        actionbar.setHomeAsUpIndicator(drawerArrow);

        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
                    ObjectAnimator.ofFloat(drawerArrow, "progress", getSupportFragmentManager().getBackStackEntryCount() > 1 ? 1 : 0).start();
                    Handler h = new Handler();
                    h.postDelayed(() -> {
                        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                            navigationView.getMenu().getItem(0).setChecked(true);
                            lastMenuId = navigationView.getMenu().findItem(R.id.tiivistelma).getItemId();
                        }
                    }, 500);
                });

        navigationView.setNavigationItemSelectedListener(this);
        lastMenuId = navigationView.getMenu().findItem(R.id.tiivistelma).getItemId();

        Fragment fragment;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragment = new RootFragmentSummary();
        fragmentTransaction.replace(R.id.frame, fragment, "SUMMARY_FRAGMENT");
        fragmentTransaction.commit();

        navigationView.getMenu().getItem(0).setChecked(true);

        // close app in some specific circumstances??
        if (!isTaskRoot() && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER) && getIntent().getAction() != null && getIntent().getAction().equals(Intent.ACTION_MAIN)) {
            finish();
        }

        // clear all old settings for new version
        if (!sharedPreferences.getBoolean("v2reset", false)){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            editor.putBoolean("v2reset", true);
            editor.commit();
        }

        // start app intro setup activity
        if (!sharedPreferences.getBoolean("setupComplete", false)){
            Intent intent = new Intent(this, SetupActivity.class);
            startActivity(intent);
        }

        // load ads
        mAdView.loadAd(new AdRequest.Builder().build());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id != lastMenuId) {

            if (id == R.id.tiivistelma) {
                if (sharedPreferences.getBoolean("showAdverts", true)) {
                    mAdView.setVisibility(View.VISIBLE);
                } else {
                    mAdView.setVisibility(View.INVISIBLE);
                }
            } else {
                mAdView.setVisibility(View.INVISIBLE);
            }

            Fragment fragment;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                FragmentUtils.sDisableFragmentAnimations = true;
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            }

            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);

            switch (id) {
                case R.id.tiivistelma:
                    fragment = new RootFragmentSummary();
                    fragmentTransaction.replace(R.id.frame, fragment, "SUMMARY_FRAGMENT");
                    break;
                case R.id.timetable_view:
                    fragment = new FragmentTimetableGrid();
                    fragmentTransaction.replace(R.id.frame, fragment, "TIMETABLE_WEEK_FRAGMENT");
                    fragmentTransaction.addToBackStack("TAG");
                    break;
                case R.id.food_view:
                    fragment = new RootFragmentRestaurants();
                    fragmentTransaction.replace(R.id.frame, fragment, "RESTAURANTS_FRAGMENT");
                    fragmentTransaction.addToBackStack("TAG");
                    break;
                case R.id.find_view:
                    fragment = new FragmentFind();
                    fragmentTransaction.replace(R.id.frame, fragment, "SEARCH_FRAGMENT");
                    fragmentTransaction.addToBackStack("TAG");
                    break;
                case R.id.basket_view:
                    fragment = new FragmentStudyBasket();
                    fragmentTransaction.replace(R.id.frame, fragment, "BASKET_FRAGMENT");
                    fragmentTransaction.addToBackStack("TAG");
                    break;
                case R.id.more_view:
                    fragment = new FragmentExtras();
                    fragmentTransaction.replace(R.id.frame, fragment, "EXTRAS_FRAGMENT");
                    fragmentTransaction.addToBackStack("TAG");
                    break;
                case R.id.settings_view:
                    fragment = new FragmentSettings();
                    fragmentTransaction.replace(R.id.frame, fragment, "SETTINGS_FRAGMENT");
                    fragmentTransaction.addToBackStack("TAG");
                    break;
            }

            fragmentTransaction.commit();
            drawer.closeDrawer(GravityCompat.START);
            lastMenuId = id;
            FragmentUtils.sDisableFragmentAnimations = true;
        } else {
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    public void barLukkariOnClick(View view) {
        MenuItem item = navigationView.getMenu().getItem(1);
        navigationView.getMenu().getItem(0).setChecked(false);
        navigationView.setCheckedItem(R.id.timetable_view);
        onNavigationItemSelected(item);
    }

    public void barRuokalistaOnClick(View view) {
        MenuItem item = navigationView.getMenu().getItem(4);
        navigationView.getMenu().getItem(0).setChecked(false);
        navigationView.setCheckedItem(R.id.food_view);
        onNavigationItemSelected(item);
    }

    public void goToStudyBasket() {
        MenuItem item = navigationView.getMenu().getItem(2);
        navigationView.getMenu().getItem(0).setChecked(false);
        navigationView.setCheckedItem(R.id.basket_view);
        onNavigationItemSelected(item);
    }
}