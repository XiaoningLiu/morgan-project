package engine;


public class SettlePrice {
	public static String settlePrice(String m,String y) {
		//get URL
		String URL="http://www.cmegroup.com/CmeWS/md/MDServer/V1" +
				"/Venue/G/Exchange/XNYM/FOI/FUT/Product/CL?" +
				"contractCDs=,";
		URL+=ContractCD.contractCD(m, y);
		
		//get content of the web page
		String content=PageSpider.reqForGet(URL);
		
		//get result
		String result="";
		while(true){
			int i=content.indexOf("\"settlePrice\":");
			content=content.substring(i+15);
			if(!content.substring(0, 1).equals("-")){
				result=content.substring(0, content.indexOf("\""));
				break;
			}
			if(i==-1)
				break;
		}
		return result;
	}
	//main for test
	public static void main(String[] args) {
		//get settle price
		//take 2020.1 for example
		System.out.print(settlePrice("1", "2020")+"\n");
	}
}
