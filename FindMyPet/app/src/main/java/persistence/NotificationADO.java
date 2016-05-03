package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cat.proven.findmypet.findmypet.ConnectionDB;
import model.NotificationClass;

/**
 * Created by Alumne on 03/05/2016.
 */
public class NotificationADO {
    private final String QUERY_SELECT_NOTIFICATION = "SELECT * FROM `notification`";
    private final String QUERY_INSERT_NOTIFICATION = "INSERT INTO `notification` (`active`,`description`) VALUES(?,?)";
    private final String QUERY_UPDATE_NOTIFICATION = "UPDATE `notification` SET `active`=?, `description`=? where `id`=?";
    private final String QUERY_UPDATE_ACTIVE_NOTIFICATION = "UPDATE `notification` SET `active`=? WHERE `id`=?";

    public NotificationADO(){  }

    public List<NotificationClass> getNotifications(){
        int i = 1;
        List<NotificationClass> notificationList = new ArrayList<>();
        NotificationClass notification=null;
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
                ResultSet rs = sentencia.executeQuery(QUERY_SELECT_NOTIFICATION);

                while(rs.next())
                {
                    notification = resultsetToNotification(rs);
                    notificationList.add(notification);
                }
            }

        }catch(SQLException | ClassNotFoundException e) {
            notificationList = null;
        }

        return notificationList;
    }

    public int addNotification(NotificationClass n)
    {
        int result = -1;

        PreparedStatement st=null;
        try{
            if(n!=null)
            {

                ConnectionDB db = new ConnectionDB();
                Connection conn = db.getConnection();
                if (conn != null){
                    st = conn.prepareStatement(QUERY_INSERT_NOTIFICATION);
                    st.setInt(1,n.getId());
                    st.setInt(2,n.getActive());
                    st.setString(3,n.getDescription());

                    result = st.executeUpdate();
                }
            }
        } catch (SQLException | ClassNotFoundException e){
            result = 0;
        }
        return result;
    }

    public int modifyNotification(NotificationClass n)
    {
        int result = -1;

        PreparedStatement st=null;
        try{
            if(n!=null)
            {

                ConnectionDB db = new ConnectionDB();
                Connection conn = db.getConnection();
                if (conn != null){
                    st = conn.prepareStatement(QUERY_UPDATE_NOTIFICATION);
                    st.setInt(1,n.getActive());
                    st.setString(2,n.getDescription());
                    st.setInt(3,n.getId());
                    result = st.executeUpdate();
                }
            }
        } catch (SQLException | ClassNotFoundException e){
            result = 0;
        }
        return result;
    }

    public int modifyNotificationActive(NotificationClass n)
    {
        int result = -1;
        PreparedStatement st=null;

        try{
            if(n!=null)
            {
                ConnectionDB db = new ConnectionDB();
                Connection conn = db.getConnection();
                if (conn != null){
                    st = conn.prepareStatement(QUERY_UPDATE_ACTIVE_NOTIFICATION);
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

    private NotificationClass resultsetToNotification(ResultSet rs) throws SQLException {
        NotificationClass n = null;
        int id = rs.getInt("id");
        int active = rs.getInt("active");
        String description = rs.getString("description");

        n = new NotificationClass(id, active, description);
        return n;
    }
}
