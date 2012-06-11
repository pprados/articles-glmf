package fr.prados.android.c2dm.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

// Simulation de la gestion des messages
class Messages
{
	static class Message
	{
		String collapseKey;

		String applicationId;

		String registrationId;

		HashMap<String, String> params;
	}

	HashMap<String, String> ids = new HashMap<String, String>();

	HashMap<String, List<Message>> messages = new HashMap<String, List<Message>>();

	private static Messages _messages = new Messages();

	public static Messages getSingleton()
	{
		return _messages;
	}

	Random random = new Random(System.currentTimeMillis());

	String getRegistrationId(String applicationId)
	{
		String registrationid = ids.get(applicationId);
		if (registrationid == null)
		{
			do
			{
				registrationid = Long.toString(random.nextLong());
			} while (ids.get(registrationid) != null);
			ids.put(registrationid, applicationId);
		}
		return registrationid;
	}

	String getApplicationId(String registrationId)
	{
		return ids.get(registrationId);
	}

	void putMessage(Message msg)
	{
		List<Message> msgs = messages.get(msg.registrationId);
		if (msgs == null)
		{
			msgs = new ArrayList<Message>();
			messages.put(
				msg.registrationId, msgs);
		}
		for (Message m : msgs)
		{
			if (m.collapseKey.equals(msg.collapseKey))
			{
				m.applicationId = msg.applicationId;
				m.params = msg.params;
				return;
			}
		}
		msgs.add(msg);
	}
	
	List<Message> getMessage(String registrationId)
	{
		List<Message> m=messages.get(registrationId);
		if (m==null) m=Collections.emptyList();
		else messages.remove(registrationId);
		return m;
	}
}
