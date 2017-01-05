package com.zhongzhou.Excavator.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.zhongzhou.Excavator.model.NC.report.SaleOrderItemState;
import com.zhongzhou.Excavator.model.NC.report.SaleOrderState;
import com.zhongzhou.Excavator.springsupport.injectlist.ETLProcessList;

public class BeanComparerTest {
	
	
	private static SaleOrderState one;
	private static SaleOrderState two;
	
	@BeforeClass  
	public static void configTest(){

		try {
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void testCompareBean() throws IllegalArgumentException, IllegalAccessException{
		
		one = new SaleOrderState();
		one.orderId = "1";
		one.customerOrderId = "1";
		
		SaleOrderItemState itemOne= new SaleOrderItemState();
		itemOne.name = "itemOne";
		itemOne.forecastDeliveryTime = "2016/12/3";
		
		SaleOrderItemState itemTwo= new SaleOrderItemState();
		itemTwo.name = "itemTwo";
		
		one.items = new ArrayList( Arrays.asList(itemOne, itemTwo) );
		
		
		
		two = new SaleOrderState();
		two.orderId = "1";
		two.customerOrderId = "2";
				
	    SaleOrderItemState itemThree= new SaleOrderItemState();
	    itemThree.name = "itemOne";
	    itemThree.forecastDeliveryTime = "2016/12/1";
		
		SaleOrderItemState itemFour= new SaleOrderItemState();
		itemFour.name = "itemThree";
		
		two.items = new ArrayList( Arrays.asList(itemThree, itemFour) );
		
		BeanComparer comparer = new BeanComparer();
		
		List<String> allChanged = comparer.beanDataChanged( one, two );
		for( String changed : allChanged  ){
			System.out.println( changed );
		}
	}
}
