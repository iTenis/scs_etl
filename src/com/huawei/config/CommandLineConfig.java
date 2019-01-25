/**
 * 
 */
package com.huawei.config;

import java.util.Arrays;
import java.util.List;

public class CommandLineConfig {

	public enum ActionType {
		ACTIONIMPORT("import"), ACTIONEXPORT("export");

		private String name;

		private ActionType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public static List<String> getNames() {
			return Arrays.asList("actionimport", "actionexport");
		}

	}

	public enum SinkType {
		SINKES("es"), SINKFILE("file");

		private String name;

		private SinkType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public static List<String> getNames() {
			return Arrays.asList("sinkes", "sinkfile");
		}

	}

	public enum SourceType {
		SOURCEES("es"), SOURCEFILE("file"), SOURCELUCENE("lucene");

		private String name;

		private SourceType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public static List<String> getNames() {
			return Arrays.asList("sourcees", "sourcefile", "sourcelucene");
		}

	}

	private ActionType actionType;
	private String output;
	private SinkType sinktype;
	private SourceType sourcetype;
	private String esindexname;
	private String esindextype;
	private String eshost;
	private String espost ;
	private int export_num ;
	private String lucene_dir ;
	private String sinkthread;
	private String sinkbathsize;
	private String separator;

	/**
	 * @return the ActionType
	 */
	public ActionType getActionType() {
		return actionType;
	}

	/**
	 * @param ActionType
	 *            the ActionType to set
	 */
	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}


	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getEsindexname() {
		return esindexname;
	}

	public void setEsindexname(String esindexname) {
		this.esindexname = esindexname;
	}

	public SinkType getSinktype() {
		return sinktype;
	}

	public void setSinktype(SinkType sinktype) {
		this.sinktype = sinktype;
	}

	public SourceType getSourcetype() {
		return sourcetype;
	}

	public void setSourcetype(SourceType sourcetype) {
		this.sourcetype = sourcetype;
	}

	public String getEsindex() {
		return esindexname;
	}

	public void setEsindex(String esindexname) {
		this.esindexname = esindexname;
	}

	public String getEsindextype() {
		return esindextype;
	}

	public void setEsindextype(String esindextype) {
		this.esindextype = esindextype;
	}

	public String getEshost() {
		return eshost;
	}

	public void setEshost(String eshost) {
		this.eshost = eshost;
	}

	public String getEspost() {
		return espost;
	}

	public void setEspost(String espost) {
		this.espost = espost;
	}

	public int getExport_num() {
		return export_num;
	}

	public void setExport_num(int export_num) {
		this.export_num = export_num;
	}

	public String getLucene_dir() {
		return lucene_dir;
	}

	public void setLucene_dir(String lucene_dir) {
		this.lucene_dir = lucene_dir;
	}

	public String getSinkthread() {
		return sinkthread;
	}

	public void setSinkthread(String sinkthread) {
		this.sinkthread = sinkthread;
	}

	public String getSinkbathsize() {
		return sinkbathsize;
	}

	public void setSinkbathsize(String sinkbathsize) {
		this.sinkbathsize = sinkbathsize;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		this.separator = separator;
	}


}
