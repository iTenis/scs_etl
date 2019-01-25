package com.huawei.config;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.huawei.config.CommandLineConfig.ActionType;
import com.huawei.config.CommandLineConfig.SinkType;
import com.huawei.config.CommandLineConfig.SourceType;

public class ConfigFactory {
	private static CommandLineConfig c;
	private static Logger logger = LoggerFactory.getLogger(ConfigFactory.class);
	private static final String[] ACTION_TYPE = new String[] { "a", "actiontype" };
	private static final String[] SINKBATHSIZE = new String[] { "s", "sinkbathsize" };
	private static final String[] SINKTHREAD = new String[] { "t", "sinkthread" };
	private static final String[] SINK_TYPE = new String[] { "k", "sinktype" };
	private static final String[] SOURCE_TYPE = new String[] { "e", "sourcetype" };
	private static final String[] OUTPUT = new String[] { "o", "output" };
	private static final String[] HELP = new String[] { "h", "help" };
	public static final String[] ESINDEXNAME= new String[] { "n", "esindexname" };
	public static final String[] ESINDEXTYPE= new String[] { "t", "esindextype" };
	public static final String[] ESHOST= new String[] { "H", "eshost" };
	public static final String[] ESPORT= new String[] { "p", "esport" };
	public static final String[] LUCENE_DIR = new String[] { "d", "lucenedir" };
	
	public static CommandLineConfig getConfigFromArgs(String[] args) throws ParseException {
		CommandLine cmd = parseCommandLine(args);
		String outputdir = cmd.getOptionValue(OUTPUT[1]);
		String sinkbatchsize = cmd.getOptionValue(SINKBATHSIZE[1]);
		String sinkthread = cmd.getOptionValue(SINKTHREAD[1]);
		String esindexname = cmd.getOptionValue(ESINDEXNAME[1]);
		String esindextype = cmd.getOptionValue(ESINDEXTYPE[1]);
		String eshost = cmd.getOptionValue(ESHOST[1]);
		String esport = cmd.getOptionValue(ESPORT[1]);
		String lucene_dir = cmd.getOptionValue(LUCENE_DIR[1]);
		String actionType = cmd.getOptionValue(ACTION_TYPE[1]);
		String sinkType = cmd.getOptionValue(SINK_TYPE[1]);
		String sourceType = cmd.getOptionValue(SOURCE_TYPE[1]);

//		if (actionType == null) {
//			throw new MissingArgumentException("actionType should be [" + ActionType.getNames() + "]");
//		}
		if (sinkType == null) {
			throw new MissingArgumentException("sinktype should be [" + SinkType.getNames() + "]");
		}
		if (sourceType == null) {
			throw new MissingArgumentException("sourcetype should be [" + SourceType.getNames() + "]");
		}

		CommandLineConfig c = new CommandLineConfig();
		c.setOutput(outputdir);
		c.setEsindex(esindexname);
		c.setEsindextype(esindextype);
		c.setEshost(eshost);
		c.setEspost(esport);
		c.setSinkthread(sinkthread);
		c.setExport_num(0);
		c.setLucene_dir(lucene_dir);
		c.setSinkbathsize(sinkbatchsize);
		if("".equals(sinkbatchsize)||null==sinkbatchsize) {
			c.setSinkbathsize("2000");
		}
			
		
		
//		for (ActionType o : ActionType.values()) {
//			if (actionType.equalsIgnoreCase(o.toString())) {
//				c.setActionType(o);
//				break;
//			}
//		}
//		if (c.getActionType() == null) {
//			throw new MissingArgumentException("actionType should be [" + ActionType.getNames() + "]");
//		}

		for (SourceType o : SourceType.values()) {
			if (sourceType.equalsIgnoreCase(o.toString())) {
				c.setSourcetype(o);
				break;
			}
		}
		if (c.getSourcetype() == null) {
			throw new MissingArgumentException("sourcetype should be [" + SourceType.getNames() + "]");
		}

		for (SinkType o : SinkType.values()) {
			if (sinkType.equalsIgnoreCase(o.toString())) {
				c.setSinktype(o);;
				break;
			}
		}
		if (c.getSinktype() == null) {
			throw new MissingArgumentException("sinktype should be [" + SinkType.getNames() + "]");
			
		}
		logger.info("Current configuration " + c);

		return c;
	}

	/**
	 * Parse command line
	 * 
	 * @param args
	 * @return
	 * @throws ParseException
	 */
	private static CommandLine parseCommandLine(String[] args) throws ParseException {
		Options cliOptions = new Options();
		cliOptions.addOption(ACTION_TYPE[0], ACTION_TYPE[1], true, "action type [" + ActionType.getNames() + "]");
		cliOptions.addOption(OUTPUT[0], OUTPUT[1], true, "数据输出路径");
		cliOptions.addOption(SINKTHREAD[0], SINKTHREAD[1], true, "sink线程数");
		cliOptions.addOption(SOURCE_TYPE[0], SOURCE_TYPE[1], true, "source type [" + SourceType.getNames() + "]");
		cliOptions.addOption(SINK_TYPE[0], SINK_TYPE[1], true, "sink type [" + SinkType.getNames() + "]");
		cliOptions.addOption(ESINDEXNAME[0],ESINDEXNAME[1], true, "esindexname");
		cliOptions.addOption(ESINDEXTYPE[0],ESINDEXTYPE[1], true, "esindextype");
		cliOptions.addOption(ESHOST[0],ESHOST[1], true, "eshost");
		cliOptions.addOption(ESPORT[0],ESPORT[1], true, "esport");
		cliOptions.addOption(LUCENE_DIR[0],LUCENE_DIR[1], true, "lucene_dir");
		cliOptions.addOption(HELP[0], HELP[1], false, "help");
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(cliOptions, args);

		if (cmd.hasOption("help") || cmd.hasOption("h")) {
			String header = "import-export\n\n";
			String footer = "\nThinkYou";

			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("run", header, cliOptions, footer, true);
			System.exit(0);
		}

		return cmd;
	}

}
