package com.example.ezsource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Semaphore;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RemoteViews.RemoteView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.source.AphaseItemTemplate;
import com.example.source.Cargo;
import com.example.source.NumberDialog;
import com.example.source.Output;
import com.example.source.UserMaster;
import com.google.android.gms.internal.i;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;

public class TransactionActivity extends Activity {
	BluetoothAdapter mBluetoothAdapter;
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
	
	ListView list;
	
//	String statetime = null;
	
//	static Handler ahandler;
	
	
	int bluetoothdevice;

	  static final int REQUEST_ACCOUNT_PICKER = 1;
	  static final int REQUEST_AUTHORIZATION = 2;
	  static final int CAPTURE_IMAGE = 3;
	
	Semaphore semp = new Semaphore(0);
	
	Output output = new Output();
	//Cargo cargo = new Cargo();
	List<Cargo> cargolist = new ArrayList();
	Calendar calendar = Calendar.getInstance();  

	SharedPreferences costomercode;
	String costomercodeString;
	ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
	
	public boolean costomerCodeOn()
	{
		costomercode = getSharedPreferences("MyPrefsFile", 0);
		costomercodeString = costomercode.getString("costomercode", "");
		if(costomercodeString.equals(""))
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
	 * Handler
	 * */
	static Handler ahandler = new Handler()
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
	        	case 1:
	        	{
	        		tempstring = msg.obj.toString();
	        		ins.enteruseridplus(tempstring);
	        	}
		        case 2:
		        {
		        	Log.e("ysy", "enterpin");
		        //	tempstring = msg.obj.toString();
		        	ins.enteruserpinplus(tempstring);
	
		        	break;
		        }
		        case 4:
		        {
		       // 	tempstring = msg.obj.toString();
		        	ins.entershiptoplus(tempstring);
		        	break;
		        }
		        case 5:
		        {
		         //	tempstring = msg.obj.toString();
		        	ins.workordernumberplus(tempstring);
		        	break;
		        }
		        case 6:
		        {
		     //   	tempstring = msg.obj.toString();
		        	ins.scanitemplus(tempstring);
		        	break;
		        }        	
	        	}
	     //   	tempstring = msg.obj.toString();
	        //	enteruseridplus(tempstring);
	        	Log.e("ysy","lalalala" + tempstring);
	        	break;
	        }
