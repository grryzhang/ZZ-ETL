package com.zhongzhou.Excavator.ETL.transporter.impl;

import com.zhongzhou.Excavator.ETL.transporter.IntermediateData;

public class SimpleIntermediateData<T> implements IntermediateData<T>{
	
	private String processId;
	
	private T data;
	
	private String dataName;

	@Override
	public String getProcessId() {
		return processId;
	}

	@Override
	public void setProcessId(String processId) {
		this.processId = processId;
	}

	@Override
	public T getData() {
		return data;
	}

	@Override
	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String getDataName() {
		return dataName;
	}

	@Override
	public void setDataName(String dataName) {
		this.dataName = dataName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Class<T> getDataClass() {
		return null == this.data ? null : (Class<T>)this.data.getClass();
	}
}
