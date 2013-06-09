/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import engine.Engine;
import java.util.Date;

/**
 *
 * @author liuxiaoning
 */
public class Swap {
    
    // Atributes, to be dealt with database design
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
    
    public Swap(Con con, int swapId){
        // initialize swap from database
    }

    public void saveToDB(Con con){
        // save this swap to database
    }
}
