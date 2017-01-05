package com.zhongzhou.Excavator.ETL.loader.inner;

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
import com.zhongzhou.Excavator.ETL.loader.Loader;
import com.zhongzhou.Excavator.model.NC.report.NCReportMongoData;
import com.zhongzhou.Excavator.springsupport.injectlist.DAOBeanNameList;
import com.zhongzhou.Excavator.springsupport.injectlist.ETLLoaderList;

@Component( ETLLoaderList.load_saleOrderState_Inner  )
@Scope("prototype") 
public class SaleOrderStateInner  extends SimpleBasicNode  implements Loader {
	
	@Resource( name=DAOBeanNameList.oracle_nc_report_saleOrderState_inner )
	SaleOrderInnerDAO mongoInnerSaleOrderDAO;
	
	private List<com.zhongzhou.Excavator.model.NC.report.SaleOrderState> saleOrderStates;
	
	private static final String INPUT_NECESSARY_1 = "saleOrderStates";

	private static final boolean[] necessaryInputReady = {false};
	
	@Override
	public boolean prepare() {
		
		try{
			if( this.inputData.containsKey( INPUT_NECESSARY_1 ) ){
				necessaryInputReady[0] = true;
				saleOrderStates = (List<com.zhongzhou.Excavator.model.NC.report.SaleOrderState>)this.inputData.get(INPUT_NECESSARY_1).getData();
			}
		}catch( java.lang.ClassCastException exception ) {
			throw new ETLRuntimeException( 
					"Wrong input for " +this.getClass().toString()+ " node, failed in prepare" , 
					exception );
		}
		
		for( boolean inputReady : necessaryInputReady ){
			 
			if( !inputReady ){
				return false;
			}
		}
		
		return true;
	}

	@Override
	public boolean run() {
		
		this.doLoad();
		
		return true;
	}
	
	@Override
	public void doLoad() throws ETLRuntimeException {
		
		Date date = new Date();
		
		if( this.saleOrderStates != null && this.saleOrderStates.size() > 0 ){
			
			List< NCReportMongoData<com.zhongzhou.Excavator.model.NC.report.SaleOrderState> > mongoDatas = new ArrayList< NCReportMongoData< com.zhongzhou.Excavator.model.NC.report.SaleOrderState> > ();
			List<String> orderIds = new ArrayList<String>();		
			
			for( com.zhongzhou.Excavator.model.NC.report.SaleOrderState saleOrderState : this.saleOrderStates ){
				
				if( saleOrderState.isTheLastVersion ){
					
					NCReportMongoData<com.zhongzhou.Excavator.model.NC.report.SaleOrderState> data = new NCReportMongoData<com.zhongzhou.Excavator.model.NC.report.SaleOrderState>();
					
					data.insertTime         = date.getTime();
					data.version            = date.getTime();
					data.modelClassName     = com.zhongzhou.Excavator.model.NC.report.SaleOrderState.class.getName();
					data.modelClass         = com.zhongzhou.Excavator.model.NC.report.SaleOrderState.class;
					data.data               = saleOrderState;
					
					mongoDatas.add( data );
					orderIds.add( saleOrderState.orderId );
				}
			}
			
			mongoInnerSaleOrderDAO.updateRerports2NotLastVersion( orderIds );
			mongoInnerSaleOrderDAO.insertReports( mongoDatas );
		}
	}
}
