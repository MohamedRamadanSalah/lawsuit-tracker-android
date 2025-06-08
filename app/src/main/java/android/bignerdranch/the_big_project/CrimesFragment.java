package android.bignerdranch.the_big_project;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CrimesFragment extends Fragment {

    private RecyclerView recyclerView;
    private CrimeAdapter adapter;
    private CrimeDataBase dbHelper;
    private List<Crime> crimeList = new ArrayList<>();
    private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crimes, container, false);

        dbHelper = new CrimeDataBase(getContext());
        recyclerView = view.findViewById(R.id.recycler34455);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set up the adapter with a click listener for the crimes
        adapter = new CrimeAdapter(getContext(), crimeList, crime -> {
            dbHelper.markCrimeAsSolved(crime.getId());
            refreshData();
        });

        recyclerView.setAdapter(adapter);

        refreshData();  // Load data initially

        return view;
    }

    @SuppressLint("Range")
    private void loadUnsolvedCrimes() throws ParseException {
        crimeList.clear();
        Cursor cursor = null;
        try {
            cursor = dbHelper.getUnsolvedCrimes();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID"));
                String Name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
                String dateStr = cursor.getString(cursor.getColumnIndexOrThrow("Date"));
                String decr = cursor.getString(cursor.getColumnIndexOrThrow("Description"));
                boolean isSolved = cursor.getInt(cursor.getColumnIndexOrThrow("Solved")) == 1;

                // Check and parse the image Uri if valid
                String imageStr = cursor.getString(cursor.getColumnIndex("Image"));
                Uri image = imageStr != null && !imageStr.isEmpty() ? Uri.parse(imageStr) : null;

                // Parse the date string safely
                Date date = null;
                if (dateStr != null && !dateStr.isEmpty()) {
                    date = sdf2.parse(dateStr);
                }

                // Add the Crime object to the list
                crimeList.add(new Crime(id, Name, image, date, decr, isSolved));
            }

            // Notify the adapter to update the view
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        } catch (ParseException e) {
            e.printStackTrace();  // Handle any parsing exceptions
        } finally {
            if (cursor != null) {
                cursor.close();  // Always close the cursor
            }
        }
    }

    public void refreshData() {
        try {
            loadUnsolvedCrimes();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
