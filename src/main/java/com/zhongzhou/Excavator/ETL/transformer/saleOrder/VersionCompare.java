package com.zhongzhou.Excavator.ETL.transformer.saleOrder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.zhongzhou.Excavator.DAO.mongo.NC.report.SaleOrderDAO;
import com.zhongzhou.Excavator.DAO.mongo.NC.report.SaleOrderInnerDAO;
import com.zhongzhou.Excavator.ETL.Exception.ETLRuntimeException;
import com.zhongzhou.Excavator.ETL.integration.SimpleBasicNode;
import com.zhongzhou.Excavator.ETL.transformer.Transformer;
import com.zhongzhou.Excavator.ETL.transporter.IntermediateData;
import com.zhongzhou.Excavator.ETL.transporter.impl.SimpleIntermediateData;
import com.zhongzhou.Excavator.Util.BeanComparer;
import com.zhongzhou.Excavator.springsupport.injectlist.DAOBeanNameList;
import com.zhongzhou.Excavator.springsupport.injectlist.ETLTransformerList;

@Component( ETLTransformerList.saleOrderState_versionCompare  )
@Scope("prototype") 
public class VersionCompare extends SimpleBasicNode  implements Transformer {
	
	@Resource( name = DAOBeanNameList.oracle_nc_report_saleOrderState_inner )
	private SaleOrderInnerDAO saleOrderInnerDAO;
	
	private List<com.zhongzhou.Excavator.model.NC.report.SaleOrderState> orderStates;

	private static final String INPUT_NECESSARY_1 = "saleOrderStates";
	
	public static final String[] necessaryInput = {INPUT_NECESSARY_1};
	
	@Override
	public boolean prepare() {
		
		try{
			if( this.inputData.containsKey(INPUT_NECESSARY_1) ){
				orderStates = (List<com.zhongzhou.Excavator.model.NC.report.SaleOrderState>)this.inputData.get(INPUT_NECESSARY_1).getData();
			}
		}catch( java.lang.ClassCastException exception ) {
			throw new ETLRuntimeException( 
					"Wrong input for " +this.getClass().toString()+ " node, failed in prepare" , 
					exception );
		}
		
		if( this.orderStates == null ){
			return false;
		}
		
		return true;
	}

	@Override
	public boolean run() {
		
		this.doTransform();
		
		return true;
	}

	@Override
	public void doTransform() {
		
		List<com.zhongzhou.Excavator.model.NC.report.SaleOrderState> pendingProcessSaleOrderState = new ArrayList<com.zhongzhou.Excavator.model.NC.report.SaleOrderState>();
		
		Long version = (new Date()).getTime();
		
		for( com.zhongzhou.Excavator.model.NC.report.SaleOrderState saleOrderState : orderStates ){
			
			boolean changed = false;
			saleOrderState.version = version;
			
			com.zhongzhou.Excavator.model.NC.report.SaleOrderState lastVersion = saleOrderInnerDAO.getLastVersionSaleOrderState( "orderId", saleOrderState.orderId );
			
			if( lastVersion != null ){
				
				BeanComparer beanComparer = new BeanComparer();
				
				try {
					saleOrderState.changed = beanComparer.beanDataChanged( saleOrderState , lastVersion );
					if( saleOrderState.changed != null && saleOrderState.changed.size() > 0 ){
						changed = true;
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new ETLRuntimeException("bean comparer failed with Exception:",e);
				}
						
				saleOrderState.lastVersion = lastVersion.version;
			}
			
			if( changed || lastVersion == null ){
				saleOrderState.isTheLastVersion = true;
				pendingProcessSaleOrderState.add( saleOrderState );
			}
		}
		
		IntermediateData mediData = new SimpleIntermediateData();
		mediData.setProcessId ( this.getProcessId() );
		mediData.setDataName  ( "saleOrderStates" );
		mediData.setData      ( pendingProcessSaleOrderState );
		this.outputData.add( mediData );
	}
}