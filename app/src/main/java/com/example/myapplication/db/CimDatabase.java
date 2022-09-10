package com.example.myapplication.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.myapplication.Cim;

@Database(entities = {Cim.class}, version = 1)
public abstract class CimDatabase extends RoomDatabase {
    public abstract CimDAO getCimDAO();

}
