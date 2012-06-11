package com.renault.c2dm.demo;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.util.Log;
import android.view.View;

// Cette démonstration utilise l'implémentation de C2DM de Renault.
// Les APIs sont compatibles avec l'approche de Google.
public class DemoC2DMActivity extends Activity 
implements View.OnClickListener
{
	private static final String TAG="DemoC2DM";
	
	private static final String SENDER_ID="caravan@renault.com";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ((Button)findViewById(R.id.Register)).setOnClickListener(this);
    }
	@Override
	public void onClick(View view)
	{
		if (view==findViewById(R.id.Register))
			handleRegister();
	}
	
	// Sur clic du bouton, déclanche l'enregistrement
	private void handleRegister()
	{
		Intent registrationIntent=new Intent(Config.REGISTER);
		registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
		registrationIntent.putExtra("sender", SENDER_ID); // L'email du compte applicatif
		startService(registrationIntent);
		Log.d(TAG,"registration stated");
		// L'enregistrement est effectif lors de la réception d'un broadcast
	}
}