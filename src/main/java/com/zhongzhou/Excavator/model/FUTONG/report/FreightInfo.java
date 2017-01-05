package com.zhongzhou.Excavator.model.FUTONG.report;

import java.sql.Timestamp;

import com.zhongzhou.Excavator.Annotation.BeanComparableProp;
import com.zhongzhou.Excavator.Annotation.BeanComparePropId;
import com.zhongzhou.Excavator.model.webData.delmar.ShipmentTracing;

public class FreightInfo {
	
	@BeanComparePropId
	public String contractNo;
	
	@BeanComparePropId
	public String custContractNo;
	
	public String goodsCode;
	
	public String custGoodsCode;

	@BeanComparableProp
	public String invoiceNumber;
	
	@BeanComparableProp
	public String invoiceDate;
	
	@BeanComparableProp
	public String estimatedTimeofDelivery;
	
	@BeanComparableProp
	public String estimatedTimeOfArrival;
	
	@BeanComparableProp
	public String receiptDate;

	public String customerContractNumber;
	
	public String mbl;
	
	public String container;
	
	@BeanComparableProp
	public String startPlace;
	
	@BeanComparableProp
	public String endPlace;
	
	@BeanComparableProp
	public String transportMode;
	
	public String totalBulk;
	
	@BeanComparableProp( isRecursion = true )
	public ShipmentTracing shipmentTracing;
}
