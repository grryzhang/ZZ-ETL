package com.zhongzhou.Excavator.model.NC.report;

import java.util.List;

import org.mongodb.morphia.annotations.Entity;

import com.zhongzhou.Excavator.Annotation.BeanComparableProp;
import com.zhongzhou.Excavator.Annotation.BeanComparePropId;
import com.zhongzhou.Excavator.model.FUTONG.report.FreightInfo;

/**
 * User for data select from NC DB as report
 * @author zhanghuanping
 *
 */
@Entity( value="BIReports" )
public class SaleOrderItemState {
	
	@BeanComparePropId
	/** 销售订单号*/
	public String orderId;
	
	/** 客户订单号*/
	@BeanComparePropId
	public String customerOrderId;
	
	@BeanComparePropId
	public String code;
	
	@BeanComparePropId
	public String type;
	
	public String name;
	
	public String specification;
	
	@BeanComparableProp
	public float totalNumber;
	
	public String unit;
	
	@BeanComparableProp
	public float unitPrice;
	
	@BeanComparableProp
	public float totalPrice;
	
	@BeanComparableProp
	/** 预计交货日期 */
	public String forecastDeliveryTime;

	@BeanComparableProp
	/** 预计出厂日期*/
	public String deliveryTime;
	
	@BeanComparableProp
	/* production */
	/** 实际交货日期，实际出厂日期，工厂生产完成日期 ，公司收货日期*/
	public String productionEndTime;
	
	
	
	/* QC */
	/** 质检结果*/
	@BeanComparableProp
	public String inspection;
	
	/** 质检日期*/
	@BeanComparableProp
	public String inspectionDate;
	
	@BeanComparableProp( isRecursion = true )
	public List<FreightInfo> freightInfos;
	
	/* finance */
	public String invoicedTime;
	
	public String paymentTerm;
}
