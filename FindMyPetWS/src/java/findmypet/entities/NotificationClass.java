package findmypet.entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class NotificationClass {
    private int id;
    private int active;
    private String description;

    public NotificationClass() {
    }

    public NotificationClass(int active, String description) {
        this.active = active;
        this.description = description;
    }

    public NotificationClass(int id, int active, String description) {
        this.id = id;
        this.active = active;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
