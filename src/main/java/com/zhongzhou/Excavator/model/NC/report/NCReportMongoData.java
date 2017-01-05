package com.zhongzhou.Excavator.model.NC.report;

import org.mongodb.morphia.annotations.Entity;

@Entity( value="report_nc_saleOrder" )
public class NCReportMongoData<T> {

	public long insertTime;
	
	public String modelClassName;
	
	public Class<T> modelClass;
	
	public long version;
	
	public T data;
}
