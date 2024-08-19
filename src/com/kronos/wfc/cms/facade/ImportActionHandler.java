package com.kronos.wfc.cms.facade;

import com.kronos.wfc.cms.business.CMSService;
import com.kronos.wfc.cms.business.PrincipalEmployee;
import com.kronos.wfc.commonapp.people.business.jobassignment.PrimaryLaborAccount;
import com.kronos.wfc.commonapp.people.business.personality.Personality;
import com.kronos.wfc.commonapp.people.business.user.CurrentUserAccountManager;
import com.kronos.wfc.platform.businessobject.framework.BusinessValidationException;
import com.kronos.wfc.platform.exceptions.framework.GenericException;
import com.kronos.wfc.platform.i18n.framework.messages.Message;
import com.kronos.wfc.platform.persistence.framework.ObjectIdLong;
import com.kronos.wfc.platform.properties.framework.KronosProperties;
import com.kronos.wfc.platform.struts.framework.DynamicMapBackedForm;
import com.kronos.wfc.platform.struts.framework.StrutsUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jcifs.smb.SmbFileOutputStream;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

public class ImportActionHandler extends com.kronos.wfc.platform.struts.framework.WFPLookupDispatchActions
{
  private static final String SAVE_DIR = "uploadFiles";
  private static final String DATA = "data";
  private static final String LANGUAGE_LIST = "languageList";
  private static final String COUNTRY_LIST = "countryList";
  private static final String ENTITY_TYPE_LIST = "entityTypeList";
  private static final String LANGUAGE_CODE = "languageCode";
  private static final String COUNTRY_CODE = "countryCode";
  public static final String SELECTED_ENTITY_TYPE_IDS = "comkronosui_selected_ids";
  
  public ImportActionHandler()
  {
    Map keyMethodMap = super.getKeyMethodMap();
    keyMethodMap.put("cms.action.refresh", "doRefresh");
    keyMethodMap.put("cms.actions.import", "doImport");
  }
  
  private List getPrincipalEmployerList(Personality personality)
  {
    PrincipalEmployeeFacade peFacade = new PrincipalEmployeeFacadeImpl();
    List pes = peFacade.getPrincipalEmployees();
    if (personality.getPersonId().longValue() > 0L)
    {
      PrimaryLaborAccount la = CurrentUserAccountManager.getPersonality().getJobAssignment().getCurrentPrimaryLaborAccount();
      String[] laNames = la.getLaborAccount().getLaborLevelEntryNames_optimized();
      List<PrincipalEmployee> filteredList = new ArrayList<PrincipalEmployee>();
      for (Iterator<PrincipalEmployee> iterator = pes.iterator(); iterator.hasNext();) 
		{
			PrincipalEmployee pe = (PrincipalEmployee) iterator.next();
			for (int i = 0; i < laNames.length; i++) 
			{
				String laName = laNames[i];
				if(laName.equalsIgnoreCase(pe.getUnitCode()))
					filteredList.add(pe);
			}
		}
      






      return filteredList;
    }
    

    return pes;
  }
  

  public ActionForward doRefresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    Personality personality = CurrentUserAccountManager.getPersonality();
    long Personid;
    //CMSService cmss;
   // List InterList;
   // List pes; 
    //CMSException ex;
    try {
    	String unitId = (String)data.getValue("unitId");
      request.setAttribute("unitId", unitId);
    }
    catch (CMSException ex)
    {
      StrutsUtils.addErrorMessage(request, ex); } catch (Exception e) { /*long Personid;*/
      CMSService cmss;
      List InterList;
      List pes;
      //CMSException ex; 
      CMSException ex = CMSException.unknown(e.getLocalizedMessage());
      StrutsUtils.addErrorMessage(request, ex); } 
    	finally { 
    	//long Personid;
     /* CMSService cmss;
      List InterList;
      List pes;*/
      CMSException ex;
      Personid = personality.getPersonId().longValue();
      CMSService cmss = new CMSService();
      List InterList = cmss.getAllInterfacesList(Personid);
      try
      {
        List pes = getPrincipalEmployerList(personality);
        data.setValue("availUnitNames", pes);
      }
      catch (Exception e)
      {
        data.setValue("availUnitNames", "");
         ex = CMSException.unknown(e.getLocalizedMessage());
        StrutsUtils.addErrorMessage(request, ex);
      }
      request.setAttribute("availIntrName", InterList);
    }
    
