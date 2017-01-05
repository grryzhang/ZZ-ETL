package com.zhongzhou.Excavator.DAO.MSSQL.FUTONG;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.zhongzhou.Excavator.model.FUTONG.report.FreightInfo;
import com.zhongzhou.Excavator.model.FUTONG.report.FreightInfoSearchParameter;
import com.zhongzhou.Excavator.springsupport.injectlist.DAOBeanNameList;

public class FreightDAOTest {

	private static XmlWebApplicationContext  context;
	private static String[] configs = { "classpath:applicationContext.xml" }; 
	
	private static FreightDAO freightDAO;
	
	@BeforeClass  
	public static void configTest(){

		try {
			context = new XmlWebApplicationContext ();
			context.setConfigLocations(configs);
			
			context.refresh();
			
			freightDAO = (FreightDAO)context.getBean(DAOBeanNameList.MSSQL_FUTONG_freight);	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testAll() throws SQLException{

		this.testSelectFreightInfo();
	} 
	
	public void testSelectFreightInfo() throws SQLException{
		
		FreightInfoSearchParameter searchParameters = new FreightInfoSearchParameter();
		searchParameters.contractNumbers = new ArrayList<String>( Arrays.asList("16PR277A") );
		
		List<FreightInfo> freightInfos = freightDAO.selectFreightInfo(searchParameters);
		
		System.out.println( freightInfos.size() );
		
		Assert.assertNotEquals( null, freightInfos );
		Assert.assertEquals( true , freightInfos.size() > 0 );
		Assert.assertNotEquals( null, freightInfos.get(0).customerContractNumber );
	}
}
