package com.example.ezsource;



import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Semaphore;

import jxl.write.WriteException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.function.GMailSender;
import com.example.function.StringToXls;
import com.example.source.AphaseItemTemplate;
import com.example.source.Cargo;
import com.example.source.Output;
import com.example.source.UserMaster;

public class TransactionActivity extends Activity {
	BluetoothAdapter mBluetoothAdapter;
	View parentView;
	UUID uuid;
	String tempstring ;
int qtn = 0;
	public static int state =0;//automata
	final int STATE = 8908;
	final int ENTERUSERID = 1;
	final int ENTERPIN = 2;
	final int CHOOSECUSTOMER = 3;
	final int CHOOSERETURNABLE =4;
	final int COSTCODE = 5;
	static final int STATE_FINAL = 522;
//	final int CHOOSESHIPTONUMBER = ;
	AlertDialog.Builder ad;
	DatePickerDialog dialog;
	String stateuserid = null;
	String statereturnable = null;
	String statecustomer = null;
	String stateshiptonumer = null;
	String stateitemid = null;
	String stateshipdate = null;
	String stateshiptoname = null;
	String stateshiptoaddress = null;
	String stateshiptostate = null;
	String stateshiptocity = null;
	String stateshiptozip = null;
	String statePrice = null;
	String workorder = null;
	int stateplus = 0;
	PopupWindow pw;
	ListView list;
	//mylist is the this that should be showed in listview
	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	
	int bluetoothdevice;
//	  static final int REQUEST_ACCOUNT_PICKER = 1;
//	  static final int REQUEST_AUTHORIZATION = 2;
//	  static final int CAPTURE_IMAGE = 3;
	
	Semaphore semp = new Semaphore(0);
	
	
	//a output contain the user and costermer information and many caogo(item) information
	Output output = new Output();
	List<Cargo> cargolist = new ArrayList();
	//
	Calendar calendar = Calendar.getInstance();  

	//get prefix
	SharedPreferences prefixCode;
	String prefixString;
	public boolean getPrefix()
	{
		prefixCode = getSharedPreferences("MyPrefsFile", 0);
		prefixString = prefixCode.getString("costomercode", "");
		if(prefixString.equals(""))
		{
			Toast.makeText(this, "please input your costomer code in device", Toast.LENGTH_LONG).show();
			return false;
		}
		else {
			return true;
		}	
	}
	public static TransactionActivity ins = null;
	
