package fr.prados.android.c2dm.demo;

import fr.prados.android.c2dm.demo.Config;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


// Le receveur de messages C2DM.
// Pour la documentation, consultez la doc de Google sur C2DM.
public class C2DMReceiver extends BroadcastReceiver
{
	private static final String TAG="DemoC2DM";
	
	private Context mContext;
	
	@Override
	public void onReceive(Context context, Intent intent) 
	{
		mContext=context;
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
			handleRegistration(context,registrationId);
		}
		else if (Config.RECEIVE.equals(intent.getAction()))
		{
			handleReceive(intent.getExtras());
		}
	}
	// Gestion de l'enregistrement
	// registrationId doit être propagé au serveur applicatif pour que ce dernier
	// puisse envoyer des messages au terminal.
	private void handleRegistration(Context context,String registrationId)
	{
		Log.d(TAG,"registration id="+registrationId);
        final SharedPreferences prefs = context.getSharedPreferences(
                Config.PREFERENCE,
                Context.MODE_PRIVATE);
        prefs.edit().putString("registration_id", registrationId).commit();
		Toast.makeText(mContext, "Registered", Toast.LENGTH_LONG).show();
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
		String message = bundle.getString("message");
		Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
	}
}