//	        case 2:
//	        {
//	        	Log.e("ysy", "enterpin");
//	        //	tempstring = msg.obj.toString();
//	        	enteruserpinplus(tempstring);
//
//	        	break;
//	        }
//	        case 4:
//	        {
//	       // 	tempstring = msg.obj.toString();
//	        	entershiptoplus(tempstring);
//	        	break;
//	        }
//	        case 5:
//	        {
//	         //	tempstring = msg.obj.toString();
//	        	workordernumberplus(tempstring);
//	        	break;
//	        }
//	        case 6:
//	        {
//	     //   	tempstring = msg.obj.toString();
//	        	scanitemplus(tempstring);
//	        	break;
//	        }
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
		if(!costomerCodeOn())
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
			
				output.getCargoList().remove(position-1);
				Log.e("ysy", "listview" + " " + position + " " + arg3);
				mylist.remove(position);
				mSchedule.notifyDataSetChanged();
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

	  private void saveFileToDrive() {
		    Thread t = new Thread(new Runnable() {
		      @Override
		      public void run() {
		        try {
		          // File's binary content
		        	Drive service =AccountActivity.service;
		          
		          File body = new File();
		          body.setTitle("um");
		          body.setDescription("A test document");
		          body.setMimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		          
		          String path = Environment.getExternalStorageDirectory().getPath();
			//	  File file = new File(path + "/Ezsource/UserMaster");
		          java.io.File fileContent1 = new java.io.File(path + "/Ezsource/UserMaster/UserMaster.xls");
		          FileContent mediaContent = new FileContent("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", fileContent1);

		          File file = service.files().insert(body, mediaContent).execute();
		          if (file != null) {
		            showToast("Photo uploaded: " + file.getTitle());
		            Log.e("ysy", "uploaded");
		 //           startCameraIntent();
		          }
		          else {
		        	  showToast("Photo not uploaded: ");
		        	  Log.e("ysy", "notuploaded");
				}
		        } catch (UserRecoverableAuthIOException e) {
		        	Log.e("ysy", "UserRecoverableAuthIOException");
		        	e.printStackTrace();
		          startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
		        } catch (IOException e) {
		          e.printStackTrace();
		        }
		      }
		    });
		    t.start();
		  }
	  public void showToast(final String toast) {
		    runOnUiThread(new Runnable() {
		      @Override
		      public void run() {
		        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
		      }
		    });
		  }
	  
	  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.transaction, menu);
		return true;
	}

	/*
	
	public String bluetoothGetMessage(){
		String tempStr = null;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(mBluetoothAdapter == null){
			new AlertDialog.Builder(TransactionActivity.this).setTitle("Alert")
			.setMessage("no bluetooth").show();
		}
		return tempStr;
	}

	private class ConnectThread extends Thread {
	    private final BluetoothSocket mmSocket;
	    private final BluetoothDevice mmDevice;

	    public ConnectThread(BluetoothDevice device) {
	        // Use a temporary object that is later assigned to mmSocket,
	        // because mmSocket is final
	        BluetoothSocket tmp = null;
	        mmDevice = device;

	        // Get a BluetoothSocket to connect with the given BluetoothDevice
	        try {
	            // MY_UUID is the app's UUID string, also used by the server code
	       //     tmp = device.createRfcommSocketToServiceRecord(uuid);
	            
	            Method m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
	            tmp = (BluetoothSocket) m.invoke(device, 1);
	            
	        } catch (Exception e) { 
	        	Log.e("ysy", e.toString());
	        }
	        mmSocket = tmp;
	    }
	    

	    public void run() {
	        // Cancel discovery because it will slow down the connection
	        mBluetoothAdapter.cancelDiscovery();

	        try {
	            // Connect the device through the socket. This will block
	            // until it succeeds or throws an exception
	            mmSocket.connect();
	            semp.release();
	        } catch (IOException connectException) {
	            // Unable to connect; close the socket and get out

	        	System.exit(0);
//	            try {
//	                mmSocket.close();
//	                return;
//	            } 
//	            catch (IOException closeException) { 
//	            	closeException.printStackTrace();
//	            }
//	        
//		            return;          	
	            

	        }

	        // Do work to manage the connection (in a separate thread)
	//        manageConnectedSocket(mmSocket);
	    }

	    //  Will cancel an in-progress connection, and close the socket 
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }
	}
	
//data transmit
	private class ConnectedThread extends Thread {
	    private final BluetoothSocket mmSocket;
	    private final InputStream mmInStream;
	    private final OutputStream mmOutStream;
	    private Handler mmhandler;

	    public ConnectedThread(BluetoothSocket socket,Handler mhandler) {
	        mmSocket = socket;
	        mmhandler = mhandler;
	        InputStream tmpIn = null;
	        OutputStream tmpOut = null;

	      
	        // Get the input and output streams, using temp objects because
	        // member streams are final
	        try {
	            tmpIn = socket.getInputStream();
	            tmpOut = socket.getOutputStream();
	        } catch (IOException e) { }

	        mmInStream = tmpIn;
	        mmOutStream = tmpOut;
	    }

	    public void run() {
	    	 // lock.notify();
	        byte[] buffer = new byte[1024];  // buffer store for the stream
	        int bytes; // bytes returned from read()
	        Log.e("ysy", "connected thread");
	        // Keep listening to the InputStream until an exception occurs
	        while (true) {
	            try {
	                // Read from the InputStream
	                bytes = mmInStream.read(buffer);
	                String data = new String(buffer, 0, bytes);
	                Log.e("ysy", data);

	                // Send the obtained bytes to the UI activity
//	                mmhandler.obtainMessage(TransactionActivity.DATA_RECEIVE,data)
//	                        .sendToTarget();
	                mmhandler.obtainMessage(state, data).sendToTarget();
	            } catch (IOException e) {
	            	Log.e("ysy", e.getMessage());
	                break;
	            }
	        }
	    }

	    //  Call this from the main activity to send data to the remote device 
	    public void write(byte[] bytes) {
	        try {
	            mmOutStream.write(bytes);
	        } catch (IOException e) { }
	    }

	    //  Call this from the main activity to shutdown the connection 
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }
	}
	
	*/
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
//			while(c.moveToNext())
//			{
//				um = new UserMaster();
//				um.setCustName(c.getString(c.getColumnIndex("CustName")));
//				um.setCustomer(c.getString(c.getColumnIndex("Customer")));
//				list.add(um);		
//			}
			return list;
			
		}
		
		
		public UserMasterDB()
		{
			
		}
		
		public void openDB()
		{
			db = openOrCreateDatabase("EZsource.db", Context.MODE_PRIVATE, null);
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
		
		public String returnDBString(String tablename,String column,String row,String column2,String row2,String returncolumn )
		{
			String returnString = null;
		//	Cursor c = db.query(tablename,new String[](column,column2),"",null,null,null,null);
			Log.e("ysy", "tablename:" + tablename + "column " + column + "row" + row + "column2 "+ column2 + "row2 " + row2);
			Cursor c = db.query(tablename, null, column + " = ? AND "+ column2 + " = ?", new String[]{row,row2}, null, null, null);
		//	Cursor c = db.query(tablename, null, column + " = ?", new String[]{row}, null, null, null);

			if(c.moveToFirst())
				returnString = c.getString(c.getColumnIndex(returncolumn));
			return returnString;
		}
		
		public void setDBString()
		{
			
		}
		
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
	

	
	//state = 1
	AlertDialog adialog;
	public void enteruserid()
	{
		state = 1;
		LayoutInflater li = LayoutInflater.from(this);
		View promptsView = li.inflate(R.layout.simpledialoglayout, null);
  	  	final EditText et   = (EditText)promptsView.findViewById(R.id.editTextDialogUserInput);
			ad.setTitle("enter UserID").setView(promptsView);
		
			adialog = 	ad.setPositiveButton("ok", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // User clicked OK button
		        	   tempstring = et.getText().toString();
		        	   
		        	   adialog.cancel();
		        	   Log.e("ysy", tempstring);
		 //       	   this.state = ENTERPIN;
		        	   if(checkUserID(tempstring))
		        	   {
		        		   state = ENTERPIN;
		        		   stateuserid = tempstring;
		        		   output.setUser(tempstring);
		       // 		   testdialog();
		        		   ins.enteruserpin();
		        		   //  state = ENTERPIN;
		        	   }
		        	   else {
		        		   Log.e("ysy","wrong UserID");
		        		   ins.enteruserid();
		        	   }
		           }
		       }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					adialog.cancel();
					// TODO Auto-generated method stub
					//User clicked cancel button
					
				}
			}).show();

	}
	
	public  void enteruseridplus(String tempstring)
	{
		adialog.cancel();
 	   if(checkUserID(tempstring))
 	   {
 	//	   state = ENTERPIN;
 		   stateuserid = tempstring;
 		   output.setUser(tempstring);
 		   enteruserpin();
 		   //  state = ENTERPIN;
 	   }
 	   else {
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
	        	   Log.e("ysy", tempstring);
	        	   if(checkPin(tempstring))
	        	   {
	        		   state = CHOOSECUSTOMER;
	        		   UserMasterDB umdb = new UserMasterDB();
	        		   umdb.openDB();
	        		ArrayList<UserMaster> aList= umdb.customerNameList(stateuserid);
	        		   umdb.closeDB();
	        	//	   showlistdialog(state,aList);
	        		   choosecustomer(aList);
	        	   }
	        	   else
	        	   {
	        		   enteruserid();
	//        		   state = ENTERUSERID;	        		   
//	        		   showdialog(ENTERUSERID);
	        	   }
	           }
	       }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				adialog.cancel();
				// TODO Auto-generated method stub
				//User clicked cancel button			
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
 	//	   showlistdialog(state,aList);
 		   choosecustomer(aList);
 		
 	   }
 	   else
 	   {
 		   enteruserid();
// 		   state = ENTERUSERID;	        		   
// 		   showdialog(ENTERUSERID);
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
//							if(statereturnable == "Y")
//							{
//								//whether it can be returned?
//								
//								showTwoButtonDialog("Returnable transaction?");
//							}
//							else if (statereturnable == "N") {
//								//
//								
//							}
//							showTwoButtonDialog("Returnable transaction?");
							umdb.closeDB();
							Log.e("ysy", "returnable " + statereturnable);
							chooseReturnable();
						}
					}
					).show();
	}
	
	public void chooseReturnable()
	{
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
     		   
				if(checkShiptoNum(tempString))
				{
					//TODO find the autocrib
					stateshiptonumer = tempString;
					output.setShipToNumber(stateshiptonumer);
					getshipto();
					
					
					
					if(autocribflag().equals("Y"))
					{
						SimpleDateFormat sdf = new SimpleDateFormat("mmddyy");
				//		cargo.setEnterdate(sdf.format(new java.util.Date()));
						stateshipdate = sdf.format(new java.util.Date());
						if(workorderflag().equals("Y"))
							workordernumber();
						else
							scanitem();
					//	showTimeDialog();
					}
					else 
					{
						showTimeDialog();
						}
	//				else {
//						  dialog.dismiss();
//						  dialog.cancel();
			//			scanitem();
	//				}
					
				}
				else
				{
					Toast.makeText(TransactionActivity.this, "shipto number is wrong!", Toast.LENGTH_LONG).show();
					
				}
			}
		}).show();
	}
	
	public void entershiptoplus(String tempString)
	{
		adialog.cancel();
		if(checkShiptoNum(tempString))
		{
			//TODO find the autocrib
			stateshiptonumer = tempString;
			output.setShipToNumber(stateshiptonumer);
			getshipto();
			
			
			
			if(autocribflag().equals("Y"))
			{
				SimpleDateFormat sdf = new SimpleDateFormat("mmddyy");
		//		cargo.setEnterdate(sdf.format(new java.util.Date()));
				stateshipdate = sdf.format(new java.util.Date());
				if(workorderflag().equals("Y"))
					workordernumber();
				else
					scanitem();
			//	showTimeDialog();
			}
			else 
			{
				showTimeDialog();
				}
		}
	}
	


	
	public String autocribflag()
	{
			UserMasterDB umdb = new UserMasterDB();
			umdb.openDB();
			String stateAutoCrib = umdb.returnDBString("customermaster", "Customer", statecustomer, "shiptonumber", stateshiptonumer, "Autocrib");
			umdb.closeDB();
			Toast.makeText(TransactionActivity.this, "AutoCribNum is " + stateAutoCrib, Toast.LENGTH_LONG).show();
			return stateAutoCrib;
	}
	
	public String workorderflag()
	{
		UserMasterDB umdb = new UserMasterDB();
		umdb.openDB();
		String stateWorkerString = umdb.returnDBString("customermaster", "Customer", statecustomer, "shiptonumber", stateshiptonumer, "WorkOrder");
		umdb.closeDB();
		Toast.makeText(TransactionActivity.this, "worker order is " + stateWorkerString, Toast.LENGTH_LONG).show();
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
				workordernumberplus(tempString);
//				scanitem();
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
				if(checkItemId(stateitemid))
				{
					AphaseItemTemplate newAphaseItemTemplate = getItemFromDB();
					enterQuantity(newAphaseItemTemplate);
				
					//TODO display item information
				}
				else {
					//TODO please input the valid item number

				}
			}
		}).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
			
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
					showToast("insert item into sql");
			//		insertOutputDBPlus(output);
					whetherUpload();
				}
				else {
					//TODO please input the at least one item ;
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
			Log.e("ysy", tempstring + " is a wrong item id");
		}	
	}
	
	public void whetherUpload()
	{
		//LayoutInflater li = LayoutInflater.from(this);
		adialog = ad.setTitle("Do you want to upload or edit this order?").setView(null).setPositiveButton("Upload", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				adialog.cancel();		
			}
		}).setNegativeButton("Edit", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				adialog.cancel();
			}
		}).show();
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
 					if(minustate == 0)
 					{
 						tvminus.setText("-");
 						minustate =1;
 					}
 					else
 					{
 						tvminus.setText("");
 						minustate = 0;
 					}

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
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
				cargo.setEnterdate(sdf.format(new java.util.Date()));
				SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm:ss");
				cargo.setEntertime(sdf2.format(new java.util.Date()));
				cargo.setUOM( "EA");
				cargo.setPrice("1");
				cargo.setCustPart("NULL");
				cargo.setOnOrder("N");
				cargolist.add(cargo);
				mSchedule.notifyDataSetChanged();
