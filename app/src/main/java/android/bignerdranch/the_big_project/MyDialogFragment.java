package android.bignerdranch.the_big_project;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyDialogFragment extends DialogFragment {
    private Dialogcallback callback;
    private DialogCallbackUpdate  updated;
    private Date selectedDate;
    private Uri selectedImageUri;
    private ImageView image;
    private TextView date;
    private EditText name, description;
    private Crime crime;

    // ActivityResultLauncher to handle image selection
    private final ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    if (image != null) {
                        crime.setImage(selectedImageUri);
                        image.setImageURI(selectedImageUri);
                    }
                }
            }
    );

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Ensure the parent activity implements Dialogcallback
        if (context instanceof Dialogcallback) {
            callback = (Dialogcallback) context;
        } else if(context instanceof DialogCallbackUpdate)
        {
            updated = (DialogCallbackUpdate) context;
        }
        else
        {
            Log.e("MyDialogFragment", "Activity must implement Dialogcallback");
            throw new ClassCastException(context.toString() + " must implement Dialogcallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
        updated = null;
    }

    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_my_dialog, null);
        name = view.findViewById(R.id.editTextName);
        description = view.findViewById(R.id.editTextDescription);
        image = view.findViewById(R.id.dialog_image);
        date = view.findViewById(R.id.btnPickDate);
        crime = new Crime(name, image, date, description);
        selectedDate = new Date();

        // Handle image selection
        image.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            launcher.launch(intent);
        });

        // Handle date selection
        date.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(
                    requireContext(), (view1, year, month, dayOfMonth) -> {
                calendar.set(year, month, dayOfMonth);
                selectedDate = calendar.getTime();

                // Format the selected date for display
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                date.setText(sdf.format(selectedDate));
                crime.setDate(selectedDate);
            },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            ).show();
        });

        // Set up the dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setView(view)
                .setTitle("Enter Data")
                .setPositiveButton("Save", (dialog, which) -> {
                    String nameText = name.getText().toString();
                    String descriptionText = description.getText().toString();
                    crime.setDescription(descriptionText);
                    crime.setName(nameText);

                    // Call back to the parent activity with the crime data
                    if (callback != null) {
                        callback.enteredData(crime);
                    }
                    else if(updated!=null)
                    {
                         updated.UpdatedDate(crime);
                    }
                })
                .setNegativeButton("Cancel", null);

        // Customize the dialog buttons
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            // Change the text color of buttons
            positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.Dark_Green));
            negativeButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.Dark_Green));

            // Change the background of the buttons
            positiveButton.setBackgroundResource(R.drawable.rectangle_bg); // Custom background
            negativeButton.setBackgroundResource(R.drawable.rectangle_bg); // Same or different background
        });

        return dialog;
    }

    // Interface for communication with the parent activity
    public interface Dialogcallback {
        void enteredData(Crime crime);
    }
    public interface  DialogCallbackUpdate{
        void UpdatedDate(Crime crime);

    }
}
