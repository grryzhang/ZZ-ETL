package com.zhongzhou.Excavator.ETL.integration;

import java.util.List;

import com.zhongzhou.Excavator.ETL.transporter.IntermediateData;

public interface BasicNodeTrigger {
	
	public void setProcessId( String processId );
	
	public String getProcessId();
	
	public void setTargetNode( BasicNode node );

	@SuppressWarnings("rawtypes")
	public void trigger( IntermediateData nodeOutputData);
	
	@SuppressWarnings("rawtypes")
	public void trigger( List<IntermediateData> nodeOutputData );
}
