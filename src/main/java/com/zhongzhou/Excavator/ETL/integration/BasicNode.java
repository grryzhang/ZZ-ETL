package com.zhongzhou.Excavator.ETL.integration;

import java.util.List;

import com.zhongzhou.Excavator.ETL.transporter.IntermediateData;

public interface BasicNode {
	
	public void setProcessId( String processId );
	
	public String getProcessId();
	
	public void addNextNode( BasicNode node );
	
	public void removeNextNode( BasicNode node );
	
	@SuppressWarnings("rawtypes")
	public void start( final List<IntermediateData> inputData );
	
	public boolean prepare();
	
	public boolean run();
	
	@SuppressWarnings("rawtypes")
	public void next( final List<IntermediateData> output );
}
