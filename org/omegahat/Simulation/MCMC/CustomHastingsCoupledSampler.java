

// line 15 "CustomHastingsCoupledSampler.jweb"
 /* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/CustomHastingsCoupledSampler.java,v 1.1.1.1 2001/04/04 17:16:17 warneg Exp $  */

 /* (c) 2000 Gregory R. Warnes */ 
 /* May also contain code (c) 1999 The Omegahat Project */




// line 430 "CustomHastingsCoupledSampler.jweb"
    package org.omegahat.Simulation.MCMC;



// line 437 "CustomHastingsCoupledSampler.jweb"
import org.omegahat.Simulation.MCMC.Proposals.*;
import org.omegahat.Simulation.MCMC.Targets.*;
import org.omegahat.Simulation.MCMC.Listeners.*;
import org.omegahat.Probability.Distributions.*;
import org.omegahat.Simulation.RandomGenerators.PRNG;
import org.omegahat.GUtilities.ArrayTools;



// line 30 "CustomHastingsCoupledSampler.jweb"
/**  
 * A Hastings Coupled Sampler that consistes of $C$ component
 * samplers.  The target distribution of the $C$ components is created
 * by the product of $C$ independent copies of the target state.  At
 * each time step, only one component is updated.
 * 
 * A separate proposal distribution can be used for each component.
 * These proposal distributions can be either <code>GeneralProposal</code>s, or
 * <code>HastingsCoupledProposals</code>.  The latter propose the new state for
 * one sampler based on the current state of all samplers.
 **/
