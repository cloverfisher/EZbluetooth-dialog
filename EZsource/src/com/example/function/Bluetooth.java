package com.example.function;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;



import java.util.concurrent.Semaphore;

import android.app.AlertDialog;
import android.app.Application;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class Bluetooth extends Application{

	BluetoothAdapter mBluetoothAdapter;
	UUID uuid;
	int state =0;//automata
	String tempstring;
	Semaphore semp = new Semaphore(0);

	public String bluetoothGetMessage(){
		String tempStr = null;
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(mBluetoothAdapter == null){
//			new AlertDialog.Builder(TransactionActivity.this).setTitle("Alert")
//			.setMessage("no bluetooth").show();
			//TODO show alert
		}
/*		if(!mBluetoothAdapter.isEnabled()){
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}*/
		return tempStr;
	}
	/*
	 * bluetooth use as client*/
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
	            try {
	                mmSocket.close();
	            } catch (IOException closeException) { }
	            return;
	        }

	        // Do work to manage the connection (in a separate thread)
	//        manageConnectedSocket(mmSocket);
	    }

	    /*  Will cancel an in-progress connection, and close the socket */
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }
	}
	
	/*
	 * data transmit
	 * */
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
/*	                Handler mHandler = new Handler();
	                // Send the obtained bytes to the UI activity*/
//	                mmhandler.obtainMessage(TransactionActivity.DATA_RECEIVE,data)
//	                        .sendToTarget();
	                mmhandler.obtainMessage(state, data).sendToTarget();
	            } catch (IOException e) {
	            	Log.e("ysy", e.getMessage());
	                break;
	            }
	        }
	    }

	    /*  Call this from the main activity to send data to the remote device */
	    public void write(byte[] bytes) {
	        try {
	            mmOutStream.write(bytes);
	        } catch (IOException e) { }
	    }

	    /*  Call this from the main activity to shutdown the connection */
	    public void cancel() {
	        try {
	            mmSocket.close();
	        } catch (IOException e) { }
	    }
	}
	
	public void bluetooth()
	{
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
		intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
	//	intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
//		registerReceiver(search, filter)
		uuid = UUID.randomUUID();
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if(mBluetoothAdapter == null)
		{
			//this will change to an alert dialog
			Log.e("ysy", "this service do not support bluetooth");
		}
		
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		if(pairedDevices.size() > 0)
		{
			for(BluetoothDevice device : pairedDevices)
			{
				
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
			for(BluetoothDevice device : pairedDevices)
			{
				Log.e("ysy", "for loop");
				clientThread = new ConnectThread(device);
			}
//			for(int i = 0; i< pairedDevices.size();i++)
//			{
//				clientThread = new ConnectThread(pairedDevices.)
//			}
		}
		clientThread.start();

		try {
			semp.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * Handler
		 * */
//		Handler handler = new Handler()
//		{
//			
//		    public void handleMessage(Message msg) {
//		    	Log.e("ysy", "msg"+ msg.what);
//		        switch (msg.what) {
//		        case 1: //state 1
//		        {
//		        	tempstring = msg.obj.toString();
//		        	enteruseridplus(tempstring);
//		        	break;
//		        }
//		        case 2:
//		        {
//		        	Log.e("ysy", "enterpin");
//		        	tempstring = msg.obj.toString();
//		        	enteruserpinplus(tempstring);
//
//		        	break;
//		        }
//		        case 4:
//		        {
//		        	tempstring = msg.obj.toString();
//		        	entershiptoplus(tempstring);
//		        	break;
//		        }
//		        case 5:
//		        {
//		         	tempstring = msg.obj.toString();
//		        	workordernumberplus(tempstring);
//		        	break;
//		        }
//		        case 6:
//		        {
//		        	tempstring = msg.obj.toString();
//		        	scanitemplus(tempstring);
//		        	break;
//		        }
//		      }
//			    super.handleMessage(msg); 
//		    }
//		};
//		ConnectedThread connectedThread = new ConnectedThread(clientThread.mmSocket,handler);
//		connectedThread.start();	

	}	
	

}
