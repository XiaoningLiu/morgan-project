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
    public Date date;
    public double pnl;
    public double pv1;
    public double pv0;
    
    public DailyPnl(Date date, double pnl, double pv1, double pv0){
        this.date = date;
        this.pnl = pnl;
        this.pv1 = pv1;
        this.pv0 = pv0;
    }
}
