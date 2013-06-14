package engine;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;

public class ContractCD{

	/**
	 * 读取源文件内容
	 * @param filename String 文件路径
	 * @throws IOException
	 * @return byte[] 文件内容
	 */
	public static byte[] readFile(String filename) throws IOException {
	
	    File file =new File(filename);
	    if(filename==null || filename.equals(""))
	    {
	      throw new NullPointerException("无效的文件路径");
	    }
	    long len = file.length();
	    byte[] bytes = new byte[(int)len];
	
	    BufferedInputStream bufferedInputStream=new BufferedInputStream(new FileInputStream(file));
	    int r = bufferedInputStream.read( bytes );
	    if (r != len)
	      throw new IOException("读取文件不正确");
	    bufferedInputStream.close();
	
	    return bytes;
	
	}
	
	public static String contractCD(String m,String y) {
	    try {
	    	//get content of test.txt
			String content=new String(readFile(System.getProperty("user.dir")+"/src/ContractCD_info"));
			
			//get every single String of test.txt
			String[] subStr=content.split(",");
			
			//make output string
			String[] output=new String[subStr.length*3];
			for(int i=0;i<output.length;i++)
				output[i]="";//initialize
			int month=6;
			int year=2013;
			for(int i=0;i<subStr.length-4;i++)
			{
				if(month%12==0)year++;
				output[i*3]=subStr[i];
				output[i*3+1]+=(month%12+1);
				output[i*3+2]+=year;
				month++;
			}
			int last4=subStr.length-4;
			
			output[last4*3]="CLM0";
			output[last4*3+1]="1";
			output[last4*3+2]="2020";
			last4++;

			output[last4*3]="CLZ0";
			output[last4*3+1]="2";
			output[last4*3+2]="2020";
			last4++;

			output[last4*3]="CLM1";
			output[last4*3+1]="1";
			output[last4*3+2]="2021";
			last4++;

			output[last4*3]="CLZ1";
			output[last4*3+1]="2";
			output[last4*3+2]="2021";
			
			//get contractCD:
			for(int i=0;i<subStr.length;i++)
			{
				if(m.equals(output[i*3+1])&&y.equals(output[i*3+2]))
					return output[i*3];
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	//main for test
	public static void main(String[] args) {
	    System.out.print(contractCD("2","2020"));
	}

}