//				
//				SimpleAdapter mSchedule = new SimpleAdapter(this, mylist,
//						R.layout.mylistview,
//						new String[]{"date","qty","description"},
//						new int[]{R.id.listdate,R.id.listqty,R.id.listdescroption});
//				list.setAdapter(mSchedule);
				scanitem();					
			}
		});//.setNegativeButton("", listener)
 	  	adialog = ad.show();
	}
	
	public void enterQuantity(AphaseItemTemplate newAphaseItemTemplate)
	{
		numdialog(newAphaseItemTemplate);
	}
	
	public void showdialog(int astate)
	{
		/*
		 * AlertDialog*/
		LayoutInflater li = LayoutInflater.from(this);
		View promptsView = li.inflate(R.layout.simpledialoglayout, null);
  	  	final EditText et   = (EditText)promptsView.findViewById(R.id.editTextDialogUserInput);
		switch(state)
		{
		case ENTERUSERID:// enter UserID
		{
			ad.setTitle("enter UserID").setView(promptsView);
			ad.setPositiveButton("ok", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // User clicked OK button
		        	   tempstring = et.getText().toString();
	        		   dialog.dismiss();
		        	   Log.e("ysy", tempstring);
		 //       	   this.state = ENTERPIN;
		        	   if(checkUserID(tempstring))
		        	   {
		        		   state = ENTERPIN;
		        		   stateuserid = tempstring;
		        		   showdialog(ENTERPIN);
		        		 //  state = ENTERPIN;
		        	   }
		        	   else {
		        		   Log.e("ysy","wrong UserID");
		        		   showdialog(ENTERUSERID);
		        	   }
		           }
		       }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 
					//User clicked cancel button
					
				}
			}).show();
			break;
		}
		case ENTERPIN:// enter UserID
		{
			ad.setTitle("enter PIN").setView(promptsView);
			ad.setPositiveButton("ok", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		               // User clicked OK button
		        	   tempstring = et.getText().toString();
		        	   Log.e("ysy", tempstring);
		        	   if(checkPin(tempstring))
		        	   {
		        		   state = CHOOSECUSTOMER;
		        		   UserMasterDB umdb = new UserMasterDB();
		        		   umdb.openDB();
		        		ArrayList<UserMaster> aList= umdb.customerNameList(stateuserid);
		        		   umdb.closeDB();
		        		   showlistdialog(state,aList);
		        	   }
		        	   else
		        	   {
		        		   state = ENTERUSERID;
		        		   
		        		   showdialog(ENTERUSERID);
		        	   }
		 //       	   this.state = ENTERPIN;
		  //      	   showdialog(ENTERPIN);
		           }
		       }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 
					//User clicked cancel button
					
				}
			}).show();
			break;
		}
		case CHOOSERETURNABLE:
		{
			state = COSTCODE;
			
			LayoutInflater li2 = LayoutInflater.from(this);
			View promptsView2 = li2.inflate(R.layout.simpledialoglayout, null);
	  	  	final EditText et2   = (EditText)promptsView2.findViewById(R.id.editTextDialogUserInput);
	  	  	et2.setKeyListener(new DigitsKeyListener(false, true));
			
			
			ad.setTitle("Scan/Enter Shipto or Cost code").setView(promptsView);
			ad.setPositiveButton("ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// 
					//use the database to confirm the shipto number
					String tempString = et2.getText().toString();
					if(checkShiptoNum(tempString))
					{
		//				checkautocrib;
					}
					else
					{
	//					Toast.makeText(this, " is not exist in database", Toast.LENGTH_LONG).show();
					}
				}
			}).show();
			break;
		}
