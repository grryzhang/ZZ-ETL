package com.zhongzhou.Excavator.ETL.transporter;

import com.zhongzhou.Excavator.ETL.Exception.ETLRuntimeException;

public interface DataTransporter {

	public void doTransport() throws ETLRuntimeException;
}
