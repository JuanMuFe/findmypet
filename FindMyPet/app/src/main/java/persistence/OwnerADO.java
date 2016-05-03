package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cat.proven.findmypet.findmypet.ConnectionDB;
import model.OwnerClass;

/**
 * Created by Alumne on 03/05/2016.
 */
public class OwnerADO {
    private final String QUERY_SELECT_OWNERS = "SELECT * FROM `owner`";
    private final String QUERY_INSERT_OWNER = "INSERT INTO `owner` (`id_user`,`name`, `firstname`, `surname`, `nif`, `birthdate`, `registerdate`, `phone_number`, `address`, `entry_date`, `drop_out_date`, `id_city_province`) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
    private final String QUERY_UPDATE_OWNER = "UPDATE `owner` SET `id_user`=?, `name`=?, `firstname`=?, `surname`=?, `nif`=?, `birthdate`=?, `registerdate`=?, `phone_number`=?, `address`=?, `entry_date`=?, `drop_out_date`=?, `id_city_province`=?  where `id`=?";

    public OwnerADO() { }

    public List<OwnerClass> getOwners(){
        int i = 1;
        List<OwnerClass> ownerList = new ArrayList<>();
        OwnerClass owner=null;
        Statement sentencia;

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
                sentencia = conn.createStatement();
                ResultSet rs = sentencia.executeQuery(QUERY_SELECT_OWNERS);

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

    public int addOwner(OwnerClass o)
    {
        int result = -1;
        PreparedStatement st=null;

        try{
            if(o!=null)
            {

                ConnectionDB db = new ConnectionDB();
                Connection conn = db.getConnection();
                if (conn != null){
                    st = conn.prepareStatement(QUERY_INSERT_OWNER);
                    st.setInt(1,o.getIdUser());
                    st.setString(2,o.getName());
                    st.setString(3,o.getFirstname());
                    st.setString(4,o.getSurname());
                    st.setString(5,o.getNif());
                    st.setString(6,o.getBirthdate());
                    st.setString(7,o.getRegisterDate());
                    st.setString(8,o.getPhoneNumber());
                    st.setString(9,o.getAddress());
                    st.setString(10,o.getEntryDate());
                    st.setString(11,o.getDropOutDate());
                    st.setInt(12,o.getIdCityProvince());

                    result = st.executeUpdate();
                }
            }
        } catch (SQLException | ClassNotFoundException e){
            result = 0;
        }
        return result;
    }

    public int modifyOwner(OwnerClass o)
    {
        int result = -1;
        PreparedStatement st=null;

        try{
            if(o!=null)
            {
                ConnectionDB db = new ConnectionDB();
                Connection conn = db.getConnection();

                if (conn != null){
                    st = conn.prepareStatement(QUERY_UPDATE_OWNER);
                    st.setInt(1,o.getIdUser());
                    st.setString(2,o.getName());
                    st.setString(3,o.getFirstname());
                    st.setString(4,o.getSurname());
                    st.setString(5,o.getNif());
                    st.setString(6,o.getBirthdate());
                    st.setString(7,o.getRegisterDate());
                    st.setString(8,o.getPhoneNumber());
                    st.setString(9,o.getAddress());
                    st.setString(10,o.getEntryDate());
                    st.setString(11,o.getDropOutDate());
                    st.setInt(12,o.getIdCityProvince());
                    st.setInt(13,o.getId());
                    result = st.executeUpdate();
                }
            }
        } catch (SQLException | ClassNotFoundException e){
            result = 0;
        }
        return result;
    }

    private OwnerClass resultsetToOwner(ResultSet rs) throws SQLException {
        OwnerClass o = null;
        int id = rs.getInt("id");
        int id_user = rs.getInt("id_user");
        String name = rs.getString("name");
        String firstname = rs.getString("firstname");
        String surname = rs.getString("surname");
        String nif = rs.getString("nif");
        String birthdate = rs.getString("nirthdate");
        String registerdate = rs.getString("registerdate");
        String phone_number = rs.getString("phone_number");
        String address = rs.getString("address");
        String entry_date = rs.getString("entry_date");
        String drop_out_date = rs.getString("drop_out_date");
        int id_city_province = rs.getInt("id_city_province");


        o = new OwnerClass(id, id_user, name, firstname, surname, nif, birthdate, registerdate, phone_number, address, entry_date, drop_out_date, id_city_province);
        return o;
    }
}
