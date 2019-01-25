package com.huawei.file;

import java.util.List;

import com.huawei.main.App;

public class FileSink {
	public  void dataimport(List<String> datalist) {
		System.out.println("putData starting");
		CSVUtils.exportCsv(App.cmd.getOutput(), datalist);
	}
}
