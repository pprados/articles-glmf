package org.monapk;

import java.io.File;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MonApkActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button button=new Button(this);
        button.setText("Install");
        button.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				install();
			}
		});
        setContentView(button);
    }
    
    void install()
    {
    	final String fileName="/sdcard/test.apk";
    	if (checkPermission("android.permission.INSTALL_PACKAGES", 
    		    android.os.Process.myPid(), 
    		    android.os.Process.myUid())==PackageManager.PERMISSION_GRANTED)
    	{
    		// Installation silencieuse
    		IPackageInstallObserver obs=new IPackageInstallObserver.Stub()
    		{
    		  @Override
    		  public void packageInstalled(String packageName, int returnCode)
    		    throws RemoteException
    		  {
    		      if (returnCode==1)
    		      {
    		        // Cool, le package est installé !
    		    	  System.out.println("Le package est installé");
    		      }
    		  }

    		};
    		getPackageManager().installPackage(
    		  Uri.fromFile(new File(fileName)), obs, 0, 
    		  getPackageName());    		
    	}
    	else
    	{
	    	IntentFilter filter = new IntentFilter();
	    	filter.addAction(Intent.ACTION_PACKAGE_ADDED);
	    	filter.addAction(Intent.ACTION_PACKAGE_INSTALL);
	    	filter.addDataScheme("package");
	    	final BroadcastReceiver packageListener=new BroadcastReceiver()
	    	{
	    	  @Override
	    	  public void onReceive(Context context, Intent intent) 
	    	  {
	    	    final String action=intent.getAction();
	    	    if (Intent.ACTION_PACKAGE_ADDED.equals(action))
	    	    {
	    	      // Cool, il a accepté
	    	      // TODO :...
	    	      InstallApkActivity.accept=true;
	    	    }
	    	  }
	    	};
	    	registerReceiver(packageListener, filter);
	    	InstallApkActivity.accept=false;
			Intent intent = new Intent(this,InstallApkActivity.class);
			intent.putExtra(InstallApkActivity.EXTRA_FILENAME, fileName);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_FROM_BACKGROUND);
			startActivity(intent);	
	    }
    }
}