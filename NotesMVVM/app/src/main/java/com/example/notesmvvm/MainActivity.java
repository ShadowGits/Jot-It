package com.example.notesmvvm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    @BindView(R.id.note_add_floating_button)
    FloatingActionButton floatingActionButtonAddNote;

    public static final int ADD_NOTE_RESULT_CODE = 1;
    public static final int EDIT_NOTE_RESULT_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        floatingActionButtonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNote = new Intent(MainActivity.this, AddEditNoteActivity.class);
                startActivityForResult(addNote, ADD_NOTE_RESULT_CODE);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter noteAdapter = new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);

        noteAdapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent editNote = new Intent(MainActivity.this, AddEditNoteActivity.class);
                editNote.putExtra(AddEditNoteActivity.EXTRA_ID, note.getId());

                editNote.putExtra(AddEditNoteActivity.EXTRA_TITLE, note.getTitle());
                editNote.putExtra(AddEditNoteActivity.EXTRA_DESC, note.getDescription());
                editNote.putExtra(AddEditNoteActivity.EXTRA_PRIORITY, note.getPriority());

                startActivityForResult(editNote, EDIT_NOTE_RESULT_CODE);


            }
        });

        //not a new operator constructor instance coz it is always new but according to lifecycle provided
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        //Live Data Observe
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {

                noteAdapter.setNotes(notes);

            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int delPosition = viewHolder.getAdapterPosition();
                noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));

                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();

            }
        }).attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all_notes) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set title

            alertDialogBuilder.setTitle("Delete All Notes.");
            alertDialogBuilder.setIcon(R.drawable.ic_delete_sweep_black_24dp);

            // set dialog message
            alertDialogBuilder
                    .setMessage("Are You Sure?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            // current activity
                            noteViewModel.deleteAllNotes();
                            Toast.makeText(MainActivity.this, "All Notes Deleted", Toast.LENGTH_SHORT);
                        }
                    })
                    .setNegativeButton("NO", null);

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == ADD_NOTE_RESULT_CODE) {
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String desc = data.getStringExtra(AddEditNoteActivity.EXTRA_DESC);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, -1);

            Note newNote = new Note(title, desc, priority);
            noteViewModel.insert(newNote);
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_OK && requestCode == EDIT_NOTE_RESULT_CODE) {

            int id=data.getIntExtra(AddEditNoteActivity.EXTRA_ID,-1);
            if(id==-1)
            {
                Toast.makeText(this,"Note can't be updated",Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String desc = data.getStringExtra(AddEditNoteActivity.EXTRA_DESC);
            int priority = data.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY, -1);
            Note newNote = new Note(title, desc, priority);
            newNote.setId(id);
            noteViewModel.update(newNote);
            Toast.makeText(this, "Note Updated", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show();
        }
    }
}
