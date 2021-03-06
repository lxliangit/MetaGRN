package org.omegahat.Simulation.RandomGenerators;

/**
 * interface for factories that create PRNG's from PRNGStates.
 *
 * @author  Greg Warnes
 * @author  $Author: warneg $
 * @version $Revision: 1.1.1.1 $
 */
public interface PRNGFactory
{
  /**
   * Instantiate a PRNG of the correct class
   *
   * @param state State information used to create the PRNG
   *
   */
    public PRNG instantiate( PRNGState state ) throws Exception;
}

