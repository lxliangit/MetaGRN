

// line 13 "HastingsCoupledIteratedProposal.jweb"
/* $Header: /cvsroot/hydra-mcmc/Hydra/org/omegahat/Simulation/MCMC/Proposals/HastingsCoupledIteratedProposal.java,v 1.1.1.1 2001/04/04 17:16:26 warneg Exp $  */


// line 216 "HastingsCoupledIteratedProposal.jweb"
    package org.omegahat.Simulation.MCMC.Proposals;


// line 222 "HastingsCoupledIteratedProposal.jweb"
    import org.omegahat.Simulation.MCMC.*;

    import org.omegahat.Probability.Distributions.*;
    import org.omegahat.Simulation.RandomGenerators.PRNG;


// line 23 "HastingsCoupledIteratedProposal.jweb"
public class HastingsCoupledIteratedProposal implements HastingsCoupledProposal, TimeDependentProposal
{
  
// line 40 "HastingsCoupledIteratedProposal.jweb"
protected HastingsCoupledProposal[] proposals;
protected int[]             proposalFreqs;
protected int               currentProposal = 0;
protected int               currentCount = 0;
protected int               time = 0;
public    boolean           debug = false; 

// line 26 "HastingsCoupledIteratedProposal.jweb"
  
// line 50 "HastingsCoupledIteratedProposal.jweb"
// read-only
public  int numChains() { return proposals.length; }

public void setProposal( int which, HastingsCoupledProposal what, int freq )
{
  proposals[which] = what; 
  proposalFreqs[which] = freq;
}

public void setProposals( HastingsCoupledProposal[] what, int[] freqs )
{
  if(what.length != freqs.length) 
    throw new RuntimeException("The length of the mixture proportions vector must " + 
			       " match the number of proposals.");

  proposals = what;
  proposalFreqs = freqs;
}



// line 28 "HastingsCoupledIteratedProposal.jweb"
  
// line 120 "HastingsCoupledIteratedProposal.jweb"
/** 
 * Convenience method for computing the probability of proposing a move.  
 */
public double transitionProbability( Object from, Object to, int which, MultiState stateVector  )
 {
   return conditionalPDF( to, from, which, stateVector );
 }

/** 
 * Convenience method for computing the log probability of proposing a move.  
 */

public double logTransitionProbability( Object from, Object to, int which, MultiState stateVector )
{
   return logConditionalPDF( to, from, which, stateVector  );
}

// line 29 "HastingsCoupledIteratedProposal.jweb"
  
// line 142 "HastingsCoupledIteratedProposal.jweb"
public double conditionalPDF   ( Object state, Object conditions, int which, MultiState stateVector)
{
    return Math.exp(proposals[currentProposal].conditionalPDF(state, conditions, which, stateVector )); 
}

public double logConditionalPDF( Object state, Object conditions, int which, MultiState stateVector )
{  
    return proposals[currentProposal].logConditionalPDF( state,  conditions, which, stateVector  ); 
}
// line 30 "HastingsCoupledIteratedProposal.jweb"
  
// line 155 "HastingsCoupledIteratedProposal.jweb"
// generate a single random value 
public Object generate( Object conditionals, int which, MultiState stateVector )
{ 
    return proposals[currentProposal].generate( conditionals, which, stateVector  );
} 


// line 31 "HastingsCoupledIteratedProposal.jweb"
  
// line 74 "HastingsCoupledIteratedProposal.jweb"
public void timeInc()
{
  int seenStart = 0; // how many times have we looped past our starting location?

  time++;
  currentCount++;
  if(currentCount >= proposalFreqs[currentProposal])
  {
    currentCount = 0;

    // set currentProposal to the next proposal with nonzero frequency
    currentProposal = (currentProposal+1) % proposals.length;
    while ( proposalFreqs[currentProposal] < 1 )
      {
	currentProposal++;
	if(currentProposal >= proposals.length) 
	  {
	    currentProposal = 0;
	    seenStart++;
	    if(seenStart > 1) 
              throw new RuntimeException("Infinite loop detected. " + 
                                         "Probably due to all proposalFreqs being zero.");
	  }
      }
  }

  if(debug) System.err.println("Incrementing time to " + time + " setting proposal to " + currentProposal + ".");
}

public void resetTime()
{
    time = -1;
    timeInc();
}

public int getTime()
{
    return time;
}



// line 33 "HastingsCoupledIteratedProposal.jweb"
  
// line 167 "HastingsCoupledIteratedProposal.jweb"
protected HastingsCoupledIteratedProposal( int nProposal )
{
     proposals = new HastingsCoupledProposal[nProposal]; 
     proposalFreqs = new int[nProposal];
     currentProposal = 0;
     currentCount = 0;
     time = 0;
}

public HastingsCoupledIteratedProposal( HastingsCoupledProposal[] proposalList, 
					int[]                     proposalFreqs ) 
{ 
    if(proposalList.length != proposalFreqs.length) 
	throw new RuntimeException("proposalList and proposalFreqs must have the same length.");

    proposals = proposalList;
    this.proposalFreqs = proposalFreqs;
    currentProposal = 0;
    currentCount = 0;
    time = 0;

    int nonzero = 0;
    for(int i=0; i<proposalFreqs.length; i++)
      {
	if (proposalFreqs[i] < 0) 
	  throw new RuntimeException("proposalFreqs must be non negative.");
	else if (proposalFreqs[i] > 0) 
          nonzero++;
      }

    if( nonzero == 0 )
      throw new RuntimeException("At least one proposalFreqs must be nonzero.");

    // set currentProposal to the first proposal with nonzero frequency
    for(int i=0; i<proposalFreqs.length; i++)
	{ 
	    if (proposalFreqs[currentProposal] > 0)
		{
		    currentProposal = i;
		    break;
		}
	}

} 

// line 34 "HastingsCoupledIteratedProposal.jweb"
}

