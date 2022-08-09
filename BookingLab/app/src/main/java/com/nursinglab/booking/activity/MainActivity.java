package com.nursinglab.booking.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.nursinglab.booking.R;
import com.nursinglab.booking.databinding.ActivityMainBinding;
import com.nursinglab.booking.fragment.AllBookingFragment;
import com.nursinglab.booking.fragment.MyBookingFragment;
import com.nursinglab.booking.fragment.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private int getMenuChecked;
    private ActivityMainBinding binding;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.bottomNavigation.setOnNavigationItemSelectedListener(navBar);
        binding.bottomNavigation.getMenu().findItem(R.id.nav_my_booking).setChecked(true); // make checked Home Fragment
        //bottomNav.getMenu().findItem(R.id.nav_home).setEnabled(false); // make unchecked Home Fragment

        loadFragment(new MyBookingFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navBar = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectFrag;

            getMenuChecked = menuItem.getItemId();
            for (int i=0; i<binding.bottomNavigation.getMenu().size(); i++){
                MenuItem menuT = binding.bottomNavigation.getMenu().getItem(i);
                //boolean isEnable = menuT.getItemId() != getMenuChecked;
                boolean isChecked = menuT.getItemId() == getMenuChecked;
                //menuT.setEnabled(isEnable);
                menuT.setChecked(isChecked);
            }

            switch (menuItem.getItemId()){
                case R.id.nav_my_booking:
                    selectFrag = new MyBookingFragment();
                    loadFragment(selectFrag);
                    binding.toolbar.setTitle(R.string.app_name);
                    return true;
                case R.id.nav_all_booking:
                    selectFrag = new AllBookingFragment();
                    loadFragment(selectFrag);
                    binding.toolbar.setTitle("All Booking");
                    return true;
                case R.id.nav_olahraga:
                    selectFrag = new ProfileFragment();
                    loadFragment(selectFrag);
                    binding.toolbar.setTitle("Profile");
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_content, fragment);
        fragmentTransaction.commit();
    }
}
