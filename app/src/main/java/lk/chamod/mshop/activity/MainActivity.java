package lk.chamod.mshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import lk.chamod.mshop.R;
import lk.chamod.mshop.databinding.ActivityMainBinding;
import lk.chamod.mshop.databinding.NavItemHeaderBinding;
import lk.chamod.mshop.fragment.cartFragment;
import lk.chamod.mshop.fragment.categoryFragment;
import lk.chamod.mshop.fragment.homeFragment;
import lk.chamod.mshop.fragment.loginFragment;
import lk.chamod.mshop.fragment.messageFragment;
import lk.chamod.mshop.fragment.orderFragment;
import lk.chamod.mshop.fragment.profileFragment;
import lk.chamod.mshop.fragment.settingFragment;
import lk.chamod.mshop.fragment.watchlistFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener {


    private ActivityMainBinding binding;
    private NavItemHeaderBinding navItemHeaderBinding;
    private DrawerLayout drawerLayout;

    private MaterialToolbar toolbar;

    private NavigationView navigationView;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


binding=ActivityMainBinding.inflate(getLayoutInflater());
setContentView(binding.getRoot());

View headerView =binding.sideNavigationView.getHeaderView(0);
navItemHeaderBinding =NavItemHeaderBinding.bind(headerView);
//navItemHeaderBinding.


        drawerLayout = binding.draowerLayout;
        toolbar =binding.toolbar;
        navigationView = binding.sideNavigationView;
        bottomNavigationView = binding.bottmNavigationView;

        setSupportActionBar(toolbar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    finish();

                }

            }
        });

        navigationView.setNavigationItemSelectedListener(this);
//        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                return false;
//            }
//        });
// මෙය භාවිතා කරන්න
        bottomNavigationView.setOnItemSelectedListener(this);
        if (savedInstanceState == null) {

            loadFragment(new homeFragment());
            navigationView.getMenu().findItem(R.id.side_nav_home).setChecked(true);


            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_home).setChecked(true);

        }


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();

       Menu navmenu = navigationView.getMenu();
      Menu bottomnavmenu = bottomNavigationView.getMenu();

      for (int i=0; i<navmenu.size(); i++){

          navmenu.getItem(i).setChecked(true);
      }

      for (int i=0; i<bottomnavmenu.size(); i++){
          bottomnavmenu.getItem(i).setChecked(false);

      }



        navigationView.setCheckedItem(-1);
        if (itemId == R.id.side_nav_home || itemId == R.id.bottom_nav_home) {
            loadFragment(new homeFragment());

           navigationView.getMenu().findItem(R.id.side_nav_home).setChecked(true);


            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_home).setChecked(true);


        } else if (itemId == R.id.side_nav_profile || itemId == R.id.bottom_nav_profile) {
            loadFragment(new profileFragment());


            navigationView.getMenu().findItem(R.id.side_nav_profile).setChecked(true);
            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_profile).setChecked(true);


        } else if (itemId == R.id.side_nav_order) {
            loadFragment(new orderFragment());

            navigationView.getMenu().findItem(R.id.side_nav_order).setChecked(true);
        } else if (itemId == R.id.side_nav_watchlist) {
            loadFragment(new watchlistFragment());

            navigationView.getMenu().findItem(R.id.side_nav_order).setChecked(true);
        } else if (itemId == R.id.side_nav_cart || itemId == R.id.bottom_nav_cart) {
            loadFragment(new cartFragment());
            navigationView.getMenu().findItem(R.id.side_nav_cart).setChecked(true);
            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_cart).setChecked(true);

        } else if (itemId == R.id.side_nav_message) {
            loadFragment(new messageFragment());
            navigationView.getMenu().findItem(R.id.side_nav_message).setChecked(true);
        } else if (itemId == R.id.side_nav_setting) {
            loadFragment(new settingFragment());
            navigationView.getMenu().findItem(R.id.side_nav_setting).setChecked(true);
        } else if (itemId == R.id.bottom_nav_category) {
            loadFragment(new categoryFragment());
            bottomNavigationView.getMenu().findItem(R.id.bottom_nav_category).setChecked(true);
        } else if (itemId == R.id.side_nav_login) {
      Intent intent =new Intent(MainActivity.this, SignInActivity.class);
      startActivity(intent);
        } else if (itemId == R.id.side_nav_logout) {

        }

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START);
        }
        return true;

    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment);
    }
}