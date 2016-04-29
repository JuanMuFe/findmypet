package model;

import persistence.UserADO;

/**
 * Created by Alumne on 30/04/2016.
 */
public class UserModel {
    private String baseUrl = "http://localhost:8080/FindMyPetWS/WSfindmypet";

    public UserModel(){

    }

    public int login(UserClass u){
        //llamara al webservice
        UserADO uado = new UserADO();
        UserClass user = uado.getUserExist(u);
        if(user != null){
            return 1;
        }else {
            return 0;
        }
    }
}
