package android.bignerdranch.the_big_project;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CrimeDataBase extends SQLiteOpenHelper {
    public static final String DataBase_Name = "Crime_db";
    public static final String Table_Name = "Crime";
    public static final int Version = 1;

    public static final String Col_id = "ID";
    public static final String Col_Name = "Name";
    public static final String Col_date = "Date";
    public static final String Col_image = "Image";
    public static final String Col_Description = "Description";
    public static final String Col_solved = "Solved";

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // The Constructor of Data Base
    public CrimeDataBase(@Nullable Context context) {
        super(context, DataBase_Name, null, Version);
    }

    // Create Data Base
    @Override
    public void onCreate(SQLiteDatabase db) {
        String creatable = "CREATE TABLE " + Table_Name + " (" +
                Col_id + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Col_Name + " TEXT, " +
                Col_date + " TEXT, " +
                Col_image + " TEXT, " +
                Col_Description + " TEXT, " +
                Col_solved + " INTEGER)";
        db.execSQL(creatable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ContentValues getvalues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(Col_Name, crime.getName());
        values.put(Col_Description, crime.getDescription());
        values.put(Col_solved, crime.isSolved() ? 1 : 0);

        if (crime.getDate() != null) {
            values.put(Col_date, sdf.format(crime.getDate()));
        } else {
            values.put(Col_date, "");
        }

        if (crime.getImage() != null) {
            values.put(Col_image, crime.getImage().toString());
        } else {
            values.put(Col_image, "");
        }

        return values;
    }

    // Insert Data Into Table
    public long insertCrime(Crime crime) {
        SQLiteDatabase DB = getWritableDatabase();
        ContentValues values = getvalues(crime);
        long result = DB.insert(Table_Name, null, values);
        DB.close();
        return result;
    }

    public boolean updateCrime(Crime crime) {
        SQLiteDatabase DB = getWritableDatabase();
        ContentValues values = getvalues(crime);
        int result = DB.update(Table_Name, values, Col_id + " = ?", new String[]{String.valueOf(crime.getId())});
        DB.close();
        return (result > 0);
    }

    public long getCrimeCount() {
        SQLiteDatabase DB = getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(DB, Table_Name);
        DB.close();
        return count;
    }

    public boolean deleteCrime(int deleteId) {
        SQLiteDatabase DB = getWritableDatabase();
        long result = DB.delete(Table_Name, Col_id + " = ?", new String[]{String.valueOf(deleteId)});
        DB.close();
        return (result > 0);
    }

    @SuppressLint("Range")
    public ArrayList<Crime> getAllCrimes() throws ParseException {
        ArrayList<Crime> crimes = new ArrayList<>();
        SQLiteDatabase DB = getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = DB.rawQuery("SELECT * FROM " + Table_Name, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex(Col_id));
                    String name = cursor.getString(cursor.getColumnIndex(Col_Name));
                    String dateStr = cursor.getString(cursor.getColumnIndex(Col_date));
                    Date date = (dateStr != null && !dateStr.isEmpty()) ? sdf.parse(dateStr) : null;
                    String imageStr = cursor.getString(cursor.getColumnIndex(Col_image));
                    Uri image = (imageStr != null && !imageStr.isEmpty()) ? Uri.parse(imageStr) : null;
                    String description = cursor.getString(cursor.getColumnIndex(Col_Description));
                    boolean solved = cursor.getInt(cursor.getColumnIndex(Col_solved)) > 0;

                    crimes.add(new Crime(id, name, image, date, description, solved));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            DB.close();
        }

        return crimes;
    }

    @SuppressLint("Range")
    public Crime searchCrimeById(int crimeId) throws ParseException {
        SQLiteDatabase DB = getReadableDatabase();
        Cursor cursor = null;
        Crime crime = null;

        try {
            cursor = DB.rawQuery("SELECT * FROM " + Table_Name + " WHERE " + Col_id + " = ?", new String[]{String.valueOf(crimeId)});

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndex(Col_id));
                String name = cursor.getString(cursor.getColumnIndex(Col_Name));
                String dateStr = cursor.getString(cursor.getColumnIndex(Col_date));
                Date date = (dateStr != null && !dateStr.isEmpty()) ? sdf.parse(dateStr) : null;
                String imageStr = cursor.getString(cursor.getColumnIndex(Col_image));
                Uri image = (imageStr != null && !imageStr.isEmpty()) ? Uri.parse(imageStr) : null;
                String description = cursor.getString(cursor.getColumnIndex(Col_Description));
                boolean solved = cursor.getInt(cursor.getColumnIndex(Col_solved)) > 0;

                crime = new Crime(id, name, image, date, description, solved);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            DB.close();
        }

        return crime;
    }

    public Cursor getUnsolvedCrimes() {
        return getReadableDatabase().query(
                Table_Name, null, Col_solved + " = 0", null, null, null, null);
    }

    public Cursor getSolvedCrimes() {
        return getReadableDatabase().query(
                Table_Name, null, Col_solved + " = 1", null, null, null, null);
    }

    public void markCrimeAsSolved(int id) {
        SQLiteDatabase DB = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Col_solved, 1);
        DB.update(Table_Name, values, Col_id + " = ?", new String[]{String.valueOf(id)});
        DB.close();
    }
}
