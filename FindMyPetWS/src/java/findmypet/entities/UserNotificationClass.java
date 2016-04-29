package findmypet.entities;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserNotificationClass {
    private int id;
    private int idUser;
    private int idNotification;

    public UserNotificationClass() {
    }

    public UserNotificationClass(int id, int idUser, int idNotification) {
        this.id = id;
        this.idUser = idUser;
        this.idNotification = idNotification;
    }

    public UserNotificationClass( int idUser, int idNotification) {
        
        this.idUser = idUser;
        this.idNotification = idNotification;
    }

    public int getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(int idNotification) {
        this.idNotification = idNotification;
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
}
