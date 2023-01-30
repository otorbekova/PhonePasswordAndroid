package com.example.a2month_navigation_lesson1;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentResultListener;
import androidx.navigation.NavController;
import androidx.navigation.NavController.OnDestinationChangedListener;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    private AppBarConfiguration mAppBarConfiguration;
    private NavController navController;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private Prefs prefs;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        imageView=findViewById(R.id.imageView08);
        //ImageView imageView11=findViewById(R.id.imag
        //        fab = findViewById(R.id.fab);
        //        fab.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View view) {
        //                navController.navigate(R.id.formFragment);
        //            }
        //        });eView_5588);


        initNavController();// функция

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);//TOOLS

        boolean isShown = new Prefs(this).isShown();// if! find isshown to false
        if (!isShown)
            navController.navigate(R.id.bordFragment);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) { //если нет пользователей
            navController.navigate((R.id.phoneFragment));
        }
        initFile();//ponyat chto takoe file

    }

    private void initFile() {
        //будет файл и папки
        File folder=new File(Environment.getExternalStorageDirectory(),"TasApp");//prosto creted class
        folder.mkdir(); // make directory or papka  группа папок можно созать несколько папкок
        File file=new File(folder,"note.txt");
        try { //нужно поймать
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initNavController() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.close(); // close shtorka

                navController.navigate(R.id.profileFragment);
            }
        });
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_firestore)
                .setDrawerLayout(drawer)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(new OnDestinationChangedListener() { //показывает текущий фрагмент onDestinationChanged
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.nav_home) {
                    fab.show();
                } else {
                    fab.hide();
                }

                if (destination.getId() == R.id.bordFragment || destination.getId() == R.id.phoneFragment) {
                    toolbar.setVisibility(View.GONE); // ушел

                } else {
                    toolbar.setVisibility(View.VISIBLE);// видить
                }
            }
        });
        //getSupportFragmentManager().setFragmentResultListener("w",this, );
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}