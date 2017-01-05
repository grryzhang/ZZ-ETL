package com.zhongzhou.Excavator.ETL.integration.normal;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.zhongzhou.Excavator.ETL.transporter.IntermediateData;
import com.zhongzhou.Excavator.ETL.transporter.impl.SimpleIntermediateData;
import com.zhongzhou.Excavator.model.NC.report.SaleOrderItemState;
import com.zhongzhou.Excavator.springsupport.injectlist.DAOBeanNameList;
import com.zhongzhou.Excavator.springsupport.injectlist.ETLProcessList;

public class SaleOrderStateTest {

	private static XmlWebApplicationContext  context;
	private static String[] configs = { "classpath:applicationContext.xml" }; 
	
	private static  com.zhongzhou.Excavator.ETL.integration.normal.SaleOrderState saleOrderStateETLProcess;
	
	@BeforeClass  
	public static void configTest(){

		try {
			context = new XmlWebApplicationContext ();
			context.setConfigLocations(configs);
			
			context.refresh();
			
			saleOrderStateETLProcess = (SaleOrderState)context.getBean(ETLProcessList.saleOrderState_2_publish);	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testETL() throws SQLException{
		
		IntermediateData mediData = new SimpleIntermediateData();
		mediData.setData( UUID.randomUUID().toString() );
		mediData.setDataName( SaleOrderState.NODE_INPUT_NECESSARY_1 );
		mediData.setProcessId( UUID.randomUUID().toString() );
		
		saleOrderStateETLProcess.start( Arrays.asList( mediData ) );
	}
}
