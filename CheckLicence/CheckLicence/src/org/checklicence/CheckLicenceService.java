package org.checklicence;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CheckLicenceService extends Service
{

	@Override
	public IBinder onBind(Intent intent)
	{
		return new CheckLicenceImpl();
	}

}
