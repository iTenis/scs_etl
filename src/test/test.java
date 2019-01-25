package test;

import org.apache.commons.cli.ParseException;

import com.huawei.config.CommandLineConfig;
import com.huawei.config.ConfigFactory;

public class test {

	public static void main(String[] args) throws ParseException {
		CommandLineConfig  cmd=ConfigFactory.getConfigFromArgs(args);
		cmd.getEshost();
	}
}
