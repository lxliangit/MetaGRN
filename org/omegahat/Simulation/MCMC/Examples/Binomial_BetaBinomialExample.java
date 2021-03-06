
// line 7 "Binomial_BetaBinomialExample.jweb"
/**
 * This class fits a
 * Binomial-BetaBinomial model to a the fequency of
 * Loss-of-Heterozygosity for 40 Chromosome arms  (REFERENCE?)
 * and is intended to serve as a simple example for the HYDRA MCMC library.
 * 
 * @author  Gregory R. Warnes
 * @date    $Date: 2001/04/04 17:16:21 $
 * @version $Revision: 1.1.1.1 $
 */

package org.omegahat.Simulation.MCMC.Examples;

import org.omegahat.Simulation.MCMC.*;
import org.omegahat.Simulation.MCMC.Targets.*;
import org.omegahat.Simulation.MCMC.Proposals.*;
import org.omegahat.Simulation.MCMC.Listeners.*;
import org.omegahat.Simulation.RandomGenerators.*;
import org.omegahat.Probability.Distributions.*;


//import java.util.Vector;
//import java.util.Date;

public class Binomial_BetaBinomialExample
{
 
  static public void main( String[] argv )
  {
    
    //
    // First, we need a random number generator
    //
    
    CollingsPRNGAdministrator a = new CollingsPRNGAdministrator();
    PRNG prng = new CollingsPRNG( a.registerPRNGState() );
    
    //
    // Second, setup the model or 'target' distribution.
    //
    // In this case, we're using a Binomial-BetaBinomial Mixture
    // which loads its data from the file "BarrettsLOH.dat"
    //
    
    UnnormalizedDensity target = new B_BB_Mixture( "BarrettsLOH.dat" );
    
    //
    // Third, setup the proposal distribution 
    //
    // In this case, we'll use the a variable at a time Normal proposal
    //
    
    // define the variance for each dimension to be ( 1/4^2, 1/4^2, 1/4^2, 1/8^2 )
    double[] diagVar = new double[]{ 0.0625, 0.0625, 0.0625, 0.015625 };
    
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
						 true  );  
    
    //
    // Sixth, we need to add a listener to collect the output of the MCMC run
    //

    // Create the listener 
    StepListenerPrinter l = new StepListenerPrinter();

    // Hook it up to the MCMC object
    MCMCListenerHandle lh = mcmc.registerListener(l);

    // 
    // Finally, everything is ready to run.  
    //

    mcmc.iterate( 10 );

    
  }

}
 
