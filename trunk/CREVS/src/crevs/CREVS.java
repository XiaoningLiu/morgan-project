/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package crevs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import ui.Load;
// Notice, do not import com.mysql.jdbc.*
// or you will have problems!

/**
 *
 * @author liuxiaoning
 */
public class CREVS {

    //private static Connection con = null;
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
        }

        // create connection
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            con =
                    DriverManager.getConnection(getDBurlFromFile("dbconfig"));

            // Do something with the Connection
            /*
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM user");
            if (stmt.execute("SELECT * FROM user")) {
                rs = stmt.getResultSet();
            }
            
            while ( rs.next() ){
                System.out.println(rs.getString("psd"));
            }
            */

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
        new Load(con).setVisible(true);
        
    }
    
    private static String getDBurlFromFile(String fileName){
        String defaultDBurl = "jdbc:mysql://localhost/crevs?"
                    + "user=root&password=idonotknowthekey";
        File file = new File(fileName);
        if ( !file.exists() ){
            return defaultDBurl;
        }
        
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            tempString = reader.readLine();
            reader.close();
            if (tempString != null){
                return tempString;
            } else {
                return defaultDBurl;
            }            
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return defaultDBurl;
    }
}
