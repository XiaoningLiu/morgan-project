/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package engine;

import entity.Swap;
import entity.Pnl;
import entity.DailyPnl;
import entity.PeriodRisk;
import entity.Risk;
import java.util.List;
import java.util.Date; 
import java.util.Calendar; 
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
/**
 *
 * @author liuxiaoning
 */
public class Engine {
    private Connection con;
    public Engine(Connection con){
        this.con=con;
    }
    
    private DailyPnl tmpDP=new DailyPnl(-1,new Date(),0.0,0.0);
    
    public Pnl calPnl(Swap swap){
        //Pnl result = new Pnl(con, swap.tradeId);
        Pnl result=new Pnl();
        result.tradeId = swap.tradeId;
        
        int least=5;//least repeat time
        Date now=new Date();
        Calendar calNow=Calendar.getInstance();
        calNow.setTime(now);
        while(true){
            least--;
            //get d,m,y
            int d=calNow.get(Calendar.DATE);
            int m=calNow.get(Calendar.MONTH)+1;
            int y=calNow.get(Calendar.YEAR);
            
            //get avg floating price:
            double AFP=AvgFloatingPrice.avgFolatingPrice(d, m, y, avgCalWay(now,swap.startDate));
            if(AFP<=0&&least<=0)break;//AFP:0.-1.-2 means sth. is wrong, you can check the info in AvgFloatingPrice.java
            
            if(AFP>0){
                //use expression to calculate
                int bos=1;//buy or sell
                if(swap.buyOrSell.equals("sell"))bos=-1;
                double PV=bos*(swap.fixedPrice-AFP)*swap.quantity;

                //add DailyPnl to result
                DailyPnl addDP;
                if(tmpDP.tradeId==-1)
                    tmpDP=new DailyPnl(swap.tradeId,now,PV,PV);
                else
                {
                    addDP=new DailyPnl(swap.tradeId,now,PV,tmpDP.pvYest);
                    tmpDP=addDP;
                    if(!result.addDailyPnl(addDP))
                        System.out.println("failed to add dailyPnl!");
                }
                

            }
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
    /*
    public static void main(String[] args) {
        
        //initialize a swap for test
        Swap swap=new Swap();
        //tradeId:
        swap.tradeId=1;
        //startDate:
        Calendar FirstDate = Calendar.getInstance();
        FirstDate.set(Calendar.DATE, 1);// 设为当前月的1号
        swap.startDate=FirstDate.getTime();
        //endDate:
        Calendar EndDate = Calendar.getInstance();
        EndDate.set(Calendar.DATE, 30);// 设为当前月的30号
        swap.endDate=EndDate.getTime();
        //fixed price:
        swap.fixedPrice=99;
        //buy/sell:
        swap.buyOrSell="sell";
        //quantity:
        swap.quantity=80;
        
        //test:
        //Pnl PnlTest= engine.calPnl(swap);
        List<DailyPnl> DailyPnlTest=PnlTest.dailyPnls;
        for(int i=0;i<DailyPnlTest.size();i++)
        {
            //set format
            //date
            Calendar c=Calendar.getInstance();
            c.setTime(DailyPnlTest.get(i).date);
            String Date=c.get(Calendar.YEAR)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.DATE);
            //pnl
            double pnl=Math.round(DailyPnlTest.get(i).pnl*100)/100.0;
            //pv
            double PV=Math.round(DailyPnlTest.get(i).pvToday*100)/100.0;
            
            
            System.out.println(Date+"\t"+pnl+"\t"+PV);
        }
        
    }
    */
        
    public Risk calRisk(Swap swap){
        Risk risk = new Risk();
        
        // get daily quantity
        double dailyQuantity = swap.quantity / getIntervalDays(
                swap.startDate, swap.endDate);
        
        // get all months from trade start to end period
        List<Date> firstDays = new ArrayList<>();
        firstDays.add(swap.startDate);
        Date tmpDate = getNextMonFirstDay(swap.startDate);
        while (tmpDate.before(swap.endDate)){
            firstDays.add(tmpDate);
            tmpDate = getNextMonFirstDay(tmpDate);
        }
        
        // get partition day for every month
        List<Date> partitionDays = new ArrayList<>();
        for (int i = 0; i < firstDays.size(); i++){
            partitionDays.add(getPartitionDay(firstDays.get(i)));
        }
        
        /* get time periods */
        // first period
        Date startDate = getThisMonFirstDay(partitionDays.get(0));
        Date endDate = partitionDays.get(0);
        String period = getNextMonthString(endDate);
        PeriodRisk tmpPeriodRisk = new PeriodRisk(swap.floatingCode,
                startDate, endDate, period, dailyQuantity);
        risk.periodRisks.add(tmpPeriodRisk);
        
        for (int i = 0; i < partitionDays.size() - 1; i ++){
            startDate = getNextDay(partitionDays.get(i));
            endDate = partitionDays.get(i + 1);
            period = getNextMonthString(endDate);
            tmpPeriodRisk = new PeriodRisk(swap.floatingCode,
                startDate, endDate, period, dailyQuantity);
            risk.periodRisks.add(tmpPeriodRisk);
        }
        
        // last period
        startDate = getNextDay(partitionDays.get(partitionDays.size() - 1));
        endDate = getThisMonLastDay(partitionDays.get(partitionDays.size() - 1));
        period = getNextMonthString(getNextMonFirstDay(endDate));
        tmpPeriodRisk = new PeriodRisk(swap.floatingCode,
            startDate, endDate, period, dailyQuantity);
        risk.periodRisks.add(tmpPeriodRisk);
        
        return risk;
    }
    
    private Date getNextMonFirstDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, 1);
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();
    }
    private Date getThisMonFirstDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, 1);
        return cal.getTime();
    }
    private Date getThisMonLastDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.add(Calendar.MONTH, 1);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return cal.getTime();
    }
    private Date getNextDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        return cal.getTime();
    }
    private String getNextMonthString(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("MMM-yy", Locale.ENGLISH);
        return sdf.format(getNextMonFirstDay(date));
    }
    private double getIntervalDays(Date start, Date end){
        long interval = end.getTime() - start.getTime();
        
        //return Math.round(endCal.compareTo(startCal) / 1000.0 / 3600.0 / 24.0 );
        return Math.abs(interval / 1000 / 3600 / 24) + 1;
    }
    // This method only use the month and yesr from parameter
    private Date getPartitionDay(Date yearMonth){
        Calendar cal = Calendar.getInstance();
        cal.setTime(yearMonth);
        
        int year = cal.get(Calendar.YEAR);
        int mon = cal.get(Calendar.MONTH);
        int partitionDay = AvgFloatingPrice.separate(mon, year);
        
        cal.set(Calendar.DAY_OF_MONTH, partitionDay);
        return cal.getTime();
    }
    
}
