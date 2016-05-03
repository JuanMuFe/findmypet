package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cat.proven.findmypet.findmypet.ConnectionDB;
import model.NotificationClass;
import model.OwnerClass;
import model.PetClass;
import model.UserClass;
import model.UserNotificationClass;

/**
 * Created by Alumne on 30/04/2016.
 */
public class UserNotificationADO {

    private final String QUERY_UPDATE_NOTIFICATION_READED= "UPDATE `user_notification` SET `readed`=? WHERE `id_user`=? AND `id_notification`=?";
    private final String QUERY_ADD_NOTIFICATION_USER = "INSERT INTO `user_notification` (`id_user`,`id_notification`,`readed`) VALUES (?,?,?)";
    private final String QUERY_SEARCH_USER_NOTIFICATIONS = "SELECT * FROM `notification` WHERE `id` IN (SELECT `id_notification` FROM `user_notification` WHERE `id_user`=?)";

    public UserNotificationADO()
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

    public int setNotificationAsReaded(UserNotificationClass n)
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
                    st.setInt(1,n.getIdUser());
                    st.setInt(2,n.getIdNotification());
                    st.setInt(3, 1);

                    result = st.executeUpdate();
                }
            }
        } catch (SQLException | ClassNotFoundException e){
            result = 0;
        }

        return result;
    }

    public int sendNotificationToUser(UserNotificationClass n){
        int result = -1;

        PreparedStatement st=null;
        try{
            if(n!=null) {
                ConnectionDB db = new ConnectionDB();
                Connection conn = db.getConnection();
                if (conn != null){
                    st = conn.prepareStatement(QUERY_ADD_NOTIFICATION_USER);
                    st.setInt(1,n.getIdUser());
                    st.setInt(2,n.getIdNotification());

                    result = st.executeUpdate();
                }
            }
        } catch (SQLException | ClassNotFoundException e){
            result = 0;
        }

        return result;
    }

    public List<NotificationClass> searchUserNotifications(UserClass u){  //igual se cambia
        int i = 1;
        List<NotificationClass> notificationList = new ArrayList<>();

        NotificationClass notification=null;

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
                PreparedStatement st = conn.prepareStatement(QUERY_SEARCH_USER_NOTIFICATIONS);
                st.setInt(1,u.getId());
                ResultSet rs = st.executeQuery();

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


}
