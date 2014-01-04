package com.example.ezsource;



import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

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
import com.example.source.Output;
import com.example.source.ReturnableItem;
import com.example.source.UserMaster;


public class MainActivity extends Activity {
	BluetoothAdapter mBluetoothAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Toast.makeText(this, "Some message!", Toast.LENGTH_LONG).show();
		creatpath();
		List<UserMaster> umlist = null;
		List<CustomerMasterfile> cmlist = null;
		List<AphaseItemTemplate> aitlist = null;
		List<ReturnableItem> rilist = null;

		setContentView(R.layout.mainactivity);
		UserMasterDB newDb = new UserMasterDB();
		try {
			newDb.openDB();
			//newDb.bla();
			//newDb.checktableExist();
			
			//newDb.
		} catch (Exception e) {
			// TODO: handle exception
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
			for(int i = 0; i < rilistsize && i < 300; i++)
			{
				newDb.insertReturnableItemDB(rilist.get(i));
			}
		}


		newDb.closeDB();
		
		
		
//		UserMaster um = new UserMaster("LOU","Lou","Ray","0201010","CANADIAN NATIONAL","Y");
//		newDb.insertUserMasterDB(um);
//		
//		um = new UserMaster("LOU", "Lou", "Ray", "C201012", "CN VOUCHER-TOM DEERING", "N");	
//		newDb.insertUserMasterDB(um);
//		
//		CustomerMasterfile cm = new CustomerMasterfile("0201010", "CANADIAN NATIONAL", "01", "01", 
//				"23644200", "CN-BRIDGE & BUILDING 3010245A","Y", "N", "N", "Y", "24002 VREELAND ROAD",
//				"FLAT ROCK", "MI", "48134", "SPECIALTIES BUYER", "");
//		newDb.insertCustomerMasterDB(cm);
//		cm = new CustomerMasterfile("0201010", "CANADIAN NATIONAL", "01", "01", "26950000", 
//				"CN-SUPERVISOR MECHANICAL", "Y", "N", "N", "Y", "24002 VREELAND ROAD", 
//				"FLAT ROCK", "MI", "48134", "CHARLES KUSE", "CHARLES.KRUSE@CN.CA");
//		newDb.insertCustomerMasterDB(cm);
		Button button;
		button = (Button)findViewById(R.id.btnstartnewtran);
		button.setOnClickListener(
			new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(MainActivity.this, TransactionActivity.class);
					startActivity(intent);
				}
			}
		);
		
		Button btnViewHistory;
		
	//	btnViewHistroy = (Button)findViewById(R.id.)
		
		Button btnBlueTooth;
		btnBlueTooth = (Button)findViewById(R.id.btnbluetooth);
		btnBlueTooth.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		
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
	}
	
	public class Person
	{
		public String name;
		public int age;
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
			
			db.execSQL("DROP TABLE IF EXISTS outputmaster");
			db.execSQL("create table outputmaster (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "Customer varchar,costCode varchar,ShiptoNumber varchar,"
					+ "ShiptoName varchar, ShiptoAddress varchar, ShiptoCity varchar,"
					+ "ShiptoState varchar, ShiptoZip varchar,"
					+ "OrderTotal varchar, Warehouse varchar, WorkOrder varchar,"
					+ "Price varchar, CustPart varchar,Item varchar,"
					+ "Description varchar, EnterDate date,EnterTime varchar, OnOrder varchar)");
			
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
				cv.put("CostCode", output.getShiptocode());
				cv.put("ShiptoNumber", output.getShipToNumber());
				cv.put("ShiptoAddress", output.getShipToAddress());
				cv.put("ShiptoCity", output.getShipToCity());
				cv.put("ShiptoState",output.getShipToState());
				cv.put("ShipToZip", output.getShipToZip());
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
		File destDir = new File(path + "/Ezsource/");
		if(!destDir.exists())
		{
			destDir.mkdirs();
		}
		File fileUM = new File(path + "/Ezsource/UserMaster/");
		File fileCM = new File(path + "/Ezsource/CustomerMaster/");
		File fileAIT = new File(path + "/Ezsource/AphaseItemTemplate/");
		File fileRI = new File(path + "/Ezsource/ReturnableItems/");
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
	
	
	
	
}
