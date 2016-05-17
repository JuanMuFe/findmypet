package model;

public class NotificationClass {
    private int id;
    private int active;
    private String description;
    private int readed;
    private int id_report;

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

    public NotificationClass(int id, int active, String description, int id_report) {
        this.id = id;
        this.active = active;
        this.description = description;
        this.id_report = id_report;
    }

    public NotificationClass(int id, int active, String description, int readed, int id_report) {
        this.id = id;
        this.active = active;
        this.description = description;
        this.readed = readed;
        this.id_report = id_report;
    }

    public int getReaded() {
        return readed;
    }

    public void setReaded(int readed) {
        this.readed = readed;
    }

    public int getId_report() {
        return id_report;
    }

    public void setId_report(int id_report) {
        this.id_report = id_report;
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
