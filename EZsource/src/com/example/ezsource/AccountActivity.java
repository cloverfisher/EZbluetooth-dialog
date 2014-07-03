package com.example.ezsource;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

public class AccountActivity extends Activity {

	AlertDialog.Builder ad;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ad =new AlertDialog.Builder(this);
		setContentView(R.layout.activity_costomercode);
		
		final TextView tv = (TextView)findViewById(R.id.costomercode);
		TextView tv0 = (TextView)findViewById(R.id.prftext);
		tv0.setText("output email");	
		SharedPreferences email = getSharedPreferences("outputemail", 0);
		final String emailString = email.getString("outputemail", "ezsourcesending@gmail.com");
		if(emailString.equals("ezsourcesending@gmail.com"))
			tv.setText("ezsourcesending@gmail.com");
		else {
			tv.setText(emailString);
		}
		
		Button button = (Button)findViewById(R.id.resetbutton);
		button.setOnClickListener(new View.OnClickListener() {

		
		//String test = eText.getText()
		
		
			@Override
			public void onClick(View v) {
				LayoutInflater li = LayoutInflater.from(AccountActivity.this);//.from(this);
				View promptsView = li.inflate(R.layout.costomerdialog, null);
				final EditText eText = (EditText)promptsView.findViewById(R.id.editTextcostomercode);
				ad.setTitle("enter the outputemail").setView(promptsView).setPositiveButton("ok", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String outputemailString = eText.getText().toString();
						SharedPreferences setting = getSharedPreferences("outputemail", 0);
						SharedPreferences.Editor editor = setting.edit();
						editor.putString("outputemail", outputemailString);
						editor.commit();
						Log.e("ysy", ""  + outputemailString);
						tv.setText(outputemailString);
					}
				}).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						
					}
				}).show();
				
				
			}
		});
	}


  public void showToast(final String toast) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
      }
    });
  }
}
