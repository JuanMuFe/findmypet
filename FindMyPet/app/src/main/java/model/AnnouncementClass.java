package model;

public class AnnouncementClass {
    private int id;
    private String description;
    private String date;
    private int idUser;

    public AnnouncementClass() {
    }

    public AnnouncementClass(String description, String date, int idUser) {
        this.description = description;
        this.date = date;
        this.idUser = idUser;
    }

    public AnnouncementClass(int id, String description, String date, int idUser) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.idUser = idUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
    
    
    
}
