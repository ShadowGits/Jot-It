package com.example.notesmvvm;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabase.Callback;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    //This class will be Singelton
    private static NoteDatabase instance;

    //This method is used to access methods created in the NoteDao class
    public abstract NoteDao noteDao();

    //2 diffrent threads wont be able to create instance at the same time
    public static synchronized NoteDatabase getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context, NoteDatabase.class, "note_database").
                    fallbackToDestructiveMigration().addCallback(roomCallback).build();
        return instance;

    }

    private static Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new InsertDefaultAsyncTask(instance).execute();
        }
    };

    private static class InsertDefaultAsyncTask extends AsyncTask<Void, Void, Void> {

        private NoteDao noteDao;

        public InsertDefaultAsyncTask(NoteDatabase db) {
            noteDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            noteDao.insert(new Note("One", "Desc1", 2));
            noteDao.insert(new Note("Two", "Desc2", 3));
            noteDao.insert(new Note("Three", "Desc3", 1));

            return null;
        }
    }

}
