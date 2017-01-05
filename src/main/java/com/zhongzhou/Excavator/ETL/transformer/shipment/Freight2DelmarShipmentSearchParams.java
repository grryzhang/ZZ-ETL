/**
 * 
 */
/**
 * @author zhanghuanping
 *
 */
package com.zhongzhou.Excavator.ETL.transformer.shipment;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.zhongzhou.Excavator.ETL.Exception.ETLRuntimeException;
import com.zhongzhou.Excavator.ETL.integration.SimpleBasicNode;
import com.zhongzhou.Excavator.ETL.transformer.Transformer;
import com.zhongzhou.Excavator.ETL.transporter.IntermediateData;
import com.zhongzhou.Excavator.ETL.transporter.impl.SimpleIntermediateData;
import com.zhongzhou.Excavator.model.webData.delmar.ShipmentTracingSearchParams;
import com.zhongzhou.Excavator.springsupport.injectlist.ETLTransformerList;

@Component( ETLTransformerList.transform_freightInfo_2_delmar_ShipmentSearchParams  )
@Scope("prototype") 
public class Freight2DelmarShipmentSearchParams extends SimpleBasicNode  implements Transformer {
	
	private List<com.zhongzhou.Excavator.model.FUTONG.report.FreightInfo> freightInfos;

	private static final String INPUT_NECESSARY_1 = "futongFreightInfos";
	
	public static final boolean[] necessaryInputReady = {false};
	
	@Override
	public boolean prepare() {
		
		try{
			if( this.inputData.containsKey("futongFreightInfos") ){
				necessaryInputReady[0] = true;
				freightInfos = (List<com.zhongzhou.Excavator.model.FUTONG.report.FreightInfo>)this.inputData.get(INPUT_NECESSARY_1).getData();
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
		
		this.doTransform();
		
		return true;
	}

	@Override
	public void doTransform() {
		
		List<ShipmentTracingSearchParams> searchParams = new ArrayList<ShipmentTracingSearchParams>();
		for( com.zhongzhou.Excavator.model.FUTONG.report.FreightInfo freightInfo : freightInfos ){
			if( StringUtils.isNotBlank( freightInfo.mbl ) &&  StringUtils.isNotBlank( freightInfo.container ) ){
				ShipmentTracingSearchParams searchParam = new ShipmentTracingSearchParams();
				searchParam.containerId = freightInfo.container;
				searchParam.mbl = freightInfo.mbl;
				searchParams.add( searchParam );
			}
		}
		IntermediateData mediData = new SimpleIntermediateData();
		mediData = new SimpleIntermediateData();
		mediData.setProcessId ( this.getProcessId() );
		mediData.setDataName  ( "shipmentTracingSearchParameter" );
		mediData.setData      ( searchParams );
		this.outputData.add( mediData );
	}
}
