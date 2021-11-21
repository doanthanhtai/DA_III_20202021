package com.example.tomtep;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tomtep.adapter.MainViewPagerAdapter;
import com.example.tomtep.dialog.NewLakeDialog;
import com.example.tomtep.dialog.NewProductDailog;
import com.example.tomtep.model.Ao;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ViewPager2 viewPager2;
    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        settupToolbar();
        setEvent();

    }

    //    //Cài đặt hiển thị toolbar
    private void settupToolbar() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.main_navigation_drawer_open, R.string.main_navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setEvent() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.bnav_lake) {
                viewPager2.setCurrentItem(0);
            } else if (id == R.id.bnav_diet) {
                viewPager2.setCurrentItem(1);
            } else if (id == R.id.bnav_product) {
                viewPager2.setCurrentItem(2);
            } else if (id == R.id.bnav_statistics) {
                viewPager2.setCurrentItem(3);
            }
            return true;
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    default:
                        bottomNavigationView.getMenu().findItem(R.id.bnav_lake).setChecked(true);
                        onCreateOptionsMenu(toolbar.getMenu());
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.bnav_diet).setChecked(true);
                        onCreateOptionsMenu(toolbar.getMenu());
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.bnav_product).setChecked(true);
                        onCreateOptionsMenu(toolbar.getMenu());
                        break;
                    case 3:
                        bottomNavigationView.getMenu().findItem(R.id.bnav_statistics).setChecked(true);
                        onCreateOptionsMenu(toolbar.getMenu());
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (viewPager2.getCurrentItem() == 0) {
            toolbar.getMenu().clear();
            getMenuInflater().inflate(R.menu.lake_toolbar_menu, menu);
        } else if (viewPager2.getCurrentItem() == 1) {
            toolbar.getMenu().clear();
        } else if (viewPager2.getCurrentItem() == 2) {
            toolbar.getMenu().clear();
            getMenuInflater().inflate(R.menu.product_toolbar_menu, menu);
        } else if (viewPager2.getCurrentItem() == 3) {
            toolbar.getMenu().clear();
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.lake_new) {
            new NewLakeDialog(this).show();
        } else if (id == R.id.product_new) {
            new NewProductDailog(this).show();
        } else if (id == R.id.product_enterquantity) {

        }
        return true;
    }

    private void initView() {

        navigationView = findViewById(R.id.main_navigationview);
        toolbar = findViewById(R.id.main_toolbar);
        TextView tvEmail = navigationView.getHeaderView(0).findViewById(R.id.navigation_tv_email);
        drawerLayout = findViewById(R.id.main_drawlayout);
        viewPager2 = findViewById(R.id.main_view_pager2);
        bottomNavigationView = findViewById(R.id.main_bottom_navigationview);

        //Set email cho tv email trong navigation
        tvEmail.setText(String.valueOf(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()));

        //Cài đặt hiển thị cho viewpager2
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(this);
        viewPager2.setAdapter(mainViewPagerAdapter);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_signout) {
            singOut();
        } else if (item.getItemId() == R.id.nav_change_password) {
            clickToChangePassword();
        } else if (item.getItemId() == R.id.nav_delete_account) {
            deleteAccount();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void clickToChangePassword() {
        startActivity(new Intent(MainActivity.this, ChangePasswordActivity.class));
    }

    private void singOut() {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(MainActivity.this, SignInActivity.class));
    }

    private void deleteAccount() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.all_title_dialogconfirmdelete)
                .setMessage(getResources().getText(R.string.main_message_confirmdeleteaccount) + firebaseAuth.getCurrentUser().getEmail())
                .setPositiveButton(getResources().getText(R.string.all_button_agree_text), (dialogInterface, i) -> {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("TaiKhoan/" + firebaseAuth.getCurrentUser().getUid());
                    firebaseAuth.getCurrentUser().delete().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            databaseReference.child("daXoa").setValue(true);
                            Toast.makeText(MainActivity.this, getResources().getText(R.string.main_toast_deleteaccountsuccess), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MainActivity.this, SignInActivity.class));
                            finishAffinity();
                        } else {
                            Toast.makeText(MainActivity.this, getResources().getText(R.string.main_toast_deleteaccountfail), Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton(getResources().getText(R.string.all_button_cancel_text), (dialogInterface, i) -> {
                });
        builder.create().show();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}