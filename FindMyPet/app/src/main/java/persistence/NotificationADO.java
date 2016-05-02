package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cat.proven.findmypet.findmypet.ConnectionDB;
import model.NotificationClass;
import model.PetClass;

/**
 * Created by Alumne on 30/04/2016.
 */
public class NotificationADO {

    private final String QUERY_UPDATE_NOTIFICATION_READED= "UPDATE `notification` SET `active`=? where `id` = ?";
    private final String SELECT_NOTIFICATION= "UPDATE `notification` SET `active`=? where `id` = ?";


    public NotificationADO()
    {

    }



    private NotificationClass resultsetToNotification(ResultSet rs) throws SQLException {

        NotificationClass n = null;
        int id = rs.getInt("id");
        int active = rs.getInt("active");
        String description = rs.getString("description");


        n = new NotificationClass(id,active,description);

        return n;
    }



    public int setNotificationAsReaded(NotificationClass n)
    {
        int result = -1;

        PreparedStatement st=null;
        try{
            if(n!=null)
            {

                ConnectionDB db = new ConnectionDB();
                Connection conn = db.getConnection();
                if (conn != null){
                    st = conn.prepareStatement(QUERY_UPDATE_NOTIFICATION_READED);
                    st.setInt(1,n.getActive());
                    st.setInt(2,n.getId());

                    result = st.executeUpdate();

                }
            }
        } catch (SQLException | ClassNotFoundException e){
            result = 0;
        }


        return result;
    }




}
