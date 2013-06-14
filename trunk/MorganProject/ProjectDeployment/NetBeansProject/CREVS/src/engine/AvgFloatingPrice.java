package engine;

import java.util.Calendar;
import java.util.Vector;


public class AvgFloatingPrice {
	public static int[] getWorkdays(int m,int y){
		Calendar cal = Calendar.getInstance();  
        cal.set(Calendar.YEAR, y);  
        cal.set(Calendar.MONTH,  m - 1);  
        cal.set(Calendar.DATE, 1);  
        
        int[] tmp=new int[30];//can be larger than real
        //initialize result[]
        for(int i=0;i<30;i++)
        	tmp[i]=-1;
        
        int index=0;
        while(cal.get(Calendar.YEAR) == y &&   
                cal.get(Calendar.MONTH) < m){  
            int day = cal.get(Calendar.DAY_OF_WEEK);  
              
            if(!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)){ 
            	tmp[index]=cal.get(Calendar.DATE);
            	index++;
            }  
            cal.add(Calendar.DATE, 1);  
        }  
        int[] result=new int[index];
        for(int i=0;i<index;i++)
        	result[i]=tmp[i];
        return result;
	}
	
	//get date now, format；d/m/y
	public static int[] getDate(){
		Calendar cal = Calendar.getInstance();  
		int[]result=new int[3];
		result[0]=cal.get(Calendar.DATE);
		result[0]=cal.get(Calendar.MONTH);
		result[0]=cal.get(Calendar.YEAR);
		return result;
	}
	
	//separate a month,return the last day of former
	public static int separate(int m,int y){
		int[] workdays=getWorkdays(m,y);
		int result=0;//initialize to 0
		for(int i=0;i<workdays.length;i++){
			if(workdays[i]==25){
				result=workdays[i-4];
				break;
			}
			else if(workdays[i]>25){
				result=workdays[i-5];
				break;
			}
		}
		return result;
	}
	
	//calculate avgFolatingPrice
	/**
	 * @wrong return type
	 * @return  0: not changed
	 * @return -1: no Jan&Feb info
	 * @return -2: the date is after today
	 * 
	 * @param flag:
	 * 1:	Before pricing start date
	 * 2:	After pricing start date 
	 */
	public static double avgFolatingPrice(int d,int m,int y,int flag){
		double result=0;
		
		//get content of the URL
		/* first change the format of day/month
		 * eg:7/7/2014->07/07/2014
		 *    7/12/2020->07/12/2020
		 */
		String dd=d+"";
		String mm=m+"";
		if(d<10)dd="0"+d;
		if(m<10)mm="0"+m;
		String yy=y+"";
		String URL="http://www.cmegroup.com/CmeWS/da/DailySettlement/" +
				"V1/DSReport/ProductCode/CL/FOI/FUT/EXCHANGE/XNYM/" +
				"Underlying/CL/ProductId/425" +
				"?tradeDate="+mm+"/"+dd+"/"+yy;
		String content=PageSpider.reqForGet(URL);
		
		//get JanContractPrice&FebContractPrice
		if(content.indexOf("<sett>")==-1)
			return -1;//this date is not recorded in web
		content=content.substring(content.indexOf("JLY 13"));
		content=content.substring(content.indexOf("<sett>")+6);
		double JanContractPrice=
				Double.parseDouble(content.substring(0, 5));
		content=content.substring(content.indexOf("<sett>")+6);
		double FebContractPrice=
				Double.parseDouble(content.substring(0, 5));
		
		//get result!
		Vector <String> priceList=PriceListOfAMonth.price(m, y);
		int workday=getWorkdays(m,y).length;
		int separate=separate(m,y);
		double sumSettlementPrice=0;
		int parameter1=0;//to record enterprice number
		for(int i=0;i<priceList.size()/2;i++){
			if(Integer.parseInt(priceList.get(i*2))<d){
				parameter1++;
				sumSettlementPrice+=
						Double.parseDouble(priceList.get(i*2+1));
			}
		}
		sumSettlementPrice=Math.round(sumSettlementPrice*100)/100.0; 
		int former=0;//14 in eg. second doc
		for(int i=0;i<workday;i++)
			if(getWorkdays(m,y)[i]<=separate)
				former++;
		
		if(flag==1)
                        result=(JanContractPrice*former+FebContractPrice*(workday-former))/workday;
		if(flag==2){
                    if(parameter1<former)
                        result=(sumSettlementPrice+JanContractPrice*(former-parameter1)+FebContractPrice*(workday-former))/workday;
                    else
                        result=(sumSettlementPrice+FebContractPrice*(workday-parameter1))/workday;
		}
		result=Math.round(result*100)/100.0; 
		return result;
	}
	
	//main for test
	public static void main(String[] args) {
		//initialize month&year
		int m=6;
		int y=2013;
		
		//find all work days of a month
		/*
		for(int i=0;i<getWorkdays(m,y).length;i++){
			int tmp=getWorkdays(m,y)[i];
			System.out.print(tmp+" ");
		}
		 */
		
		//test separate
		//System.out.print("\n"+separate(m,y));
		
		System.out.println(avgFolatingPrice(5,m,y,2));
	}
}
