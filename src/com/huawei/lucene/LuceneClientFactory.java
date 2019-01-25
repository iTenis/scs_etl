package com.huawei.lucene;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NIOFSDirectory;

import com.huawei.main.App;

public class LuceneClientFactory {
	public static IndexSearcher is;
	public static FSDirectory dir;
	public static MatchAllDocsQuery query;
	public static DirectoryReader reader;
	static {
		init();
	}

	public static IndexSearcher getIndexsearch() {
		return is;
	}

	public static void init() {
		// 得到读取索引文件的路
		try {
			System.out.println("开始构建indexsearch"+App.df.format(new Date()));
			dir = NIOFSDirectory.open(Paths.get(App.cmd.getLucene_dir()));
			// 通过dir得到的路径下的所有的文件
			reader = DirectoryReader.open(dir);
			// 建立索引查询
			is = new IndexSearcher(reader);
			query = new MatchAllDocsQuery();
			System.out.println("构建indexsearch完成"+App.df.format(new Date()));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
