package com.example.notesmvvm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AddEditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.example.notesmvvm.EXTRA_ID";

    public static final String EXTRA_TITLE = "com.example.notesmvvm.EXTRA_TITLE";
    public static final String EXTRA_DESC = "com.example.notesmvvm.EXTRA_DESC";
    public static final String EXTRA_PRIORITY = "com.example.notesmvvm.EXTRA_PRIORITY";


    @BindView(R.id.edit_text_title)
    EditText editTextTitle;
    @BindView(R.id.edit_text_description)
    EditText editTextDesc;
    @BindView(R.id.number_picker_priority)
    NumberPicker numberPickerPriority;

    //private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        ButterKnife.bind(this);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        numberPickerPriority.setMaxValue(10);
        numberPickerPriority.setMinValue(1);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            editTextDesc.setText(intent.getStringExtra(EXTRA_DESC));
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            numberPickerPriority.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));

            setTitle("Edit Note");
        } else {
            setTitle("Add Note");
        }
        numberPickerPriority.setMaxValue(10);
        numberPickerPriority.setMinValue(1);
    }

    private void saveNote() {
        String title = editTextTitle.getText().toString();
        String desc = editTextDesc.getText().toString();
        int priority = numberPickerPriority.getValue();

        if (title.trim().isEmpty() || desc.trim().isEmpty()) {
            Toast.makeText(this, "Invalid Note", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();

        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESC, desc);
        data.putExtra(EXTRA_PRIORITY, priority);

        int id=getIntent().getIntExtra(EXTRA_ID,-1);

        if(id!=-1)
        {
            data.putExtra(EXTRA_ID,id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.save_note) {
            //Save the note
            saveNote();
        }
        return super.onOptionsItemSelected(item);
    }
}
