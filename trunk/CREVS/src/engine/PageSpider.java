package engine;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.net.URL; 
import java.net.URLConnection;
import org.apache.commons.lang.StringUtils;

public class PageSpider {
	public static String reqForGet(String getURL){
		try {
    		URL url = new URL(getURL);
    		URLConnection urlConn = url.openConnection();
    		HttpURLConnection httpUrlConn = (HttpURLConnection) urlConn;
            httpUrlConn.setDoOutput(false);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
    		httpUrlConn.setRequestMethod("GET");
                BufferedReader in ;
                try{
    		 in = new BufferedReader(new InputStreamReader(httpUrlConn.getInputStream()));
                }
                catch (java.net.NoRouteToHostException | java.net.UnknownHostException ee) {
                        return "";
		}
    		String line;
    		StringBuffer sb = new StringBuffer();
    		while ((line = in.readLine()) != null) {
    			sb.append(line);
    		}
    		in.close();
    		return StringUtils.trimToEmpty(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
        
}
