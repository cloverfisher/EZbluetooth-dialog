package com.example.function;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.source.AphaseItemTemplate;
import com.example.source.CustomerMasterfile;
import com.example.source.ReturnableItem;
import com.example.source.UserMaster;

import jxl.*;

import jxl.read.biff.BiffException;
import android.os.Environment;
import android.util.Log;

public class XlsToString {
	
	
	public List catchUsermaster() throws BiffException, IOException
	{
		
		
		  String path = Environment.getExternalStorageDirectory().getPath();
		  path = path + "/Android/data/com.dropbox.android/files/scratch";
		  File file = new File(path + "/UserMaster.xls");
		  
//		  String path = Environment.getExternalStorageDirectory().getPath();
//		  File file = new File(path + "/Ezsource/UserMaster");
//		  String filename = file.listFiles()[0].getPath();
//	//	  file = new File(filename);
//		  file = new File("/mnt/sdcard/Ezsource/UserMaster/UserMaster.xls");
	//	  filename = path + "/Ezsource/UserMaster/" + "UserMaster.xls";
	//	  Workbook.getWorkbook(file);
	//	  InputStream is = new FileInputStream(filename);
		  List<UserMaster> list = new ArrayList<UserMaster>();
		
		  try{
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
		  }
		  catch(Exception e)
		  {
			  e.printStackTrace();
				Log.e("ysy ", e.toString());		  
		  }
		  return list;
	}
	
	
	public List catchCustomermaster() throws BiffException, IOException
	{
		  
		  String path = Environment.getExternalStorageDirectory().getPath();
		  path = path + "/Android/data/com.dropbox.android/files/scratch";
		  File file = new File(path + "/CustomerMasterfile.xls");
	//	  File file = new File(path + "/Ezsource/UserMaster");
//		  String filename = file.listFiles()[0].getPath();
	//	  file = new File(filename);
	//	  file = new File("/mnt/sdcard/Ezsource/CustomerMaster/CustomerMasterfile.xls");
	//	  filename = path + "/Ezsource/UserMaster/" + "UserMaster.xls";

	//	  InputStream is = new FileInputStream(filename);
	//	  Workbook rwb;
		  List<CustomerMasterfile> list = new ArrayList<CustomerMasterfile>();
		  try{
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
//				  CustomerMasterfile cm= new CustomerMasterfile(cc[0].getContents(), 
//						  cc[1].getContents(), 
//						  cc[2].getContents(), 
//						  cc[3].getContents(), 
//						  cc[4].getContents(), 
//						  cc[5].getContents(), 
//						  cc[6].getContents(), 
//						  cc[7].getContents(), 
//						  cc[8].getContents(), 
//						  cc[9].getContents(), 
//						  cc[10].getContents(), 
//						  cc[11].getContents(), 
//						  cc[12].getContents(), 
//						  cc[13].getContents(), 
//						  cc[14].getContents(), 
//						  cc[15].getContents());

				  list.add(cm);
			  }
		  }
		  catch(Exception e)
		  {
				Log.e("ysy ", e.toString());		  
		  }
		  return list;
	}
	
	public List catchAphaseItemTemplate()
	{
//		String path = Environment.getExternalStorageDirectory().getPath();
//		File file = new File(path + "/Ezsource/AphaseItemTemplate");
//		String filename = file.listFiles()[0].getPath();
//		file = new File(path + "/Ezsource/AphaseItemTemplate/AphaseItemTemplate.xls");

		  String path = Environment.getExternalStorageDirectory().getPath();
		  path = path + "/Android/data/com.dropbox.android/files/scratch";
		  File file = new File(path + "/AphaseItemTemplate.xls");
		
		List<AphaseItemTemplate> list = new ArrayList<AphaseItemTemplate>();
		try {
			Workbook rwb = Workbook.getWorkbook(file);
			Sheet rs = rwb.getSheet(0);
			int rowsnum = rs.getRows();
			Log.e("ysy", "itemrow " + rowsnum);
			for(int i = 1; i < rowsnum; i++)
			{
				Cell[] cc = rs.getRow(i);
				AphaseItemTemplate ait = new AphaseItemTemplate(cc);
//				AphaseItemTemplate ait = new AphaseItemTemplate(
//						cc[0].getContents(),
//						cc[1].getContents(),
//						cc[2].getContents(),
//						cc[3].getContents(),
//						cc[4].getContents(),
//						cc[5].getContents(),
//						cc[6].getContents(),
//						cc[7].getContents(),
//						cc[8].getContents()
//						);
				list.add(ait);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return list;
	}
	
	public List catchReturnableItem()
	{
		
		  String path = Environment.getExternalStorageDirectory().getPath();
		  path = path + "/Android/data/com.dropbox.android/files/scratch";
		  File file = new File(path + "/ReturnableItems.xls");
//		String path = Environment.getExternalStorageDirectory().getPath();
//		File file = new File(path + "/Ezsource/ReturnableItems/ReturnableItems.xls");
		Workbook rwb;
		List<ReturnableItem> list = new ArrayList<ReturnableItem>();
		try{
			rwb = Workbook.getWorkbook(file);
			Sheet rs = rwb.getSheet(0);
			int rowsnum = rs.getRows();
			rs.getColumns();
			Log.e("ysy", "returnablerow " + rs.getColumns());	
			for(int i=1; i<rowsnum; i++)
			{
				Cell[] cc = rs.getRow(i);
				ReturnableItem rItem = new ReturnableItem(cc);
//				ReturnableItem rItem = new ReturnableItem(cc[0].getContents(),
//						cc[1].getContents(),
//						cc[2].getContents(),
//						cc[3].getContents(),
//						cc[4].getContents(),
//						cc[5].getContents(),
//						cc[6].getContents(),
//						cc[7].getContents(),
//						cc[8].getContents(),
//						cc[9].getContents(),
//						cc[10].getContents(),
//						cc[11].getContents(),
//						cc[12].getContents());
				list.add(rItem);
			}
		}
		catch(Exception e)
		{
			Log.e("ysy ", e.toString());		
		}
		return list;
		
	}
	
	
}
