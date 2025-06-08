package android.bignerdranch.the_big_project;

import android.net.Uri;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

public class Crime {
    private int id;
    private String name;
    private Uri image;
    private Date date;
    private String description;
    private boolean solved;

    // Constructors
    public Crime() {
        this.solved = false;
    }

    public Crime(int id, String name, Uri image, Date date, String description, boolean solved) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.date = date;
        this.description = description;
        this.solved = solved;
    }

    public Crime(int id, String name, Uri image, Date date, String description) {
        this(id, name, image, date, description, false);
    }

    public Crime(int id, String name, Uri image, Date date, boolean solved) {
        this(id, name, image, date, null, solved);
    }
    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Crime(EditText name, ImageView image, TextView date, EditText description) {
    }

    // Getters & Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }
}
