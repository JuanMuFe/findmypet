package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cat.proven.findmypet.findmypet.ConnectionDB;
import model.OwnerClass;
import model.PetClass;
import model.ReportClass;

/**
 * Created by Alumne on 30/04/2016.
 */
public class ReportADO {

    private final String QUERY_SELECT_OWNER_REPORTS = "SELECT * FROM `report` WHERE `id_owner`=?";
    private final String QUERY_SELECT_PET_REPORTS = "SELECT * FROM `report` WHERE `id_pet`=?";
    private final String QUERY_INSERT_REPORT = "INSERT INTO `report` (`id_owner`,`id_pet`,`entry_date`, `location`, `extra`) VALUES (?,?,?,?,?)";
    private final String QUERY_UPDATE_REPORT = "UPDATE `report` SET `location`=?, `extra`=? WHERE `id_owner`=? AND `id_pet`=?";
    private final String QUERY_UPDATE_REPORT_FINISHED = "UPDATE `report` SET `finished`=? WHERE `id_owner`=? AND `id_pet`=?";

    public ReportADO(){ }

    public List<ReportClass> searchOwnerReports(OwnerClass o){
        int i = 1;
        List<ReportClass> reportList = new ArrayList<>();
        ReportClass report=null;

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
                PreparedStatement st = conn.prepareStatement(QUERY_SELECT_OWNER_REPORTS);
                st.setInt(1,o.getId());
                ResultSet rs = st.executeQuery();

                while(rs.next())
                {
                    report = resultsetToReport(rs);
                    reportList.add(report);
                }
            }

        }catch(SQLException | ClassNotFoundException e) {
            reportList = null;
        }

        return reportList;
    }

    public int updateReport(ReportClass r)
    {
        int result = -1;

        PreparedStatement st=null;
        try{
            if(r!=null)
            {

                ConnectionDB db = new ConnectionDB();
                Connection conn = db.getConnection();
                if (conn != null){
                    st = conn.prepareStatement(QUERY_UPDATE_REPORT);
                    st.setString(1,r.getLocation());
                    st.setInt(2,r.getIdOwner());
                    st.setInt(3,r.getIdPet());

                    result = st.executeUpdate();
                }
            }
        } catch (SQLException | ClassNotFoundException e){
            result = 0;
        }

        return result;
    }

    public int updateReportFinished(ReportClass r)
    {
        int result = -1;

        PreparedStatement st=null;
        try{
            if(r!=null)
            {

                ConnectionDB db = new ConnectionDB();
                Connection conn = db.getConnection();
                if (conn != null){
                    st = conn.prepareStatement(QUERY_UPDATE_REPORT_FINISHED);
                    st.setInt(1, 1);
                    st.setInt(2,r.getIdOwner());
                    st.setInt(3,r.getIdPet());

                    result = st.executeUpdate();
                }
            }
        } catch (SQLException | ClassNotFoundException e){
            result = 0;
        }

        return result;
    }

    public List<ReportClass> searchPetReports(PetClass p){
        int i = 1;
        List<ReportClass> reportList = new ArrayList<>();
        ReportClass report=null;

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
                PreparedStatement st = conn.prepareStatement(QUERY_SELECT_PET_REPORTS);
                st.setInt(1,p.getId());
                ResultSet rs = st.executeQuery();

                while(rs.next())
                {
                    report = resultsetToReport(rs);
                    reportList.add(report);
                }
            }

        }catch(SQLException | ClassNotFoundException e) {
            reportList = null;
        }

        return reportList;
    }

    public int insertReport(ReportClass r){
        int result = -1;

        PreparedStatement st=null;
        try{
            if(r!=null) {
                ConnectionDB db = new ConnectionDB();
                Connection conn = db.getConnection();
                if (conn != null){
                    st = conn.prepareStatement(QUERY_INSERT_REPORT);
                    st.setInt(1,r.getIdOwner());
                    st.setInt(2,r.getIdPet());
                    st.setString(3,r.getEntryDate());
                    st.setString(4,r.getLocation());
                    st.setString(5,r.getExtra());

                    result = st.executeUpdate();
                }
            }
        } catch (SQLException | ClassNotFoundException e){
            result = 0;
        }

        return result;
    }

    private ReportClass resultsetToReport(ResultSet rs) throws SQLException {
        ReportClass n = null;

        int id = rs.getInt("id");
        int id_owner = rs.getInt("id_owner");
        int id_pet = rs.getInt("id_pet");
        String date = rs.getString("entry_date");
        String location = rs.getString("location");
        String extra = rs.getString("extra");

        n = new ReportClass(id,id_owner,id_pet,date,location,extra);
        return n;
    }


}
