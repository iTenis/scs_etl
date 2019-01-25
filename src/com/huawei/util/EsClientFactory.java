package com.huawei.util;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig.Builder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.elasticsearch.client.RestClientBuilder.RequestConfigCallback;

import com.huawei.main.App;
public class EsClientFactory {

 
 private static final int PORT = Integer.valueOf(App.cmd.getEshost());
 private static final String SCHEMA = "http";
 private static final int CONNECT_TIME_OUT = 100;
 private static final int SOCKET_TIME_OUT = 300;
 private static final int CONNECTION_REQUEST_TIME_OUT = 500;


 private static HttpHost HTTP_HOST = new HttpHost(App.cmd.getEshost(),PORT,SCHEMA);
 private static boolean uniqueConnectTimeConfig = false;
 private static RestClientBuilder builder;
 private static RestClient restClient;

 static {
     init();
 }

 public static void init(){
     builder = RestClient.builder(HTTP_HOST);
     if(uniqueConnectTimeConfig){
         setConnectTimeOutConfig();
     }
     restClient = builder.build();
 }

 public static void setConnectTimeOutConfig(){
     builder.setRequestConfigCallback(new RequestConfigCallback() {

         @Override
         public Builder customizeRequestConfig(Builder requestConfigBuilder) {
             requestConfigBuilder.setConnectTimeout(CONNECT_TIME_OUT);
             requestConfigBuilder.setSocketTimeout(SOCKET_TIME_OUT);
             requestConfigBuilder.setConnectionRequestTimeout(CONNECTION_REQUEST_TIME_OUT);
             return requestConfigBuilder;
         }

     });
 }

 public static RestClient getClient(){
     return restClient;
 }


 public static void close() {
     if (restClient != null) {
         try {
             restClient.close();
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
 }


}