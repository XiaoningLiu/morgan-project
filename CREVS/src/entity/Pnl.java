/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author liuxiaoning
 */
public class Pnl {

    // Attributes
    public int tradeId;
    public List<DailyPnl> dailyPnls= new ArrayList<>();

    public Pnl(){}

    public Pnl(Connection con, int tradeId){
        // get all Pnl with the tradeId
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT date, pv FROM pv where tradeid=" +
                    tradeId + " order by date DESC");
            rs = stmt.getResultSet();

            List<Date> dateResults = new ArrayList<Date>();
            List<Double> pvResults = new ArrayList<Double>();
            while ( rs.next() ){
                dateResults.add(rs.getDate("date"));
                pvResults.add(rs.getDouble("pv"));
            }
            rs.close();
            rs = null;

            if (dateResults.size() > 1){
                for (int i = 0; i < dateResults.size() - 1; i++){

                    long timeToday = dateResults.get(i).getTime();
                    long timeYes = dateResults.get(i+1).getTime();

                    if ( timeToday == timeYes + 86400000){
                        DailyPnl dailyPnl = new DailyPnl(tradeId,
                            dateResults.get(i), pvResults.get(i),
                            pvResults.get(i+1));
                       dailyPnls.add(dailyPnl);
                    }
                }
            }
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                } // ignore
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                } // ignore
                stmt = null;
            }
        }
    }

    public void saveToDB(Connection con){
        // save to table crevs.pv
        for(int i = 0; i < dailyPnls.size(); i++){
            dailyPnls.get(i).saveToDB(con);
        }
    }
    
    public boolean addDailyPnl(DailyPnl dailyPnl){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < this.dailyPnls.size(); i++){
            String date = sdf.format(dailyPnls.get(i).date);
            String newDate = sdf.format(dailyPnl.date);
            if (date.equals(newDate)){
                return false;
            }
        }
        
        dailyPnls.add(dailyPnl);
        return true;
    }


}