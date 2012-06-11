package fr.prados.android.c2dm.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;

@SuppressWarnings("serial")
public class RegisterServlet extends HttpServlet
{
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException
	{
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();
		out.println("");

		out.println(Messages.getSingleton().getRegistrationId(
			req.getParameter("application_id")));
	}
}
