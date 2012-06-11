package fr.prados.android.c2dm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

// Le service C2DM. Permet d'enregister un téléphone sur le servuer C2DM,
// d'écouter les évènements pour les propages en broadcast aux applications.
public class C2DMService extends IntentService 
{
	public C2DMService()
	{
		super("C2DMService");
	}
	private static final String TAG="C2DMService";

	// Base des clefs utilisées pour le service C2DM Simulator.
	static final String BASE="fr.prados.android";
	
	// Intention pour enregistrer une application pour un téléphone
	static final String REGISTER=BASE+".c2dm.intent.REGISTER";
	
	// Intention pour désenregister une application pour un téléphone
	static final String UNREGISTER=BASE+".c2dm.intent.UNREGISTER";
	
	// Intention envoyé à l'application lorsque l'application est enregistré sur le serveur C2DM.
	// Cette intention peut être envoyé plusieurs fois avec des valeurs différentes
	static final String REGISTRATION=BASE+".c2dm.intent.REGISTRATION";
	
	// Intention envoyé à l'application lors de la réception d'un message depuis le serveur C2DM
	static final String RECEIVE=BASE+".c2dm.intent.RECEIVE";
	
	// Permission donnant le droit envoyer un message C2DM
	static final String PERMISSION_SEND=BASE+".c2dm.permission.SEND";
	
	// Permission donnant le droit de recevoir un message C2DM
	static final String PERMISSION_RECEIVE=BASE+".c2dm.permission.RECEIVE";
	
	// Le host du serveur C2DM. Utilise un serveur Web sur le poste du développeur. Utilisable depuis un émulateur.
	private static final String C2DM_HOST="http://10.0.2.2:8888/";
	// L'URL pour demander un enregistrement d'une application pour un téléphone.
	private static final String C2DM_REGISTER_URL=C2DM_HOST+"register";
	// L'URL pour demander le désenregistrement d'une application pour un téléphone.
	private static final String C2DM_UNREGISTER_URL=C2DM_HOST+"unregister";
	// L'URL de pool permettant de vérifier la présence d'un message.
	private static final String C2DM_POOL_URL=C2DM_HOST+"pool";
	// L'URL permettant de demander la propagation d'un message à un téléphone particulier, identifié par son registration id.
	private static final String C2DM_POST_URL=C2DM_HOST+"post";
	
	// La liste des registrations ID récupérées. 
	// Cette liste n'est pas nettoyé lors du désenregistrement, ni persistante à l'application.
	// C'est une implémentation temporaire rapide.
	private ArrayList<String> mListId=new ArrayList<String>();
	
	// Le service de pooling pour vérifier la présence de nouveaux messages
	class C2DMPooling implements Runnable 
	{
		// La période de consultation.
		private static final int TIME_PERIOD=30000; // 30 secondes
		public void run() 
		{
			for (;;)
			{
				try
				{
					synchronized (this) 
					{
						wait(TIME_PERIOD);
						poolMessages();
					}
				} 
				catch (Exception e) 
				{
					// Ignore
				} 
			}
		}
	}

