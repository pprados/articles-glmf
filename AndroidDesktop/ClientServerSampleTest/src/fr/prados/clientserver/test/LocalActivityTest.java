package fr.prados.clientserver.test;

import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
import fr.prados.clientserver.ClientServerActivity;
import fr.prados.clientserver.R;

public class LocalActivityTest extends ActivityInstrumentationTestCase2<ClientServerActivity>
{
	ClientServerActivity mActivity;
	ToggleButton mToggleServer;
	Button mConnect;
	EditText mEditIp;
	TextView mStatus;

	public LocalActivityTest()
	{
		super("fr.prados.usbinvoke.sample", ClientServerActivity.class);
	}
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		setActivityInitialTouchMode(false);
		mActivity=getActivity();
		mToggleServer=(ToggleButton)mActivity.findViewById(R.id.toggleServer);
		mConnect=(Button)mActivity.findViewById(R.id.connect);
		mEditIp=(EditText)mActivity.findViewById(R.id.ip);
        mStatus=(TextView)mActivity.findViewById(R.id.status);
	}
	public void testPreConditions() 
	{
	    assertFalse(mToggleServer.isChecked());
	}

	public void testStartServer()
	{
		mActivity.runOnUiThread(
		      new Runnable() 
		      {
		        public void run() 
		        {
		        	mToggleServer.requestFocus();
		        }
		      }
		    );		
		try { Thread.sleep(2000); } catch (Exception e){}
		sendKeys(KeyEvent.KEYCODE_ENTER);
		getInstrumentation().waitForIdleSync();
		try { Thread.sleep(2000); } catch (Exception e){}
		assertNotNull(mActivity.mServer);
		assertTrue(mActivity.mServer.isAlive());
	}
}
