package com.zhongzhou.Excavator.model.NC.report;

import java.util.List;

import org.mongodb.morphia.annotations.Entity;

import com.zhongzhou.Excavator.Annotation.BeanComparableProp;

/**
 * User for data select from NC DB as report
 * @author zhanghuanping
 *
 */
public class SaleOrderState {
	
	/* For system */
	public boolean isTheLastVersion;
	
	public long version;
	
	public long lastVersion;
	
	public List<String> changed; 
	/* For system */
	
	
	/** 销售订单号*/
	@BeanComparableProp
	public String orderId;
	
	/** 客户订单号*/
	@BeanComparableProp
	public String customerOrderId;
	
	/** 订单时间*/
	public String orderTime;
	
	
	public String invoiceCode;
	
	
	/** 客户名字*/
	public String customerName;
	
	/** 客户采购员 */
	public String customerBuyer;
	
	/** 价格条款 */
	@BeanComparableProp
	public String tradeTerm;
	
	/** 业务员 */
	@BeanComparableProp
	public String businessUser;
	
	@BeanComparableProp( isRecursion = true )
	public List<SaleOrderItemState> items;
}
