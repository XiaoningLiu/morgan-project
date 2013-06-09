/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import entity.Swap;
import entity.Pnl;
import entity.DailyPnl;
import java.util.Date; 
import java.util.Calendar; 
import java.sql.Connection;
/**
 *
 * @author liuxiaoning
 */
public class Engine {
    public static Connection con;
    public Engine(Connection con){
        this.con=con;
    }
    
    public static Pnl calPnl(Swap swap){
        Pnl result=new Pnl(con, swap.tradeId);
        Date now=new Date();
        Calendar calNow=Calendar.getInstance();
        calNow.setTime(now);
        while(true){
            //get d,m,y
            int d=calNow.get(Calendar.DATE);
            int m=calNow.get(Calendar.MONTH);
            int y=calNow.get(Calendar.YEAR);
            
            //get avg floating price:
            double AFP=AvgFloatingPrice.avgFolatingPrice(d, m, y, avgCalWay(now,swap.startDate));
            System.out.print(AFP);
            if(AFP<=0)break;
            
            //use expression to calculate
            int bos=1;//buy or sell
            if(swap.buyOrSell.equals("sell"))bos=-1;
            double PV=bos*(swap.fixedPrice-AFP)*swap.quantity;
            
            //go to former day
            now=yesterdayDate(now);
            calNow.setTime(now);
        }
        
        return result;
    }
    private static Date yesterdayDate(Date date){
        Calendar c = Calendar.getInstance();  
        c.setTime(date);  
        int day = c.get(Calendar.DATE);  
        c.set(Calendar.DATE, day - 1); 
        Date result=c.getTime();
        return result;
    }
    private static int avgCalWay(Date date,Date startDate){
        if(date.before(startDate))
            return 1;
        else
            return 2;
    }
    //main for test
    public static void main(String[] args) {
        
    }
    
}
