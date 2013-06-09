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
public class PeriodRisk {
    
    // Attributes
    public String forwardType;
    public Date startDate;
    public Date endDate;
    public String period;
    public double dailyQuantity;
    public double total;
    
    public PeriodRisk(String forwardType, Date startDate, Date endDate,
            String period, double dailyQuantity){
        this.forwardType = forwardType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.period = period;
        this.dailyQuantity = dailyQuantity;
        
        // 
        int days = (int) ((endDate.getTime() - startDate.getTime()) / (24 * 3600));
        this.total = days * dailyQuantity;
    }
}
