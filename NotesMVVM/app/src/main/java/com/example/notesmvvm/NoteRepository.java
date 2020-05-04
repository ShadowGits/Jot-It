package com.example.notesmvvm;

import android.app.Application;
import android.os.AsyncTask;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.room.Update;

import java.util.List;

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);

        //abstract method but room auto generates code for this one
        noteDao = noteDatabase.noteDao();

        allNotes = noteDao.getAllNotes();

    }

    //DB operations
    public void insert(Note note) {
        new InsertAsyncTask(noteDao).execute(note);
    }

    public void delete(Note note) {
        new DeleteAsyncTask(noteDao).execute(note);
    }

    public void update(Note note) {
        new UpdateAsyncTask(noteDao).execute(note);
    }

    public void deleteAllNotes() {
        new DeleteAllAsyncTask(noteDao).execute();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }


    //DB Operations to be performed on background threads AsyncTask for each

    //1.InsertNoteAsyncTask
    private static class InsertAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        public InsertAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    //2.DeleteNoteAsyncTask
    private static class DeleteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        public DeleteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    //3.UpdateNoteAsyncTask
    private static class UpdateAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        public UpdateAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    //4.DeleteAllAsyncTask
    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao noteDao;

        public DeleteAllAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }

}
