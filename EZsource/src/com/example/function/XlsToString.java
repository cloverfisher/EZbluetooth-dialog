package com.example.function;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.source.AphaseItemTemplate;
import com.example.source.CustomerMasterfile;
import com.example.source.MyException;
import com.example.source.ReturnableItem;
import com.example.source.UserMaster;
import com.google.android.gms.internal.ex;

import jxl.*;
import jxl.read.biff.BiffException;
import android.os.Environment;
import android.util.Log;


//transfer excel file to string
public class XlsToString {
	
	public File getFilePath(String str) throws MyException
	{
		
		  String path = Environment.getExternalStorageDirectory().getPath();
		//  path = path + "/Download";
		  File file = new File(path+"/Download");
		  Log.e("ysy", "Download" + file.exists());
		  if(!file.exists())
		  {
			  path = path+"/download";
		  }
		  else
		  {
			  path = path+"/Download";
		  }
		  File file1 = new File(path + str + ".xls");
		  File file2 = new File(path + str + ".xlsx");
		  Log.e("ysy", file1.getPath());
		  if(file1.exists()&&file2.exists())
		  {
			  Log.e("ysy", "1");
			  throw new MyException("There are both xls file and xlsx file please delete one");
		  }
		  else if(!(file1.exists()||file2.exists()))
		  {
			  Log.e("ysy", "2");
			  throw new MyException("the" + str +"file is not exist");
		  }
		  else if(file1.exists())
		  {
			  Log.e("ysy", "3");
			  return file1;
		  }
		  else if(file2.exists())
		  {
			  Log.e("ysy", "4");
			  return file2;
		  }
		  return null;
		  
	}
	
	
	public List catchUsermaster() throws BiffException, IOException,MyException
	{
		


		  List<UserMaster> list = new ArrayList<UserMaster>();
		  File file = getFilePath("/UserMaster");
			  Workbook rwb  = Workbook.getWorkbook(file);
			  Sheet rs = rwb.getSheet(0);
	//		  Cell c00 = rs.getCell(0, 0);
			  int columnsnum = rs.getColumns();
			  int rowsnum = rs.getRows();
			  Log.e("ysy", "user row " + rowsnum);
			  for(int i=1; i<rowsnum ; i++)
			  {		
			//	  Cell cc = rs.getCell(i,0);
				  Cell[] cc = rs.getRow(i);
				  UserMaster um = new UserMaster(cc[0].getContents(),
						  cc[1].getContents(),
						  cc[2].getContents(), 
						  cc[3].getContents(), 
						  cc[4].getContents(), 
						  cc[5].getContents()) ;
				  list.add(um);
			  }

		  return list;
	}
	
	
	public List catchCustomermaster() throws BiffException, IOException, MyException
	{
		  
		 File file = getFilePath("/CustomerMasterfile");
	//	  File file = new File(path + "/CustomerMasterfile.xls");
		  List<CustomerMasterfile> list = new ArrayList<CustomerMasterfile>();
			  Workbook  rwb = Workbook.getWorkbook(file);
			  Sheet rs = rwb.getSheet(0);
	//		  Cell c00 = rs.getCell(0, 0);
			  int columnsnum = rs.getColumns();
			  int rowsnum = rs.getRows();
			  Log.e("ysy", "customermaster " + rowsnum);
			  for(int i=1; i<rowsnum ; i++)
			  {				
			//	  Cell cc = rs.getCell(i,0);
				  Cell[] cc = rs.getRow(i);
				  CustomerMasterfile cm = new CustomerMasterfile(cc);

				  list.add(cm);
			  }
	  
		  return list;
	}
	
	public List catchAphaseItemTemplate() throws BiffException, IOException,MyException
	{
		 File file = getFilePath("/AphaseItemTemplate");
		List<AphaseItemTemplate> list = new ArrayList<AphaseItemTemplate>();
			Workbook rwb = Workbook.getWorkbook(file);
			Sheet rs = rwb.getSheet(0);
			int rowsnum = rs.getRows();
			Log.e("ysy", "itemrow " + rowsnum);
			for(int i = 1; i < rowsnum; i++)
			{
				Cell[] cc = rs.getRow(i);
				AphaseItemTemplate ait = new AphaseItemTemplate(cc);
				list.add(ait);
			}

		return list;
	}
	
	public List catchReturnableItem()throws BiffException, IOException,MyException
	{
		 File file = getFilePath("/ReturnableItems");
		Workbook rwb;
		List<ReturnableItem> list = new ArrayList<ReturnableItem>();
			rwb = Workbook.getWorkbook(file);
			Sheet rs = rwb.getSheet(0);
			int rowsnum = rs.getRows();
			rs.getColumns();
			Log.e("ysy", "returnablerow " + rs.getColumns());	
			for(int i=1; i<rowsnum; i++)
			{
				Cell[] cc = rs.getRow(i);
				ReturnableItem rItem = new ReturnableItem(cc);
				list.add(rItem);
			}
		return list;
		
	}
	
	
}
