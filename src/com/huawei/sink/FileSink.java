package com.huawei.sink;

import java.util.List;

import com.huawei.main.App;
import com.huawei.util.FileUtils;

public class FileSink {
	public  void dataimport(List<String> datalist) {
		System.out.println("putData starting");
		FileUtils.exportCsv(App.cmd.getOutput(), datalist);
	}
}
