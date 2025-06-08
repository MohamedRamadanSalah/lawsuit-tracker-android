package android.bignerdranch.the_big_project;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity implements MyDialogFragment.Dialogcallback {
Toolbar mToolbar;
TabLayout tabs;
ViewPager2 viewpager;
CrimePagerAdapter adapter;
CrimeDataBase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hideSystemUI();
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(visibility -> {
            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                hideSystemUI();
            }
        });
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);



        // To Delete Default Title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }



        database = new CrimeDataBase(this);
        tabs = findViewById(R.id.tab_layout);
        viewpager = findViewById(R.id.viewpager_lay);
        adapter = new CrimePagerAdapter(this);
        viewpager.setAdapter(adapter);


        new TabLayoutMediator(tabs,viewpager,(tab, position) -> {
            if (position == 0)
            {tab.setText("Crimes");} else {tab.setText("My Data");}
        }).attach();
    }



// Inflate To The Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuss,menu);
        return true;
    }
    // Item in Menu Inflate and Actions
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.add_item_menu)
        {
           new MyDialogFragment().show(getSupportFragmentManager(),"DialogFragmentTag");
            return true;
        }
        else if(item.getItemId()==R.id.app_bar_search)
        {
            Toast.makeText(this, "Search clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void enteredData(Crime crime) {
        // Insert Crime To The SQLite Data Base
       long IDD =  database.insertCrime(crime);
        crime.setId((int)IDD);

       // Set The UnSolved Crimes Fragment To The ViewPager
        CrimesFragment fragment = adapter.getUnsolvedFragment();
        // Add The New Item To The Recycler View wit Use Its Adapter
        if (fragment != null) {
            fragment.refreshData();
        }
    }
    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
    }

}