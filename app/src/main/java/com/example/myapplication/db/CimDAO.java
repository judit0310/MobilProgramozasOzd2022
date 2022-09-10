package com.example.myapplication.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.Cim;

import java.util.List;

@Dao
public interface CimDAO {
    @Query("Select * from Cim")
    List<Cim> getAll();


    @Query("SELECT * from cim WHERE iranyitoszam= :iranyitoszam and " +
            "varos = :varos and utca = :utca and hazszam = :hazszam")
    List<Cim> findSame(String iranyitoszam, String varos, String utca, String hazszam);


    @Query("SELECT * from cim WHERE varos=:varos")
    List<Cim> findInCity(String varos);

    @Insert
    void insert(Cim cim);

    @Delete
    void delete(Cim cim);

    @Update
    void update(Cim cim);

    default List<Cim> findSame(Cim cim){
       return findSame(cim.getIranyitoszam(), cim.getVaros(), cim.getUtca(), cim.getHazszam());
    };

}