public class CustomHastingsCoupledSampler extends CustomMarkovChain implements HastingsCoupledSampler
{

    

// line 57 "CustomHastingsCoupledSampler.jweb"
protected HastingsCoupledProposal proposal;   /** proposal method **/        // inherited 
protected UnnormalizedDensity     target;     /** target distribution */     // inherited 
protected MultiState              state;      /** the current state **/
protected PRNG                    prng;       /** Random Number Generator **/      // inherited 

protected int[] order;  // permutation of chain numbers so that we can mix the order up
protected int   index;  // index into order
protected int   numSamplers; // number of chains

protected int   numProposed = 0;
protected int   numAccepted  = 0;

/*---*/
protected double              logAcceptProb;
protected double              uniformRand;
protected boolean             accepted;
protected double              log_p_X;
protected double              log_q_X_to_Y;
protected double              log_p_Y;
protected double              log_q_Y_to_X;
/*---*/

protected boolean             detailed;

public    boolean             DEBUG = false;
// line 45 "CustomHastingsCoupledSampler.jweb"
    
// line 87 "CustomHastingsCoupledSampler.jweb"
public HastingsCoupledProposal getProposal()                                   { return this.proposal; }
public HastingsCoupledProposal setProposal( HastingsCoupledProposal proposal ) { return this.proposal = proposal; }

public UnnormalizedDensity getTarget()                           { return this.target; }
public UnnormalizedDensity setTarget(UnnormalizedDensity target) { return this.target = target; }

public MCMCState getState()                 { return state; }
public MCMCState setState( MultiState state ) 
{
    if( !(state instanceof MultiState ) )
        throw new RuntimeException("state must be an instance of 'MultiState'.");

    if( (state!=null) && (numSamplers!=state.size()) )
        throw new RuntimeException("Number of samplers (" + numSamplers + ") and length of current state (" + 
                                   state.size() + ") must  match. \n" +
                                   "Please set the number of samplers using `setNumSamplers("+numSamplers+")`\n" +
                                   "before calling `setState(..)`");
    
    return this.state = state; 
}

public int getNumSamplers() { return this.numSamplers; }
public int setNumSamplers( int numSamplers ) 
{
    if( (state!=null) && (numSamplers!=state.size()) )
        throw new RuntimeException("Number of samplers (" + numSamplers + ") and length of current state (" + 
                                   state.size() + ") must  match. \n" +
                                   "Please set the current state to 'null' using 'setState(..)'\n" +
                                   "before calling setNumSamplers.");

    this.numSamplers = numSamplers; 
    initialize(); 
    return this.numSamplers; 
}
 
public int getNumAccepted()   { return this.numAccepted; }
public int resetNumAccepted() { return numProposed = numAccepted = 0; }

public int getNumProposed()   { return this.numAccepted; }
public int resetNumProposed() { return numProposed = numAccepted = 0; }



// line 46 "CustomHastingsCoupledSampler.jweb"
    
// line 373 "CustomHastingsCoupledSampler.jweb"
void initialize()
{
    //  numSamplers = proposals.length;

  // create vector to hold order we will update chains
  order = new int[numSamplers];
  for(int i=0; i<numSamplers; i++)
    order[i] = i;

  // start with the first one in the list
  index = 0;

  // for testing
  //  DEBUG = true;
}


public CustomHastingsCoupledSampler( MultiState                  state, 
                                     int                         numSamplers,
                                     UnnormalizedDensity         target, 
                                     HastingsCoupledProposal     proposal, 
                                     PRNG                        prng,
                                     boolean                     detailed )
{
  if( state.size() != numSamplers ) 
      throw new RuntimeException("Initial state does not match" + 
                                 " specified number of component samplers.");

  this.state       = state;
  this.numSamplers = numSamplers;
  this.target      = target;
  this.proposal    = proposal;
  this.prng        = prng;
  this.detailed    = detailed;
  initialize();
}

public CustomHastingsCoupledSampler( MultiState               state, 
                                     int                      numSamplers,
                                     UnnormalizedDensity      target, 
                                     HastingsCoupledProposal  proposal, 
                                     PRNG                     prng)
{
    this(state, numSamplers, target, proposal, prng, false);
}


// line 47 "CustomHastingsCoupledSampler.jweb"
    
// line 136 "CustomHastingsCoupledSampler.jweb"
   

public MCMCState generate( MultiState currentStateVector )
{
    /*---*/
    MultiState          proposedStateVector = new MultiState();;
    MultiState          newStateVector;

    Object              currentComponent;
    Object              proposedComponent;
    Object              newComponent;



    /* copy current states to new states */
    for(int i = 0; i < currentStateVector.size(); i++)
    {
        proposedStateVector.add(currentStateVector.get(i));
    }

 
    /*****************************************************************/
    /* If this proposal depends on time, tell it to update its clock */
    /*****************************************************************/
    if ( proposal instanceof TimeDependentProposal )
        ((TimeDependentProposal) proposal).timeInc();

    
    /***************************************/
    /* decide which component gets updated */
    /***************************************/
    if( index >= numSamplers ) permute();
    int which = order[index++];
   
    /***************************************/
    /* pull out the component we decided */
    /***************************************/
    currentComponent = currentStateVector.get(which);

    /***************************/
    /* generate a new proposal */
    /***************************/

    proposedComponent = proposal.generate(currentComponent, which, currentStateVector );

    /*****************************************************/
    /* put this new state into the proposed state vector */
    /*****************************************************/
    proposedStateVector.set(which, proposedComponent);

    /**********************************/
    /* compute acceptance probability */
    /**********************************/
    logAcceptProb = logAcceptanceProb( currentComponent, currentStateVector,
                                       proposedComponent, proposedStateVector,
                                       which ) ;

    /*******************/
    /* accept / reject */
    /*******************/
    uniformRand   = prng.nextDouble();
    accepted = Math.log(uniformRand) < logAcceptProb;

    if(accepted)
    {
        newStateVector    = proposedStateVector;
        newComponent      = proposedComponent;
    }
    else
    {
        newStateVector    = currentStateVector;
        newComponent      = currentComponent;
    }


    /************************/
    /* Notify any listeners */
    /************************/
    numProposed++;
    if (accepted) numAccepted++;
    double acceptRate = (double) numAccepted / (double) numProposed;
    
    if (detailed)
        {      
             notifyAll( new  DetailHastingsCoupledStepEvent( this,
                                                             newStateVector,      // new state 
                                                             proposedStateVector, // proposed state
                                                             currentStateVector,  // previous state

                                                             log_p_X,       // lastProb
                                                             log_p_Y,       // proposedProb

                                                             log_q_X_to_Y,  // forwardProb
                                                             log_q_Y_to_X,  // reverseProb

                                                             logAcceptProb, // probAccept
                                                             uniformRand,   // acceptRand

                                                             accepted,      // accepted
                                                             acceptRate,    // proportion accepted

                                                             numSamplers,   // number of components
                                                             order,         // update order
                                                             which,         // which component was updated sampler           
                                                             newComponent,      // new value for component
                                                             proposedComponent, // proposed value
                                                             currentComponent   // previous value
                                                             )    
                 );
        }
    else
        {
            notifyAll( new  GenericChainStepEvent( this,  // source object
                                                   newStateVector  // current
                                                   ) );
        }
    
    /*************************/
    /* decide what to return */
    /*************************/
    if ( accepted  )
    {
        return proposedStateVector;
    }
    else
    {
        return currentStateVector;
    }



}

// line 48 "CustomHastingsCoupledSampler.jweb"
    
// line 354 "CustomHastingsCoupledSampler.jweb"
/** Generate the next state from the current state. */
public void  step()
{
    state = (MultiState) generate( state );
}

public void iterate( int howmany )
{
    for( int i=0; i < howmany; i++)
        step();
}


// line 49 "CustomHastingsCoupledSampler.jweb"
    

// line 274 "CustomHastingsCoupledSampler.jweb"
// permute the order we will update the chains.  
// Assumes order contains (a permutation of) the values 0..(numSamplers-1)
void permute()
{
  for(int i=0; i<numSamplers; i++)
    {
      // starting at the front, randomly grab a value from somewhere later in the chain.
      // this ensures that there is only one way to get each value, consequently, the 
      // permutation is uniformly distributed.
      //
      int which = (int) (((double) (numSamplers - i)) * prng.nextDouble());
      int tmp = order[i];
      order[i] = order[which];
      order[which] = tmp;

    }


  if(false) {
    System.err.print("Permutation: ");
    for(int i=0; i < numSamplers; i++)
      System.err.print("[" +  order[i] + "] ");
    System.err.println();
  }

  // also reset index
  index = 0;
}

 
protected double acceptanceProb( Object current,  MultiState currentStateVector,
                                 Object proposed, MultiState proposedStateVector,
                                 int which
                                 )
{
  return Math.exp(logAcceptanceProb( current, currentStateVector, 
                                     proposed, proposedStateVector,
                                     which ));
}



protected double logAcceptanceProb( Object current,  MultiState currentStateVector,
                                    Object proposed, MultiState proposedStateVector,
                                    int which
                                 )
{
    if(DEBUG) System.err.println("current=" + ArrayTools.arrayToString(current) );
    if(DEBUG) System.err.println("proposed=" + ArrayTools.arrayToString(proposed) );
    log_p_X = target.logUnnormalizedPDF( current  );
    log_p_Y = target.logUnnormalizedPDF( proposed ) ;
    if(DEBUG) System.err.println("P(current)=" + log_p_X );
    if(DEBUG) System.err.println("P(proposed)=" + log_p_Y );

    /******* Compute probability of forward moves ********/

    log_q_X_to_Y =  proposal.logTransitionProbability( current, proposed, 
                                                       which, currentStateVector ) ;


    /******* Compute probability of reverse ********/
    log_q_Y_to_X =  proposal.logTransitionProbability( proposed, current, 
                                                       which, proposedStateVector ) ;
    
    double denom = log_p_X  + log_q_X_to_Y;
    double numer = log_p_Y  + log_q_Y_to_X;
    //    System.err.println("denom=" + denom);

    if( Double.isNaN(numer) )
	return Double.NEGATIVE_INFINITY;
    else if ( Double.isInfinite(denom) || Double.isNaN(denom) )
        return 0.0;
    else
        return  Math.min( 0.0,  numer - denom );
}

// line 50 "CustomHastingsCoupledSampler.jweb"
    
}
