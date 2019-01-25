package com.huawei.main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
 
public class ThreadPool {
	private static ExecutorService threadPool = null;
	public static ExecutorService getThreadPool(){
		if(threadPool==null){
			threadPool = Executors.newFixedThreadPool(10);
		}
		return 	threadPool;
	}
 
}

