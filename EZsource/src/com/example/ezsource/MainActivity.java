package com.example.ezsource;




import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Semaphore;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.function.XlsToString;
import com.example.source.AphaseItemTemplate;
import com.example.source.CustomerMasterfile;
import com.example.source.NumberDialog;
import com.example.source.Output;
import com.example.source.ReturnableItem;
import com.example.source.UserMaster;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;


public class MainActivity extends Activity {
	BluetoothAdapter mBluetoothAdapter;
	UUID uuid;
	Semaphore semp = new Semaphore(0);
	List<UserMaster> umlist = null;
	List<CustomerMasterfile> cmlist = null;
	List<AphaseItemTemplate> aitlist = null;
	List<ReturnableItem> rilist = null;
	UserMasterDB newDb = new UserMasterDB();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Toast.makeText(this, "please hold the bluetooth device for 10 s and then make a transaction!", Toast.LENGTH_LONG).show();
		creatpath();


		setContentView(R.layout.mainactivity);

//		try {
//			newDb.openDB();
//		//	newDb.buildDB();
//		//	newDb.bla();
//			//newDb.checktableExist();
//			newDb.closeDB();
//			//newDb.
//		} catch (Exception e) {
//			// TODO: handle exception
//			updatedb();
//			newDb.openDB();
//			newDb.newOutputDB();
//			newDb.closeDB();
//	
//		}
//
//
//		newDb.closeDB();
	// Transaction button	
		Button button;
		button = (Button)findViewById(R.id.btnstartnewtran);
		button.setOnClickListener(
			new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, TransactionActivity.class);
					startActivity(intent);
					
//					NumberDialog dialog = new NumberDialog(MainActivity.this);
//					dialog.show();
				}
			}
		);
		// View Transaction history
		Button btnViewHistory;
		
		btnViewHistory = (Button)findViewById(R.id.btnhistory);
		btnViewHistory.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ArrayList<String> list =  OutputHistoryPlus();
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, HistoryActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putStringArrayList("alist", list);
//			//	intent.putExtra("alist", list);
//				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		
	// BlueToothSetting	
		Button btnBlueTooth;
		btnBlueTooth = (Button)findViewById(R.id.btnbluetooth);
		btnBlueTooth.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, TestActivity.class);
				startActivity(intent);
			}
		});
	//no use now	
		Button btnSettingButton;
		btnSettingButton = (Button)findViewById(R.id.btnsetting);
		btnSettingButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, AccountActivity.class);
				startActivity(intent);
				
			}
		});
	//update the database by the inputfile
		Button btnDBUpdate;
		btnDBUpdate = (Button)findViewById(R.id.btndbupdate);
		btnDBUpdate.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updatedb();
			}
		});
		
		Button btnDBsearchButton;
		btnDBsearchButton = (Button)findViewById(R.id.btndbsearch);
		btnDBsearchButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	public class Person
	{
		public String name;
		public int age;
	}
	

	ArrayList<String> OutputHistoryPlus()
	{
		UserMasterDB umdb = new UserMasterDB();
		umdb.openDB();
		ArrayList<String> list = umdb.OutputHistory();
//		for(int i=0;i<list.size();i++)
//		{
//			Log.e("ysy", list.get(i));
//		}
		umdb.closeDB();
		return list;
	}
	
	//just for test
	private class UserMasterDB
	{
		SQLiteDatabase db;
		
//		public boolean exists(String table) {
//		    try {
//		         db.query("SELECT * FROM " + table, null, table, null, table, table, table);
//		         return true;
//		    } catch () {
//		         return false;
//		    }
//		}
		
		public void newOutputDB()
		{
			db.execSQL("DROP TABLE IF EXISTS outputmaster");
			db.execSQL("create table outputmaster (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "Customer varchar,costCode varchar,ShiptoNumber varchar,"
					+ "ShiptoName varchar, ShiptoAddress varchar, ShiptoCity varchar,"
					+ "ShiptoState varchar, ShiptoZip varchar,"
					+ "OrderTotal varchar, Warehouse varchar, WorkOrder varchar,"
					+ "Price varchar, CustPart varchar,Item varchar,"
					+ "Description varchar, EnterDate date,EnterTime varchar, OnOrder varchar)");
	
		}
		
		public UserMasterDB()
		{
			
		}
		
		public void openDB()
		{
			db = openOrCreateDatabase("EZsource.db", Context.MODE_PRIVATE, null);
		}
		
		public void closeDB()
		{
			db.close();
		}
		
		public void bla()
		{
			db= openOrCreateDatabase("EZsource.db", Context.MODE_PRIVATE, null);
			db.execSQL("create table usermaster (_id INTEGER PRIMARY KEY AUTOINCREMENT,UserID varchar,UserName varchar,UserPin varchar,Customer varchar,CustName varchar,Returnable varchar)");
		}
		
		public ArrayList<String> OutputHistory()
		{
			ArrayList<String> list = new ArrayList<String>();
		//	db.qu
			Cursor c = db.query("outputmaster", null,null, null, null, null, null);
	//c.getCount()
			if(!c.moveToFirst())
				return list;
			UserMaster um = new UserMaster();
//			um.setCustName(c.getString(c.getColumnIndex("CustName")));
//			um.setCustomer(c.getString(c.getColumnIndex("Customer")));
			StringBuffer sb2 = new StringBuffer();
			sb2.append("\tEnterDate:" + c.getString(c.getColumnIndex("EnterDate")) +"\t");
			sb2.append("\tEnterTime:" + c.getString(c.getColumnIndex("EnterTime")) +"\n");
			sb2.append("\tDescription:" + c.getString(c.getColumnIndex("Description")) +"\n");
			sb2.append("\tItem:" + c.getString(c.getColumnIndex("Item")) +"\n");
			sb2.append("\tCustomer:" + c.getString(c.getColumnIndex("Customer")) +"\t");
			sb2.append("\tcostCode:" + c.getString(c.getColumnIndex("costCode")) +"\n");
			sb2.append("\tShiptoNumber:" + c.getString(c.getColumnIndex("ShiptoNumber")) +"\n");
			sb2.append("\tShiptoAddress:" + c.getString(c.getColumnIndex("ShiptoAddress")) +"\n");
			sb2.append("\tCity:" + c.getString(c.getColumnIndex("ShiptoCity")) +"\n");
			sb2.append("\tState:" + c.getString(c.getColumnIndex("ShiptoState")) +"\t");
			sb2.append("\tZip:" + c.getString(c.getColumnIndex("ShiptoZip")) +"\n");
			sb2.append("\tOrderTotal:" + c.getString(c.getColumnIndex("OrderTotal")) +"\n");
			sb2.append("\tWarehouse:" + c.getString(c.getColumnIndex("Warehouse")) +"\n");
			sb2.append("\tWorkOrder:" + c.getString(c.getColumnIndex("WorkOrder")) +"\n");
			sb2.append("\tPrice:" + c.getString(c.getColumnIndex("Price")) +"\n");

			sb2.append("OnOrder:" + c.getString(c.getColumnIndex("OnOrder")) +"");

			list.add(sb2.toString());		
			while(c.moveToNext())
			{
				StringBuffer sb = new StringBuffer();
				sb.append("Customer:" + c.getString(c.getColumnIndex("Customer")) +";");
				sb.append("costCode:" + c.getString(c.getColumnIndex("costCode")) +";");
				sb.append("ShiptoNumber:" + c.getString(c.getColumnIndex("ShiptoNumber")) +";");
				sb.append("ShiptoAddress:" + c.getString(c.getColumnIndex("ShiptoAddress")) +";");
				sb.append("ShiptoCity:" + c.getString(c.getColumnIndex("ShiptoCity")) +";");
				sb.append("ShiptoState:" + c.getString(c.getColumnIndex("ShiptoState")) +";");
				sb.append("ShiptoState:" + c.getString(c.getColumnIndex("ShiptoState")) +";");
				sb.append("ShiptoZip:" + c.getString(c.getColumnIndex("ShiptoZip")) +";");
				sb.append("OrderTotal:" + c.getString(c.getColumnIndex("OrderTotal")) +";");
				sb.append("Warehouse:" + c.getString(c.getColumnIndex("Warehouse")) +";");
				sb.append("WorkOrder:" + c.getString(c.getColumnIndex("WorkOrder")) +";");
				sb.append("Price:" + c.getString(c.getColumnIndex("Price")) +";");
				sb.append("Item:" + c.getString(c.getColumnIndex("Item")) +";");
				sb.append("Description:" + c.getString(c.getColumnIndex("Description")) +";");
				sb.append("EnterDate:" + c.getString(c.getColumnIndex("EnterDate")) +";");
				sb.append("EnterTime:" + c.getString(c.getColumnIndex("EnterTime")) +";");
				sb.append("OnOrder:" + c.getString(c.getColumnIndex("OnOrder")) +";");
				list.add(sb.toString());		
			}
			return list;
			
		}
		public void checktableExist()
		{
			db= openOrCreateDatabase("EZsource.db", Context.MODE_PRIVATE, null);
			
		//	db.execSQL("DROP TABLE IF EXISTS outputmaster");
			db.execSQL("create table outputmaster (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "Customer varchar,costCode varchar,ShiptoNumber varchar,"
					+ "ShiptoName varchar, ShiptoAddress varchar, ShiptoCity varchar,"
					+ "ShiptoState varchar, ShiptoZip varchar,"
					+ "OrderTotal varchar, Warehouse varchar, WorkOrder varchar,"
					+ "Price varchar, CustPart varchar,Item varchar,"
					+ "Description varchar, EnterDate date,EnterTime varchar, OnOrder varchar)");
			
	//		db.execSQL("DROP TABLE IF EXISTS usermaster"); 
		//	db.execSQL("drop table if exists usermaster");
	    //    db.execSQL("CREATE TABLE person (_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age SMALLINT)");  
			db.execSQL("create table usermaster (_id INTEGER PRIMARY KEY AUTOINCREMENT,UserID varchar,UserName varchar,UserPin varchar,Customer varchar,CustName varchar,Returnable varchar)");
	//		db.execSQL("DROP TABLE if EXISTS customermaster");
			db.execSQL("create table customermaster (_id INTEGER PRIMARY KEY AUTOINCREMENT,Customer varchar,CustName varchar,Branch varchar,Warehouse varchar,ShiptoNumber varchar,ShipToName varchar,SRM varchar,Autocrib varchar,WorkOrder varchar,Price varchar,ShipToAddress varchar,ShipToCity varchar,ShipToState varchar,ShipToZip varchar,ShipToContactName varchar,ShipToContactEmail varchar )");
	//		db.execSQL("DROP TABLE IF EXISTS itemmaster");
			db.execSQL("create table itemmaster (_id INTEGER PRIMARY KEY AUTOINCREMENT, Customer varchar,ItemNumber varchar,CustPart varchar,Description varchar,SRM verchar,Price varchar,UOM varchar,OnOrder varchar, Returnable varchar)");
	//		db.execSQL("DROP TABLE IF EXISTS returnablemaster");
			db.execSQL("create table returnablemaster (_id INTEGER PRIMARY KEY AUTOINCREMENT, ItemNumber varchar, CustPart varchar,Description varchar,Date varchar,Time varchar,Status varchar, UOM varchar, USERNAME varchar, ShiptoNumber varchar, ShiptoName varchar, WorkOrder varchar, Customer varchar,CustName varchar)");

		}
		
		public void buildDB()
		{
			db= openOrCreateDatabase("EZsource.db", Context.MODE_PRIVATE, null);
			
		
			db.execSQL("DROP TABLE IF EXISTS usermaster"); 
		//	db.execSQL("drop table if exists usermaster");
	    //    db.execSQL("CREATE TABLE person (_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR, age SMALLINT)");  
			db.execSQL("create table usermaster (_id INTEGER PRIMARY KEY AUTOINCREMENT,UserID varchar,UserName varchar,UserPin varchar,Customer varchar,CustName varchar,Returnable varchar)");
			db.execSQL("DROP TABLE if EXISTS customermaster");
			db.execSQL("create table customermaster (_id INTEGER PRIMARY KEY AUTOINCREMENT,Customer varchar,CustName varchar,Branch varchar,Warehouse varchar,ShiptoNumber varchar,ShipToName varchar,SRM varchar,Autocrib varchar,WorkOrder varchar,Price varchar,ShipToAddress varchar,ShipToCity varchar,ShipToState varchar,ShipToZip varchar,ShipToContactName varchar,ShipToContactEmail varchar )");
			db.execSQL("DROP TABLE IF EXISTS itemmaster");
			db.execSQL("create table itemmaster (_id INTEGER PRIMARY KEY AUTOINCREMENT, Customer varchar,ItemNumber varchar,CustPart varchar,Description varchar,SRM verchar,Price varchar,UOM varchar,OnOrder varchar, Returnable varchar)");
			db.execSQL("DROP TABLE IF EXISTS returnablemaster");
			db.execSQL("create table returnablemaster (_id INTEGER PRIMARY KEY AUTOINCREMENT, ItemNumber varchar, CustPart varchar,Description varchar,Date varchar,Time varchar,Status varchar, UOM varchar, USERNAME varchar, ShiptoNumber varchar, ShiptoName varchar, WorkOrder varchar, Customer varchar,CustName varchar)");
			/*
			 * 
			 * Customer	CustName	Branch	Warehouse	Shipto Number	Ship To Name	SRM	Autocrib	
			 * Work Order	Price	Ship to Address	Ship to City	Ship to State	Ship to Zip	
			 * Ship to Contact Name	Ship to Contact Email
0201010	CANADIAN NATIONAL	01	01	23644200	CN-BRIDGE & BUILDING 3010245A	Y	N	N	Y	24002 VREELAND ROAD	FLAT ROCK	MI	48134	SPECIALTIES BUYER	
0201010	CANADIAN NATIONAL	01	01	26950000	CN-SUPERVISOR MECHANICAL	Y	N	N	Y	24002 VREELAND ROAD	FLAT ROCK	MI	48134	CHARLES KUSE	CHARLES.KRUSE@CN.CA
*/
		}
		public void insertUserMasterDB(UserMaster um)
		{
			ContentValues cv=new ContentValues();
		//	cv.put("_id",1);
			cv.put("UserID",um.getUserID());
			cv.put("UserName", um.getUserName());
			cv.put("UserPin", um.getUserPin());
			cv.put("Customer", um.getCustomer());
			cv.put("CustName", um.getCustName());
			cv.put("Returnable", um.getReturnable());
			db.insert("usermaster", null, cv);
		}
		public void insertCustomerMasterDB(CustomerMasterfile cm)
		{
			ContentValues cv = new ContentValues();
			cv.put("Customer", cm.getCustomer());
			cv.put("CustName", cm.getCustName());
			cv.put("Branch", cm.getBranch());
			cv.put("Warehouse", cm.getWarehouse());
			cv.put("ShipToName", cm.getShiptoName());
			cv.put("ShipToNumber", cm.getShiptoNumber());
			cv.put("SRM", cm.getSRM());
			cv.put("Autocrib", cm.getAutocrib());
			cv.put("WorkOrder", cm.getWorkorder());
			cv.put("Price", cm.getPrice());
			cv.put("ShipToAddress",cm.getShipToAddress());
			cv.put("ShipToCity", cm.getShipToCity());
			cv.put("ShipToState", cm.getShipToState());
			cv.put("ShipToZip", cm.getShipToZip());
			cv.put("ShipToContactName", cm.getShipToContactName());
			cv.put("ShipToContactEmail", cm.getShipToContactEmail());
			db.insert("customermaster", null, cv);
		}
		
		public void insertItemDB(AphaseItemTemplate ai)
		{
			ContentValues cv = new ContentValues();
			cv.put("Customer", ai.getCustomer());
			cv.put("ItemNumber", ai.getItemNumber());
			cv.put("CustPart", ai.getCustPart());
			cv.put("Description", ai.getDescription());
			cv.put("SRM", ai.getSrm());
			cv.put("Price", ai.getPrice());
			cv.put("UOM", ai.getUom());
			cv.put("OnOrder", ai.getOnOrder());
			cv.put("Returnable", ai.getReturnable());
			db.insert("itemmaster", null, cv);
		}
		
		public void insertReturnableItemDB(ReturnableItem ri)
		{
			ContentValues cv = new ContentValues();
			cv.put("ItemNumber", ri.getItemNumber());
			cv.put("CustPart", ri.getCustPart());
			cv.put("Description", ri.getDescription());
			cv.put("Date",ri.getTime());
			cv.put("Time", ri.getTime());
			cv.put("Status", ri.getStatus());
			cv.put("UOM", ri.getUom());
			cv.put("UserName", ri.getUserName());
			cv.put("ShiptoNumber", ri.getShiptoNumber());
			cv.put("ShiptoName", ri.getShiptoName());
			cv.put("WorkOrder", ri.getWortOrder());
			cv.put("Customer", ri.getCustomer());
			cv.put("CustName", ri.getCustName());
			db.insert("returnablemaster", null, cv);
		}
		
		public void insertOutputDB(Output output)
		{
			for(int i=0;i<output.getCargoList().size();i++)
			{
				ContentValues cv = new ContentValues();
				cv.put("Customer", output.getCustomerNumber());
				cv.put("costCode", output.getShiptocode());
				cv.put("ShiptoNumber", output.getShipToNumber());
				cv.put("ShiptoAddress", output.getShipToAddress());
				cv.put("ShiptoCity", output.getShipToCity());
				cv.put("ShiptoState",output.getShipToState());
				cv.put("ShiptoZip", output.getShipToZip());
				cv.put("OrderTotal", output.getOrderTotal());
				cv.put("Warehouse", output.getWarehouse());
				cv.put("WorkOrder", output.getWorkOrder());
				cv.put("Price", output.getCargoList().get(i).getPrice());
				cv.put("CustPart", output.getCargoList().get(i).getCustPart());
				cv.put("Item", output.getCargoList().get(i).getItem());
				cv.put("Description", output.getCargoList().get(i).getDescription());
				cv.put("EnterDate",output.getCargoList().get(i).getEnterdate());
				cv.put("EnterTime", output.getCargoList().get(i).getEntertime());
				cv.put("OnOrder",output.getCargoList().get(i).getOnOrder());
				
				db.insert("outputmaster", null, cv);
			}
					
		}
		
		public void updateOutputDB()
		{
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");
			java.util.Calendar   calendar=java.util.Calendar.getInstance();  
			calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+1);
			String[] lastmonth = {sdf.format(calendar)};
			sdf.format(new java.util.Date());
			db.delete("outputmaster", "EnterDate<?",lastmonth);
		}
		
		public void viewhistry()
		{
			
		}
		public boolean checkexit(String tablename,String row, String column)
		{
			Cursor c = db.query(tablename, null, column + " = ?", new String[]{row}, null, null, null);

			if(c.moveToFirst())
			//c.isAfterLast();.isNull(columnIndex)
			{
				Log.e("ysy", row + " exit");
				return true;				
			}
			else 
			{
				Log.e("ysy", row + " not exit");
				return false; 
			}
		}

		
	}
	
