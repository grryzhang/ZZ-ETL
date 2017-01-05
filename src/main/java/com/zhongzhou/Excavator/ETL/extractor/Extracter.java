package com.zhongzhou.Excavator.ETL.extractor;

import com.zhongzhou.Excavator.ETL.Exception.ETLRuntimeException;

public interface Extracter{
	
	public void doExtract() throws ETLRuntimeException;
}
