package fr.prados.android.c2dm.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class SendServlet extends HttpServlet
{
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException
	{
		String registrationId = req.getParameter("registration_id");
		String collapseKey = req.getParameter("collapse_key");
		String delayWhileIdle = req.getParameter("delay_while_idle");

		Messages messages = Messages.getSingleton();
		Messages.Message message = new Messages.Message();
		message.registrationId = registrationId;
		message.applicationId = messages.getApplicationId(registrationId);
		message.collapseKey = collapseKey;
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();
		message.params=new HashMap<String, String>();
		out.println("id=1234");
		for (Enumeration<String> e = req.getParameterNames(); e
				.hasMoreElements();)
		{
			String name = e.nextElement();
			if (name.startsWith("data."))
			{
				String paramname = name.substring(5);
				message.params.put(
					paramname, req.getParameter(name));
			}
		}

		messages.putMessage(message);
	}
}
