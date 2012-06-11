package fr.prados.android.c2dm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


// Service déclanché lors du démarrage du device.
// Lance le service C2DM car il y a une tache de fond à déclencher pour recevoir les évènements.
public class BootAutoStarter extends BroadcastReceiver 
{
    @Override
    public void onReceive(Context context, Intent intent) 
    {
		context.startService(new Intent(context,C2DMService.class));
    }

}