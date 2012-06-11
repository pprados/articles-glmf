package fr.prados.android.c2dm.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;

@SuppressWarnings("serial")
public class ClientLoginServlet extends HttpServlet
{
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException
	{
		resp.setContentType("text/plain");
		PrintWriter out = resp.getWriter();
		out.println("");
		out.println("");
		out.println("Auth=1234");
	}
}
