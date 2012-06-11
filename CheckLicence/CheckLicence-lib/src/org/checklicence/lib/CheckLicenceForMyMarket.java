package org.checklicence.lib;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


import dalvik.system.DexClassLoader;
import android.content.Context;
import android.content.pm.PackageInfo;

public abstract class CheckLicenceForMyMarket
{
	// API publique pour les applications
	public abstract boolean checkLicenceForMyMarket();
	
	private static CheckLicenceForMyMarket mSingleton;
	
    public static synchronized CheckLicenceForMyMarket getManager(Context context)
    {
		
		if (mSingleton==null)
		{
			try
			{
	    		ClassLoader classLoader=CheckLicenceForMyMarket.class.getClassLoader();
	    		if (USE_SHAREDLIB)
	    		{
					File dir=context.getApplicationContext().getDir("dexopt", Context.MODE_PRIVATE); 
					final String packageName="org.checklicence";
					PackageInfo info=context.getPackageManager().getPackageInfo(packageName, 0);
					String jar=info.applicationInfo.dataDir+"/files/"+SHARED_LIB; 
					InputStream in=new FileInputStream(jar); in.read(); in.close(); // Check if is readable
					classLoader=
						new DexClassLoader(jar,
								dir.getAbsoluteFile().getAbsolutePath(),null,
								classLoader
								);
	    		}
				mSingleton=(CheckLicenceForMyMarket)classLoader.loadClass(BOOTSTRAP_CLASS)
					.getConstructor(Context.class).newInstance(context);
			}
			catch (Exception e)
			{
				throw new Error("Install the Remote Android package",e);
			}
		}
		return mSingleton;
	}
	
	/** Bootstrap implementation. */
	private static final String BOOTSTRAP_CLASS="org.checklicence.lib.shared.CheckLicenceForMyMarketImpl";
	static final boolean USE_SHAREDLIB=true;
	static final String SHARED_LIB="sharedlib.jar";
}
