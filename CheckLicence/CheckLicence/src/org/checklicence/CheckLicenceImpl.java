package org.checklicence;

import org.checklicence.lib.shared.ICheckLicence;

import android.os.RemoteException;

public class CheckLicenceImpl extends ICheckLicence.Stub
{

	@Override
	public boolean checkLicence() throws RemoteException
	{
		// TODO Vérifier la licence de l'utilisateur
		return true;
	}

}
