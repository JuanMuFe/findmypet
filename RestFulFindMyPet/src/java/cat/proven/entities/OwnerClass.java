package cat.proven.entities;

import java.util.ArrayList;
import java.util.List;

public class OwnerClass {
    private int id;
    private int idUser;
    private String name;
    private String firstname;
    private String surname;
    private String nif;
    private String birthdate;
    private String registerDate;
    private String phoneNumber;
    private String address;
    private String entryDate;
    private String dropOutDate;
    private int idCityProvince;
    private UserClass user;

    public OwnerClass() {
    }

    public OwnerClass(int idUser, String name, String firstname, String surname, String nif, String birthdate, String registerDate, String phoneNumber, String address, String entryDate, String dropOutDate, int idCityProvince) {
        this.idUser = idUser;
        this.name = name;
        this.firstname = firstname;
        this.surname = surname;
        this.nif = nif;
        this.birthdate = birthdate;
        this.registerDate = registerDate;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.entryDate = entryDate;
        this.dropOutDate = dropOutDate;
        this.idCityProvince = idCityProvince;
    }

    public OwnerClass(String name, String firstname, String surname, String nif, String birthdate, String phoneNumber, String address, int idCityProvince)
    {
        this.name = name;
        this.firstname = firstname;
        this.surname = surname;
        this.nif = nif;
        this.birthdate = birthdate;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.idCityProvince = idCityProvince;
    }

    public OwnerClass(int id, int idUser, String name, String firstname, String surname, String nif, String birthdate, String registerDate, String phoneNumber, String address, String entryDate, String dropOutDate, int idCityProvince) {
        this.id = id;
        this.idUser = idUser;
        this.name = name;
        this.firstname = firstname;
        this.surname = surname;
        this.nif = nif;
        this.birthdate = birthdate;
        this.registerDate = registerDate;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.entryDate = entryDate;
        this.dropOutDate = dropOutDate;
        this.idCityProvince = idCityProvince;
    }

    public OwnerClass( UserClass user, String name, String firstname, String surname, String nif, String birthdate, String registerDate, String phoneNumber, String address, String entryDate, String dropOutDate, int idCityProvince) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.firstname = firstname;
        this.surname = surname;
        this.nif = nif;
        this.birthdate = birthdate;
        this.registerDate = registerDate;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.entryDate = entryDate;
        this.dropOutDate = dropOutDate;
        this.idCityProvince = idCityProvince;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getDropOutDate() {
        return dropOutDate;
    }

    public void setDropOutDate(String dropOutDate) {
        this.dropOutDate = dropOutDate;
    }

    public int getIdCityProvince() {
        return idCityProvince;
    }

    public void setIdCityProvince(int idCityProvince) {
        this.idCityProvince = idCityProvince;
    }
    
    
    
}
