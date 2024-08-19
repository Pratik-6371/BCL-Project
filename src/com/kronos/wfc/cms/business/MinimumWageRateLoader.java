package com.kronos.wfc.cms.business;

import com.kronos.wfc.commonapp.genies.business.ColumnData;
import com.kronos.wfc.commonapp.genies.business.ColumnDataLoader;
import com.kronos.wfc.commonapp.genies.business.ICommonData;
import com.kronos.wfc.commonapp.genies.business.IPluginAppCommonData;
import com.kronos.wfc.commonapp.genies.business.SummaryViewCommonData;
import com.kronos.wfc.commonapp.genies.business.SummaryViewDetail;
import com.kronos.wfc.commonapp.genies.business.TableDataHandler;
import com.kronos.wfc.commonapp.hyperfind.business.HyperFindPreparedStmtMarshal;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.SQLStatement;
import com.kronos.wfc.platform.persistence.framework.statement.DataStore;
import com.kronos.wfc.platform.persistence.framework.statement.LiteralSQLString;
import com.kronos.wfc.platform.properties.framework.KronosProperties;
import com.kronos.wfc.platform.utility.framework.datetime.KDateSpan;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MinimumWageRateLoader
  extends ColumnDataLoader
  implements IPluginAppCommonData
{
  private ArrayList personIdList;
  protected static int PERSON_ID = 1;
  protected static int LABOR_ACCOUNT_ID = 2;
  protected static int EMPLOYEE_NAME = 3;
  private static int InClauseMaxCount = Integer.parseInt(KronosProperties.getProperty("business.persistence.InClauseMaxCount"));
  ArrayList list;
  HashMap<ObjectIdLong, Double> map;
  
  public MinimumWageRateLoader(TableDataHandler tbHandler, SummaryViewDetail svDetail, ArrayList ccDetail, int vType, int col, ICommonData data) {
    super(tbHandler, svDetail, ccDetail, vType, data);
    personIdList = new ArrayList();
    list = new ArrayList();
    map = new HashMap();
    m_data.setPluginAppCommonData(getFullyQualifiedName(), this);
  }
  
  protected ColumnData getData() {
    ArrayList sqlSubstitutionArrayList = null;
    ArrayList parameterList = null;
    mcMarshal = m_data.getHFPreparedStmtMarshal();
    sqlSubstitutionArrayList = new ArrayList();
    sqlSubstitutionArrayList.add(new LiteralSQLString(mcMarshal.getHfQuery()));
    parameterList = new ArrayList();
    parameterList.addAll(mcMarshal.getHfQueryParameters());
    SQLStatement stmt = null;
    ObjectIdLong timeFrameId = m_data.getTimeFrameId();
    if (timeFrameId.toLong().intValue() == 0) {
      stmt = new SQLStatement(3, "business.cms.SummaryViewSqlStringConstant.MINIMUMWAGE_CURRENT_PAY_PERIOD", sqlSubstitutionArrayList);
    }
    else if (timeFrameId.toLong().intValue() == 1) {
      stmt = new SQLStatement(3, "business.cms.SummaryViewSqlStringConstant.MINIMUMWAGE_PREVIOUS_PAY_PERIOD", sqlSubstitutionArrayList);
    }
    else if (timeFrameId.toLong().intValue() == 2)
    {
      stmt = new SQLStatement(3, "business.cms.SummaryViewSqlStringConstant.MINIMUMWAGE_NEXT_PAY_PERIOD", sqlSubstitutionArrayList);
    }
    else {
      KDateSpan dateRange = m_data.getDateRange();
      parameterList.add(dateRange.getEnd());
      parameterList.add(dateRange.getBegin());
      stmt = new SQLStatement(3, "business.cms.SummaryViewSqlStringConstant.MINIMUMWAGE_DATE_RANGE", sqlSubstitutionArrayList);
    }
    stmt.execute(parameterList);
    
    for (DataStore ds = stmt.getDataStore(); ds.nextRow();)
    {

      Object[] rowData = new Object[2];
      rowData[0] = ds.getObjectIdLong(1);
      rowData[1] = Double.valueOf(ds.getDouble(2).doubleValue() + ds.getDouble(3).doubleValue() + ds.getDouble(3).doubleValue());
      map.put(ds.getObjectIdLong(1), Double.valueOf(ds.getDouble(2).doubleValue() + ds.getDouble(3).doubleValue() + ds.getDouble(3).doubleValue()));
      list.add(rowData);
    }
    
    filterList(personIdList, list, null);
    return new ColumnData(personIdList, 1);
  }
  
  public Map<ObjectIdLong, Double> getRateMap() {
    return map;
  }
  
  public String getFullyQualifiedName()
  {
    return "com.kronos.wfc.cms.business.MinimumWageRateLoader";
  }
}
