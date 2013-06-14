/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import engine.Engine;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author liuxiaoning
 */
public class Swap {

    // Atributes, to be dealt with database design
    public int tradeId;
    public String traderId;
    public Date bookingDate;
    public String counterparty;
    public String buyOrSell; // "buy" or "sell"
    public double fixedPrice;
    public String floatingCode; // default is "WTINY1nB"
    public int quantity;
    public Date startDate;
    public Date endDate;
    public String settleDateSpec; // settlement date specification

    public Swap() {
        // default empty class
    }

    public Swap(Connection con, int swapId) {
        // initialize swap from database
    }

    public int saveToDB(Connection con) {
        // save this swap to database
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = con.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                    java.sql.ResultSet.CONCUR_UPDATABLE);
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            stmt.executeUpdate(
                    "INSERT INTO `crevs`.`swap` "
                    + "(`traderid`, `bookingdate`, `counterparty`, "
                    + " `buyorsell`, `fixedprice`, `floatingquotecode`, "
                    + " `quantity`, `pricingperiodstart`, `pricingperiodend`, "
                    + " `settlementspecification`) VALUES ( '"
                    + this.traderId + "', '"
                    + sdf.format(date) + "', '"
                    + this.counterparty + "', '"
                    + this.buyOrSell + "', '"
                    + this.fixedPrice + "', '"
                    + this.floatingCode + "', '"
                    + this.quantity + "', '"
                    + sdf.format(this.startDate) + "', '"
                    + sdf.format(this.endDate) + "', '"
                    + this.settleDateSpec + "')",
                    Statement.RETURN_GENERATED_KEYS);

            int autoIncKeyFromApi = -1;
            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                autoIncKeyFromApi = rs.getInt(1);
            } else {
            }
            rs.close();
            rs = null;

            this.tradeId = autoIncKeyFromApi;
            return autoIncKeyFromApi;
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
        return -1;
    }
}
