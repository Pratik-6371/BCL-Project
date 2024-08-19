package com.kronos.wfc.cms.business;

import com.kronos.wfc.commonapp.genies.business.ColumnConfigDetail;
import com.kronos.wfc.commonapp.genies.business.ColumnData;
import com.kronos.wfc.commonapp.genies.business.ColumnDataLoader;
import com.kronos.wfc.commonapp.genies.business.ICommonData;
import com.kronos.wfc.commonapp.genies.business.IPluginAppCommonData;
import com.kronos.wfc.commonapp.genies.business.SummaryViewCommonData;
import com.kronos.wfc.commonapp.genies.business.SummaryViewDetail;
import com.kronos.wfc.commonapp.genies.business.SummaryViewPackageManager;
import com.kronos.wfc.commonapp.genies.business.TableDataHandler;
import com.kronos.wfc.commonapp.hyperfind.business.HyperFindPreparedStmtMarshal;
import com.kronos.wfc.platform.logging.framework.Log;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.business.SQLStatementWithInClause;
import com.kronos.wfc.platform.persistence.framework.statement.DataStore;
import com.kronos.wfc.platform.properties.framework.KronosProperties;
import com.kronos.wfc.platform.utility.framework.datetime.KDate;
import com.kronos.wfc.platform.utility.framework.datetime.KDateSpan;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class WorkmenPFWageLoader extends ColumnDataLoader implements IPluginAppCommonData
{
  private ArrayList personIdList;
  protected static int PERSON_ID = 1;
  protected static int LABOR_ACCOUNT_ID = 2;
  protected static int PAYCODE_ID = 3;
  protected static int MONEY_AMOUNT = 4;
  protected static int SECONDS_AMOUNT = 5;
  protected static int WAGE_AMOUNT = 6;
  protected static int DAY_AMOUNT = 7;
  private static int InClauseMaxCount = Integer.parseInt(KronosProperties.getProperty("business.persistence.InClauseMaxCount"));
  ArrayList list;
  ObjectIdLong regularPayCodeId;
  ObjectIdLong otPayCodeId;
  ObjectIdLong holidayPayCodeId;
  private ArrayList idPairList;
  
  public WorkmenPFWageLoader(TableDataHandler tbHandler, SummaryViewDetail svDetail, ArrayList ccDetail, int vType, int col, ICommonData data) {
    super(tbHandler, svDetail, ccDetail, vType, data);
    personIdList = new ArrayList();
    list = new ArrayList();
    
    int regularPaycolNum = Integer.parseInt(KronosProperties.get("cms.business.regularColNum", "6"));
    int otPaycolNum = Integer.parseInt(KronosProperties.get("cms.business.otColNum", "7"));
    int holidayColNum = Integer.parseInt(KronosProperties.get("cms.business.holidayColNum", "8"));
    ColumnConfigDetail ccd = (ColumnConfigDetail)columnConfigDetail.get(regularPaycolNum);
    regularPayCodeId = ccd.getPayCodeId();
    ColumnConfigDetail ccd1 = (ColumnConfigDetail)columnConfigDetail.get(otPaycolNum);
    otPayCodeId = ccd1.getPayCodeId();
    ColumnConfigDetail ccd2 = (ColumnConfigDetail)columnConfigDetail.get(holidayColNum);
    holidayPayCodeId = ccd2.getPayCodeId();
    idPairList = new ArrayList();
  }
  
  protected ColumnData getData() {
    ArrayList payCodeTotalLoaderList = m_data.getPayCodeTotalLoader();
    ArrayList paycodeList = m_data.getActualPaycodeList();
    ArrayList sqlSubstitutionArrayList = new ArrayList();
    mcMarshal = m_data.getHFPreparedStmtMarshal();
    if (handler.getPersonIdListCondition() != null) {
      sqlSubstitutionArrayList.add(handler.getPersonIdListCondition());
    } else
      sqlSubstitutionArrayList.add(m_data.getHfQueryParametersFromHFPreparedStmtMarshal());
    sqlSubstitutionArrayList.add(paycodeList);
    sqlSubstitutionArrayList.add(handler.getLaborAccountCondition());
    if (Log.priorityEnabled(SummaryViewPackageManager.getSummaryViewPackageLogContext(), 4))
      Log.log(SummaryViewPackageManager.getSummaryViewPackageLogContext(), 4, "mcMarshal.getHfQuery() is: " + mcMarshal.getHfQuery());
    ArrayList parameterList = new ArrayList();
    if (Log.priorityEnabled(SummaryViewPackageManager.getSummaryViewPackageLogContext(), 4))
      Log.log(SummaryViewPackageManager.getSummaryViewPackageLogContext(), 4, "mcMarshal.getHfQueryParameters() is: " + mcMarshal.getHfQueryParameters());
    SQLStatementWithInClause stmt = null;
    ObjectIdLong timeFrameId = m_data.getTimeFrameId();
    if (timeFrameId.toLong().intValue() == 0) {
      stmt = new SQLStatementWithInClause("business.summaryview.SummaryViewSqlStringConstant.GET_OPTIMIZED_WORKED_ACCOUNT_PAYCODE_TOTAL_PREVIOUS_PAY_PERIOD", sqlSubstitutionArrayList);
    }
    else if (timeFrameId.toLong().intValue() == 1) {
      stmt = new SQLStatementWithInClause("business.summaryview.SummaryViewSqlStringConstant.GET_OPTIMIZED_WORKED_ACCOUNT_PAYCODE_TOTAL_CURRENT_PAY_PERIOD", sqlSubstitutionArrayList);
    }
    else if (timeFrameId.toLong().intValue() == 2)
    {
      stmt = new SQLStatementWithInClause("business.summaryview.SummaryViewSqlStringConstant.GET_OPTIMIZED_WORKED_ACCOUNT_PAYCODE_TOTAL_NEXT_PAY_PERIOD", sqlSubstitutionArrayList);
    }
    else {
      KDateSpan dateRange = m_data.getDateRange();
      parameterList.add(dateRange.getBegin().getSQLTimestamp());
      parameterList.add(dateRange.getEnd().getSQLTimestamp());
      stmt = new SQLStatementWithInClause("business.summaryview.SummaryViewSqlStringConstant.GET_OPTIMIZED_WORKED_ACCOUNT_PAYCODE_TOTAL_DATE_RANGE", sqlSubstitutionArrayList);
    }
    stmt.execute(parameterList);
    Map<ObjectIdLong, WageRow> personIdMap = new HashMap();
    
    for (DataStore ds = stmt.getDataStore(); ds.nextRow();)
    {
      boolean found = false;
      Integer durationQty = null;
      ObjectIdLong pcId = ds.getObjectIdLong(PAYCODE_ID);
      

      WageRow row = (WageRow)personIdMap.get(ds.getObjectIdLong(1));
      if (row == null) {
        row = new WageRow(ds.getObjectIdLong(1), ds.getObjectIdLong(LABOR_ACCOUNT_ID), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Boolean.FALSE);
      }
      
      if (pcId.longValue() == regularPayCodeId.longValue()) {
        durationQty = ds.getInt(SECONDS_AMOUNT);
        int hrs = durationQty.intValue() / 3600;
        int mins = durationQty.intValue() % 3600 / 60;
        int amountInMinutes = hrs * 60 + mins;
        int regularHours = row.getRegularHours().intValue() + amountInMinutes;
        row.setRegularHours(Integer.valueOf(regularHours));

      }
      else if (pcId.longValue() == otPayCodeId.longValue()) {
        durationQty = ds.getInt(SECONDS_AMOUNT);
        int hrs = durationQty.intValue() / 3600;
        int mins = durationQty.intValue() % 3600 / 60;
        int amountInMinutes = hrs * 60 + mins;
        int otHours = row.getOtHours().intValue() + amountInMinutes;
        row.setOtHours(Integer.valueOf(otHours));

      }
      else if (pcId.longValue() == holidayPayCodeId.longValue()) {
        durationQty = ds.getInt(SECONDS_AMOUNT);
        int hrs = durationQty.intValue() / 3600;
        int mins = durationQty.intValue() % 3600 / 60;
        int amountInMinutes = hrs * 60 + mins;
        int hours = row.getHolidayHours() + amountInMinutes;
        row.setHolidayHours(hours);
      }
      

      personIdMap.put(ds.getObjectIdLong(1), row);
    }
    
    Map idRate = new HashMap();
    WorkmenWageRateLoader data = (WorkmenWageRateLoader)m_data.getPluginAppCommonData("com.kronos.wfc.cms.business.WorkmenWageLoader");
    if (data != null) {
      idRate = data.getRateMap();
    }
    for (Iterator iterator = personIdMap.entrySet().iterator(); iterator.hasNext();) {
      Map.Entry<ObjectIdLong, WageRow> entry = (Map.Entry)iterator.next();
      WageRow row = (WageRow)entry.getValue();
      
      Object[] rowData = new Object[4];
      rowData[0] = entry.getKey();
      rowData[1] = row.getLaborAcctId();
      Object[] idPair = new Object[2];
      idPair[0] = entry.getKey();
      idPair[1] = row.getLaborAcctId();
      idPairList.add(idPair);
      Double rate = (Double)idRate.get(entry.getKey());
      if (rate != null) {
        DecimalFormat df = new DecimalFormat("###.##");
        Double pf = Double.valueOf(Double.parseDouble(KronosProperties.get("cms.pf.value", "0.12")));
        Double amount = Double.valueOf(rate.doubleValue() * (row.getRegularHours().intValue() / 480.0D) + 
          row.getOtHours().intValue() * 2 / 480.0D + 
          row.getHolidayHours() * 3 / 480.0D);
        Double pfCeiling = Double.valueOf(Double.parseDouble(KronosProperties.get("cms.pfceiling.value", "6500")));
        if (amount.doubleValue() > pfCeiling.doubleValue()) {
          rowData[2] = df.format(pf.doubleValue() * pfCeiling.doubleValue());
        }
        else {
          rowData[2] = df.format(rate.doubleValue() * pf.doubleValue() * (row.getRegularHours().intValue() / 480.0D + 
            row.getOtHours().intValue() * 2 / 480.0D + 
            row.getHolidayHours() * 2 / 480.0D));
        }
      }
      else {
        rowData[2] = "0";
      }
      rowData[3] = row.getType();
      list.add(rowData);
    }
    handler.setIdPairList(idPairList);
    filterList(personIdList, list, null);
    return new ColumnData(personIdList, 1);
  }
  

  public String getFullyQualifiedName()
  {
    return "com.kronos.wfc.cms.business.WorkmenWageLoader";
  }
}
