package model;

public class CityClass {
    private int id;
    private String name;
    private String postalCode;

    public CityClass() {
    }

    public CityClass(String name, String postalCode) {
        this.name = name;
        this.postalCode = postalCode;
    }

    public CityClass(int id, String name, String postalCode) {
        this.id = id;
        this.name = name;
        this.postalCode = postalCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    
}
