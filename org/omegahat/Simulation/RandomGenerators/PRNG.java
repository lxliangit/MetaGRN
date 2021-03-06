package org.omegahat.Simulation.RandomGenerators;

/**
 *   Interface to Pseudo-random number generators.  
 *
 * <body> This interface provides access to classes that generate
 * <b>uniform<b> random numbers.  Values can be obtained either as
 * integers, or as doubles on [0,1).
 */
public interface PRNG 
{
    /** Generate a pseudo-random integer from a uniforml distribion on [0,intRange) */
    public int      nextInt();               /* synchronized*/

    /** 
     * Generate several pseudo-random integers from a uniforml distribion on [0,intRange) 
     * @param n number of integer deviates to return
     * @retval an array of <b>n</b> int's.
     */
    public int[]    nextIntArray(int n);          /* synchronized*/

    /** 
     * Return the largest int value returned by <b>nextInt<\b>.
     * <body> This value can be used to convert int's returned by
     * <b>nextInt</b> to doubles uniformly distributed on the range [0,1).
     */
    public int getIntRange( );
    
    
    /**
     * Generate a pseudo-random double value from a uniforml distribion on [0,1) 
     */
    public double    nextDouble();           /* synchronized*/


   /** 
     * Generate several pseudo-random doubles from a uniforml distribion on [0,1)
     * @param n number of integer deviates to return
     * @retval an array of <b>n</b> double's.
     */
    public double[ ] nextDoubleArray(int n);      /* synchronized*/

    /**
     * Return the minimum possible difference between two generated double's.
     */
    public double    getDoubleEpsilon();
    
    /**
     * Return an object describing the state of the PRNG.  
     * 
     * <body> This state can be used to create another PRNG that has will generate
     *        the same stream of random numbers.  This can be useful for reproducing
     *        simulation results.
     * </body>
     */
    public PRNGState      getState( );            /* synchronized*/
}

