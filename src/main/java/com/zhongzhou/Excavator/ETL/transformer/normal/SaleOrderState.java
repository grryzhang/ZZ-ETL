package com.zhongzhou.Excavator.ETL.transformer.normal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.itextpdf.text.pdf.AcroFields.Item;
import com.zhongzhou.Excavator.ETL.Exception.ETLRuntimeException;
import com.zhongzhou.Excavator.ETL.integration.SimpleBasicNode;
import com.zhongzhou.Excavator.ETL.transformer.Transformer;
import com.zhongzhou.Excavator.ETL.transporter.IntermediateData;
import com.zhongzhou.Excavator.ETL.transporter.impl.SimpleIntermediateData;
import com.zhongzhou.Excavator.model.webData.delmar.ShipmentTracing;
import com.zhongzhou.Excavator.springsupport.injectlist.ETLTransformerList;

@Component( ETLTransformerList.transform_saleOrderState_from_NC_FUTONG  )
@Scope("prototype") 
public class SaleOrderState extends SimpleBasicNode  implements Transformer {
	
	private List<com.zhongzhou.Excavator.model.NC.report.SaleOrderState> ncSaleOrderStates;
	
	private List<com.zhongzhou.Excavator.model.FUTONG.report.FreightInfo> futongFreightInfos;
	
	private List<ShipmentTracing> delmarShipmentTracings;

	private static final String INPUT_NECESSARY_1 = "saleOrderState";
	private static final String INPUT_NECESSARY_2 = "futongFreightInfos";
	private static final String INPUT_NECESSARY_3 = "delmarShipmentTracings";
	
	
	public static final boolean[] necessaryInputReady = {false,false,false};
	
	@Override
	public boolean prepare() {
		
		try{
			if( this.inputData.containsKey( INPUT_NECESSARY_1 ) ){
				necessaryInputReady[0] = true;
				ncSaleOrderStates = (List<com.zhongzhou.Excavator.model.NC.report.SaleOrderState>)this.inputData.get(INPUT_NECESSARY_1).getData();
			}
			if( this.inputData.containsKey( INPUT_NECESSARY_2 ) ){
				necessaryInputReady[1] = true;
				futongFreightInfos = (List<com.zhongzhou.Excavator.model.FUTONG.report.FreightInfo>)this.inputData.get(INPUT_NECESSARY_2).getData();
			}
			if( this.inputData.containsKey( INPUT_NECESSARY_3 ) ){
				necessaryInputReady[2] = true;
				delmarShipmentTracings = (List<ShipmentTracing>)this.inputData.get(INPUT_NECESSARY_3).getData();
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
		
		//attach freightInfo to sale order item
		for( com.zhongzhou.Excavator.model.NC.report.SaleOrderState saleOrderState : ncSaleOrderStates ){
			
			if( saleOrderState.items != null ){

				for( com.zhongzhou.Excavator.model.NC.report.SaleOrderItemState item : saleOrderState.items ){
					
					for( com.zhongzhou.Excavator.model.FUTONG.report.FreightInfo freightInfo : futongFreightInfos ){
						
						if( saleOrderState.orderId!=null && saleOrderState.orderId.equals( freightInfo.contractNo  ) 
							&& item.code != null && item.code.equals( freightInfo.goodsCode ) ){
							
							if( item.freightInfos == null ){
								item.freightInfos = new ArrayList<com.zhongzhou.Excavator.model.FUTONG.report.FreightInfo>(3);
							}
							item.freightInfos.add( freightInfo );
						}
					}
				}
			}
		}
		
		//attach delmar shipmentInfo to freight Info
		for( com.zhongzhou.Excavator.model.NC.report.SaleOrderState saleOrderState : ncSaleOrderStates ){
			if( saleOrderState.items != null ){
				
				for( com.zhongzhou.Excavator.model.NC.report.SaleOrderItemState item : saleOrderState.items ){
					
					if( item.freightInfos != null ){
						
						for( com.zhongzhou.Excavator.model.FUTONG.report.FreightInfo freightInfo : item.freightInfos ){
							
							for( ShipmentTracing tracing : delmarShipmentTracings ){
								
								if( StringUtils.isNoneBlank( freightInfo.mbl ) && StringUtils.isNoneBlank( freightInfo.container ) ){
									
									if( freightInfo.mbl.equals( tracing.mbl ) && freightInfo.container.equals( tracing.containerId ) ){
										
										freightInfo.shipmentTracing = tracing;
									}
								}
							}
						}
					}
				}
			}
		}
		
		IntermediateData mediData = new SimpleIntermediateData();
		mediData.setProcessId ( this.getProcessId() );
		mediData.setDataName  ( "saleOrderStates" );
		mediData.setData      ( ncSaleOrderStates );
		this.outputData.add( mediData );
	}
}
