package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cat.proven.findmypet.findmypet.ConnectionDB;
import model.OwnerClass;
import model.PetClass;
import model.UserClass;

/**
 * Created by Alumne on 30/04/2016.
 */
public class PetADO {

    private final String QUERY_SELECT_pet= "SELECT `id_owner`, `name`, `race`, `image`,`descrition` FROM `pet`";
    private final String QUERY_INSERT_PET = "INSERT INTO `pet` ( `id_owner`, `name`, `race`, `image`,`descrition`) VALUES( ?,?,?,?,?)";
    private final String QUERY_UPDATE_PET = "UPDATE `pet` SET `id_owner`=?,  `name`=?, `race`=?,`image`=?,`descrition`=? where `id`=?";


    public PetADO()
    {

    }



    private PetClass resultsetToPet(ResultSet rs) throws SQLException {
        PetClass p = null;
        int id = rs.getInt("id");
        int id_owner = rs.getInt("id_owner");
        String name = rs.getString("name");
        String race = rs.getString("race");
        String image = rs.getString("image");
        String description = rs.getString("description");


        p = new PetClass(id,id_owner,name,race,image,description);

        return p;
    }

    public int addPet(PetClass p)
    {
        int result = -1;

        PreparedStatement st=null;
            try{
                if(p!=null)
                {

                    ConnectionDB db = new ConnectionDB();
                    Connection conn = db.getConnection();
                    if (conn != null){
                        st = conn.prepareStatement(QUERY_INSERT_PET);
                        st.setInt(1,p.getIdOwner());
                        st.setString(2,p.getName());
                        st.setString(3,p.getRace());
                        st.setString(4,p.getImage());
                        st.setString(5,p.getDescription());

                        result = st.executeUpdate();

                    }
                }
            } catch (SQLException | ClassNotFoundException e){
                result = 0;
            }


        return result;
    }

    public int modifyPet(PetClass p)
    {
        int result = -1;

        PreparedStatement st=null;
        try{
            if(p!=null)
            {

                ConnectionDB db = new ConnectionDB();
                Connection conn = db.getConnection();
                if (conn != null){
                    st = conn.prepareStatement(QUERY_UPDATE_PET);
                    st.setInt(1,p.getIdOwner());
                    st.setString(2,p.getName());
                    st.setString(3,p.getRace());
                    st.setString(4,p.getImage());
                    st.setString(5,p.getDescription());
                    st.setInt(6,p.getId());
                    result = st.executeUpdate();

                }
            }
        } catch (SQLException | ClassNotFoundException e){
            result = 0;
        }


        return result;
    }
}