//	
//	


	public void creatpath()
	{
		String path = Environment.getExternalStorageDirectory().getPath();//.getExternalStorageDirectory();
		java.io.File destDir = new java.io.File(path + "/Ezsource/");
		if(!destDir.exists())
		{
			destDir.mkdirs();
		}
		java.io.File fileUM = new java.io.File(path + "/Ezsource/UserMaster/");
		java.io.File fileCM = new java.io.File(path + "/Ezsource/CustomerMaster/");
		java.io.File fileAIT = new java.io.File(path + "/Ezsource/AphaseItemTemplate/");
		java.io.File fileRI = new java.io.File(path + "/Ezsource/ReturnableItems/");
		if(!fileUM.exists())
		{
			fileUM.isDirectory();
			fileUM.mkdirs();
		}
		if(!fileCM.exists())
		{
			fileCM.mkdirs();
		}
		if(!fileAIT.exists())
		{
			fileAIT.mkdirs();
		}
		if(!fileRI.exists())
		{
			fileRI.mkdirs();
		}
	}
	
	private void download()
	{
		 Thread t = new Thread(new Runnable() {

		@Override
		public void run() {
			try {
				List<File> files= retrieveAllFiles(AccountActivity.service);
				Log.e("ysy", files.size() + " ");
				for(int i=0;i<files.size();i++)
				{
					Log.e("ysy", files.get(i).getId());
					Log.e("ysy", files.get(i).getTitle());
					Log.e("ysy", files.get(i).getDownloadUrl());
				}
			
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		 });
		 t.start();
	}
	  void updatedb()
	  {
			Log.e("ysy", "add new db");
			newDb.openDB();
			newDb.buildDB();
			 XlsToString xts = new XlsToString();
			 try {
				umlist =  xts.catchUsermaster();
				cmlist = xts.catchCustomermaster();
				aitlist = xts.catchAphaseItemTemplate();
				rilist = xts.catchReturnableItem();
			} catch (Exception ea) {
				// TODO: handle exception
			}
			int umlistsie = umlist.size();
			int cmlistsize = cmlist.size();
			int aitlistsize = aitlist.size();
			int rilistsize = rilist.size();
			Log.e("ysy", "item set size = "+aitlistsize);
			for(int i = 0; i < umlistsie; i++)
			{
				newDb.insertUserMasterDB(umlist.get(i));
			}
			for(int i = 0;i <  cmlistsize;i++)
			{
				newDb.insertCustomerMasterDB(cmlist.get(i));
			}
			for(int i=0; i < aitlistsize ; i++)
			{
				newDb.insertItemDB(aitlist.get(i));
			}
			for(int i = 0; i < rilistsize ; i++)
			{
				newDb.insertReturnableItemDB(rilist.get(i));
			}
			newDb.closeDB();
	  }
	
	
	  private static List<File> retrieveAllFiles(Drive service) throws IOException {
		    List<File> result = new ArrayList<File>();
		    
		//    Thread t = new Thread(new Runnable() {
		    
		    Files.List request = service.files().list();
		//    request.setQ("trashed=false");
		 //   request.s
	//	    request.
		    do {
		      try {
		        FileList files = request.execute();
		    //    result.addAll(files);
		        for(int i=0;i<files.size();i++)
		        {
		        	result.add(files.getItems().get(i));
		        }
		        result.addAll(files.getItems());
		        request.setPageToken(files.getNextPageToken());
		      } catch (IOException e) {
		        System.out.println("An error occurred: " + e);
		        request.setPageToken(null);
		      }
		    } while (request.getPageToken() != null &&
		             request.getPageToken().length() > 0);

		    return result;
		  }

	
	
}
