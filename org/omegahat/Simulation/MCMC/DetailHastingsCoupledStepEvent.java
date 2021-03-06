

  package org.omegahat.Simulation.MCMC;


import org.omegahat.Simulation.MCMC.Proposals.*;
import org.omegahat.Simulation.MCMC.Targets.*;
import org.omegahat.Simulation.MCMC.Listeners.*;

import org.omegahat.Simulation.MCMC.Proposals.*;
import org.omegahat.Simulation.MCMC.Targets.*;
import org.omegahat.Simulation.MCMC.Listeners.*;

import java.lang.reflect.Array;
    


public class DetailHastingsCoupledStepEvent extends DetailChainStepEvent 
{
    
        // -- inherited -- //
    // public MCMCState current;
    // public MCMCState proposed;
    // public MCMCState last;  
    // public double    lastProb;
    // public double    proposedProb;
    // public double    forwardProb;
    // public double    reverseProb;
    // public double    probAccept;
    // public double    acceptRand;
    // public boolean   accepted;
    // public double    acceptRate;


    public int[] order;  // permutation of chain numbers so that we can mix the order up
    public int   numSamplers; // number of component chains
    public int   which;  // index of sub-chain being updated

    public Object currentComponent;      // new value for component
    public Object proposedComponent; // proposed value
    public Object lastComponent;  // previous value


    public boolean showAllStates = false;


    
        /* none, everything is public */

    
    /** only for inheriting classes */
    public /*protected*/ DetailHastingsCoupledStepEvent()
    {};
        

    public DetailHastingsCoupledStepEvent( Object    source,   // caller uses "this"
                                           MCMCState current,
                                           MCMCState proposed,
                                           MCMCState last,
                                           double    lastProb,
                                           double    proposedProb,
                                           double    forwardProb,
                                           double    reverseProb,
                                           double    probAccept,
                                           double    acceptRand,
                                           boolean   accepted,
                                           double    acceptRate,
                                           
                                           int       numSamplers,
                                           int[]     order,
                                           int       which,
                                           
                                           Object currentComponent,  // new value for component
                                           Object proposedComponent, // proposed value
                                           Object lastComponent      // previous value
                                           )
    {
        super(source, 
              current,
              proposed,
              last,
              lastProb,
              proposedProb,
              forwardProb,
              reverseProb,
              probAccept,
              acceptRand,
              accepted,
              acceptRate);
        
        
        this.source = source;
        this.description = "Hastings Coupled Chain Step Event (with details)";
        
        this.numSamplers = numSamplers;
        this.order = order;
        this.which = which;

        this.currentComponent  = currentComponent;
        this.proposedComponent = proposedComponent;
        this.lastComponent     = lastComponent;
        
    }





        /* none */

    

    protected String arrayToString( Object contents )
    {

      String contentsString = "" ;
           
      try
        {
          int len = Array.getLength( contents );
          
          contentsString += "[ ";
          
          for(int j = 0; j < len ; j++)
            {
              contentsString += Array.get(contents,j) + " ";
            }
          
          contentsString += "] ";
        }
      catch ( Throwable e )
        {
                  contentsString = contents.toString();
        }
      
      
      return contentsString;
      
    }


    public String toString()
    {
        String retval = description + "\n";


        retval += ( "Number of Component Samplers = " + numSamplers + "\n");

        retval += ( "Order of Sampler Updates = [" ) ;
        for(int i=0; i<order.length; i++)
            retval += " " + order[i] ;
        retval += (" ]\n");
        
        retval += ( "Updated Component Sampler = " + which + "\n");

        if(showAllStates)
        {
            retval += ( "Last            = " + arrayToString(last)         + "\n" );
            retval += ( "Last     Prob   = " + Math.exp(lastProb) + ", Log = " + lastProb + "\n" );
            
            retval += ( "Proposed State  = " + arrayToString(proposed)     + "\n" );
            retval += ( "Proposed Prob   = " + Math.exp(proposedProb) + ", Log = " + proposedProb + "\n" );
            
            retval += ( "Current  State  = " + arrayToString(current)      + "\n" );
        }
        else
        {
            retval += ( "Last     (Component) State = " + arrayToString(lastComponent) + "\n");
            retval += ( "Last     Prob              = " + Math.exp(lastProb) + ", Log = " + lastProb + "\n" );


            retval += ( "Proposed (Component) State = " + arrayToString(proposedComponent) + "\n");
            retval += ( "Proposed Prob              = " + Math.exp(proposedProb) + ", Log = " + proposedProb + "\n" );

            retval += ( "Current  (Component) State = " + arrayToString(currentComponent) + "\n");
        }

        retval += ( "Forward Prob    = " + Math.exp(forwardProb) + ", Log = " + forwardProb  + "\n" );
        retval += ( "Reverse Prob    = " + Math.exp(reverseProb) + ", Log = " + reverseProb  + "\n" );

        retval += ( "Acceptance Prob = " + Math.exp(probAccept) + ", Log = " + probAccept   + "\n" );
        retval += ( "Acceptance Val  = " + acceptRand   + "\n" );

        retval += ( "Accepted?       = " + accepted     + "\n" );
        retval += ( "Acceptance Rate = " + acceptRate   + "\n" );

        return retval;
    }
     

    public MCMCState getCurrent()
    {
        return super.getCurrent();
    }


}
