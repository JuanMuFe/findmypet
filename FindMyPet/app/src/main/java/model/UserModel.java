package model;

import persistence.UserADO;

/**
 * Created by Alumne on 30/04/2016.
 */
public class UserModel {

    public int login(UserClass u){
        //llamara al webservice, que a su vez llamara al ado
        UserADO uado = new UserADO();
        UserClass user = uado.getUserExist(u);
        if(user != null){
            return 1;
        }else {
            return 0;
        }
    }
}
