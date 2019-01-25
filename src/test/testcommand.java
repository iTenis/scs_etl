package test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class testcommand {
	public static void testOption(String[] args) throws ParseException {
		Options options = new Options();
		options.addOption("t", false, "display current time");
		options.addOption("c", true, "country code");
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);
		if (cmd.hasOption("t")) {
			System.out.println((new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) + " in "
					+ cmd.getOptionValue("c"));// 这里调用cmd.getOptionValue("c")会输出hello     
		} else {
			System.out.println((new SimpleDateFormat("yyyy-MM-dd")).format(new Date()));
		}
	}
	
	public static void main(String[] args) throws ParseException {
		testOption(args);
	}
}
