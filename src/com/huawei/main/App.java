package com.huawei.main;

import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;

import org.apache.commons.cli.ParseException;
import org.elasticsearch.client.RestClient;

import com.huawei.config.CommandLineConfig;
import com.huawei.config.ConfigFactory;
import com.huawei.source.LuceneSource;

public class App {

	public static RestClient client = null;
	public static CommandLineConfig cmd = null;
	private static ExecutorService threadPool;
	public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S");//设置日期格式
	public static void main(String[] args) {
		try {
			cmd = ConfigFactory.getConfigFromArgs(args);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		
		switch (cmd.getSourcetype()) {
		case SOURCEFILE:;
		case SOURCELUCENE:
			try {
				new Thread(new Runnable() {
					@Override
					public void run() {
						new LuceneSource().searchPageBySearchAfter(App.cmd.getLucene_dir(), "*:*", 10000, null);
					}
				}).start();
			} catch (Exception e) {
				e.printStackTrace();
			};
		default:
			break;
		}
		
		switch (cmd.getSinktype()) {
		case SINKES:;
		case SINKFILE:
			try {
				threadPool = ThreadPool.getThreadPool();
				for (int i = 0; i < Integer.valueOf(App.cmd.getSinkthread()); i++) {
					threadPool.execute(Consumer.getInstance());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			;
		default:
			break;
		}

		

	}
}
