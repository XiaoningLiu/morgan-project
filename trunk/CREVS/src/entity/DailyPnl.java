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
import java.util.Date;

/**
 *
 * @author liuxiaoning
 */
public class DailyPnl {
    
    // Atributes
    public int tradeId;
    public Date date;
    public double pnl;
    public double pvToday;
    public double pvYest;
    
    public DailyPnl(int tradeId, Date date, double pnl, double pv1, double pv0){
        this.tradeId = tradeId;
        this.date = date;
        this.pnl = pnl;
        this.pvToday = pv1;
        this.pvYest = pv0;
    }

    public DailyPnl(int tradeId, Date date, double pvtoday, double pvyest) {
        this.tradeId = tradeId;
        this.date = date;
        this.pvToday = pvtoday;
        this.pvYest = pvyest;
        this.pnl = pvtoday - pvyest;
    }
    
    public void saveToDB(Connection con){
        // if not saved save to database 
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_UPDATABLE);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            // check if existence or not
            stmt.executeQuery("select date from pv where tradeId=" + this.tradeId
                    + " and date='" + sdf.format(this.date) + "'");
            rs = stmt.getResultSet();
            if (rs.next()){
                // there are the date 
                return;
            }

            stmt.executeUpdate("insert into pv values('" + sdf.format(date)
                    + "'," + this.tradeId + " ," + this.pvToday + ")");

            rs.close();
            rs = null;

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
}
