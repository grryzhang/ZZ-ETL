package com.zhongzhou.Excavator.ETL.integration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.zhongzhou.Excavator.ETL.Exception.ETLRuntimeException;
import com.zhongzhou.Excavator.ETL.transporter.IntermediateData;
import com.zhongzhou.Excavator.springsupport.injectlist.ProcessComponentList;

@Component(ProcessComponentList.simpleNodeTrigger)
@Scope("prototype") 
public class SimpleBasicTrigger implements BasicNodeTrigger {
	
	private BasicNode targetNode;
	
	private String processId;
	
	public SimpleBasicTrigger(){};
	
	public SimpleBasicTrigger( String processId, BasicNode nextNode ){
		
		this.setProcessId  ( processId );
		this.setTargetNode ( nextNode  );
	}
	
	@Override
	public void setProcessId( String processId ){
		this.processId = processId;
	}
	
	@Override
	public String getProcessId(){
		return this.processId;
	}

	@Override
	public void setTargetNode( BasicNode node ){
		this.targetNode = node;
	}

	@SuppressWarnings("rawtypes") 
	@Override
	public void trigger(final IntermediateData inputData ) throws ETLRuntimeException {
		
		List<IntermediateData> outputData = new ArrayList<IntermediateData>(1);
		outputData.add( inputData );
		this.trigger( outputData );
	}

	@SuppressWarnings("rawtypes") 
	@Override
	public void trigger( final List<IntermediateData> nodeOutputData ) {
		
		if( this.targetNode==null ){
			throw new ETLRuntimeException( "No specified node in Trigger, trigger type:" + this.getClass().getName() );
		}
		
		targetNode.start( nodeOutputData );
	}
}
