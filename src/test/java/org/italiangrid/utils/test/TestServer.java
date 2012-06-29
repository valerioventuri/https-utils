package org.italiangrid.utils.test;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.italiangrid.utils.https.ServerFactory;

public class TestServer {

	
	TestServer(String hostname, int port) throws Exception{
		
		Server s = ServerFactory.newServer(hostname, port);
		s.setHandler(new HelloHandler());
		s.start();
		s.join();
		
	}
	
	public static void main(String[] args) throws Exception {
		
		if (args.length < 2){
			
			System.err.println("Please provide hostname and port as arguments.");
		}
		
		String hostname = args[0];
		int port = Integer.parseInt(args[1]);
		new TestServer(hostname, port);
		
	}
}

class HelloHandler extends AbstractHandler{

	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);
        response.getWriter().println("<h1>Hello World</h1>");
		
	}
	
	
	
}