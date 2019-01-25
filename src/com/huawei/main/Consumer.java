package com.huawei.main;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.huawei.es.EsSink;
import com.huawei.file.FileSink;

public class Consumer implements Runnable {
	private static Consumer consumer;
	
	public static volatile boolean isRunning=true;
	public void run() {
		
		while(Thread.currentThread().isInterrupted()==false && isRunning)  
        {  
			try {
				String line="";
				int count=0;
				List<String> list = new ArrayList<String>();
				while(count<Integer.valueOf(App.cmd.getSinkbathsize())) {
					count++;
					line=TaskQueue.getTaskQueue().poll()+"";
					if(!line.equals("")&&null!=line&&!"null".equals(line)) {
						list.add(line);
					}
				}
				if(!list.isEmpty()) {
					switch (App.cmd.getSinktype()) {
					case SINKES:
						new EsSink().dataimport(list, App.cmd.getEsindex(), App.cmd.getEsindextype());
					case SINKFILE:
						new FileSink().dataimport(list);
					default:
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
        }
		
	}
	public static Consumer getInstance(){
		if(consumer==null){
			consumer = new Consumer();
			System.out.println("初始化消费线程");
		}
		return consumer;
	}
 
}
