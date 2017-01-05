package com.zhongzhou.Excavator.ETL.transformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.zhongzhou.Excavator.DAO.mongo.NC.report.SaleOrderInnerDAO;
import com.zhongzhou.Excavator.ETL.Exception.ETLRuntimeException;
import com.zhongzhou.Excavator.Util.BeanComparer;
import com.zhongzhou.Excavator.model.NC.report.SaleOrderItemState;
import com.zhongzhou.Excavator.model.NC.report.SaleOrderState;
import com.zhongzhou.Excavator.springsupport.injectlist.DAOBeanNameList;
import com.zhongzhou.Excavator.springsupport.injectlist.ETLProcessList;

public class BeanComparerTest {
	
	private static XmlWebApplicationContext  context;
	private static String[] configs = { "classpath:applicationContext.xml" }; 
	
	private static SaleOrderInnerDAO saleOrderInnerDAO ;
	
	@BeforeClass  
	public static void configTest(){

		try {
			context = new XmlWebApplicationContext ();
			context.setConfigLocations(configs);
			
			context.refresh();
			
			saleOrderInnerDAO = (SaleOrderInnerDAO)context.getBean(DAOBeanNameList.oracle_nc_report_saleOrderState_inner);	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testCompareBean() throws IllegalArgumentException, IllegalAccessException{
		
		com.zhongzhou.Excavator.model.NC.report.SaleOrderState lastVersion 
			= saleOrderInnerDAO.getLastVersionSaleOrderState( "test",  "test" );
		
		com.zhongzhou.Excavator.model.NC.report.SaleOrderState theLastVersion 
		= saleOrderInnerDAO.getLastVersionSaleOrderState( "customerOrderId",  "Warranty" );
		
		BeanComparer beanComparer = new BeanComparer();
		List<String> changed = beanComparer.beanDataChanged( theLastVersion , lastVersion );

		for( String change : changed ){
			System.out.println( change );
		}
		
	}
}
