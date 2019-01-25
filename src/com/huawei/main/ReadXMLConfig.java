package com.huawei.main;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * @读取配置文件获得配置
 * 
 */
public class ReadXMLConfig {

    // 单例模式ReadXMLConfig类中维护本来对象
    private static ReadXMLConfig readXMLConfig;

    // 存储xml中的键值对
    private HashMap<String, String> confMap;

    // 定义为private防止类外调用构造函数生成对象
    private ReadXMLConfig() {
        try {
            initReadConfig("config.xml");
           
            System.out.println("---------" + "com" + File.separator + "config"
                    + File.separator + "config.xml");
        } catch (Exception e) {
            System.out.println("init config.xml failure!");
        }
    }

    // 单例模式通过getInstance始终返回同一个readXMLConfig对象
    public static ReadXMLConfig getInstance() {
        if (null == readXMLConfig) {
            readXMLConfig = new ReadXMLConfig();
        }
        return readXMLConfig;
    }

    // 读取配置文件并读入至hashmap中
    private void initReadConfig(String xmlFileName) throws Exception {
        InputStream input = ReadXMLConfig.class.getClassLoader()
                .getResourceAsStream(xmlFileName);
        if (input == null) {
            input = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(xmlFileName);
        }
        if (input == null) {
            throw new Exception(xmlFileName + "not found!");
        }
        try {
            // 使用DOM方式解析xml文件，并将结果存至hashmap中
            DOMParser parser = new DOMParser();
            parser.parse(new InputSource(input));
            Document m_config = parser.getDocument();
            Element root = m_config.getDocumentElement();

            NodeList nodeList = root.getElementsByTagName("config");

            int confNum = nodeList.getLength();
            confMap = new HashMap<String, String>();
            for (int i = 0; i < confNum; i++) {
                Element ele = (Element) nodeList.item(i);
                if (!ele.getAttribute("key").equals("")) {
                    confMap.put(ele.getAttribute("key"),
                            ele.getAttribute("value"));
                }
            }
        } catch (Exception e) {
            System.out.println("init config.xml failure!");
        } finally {
            input.close();
        }
    }

    // 通过类中的该方法获取hashmap中的值
    public String getConfigValue(String key, String defaultValue) {
        if (confMap == null) {
            return defaultValue;
        } else {
            String result = confMap.get(key);
            if (result != null && !result.equals("")) {
                return result;
            }
        }
        return defaultValue;
    }
    
    public HashMap<?, ?> scanConfigValue() {
        if (confMap == null) {
        	return null;
        } else {
        	 return confMap;
        }
    }
}
