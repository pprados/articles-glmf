package org.monapk;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

public class InstallApkActivity extends Activity
{
  public static final String EXTRA_FILENAME="filename";
  
  public static boolean accept=false;
  
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
   // Keep alive
    PackageManager packageManager=getPackageManager();
    packageManager.setComponentEnabledSetting(getComponentName(),
      PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
      PackageManager.DONT_KILL_APP);
    startRealInstallApkActivity();
  }

  private void startRealInstallApkActivity()
  {
    final Intent intent = new Intent(Intent.ACTION_VIEW);
    String filename=getIntent().getStringExtra(EXTRA_FILENAME);
    intent.setDataAndType(Uri.fromFile(new File(filename)), 
      "application/vnd.android.package-archive");
    intent.setFlags(Intent.FLAG_FROM_BACKGROUND);
    startActivity(intent);
  }
  @Override
  protected void onRestart()
  {
    super.onRestart();
    finish();
  }
  @Override
  protected void onDestroy()
  {
    super.onDestroy();
    if (!accept)
    {
        // Il refuse l'installation
    	//TODO ...
    }
  }
}
