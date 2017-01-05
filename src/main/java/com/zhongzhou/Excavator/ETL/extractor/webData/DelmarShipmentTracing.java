
package com.zhongzhou.Excavator.ETL.extractor.webData;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.zhongzhou.Excavator.DAO.mongo.webData.DelmarShipmentTracingDAO;
import com.zhongzhou.Excavator.ETL.Exception.ETLRuntimeException;
import com.zhongzhou.Excavator.ETL.extractor.Extracter;
import com.zhongzhou.Excavator.ETL.integration.BasicNode;
import com.zhongzhou.Excavator.ETL.integration.SimpleBasicNode;
import com.zhongzhou.Excavator.ETL.transporter.IntermediateData;
import com.zhongzhou.Excavator.ETL.transporter.impl.SimpleIntermediateData;
import com.zhongzhou.Excavator.model.webData.delmar.ShipmentTracing;
import com.zhongzhou.Excavator.model.webData.delmar.ShipmentTracingSearchParams;
import com.zhongzhou.Excavator.springsupport.injectlist.DAOBeanNameList;
import com.zhongzhou.Excavator.springsupport.injectlist.ETLExtractorList;

@Component(ETLExtractorList.delmar_shipment_tracing)
@Scope("prototype")  
public class DelmarShipmentTracing extends SimpleBasicNode implements Extracter, BasicNode {

	
	@Resource( name = DAOBeanNameList.mongo_webData_delmar_shipmentTracing )
	private DelmarShipmentTracingDAO delmarShipmentTracingDAO;
	
	private static final String INPUT_NECESSARY_1 = "shipmentTracingSearchParameter";
	public static final String[] necessaryInput = {INPUT_NECESSARY_1};
	
	private List<ShipmentTracingSearchParams> searchParameters;

	@Override
	public boolean prepare() {
		
		boolean input1 = true;
		
		/* start - restore data */
		try{
			searchParameters = ( List<ShipmentTracingSearchParams> )this.inputData.get(INPUT_NECESSARY_1).getData();
		}catch( java.lang.ClassCastException exception ) {
			throw new ETLRuntimeException( 
				"Wrong \"shipmentTracingSearchParameter\" input for "
				+ this.getClass().getName()
				+ "node,failed in prepare" , exception );
		}		
		/* end - restore data */
		
		if( input1 ){
			return true;
		}
		
		return false;
	}

	@Override
	public boolean run() {
		
		this.doExtract();
		
		return true;
	}

	@Override
	public void doExtract() throws ETLRuntimeException {
		
		try {
			
			List<ShipmentTracing> tracings = new ArrayList<ShipmentTracing>();
			for( ShipmentTracingSearchParams searchParameter : searchParameters   ){
				
				if( searchParameter.mbl != null && searchParameter.containerId != null ){
					
					ShipmentTracing tracing = delmarShipmentTracingDAO.getLastVersion( searchParameter );
					if( tracing != null ){
						tracings.add( delmarShipmentTracingDAO.getLastVersion( searchParameter ) );
					}
				}
			}
			
			IntermediateData mediData = new SimpleIntermediateData();
			mediData.setProcessId ( this.getProcessId() );
			mediData.setDataName  ( "delmarShipmentTracings" );
			mediData.setData      ( tracings );
			this.outputData.add( mediData );
		} catch ( Exception e ) {
			throw new ETLRuntimeException( "Failed in extracting of extractor : " + this.getClass().toString() , e);
		}
	}
}
