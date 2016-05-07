package model;

import java.util.List;

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

    public int register(UserClass u, OwnerClass o){
        //llamara al webservice, que a su vez llamara al ado
        UserADO uado = new UserADO();
        int result = uado.register(u,o);

        return result;

    }

    public List<OwnerClass> searchUser(UserClass u,OwnerClass o)
    {
        UserADO uado = new UserADO();
        List<OwnerClass> listUser = uado.searchUser(u,o);

        return listUser;
    }
}
