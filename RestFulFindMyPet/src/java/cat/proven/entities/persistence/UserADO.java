package cat.proven.entities.persistence;


import cat.proven.entities.ConnectionDB;
import cat.proven.entities.OwnerClass;
import cat.proven.entities.UserClass;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;




/**
 * Created by Alumne on 30/04/2016.
 */
public class UserADO {

    private final String QUERY_FIND_USER = "SELECT * FROM `user` WHERE `user_name` = ? AND `pswd` = ?";
    private final String QUERY_INSERT_USER = "INSERT INTO `user` ( `user_name`, `pswd`, `id_profile`, `email`,`active`,`image`) VALUES( ?, ?, ?, ?, ?, ?)";
    private final String QUERY_INSERT_OWNER = "INSERT INTO `owner` ( `id_user`, `name`, `firstsurname`, `surname`,`nif`,`birthdate`,`phone_number`,`address`,`id_city_province`) VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String QUERY_SEARCH_USER = "SELECT  u.`id_profile`, u.`user_name`, u.`pswd`, u.`email`, u.`active`, u.`image`,o.`id_user`, o.`name`, o.`firstname`, o.`surname`, o.`nif`, o.`birthdate`, o.`registerdate`, o.`phone_number`, o.`address`, o.`entry_date`, o.`drop_out_date`, o.`id_city_province` FROM user u ,owner o WHERE (u.`user_name` LIKE ? OR u.`email` LIKE ? OR o.`name` LIKE ?) AND u.id = o.id_user";

    public UserADO()
    {

    }

    public UserClass getUserExist(String userName, String password)
    {
        int result = -1;
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
                st.setString(1,userName);
                st.setString(2,password);
                ResultSet rs = st.executeQuery();

                while(rs.next())
                {
                    checkuser = resultsetToUser(rs);
                }
            }

        }catch(Exception e) {
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
    
     public int register(UserClass u, OwnerClass o)
    {
        int result = -1;
        int resultUser = -1;
        PreparedStatement st=null;
            try{
                if(u!=null)
                {

                    ConnectionDB db = new ConnectionDB();
                    Connection conn = db.getConnection();
                    if (conn != null){
                        st = conn.prepareStatement(QUERY_INSERT_USER);
                        st.setString(1,u.getUserName());
                        st.setString(2,u.getPassword());
                        st.setInt(3,u.getIdProfile());
                        st.setString(4,u.getEmail());
                        st.setInt(5,u.getActive());
                        st.setString(6,u.getImage());
                        resultUser = st.executeUpdate();
                        if(resultUser==1) {
                            try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                                if (generatedKeys.next()) {
                                    u.setId((int) generatedKeys.getLong(1));
                                }
                            }

                            st = conn.prepareStatement(QUERY_INSERT_OWNER);
                            st.setInt(1,u.getId());
                            st.setString(2,o.getName());
                            st.setString(3,o.getFirstname());
                            st.setString(4,o.getSurname());
                            st.setString(5,o.getNif());
                            st.setString(6,o.getBirthdate());
                            st.setString(7,o.getPhoneNumber());
                            st.setString(8,o.getAddress());
                            st.setInt(9,o.getIdCityProvince());
                            result = st.executeUpdate();

                        }


                    }
                }
            } catch (SQLException | ClassNotFoundException e){
                result = 0;
            }


        return result;
    }

    private OwnerClass resultsetToOwner(ResultSet rs) throws SQLException {
        OwnerClass o = null;

        UserClass user = resultsetToUser(rs);


        String name = rs.getString("name");
        String firstname = rs.getString("firstname");
        String surname = rs.getString("surname");
        String nif = rs.getString("nif");
        String birthdate = rs.getString("birthdate");
        String registerdate = rs.getString("registerdate");
        String phone_number = rs.getString("phone_number");
        String address = rs.getString("address");
        String entry_date = rs.getString("entry_date");
        String drop_out_date = rs.getString("drop_out_date");
        int id_city_province = rs.getInt("id_city_province");


        o = new OwnerClass(user,name,firstname,surname,nif,birthdate,registerdate,phone_number,address,entry_date,drop_out_date,id_city_province);

        return o;
    }
/*
    public List<OwnerClass> searchUser(UserClass u, OwnerClass o)
    {
        int i = 1;
        List<OwnerClass> ownerList = new ArrayList<>();

        OwnerClass owner=null;

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
                PreparedStatement st = conn.prepareStatement(QUERY_SEARCH_USER);
                st.setString(1,"%"+u.getUserName()+"%");
                st.setString(2,"%"+u.getEmail()+"%");
                st.setString(3,"%"+o.getName()+"%");
                ResultSet rs = st.executeQuery();

                while(rs.next())
                {

                    owner = resultsetToOwner(rs);
                    ownerList.add(owner);
                }
            }

        }catch(SQLException | ClassNotFoundException e) {
            ownerList = null;
        }

        return ownerList;
    }

   */
}
