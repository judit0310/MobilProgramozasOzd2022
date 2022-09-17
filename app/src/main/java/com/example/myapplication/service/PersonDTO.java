package com.example.myapplication.service;

import java.io.Serializable;

public class PersonDTO implements Serializable {

    private int id;
    private String keresztNev;
    private String vezetekNev;
    private int fizetes;

    public PersonDTO() {
    }

    public PersonDTO(int id, String keresztNev, String vezetekNev, int fizetes) {
        this.id = id;
        this.keresztNev = keresztNev;
        this.vezetekNev = vezetekNev;
        this.fizetes = fizetes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeresztNev() {
        return keresztNev;
    }

    public void setKeresztNev(String keresztNev) {
        this.keresztNev = keresztNev;
    }

    public String getVezetekNev() {
        return vezetekNev;
    }

    public void setVezetekNev(String vezetekNev) {
        this.vezetekNev = vezetekNev;
    }

    public int getFizetes() {
        return fizetes;
    }

    public void setFizetes(int fizetes) {
        this.fizetes = fizetes;
    }

    @Override
    public String toString() {
        return "PersonDTO{" +
                "id=" + id +
                ", keresztNev='" + keresztNev + '\'' +
                ", vezetekNev='" + vezetekNev + '\'' +
                ", fizetes=" + fizetes +
                '}';
    }
}
