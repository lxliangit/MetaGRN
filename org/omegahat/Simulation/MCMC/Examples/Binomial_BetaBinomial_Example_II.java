
// line 11 "Binomial_BetaBinomial_Example_II.jweb"
/**
 * This class fits a
 * Binomial-BetaBinomial model to a the fequency of
 * Loss-of-Heterozygosity for 40 Chromosome arms  (REFERENCE?)
 * and is intended to serve as a simple example for the HYDRA MCMC library.
 * 
 * @author  Gregory R. Warnes
 * @date    $Date: 2001/04/04 17:16:23 $
 * @version $Revision: 1.1.1.1 $
 */

package org.omegahat.Simulation.MCMC.Examples;

import org.omegahat.Simulation.MCMC.*;
import org.omegahat.Simulation.MCMC.Proposals.*;
import org.omegahat.Simulation.MCMC.Listeners.*;
import org.omegahat.Simulation.RandomGenerators.*;
import org.omegahat.Probability.Distributions.*;


//import java.util.Vector;
//import java.util.Date;

public class Binomial_BetaBinomial_Example_II 
{
 
  static public void main( String[] argv ) throws Throwable
  {
    
    //
    // First, we need a random number generator
    //
    
    CollingsPRNGAdministrator a = new CollingsPRNGAdministrator();
    PRNG prng = new CollingsPRNG( a.registerPRNGState() );
    
    //
    // Second, setup the model or 'target' distribution.
    //
    // In this case, we're using a Binomial-BetaBinomial Mixture Model
    //
    
    UnnormalizedDensity target = new Binomial_BetaBinomial_Likelihood();
    
    //
    // Third, setup the proposal distribution 
    //
    // In this case, we'll use the a variable at a time Normal proposal
    //
    
    // define the variance for each dimension to be the variance of the prior:
    //                             { 1/12,       1/12,       1/12,       1/24      }
    double[] diagVar = new double[]{ 0.08333333, 0.08333333, 0.08333333, 0.04166667};
    
    SymmetricProposal proposal = new NormalMetropolisComponentProposal(diagVar, prng );
    
    
    //
    // Fourth, create an initial state for the sampler
    //
    
    double[] state = new double[]{0.90, 0.23, 0.71, 0.49 };

    //
    // Fifth, we construct the MCMC sampler
    //

    CustomMetropolisHastingsSampler mcmc = 
             new CustomMetropolisHastingsSampler(state,    // initial state
                                                 target,   // target distribution
                                                 proposal, // proposal distribution
                                                 prng,     // RNG
                                                 true  );  // report all details?
    
    //
    // Sixth, we need to add a listener to collect the output of the MCMC run
    //

    // Create the listener 
    ListenerWriter l = new StrippedListenerWriter("MCMC.output");

    // Hook it up to the MCMC object
    MCMCListenerHandle lh = mcmc.registerListener(l);

    // 
    // Finally, everything is ready to run.  
    //

    mcmc.iterate( 10000 );

    //
    // Close the listener (Saves all cached data)
    //
    
    l.close();

  }

}
 