    return mapping.findForward("success");
  }
  

  public ActionForward doImportTabRefresh(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm textlabelImportInputForm = (DynamicMapBackedForm)form;
    return mapping.findForward("success");
  }
  
  private void setResponseContentDisposition(String translationFileName, HttpServletResponse response)
  {
    response.reset();
    response.setContentType("application/vnd.ms-excel");
    response.setHeader("Content-Disposition", "attachment; filename=\"" + translationFileName + ".xls" + "\"");
  }
  
  public ActionForward doImport(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    try
    {
      importForm(mapping, form, request, response);
      Message message = new Message("wcb.textlabel.config.message.import.success");
      StrutsUtils.addMessage(request, message);
    }
    catch (BusinessValidationException e)
    {
      Exception[] wrappedEx = e.getWrappedExceptions();
      for (int i = 0; i < wrappedEx.length; i++)
      {
        GenericException ge = (GenericException)wrappedEx[i];
        StrutsUtils.addErrorMessage(request, ge);
      }
    }
    catch (GenericException e)
    {
      StrutsUtils.addErrorMessage(request, e);

    }
    catch (Exception e)
    {
      Message message = new Message(e.toString());
      if ("java.lang.Exception: Invalid File Format".equals(e.toString()))
      {
        message = new Message("cms.wrong.format");
      }
      if ("java.lang.Exception: Select Dropdowns".equals(e.toString()))
      {
        message = new Message("cms.wrong.dropdown");
      }
      if ("java.lang.Exception: Running Interface Failed".equals(e.toString()))
      {
        message = new Message("cms.wrong.RunIntrFail");
      }
      StrutsUtils.addErrorMessage(request, message);
    }
    doRefresh(mapping, form, request, response);
    return mapping.findForward("success");
  }
  
  private void importForm(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
    throws Exception
  {
    DynamicMapBackedForm data = (DynamicMapBackedForm)form;
    String unitId = (String)data.getValue("unitId");
    String InterfaceID = (String)data.getValue("InterfaceName");
    if ((!"-1".equals(unitId)) && (!"-1".equals(InterfaceID)))
    {
      try
      {
        String SelectedUnitname = null;
        PrincipalEmployee pe = PrincipalEmployee.doRetrieveById(new ObjectIdLong(unitId));
        if (pe != null)
        {
          SelectedUnitname = pe.getUnitName();
        }
        ImportActionForm textLabelConfigActionForm = (ImportActionForm)form;
        FormFile importFile = textLabelConfigActionForm.getFormFile();
        byte[] bdata = importFile.getFileData();
        int filesize = importFile.getFileSize();
        double bytes = filesize;
        double kb = bytes / 1024.0D;
        double mb = kb / 1024.0D;
        
        String appPath = System.getProperty("user.dir");
        String savePath = appPath + File.separator + "uploadFiles";
        String fileNamePre = new java.text.SimpleDateFormat("yyyyMMddhhmmss").format(new java.util.Date());
        String xlfileName = fileNamePre + ".xls";
        String csvfileName = null;
        if ("1".equals(InterfaceID))
        {

          csvfileName = "Schedule Pattern Upload with Transfer_" + SelectedUnitname + "_" + fileNamePre + ".csv";
        }
        
        File fileSaveDir = new File(savePath);
        if (!fileSaveDir.exists())
        {
          fileSaveDir.mkdir();
        }
        if ("xls".equals(importFile.toString().substring(importFile.toString().lastIndexOf(".") + 1).trim()))
        {
          if (mb > 5.0D)
          {
            Exception e = new Exception("File size excceds limit. Max file size is 5 mb");
            throw e;
          }
          

          org.apache.commons.io.FileUtils.writeByteArrayToFile(new File(savePath + File.separator + xlfileName), bdata);
          File csvf = new File(savePath + File.separator + csvfileName);
          csvf.getParentFile().mkdirs();
          csvf.createNewFile();

        }
        else
        {
          Exception exc = new Exception("Invalid File Format");
          throw exc;
        }
        

        String FilePath = savePath + File.separator + xlfileName;
        FileInputStream fs = new FileInputStream(FilePath);
        try
        {
          Workbook wb = Workbook.getWorkbook(fs);
          Sheet sh = wb.getSheet(0);
          int totalNoOfRows = sh.getRows();
          int totalNoOfCols = sh.getColumns();
          
          String COMMA_DELIMITER = ",";
          String NEW_LINE_SEPARATOR = "\n";
          FileWriter fileWriter = null;
          try
          {
            fileWriter = new FileWriter(savePath + File.separator + csvfileName);
            for (int rowh = 0; rowh == 0; rowh++)
            {
              for (int colh = 0; colh < totalNoOfCols; colh++)
              {
                fileWriter.append(sh.getCell(colh, rowh).getContents());
                fileWriter.append(COMMA_DELIMITER);
              }
              fileWriter.append(NEW_LINE_SEPARATOR);
            }
            for (int row = 1; row < totalNoOfRows; row++)
            {
              for (int col = 0; col < totalNoOfCols; col++)
              {
                fileWriter.append(sh.getCell(col, row).getContents());
                fileWriter.append(COMMA_DELIMITER);
              }
              fileWriter.append(NEW_LINE_SEPARATOR);
            }
          }
          catch (Exception e)
          {
            e.printStackTrace();
          }
          finally
          {
            fileWriter.flush();
            fileWriter.close();
          }
          






        
        }
        catch (BiffException e)
        {
          e.printStackTrace();
        }
        
        String  sourcePath = savePath + File.separator + csvfileName;
        
        String destinationPath = "smb://" + KronosProperties.getProperty("cms.upload.server.name") + "/" + KronosProperties.getProperty("cms.upload.shared.folder.name") + "/" + csvfileName;
        copyFileUsingJcifs(KronosProperties.getProperty("cms.upload.server.name"), KronosProperties.getProperty("cms.upload.username"), KronosProperties.getProperty("cms.upload.password"), sourcePath, destinationPath);
        

        String xml_send = "";
        String xml_recv = "";
        URL url = new URL(KronosProperties.get("cms.protocol", "localhost") + "://" + KronosProperties.get("cms.servername", "localhost") + "/wfc/XmlService");
        xml_send = "<?xml version = \"1.0\"?><kronos_wfc version=\"1.0\"><Request object=\"SYSTEM\" action=\"LOGON\" username=\"SuperUser\" password=\"" + 
        
          KronosProperties.get("cms.password", "Kronos123") + "\"" + "/>" + 
          "<Request Action=\"Execute\">" + 
          "<WimInterface Name=\"Schedule Pattern Upload with Transfer\">" + 
          "<AnsweredPrompts>" + 
          "<WimInputPrompt>" + 
          "<PromptNoValidation>" + 
          "<NoValidation>" + 
          "<ValidationString>NA</ValidationString> " + 
          "</NoValidation>" + 
          "</PromptNoValidation>" + 
          "<AssocLinkName>Schedule Pattern Import Staging.KNX</AssocLinkName>" + 
          "<AssocStepNumber>1</AssocStepNumber>" + 
          "<Request>Please Enter FileName</Request>" + 
          "<Response>" + csvfileName + "</Response>" + 
          "<VariableName>:FileName</VariableName>" + 
          "</WimInputPrompt>" + 
          "</AnsweredPrompts>" + 
          "</WimInterface>" + 
          "</Request>" + 
          "<Request object=\"SYSTEM\" action=\"LOGOFF\" />" + 
          "</kronos_wfc>";
        
        com.kronos.wfc.platform.logging.framework.Log.log(4, "URL: " + url + "XML: " + xml_send);
        
        xml_recv = sendRequest(url, xml_send);
        if (xml_recv.contains("<Response Status=\"Success\" Action=\"Execute\">"))
        {
          String JobId = org.apache.commons.lang.StringUtils.substringBetween(xml_recv, "JobId=\"", "\"");
          System.out.println(JobId);
          return;
        }
        

        Exception exc = new Exception("Running Interface Failed");
        throw exc;

      }
      catch (Exception exception)
      {
        throw exception;
      }
    }
    else
    {
      Exception e = new Exception("Select Dropdowns");
      throw e;
    }
  }
  
  private String sendRequest(URL url, String xml)
  {
    String recv = "";
    
    try
    {
      HttpURLConnection conn = (HttpURLConnection)
        url.openConnection();
      
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-type", "text/xml");
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setUseCaches(false);
      conn.setAllowUserInteraction(true);
      conn.connect();
      
      PrintWriter out = new PrintWriter(
        new java.io.OutputStreamWriter(conn
        .getOutputStream()));
      out.println(xml);
      out.flush();
      out.close();
      InputStream is = conn.getInputStream();
      recv = readFromStream(is, conn);
      conn.disconnect();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return recv;
  }
  
  private String readFromStream(InputStream in, HttpURLConnection conn) throws IOException
  {
    String sRet = "";
    

    try
    {
      byte[] b = new byte[1];
      int theInt; while ((theInt = in.read()) >= 0) {
       // int theInt;
        b[0] = ((byte)theInt);
        sRet = sRet + new String(b);
      }
    }
    catch (IOException e)
    {
      try
      {
        System.out.println(" HTTP Response message is: " + conn.getResponseMessage());
      }
      catch (IOException ex)
      {
        ex.printStackTrace();
      }
      e.printStackTrace();
    }
    if (sRet.length() <= 0)
      System.out.println("\n\nZERO Data Returned !!! \n");
    return sRet;
  }
  
  public static void copyFileUsingJcifs(String domain, String userName, String password, String sourcePath, String destinationPath)
    throws IOException
  {
    try
    {
      jcifs.smb.NtlmPasswordAuthentication auth = new jcifs.smb.NtlmPasswordAuthentication(domain, userName, password);
      jcifs.smb.SmbFile sFile = new jcifs.smb.SmbFile(destinationPath, auth);
      SmbFileOutputStream smbFileOutputStream = new SmbFileOutputStream(sFile);
      FileInputStream fileInputStream = new FileInputStream(new File(sourcePath));
      
      byte[] buf = new byte[16777216];
      int len;
      while ((len = fileInputStream.read(buf)) > 0) {
       // int len;
        smbFileOutputStream.write(buf, 0, len);
      }
      fileInputStream.close();
      smbFileOutputStream.close();
      System.out.println("File Copied Succesfully");
    }
    catch (IOException e)
    {
      System.out.println("Copying file was failed.");
      throw e;
    }
  }
  







  private static String EXPORT_TAB = "wcb.textlabel.config.export.tab";
  private static String IMPORT_TAB = "wcb.textlabel.config.import.tab";
  private static String SELECTED_TAB_KEY = "selected-tab";
  private static String FIND_FORWARD_EXPORT = "wcb.translationsupport.export-view";
  private static String FIND_FORWARD_IMPORT = "wcb.translationsupport.import-view";
}
