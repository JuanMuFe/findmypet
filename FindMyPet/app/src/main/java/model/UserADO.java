package model;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Alumne on 30/04/2016.
 */
public class UserADO {

    private final String QUERY_FIND_USER= "SELECT * FROM user WHERE user_name = ? AND password = ?";

    public UserADO()
    {

    }

   /* public UserClass getUserExit(UserClass u)
    {
        int result = -1;
        UserClass checkuser=null;
        String userName = u.getUserName();
        String password = u.getPassword();

        try{
            ConnectionDB db = new ConnectionDB();
            Connection conn = db.getConnection();
            if (conn != null)
            {
                PreparedStatement st = conn.prepareStatement(QUERY_FIND_USER);
                st.setString(1,userName);
                st.setString(2,password);
                ResultSet rs = st.executeQuery();

                while(rs.next())
                {
                    checkuser = resultsetToUser(rs);
                }
            }

        }catch(SQLException | ClassNotFoundException e) {
            checkuser = null;
        }


        return checkuser;
    }*/

    private UserClass resultsetToUser(ResultSet rs) throws SQLException {
        UserClass u = null;
        int id = rs.getInt("id");
        int id_profile = rs.getInt("id_profile");
        String user_name = rs.getString("user_name");
        String passsword = rs.getString("passsword");
        String email = rs.getString("email");
        int active = rs.getInt("active");
        String image = rs.getString("image");

        u = new UserClass(id,id_profile,user_name,passsword,email,active,image);

        return u;
    }
}
