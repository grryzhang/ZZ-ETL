
/**
 * @author zhanghuanping
 *
 */
package com.zhongzhou.Excavator.DAO.mongo.NC.report;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.springframework.stereotype.Service;

import com.zhongzhou.Excavator.model.NC.report.NCReportMongoData;
import com.zhongzhou.Excavator.model.NC.report.SaleOrderState;
import com.zhongzhou.Excavator.springsupport.injectlist.DAOBeanNameList;
import com.zhongzhou.Excavator.springsupport.injectlist.DataSourceList;

@Service( DAOBeanNameList.mongo_nc_report_saleOrder )
public class SaleOrderDAO{
	
	@Resource(name=DataSourceList.MONGO_MD_OUTERNET_DOCUMENTS)
	Datastore  mongoMorphiaDataStore;
	
	public <SaleOrderState> void insertReport( NCReportMongoData<SaleOrderState> data ){
		
		mongoMorphiaDataStore.save( data );
	}
	
	public <SaleOrderState> void insertReports( List< NCReportMongoData<SaleOrderState> > dataList ){
		
		mongoMorphiaDataStore.save( dataList );
	}
	
	public void updateRerports2NotLastVersion( List<String> orderIds ){
		
		Query< NCReportMongoData<SaleOrderState> > query =
				( Query< NCReportMongoData<SaleOrderState> > )mongoMorphiaDataStore
				.createQuery( (new NCReportMongoData<SaleOrderState>() ).getClass() ).disableValidation()
				.filter( "modelClassName", SaleOrderState.class.getName() );
		query.disableValidation().field("data.orderId").hasAnyOf( orderIds );
		
		UpdateOperations< NCReportMongoData<SaleOrderState> > updateOperations = 
				( UpdateOperations< NCReportMongoData<SaleOrderState> > )mongoMorphiaDataStore
				.createUpdateOperations( (new NCReportMongoData<SaleOrderState>() ).getClass() )
				.disableValidation()
				.set("data.isTheLastVersion", false);
		
		mongoMorphiaDataStore.update( query , updateOperations );
	}
	
	public SaleOrderState getSaleOrderState( String id ){

		Query< NCReportMongoData<SaleOrderState> > query =
				( Query< NCReportMongoData<SaleOrderState> > )mongoMorphiaDataStore
				.createQuery( (new NCReportMongoData<SaleOrderState>() ).getClass() )
				.filter( "modelClassName", SaleOrderState.class.getName() )
				.disableValidation();
		
		query.filter( "_id" , id );
		
		NCReportMongoData<SaleOrderState> lastOrderStatus = query.get();
		
		if( lastOrderStatus != null ){
			return lastOrderStatus.data;
		}
			
		return null;
	}
	
	public SaleOrderState getLastVersionSaleOrderState( String dataKey, String value ){

		Query< NCReportMongoData<SaleOrderState> > query =
				( Query< NCReportMongoData<SaleOrderState> > )mongoMorphiaDataStore
				.createQuery( (new NCReportMongoData<SaleOrderState>() ).getClass() )
				.filter( "modelClassName", SaleOrderState.class.getName() )
				.disableValidation();
		
		query.filter( "data." + dataKey , value );
		
		NCReportMongoData<SaleOrderState> lastOrderStatus = query.order("-data.version").get();
		
		if( lastOrderStatus != null ){
			return lastOrderStatus.data;
		}
			
		return null;
	}
}
