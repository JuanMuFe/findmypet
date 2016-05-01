package model;

public class CityProvinceClass {
    private int idCityProvince;
    private int idCity;
    private int idProvince;

    public CityProvinceClass() {
    }

    public CityProvinceClass(int idCity, int idProvince) {
        this.idCity = idCity;
        this.idProvince = idProvince;
    }

    public CityProvinceClass(int idCityProvince, int idCity, int idProvince) {
        this.idCityProvince = idCityProvince;
        this.idCity = idCity;
        this.idProvince = idProvince;
    }

    public int getIdCityProvince() {
        return idCityProvince;
    }

    public void setIdCityProvince(int idCityProvince) {
        this.idCityProvince = idCityProvince;
    }

    public int getIdCity() {
        return idCity;
    }

    public void setIdCity(int idCity) {
        this.idCity = idCity;
    }

    public int getIdProvince() {
        return idProvince;
    }

    public void setIdProvince(int idProvince) {
        this.idProvince = idProvince;
    }
    
    
    
}
