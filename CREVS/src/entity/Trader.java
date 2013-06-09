/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author liuxiaoning
 */
public class Trader {
    
    public String traderId;
    private String psd;
    public String name;
    
    public Trader(String id, String psd, String name){
        this.traderId = id;
        this.psd = psd;
        this.name = name;
    }
    
    // get Trader from database
    public Trader(Connection con, String traderId){
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT name, password FROM trader where "
                    + "traderId='" + traderId + "'");
            rs = stmt.getResultSet();
            
            if ( rs.next() ){
                this.traderId = traderId;
                this.psd = rs.getString("password");
                this.name = rs.getString("name");
            } else {
                this.traderId = null;
                this.psd = null;
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
    
    public boolean checkPsd(String psd){
        if (psd.equals(this.psd)){
            return true;
        }
        return false;
    }
}
