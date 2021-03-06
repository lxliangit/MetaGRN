
// line 12 "HeavyAndLight_Example_NKC.jweb"
/**
 * This class fits a mixture of two normal distributions, one at (0,0,0,0)
 * the other at (9,9,9,9) with diagonal variance matrices I and 1/4 * I.
 * 
 * @author  Gregory R. Warnes
 * @date    $Date: 2001/04/04 17:16:22 $
 * @version $Revision: 1.1.1.1 $
 */

package org.omegahat.Simulation.MCMC.Examples;

import org.omegahat.Simulation.MCMC.*;
import org.omegahat.Simulation.MCMC.Proposals.*;
import org.omegahat.Simulation.MCMC.Listeners.*;
import org.omegahat.Simulation.MCMC.Targets.*;
import org.omegahat.Simulation.RandomGenerators.*;
import org.omegahat.Probability.Distributions.*;
import org.omegahat.GUtilities.ReadData;
import org.omegahat.GUtilities.ArrayTools;

//import java.util.Vector;
//import java.util.Date;

public class HeavyAndLight_Example_NKC
{
 
  static public void main( String[] argv ) throws java.io.IOException
  {
    
    //
    // First, we need a random number generator
    //
    
    CollingsPRNGAdministrator a = new CollingsPRNGAdministrator(Integer.parseInt(argv[1]));
    PRNG prng = new CollingsPRNG( a.registerPRNGState() );
    
    //
    // Second, setup the model or 'target' distribution.
    //
    // In this case, we're using a Mixture of two Multivariate Normal Distributions
    //
    
    double[][] cov =  { {1.00, 0.00, 0.00, 0.00}, 
 			{0.00, 1.00, 0.00, 0.00}, 
 			{0.00, 0.00, 1.00, 0.00}, 
 			{0.00, 0.00, 0.00, 1.00} };

    double[] mean0 = { 0.00, 0.00, 0.00, 0.00} ; 
    
    double[] mean1 = { 9.00, 9.00, 9.00, 9.00} ; 

    UnnormalizedDensity target = new TwoMultivariateNormalMixture( mean0, cov,  // mode 0
                                                                   mean1, cov,  // mode 1
                                                                   7.0/8.0,     // upper weight
                                                                   prng );
    //
    // Third, create an initial states for the sampler
    //
    // In this case, we'll read them from a data file
    //
    
    double[][] stateMat = ReadData.readDataAsColumnMatrix( "NKC.states.in", 
                                                           4,    /* 4 columns */
                                                           true, /* byRow */ 
                                                           true  /* verbose */);

    int numComponents = stateMat.length;

    MultiDoubleState state0 = new MultiDoubleState ( numComponents );
    for(int i=0; i < numComponents; i++)
        state0.add( stateMat[i] );

    //
    // Fourth, setup the proposal distribution 
    //

    
    // read the the variance for each dimension from the file "variance.NKC"
    //
    double[][] var = ReadData.readDataAsColumnMatrix( "NKC.variance.in", 
                                                      4,    /* 4 columns */
                                                      true, /* byRow */ 
                                                      true  /* verbose */);
    
    double scaleFactor = 1.4 * Math.pow( numComponents, -2.0 / (4.0 + 4.0 ) );

    System.err.println("Adjusted Variance is :");

    for(int i=0; i < 4; i++)
      {
        for(int j=0; j < 4; j++)
	  {
	    var[i][j] = var[i][j] * scaleFactor;
	    System.err.print( var[i][j] + " " );
	    }
	System.err.println();
      }

    HastingsCoupledProposal proposal = new NormalKernelProposal(var, prng );
    
    
    //
    // Fifth, we construct the MCMC sampler
    //

    double[] minb = {-20.0, -20.0, -20.0, -20.0};
    double[] maxb = { 20.0,  20.0,  20.0,  20.0};

    CustomHastingsCoupledSampler mcmc = 
        mcmc = new CustomHastingsCoupledSampler( state0,   
                                                  numComponents,
                                                  target,
                                                  proposal,
                                                  prng,
						 //                                                  minb,
						 //                                                  maxb,
                                                  true);

    
    //
    // Sixth, we need to add a listener to collect the output of the MCMC run
    //


    //
    // Create the a listener that writes out the MCMC chain 
    //
    // We want to "thin" the output so we only see the output once per "scan"
    //
    ThinningProxyListener pL = new ThinningProxyListener(numComponents);  // create the proxy
    MCMCListenerHandle pLh = mcmc.registerListener(pL);                   // connect it to the MCMC sampler

    MCMCListenerWriter l1 = new StrippedListenerWriter("NKC.states.out");
    MCMCListenerHandle lh1 = pL.registerListener(l1);

    //
    // Create a listener that reports some useful information
    //
    MCMCListenerWriter l2 = new DistanceWriter("NKC.distance.out");
    MCMCListenerHandle lh2 = mcmc.registerListener(l2);


    //
    // Create a listener that reports the correlation matrix
    //
 //   MCMCListenerWriter l3 = new CovarianceWriter("NKC.covariance.out");
 //   MCMCListenerHandle lh3 = mcmc.registerListener(l3);

    // 
    // Finally, everything is ready to run.  
    //

    mcmc.iterate( Integer.parseInt( argv[0] ) );

    //
    // Cleanup 
    //

    l1.flush();
    l1.close();

    l2.flush();
    l2.close();

    //l3.flush();
    //l3.close();

    // Debug //
    
    MVNormal m0 = ((TwoMultivariateNormalMixture) target).getMVNorm0();
    MVNormal m1 = ((TwoMultivariateNormalMixture) target).getMVNorm1();

    System.err.println("Cov0:");
    System.err.println( ArrayTools.arrayToString( m0.getCovariance() ) );
    
    System.err.println("Cov1:");
    System.err.println( ArrayTools.arrayToString( m1.getCovariance() ) );
    

  }

}
 
