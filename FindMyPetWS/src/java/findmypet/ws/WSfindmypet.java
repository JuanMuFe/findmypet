package findmypet.ws;

import findmypet.entities.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.jws.WebService;

@WebService(serviceName="WSfindmypet")
public class WSfindmypet {
    private final String QUERY_FIND_USER= "SELECT * FROM user WHERE user_name = ? AND pswd = ?;";
        
    public UserClass getUserExist(String username, String passwordPassed){
        UserClass checkuser=null;
   
        try{
            ConnectionDB db = new ConnectionDB();
            Connection conn = null;
            try {
                conn = db.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (conn != null)
            {
                PreparedStatement st = conn.prepareStatement(QUERY_FIND_USER);
                st.setString(1,username);
                st.setString(2,passwordPassed);
                ResultSet rs = st.executeQuery();

                while(rs.next()){
                    checkuser = resultsetToUser(rs);                
                }
            }
        }catch(SQLException e) {
            checkuser = null;
        }catch( ClassNotFoundException  e1){
            checkuser = null;
        }
        return checkuser;
    }
    
    private UserClass resultsetToUser(ResultSet rs) throws SQLException {
        UserClass u = null;
        int id = rs.getInt("id");
        int id_profile = rs.getInt("id_profile");
        String user_name = rs.getString("user_name");
        String passsword = rs.getString("pswd");
        String email = rs.getString("email");
        int active = rs.getInt("active");
        String image = rs.getString("image");

        u = new UserClass(id,id_profile,user_name,passsword,email,active,image);

        return u;
    }
}
