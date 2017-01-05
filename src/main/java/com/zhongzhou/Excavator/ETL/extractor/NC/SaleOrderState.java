package com.zhongzhou.Excavator.ETL.extractor.NC;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.zhongzhou.Excavator.ETL.Exception.ETLRuntimeException;
import com.zhongzhou.Excavator.ETL.extractor.Extracter;
import com.zhongzhou.Excavator.ETL.integration.BasicNode;
import com.zhongzhou.Excavator.ETL.integration.SimpleBasicNode;
import com.zhongzhou.Excavator.ETL.transporter.IntermediateData;
import com.zhongzhou.Excavator.ETL.transporter.impl.SimpleIntermediateData;
import com.zhongzhou.Excavator.model.FUTONG.report.FreightInfoSearchParameter;
import com.zhongzhou.Excavator.springsupport.injectlist.DAOBeanNameList;
import com.zhongzhou.Excavator.springsupport.injectlist.ETLExtractorList;
import com.zhongzhou.Excavator.springsupport.injectlist.ETLProcessList;

@Component(ETLExtractorList.nc_saleOrderState_report_extractor)
@Scope("prototype") 
public class SaleOrderState extends SimpleBasicNode implements Extracter, BasicNode {
	
	@Resource( name=DAOBeanNameList.oracle_nc_report_saleOrderState )
	com.zhongzhou.Excavator.DAO.oracle.NC.report.SaleOrderDAO ncSaleOrderDAO;

	@Override
	public void doExtract() throws ETLRuntimeException {
		
		try{
			List<com.zhongzhou.Excavator.model.NC.report.SaleOrderState> saleOrderStates = ncSaleOrderDAO.selectSaleOrderStates();
			
			IntermediateData mediData = new SimpleIntermediateData();
			mediData.setProcessId ( this.getProcessId() );
			mediData.setDataName  ( "saleOrderState" );
			mediData.setData      ( saleOrderStates );
			this.outputData.add( mediData );
			
			
			List<String> customerOrderIds = new ArrayList<String>();
			for( com.zhongzhou.Excavator.model.NC.report.SaleOrderState state : saleOrderStates ){
				customerOrderIds.add( state.orderId );
			}
			FreightInfoSearchParameter sp = new FreightInfoSearchParameter();
			sp.contractNumbers = customerOrderIds;
			
			mediData = new SimpleIntermediateData();
			mediData.setProcessId ( this.getProcessId() );
			mediData.setDataName  ( "freightInfoSearchParameter" );
			mediData.setData      ( sp );
			this.outputData.add( mediData );
			
		}catch( SQLException e ){
			throw new ETLRuntimeException( e );
		}
	}
	
	@Override
	public boolean prepare() {
		return true;
	}

	@Override
	public boolean run() {
		
		this.doExtract();
		
		return true;
	}

}
