package edu.caltech.cs141b.hw2.gwt.collab.server;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ClearLockRequest extends HttpServlet{
	
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    	throws IOException {
    	CollaboratorServiceImpl.clearLockReqeust();
    }

}
