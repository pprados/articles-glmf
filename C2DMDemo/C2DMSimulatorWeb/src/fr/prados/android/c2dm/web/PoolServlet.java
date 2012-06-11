package fr.prados.android.c2dm.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.*;

@SuppressWarnings("serial")
public class PoolServlet extends HttpServlet
{
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException
	{
		String registrationId = req.getParameter("registration_id");
		String applicationId=Messages.getSingleton().getApplicationId(registrationId);
		List<Messages.Message> msgs=Messages.getSingleton().getMessage(registrationId);
		
		resp.setContentType("text/plain");
		
		PrintWriter out = resp.getWriter();
		out.println(applicationId);
		for (Messages.Message m:msgs)
		{
			m.params.entrySet();
			for (Map.Entry<String, String> s:m.params.entrySet())
			{
				out.println(s.getKey()+"="+s.getValue());
			}
			out.println();
		}
	}
}
