package com.zhongzhou.Excavator.ETL.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zhongzhou.Excavator.ETL.Exception.ETLRuntimeException;
import com.zhongzhou.Excavator.ETL.transporter.IntermediateData;
import com.zhongzhou.Excavator.ETL.transporter.impl.SimpleIntermediateData;

public abstract class SimpleBasicNode implements BasicNode {
	
	public static final String NODE_INPUT_NECESSARY_1 = "ProcessID";
	
	private List<BasicNodeTrigger> nexNodeTriggers = new ArrayList<BasicNodeTrigger>( 2 );
	
	@SuppressWarnings("rawtypes")
	protected Map<String,IntermediateData> inputData;
	
	@SuppressWarnings("rawtypes")
	protected List<IntermediateData> outputData;
	
	protected String processId;
	
	@Override
	public String getProcessId(){
		return this.processId;
	}
	
	@Override
	public void setProcessId( String processId ){
		this.processId = processId;
	}
	
	@Override
	public void addNextNode(BasicNode node) {
		
		BasicNodeTrigger trigger = new SimpleBasicTrigger();
		trigger.setTargetNode( node );
		
		if( !this.nexNodeTriggers.contains( trigger ) ){
			this.nexNodeTriggers.add( trigger );
		}
	}

	@Override
	public void removeNextNode(BasicNode node) {
		this.nexNodeTriggers.remove( node );
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void start( List<IntermediateData> inputData ){
		
		this.inputData = new HashMap<String,IntermediateData>();
		this.outputData = new ArrayList<IntermediateData>();
		
		/* start - check if get process ID */
		boolean hasProcessId = false;
		if( inputData != null ){
			
			for( IntermediateData mediaData : inputData ){
				
				if( mediaData.getDataName()!=null && mediaData.getData() != null ){
					
					if( !hasProcessId 
							&& NODE_INPUT_NECESSARY_1.equals( mediaData.getDataName() )
							&& ( mediaData.getData() instanceof String ) ){
						hasProcessId = true;
						this.setProcessId( (String)mediaData.getData() );
					}
					this.inputData.put( mediaData.getDataName(), mediaData );
				}
			}
		}
		if(!hasProcessId){
			throw new ETLRuntimeException( 
					"ProcessID missing in "+this.getClass().getName()+ " node, failed in start" );
		}
		/* end - check if get process ID */
 		
		
		boolean isRun = false;
		boolean isPrepare = false;
		
		isPrepare = this.prepare();
		
		if( isPrepare  )  isRun = this.run();
		
		if( isRun ) this.next(this.outputData);
	}

	@Override
	public void next( List<IntermediateData> outputData ){
		
		IntermediateData<String> mediData = new SimpleIntermediateData<String>();
		mediData.setData( this.getProcessId() );
		mediData.setDataName( NODE_INPUT_NECESSARY_1 );
		mediData.setProcessId( this.getProcessId() );
		outputData.add( mediData );
		
		for( BasicNodeTrigger trigger : nexNodeTriggers ){
			trigger.trigger( outputData );
		}
	}
}
