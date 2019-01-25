package com.huawei.es;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import com.alibaba.fastjson.JSONObject;
import com.huawei.main.App;

public class EsSink {

	public  void dataimport(List<String> list,String index,String type) {
		System.out.println("putData starting");
		Map<String, String> params = Collections.singletonMap("pretty", "true");
		StringBuilder builder=new StringBuilder();
		for(String str:list) {
			builder.append(str);
		}
		HttpEntity entity = new NStringEntity(JSONObject.parse(builder.toString()).toString(), ContentType.APPLICATION_JSON);
		Response response = null;
		try {
			response = EsClientFactory.getClient().performRequest("POST", "/" + index + "/" + type , params, entity);
			System.out.println(response);
			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()
					|| HttpStatus.SC_CREATED == response.getStatusLine().getStatusCode()) {
			} else {
			}
		} catch (Exception e) {
			System.out.println("putData failed." + e);
		}

	}
}
