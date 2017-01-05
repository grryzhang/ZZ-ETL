package com.zhongzhou.Excavator.DAO.mongo.NC.report;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.zhongzhou.Excavator.model.NC.report.NCReportMongoData;
import com.zhongzhou.Excavator.model.NC.report.SaleOrderItemState;
import com.zhongzhou.Excavator.model.NC.report.SaleOrderState;

import com.zhongzhou.Excavator.springsupport.injectlist.DAOBeanNameList;
import com.zhongzhou.Excavator.springsupport.injectlist.DataSourceList;

public class SaleOrderDAOTest {
	
	private static XmlWebApplicationContext  context;
	private static String[] configs = { "classpath:applicationContext.xml" }; 
	
	private static SaleOrderDAO saleOrderDAO;

	private static com.zhongzhou.Excavator.DAO.oracle.NC.report.SaleOrderDAO NCReportSaleOrderDAO;
	
	@BeforeClass  
	public static void configTest(){

		try {
			context = new XmlWebApplicationContext ();
			context.setConfigLocations(configs);
			
			context.refresh();
			
			saleOrderDAO = (SaleOrderDAO)context.getBean(  DAOBeanNameList.mongo_nc_report_saleOrder );
			
			NCReportSaleOrderDAO = (com.zhongzhou.Excavator.DAO.oracle.NC.report.SaleOrderDAO)context.getBean(DAOBeanNameList.oracle_nc_report_saleOrderState);	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testAll() throws SQLException{
		
		//this.testInsertReport();
		this.testInsertReports(); 
	}
	
	//@Test
	public void testInsertReport(){
		
		SaleOrderState state = new SaleOrderState();
		
		SaleOrderItemState itemSate = new SaleOrderItemState();
		
		state.customerOrderId = "test001";
		itemSate.code = "P1100011";
		state.items = new ArrayList<SaleOrderItemState>( Arrays.asList(itemSate)  );
		
		
		NCReportMongoData<SaleOrderState> data = new NCReportMongoData<SaleOrderState>();
		long currentTime = System.currentTimeMillis();
		
		data.data = state;
		data.modelClass = SaleOrderState.class;
		data.insertTime = currentTime;
		data.version 	= currentTime;
		data.modelClassName = SaleOrderState.class.getName() ;
				
		
		saleOrderDAO.insertReport( data );
	}
	
	//@Test
	// this function depend on com.zhongzhou.Excavator.DAO.oracle.NC.report.SaleOrderDAO
	public void testInsertReports() throws SQLException{
		
		List<SaleOrderState> SOStates = NCReportSaleOrderDAO.selectSaleOrderStates();
		
		List< NCReportMongoData<SaleOrderState> > docs = new ArrayList< NCReportMongoData<SaleOrderState> >();
		long currentTime = System.currentTimeMillis();
		for( SaleOrderState SOState : SOStates ){
			NCReportMongoData<SaleOrderState> data = new NCReportMongoData<SaleOrderState>();
			
			data.data = SOState;
			data.modelClass = SaleOrderState.class;
			data.insertTime = currentTime;
			data.version 	= currentTime;
			data.modelClassName = SaleOrderState.class.getName() ;
			
			docs.add( data );
		}
		
		saleOrderDAO.insertReports( docs );
	}
}
