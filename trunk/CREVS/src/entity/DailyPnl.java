/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

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
    
    public DailyPnl(Date date, double pnl, double pv1, double pv0){
        this.date = date;
        this.pnl = pnl;
        this.pvToday = pv1;
        this.pvYest = pv0;
    }
    
    public saveToDB(Con con){
        // if not saved save to database
    }
    
    
}
