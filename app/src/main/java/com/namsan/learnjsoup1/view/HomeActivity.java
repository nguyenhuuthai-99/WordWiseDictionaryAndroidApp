package com.namsan.learnjsoup1.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.namsan.learnjsoup1.R;
import com.namsan.learnjsoup1.controller.DictionaryFragment;
import com.namsan.learnjsoup1.controller.HomeFragment;
import com.namsan.learnjsoup1.controller.VocabularyFragment;

public class HomeActivity extends AppCompatActivity {

    FrameLayout fragmentHolder;

    HomeFragment homeFragment;

    DictionaryFragment dictionaryFragment;
    VocabularyFragment vocabularyFragment;

    Fragment activeFragment;

    Intent intent;

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();



        String word=null;

        Intent getIntent = getIntent();

        word = getIntent.getStringExtra("inputSearch");

        Bundle bundle = new Bundle();
        bundle.putString("inputSearch", word);

        fragmentHolder = findViewById(R.id.fragment_holder);


        dictionaryFragment = new DictionaryFragment();
        dictionaryFragment.setArguments(bundle);
        homeFragment = new HomeFragment();
        vocabularyFragment = new VocabularyFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_holder,dictionaryFragment).hide(dictionaryFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_holder,vocabularyFragment).hide(vocabularyFragment).commit();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_holder,homeFragment).hide(vocabularyFragment).commit();


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_home);
        View view = findViewById(R.id.bottom_home);
        if (view.isSelected()){
            getSupportFragmentManager().beginTransaction().show(homeFragment).commit();
            activeFragment = homeFragment;
        }
        if (word!=null){
            bottomNavigationView.setSelectedItemId(R.id.bottom_dictionary);
            getSupportFragmentManager().beginTransaction().show(dictionaryFragment).hide(activeFragment).commit();
            activeFragment = dictionaryFragment;
        }


        BottomNavigationItemView bottomNavigationItemView = findViewById(R.id.bottom_search);
        bottomNavigationItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(),SearchActivity.class);
                startActivity(intent);
            }
        });
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.bottom_home:
                            checkDisplayed(homeFragment);
                            return true;
                        case R.id.bottom_search:
                            item.setCheckable(false);
                            return true;
                        case R.id.bottom_dictionary:
                            checkDisplayed(dictionaryFragment);
                            return true;
                        case R.id.bottom_vocabulary:
                            checkDisplayed(vocabularyFragment);
                            return true;
                    }
                return false;
            }
        });
    }

    public void checkDisplayed(Fragment fragment){
        if (activeFragment != fragment){
            if (fragment == vocabularyFragment){
                getSupportFragmentManager().beginTransaction().detach(vocabularyFragment).commit();
                getSupportFragmentManager().beginTransaction().attach(vocabularyFragment).commit();
                getSupportFragmentManager().beginTransaction().hide(activeFragment).commit();
                getSupportFragmentManager().beginTransaction().show(fragment).commit();

            }else{
                getSupportFragmentManager().beginTransaction().hide(activeFragment).commit();
                getSupportFragmentManager().beginTransaction().show(fragment).commit();
            }
        }
        activeFragment = fragment;
    }

}