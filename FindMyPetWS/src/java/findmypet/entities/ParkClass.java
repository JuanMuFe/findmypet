package findmypet.entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ParkClass {
    private int id;
    private String name;
    private String address;
    private int idCityProvince;

    public ParkClass() {
    }

    public ParkClass(String name, String address, int idCityProvince) {
        this.name = name;
        this.address = address;
        this.idCityProvince = idCityProvince;
    }

    public ParkClass(int id, String name, String address, int idCityProvince) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.idCityProvince = idCityProvince;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIdCityProvince() {
        return idCityProvince;
    }

    public void setIdCityProvince(int idCityProvince) {
        this.idCityProvince = idCityProvince;
    }
    
    
}
