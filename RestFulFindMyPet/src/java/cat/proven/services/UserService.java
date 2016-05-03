package cat.proven.services;


import cat.proven.entities.OwnerClass;
import cat.proven.entities.UserClass;
import cat.proven.entities.persistence.UserADO;
import java.util.Collection;

/**
 *
 * @author AMS
 */
public class UserService {

    private Collection<UserClass> db;
    
    
    
    public UserService(){
       
    }
    
    public UserClass login(String userName, String password){
        UserADO uADO = new UserADO();
        return uADO.getUserExist(userName,password);
    }
    
    public int register(UserClass u, OwnerClass o){
        UserADO uADO = new UserADO();
        return uADO.register(u,o);
    }
    
   /* public UserClass findById(int id){
        for (UserClass st : db) {
            if(st.getId() == id) return st;
        }
        return null;
    }
*/
    /**
     * Afegeix l'estudiant i el retorna amb l'Id 
     * autoincremental.
     * @param student
     * @return 
     */
       /* 
    public UserClass add(UserClass student){
        //Cerquem el màxim id per incrementar-lo en 1
        int idMax = 0;
        for (UserClass st : db) {
            if(idMax < st.getId()) idMax = st.getId();
        }
        student.setId(idMax + 1);
        db.add(student);
        return student;        
    }
    
    public void remove(int id) {
        UserClass s = findById(id);
        this.remove(s);
    }
    
    public void remove(UserClass st){
        db.remove(st);
    }*/
    
}
