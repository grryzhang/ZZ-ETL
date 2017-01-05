package com.zhongzhou.Excavator.ETL.integration.normal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.zhongzhou.Excavator.ETL.integration.BasicNode;
import com.zhongzhou.Excavator.ETL.integration.StartNode;
import com.zhongzhou.Excavator.springsupport.SpringContextHolder;
import com.zhongzhou.Excavator.springsupport.injectlist.ETLExtractorList;
import com.zhongzhou.Excavator.springsupport.injectlist.ETLLoaderList;
import com.zhongzhou.Excavator.springsupport.injectlist.ETLProcessList;
import com.zhongzhou.Excavator.springsupport.injectlist.ETLTransformerList;


@Service(ETLProcessList.saleOrderState_2_publish)
@Scope("prototype") 
public class SaleOrderState extends StartNode{

	@Autowired
	private SpringContextHolder springContextHolder;

	@Override
	public boolean prepare() {
		
		BasicNode saleOrderStateExtractor =
				(BasicNode)springContextHolder.getBean(ETLExtractorList.nc_saleOrderState_report_extractor);
		
		BasicNode freightInfoExtractor =
				(BasicNode)springContextHolder.getBean(ETLExtractorList.futong_freight_info_report_extractor);
		
		BasicNode delmarShipmentTracing =
				(BasicNode)springContextHolder.getBean(ETLExtractorList.delmar_shipment_tracing);
		
		BasicNode freight2DelmarShipmentSearchParams =
				(BasicNode)springContextHolder.getBean(ETLTransformerList.transform_freightInfo_2_delmar_ShipmentSearchParams);
		
		BasicNode saleOrderStateTransformer =
				(BasicNode)springContextHolder.getBean(ETLTransformerList.transform_saleOrderState_from_NC_FUTONG);
		
		BasicNode saleOrderStateVersionCompareTransformer =
				(BasicNode)springContextHolder.getBean(ETLTransformerList.saleOrderState_versionCompare);
		
		BasicNode saleOrderStateLoader =  
				(BasicNode)springContextHolder.getBean(ETLLoaderList.load_saleOrderState_to_ec);
		
		BasicNode saleOrderStateInnerLoader = 
				(BasicNode)springContextHolder.getBean(ETLLoaderList.load_saleOrderState_Inner);
		
		this.addNextNode( saleOrderStateExtractor );
		
		saleOrderStateExtractor.addNextNode( freightInfoExtractor );
		saleOrderStateExtractor.addNextNode( saleOrderStateTransformer );
		
		freightInfoExtractor.addNextNode( freight2DelmarShipmentSearchParams );
		freightInfoExtractor.addNextNode( saleOrderStateTransformer );
		
		freight2DelmarShipmentSearchParams.addNextNode( delmarShipmentTracing );
		
		delmarShipmentTracing.addNextNode( saleOrderStateTransformer );
		
		saleOrderStateTransformer.addNextNode( saleOrderStateVersionCompareTransformer );
		
		saleOrderStateVersionCompareTransformer.addNextNode( saleOrderStateLoader );
		saleOrderStateVersionCompareTransformer.addNextNode( saleOrderStateInnerLoader );
		
		return true;
	}

	@Override
	public boolean run() {
		
		return true;
	}
}
