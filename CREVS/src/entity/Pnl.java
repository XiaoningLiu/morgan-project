/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author liuxiaoning
 */
public class Pnl {
    
    // Attributes
    public List<DailyPnl> a = new ArrayList<>();
    public int tradeId;
    
    public Pnl(){
        // default empty 
    }
    
    public Pnl(Con con, int tradeId){
        // get all Pnl with the tradeId
    }
    
    public saveToDB(Con con){
        // save to table crevs.pv
    }
}
