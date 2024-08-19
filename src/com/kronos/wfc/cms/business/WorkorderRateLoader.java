package com.kronos.wfc.cms.business;

import com.kronos.wfc.commonapp.genies.business.ColumnData;
import com.kronos.wfc.commonapp.genies.business.ICommonData;
import com.kronos.wfc.commonapp.genies.business.OptimizedWorkedAccountLoader;
import com.kronos.wfc.commonapp.genies.business.SummaryViewDetail;
import com.kronos.wfc.commonapp.genies.business.SummaryViewUtil;
import com.kronos.wfc.commonapp.genies.business.TableDataHandler;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.SQLStatement;
import com.kronos.wfc.platform.persistence.framework.statement.DataStore;
import com.kronos.wfc.platform.persistence.framework.statement.DataStoreIfc;
import java.util.ArrayList;
import java.util.Map;

public class WorkorderRateLoader extends OptimizedWorkedAccountLoader implements com.kronos.wfc.commonapp.genies.business.IMultipleLinesColumn
{
  private int colNum;
  private ArrayList dataList;
  private ArrayList idPairList;
  private ArrayList list;
  private Map<ObjectIdLong, Double> idRateMap;
  
  public WorkorderRateLoader(TableDataHandler tbHandler, SummaryViewDetail svDetail, ArrayList ccDetail, int vType, int col, ICommonData data)
  {
    super(tbHandler, svDetail, ccDetail, vType, col, data);
    colNum = -1;
    dataList = new ArrayList();
    idPairList = new ArrayList();
    list = null;
    colNum = col;
    idRateMap = new java.util.HashMap();
  }
  
  protected ColumnData getData()
  {
    handler.setIdPairList(idPairList);
    filterList(dataList, list, null);
    return new ColumnData(dataList, 1);
  }
  
  protected void getDataFromDataStore(DataStore ds, ObjectIdLong personId)
  {
    if (list == null)
      list = new ArrayList();
    Object[] rowData = new Object[4];
    rowData[0] = personId;
    ObjectIdLong laborAcctId = ds.getObjectIdLong(LABOR_ACCOUNT_ID);
    rowData[1] = laborAcctId;
    String accountName = SummaryViewUtil.retrieveLaborAccountName(ds, 3);
    String[] accts = accountName.split("/");
    ArrayList params = new ArrayList();
    params.add(accts[5]);
    params.add(personId.toSQLString());
    SQLStatement select = new SQLStatement(3, "cms.business.SELECT_RATE");
    select.execute(params);
    DataStoreIfc ds1 = select.getDataStore();
    rowData[2] = ds1.getString(1);
    rowData[3] = Boolean.FALSE;
    Object[] idPair = new Object[2];
    idPair[0] = personId;
    idPair[1] = laborAcctId;
    if (ds1.getString(1) != null) {
      idRateMap.put(personId, Double.valueOf(Double.parseDouble(ds1.getString(1))));
    }
    idPairList.add(idPair);
    list.add(rowData);
  }
  
  public Map<ObjectIdLong, Double> getRateMap()
  {
    return idRateMap;
  }
}
