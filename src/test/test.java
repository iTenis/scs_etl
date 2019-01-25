package test;

import java.io.UnsupportedEncodingException;

import org.apache.commons.cli.ParseException;

import com.huawei.config.CommandLineConfig;
import com.huawei.config.ConfigFactory;

public class test {

	public static void main(String[] args) throws ParseException, UnsupportedEncodingException {
		 byte[] bytes = new byte[] {5};
		 String sendString=new String( bytes ,"GBK"); 
		 System.out.println(sendString);
	}
}
