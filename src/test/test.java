package test;

import java.io.UnsupportedEncodingException;

import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.StringEscapeUtils;

import com.huawei.config.CommandLineConfig;
import com.huawei.config.ConfigFactory;

public class test {

	public static void main(String[] args) throws ParseException, UnsupportedEncodingException {
		 byte[] bytes = new byte[] {5};
		 String sendString=new String( bytes ,"GBK"); 
		 StringBuilder s=new StringBuilder();
		 s.append("\u0001");
		 System.out.println(StringEscapeUtils.unescapeJava(args[0]));
		 
	}
}
