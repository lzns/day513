package com.dzf.conventer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.Converter;
/**
 * 自定义转换器
 * @author adminstrtor
 *
 */
public class MyConventer implements Converter {

	/**
	 * class 要转成的类型
	 * object 要转换的值
	 */
	@Override
	public Object convert(Class arg0, Object arg1) {
		SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = format.parse((String)arg1);
			return date;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
