package engine;

import java.util.Vector;


//this price means the real price of WTI
public class PriceListOfAMonth {
	//to know if the year is a leap year
	public static boolean isLeapYear(int y) {
		if(y%400==0)
			return true;
		else if(y%100==0)
			return false;
		else if(y%4==0)
			return true;
		return false;
	}
	//how many days are in the month
	public static int days(int m,int y){
		switch(m)
		{
		case 1:return 31;
		case 3:return 31;
		case 4:return 30;
		case 5:return 31;
		case 6:return 30;
		case 7:return 31;
		case 8:return 31;
		case 9:return 30;
		case 10:return 31;
		case 11:return 30;
		case 12:return 31;
		default://when month is 2
			if(isLeapYear(y))
				return 29;
			else
				return 28;
		}
	}

	//get price
	public static Vector<String> price(int m,int y){
		//get 2 URLs 
		//reason: if only 1 is used, the information
		//        won't be completed;
		//first URL2 because the date is shown in inverted order
		String URL2="http://oil.in-en.com/quote/futures-oil-info.asp?" +
				"pageid=1&cp_id=1&price=" +
				"&BeginDate=" +y+"-"+m+"-"+"1"+
				"&LastDate=" +y+"-"+m+"-"+"15";

		String URL1="http://oil.in-en.com/quote/futures-oil-info.asp?" +
				"pageid=1&cp_id=1&price=" +
				"&BeginDate=" +y+"-"+m+"-"+"16"+
				"&LastDate=" +y+"-"+m+"-"+days(m,y);
		
		//get content of the 2 pages
		String content=PageSpider.reqForGet(URL1)+
				PageSpider.reqForGet(URL2);

		//get price in String[] price
		Vector<String> price =new Vector<String>();
		while(true){
			int index=content.indexOf("<TR bgcolor=");
			if(index==-1)
				break;
			//when get substring, break "<TR ..."
			content=content.substring(index+1);
			//give value to String[] price
			price.add(content.substring(33,35));
			price.add(content.substring(60,65));
		}

		return price;
	}
	
	//main for test
	public static void main(String[] args) {
		//test days of a month&year
		//System.out.print(days(5,2013));
		Vector<String>price=price(5,2013);
		for(int i=0;i<price.size()/2;i++)
			System.out.print(price.get(i*2)+"\t"+price.get(i*2+1)+"\n");
	}
}
