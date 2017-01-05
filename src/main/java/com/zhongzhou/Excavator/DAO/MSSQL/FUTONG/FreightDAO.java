package com.zhongzhou.Excavator.DAO.MSSQL.FUTONG;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zhongzhou.Excavator.Annotation.DataSource;
import com.zhongzhou.Excavator.model.FUTONG.report.FreightInfo;
import com.zhongzhou.Excavator.model.FUTONG.report.FreightInfoSearchParameter;
import com.zhongzhou.Excavator.springsupport.injectlist.DAOBeanNameList;
import com.zhongzhou.Excavator.springsupport.injectlist.DataSourceList;

@DataSource( dataSource=DataSourceList.FUTONG_MSSQL )  
@Service( DAOBeanNameList.MSSQL_FUTONG_freight )
public interface FreightDAO {

	public List<FreightInfo> selectFreightInfo( FreightInfoSearchParameter searchParameters ) throws SQLException;
	
}
