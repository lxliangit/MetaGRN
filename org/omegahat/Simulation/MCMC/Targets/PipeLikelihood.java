

// line 5 "PipeLikelihood.jweb"
/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/Targets/PipeLikelihood.java,v 1.1.1.1 2001/04/04 17:16:27 warneg Exp $  */


// line 207 "PipeLikelihood.jweb"
    package org.omegahat.Simulation.MCMC.Targets;


// line 213 "PipeLikelihood.jweb"
import org.omegahat.Probability.Distributions.*;
import org.omegahat.Simulation.RandomGenerators.*;
import java.util.Vector;
import java.io.*;


import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;
//import java.lang.Runnable;
import java.awt.*;
import java.text.*;


// line 20 "PipeLikelihood.jweb"
/**
 * 
 */
public class PipeLikelihood implements UnnormalizedDensity
{
    
// line 36 "PipeLikelihood.jweb"
protected String[] command;
protected boolean  returnsLogLikelihood = false;
protected int      numComputed = 0;

protected InputStreamReader in;
protected   StreamTokenizer inT;
protected       PrintWriter out; 
// line 26 "PipeLikelihood.jweb"
    
// line 55 "PipeLikelihood.jweb"
public double logUnnormalizedPDF( Object state )
{
  // state should be an array of Double containing the parameter values 
  double retval;

  Double[] current = (Double[]) state;

  //  out = new PrintWriter(System.err);

  for(int i = 0; i < current.length; i++)
  {
     out.print( current[i] + " " );
  }

  System.err.println();

  out.println();
  out.flush();

  numComputed++;

  
  /* Read file looking for numbers */
  try{ 
    char[] cbuf = new char[1024];

    /* wait for something to read */
    while( !in.ready() ) 
      { 
	Thread.sleep(1);
      }

    in.read(cbuf,1,1000) ;

    retval = Double.parseDouble( new String( cbuf ) );
   
    if( !returnsLogLikelihood )
      retval = Math.log(retval);
    
    return retval;
  }
  catch(Exception e) {
    e.printStackTrace();
    throw new RuntimeException("Error reading from pipe.");
  }

}

public double unnormalizedPDF( Object state )
{
    return Math.exp( logUnnormalizedPDF( state ) );
}


// line 27 "PipeLikelihood.jweb"
    
// line 113 "PipeLikelihood.jweb"
protected void startProcess(String[] commandArray)
{
	
	// Start a process //
	Runtime r = Runtime.getRuntime();
	Process p = null;
	
	try{
	    p = r.exec( commandArray );
	}
	catch (IOException e)
	    {
		e.printStackTrace();
		throw new RuntimeException("Error executing command");
	    }
	
	// Grab input and output streams for the process //

        out = new PrintWriter(p.getOutputStream(), true);	
	in = new InputStreamReader(p.getInputStream());

	inT = new StreamTokenizer(in);

        inT.parseNumbers();           /* please parse doubles */
        inT.slashStarComments(true);  /* Ignore comments */
        inT.slashSlashComments(true); /* Ignore comments */

        this.command = commandArray;

}

// line 28 "PipeLikelihood.jweb"
    
// line 47 "PipeLikelihood.jweb"
public String[] command() { return command; }
public boolean  returnsLogLikelihood() { return returnsLogLikelihood; }
public int      numComputed() { return numComputed; }

// line 29 "PipeLikelihood.jweb"
    
// line 149 "PipeLikelihood.jweb"
public PipeLikelihood(String command, 
                      String arguments[],
                      boolean returnsLogLikelihood
                      )
{

String[] cmdArray = new String[arguments.length+1];
cmdArray[0] = command;
for(int i=0; i < arguments.length; i++)
  cmdArray[i+1] = arguments[i];

this.returnsLogLikelihood =  returnsLogLikelihood;


startProcess(cmdArray);

}



public PipeLikelihood(String command, 
                      boolean returnsLogLikelihood
                      )
{

this(command, new String[0], returnsLogLikelihood);

}



// line 30 "PipeLikelihood.jweb"
    

// line 186 "PipeLikelihood.jweb"
public static void main(String argv[])
{
    PipeLikelihood p = new PipeLikelihood( "/usr/bin/perl", 
					   new String[]{ "../test.lik.pl" }, 
					   false );

    Double[] data = new Double[3];
    data[0] = new Double(1.0);
    data[1] = new Double(2.0);
    data[2] = new Double(3.0);

    for(int i = 0; i < 10 ; i++)
      System.out.println("Result: " + p.unnormalizedPDF( data ) );
}


// line 31 "PipeLikelihood.jweb"
}
