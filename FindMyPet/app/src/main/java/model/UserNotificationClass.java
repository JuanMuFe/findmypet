package model;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alumne
 */
public class UserNotificationClass {
    private int id;
    private int idUser;
    private int idNotification;
    private int readed;

    public UserNotificationClass() {
    }

    public UserNotificationClass(int id, int idUser, int idNotification, int readed) {
        this.id = id;
        this.idUser = idUser;
        this.idNotification = idNotification;
        this.readed = readed;
    }

    public UserNotificationClass(int idUser, int idNotification, int readed) {
        this.idUser = idUser;
        this.idNotification = idNotification;
        this.readed = readed;
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

    public int getReaded() { return readed; }

    public void setReaded(int readed) { this.readed = readed; }
}
