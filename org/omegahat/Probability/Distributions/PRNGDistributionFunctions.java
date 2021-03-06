
// line 3 "PRNGDistributionFunctions.jweb"
package org.omegahat.Probability.Distributions;

import org.omegahat.Simulation.RandomGenerators.PRNG;

public class PRNGDistributionFunctions extends DistributionFunctions
{
  PRNG prng;

  /** Acessors */

  public PRNG getPRNG()
    {
      return prng;
    }

  public void setPRNG( PRNG prng_in )
    {
      prng = prng_in;
    }


  /** Constructor */
  public PRNGDistributionFunctions(PRNG prng_in)
    {
      prng = prng_in;
    }
 
  public double uniformRand()
    {
      return prng.nextDouble();
    }
}

