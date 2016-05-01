/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cat.proven.findmypet.findmypet;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String BD_URL = "jdbc:mysql://"+"127.0.0.1/findmypet";  //cambiar al subir al servidor
    private static final String USUARI = "petadmin";
    private static final String PASSWORD = "admin";

    public ConnectionDB() throws ClassNotFoundException {
        Class.forName(this.DRIVER);       
    }
    
    /**
     * 
     * @return a connection 
     * @throws SQLException if a connection error occurs
     */
    public Connection getConnection() throws SQLException {
        Connection conn=DriverManager.getConnection(BD_URL, USUARI, PASSWORD);
        return conn;
    }
}

