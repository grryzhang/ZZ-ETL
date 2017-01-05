package com.zhongzhou.Excavator.DAO.oracle.NC.report;

import java.sql.SQLException;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.zhongzhou.Excavator.model.NC.report.SaleOrderItemState;
import com.zhongzhou.Excavator.model.NC.report.SaleOrderState;
import com.zhongzhou.Excavator.springsupport.injectlist.DAOBeanNameList;

public class SaleOrderDAOTest {

	private static XmlWebApplicationContext  context;
	private static String[] configs = { "classpath:applicationContext.xml" }; 
	
	private static SaleOrderDAO saleOrderDAO;
	
	@BeforeClass  
	public static void configTest(){

		try {
			context = new XmlWebApplicationContext ();
			context.setConfigLocations(configs);
			
			context.refresh();
			
			saleOrderDAO = (SaleOrderDAO)context.getBean(DAOBeanNameList.oracle_nc_report_saleOrderState);	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testAll() throws SQLException{
		
		this.testSelectSaleOrderStates();
	}
	
	/** Test if we can get any data of saleOrderState*/
	public void testSelectSaleOrderStates() throws SQLException{
		
		List<SaleOrderState> SOStates = this.saleOrderDAO.selectSaleOrderStates();
		
		System.out.println( SOStates.size() );
		
		Assert.assertNotEquals( null, SOStates );
		Assert.assertEquals( true, SOStates.size() > 0 );
		
		SaleOrderState firstRecord = SOStates.get(0);
		
		Assert.assertNotEquals( null, firstRecord.customerOrderId );
		Assert.assertEquals( true, firstRecord.customerOrderId.length() > 0 );
		
		Assert.assertNotEquals( null, firstRecord.items );
		Assert.assertEquals( true, firstRecord.items.size() > 0 );
		
		SaleOrderItemState firstRecordItem = firstRecord.items.get(0);
		
		Assert.assertNotEquals( null, firstRecordItem );
		Assert.assertNotEquals( null, firstRecordItem.code );
		Assert.assertEquals( true, firstRecordItem.code.length() > 0 );
	}
}