	private Thread mPooling;
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
		// Création et déclanchement du pool
		if (mPooling==null)
		{
			mPooling=new Thread(new C2DMPooling(),"C2DM Pooling");
			mPooling.setDaemon(true);
			mPooling.start();
		}
	}
	@Override
	public IBinder onBind(Intent intent)
	{
		Log.d(TAG,"onBind");
		return null;
	}
	// Invoqué lors des startService par les applications lors de l'enregistrement ou le désenregistrement
	// Pour le détail de l'API voir la document de C2DM de Google.
	@Override
	protected void onHandleIntent(Intent intent) 
	{
		try
		{
			if (REGISTER.equals(intent.getAction()))
			{
				String sender=intent.getStringExtra("sender");
				PendingIntent pendingIntent=intent.getParcelableExtra("app");
				register(pendingIntent,sender);
			}
			else if (UNREGISTER.equals(intent.getAction()))
			{
				PendingIntent pendingIntent=intent.getExtras().getParcelable("app");
				unregister(pendingIntent);
			}
		}
		catch (IOException e)
		{
			// Ignore
			e.printStackTrace();
		}
	}
	
	// Enregistrement d'une application pour ce téléphone.
	// Le pendingIntent sert à connaitre l'application à l'origine de l'enregistrement.
	// Le sender est le mail du compte applicatif sur le serveur C2DM.
	private void register(PendingIntent pendingIntent,String sender) throws IOException
	{
		String applicationId=pendingIntent.getTargetPackage();
		
		// Soumet l'enregistrement au serveur C2DM.
		// Deux parametres dans l'URL : 
		// - app avec l'application id, 
		// - sender avec le mail du compte.
		URL url=new URL(C2DM_REGISTER_URL
			+"?application_id="+URLEncoder.encode(applicationId)
			+"&sender="+URLEncoder.encode(sender));
		// Invocation de la requete et lecture de la réponse.
		// Pas de gestion d'erreur pour le moment.
		// Format attendu : 
		// - une ligne avec l'erreur eventuelle.
		// - une ligne avec le registration id.
		BufferedReader reader=new BufferedReader(new InputStreamReader(url.openStream()));
		String status=reader.readLine();
		String registration_id=reader.readLine();
		reader.close();
		
		boolean error=false;
		if (status.length()!=0)
		{
			error=true;		// Simulation d'une erreur
		}

		mListId.add(registration_id);
		
		// Broadcast intent aux applications.
		// Elles doivent vérifier que le sender possède 
		// le privilège fr.prados.android.c2dm.permission.SEND
		Intent broadcast=new Intent(REGISTRATION);
		broadcast.putExtra("registration_id", registration_id);
		
		if (error)
		{
			broadcast.putExtra("error","true");
		}
//		if (unregister)
//		{
//			broadcast.putExtra("unregistered","true");
//		}
		sendBroadcast(broadcast,PERMISSION_RECEIVE);
	}
	
	// Désenregistrement. Non codé pour le moment.
	private void unregister(PendingIntent pendingIntent) throws IOException
	{
		Log.d(TAG,"unregister");
		String applicationId=pendingIntent.getTargetPackage();
		URL url=new URL(C2DM_UNREGISTER_URL+"?app="+URLEncoder.encode(applicationId));
		BufferedReader reader=new BufferedReader(new InputStreamReader(url.openStream()));
		reader.close();
	}
	
	// Service invoqué périodiquement pour vérifier la présence de nouveaux messages.
	// Cette approche est très mauvaise pour une utilisation en mobilité.
	// Mais permet de présenter un code court pour un article.
	private synchronized void poolMessages() throws IOException
	{
		Log.d(TAG,"pool Messages");
		for (String id:mListId)
		{
			Intent broadcast=new Intent(RECEIVE);
			// Format de l'URL: un paramètre registration_id avec la chaine récupéré lors d'un enregistrement
			URL url=new URL(C2DM_POOL_URL+"?registration_id="+URLEncoder.encode(id));
			// Réponse attendu:
			// La première ligne avec l'application ID associé
			// Les lignes suivantes au format key;valeur
			BufferedReader reader=new BufferedReader(new InputStreamReader(url.openStream()));
			String line=null;
			boolean first=true;
			String applicationId=null;
			while ((line=reader.readLine())!=null)
			{
				if (first)
				{
					first=false;
					applicationId=line;
				}
				else
				{
					int idx=line.indexOf('=');
					if (idx==-1) break;
					String key=line.substring(0,idx);
					String value=line.substring(idx+1);
					broadcast.putExtra(key, value);
				}
			}
			reader.close();
			// Si l'analyse de la réponse est conforme, 
			if ((applicationId!=null) && broadcast.getExtras()!=null && !broadcast.getExtras().isEmpty())
			{
				// Broadcast le message aux destinataires ayant le privilège
				// applicationId+".permission.C2DM_MESSAGE"
				sendBroadcast(broadcast,applicationId+".permission.C2D_MESSAGE");
			}
		}
	}
}
