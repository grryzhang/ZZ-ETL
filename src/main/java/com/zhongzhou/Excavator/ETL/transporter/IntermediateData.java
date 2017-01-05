package com.zhongzhou.Excavator.ETL.transporter;

public interface IntermediateData<T> {

	public void setProcessId( final String processId );
	
	public String getProcessId();

	public String getDataName();

	public void setDataName(String dataName);

	public Class<T> getDataClass();

	public T getData();

	public void setData(T data);
}
