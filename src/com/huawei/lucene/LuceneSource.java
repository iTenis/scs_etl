package com.huawei.lucene;

import java.util.Date;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import com.google.gson.JsonObject;
import com.huawei.main.App;
import com.huawei.main.TaskQueue;

public class LuceneSource {

	public void searchPageBySearchAfter(String indexDir, String queryString, int pageSize, ScoreDoc lastdoc) {
		System.out.println("开始search"+App.df.format(new Date()));
		Document doc=null;
		JsonObject jsonobj =null;
		try {
			ScoreDoc newlastdoc = lastdoc;
			Query query = LuceneClientFactory.query;
			TopDocs topDocs = null;
			if (lastdoc != null) {
				
				topDocs = LuceneClientFactory.getIndexsearch().searchAfter(lastdoc, query, pageSize);
			} else {
				System.out.println("start search:"+App.df.format(new Date()));
				topDocs = LuceneClientFactory.getIndexsearch().search(query, pageSize);
				System.out.println("end search:"+App.df.format(new Date()));
			}
			if (topDocs.scoreDocs.length > pageSize - 1)
				newlastdoc = topDocs.scoreDocs[pageSize - 1];
			for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
				doc = LuceneClientFactory.getIndexsearch().doc(scoreDoc.doc);
				Iterator<IndexableField> iterator = doc.iterator();
				
				
				jsonobj = new JsonObject();
				while (iterator.hasNext()) {
					IndexableField entry = iterator.next();
					jsonobj.addProperty(entry.name(), entry.stringValue());
				}
				TaskQueue.put(jsonobj.toString());
			}
			if (newlastdoc != lastdoc) {
				searchPageBySearchAfter(indexDir, queryString, pageSize, newlastdoc);
				System.out.println("export end---------------");
				
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						System.out.println("remaining num:" + TaskQueue.getTaskQueue().size()+":"+App.df.format(new Date()));
						if(TaskQueue.getTaskQueue().size()==0) {
							System.exit(0);
						}
					}
				}, 5000);
//				System.exit(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
