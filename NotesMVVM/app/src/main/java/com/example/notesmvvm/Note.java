package com.example.notesmvvm;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    //Auto-Incremented
    @PrimaryKey(autoGenerate = true)
    private int id;

    //other columns
    private String title;
    private String description;
    private int priority;

    public void setId(int id) {
        this.id = id;
    }

    public Note(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getPriority() {
        return priority;
    }
}
