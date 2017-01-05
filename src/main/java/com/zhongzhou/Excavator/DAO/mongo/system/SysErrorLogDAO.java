package com.zhongzhou.Excavator.DAO.mongo.system;

import javax.annotation.Resource;

import org.mongodb.morphia.Datastore;
import org.springframework.stereotype.Service;

import com.zhongzhou.Excavator.springsupport.injectlist.DAOBeanNameList;
import com.zhongzhou.common.model.log.SysErrorLog;

@Service( DAOBeanNameList.mongo_md_system_SysErrorLog )
public class SysErrorLogDAO {

	@Resource(name="mongoMorphiaDataStore")
	Datastore  mongoMorphiaDataStore;
	
	public void insertLog( SysErrorLog sysErrorLog ){
		
		mongoMorphiaDataStore.save( sysErrorLog );
	}
}
