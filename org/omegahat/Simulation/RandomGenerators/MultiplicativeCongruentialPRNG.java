package org.omegahat.Simulation.RandomGenerators;
/**
 * Compute pseudo-random number using multiplicative congruential formula.
 *
 * <body>
 * The next value is computed using the formula:
 * <b>
 * X[t+1] = X[t] * mult % mod
 * <b>
 * </body>
 * 
 *
 */
public class MultiplicativeCongruentialPRNG 
{
  /*-------*/
  /* State */
  /*-------*/

    /** Seed value in [1, 2^31-1] */
    public long seed = 3141579;

    /** Multiplicative constant */
    public long mult = 28070;

    /** Modulus constant */
    public long mod  = 2147483647;
       
    /*--------------*/
    /* Constructors */
    /*--------------*/

    /** 
     * Create a Multiplicative Congruential Generator using the
     * default seed, multiplier and modulus constants. 
     */
    MultiplicativeCongruentialPRNG() {}

    /** 
     * Create A Multiplicative Congruential Generator using a specified seed. 
     * 
     * @param newSeed Value in [1,2^31-1] for the seed
     */
    MultiplicativeCongruentialPRNG( int newSeed )
    {
        seed=newSeed;
    }
    
    /** 
     * Create A Multiplicative Congruential Generator using a specified seed and 
     * multiplicative constant. 
     * 
     * @param newSeed Value in [1,2^31-1] for the seed
     * @param newMult Value in [1,2^32-1] for the multiplier
     */
     
    MultiplicativeCongruentialPRNG( int newSeed, int newMult )
    {
        seed=newSeed;
        mult=newMult;
    }
    
    /** 
     * Create A Multiplicative Congruential Generator using a specified seed and 
     * multiplicative constant. 
     * 
     * @param newSeed Value in [1,2^31-1] for the seed 
     * @param newMult Value in [1,2^32-1] for the multiplier constant
     * @param newMod  Value in [1,2^32-1] for the modulus constant
     */
    MultiplicativeCongruentialPRNG( int newSeed, int newMult, int newMod)
    {
        seed=newSeed;
        mult=newMult;
        mod=newMod;
    }
    
    
    
    /**
     * Generate a random integer in [1,Mod]
     */
    public synchronized int nextInt()
    {
        seed = ( (long) seed * (long) mult )% (long) mod;
        return (int) seed;
    }
    
    
    /**
     * Generate a random double in [0,1)
     */
    public synchronized double nextDouble()
    {
        seed = nextInt();
        return ((double) seed) / ((double) mod);
    }  
    
}

