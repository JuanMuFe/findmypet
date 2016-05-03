package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cat.proven.findmypet.findmypet.ConnectionDB;
import model.ParkClass;

/**
 * Created by Alumne on 03/05/2016.
 */
public class ParkADO {
    private final String QUERY_SELECT_PARKS = "SELECT * FROM `park`";
    private final String QUERY_INSERT_PARK = "INSERT INTO `park` (`name`,`address`, `id_city_province`) VALUES(?,?,?)";
    private final String QUERY_UPDATE_PARK = "UPDATE `park` SET `name`=?, `address`=?, `id_city_province`=? where `id`=?";

    public ParkADO() { }

    public List<ParkClass> getNotifications(){
        int i = 1;
        List<ParkClass> parkList = new ArrayList<>();
        ParkClass park=null;
        Statement sentencia;

        try{
            ConnectionDB db = new ConnectionDB();
            Connection conn = null;
            try {
                conn = db.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (conn != null){
                sentencia = conn.createStatement();
                ResultSet rs = sentencia.executeQuery(QUERY_SELECT_PARKS);

                while(rs.next())
                {
                    park = resultsetToPark(rs);
                    parkList.add(park);
                }
            }

        }catch(SQLException | ClassNotFoundException e) {
            parkList = null;
        }

        return parkList;
    }

    public int addPark(ParkClass p)
    {
        int result = -1;
        PreparedStatement st=null;

        try{
            if(p!=null)
            {

                ConnectionDB db = new ConnectionDB();
                Connection conn = db.getConnection();
                if (conn != null){
                    st = conn.prepareStatement(QUERY_INSERT_PARK);
                    st.setString(1,p.getName());
                    st.setString(2,p.getAddress());
                    st.setInt(3,p.getIdCityProvince());

                    result = st.executeUpdate();
                }
            }
        } catch (SQLException | ClassNotFoundException e){
            result = 0;
        }
        return result;
    }

    public int modifyPark(ParkClass p)
    {
        int result = -1;

        PreparedStatement st=null;
        try{
            if(p!=null)
            {
                ConnectionDB db = new ConnectionDB();
                Connection conn = db.getConnection();
                if (conn != null){
                    st = conn.prepareStatement(QUERY_UPDATE_PARK);
                    st.setString(1,p.getName());
                    st.setString(2,p.getAddress());
                    st.setInt(3,p.getIdCityProvince());
                    st.setInt(4,p.getId());
                    result = st.executeUpdate();
                }
            }
        } catch (SQLException | ClassNotFoundException e){
            result = 0;
        }
        return result;
    }


    private ParkClass resultsetToPark(ResultSet rs) throws SQLException {
        ParkClass p = null;
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String address = rs.getString("address");
        int id_city_province = rs.getInt("id_city_province");

        p = new ParkClass(id, name, address, id_city_province);
        return p;
    }
}
