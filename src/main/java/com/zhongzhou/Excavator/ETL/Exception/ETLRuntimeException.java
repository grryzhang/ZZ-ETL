package com.zhongzhou.Excavator.ETL.Exception;

@SuppressWarnings("serial")
public class ETLRuntimeException extends RuntimeException {
	
	private String exceptionId;

	public ETLRuntimeException(String message){
		super(message);
	}

	public ETLRuntimeException(String message,Throwable cause){
		
		super(message,cause);
	}

	public ETLRuntimeException(Throwable cause){
		
		super(cause);
	}

	public String getExceptionId() {
		return exceptionId;
	}

	public void setExceptionId(String exceptionId) {
		this.exceptionId = exceptionId;
	}
}
