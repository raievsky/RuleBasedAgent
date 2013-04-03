package siet.lcis;

import java.net.*;
import java.text.MessageFormat;
import java.io.*;

public class CommHandlerTask extends Thread {
	    private Socket mSocket = null;
	    private VAssistant mVAssistant = null;

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
		    PrintWriter out = new PrintWriter(mSocket.getOutputStream(), true);
		    BufferedReader in = new BufferedReader(
					    new InputStreamReader(
					    mSocket.getInputStream()));

		    String inputLine, outputLine;

		    while ((inputLine = in.readLine()) != null)
		    {
		    	// VAssistant.mPerceptions is thread-sage.
		    	mVAssistant.mPerceptions.add(new Stimulus(inputLine));
		    }
		    out.close();
		    in.close();
		    mSocket.close();

		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }

}