	/*
	 * Bluetooth Handler
	 * */
	public static Handler ahandler = new Handler()
	{
		
		String tempstring;
	//	String tempstring;
	    public void handleMessage(Message msg) {
	    	Log.e("ysy", "msg"+ msg.what);
        	tempstring = msg.obj.toString();
        	tempstring = tempstring.trim();
	        switch (msg.what) {
	        case 1: //state 1 
	        {
	        	switch(state)
	        	{
	        	case 1: //enteruserid
	        	{
	        		tempstring = msg.obj.toString();
	        		ins.enteruseridplus(tempstring);
	        	}
		        case 2: //enter user pin
		        {
		        	Log.e("ysy", "enterpin");
		        //	tempstring = msg.obj.toString();
		        	ins.enteruserpinplus(tempstring);
	
		        	break;
		        }
		        case 4: //enter shipto code
		        {
		       // 	tempstring = msg.obj.toString();
		        	ins.entershiptoplus(tempstring);
		        	break;
		        }
		        case 5: //enter workorder number
		        {
		         //	tempstring = msg.obj.toString();
		        	ins.workordernumberplus(tempstring);
		        	break;
		        }
		        case 6: //enter item number
		        {
		     //   	tempstring = msg.obj.toString();
		        	ins.scanitemplus(tempstring);
		        	break;
		        }  
		        case STATE_FINAL: //delete next order or down
		        {
		        	ins.uploadbyBluetoothplus(tempstring);
		        }
	        	}
	        	Log.e("ysy","lalalala" + tempstring);
	        	break;
	        }

	      }
		    super.handleMessage(msg); 
	    }
	};
	SimpleAdapter mSchedule;
	
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_transaction);
		ins = this;
		parentView = (View)findViewById(R.id.myTransactionView);
		if(!getPrefix())
		{
			finish();
		}


		ad =new AlertDialog.Builder(this);

	    ins.enteruserid();


		
	    list  = (ListView)findViewById(R.id.mylistview);
		list.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0,
					View arg1, int position, long arg3) {
			if(position != 0)
			{
				output.getCargoList().remove(position-1);
				output.setCargoList(cargolist);
				output.setOrderTotal(cargolist.size());
				output.setShipdate(stateshipdate);
				Log.e("ysy", "listview" + " " + position + " " + arg3);
				mylist.remove(position);
		//		mylist.
				mSchedule.notifyDataSetChanged();				
			}

				return false;
			}
		});
		HashMap<String, String> map = new HashMap<String ,String>();
		map.put("date", "date");
		map.put("qty", "qty");
		map.put("description", "description");
		mylist.add(map);
		mSchedule = new SimpleAdapter(TransactionActivity.this, mylist,
					R.layout.mylistview,
					new String[]{"date","qty","description"},
					new int[]{R.id.listdate,R.id.listqty,R.id.listdescription});
		list.setAdapter(mSchedule);

	
	}

	// show the toast message
	  public void showToast(final String toast) {
		    runOnUiThread(new Runnable() {
		      @Override
		      public void run() {
		        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_LONG).show();
		      }
		    });
		  }
	  
	  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.transaction, menu);
		return true;
	}

	//database function
	private class UserMasterDB
	{
		SQLiteDatabase db;

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
		
		public void insertReturnableDB(String itemNumberString)
		{
			ContentValues cv = new ContentValues();
			cv.put("ItemNumber", itemNumberString);
		//	int i = cargolist.size()-1;
			int i = output.getCargoList().size()-1;
			Log.e("ysy", "i = " + i);
			cv.put("CustPart", output.getCargoList().get(i).getCustPart());
	
			cv.put("Description", output.getCargoList().get(i).getDescription());
			cv.put("Date", output.getCargoList().get(i).getEnterdate());
			cv.put("Time", output.getCargoList().get(i).getEntertime());
		//	cv.put("Status", );
			cv.put("UOM", output.getCargoList().get(i).getUOM());
			cv.put("USERNAME", output.getUser());
			cv.put("ShiptoNumber", output.getShipToNumber());
			cv.put("ShiptoName", output.getShipToName());
			cv.put("WorkOrder", output.getWorkOrder());
			cv.put("Customer", output.getCustomerNumber());
		//	cv.put("CustName", output.getCargoList());
			db.insert("returnablemaster",null,cv);
		}
		
		public void deleteItemOfReturnableDB(String itemNumberString)
		{
		//	String[] tempStrings = {itemNumberString};
			db.delete("returnablemaster", "itemNumber" + "=?",new String[]{itemNumberString});
		}

			
		public UserMasterDB()
		{
			
		}
		
		public void openDB()
		{
			db = openOrCreateDatabase("EZsource.db", Context.MODE_PRIVATE, null);
		}

		/*
		 * check whether a name exist in database
		 * column is the property
		 * row is the word u want to check 
		 * */
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
		/*
		 * query tablename when column = row and column2 = row2
		 * */
		public boolean checkexitplus(String tablename,String row, String column,String row2, String column2)
		{
			Cursor c = db.query(tablename, null, column + " = ?" + " AND " + column2 + " = ?", new String[]{row,row2}, null, null, null);

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
		
		
		public String returnDBString(String tablename,String column,String row,String column2,String row2,String returncolumn )
		{
			String returnString = null;
		//	Cursor c = db.query(tablename,new String[](column,column2),"",null,null,null,null);
			Log.e("ysy", "tablename:" + tablename + "column " + column + "row" + row + "column2 "+ column2 + "row2 " + row2);
			Cursor c = db.query(tablename, null, column + " = ? AND "+ column2 + " = ?", new String[]{row,row2}, null, null, null);
		//	Cursor c = db.query(tablename, null, column + " = ?", new String[]{row}, null, null, null);
			Log.e("ysy", returncolumn);
			if(c.moveToFirst())
				returnString = c.getString(c.getColumnIndex(returncolumn));
			return returnString;
		}
		
//		public String returnDBStringplus(String tablename, String column, String row, String returncolumn)
//		{
//			String returnString = null;
//			//Log.e("ysy", msg)
//		
//			Cursor c  = db.query(tablename,null, column + "= ?",new String[]{row},null,null,null);
//			if(c.moveToFirst())
//				returnString = c.getString(c.getColumnIndex(returncolumn));
//			return returnString;
//		}
		

		
		public ArrayList<UserMaster> customerNameList(String row)
		{
			ArrayList<UserMaster> list = new ArrayList<UserMaster>();
			Cursor c = db.query("usermaster", null,"UserID = ?", new String[]{row}, null, null, null);
			if(!c.moveToFirst())
				return list;
			UserMaster um = new UserMaster();
			um.setCustName(c.getString(c.getColumnIndex("CustName")));
			um.setCustomer(c.getString(c.getColumnIndex("Customer")));
			list.add(um);		
			while(c.moveToNext())
			{
				um = new UserMaster();
				um.setCustName(c.getString(c.getColumnIndex("CustName")));
				um.setCustomer(c.getString(c.getColumnIndex("Customer")));
				list.add(um);		
			}
			return list;
			
		}
		
		//get a AphaseItemTemplate class
		AphaseItemTemplate getAphaseItemTemplate()
		{
			AphaseItemTemplate aitl = new AphaseItemTemplate();
			//	Cursor c = db.query(tablename,new String[](column,column2),"",null,null,null,null);
			Log.e("ysy", "itemmaster" + stateitemid);
			Cursor c = db.query("itemmaster", null, "ItemNumber=?" , new String[]{stateitemid}, null, null, null);
		//	Cursor c = db.query(tablename, null, column + " = ?", new String[]{row}, null, null, null);

			if(c.moveToFirst())
			{
				aitl.setCustomer(c.getString(c.getColumnIndex("Customer")));
				aitl.setItemNumber(c.getString(2));
				aitl.setCustPart(c.getString(3));
				aitl.setDescription(c.getString(4));
				aitl.setSrm(c.getString(5));
				aitl.setPrice(c.getString(6));
				aitl.setUom(c.getString(7));
				aitl.setOnOrder(c.getString(8));
				aitl.setReturnable(c.getString(9));
			}
		//	Log.e("ysy","")
			return aitl;
		}
		
		public void closeDB()
		{
			db.close();
		}
	}
	
	//if a string is empty, return true
	boolean whetherEmpty(String string)
	{
		if(string == null)
			return true;
		if(string.trim().equals(""))
			return true;
		return false;
	}
	
	private class showtextThread extends Thread{
		
		String titleString;
		
		public void setTitle(String title)
		{
			this.titleString = title;
		}
		@Override
		public void run(){
			LayoutInflater li = LayoutInflater.from(TransactionActivity.this);
			View promptsView = li.inflate(R.layout.msgpopoutwindow, null);
//			AlertDialog laladialog = new AlertDialog.Builder(TransactionActivity.this).setView(promptsView).setTitle(title).create();
//			laladialog.setCanceledOnTouchOutside(false);
//			laladialog.show();
			TextView tView = (TextView)promptsView.findViewById(R.id.popuptext);
			Button button = (Button)promptsView.findViewById(R.id.popupbutton);
			pw = new PopupWindow(promptsView,100, 100,true);

			tView.setText(titleString);
			button.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					pw.dismiss();
					semp.release();
					// TODO Auto-generated method stub
					
				}
			});	
			pw.showAtLocation(findViewById(R.id.myTransactionView), Gravity.CENTER, 0, 0);
			super.run();
		}
		
	}
	
	public void showtextDialog(String title)
	{
		showtextThread stThread = new showtextThread();
		stThread.setTitle(title);
		stThread.start();
		try {
			semp.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

	}
	
	
	//state = 1
	AlertDialog adialog;
	public void enteruserid()
	{
		state = 1;
		//initial
		if(stateplus == 1)
		{
			Log.e("ysy", "stateplus=1");
			

			for(int i = output.getOrderTotal();i>=1;i--)
			{
				mylist.remove(i);
			}
			output=new Output();
//			mylist.remove(index)
//			mylist.removeAll(cargolist);
			mSchedule.notifyDataSetChanged();

		}
		stateplus = 1;
		LayoutInflater li = LayoutInflater.from(this);
		View promptsView = li.inflate(R.layout.simpledialoglayout, null);
  	  	final EditText et   = (EditText)promptsView.findViewById(R.id.editTextDialogUserInput);
			ad.setTitle("enter UserID").setView(promptsView);
		
			adialog = 	ad.setPositiveButton("ok", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // User clicked OK button
		        	   adialog.cancel();
		        	   adialog.dismiss();
		        	   tempstring = et.getText().toString();
		        	   if(whetherEmpty(tempstring))
		        	   {
		        		  // ins.showtextDialog("empty");
		        		showToast("empty userID");
		        		   ins.enteruserid();
		        		   return;
		        	   }

		        	   Log.e("ysy", tempstring);
		 //       	   this.state = ENTERPIN;
		        	   if(checkUserID(tempstring))
		        	   {
		        		   state = ENTERPIN;
		        		   stateuserid = tempstring;
		        		   output.setUser(tempstring);
		       // 		   testdialog();
		        		   ins.enteruserpin();
		        		   return;
		        		   //  state = ENTERPIN;
		        	   }
		        	   else {
		        		   showToast("wrong UserID");
		        		   Log.e("ysy","wrong UserID");
		        		   ins.enteruserid();
		        		   return;
		        	   }
		           }
		       }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					adialog.cancel();
					finish();
				}
			}).show();

	}
	//the function with plus used in database handle
	public  void enteruseridplus(String tempstring)
	{
		adialog.cancel();
 	   if(checkUserID(tempstring))
 	   {
 	//	   state = ENTERPIN;
 		   stateuserid = tempstring;
 		   if(whetherEmpty(tempstring))
 		   {
 			   showToast("The user Id is empty");
 			   enteruserid();
 			   return;
 		   }
 		   output.setUser(tempstring);
 		   
 		   enteruserpin();
 		   return;
 		   //  state = ENTERPIN;
 	   }
 	   else {
 		  showToast("wrong UserID");
 		   Log.e("ysy","wrong UserID");
 		   enteruserid();
 	   }		
	}
	
	//state 2
	public void enteruserpin()
	{
		state = 2;
		Log.e("ysy", "enteruserpin");

		LayoutInflater li = LayoutInflater.from(this);
		View promptsView = li.inflate(R.layout.simpledialoglayout, null);
  	  	final EditText et   = (EditText)promptsView.findViewById(R.id.editTextDialogUserInput);	
		ad.setTitle("enter PIN").setView(promptsView);
		adialog = ad.setPositiveButton("ok", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User clicked OK button
	        	   adialog.cancel();
	        	   tempstring = et.getText().toString();
	        	   if(whetherEmpty(tempstring))
	        	   {
	        		   showToast("The user Pin is empty");
	        		   ins.enteruserpin();
	        		   return;
	        	   }
	        	   Log.e("ysy", tempstring);
	        	   if(checkPin(tempstring))
	        	   {
	        		   state = CHOOSECUSTOMER;
	        		   UserMasterDB umdb = new UserMasterDB();
	        		   umdb.openDB();
	        		ArrayList<UserMaster> aList= umdb.customerNameList(stateuserid);
	        		   umdb.closeDB();
	        	//	   showlistdialog(state,aList);
	        		   ins.choosecustomer(aList);
	        		   return;
	        	   }
	        	   else
	        	   {
	        		   ins.enteruserid();
	        		   showToast("wrong Userpin");
	        		   return;
	        	   }
	           }
	       }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				adialog.cancel();
				ins.enteruserid();
				return;
			}
		}).show();
	}
	
	public void enteruserpinplus(String tempString)
	{
		adialog.cancel();
		   Log.e("ysy", "bluetooth" + tempString.length());
 	   if(checkPin(tempString))
 	   {
 	//	   state = CHOOSECUSTOMER;
 		   UserMasterDB umdb = new UserMasterDB();
 		   umdb.openDB();
 		ArrayList<UserMaster> aList= umdb.customerNameList(stateuserid);
 		   umdb.closeDB();
 		   ins.choosecustomer(aList);
 		   return;
 	   }
 	   else
 	   {
 		   ins.enteruserid();
 		  showToast("wrong user pin");
 		  return;
 	   }	
	}
	
	//state 3
	public void choosecustomer(ArrayList<UserMaster> alist)
	{
		state = 3;
		final ArrayList<UserMaster> list = alist;
		final String[] st = new String[list.size()];
		Log.e("ysy", "choosecustomer");
		Log.e("ysy","list size " + list.size());
			for(int i = 0 ; i < list.size(); i++)
			{
				st[i] = list.get(i).getCustName();
			}
		if(list.size() == 1)
		{
			Log.e("ysy", "only one costomer, so just default");
			//TODO 
		}
			adialog = 	new AlertDialog.Builder(this).setTitle("Choose customerName").setItems(st,
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							Log.e("ysy",st[which]);
							adialog.cancel();
							//find the costomer in the  usermaster to see whether it is returnable customer master 
							UserMasterDB umdb = new UserMasterDB();
							umdb.openDB();
							statecustomer = list.get(which).getCustomer();
							output.setCustomerNumber(statecustomer);
							statereturnable = umdb.returnDBString("usermaster", "UserID", stateuserid, "Customer", list.get(which).getCustomer(), "Returnable");
							Log.e("ysy", "returnable " + statereturnable);

							umdb.closeDB();
							Log.e("ysy", "returnable " + statereturnable);
							if(statereturnable.equals("Y"))
							{
								chooseReturnable();
							}
							else {
								entershipto();
							}

						}
					}
					).show();
	}
	
	public void chooseReturnable()
	{
		state = 255;
		Log.e("ysy", "choosereturnable");
		Log.e("ysy", "returnable " + statereturnable);
		if(statereturnable.equals("Y"))
		{
			//whether it can be returned?
			Log.e("ysy", "returnable " + statereturnable);
			//showTwoButtonDialog("Returnable transaction?");
			adialog = new AlertDialog.Builder(this).setTitle("Returnable transaction?").setPositiveButton("yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					adialog.dismiss();
					statereturnable = "Y";
//					showdialog(state);
					entershipto();
				}
			}).setNegativeButton("No",new DialogInterface.OnClickListener()
					{

						@Override
						public void onClick(DialogInterface dialog, int which) {
							adialog.cancel();
							// TODO Auto-generated method stub
							statereturnable = "N";
							entershipto();
						}
				
					}).show();		

		}
		else if (statereturnable == "N") {
			//
			Log.e("ysy", "returnable " + statereturnable);
		}
	
	}
	

	
	// state 4
	public void entershipto()
	{
		/*
		 * AlertDialog*/
//		AlertDialog.Builder ad;
//		ad =new AlertDialog.Builder(this);
		state = 4;
		LayoutInflater li = LayoutInflater.from(this);
		View promptsView = li.inflate(R.layout.simpledialoglayout, null);
  	  	final EditText et   = (EditText)promptsView.findViewById(R.id.editTextDialogUserInput);
  	  	ad.setTitle("Scan/Enter Shipto or Cost Code").setView(promptsView);
  	  adialog =	ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				String tempString = et.getText().toString();
				Log.e("ysy","shiptonum" + tempString);
				adialog.cancel();
     		   	if(whetherEmpty(tempString))
     		   	{
     		   		showToast("The shiptoNumber/costCode is empty ");
     		   		entershipto();
     		   		return;
     		   	}
     		   	
				if(checkShiptoNum(tempString,statecustomer))
				{
					//TODO find the autocrib
					stateshiptonumer = tempString;
					output.setShipToNumber(stateshiptonumer);
					getshipto();
					autocrib5();
	//				else {
//						  dialog.dismiss();
//						  dialog.cancel();
			//			scanitem();
	//				}
					
				}
				else
				{
					Toast.makeText(TransactionActivity.this, "shipto number is wrong!", Toast.LENGTH_LONG).show();
					entershipto();
				}
			}
		}).show();
	}
	
	public void entershiptoplus(String tempString)
	{
		adialog.cancel();
		if(checkShiptoNum(tempString,statecustomer))
		{
			//TODO find the autocrib
			stateshiptonumer = tempString;
			output.setShipToNumber(stateshiptonumer);
			getshipto();
			autocrib5();
		}
		else {
			showToast("shipto number is wrong!");
			entershipto();
		}
	}
	
	public void autocrib5()
	{
		if(autocribflag().equals("Y"))
		{
			SimpleDateFormat sdf = new SimpleDateFormat("MMddyy");
	//		cargo.setEnterdate(sdf.format(new java.util.Date()));
			stateshipdate = sdf.format(new java.util.Date());
			if(workorderflag().equals("Y"))
				workordernumber();
			else
				scanitem();
		}
		else 
		{
			showTimeDialog();
			}
	}
	
	

	
	public String autocribflag()
	{
			UserMasterDB umdb = new UserMasterDB();
			umdb.openDB();
			String stateAutoCrib = umdb.returnDBString("customermaster", "Customer", statecustomer, "ShiptoNumber", stateshiptonumer, "Autocrib");//("customermaster", "Customer", statecustomer,"Autocrib");
			umdb.closeDB();
		//	Toast.makeText(TransactionActivity.this, "AutoCribNum is " + stateAutoCrib, Toast.LENGTH_LONG).show();
			Log.e("ysy", "autocrib"+stateAutoCrib);
			return stateAutoCrib;
	}
	
	public String SRMflag()
	{
				UserMasterDB umdb = new UserMasterDB();
				umdb.openDB();
				String stateAutoCrib = umdb.returnDBString("customermaster", "Customer", statecustomer, "ShiptoNumber", stateshiptonumer, "SRM");
				//String stateAutoCrib = umdb.returnDBStringplus("customermaster", "Customer", statecustomer,"SRM");
				umdb.closeDB();
	//			Toast.makeText(TransactionActivity.this, "SRM is " + stateAutoCrib, Toast.LENGTH_LONG).show();
				return stateAutoCrib;
	}
	public String workorderflag()
	{
		UserMasterDB umdb = new UserMasterDB();
		umdb.openDB();
		String stateWorkerString = umdb.returnDBString("customermaster", "Customer", statecustomer, "ShiptoNumber",stateshiptonumer, "WorkOrder");
		//String stateWorkerString = umdb.returnDBStringplus("customermaster", "Customer", statecustomer,  "WorkOrder");
		umdb.closeDB();
	//	Toast.makeText(TransactionActivity.this, "worker order is " + stateWorkerString, Toast.LENGTH_LONG).show();
		return stateWorkerString;
	}
	
	
	//state 5
	public void workordernumber()
	{
		state = 5;
		Log.e("ysy", "workordernumber");

		LayoutInflater li  = LayoutInflater.from(this);
		View promptsView = li.inflate(R.layout.simpledialoglayout, null);
		final EditText et = (EditText)promptsView.findViewById(R.id.editTextDialogUserInput);
		adialog = ad.setTitle("Enter work order number").setView(promptsView).
		setPositiveButton("ok", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO add scanitem function get the string to somewhere
				adialog.cancel();
				String tempString = et.getText().toString();
				if(whetherEmpty(tempString))
				{
					showToast("the workordernumber is empty");
					ins.workordernumber();
					return;
				}
				workordernumberplus(tempString);
			}
		}).show();
	}
	
	public void workordernumberplus(String tempString)
	{
		adialog.cancel();
		Log.e("ysy", "work order" + tempString);
		output.setWorkOrder(tempString);
		scanitem();
	}
	

	
	//Item scanning process
	// state 6
	//AlertDialog alDialog6;
	public void scanitem()
	{
		state = 6;
		
		LayoutInflater li = LayoutInflater.from(this);
		View promptsView = li.inflate(R.layout.simpledialoglayout, null);
		final EditText et = (EditText)promptsView.findViewById(R.id.editTextDialogUserInput);
		adialog  = ad.setTitle("Scan or enter ItemID").setView(promptsView).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			//	alDialog.cancel();
				adialog.cancel();
				stateitemid = et.getText().toString();
				Log.e("ysy", "itemid "+ stateitemid);
				if(whetherEmpty(stateitemid))
				{
					showToast("the the item id is empty");
					ins.workordernumber();
					return;
				}
				if(checkItemId(stateitemid))
				{
					AphaseItemTemplate newAphaseItemTemplate = getItemFromDB();
					enterQuantity(newAphaseItemTemplate);
				
					//TODO display item information
				}
				else {
					showToast("please input the correct item number");
					scanitem();
					//TODO please input the valid item number

				}
			}
		}).setNegativeButton("cancel and next step", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				adialog.dismiss();
				adialog.cancel();
				if(cargolist.size()!=0)
				{
					output.setCargoList(cargolist);
					output.setOrderTotal(cargolist.size());
					output.setShipdate(stateshipdate);
					//Log.e("ysy", "insert item into sql");
			//		showToast("insert item into sql");
			//		insertOutputDBPlus(output);
					whetherUpload();
				}
				else {
					//TODO please input the at least one item ;
					showToast("please input the correct item number");
					scanitem();
				}
			}
		}).show();
	}
	//TODO have some question in this part.
	public void scanitemplus(String tempstring)
	{
		adialog.cancel();
		stateitemid = tempstring;
		if(checkItemId(stateitemid))
		{
			AphaseItemTemplate newAphaseItemTemplate = getItemFromDB();
			enterQuantity(newAphaseItemTemplate);
		
			//TODO display item information
		}
		else {
			//TODO please input the valid item number
			scanitem();
			Log.e("ysy", tempstring + " is a wrong item id");
		}	
	}
	
	public void whetherUpload()
	{
		state = 255;
		//LayoutInflater li = LayoutInflater.from(this);
		adialog = ad.setTitle("Do you want to upload or edit this order?").setView(null).setPositiveButton("Upload", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				insertOutputDBPlus(output);
				sendemail(output);//just for test
				finish();
			//	adialog.cancel();		
			}
		}).setNegativeButton("Edit", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				adialog.cancel();
				uploadbyBluetooth();
			}
		}).show();
	}
	
	void uploadbyBluetooth()
	{
		state = 	STATE_FINAL;
	}
	
	void uploadbyBluetoothplus(String astring)
	{
		if(astring.equals("~DONE"))
		{
			insertOutputDBPlus(output);	
			sendemail(output);
			finish();
		}
		else if (astring.equals("~DELETE")) {
			// TODO Auto-generated method stub

			mSchedule.notifyDataSetChanged();
			finish();
		}
		else if(astring.equals("~NEXT-ORDER")){
			insertOutputDBPlus(output);
			sendemail(output);
			ins.enteruserid();		
		}
		
		
	}
	
	public void deleteitem()
	{
		
	}
	
	AphaseItemTemplate getItemFromDB()
	{
		UserMasterDB umdb = new UserMasterDB();
		umdb.openDB();
			AphaseItemTemplate ait = umdb.getAphaseItemTemplate();		
		umdb.closeDB();
		return ait;
	}

	
	int testi =0;
	int minustate = 0;
	public void numdialog(AphaseItemTemplate newAphaseItemTemplate)
	{
		state = 255;
		
		View view = LayoutInflater.from(this).inflate(R.layout.numdialoglayout, null);
		ad.setView(view);
		
		final TextView tv = (TextView)view.findViewById(R.id.textView1);
		final TextView tvminus = (TextView)view.findViewById(R.id.textView0);
		 testi =0;
				 minustate = 0;
				 
		final String title = newAphaseItemTemplate.getDescription();
	//	Button button1 = (Button)view.findViewById(R.id.button1);
 	  	 Button btn1 = (Button)view.findViewById(R.id.SearchButton); 
 	  	 Button btn0 = (Button)view.findViewById(R.id.button0); 
 	  	 Button btn2 = (Button)view.findViewById(R.id.button2); 
 	  	 Button btn3 = (Button)view.findViewById(R.id.button3); 
 	  	 Button btn4 = (Button)view.findViewById(R.id.button4); 
 	  	 Button btn5 = (Button)view.findViewById(R.id.button5); 
 	  	 Button btn6 = (Button)view.findViewById(R.id.button6); 
 	  	 Button btn7 = (Button)view.findViewById(R.id.button7); 
 	  	 Button btn8 = (Button)view.findViewById(R.id.button8); 
 	  	 Button btn9 = (Button)view.findViewById(R.id.button9);
 	  	 Button btnminus = (Button)view.findViewById(R.id.buttonminus); 
 	  	 Button btndelete = (Button)view.findViewById(R.id.buttondelete); 
 	  	 TextView tv2 = (TextView) view.findViewById(R.id.textView2);
 	  	 tv2.setText("enter quantaty");
 	  	 
   	  	btn1.setOnClickListener(new View.OnClickListener() {
			
 				@Override
 				public void onClick(View v) {
 					testi = testi*10 + 1;
 					Log.e("ysy", "testi " +testi);
 					tv.setText(""+testi);
 				}
 			});
 	  	  	btn2.setOnClickListener(new View.OnClickListener() {
 				
 				@Override
 				public void onClick(View v) {
 					testi = testi*10 + 2;
 					tv.setText(""+testi);

 				}
 			});
 	  	  	btn3.setOnClickListener(new View.OnClickListener() {
 				
 				@Override
 				public void onClick(View v) {
 					testi = testi*10 + 3;
 					tv.setText(""+testi);

 				}
 			});
 	  	  	btn4.setOnClickListener(new View.OnClickListener() {
 				
 				@Override
 				public void onClick(View v) {
 					testi = testi*10 + 4;
 					tv.setText(""+testi);

 				}
 			});
 	  	  	btn5.setOnClickListener(new View.OnClickListener() {
 				
 				@Override
 				public void onClick(View v) {
 					testi = testi*10 + 5;
 					tv.setText(""+testi);

 				}
 			});
 	  	  	btn6.setOnClickListener(new View.OnClickListener() {
 				
 				@Override
 				public void onClick(View v) {
 					testi = testi*10 + 6;
 					tv.setText(""+testi);

 				}
 			});
 	  	  	btn7.setOnClickListener(new View.OnClickListener() {
 				
 				@Override
 				public void onClick(View v) {
 					testi = testi*10 + 7;
 					tv.setText(""+testi);
 				}
 			});
 	  	  	btn8.setOnClickListener(new View.OnClickListener() {
 				
 				@Override
 				public void onClick(View v) {
 					testi = testi*10 + 8;
 					tv.setText(""+testi);
 				}
 			});
 	  	  	btn9.setOnClickListener(new View.OnClickListener() {
 				
 				@Override
 				public void onClick(View v) {
 					testi = testi*10 + 9;
 					tv.setText(""+testi);
 				}
 			});
 	  	  	btn0.setOnClickListener(new View.OnClickListener() {
 				
 				@Override
 				public void onClick(View v) {
 					testi = testi*10 + 0;
 					tv.setText(""+testi);
 				}
 			});
 	  	  	btndelete.setOnClickListener(new View.OnClickListener() {
 				
 				@Override
 				public void onClick(View v) {
 					testi = testi/10 ;
 					tv.setText(""+testi);
 				}
 			});
 	  	  	btnminus.setOnClickListener(new View.OnClickListener() {
 				
 				@Override
 				public void onClick(View v) {
// 					if(minustate == 0)
// 					{
// 						tvminus.setText("-");
// 						minustate =1;
// 					}
// 					else
// 					{
// 						tvminus.setText("");
// 						minustate = 0;
// 					}

 				}
 			});
 	  	ad.setTitle(title).setPositiveButton("ok", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				adialog.cancel();
				HashMap<String, String> map = new HashMap<String ,String>();
				String tempString = tvminus.getText().toString();
				Log.e("ysy", tempString + tv.getText().toString());
				map.put("date", stateshipdate);
				
				map.put("qty", tempString + tv.getText().toString());
				map.put("description", title);
				mylist.add(map);
		//		mylist.
				Cargo cargo = new Cargo();
				cargo.setQty(tv.getText().toString());
				cargo.setItem(stateitemid);
				cargo.setDescription(title);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Log.e("ysy", sdf.format(new java.util.Date()));
				cargo.setEnterdate(sdf.format(new java.util.Date()));
				SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
				cargo.setEntertime(sdf2.format(new java.util.Date()));
				cargo.setUOM( "EA");
				cargo.setPrice("1");
				cargo.setCustPart("NULL");
				cargo.setOnOrder("N");
				cargolist.add(cargo);

				if(statereturnable.equals("Y"))
				{
					chooseCheckoutOrReturn();
				}
				else {
					mSchedule.notifyDataSetChanged();
					scanitem();					
				}
			}
		});//.setNegativeButton("", listener)
 	  	adialog = ad.show();
	}
	
	//TODO need to think.
	public void chooseCheckoutOrReturn()
	{
		state = 255;
		Log.e("ysy", "choosecheckoutorreturn");
		Log.e("ysy", stateitemid);
		adialog = new AlertDialog.Builder(this).setTitle("Check out or return").setPositiveButton("CHECKING OUT", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				adialog.cancel();
			//	cargolist.get(cargolist.size()-1).setWeatherTeturn(true);
			//	String qty = mylist.get(mylist.size()-1).get("qty");
			//	Log.e("ysy", qty);
			//	mylist.get(mylist.size()-1).put("qty", "-" + qty);
				if(checkReturnableItem(stateitemid))
				{

					//showToast("This item has been sent out, please input the right item");
					mylist.remove(mylist.size()-1);
					cargolist.remove(cargolist.size()-1);
				}
				mSchedule.notifyDataSetChanged();
				scanitem();			
			}
		}).setNegativeButton("RETURNING", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				adialog.cancel();
				cargolist.get(cargolist.size()-1).setWeatherTeturn(true);
				String qty = mylist.get(mylist.size()-1).get("qty");
				Log.e("ysy", qty);
				mylist.get(mylist.size()-1).put("qty", "-" + qty);
				if(!checkReturnableItem(stateitemid))
				{
					mylist.remove(mylist.size()-1);
					showToast("This item is not in returnable, please input the right item");
					cargolist.remove(cargolist.size()-1);
				}
//				UserMasterDB db = new UserMasterDB();
//				db.openDB();
//				try {
//					db.deleteItemOfReturnableDB(stateitemid);//;ReturnableDB(stateitemid);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				db.closeDB();
				mSchedule.notifyDataSetChanged();
				scanitem();		
			}
		}).show();
	}
	
	public void enterQuantity(AphaseItemTemplate newAphaseItemTemplate)
	{
		numdialog(newAphaseItemTemplate);
	}
	
	public void sendemail(Output output)
	{
		Log.e("ysy", "sendmail");
		StringToXls stx = new StringToXls();
		if(autocribflag().equals("Y"))
		{
			
		}
		else
		{
			
		}
		
//	    WriteExcel test = new WriteExcel();
//	    test.setOutputFile("/Users/shiyaoyu/test.xls");
	//    test.write();
	//	String name = "/mnt/sdcard/Ezsource/test.xls";
		String filenameString = "";
		SharedPreferences costomercode = getSharedPreferences("MyPrefsFile", 0);
		String costomercodeString = costomercode.getString("costomercode", "");
		String date = output.getCargoList().get(0).getEnterdate().replace("-", "");
		String time = output.getCargoList().get(0).getEntertime().replace(":", "");
		//1
		if(autocribflag().equals("Y"))
		{
			filenameString = "Autocrib" + costomercodeString + "_" + output.getCustomerNumber() +"_" + "EZ" + "_" +date + "_" +time+ ".xls";
		}
		//2
		if(autocribflag().equals("N")&&SRMflag().equals("N"))
		{
			filenameString = costomercodeString + "_" + output.getCustomerNumber() + "_" + date + "_" +time + ".xls";
		}
		//3
		if(SRMflag().equals("Y"))
		{
			filenameString = "SRM" + costomercodeString + "_" + output.getCustomerNumber() +"_" + "SRM" +  "_" + date + "_" +time  + ".xls";
		}
		//4
		if(statereturnable.equals("Y"))
		{
			filenameString = costomercodeString +"_"+ "RET" + "_" + date + "_" +time +  ".xls";
		}
		stx.setOutputFile(filenameString);
		try {
			stx.write(output);
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SendEmailThread seThread = new SendEmailThread();
		seThread.setname(filenameString);
		seThread.start();

	}
	
	private class SendEmailThread extends Thread{

		String name;
		public void setname(String aname)
		{
			this.name = aname;
		}
		@Override
		public void run() {
			try {
				Output outputplus = (Output) output.clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			  String astring =Environment.getExternalStorageDirectory().getPath()  +java.io.File.separator+"Ezsource/"+name;; 
			try{
				GMailSender sender = new GMailSender("ezsourcesending@gmail.com", "sending78");
		//		GMailSender sender = new GMailSender("yuysyu@gmail.com", "g1heart2love");
				SharedPreferences email = getSharedPreferences("outputemail", 0);
				final String emailString = email.getString("outputemail", "ezsourcesending@gmail.com");
				sender.sendMail("output", "output", "ezsourcesending@gmail.com", "yuysyu@gmail.com",astring,name);
			}
			catch(Exception e)
			{
				Log.e("SendMail", e.getMessage());
			}
		//	  String astring =Environment.getExternalStorageDirectory().getPath()  +java.io.File.separator+"Ezsource/"+name;; 
			    java.io.File file = new  java.io.File(astring);
			    file.delete();
			
			super.run();
		}
		
	}
	

	public void showTimeDialog()
	{
		state = 255;
		DatePickerDialog.OnDateSetListener dateListener =   
			    new DatePickerDialog.OnDateSetListener() {  
			        @Override  
			        public void onDateSet(DatePicker datePicker,   
			                int year, int month, int dayOfMonth) {  

			        	dialog.dismiss();
			       // 	dialog.
			        	Log.e("ysy", "ondatesetlisterner");
			        	Log.e("ysy","year "+ year + "month " + month + "day " + dayOfMonth);
			        	stateshipdate = month + "/" + dayOfMonth + "/" + year;
						if(workorderflag().equals("Y"))
						{
							workordernumber();
						}
						else
						{
							scanitem();
						}
			        }  
			    };  
			    
		Log.e("ysy", "showtimedialog");
		
		dialog = new DatePickerDialog(this,  
                    dateListener,  
                    calendar.get(Calendar.YEAR),  
                    calendar.get(Calendar.MONTH),  
                    calendar.get(Calendar.DAY_OF_MONTH));
	//	ad = (AlertDialog)dialog;
		dialog.show();
	}
	


	void insertOutputDBPlus(Output output)
	{
		UserMasterDB umdb = new UserMasterDB();
		umdb.openDB();
		umdb.insertOutputDB(output);
		if(statereturnable.equals("Y"))
		{
			Log.e("ysy", "yinyinyin");
			int i = output.getCargoList().size();
			for(;i>0;i--)
			{
				if(output.getCargoList().get(i-1).isWeatherTeturn())
				{
					umdb.deleteItemOfReturnableDB(output.getCargoList().get(i-1).getItem());
				}
				else {
					umdb.insertReturnableDB(output.getCargoList().get(i-1).getItem());
				}
			}
		}
		umdb.closeDB();
	}
	
//	void OutputHistoryPlus()
//	{
//		UserMasterDB umdb = new UserMasterDB();
//		umdb.openDB();
//		List<String> list = umdb.OutputHistory();
//		for(int i=0;i<list.size();i++)
//		{
//			Log.e("ysy", list.get(i));
//		}
//		umdb.closeDB();
//	}

	boolean checkUserID(String id)
	{
		//find id in USER master database
		UserMasterDB umdb = new UserMasterDB();
		umdb.openDB();
		boolean tpstate =  umdb.checkexit("usermaster", id, "UserID");
		umdb.closeDB();
		return tpstate;
	}
	
	boolean checkPin(String userPin)
	{
		//find id in USER master database
		UserMasterDB umdb = new UserMasterDB();
		umdb.openDB();
		boolean tpstate =  umdb.checkexit("usermaster", userPin, "UserPin");
		umdb.closeDB();
		return tpstate;		
	}
	
	boolean checkReturnableItem(String returnableItemString)
	{
		UserMasterDB umdb = new UserMasterDB();
		umdb.openDB();
		boolean tpstate = umdb.checkexit("returnablemaster",  returnableItemString,"ItemNumber");
		umdb.closeDB();
		Log.e("ysy", "returnable " + tpstate);
		return tpstate;
	}
	
	boolean checkShiptoNum(String shipto,String customer )
	{
		Log.e("ysy", "chechshiptonum");
		UserMasterDB umdb = new UserMasterDB();
		umdb.openDB();
	//boolean tpstate = umdb.checkexit("customermaster", shipto,"ShiptoNumber" );
		boolean tpstate = umdb.checkexitplus("customermaster", shipto, "ShiptoNumber", customer, "Customer");
		umdb.closeDB();
		return tpstate;
	}
	
	void getshipto()
	{
		UserMasterDB umdb = new UserMasterDB();
		umdb.openDB();
		
		stateshiptoname = umdb.returnDBString("customermaster", "Customer", statecustomer, "shiptonumber", stateshiptonumer, "ShipToName");
		stateshiptocity = umdb.returnDBString("customermaster", "Customer", statecustomer, "shiptonumber", stateshiptonumer, "ShipToCity");
		stateshiptoaddress = umdb.returnDBString("customermaster", "Customer", statecustomer, "shiptonumber", stateshiptonumer, "ShipToAddress");
		stateshiptozip = umdb.returnDBString("customermaster", "Customer", statecustomer, "shiptonumber", stateshiptonumer, "ShipToZip");
		stateshiptostate =umdb.returnDBString("customermaster", "Customer", statecustomer, "shiptonumber", stateshiptonumer, "ShipToState");
		output.setShipToName(stateshiptoname);
		output.setShipToCity(stateshiptocity);
		output.setShipToAddress(stateshiptoaddress);
		output.setShipToZip(stateshiptozip);
		output.setShipToState(stateshiptostate);
		umdb.closeDB();

	}
	
	boolean checkItemId(String itemid)
	{
		UserMasterDB umdb = new UserMasterDB();
		umdb.openDB();
		boolean tpstate = umdb.checkexit("itemmaster", itemid, "ItemNumber");
		umdb.closeDB();
		return tpstate;
	}

}

