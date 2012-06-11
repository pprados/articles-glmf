package com.renault.c2dm.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.renault.c2dm.demo.Config;

// Le receveur de messages C2DM.
// Pour la documentation, consultez la doc de Google sur C2DM.
public class C2DMReceiver extends BroadcastReceiver
{
	private static final String TAG="C2DMReceiver";
	@Override
	public void onReceive(Context context, Intent intent) {
		if (Config.REGISTRATION.equals(intent.getAction()))
		{
			// Suivant le message, traite l'erreur, le désenregistrement ou l'enregistrement de l'application
			String registrationId=intent.getStringExtra("registration_id");
			if (intent.getStringExtra("error")!=null)
			{
				handleRegistrationError();
				return;
			}
			if (intent.getStringExtra("unregister")!=null)
			{
				handleUnregistered();
				return;
			}
			handleRegistration(registrationId);
		}
		else if (Config.RECEIVE.equals(intent.getAction()))
		{
			handleReceive(intent.getExtras());
		}
	}
	// Gestion de l'enregistrement
	// registrationId doit être propagé au serveur applicatif pour que ce dernier
	// puisse envoyer des messages au terminal.
	private void handleRegistration(String registrationId)
	{
		Log.d(TAG,"handle registration id="+registrationId);
	}
	// Gestion d'erreur
	private void handleRegistrationError()
	{
		Log.d(TAG,"handle registration error");
	}
	// Gestion du désenregistrement
	private void handleUnregistered()
	{
		Log.d(TAG,"handle unregistered");
	}
	// Gestion de la reception d'un message.
	// Le bundle possède un ensemble de clefs/valeurs
	private void handleReceive(Bundle bundle)
	{
		Log.d(TAG,"handle receive "+bundle);
	}
}
