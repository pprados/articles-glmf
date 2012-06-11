package org.checklicence;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class MyApplication extends Application
{
	private static final String TAG="app";
	private static final boolean USE_SHAREDLIB=true;
	private static final String SHARED_LIB="sharedlib.jar";
	
	@Override
	public void onCreate()
	{
		super.onCreate();
		if (USE_SHAREDLIB)
		{
			// Copy a public version of shared library
			new Thread("Copy shared library")
			{
				public void run() 
				{
					//TODO: ecrire dans un fichier temporaire avant validation
					final SharedPreferences prefs=getSharedPreferences("sharedlib",Context.MODE_PRIVATE);
					final long lastCopied=prefs.getLong("copy", -1);
					final long packageLastModified=new File(getApplicationInfo().publicSourceDir).lastModified();
					if (packageLastModified>lastCopied)
					{
						InputStream in=null;
						OutputStream out=null;
						try
						{
							in=getAssets().open(SHARED_LIB);
							out=openFileOutput(SHARED_LIB, Context.MODE_WORLD_READABLE);
							byte[] buf=new byte[1024*4];
							for (;;)
							{
								int s=in.read(buf);
								if (s<1) break;
								out.write(buf,0,s);
							}
							prefs.edit().putLong("copy",packageLastModified).commit();
						}
						catch (IOException e)
						{
							Log.e(TAG,"Impossible to copy shared library",e);
						}
						finally
						{
							if (in!=null)
							{
								try
								{
									in.close();
								} 
								catch (IOException e)
								{
									Log.e(TAG,"Impossible to close input stream",e);
								}
								try
								{
									out.close();
								} 
								catch (IOException e)
								{
									Log.e(TAG,"Impossible to close input stream",e);
								}
							}
						}
					}
				}
			}.start();
			
		}
	}
}
