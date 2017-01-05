
/**
 * @author zhanghuanping
 *
 */
package com.zhongzhou.Excavator.DAO.mongo.webData;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Service;

import com.zhongzhou.Excavator.model.NC.report.NCReportMongoData;
import com.zhongzhou.Excavator.model.NC.report.SaleOrderState;
import com.zhongzhou.Excavator.model.webData.delmar.ShipmentTracing;
import com.zhongzhou.Excavator.model.webData.delmar.ShipmentTracingSearchParams;
import com.zhongzhou.Excavator.springsupport.injectlist.DAOBeanNameList;
import com.zhongzhou.Excavator.springsupport.injectlist.DataSourceList;

@Service( DAOBeanNameList.mongo_webData_delmar_shipmentTracing )
public class DelmarShipmentTracingDAO{
	
	@Resource(name=DataSourceList.MONGO_MD_DOCUMENTS)
	Datastore  mongoMorphiaDataStore;
	
	public ShipmentTracing getLastVersion( ShipmentTracingSearchParams searchParams ){

		Query< ShipmentTracing > query =
				( Query< ShipmentTracing > )mongoMorphiaDataStore
				.createQuery( (new ShipmentTracing() ).getClass() )
				.disableValidation();
		
		query.filter( "mbl"         , searchParams.mbl         );
		query.filter( "containerId" , searchParams.containerId );
		
		ShipmentTracing lastOrderStatus = query.order("-version").get();
		
		if( lastOrderStatus != null ){
			return lastOrderStatus;
		}
			
		return null;
	}
}
