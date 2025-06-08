package android.bignerdranch.the_big_project;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DetailsFragment extends Fragment {
    CrimeDataBase DB;
    TableLayout table;
    TableRow Header;

    public DetailsFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details, container, false);
        table = v.findViewById(R.id.tableeLayout);
        DB = new CrimeDataBase(getContext());
        Header = new TableRow(getContext());

        // Add header row
        CreateHeader();

        try {
            RefrshTable();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        try {
            RefrshTable();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    // Create header for the table
    private TextView CreateHeaderCell(String head) {
        Context context = getContext();
        TextView Cell = new TextView(context);
        Cell.setText(head);
        Cell.setTextColor(Color.parseColor("#FFFFFF"));
        Cell.setTypeface(null, Typeface.BOLD);
        Cell.setBackgroundColor(Color.parseColor("#388E3C"));
        Cell.setPadding(8, 8, 8, 8);
        return Cell;
    }

    private void CreateHeader() {
        TextView Name = CreateHeaderCell("Name");
        TextView Solved = CreateHeaderCell("IsSolved");
        TextView Details = CreateHeaderCell("Details");
        Solved.setGravity(Gravity.CENTER);
        Details.setGravity(Gravity.CENTER);
        Name.setGravity(Gravity.CENTER);
        Header.addView(Name);
        Header.addView(Solved);
        Header.addView(Details);

        table.addView(Header);
    }


    private TextView CreateCell(String text, int i) {
        Context context = getContext();
        TextView Cell = new TextView(context);
        Cell.setText(text);
        Cell.setTextColor(Color.parseColor("#000000"));
        Cell.setTypeface(null, Typeface.BOLD);
        Cell.setGravity(Gravity.CENTER);

        Cell.setGravity(View.TEXT_ALIGNMENT_CENTER);


        if (i % 2 == 0) {
            Cell.setBackgroundColor(Color.parseColor("#E8F5E9"));
        } else {
            Cell.setBackgroundColor(Color.parseColor("#DCEDC8"));
        }


        return Cell;
    }

    public void RefrshTable() throws ParseException {
        table.removeAllViews();
        table.addView(Header);

        ArrayList<Crime> crimes = DB.getAllCrimes();
        int i = 0;
        for (Crime C : crimes) {
            TableRow Row = new TableRow(getContext());
            TextView Name = CreateCell(C.getName(), i);
            Name.setGravity(Gravity.CENTER);

            TextView Solved;

            if (!C.isSolved()) {
                Solved = CreateCell("No", i);
                Solved.setGravity(Gravity.CENTER);
                Solved.setBackgroundColor(Color.parseColor("#F44336"));
            } else {
                Solved = CreateCell("Yes", i);
                Solved.setGravity(Gravity.CENTER);
                Solved.setBackgroundColor(Color.parseColor("#C8E6C9"));
            }


            String D = "Details";
            TextView Details = CreateCell(D, i);

            Details.setTextColor(Color.parseColor("#003366"));

            Details.setPaintFlags(Details.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            Details.setGravity(Gravity.CENTER);

            Row.addView(Name);
            Row.addView(Solved);
            Row.addView(Details);

            Details.setOnClickListener(v -> {
                Context CC = getContext();
                Intent intent = new Intent(CC,CrimeDeatials.class);

                intent.putExtra("id",C.getId());
                intent.putExtra("name",C.getName());
                intent.putExtra("Image",C.getImage().toString());

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String dateString = formatter.format(C.getDate());
                intent.putExtra("Date", dateString);
                intent.putExtra("Description",C.getDescription());
                startActivity(intent);
            });

            table.addView(Row);
            i++;
        }
    }

}

