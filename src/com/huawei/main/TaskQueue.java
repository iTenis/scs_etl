package com.huawei.main;
import java.util.Date;
 
public class TaskQueue {
	private static  DoubleBufferedQueue<String> queues = null;
	
	public static DoubleBufferedQueue<String> getTaskQueue(){
		if(queues==null){
			queues =new DoubleBufferedQueue<String>(100000);
			System.out.println("初始化 队列");
		}
		return queues;
	}
	
	public static void add(String obj){
		if(queues==null)
			queues =  getTaskQueue();
		queues.offer(obj);
	}
	
	public static void put(String obj){
		if(queues==null)
			queues =  getTaskQueue();
		try {
			queues.put(obj);
			App.cmd.setExport_num(App.cmd.getExport_num()+1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(App.cmd.getExport_num()%5000==0)
			System.out.println("export："+App.cmd.getExport_num()+"条:"+App.df.format(new Date()));
	}
}
