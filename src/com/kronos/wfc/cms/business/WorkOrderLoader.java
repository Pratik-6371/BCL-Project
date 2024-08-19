package com.kronos.wfc.cms.business;

import com.kronos.wfc.commonapp.genies.business.ColumnData;
import com.kronos.wfc.commonapp.genies.business.ICommonData;
import com.kronos.wfc.commonapp.genies.business.IMultipleLinesColumn;
import com.kronos.wfc.commonapp.genies.business.OptimizedWorkedAccountLoader;
import com.kronos.wfc.commonapp.genies.business.SummaryViewDetail;
import com.kronos.wfc.commonapp.genies.business.SummaryViewUtil;
import com.kronos.wfc.commonapp.genies.business.TableDataHandler;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.persistence.framework.statement.DataStore;
import java.util.ArrayList;








public class WorkOrderLoader
  extends OptimizedWorkedAccountLoader
  implements IMultipleLinesColumn
{
  private int colNum;
  private ArrayList dataList;
  private ArrayList idPairList;
  private ArrayList list;
  
  public WorkOrderLoader(TableDataHandler tbHandler, SummaryViewDetail svDetail, ArrayList ccDetail, int vType, int col, ICommonData data)
  {
    super(tbHandler, svDetail, ccDetail, vType, col, data);
    colNum = -1;
    dataList = new ArrayList();
    idPairList = new ArrayList();
    list = null;
    colNum = col;
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
    rowData[2] = accts[5];
    
    rowData[3] = Boolean.FALSE;
    Object[] idPair = new Object[2];
    idPair[0] = personId;
    idPair[1] = laborAcctId;
    idPairList.add(idPair);
    list.add(rowData);
  }
  
  public ArrayList getList() {
    return list;
  }
}
