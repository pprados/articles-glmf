package org.checklicence.test;

import org.checklicence.lib.CheckLicenceForMyMarket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TestCheckLicenceActivity extends Activity 
{
	private CheckLicenceForMyMarket mLicenceManager;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        mLicenceManager=CheckLicenceForMyMarket.getManager(this);
        
        Button button=new Button(this);
        button.setText("Check licence");
        button.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				checkLicence();
			}
		});
        setContentView(button);
    }
    private void checkLicence()
    {
    	boolean licence=mLicenceManager.checkLicenceForMyMarket();
    	new AlertDialog.Builder(this)
	        .setMessage("Licence ="+licence)
	        .setOnCancelListener(
	        		new AlertDialog.OnCancelListener() 
	        		{
	        			public void onCancel(DialogInterface dialog) 
	        			{
	        				finish();
	        			}
	        		})
	        .show();
    }
}