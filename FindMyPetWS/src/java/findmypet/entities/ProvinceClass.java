package findmypet.entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ProvinceClass {
    private int id;
    private String province;

    public ProvinceClass() {
    }

    public ProvinceClass(String province) {
        this.province = province;
    }

    public ProvinceClass(int id, String province) {
        this.id = id;
        this.province = province;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
    
    
}
