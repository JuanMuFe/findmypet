package persistence;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cat.proven.findmypet.findmypet.ConnectionDB;
import model.ProvinceClass;

/**
 * Created by Alumne on 30/04/2016.
 */
public class ProvinceADO {

    private final String QUERY_SELECT_PROVINCES = "SELECT * FROM `province`";

    public ProvinceADO(){ }

    public List<ProvinceClass> getProvinces(){
        int i = 1;
        List<ProvinceClass> provincesList = new ArrayList<>();
        ProvinceClass province=null;
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
                ResultSet rs = sentencia.executeQuery(QUERY_SELECT_PROVINCES);

                while(rs.next())
                {
                    province = resultsetToProvince(rs);
                    provincesList.add(province);
                }
            }

        }catch(SQLException | ClassNotFoundException e) {
            provincesList = null;
        }

        return provincesList;
    }

    private ProvinceClass resultsetToProvince(ResultSet rs) throws SQLException {
        ProvinceClass p = null;
        int id = rs.getInt("id");
        String name = rs.getString("name");

        p = new ProvinceClass(id,name);
        return p;
    }

}
