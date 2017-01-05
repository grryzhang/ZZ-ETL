package com.zhongzhou.Excavator.DAO.oracle.NC.report;

import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zhongzhou.Excavator.Annotation.DataSource;
import com.zhongzhou.Excavator.model.NC.report.SaleOrderState;
import com.zhongzhou.Excavator.springsupport.injectlist.DAOBeanNameList;
import com.zhongzhou.Excavator.springsupport.injectlist.DataSourceList;

@DataSource( dataSource=DataSourceList.NC_ORACLE )  
@Service ( DAOBeanNameList.oracle_nc_report_saleOrderState )
public interface SaleOrderDAO {
	
	public List<SaleOrderState> selectSaleOrderStates() throws SQLException;
}
