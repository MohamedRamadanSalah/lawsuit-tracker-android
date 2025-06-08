package android.bignerdranch.the_big_project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

public class CrimeAdapter extends RecyclerView.Adapter<CrimeAdapter.ViewHolder> {

    private List<Crime> crimeList;
    private Context context;
    private OnCrimeSolvedListener listener;

    public interface OnCrimeSolvedListener {
        void onCrimeSolved(Crime crime);
    }

    public CrimeAdapter(Context context, List<Crime> crimes, OnCrimeSolvedListener listener) {
        this.context = context;
        this.crimeList = crimes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_crimefr, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Crime crime = crimeList.get(position);

        holder.Name.setText(crime.getName());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        holder.date.setText(sdf.format(crime.getDate()));


        if (crime.getImagePath() == null && crime.getImage() != null) {
            String path = saveImageToInternalStorage(context, crime.getImage());
            crime.setImagePath(path);
        }


        if (crime.getImagePath() != null) {
            File imgFile = new File(crime.getImagePath());
            if (imgFile.exists()) {
                holder.image.setImageURI(Uri.fromFile(imgFile));
            }
        }

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(crime.isSolved());

        holder.checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                crime.setSolved(isChecked);
                if (listener != null && isChecked) {
                    listener.onCrimeSolved(crime);
                }
                notifyItemChanged(adapterPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return crimeList.size();
    }


    private String saveImageToInternalStorage(Context context, Uri imageUri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            File file = new File(context.getFilesDir(), "crime_" + System.currentTimeMillis() + ".png");
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();

            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name, date;
        CheckBox checkBox;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.custom_name);
            date = itemView.findViewById(R.id.custom_date);
            checkBox = itemView.findViewById(R.id.custom_checkbox);
            image = itemView.findViewById(R.id.custom_image);
        }
    }
}
