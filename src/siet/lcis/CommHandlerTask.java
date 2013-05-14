package siet.lcis;

import java.net.*;
import java.io.*;

/**
 * Class responsible for low level communication with a 
 * service. Request an id from the connecting service
 * and register this service to the agent ({@link VAssistant})
 * on creation.
 */
public class CommHandlerTask extends Thread {
	    private Socket mSocket = null;
	    private VAssistant mVAssistant = null;
		private String mSenderId;
		
		private PrintWriter mOutputStream = null;
		
		private static final String TAG = "siet.lcis. CommHandlerTask";

	    public CommHandlerTask(Socket socket)
	    {
	    	super("CommHandlerTask");
	    	this.mSocket = socket;
	    }
	    
	    public void setAssistant(VAssistant pVAssistant)
	    {
	    	mVAssistant = pVAssistant;
	    }

	    public void run() {

		try {
		    mOutputStream = new PrintWriter(mSocket.getOutputStream(), true);
		    BufferedReader in = new BufferedReader(
					    new InputStreamReader(
					    mSocket.getInputStream()));
		    
		    try {
				sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		    mOutputStream.println("id?");

		    String inputLine = in.readLine();
		    if (inputLine != null)
		    {
				mSenderId = inputLine;
				mVAssistant.registerService(mSenderId, this);
			}

		    while ((inputLine = in.readLine()) != null)
		    {
		    	// VAssistant.mPerceptions is thread-safe.
		    	mVAssistant.mPerceptions.add(new Stimulus(inputLine));
		    }
		    
		    System.out.println("closing communication handler task's socket for service ["+mSenderId+"]");
		    mVAssistant.unregisterService(mSenderId);
		    
		    mOutputStream.close();
		    in.close();
		    mSocket.close();

		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }

		public void writeToStream(String message) {
			try
		    {
		        if (mSocket.isConnected() && mOutputStream != null)
		        {
		            mOutputStream.println(message);
		        }
		        else
		        {
		        	if (mOutputStream == null)
		        	{
		        		System.err.println(TAG+" output stream null.");
					}
		        	else
		        	{
		        		System.err.println(TAG+" socket not connected.");
		        	}
		        }
		    }
		    catch (Exception e)
		    {
		    	System.err.println(TAG+" writeToStream : Writing failed");
		        e.printStackTrace();
		    }	
		}

}
