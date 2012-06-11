package fr.prados.android.c2dm.demo;

import static fr.prados.android.c2dm.demo.Config.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import fr.prados.android.c2dm.demo.R;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

// Cette démonstration utilise l'implémentation de C2DM Simulator.
// Les APIs sont compatibles avec l'approche de Google.
public class DemoC2DMActivity extends Activity 
implements View.OnClickListener
{
	private static final String TAG="DemoC2DM";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ((Button)findViewById(R.id.Register)).setOnClickListener(this);
        ((Button)findViewById(R.id.Send)).setOnClickListener(this);

        
    }
	@Override
	public void onClick(View view)
	{
		if (view==findViewById(R.id.Register))
			handleRegister();
		else
			sendEvent("Hello from C2DM");
	}
	
	// Sur clic du bouton, déclanche l'enregistrement
	private void handleRegister()
	{
		Intent registrationIntent=new Intent(Config.REGISTER);
		registrationIntent.putExtra("app", PendingIntent.getBroadcast(this, 0, new Intent(), 0));
		registrationIntent.putExtra("sender", SENDER_ID); // L'email du compte applicatif
		startService(registrationIntent);
		
		Log.d(TAG,"registration stated");
	}
	
	// Sur certains Android, bug de gestion des certificats avec un nom comportant une étoile
	// Issue 17680 http://code.google.com/p/android/issues/detail?id=17680
//	static
//	{
//		try
//		{
//			TrustManager[] trustAllCerts = new TrustManager[]
//		   	{
//		   	    new X509TrustManager()
//		   		{
//		   			
//		   			@Override
//		   			public X509Certificate[] getAcceptedIssuers()
//		   			{
//		   				return null;
//		   			}
//		   			
//		   			@Override
//		   			public void checkServerTrusted(
//		   					X509Certificate[] paramArrayOfX509Certificate, String paramString)
//		   					throws CertificateException
//		   			{
//		   			}
//		   			
//		   			@Override
//		   			public void checkClientTrusted(
//		   					X509Certificate[] paramArrayOfX509Certificate, String paramString)
//		   					throws CertificateException
//		   			{
//		   			}
//		   		}
//		   	};
//			// Install the all-trusting trust manager
//		    SSLContext sc = SSLContext.getInstance("TLS");
//		    sc.init(null, trustAllCerts, new java.security.SecureRandom());
//		    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//	}

	// Now you can access an https URL without having the certificate in the truststore
	private String getAuthToken()
	{
		try
		{
		    StringBuilder postDataBuilder = new StringBuilder();
			postDataBuilder
				.append("Email=").append(SENDER_ID)
				.append("&Passwd=").append(SENDER_PASSWD)
				.append("&accountType=GOOGLE")
				.append("&service=ac2dm");

			byte[] postData = postDataBuilder.toString().getBytes("UTF-8");

	        // Hit the dm URL.

	        URL url = new URL(POST_REGISTER);
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        conn.setDoOutput(true);
	        conn.setUseCaches(false);
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
	        conn.setRequestProperty("Content-Length", Integer.toString(postData.length));

	        OutputStream out = conn.getOutputStream();
	        out.write(postData);
	        out.close();

	        int responseCode = conn.getResponseCode();

	        if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED ||
	                responseCode == HttpURLConnection.HTTP_FORBIDDEN) 
	        {
	            throw new IOException("Unauthorized.");
	        }

	        BufferedReader reader=new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        String responseLine;
	        responseLine=reader.readLine();
	        responseLine=reader.readLine();
	        responseLine = reader.readLine();

	        String[] responseParts = responseLine.split("=", 2);
	        return responseParts[1];
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	private void updateToken(String token)
	{
		
	}
	private void invalidateCachedToken()
	{
		
	}
	private boolean sendEvent(String message)
	{
		try
		{
	        String authToken = getAuthToken();

	        final String registrationid=getSharedPreferences(
	                Config.PREFERENCE,
	                Context.MODE_PRIVATE).getString("registration_id", "");

	        StringBuilder postDataBuilder = new StringBuilder();
			postDataBuilder
				.append("registration_id=").append(registrationid)
				.append("&collapse_key=none")
				.append("&data.message="+message)
				.append("&delay_while_idle=1");

			byte[] postData = postDataBuilder.toString().getBytes("UTF-8");

	        // Hit the dm URL.

	        URL url = new URL(POST_URL);
	        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
	        conn.setDoOutput(true);
	        conn.setUseCaches(false);
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
	        conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
	        conn.setRequestProperty("Authorization", "GoogleLogin auth=" + authToken);

	        OutputStream out = conn.getOutputStream();
	        out.write(postData);
	        out.close();

	        int responseCode = conn.getResponseCode();

	        if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED ||
	                responseCode == HttpURLConnection.HTTP_FORBIDDEN) 
	        {
	            // The token is too old - return false to retry later, will fetch the token
	            // from DB. This happens if the password is changed or token expires. Either admin
	            // is updating the token, or Update-Client-Auth was received by another server,
	            // and next retry will get the good one from database.
	            Log.w(TAG,"Unauthorized - need token");
	            invalidateCachedToken();
	            return false;
	        }

	        // Check for updated token header
	        String updatedAuthToken = conn.getHeaderField("Update-Client-Auth");
	        if (updatedAuthToken != null && !authToken.equals(updatedAuthToken)) 
	        {
	            Log.i(TAG,"Got updated auth token from datamessaging servers: " + updatedAuthToken);
	            updateToken(updatedAuthToken);
	        }

	        String responseLine = new BufferedReader(new InputStreamReader(conn.getInputStream()))
	            .readLine();
	        
	        // NOTE: You *MUST* use exponential backoff if you receive a 503 response code.
	        // Since App Engine's task queue mechanism automatically does this for tasks that
	        // return non-success error codes, this is not explicitly implemented here.
	        // If we weren't using App Engine, we'd need to manually implement this.
	        if (responseLine == null || responseLine.equals("")) 
	        {
	            Log.i(TAG,"Got " + responseCode +
	                    " response from Google AC2DM endpoint.");
	            throw new IOException("Got empty response from Google AC2DM endpoint.");
	        }

	        String[] responseParts = responseLine.split("=", 2);
	        if (responseParts.length != 2) {
	            Log.w(TAG,"Invalid message from google: " +
	                    responseCode + " " + responseLine);
	            throw new IOException("Invalid response from Google " +
	                    responseCode + " " + responseLine);
	        }

	        if (responseParts[0].equals("id")) {
	            Log.i(TAG,"Successfully sent data message to device: " + responseLine);
	            return true;
	        }

	        if (responseParts[0].equals("Error")) {
	            String err = responseParts[1];
	            Log.w(TAG,"Got error response from Google datamessaging endpoint: " + err);
	            // No retry.
	            // TODO(costin): show a nicer error to the user.
	            throw new IOException(err);
	        } else {
	            // 500 or unparseable response - server error, needs to retry
	            Log.w(TAG,"Invalid response from google " + responseLine + " " +
	                    responseCode);
	            return false;
	        }
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}
}