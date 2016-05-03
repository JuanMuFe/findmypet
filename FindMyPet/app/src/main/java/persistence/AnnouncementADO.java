package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import cat.proven.findmypet.findmypet.ConnectionDB;
import model.AnnouncementClass;

/**
 * Created by Alumne on 03/05/2016.
 */
public class AnnouncementADO {

    private final String QUERY_SELECT_ANNOUNCEMENTS = "SELECT * FROM `announcement`";
    private final String QUERY_SELECT_OWNER_ANNOUNCEMENTS = "SELECT * FROM `announcement` WHERE `id_user`=?";
    private final String QUERY_DELETE_ANNOUNCEMENT = "DELETE FROM `announcement` WHERE `id` = ?";

    public AnnouncementADO(){ }

    public List<AnnouncementClass> getProvinces(){
        int i = 1;
        List<AnnouncementClass> announcementList = new ArrayList<>();
        AnnouncementClass announcement=null;
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
                ResultSet rs = sentencia.executeQuery(QUERY_SELECT_ANNOUNCEMENTS);

                while(rs.next())
                {
                    announcement = resultsetToAnnouncement(rs);
                    announcementList.add(announcement);
                }
            }

        }catch(SQLException | ClassNotFoundException e) {
            announcementList = null;
        }

        return announcementList;
    }

    public List<AnnouncementClass> searchOwnerReports(AnnouncementClass a){
        int i = 1;
        List<AnnouncementClass> announcementList = new ArrayList<>();
        AnnouncementClass announcement=null;

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
                PreparedStatement st = conn.prepareStatement(QUERY_SELECT_OWNER_ANNOUNCEMENTS);
                st.setInt(1, a.getIdUser());
                ResultSet rs = st.executeQuery();

                while(rs.next())
                {
                    announcement = resultsetToAnnouncement(rs);
                    announcementList.add(announcement);
                }
            }

        }catch(SQLException | ClassNotFoundException e) {
            announcementList = null;
        }

        return announcementList;
    }

    public int deleteAnnouncement(AnnouncementClass a){
        int result = -1;
        PreparedStatement st=null;

        try{
            if(a!=null)
            {
                ConnectionDB db = new ConnectionDB();
                Connection conn = db.getConnection();
                if (conn != null){
                    st = conn.prepareStatement(QUERY_DELETE_ANNOUNCEMENT);
                    st.setInt(1,a.getId());
                    result = st.executeUpdate();
                }
            }
        } catch (SQLException | ClassNotFoundException e){
            result = 0;
        }

        return result;
    }

    private AnnouncementClass resultsetToAnnouncement(ResultSet rs) throws SQLException {
        AnnouncementClass a = null;
        int id = rs.getInt("id");
        String description = rs.getString("description");
        String date = rs.getString("date");
        int id_user = rs.getInt("id_user");

        a = new AnnouncementClass(id,description, date, id_user);
        return a;
    }

}
