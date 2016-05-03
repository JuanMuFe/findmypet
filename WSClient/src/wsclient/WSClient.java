/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package wsclient;


import javax.ws.rs.core.Response;

/**
 *
 * @author Alumne
 */
public class WSClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        NewJerseyClient nj = new NewJerseyClient();
        Response response = nj.login();
        
        System.out.println(response.toString());
    }
    
}
