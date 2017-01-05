package com.zhongzhou.Excavator.web.controller.RESTService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhongzhou.Excavator.DAO.mongo.system.ServiceLogDAO;
import com.zhongzhou.Excavator.ETL.integration.normal.SaleOrderState;
import com.zhongzhou.Excavator.ETL.transporter.IntermediateData;
import com.zhongzhou.Excavator.ETL.transporter.impl.SimpleIntermediateData;
import com.zhongzhou.Excavator.springsupport.injectlist.DAOBeanNameList;
import com.zhongzhou.Excavator.springsupport.injectlist.ETLProcessList;
import com.zhongzhou.common.model.log.ServiceLog;
import com.zhongzhou.common.model.web.JsonResponse;

@Controller
public class IntegrationController {
	
	@Resource( name=DAOBeanNameList.mongo_md_system_serviceLog )
	ServiceLogDAO serviceLogDAO;
	
	@Resource( name=ETLProcessList.saleOrderState_2_publish )
	SaleOrderState exportNCSOProcess;
	
	@RequestMapping(method=RequestMethod.GET, value="/EXPORT/NC/SOState")
	public @ResponseBody JsonResponse loadRTPOStatus( 
			HttpServletRequest request, 
			HttpServletResponse response, 
			HttpSession session ) throws Exception{  
		
		JsonResponse result = new JsonResponse();
		
		result.setSuccess( true );
		result.setActionMessage( "The Service is ok and ready, please attach the /{logId} to this link to run the process" );
		result.setTotalResult( 0 );
		
		return result;
	}
	
	@RequestMapping(method=RequestMethod.GET, value="/EXPORT/NC/SOState/{id}")
	public @ResponseBody JsonResponse loadRTPO( 
			@PathVariable String id,
			HttpServletRequest request, 
			HttpServletResponse response, 
			HttpSession session ) throws Exception{  
		
		JsonResponse result = new JsonResponse();
		
		if( id == null || id.length() < 1 ){
			
			result.setSuccess( false );
			result.setActionMessage( "No log id, service would not run. Please attach log id in path." );
			result.setTotalResult( 0 );
			
			return result;
		}
		
		boolean processOK = true;
		
		ServiceLog serviceLog = new ServiceLog();
		serviceLog.setLogId(id);
		serviceLog.setServiceLog("In processing");
		serviceLog.setSuccess( String.valueOf( processOK ) );
		
		serviceLogDAO.insertLog(serviceLog);
		
		String resultLog = null;
		try{
			
			IntermediateData mediData = new SimpleIntermediateData();
			mediData.setData( id );
			mediData.setDataName( SaleOrderState.NODE_INPUT_NECESSARY_1 );
			mediData.setProcessId( id );
			
			exportNCSOProcess.start( Arrays.asList( mediData ) );
			
			serviceLog.setServiceLog(resultLog);
			
		}catch( Exception e ){
			
			processOK = false;

			serviceLog.setExceptionId( id );
			serviceLog.setSuccess("false");
			
			StringWriter sw = new StringWriter();  
            PrintWriter pw = new PrintWriter(sw);  
            e.printStackTrace(pw);
            String statckTrace = sw.toString();
            if( statckTrace.length() > 65530 ){
            	statckTrace = statckTrace.substring(0, 65530);
            }
            serviceLog.setServiceLog( statckTrace );
		}
		serviceLogDAO.insertLog(serviceLog);

		result.setSuccess( processOK );
		result.setActionMessage( serviceLog.getSuccess() );
		result.setData( resultLog );
		result.setTotalResult( 0 );
		
		return result;
	}
}