//		case CHOOSESHIPTONUMBER:
		}	
	//	ad.setTitle(title)

	}
	
	public void showTimeDialog()
	{
		
		DatePickerDialog.OnDateSetListener dateListener =   
			    new DatePickerDialog.OnDateSetListener() {  
			        @Override  
			        public void onDateSet(DatePicker datePicker,   
			                int year, int month, int dayOfMonth) {  
//			            EditText editText =   
//			                (EditText) findViewById(R.id.editText);  
//			             //Calendar月份是从0开始,所以month要加1  
//			            editText.setText("你选择了" + year + "年" +   
//			                    (month+1) + "月" + dayOfMonth + "日");  
			        //	dialog.cancel();
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
	

	
	public void showTwoButtonDialog(String title)
	{
		switch (state) {
		case CHOOSECUSTOMER:
		{
			state = CHOOSERETURNABLE;
			if(statereturnable == "Y")
			{
				//whether it can be returned?
				
				//showTwoButtonDialog("Returnable transaction?");
				showdialog(state);
			}
			else if (statereturnable == "N") {
				//
				
			}
			new AlertDialog.Builder(this).setTitle(title).setPositiveButton("yes", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					statereturnable = "Y";
					showdialog(state);
				}
			}).setNegativeButton("No",new DialogInterface.OnClickListener()
					{

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 
							statereturnable = "N";
						}
				
					}).show();
			break;
		}
		default:
			break;
		}

	}
	
	public void showlistdialog(int state, ArrayList<UserMaster> alist)
	{
		final ArrayList<UserMaster> list = alist;
		final String[] st = new String[list.size()];
		switch (state) {
		case CHOOSECUSTOMER:
		{
			for(int i = 0 ; i < list.size(); i++)
			{
				st[i] = list.get(i).getCustName();
			}
			new AlertDialog.Builder(this).setTitle("Choose customerName").setItems(st,
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 
							Log.e("ysy",st[which]);
							
							//find the costomer in the  usermaster to see whether it is returnable customer master 
							UserMasterDB umdb = new UserMasterDB();
							umdb.openDB();
							statecustomer = list.get(which).getCustomer();
							statereturnable = umdb.returnDBString("usermaster", "UserID", stateuserid, "Customer", list.get(which).getCustomer(), "Returnable");
							Log.e("ysy", "returnable " + statereturnable);
							if(statereturnable == "Y")
							{
								//whether it can be returned?
								
								showTwoButtonDialog("Returnable transaction?");
							}
							else if (statereturnable == "N") {
								//
								
							}
//							showTwoButtonDialog("Returnable transaction?");
							umdb.closeDB();
							
						}
					}
					).show();
			break;
		}
		default:
			break;
		}
		
		{
			UserMasterDB umdb = new UserMasterDB();
			umdb.openDB();
	//		String stateAutoCrib = umdb.returnDBString("customermaster", "Customer", statecustomer, "", row2, returncolumn)
		}

	}

	void insertOutputDBPlus(Output output)
	{
		UserMasterDB umdb = new UserMasterDB();
		umdb.openDB();
		umdb.insertOutputDB(output);
		umdb.closeDB();
	}
	
	void OutputHistoryPlus()
	{
		UserMasterDB umdb = new UserMasterDB();
		umdb.openDB();
		List<String> list = umdb.OutputHistory();
		for(int i=0;i<list.size();i++)
		{
			Log.e("ysy", list.get(i));
		}
		umdb.closeDB();
	}

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
	
	boolean checkShiptoNum(String shipto)
	{
		Log.e("ysy", "chechshiptonum");
		UserMasterDB umdb = new UserMasterDB();
		umdb.openDB();
		boolean tpstate = umdb.checkexit("customermaster", shipto,"ShiptoNumber" );
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
	

	/*
	public void bluetooth()
	{
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
		//this cannot be use in 2.3.3
		intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
//		registerReceiver(search, filter)
		uuid = UUID.randomUUID();
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(mBluetoothAdapter == null)
		{
			//this will change to an alert dialog
			Log.e("ysy", "this service do not support bluetooth");
		}
		
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		if(pairedDevices.size()<=0)
		{
			showToast("can not find bluetooth device,please open the bluetooth");
			return;
		}
		final String[] st = new String[pairedDevices.size()];
		if(pairedDevices.size() > 0)
		{
			
			
			
			int i=0;
			for(BluetoothDevice device : pairedDevices)
			{
				st[i] = device.getName();
			}
		}


		   // Create a BroadcastReceiver for ACTION_FOUND
		final BroadcastReceiver mReceiver = new BroadcastReceiver() {
		    public void onReceive(Context context, Intent intent) {
		        String action = intent.getAction();
		        // When discovery finds a device
		        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
		            // Get the BluetoothDevice object from the Intent
		            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		            // Add the name and address to an array adapter to show in a ListView
		       //     mArrayAdapter.add(device.getName() + "n" + device.getAddress());
		        }
		    }
		};
		
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
	
		
		
		


		bluetoothGetMessage();
		ConnectThread clientThread = null;
		if(pairedDevices.size() > 0)
		{
			int i=0;
			for(BluetoothDevice device : pairedDevices)
			{
				Log.e("ysy", "for loop");
				if(i==bluetoothdevice)
				{
					clientThread = new ConnectThread(device);
					break;
				}
			}
//			for(int i = 0; i< pairedDevices.size();i++)
//			{
//				clientThread = new ConnectThread(pairedDevices.)
//			}
		}
		clientThread.start();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//			//semp.acquire();
//			
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		

		Handler handler = new Handler()
		{
			
		    public void handleMessage(Message msg) {
		    	Log.e("ysy", "msg"+ msg.what);
	        	tempstring = msg.obj.toString();
	        	tempstring = tempstring.trim();
		        switch (msg.what) {
		        case 1: //state 1
		        {
		     //   	tempstring = msg.obj.toString();
		        	enteruseridplus(tempstring);
		        	break;
		        }
		        case 2:
		        {
		        	Log.e("ysy", "enterpin");
		        //	tempstring = msg.obj.toString();
		        	enteruserpinplus(tempstring);

		        	break;
		        }
		        case 4:
		        {
		       // 	tempstring = msg.obj.toString();
		        	entershiptoplus(tempstring);
		        	break;
		        }
		        case 5:
		        {
		         //	tempstring = msg.obj.toString();
		        	workordernumberplus(tempstring);
		        	break;
		        }
		        case 6:
		        {
		     //   	tempstring = msg.obj.toString();
		        	scanitemplus(tempstring);
		        	break;
		        }
		      }
			    super.handleMessage(msg); 
		    }
		};
		ConnectedThread connectedThread = new ConnectedThread(clientThread.mmSocket,handler);
		connectedThread.start();	

	}

*/

}

