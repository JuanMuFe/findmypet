package model;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alumne
 */
public class ReportClass {
    private int id;
    private int idOwner;
    private int idPet;
    private String entryDate;
    private String location;
    private String extra;

    public ReportClass() {
    }

    public ReportClass(int idOwner, int idPet, String entryDate, String location, String extra) {
        this.idOwner = idOwner;
        this.idPet = idPet;
        this.entryDate = entryDate;
        this.location = location;
        this.extra = extra;
    }

    public ReportClass(int id, int idOwner, int idPet, String entryDate, String location, String extra) {
        this.id = id;
        this.idOwner = idOwner;
        this.idPet = idPet;
        this.entryDate = entryDate;
        this.location = location;
        this.extra = extra;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(int idOwner) {
        this.idOwner = idOwner;
    }

    public int getIdPet() {
        return idPet;
    }

    public void setIdPet(int idPet) {
        this.idPet = idPet;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
    
    
}
