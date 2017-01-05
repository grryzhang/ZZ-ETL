package com.zhongzhou.Excavator.ETL.extractor.FUTONG;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.zhongzhou.Excavator.ETL.Exception.ETLRuntimeException;
import com.zhongzhou.Excavator.ETL.extractor.Extracter;
import com.zhongzhou.Excavator.ETL.integration.BasicNode;
import com.zhongzhou.Excavator.ETL.integration.SimpleBasicNode;
import com.zhongzhou.Excavator.ETL.transporter.IntermediateData;
import com.zhongzhou.Excavator.ETL.transporter.impl.SimpleIntermediateData;
import com.zhongzhou.Excavator.model.FUTONG.report.FreightInfoSearchParameter;
import com.zhongzhou.Excavator.model.webData.delmar.ShipmentTracingSearchParams;
import com.zhongzhou.Excavator.springsupport.injectlist.DAOBeanNameList;
import com.zhongzhou.Excavator.springsupport.injectlist.ETLExtractorList;

@Component(ETLExtractorList.futong_freight_info_report_extractor)
@Scope("prototype")  
public class FreightInfo extends SimpleBasicNode implements Extracter, BasicNode {
	
	@Resource( name=DAOBeanNameList.MSSQL_FUTONG_freight )
	com.zhongzhou.Excavator.DAO.MSSQL.FUTONG.FreightDAO ncSaleOrderDAO;
	
	private static final String INPUT_NECESSARY_1 = "freightInfoSearchParameter";
	
	public static final boolean[] necessaryInputReady = {false};
	
	private FreightInfoSearchParameter searchParameters;

	@Override
	public boolean prepare() {
		
		/* start - restore data */
		try{
			if( this.inputData.containsKey(INPUT_NECESSARY_1) ){
				necessaryInputReady[0] = true;
				searchParameters = (FreightInfoSearchParameter)this.inputData.get(INPUT_NECESSARY_1).getData();
			}
		}catch( java.lang.ClassCastException exception ) {
			throw new ETLRuntimeException( 
				"Wrong \"freightInfoSearchParameter\" input for "
				+ this.getClass().getName()
				+ "node,failed in prepare" , exception );
		}		
		/* end - restore data */
		
		for( boolean inputReady : necessaryInputReady ){
			 
			if( !inputReady ){
				return false;
			}
		}
		
		return true;
	}

	@Override
	public boolean run() {
		
		this.doExtract();
		
		return true;
	}

	@Override
	public void doExtract() throws ETLRuntimeException {
		
		try {
			List<com.zhongzhou.Excavator.model.FUTONG.report.FreightInfo> freightInfos = ncSaleOrderDAO.selectFreightInfo(this.searchParameters);
			
			IntermediateData mediData = new SimpleIntermediateData();
			mediData.setProcessId ( this.getProcessId() );
			mediData.setDataName  ( "futongFreightInfos" );
			mediData.setData      ( freightInfos );
			this.outputData.add( mediData );
			
		} catch (SQLException e) {
			throw new ETLRuntimeException( "Failed in extracting of extractor : " + this.getClass().toString() , e);
		}
	}
}
