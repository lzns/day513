package com.dzf.utils;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * bean工厂
 * @author adminstrtor
 *
 */
public class BeanFactory {
	/**
	 * 获取对应的实体类
	 * @param id
	 * @return
	 */
	public static Object getBean(String id){
		SAXReader s = new SAXReader();
		try {
			Document doc = s.read(BeanFactory.class.getClassLoader().getResourceAsStream("beans.xml"));
			Element ele = (Element) doc.selectSingleNode("//bean[@id='"+id+"']");
			String path = ele.attributeValue("class");
			return Class.forName(path).newInstance();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
