package org.checklicence.lib.shared;

import org.checklicence.lib.CheckLicenceForMyMarket;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

public class CheckLicenceForMyMarketImpl extends CheckLicenceForMyMarket
{
	private static final String ACTION_LICENCE="org.checklicence.LICENCE";
	private ICheckLicence mRemoteLicence;
	public CheckLicenceForMyMarketImpl(Context context)
	{
		final Intent intent=new Intent(ACTION_LICENCE);
		context.bindService(intent, new ServiceConnection()
		{
			
			@Override
			public void onServiceDisconnected(ComponentName name)
			{
				mRemoteLicence=null;
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder binder)
			{
				mRemoteLicence=ICheckLicence.Stub.asInterface(binder);
			}
		}, Context.BIND_AUTO_CREATE);
	}

	public boolean checkLicenceForMyMarket()
	{
		try
		{
			return mRemoteLicence.checkLicence();
		}
		catch (RemoteException e)
		{
			throw new Error(e);
		}
	}

}
