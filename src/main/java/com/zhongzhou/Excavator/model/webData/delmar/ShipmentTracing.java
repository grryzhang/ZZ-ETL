package com.zhongzhou.Excavator.model.webData.delmar;

import org.mongodb.morphia.annotations.Entity;

import com.zhongzhou.Excavator.Annotation.BeanComparableProp;
import com.zhongzhou.Excavator.Annotation.BeanComparePropId;

@Entity( value="web_data_delmar_shipment_tracking" )
public class ShipmentTracing {
	
	public long version;
	
	@BeanComparePropId
	public String mbl;
	
	@BeanComparePropId
	public String containerId;
		
	@BeanComparableProp
	public String consignee;
	
	@BeanComparableProp
	public String loading;
	
	@BeanComparableProp
	public String ETD;
	
	public String finalDestination;
	
	@BeanComparableProp
	public String discharge;
	
	@BeanComparableProp
	public String ETAPortOfDischarge;
	
	@BeanComparableProp
	public String destination;
	
	@BeanComparableProp
	public String ETAFinalDestination;
	
	@BeanComparableProp
	public String currentPortOfDischarge;
	
	@BeanComparableProp
	public String dischargeCurrentETA;
	
	@BeanComparableProp
	public String currentPortOfDestination;
	
	@BeanComparableProp
	public String destinationcurrentETA;
		
	@BeanComparableProp
	public String customsReleaseDate;
	
	@BeanComparableProp
	public String dateStorageBegins;
	
	@BeanComparableProp
	public String dateOfDeliveryOrPickUp;
}
