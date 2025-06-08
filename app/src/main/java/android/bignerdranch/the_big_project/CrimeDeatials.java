package android.bignerdranch.the_big_project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CrimeDeatials extends AppCompatActivity implements MyDialogFragment.DialogCallbackUpdate {

    ImageView image;
    TextView Name, Date, Desc;
    Button Delete, Edit;
    CrimeDataBase DBB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_deatials);
        hideSystemUI();
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(visibility -> {
            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                hideSystemUI();
            }
        });
        // Initializing views
        image = findViewById(R.id.dialog_image);
        Name = findViewById(R.id.name_tv);
        Date = findViewById(R.id.date_tv);
        Desc = findViewById(R.id.decrption_tv);
        Delete = findViewById(R.id.delete_btn);
        Edit = findViewById(R.id.edit_btn);

        // Initialize database
        DBB = new CrimeDataBase(this);

        // Getting data from the intent
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("Description");
        String imageUriString = intent.getStringExtra("Image");

        String dateString = intent.getStringExtra("Date");

        // Setting data to views
        Name.setText(name);
        Date.setText(dateString);
        Desc.setText(description);

        // Setting image if exists
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            image.setImageURI(imageUri);
        }

        // Delete button click listener
        Delete.setOnClickListener(v -> {
            if (id >0) {
                DBB.deleteCrime(id);  // Delete crime from the database
                Toast.makeText(this, "This Crime Deleted Now", Toast.LENGTH_SHORT).show();
                // Navigate to the main activity after deletion
                Intent mainIntent = new Intent(this, MainActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Optional: clear back stack
                startActivity(mainIntent);
                finish();  // Close current activity
            }
        });

        // Edit button click listener
        Edit.setOnClickListener(v -> {
            // Show dialog fragment for updating crime
            new MyDialogFragment().show(getSupportFragmentManager(), "Fragments");
        });
    }

    @Override
    public void UpdatedDate(Crime crime) {


        // Update crime in the database
        DBB.updateCrime(crime);

        // Update the displayed data after modification
        Name.setText(crime.getName());
        Date.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(crime.getDate()));
        Desc.setText(crime.getDescription());
        if(crime.getImage()!=null)
        {
            image.setImageURI(Uri.parse(crime.getImage().toString()));
        }


        Toast.makeText(this, "Crime updated successfully", Toast.LENGTH_SHORT).show();
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

