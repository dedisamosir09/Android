package com.dedi.finalprojectdedi.controller.publics;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.dedi.finalprojectdedi.R;
import com.dedi.finalprojectdedi.utils.MySession;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private MySession mySession;
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
            getSupportActionBar().setElevation(0);
        }

        mySession = new MySession(this);




        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            fragment = null;
            int id = item.getItemId();
            if (id == R.id.frHome){
                fragment = new HomeFragment();
            } else if (id == R.id.ftTicket){
                fragment = new TicketFragment();
            } else if (id == R.id.frProfile){
                fragment = new ProfileFragment();
            }
//            else if (id == R.id.frLogout) {
//                logout();
//            }
            return loadFragment(fragment);
        });

        if(getIntent().getIntExtra("ticketFragment",0) == 1){
            loadFragment(new TicketFragment());
        } else {
            loadFragment(new HomeFragment());
        }
    }

    private boolean loadFragment(Fragment fragment){
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(
                            R.anim.slide_in,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.slide_out
                    )
                    .replace(R.id.main_frame, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logout() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Logout Akun");
        builder.setMessage("Apakah anda yakin ingin logout dari aplikasi ini?");
        builder.setPositiveButton("ya",
                (dialog, which) ->  {
                    mySession.logoutUser();
                    dialog.dismiss();
                    finish();
                });
        builder.setNegativeButton("tidak",
                (dialog, which) -> dialog.dismiss());
        builder.setCancelable(false);
        builder.show();
    }

    private void closeApp() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Menutup Aplikasi");
        builder.setMessage("Apakah anda ingin keluar dari aplikasi ini?");
        builder.setPositiveButton("ya",
                (dialog, which) ->  {
                    dialog.dismiss();
                    finishAffinity();
                });
        builder.setNegativeButton("tidak",
                (dialog, which) -> dialog.dismiss());
        builder.setCancelable(false);
        builder.show();
    }

    @Override
    public void onBackPressed() {
        closeApp();
    }
}
