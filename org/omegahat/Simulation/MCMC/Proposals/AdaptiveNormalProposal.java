

// line 193 "AdaptiveNormalProposal.jweb"
  package org.omegahat.Simulation.MCMC.Proposals;


// line 199 "AdaptiveNormalProposal.jweb"
    import org.omegahat.Simulation.MCMC.*;

  import org.omegahat.Probability.Distributions.*;
  import org.omegahat.Simulation.RandomGenerators.*;
//   import Jama.*;



// line 14 "AdaptiveNormalProposal.jweb"
public class AdaptiveNormalProposal extends AdaptiveProposal
{

    
// line 27 "AdaptiveNormalProposal.jweb"
protected double         inflationFactor = 1.0;

protected boolean DEBUG = false;


// line 18 "AdaptiveNormalProposal.jweb"
    
// line 35 "AdaptiveNormalProposal.jweb"
    
double getInflationFactor() { return inflationFactor; }
double setInflationFactor( double in ) { return inflationFactor = in; }

// line 19 "AdaptiveNormalProposal.jweb"
    
// line 44 "AdaptiveNormalProposal.jweb"
/** Constructor for normal with specified mean and covariance matrix, no variance inflation 
 * @param length number of dimensions
 * @param mean  mean vector
 * @param var   variance matrix
**/
    public AdaptiveNormalProposal( double[] mean, double[][] var, PRNG prng )
{
    this(mean, var, 1.0, prng );
}

/** Constructor for normal with specified mean and covariance matrix.
 * @param length number of dimensions
 * @param mean  mean vector
 * @param var   variance matrix
**/
public AdaptiveNormalProposal( double[] mean, double[][] var, double inflationFactor, PRNG prng )
{
    proposal = new NormalProposal(mean, var, prng );
    this.inflationFactor = inflationFactor;
    this.prng = prng;
}

/** Constructor for a spherical normal with the same mean and variances for each dimension.  No variance inflation.
 * Each element mean will be set the specified value.
 * The diagonal elements of the covariance matrix will be set to the specified value, with off diagonals set to 0
 * @param length number of dimensions
 * @param mean   mean of each dimension
 * @param var    diagnonal values for covariance matrix (off diagonals are set to 0)
**/
public AdaptiveNormalProposal( int length, double mean, double var, PRNG prng )
{
    this( length, mean, var, 1.0, prng );
}



/** Constructor for a spherical normal with the same mean and variances for each dimension.  
 * Each element mean will be set the specified value.
 * The diagonal elements of the covariance matrix will be set to the specified value, with off diagonals set to 0
 * @param length number of dimensions
 * @param mean   mean of each dimension
 * @param var    diagnonal values for covariance matrix (off diagonals are set to 0)
 * @param inflationFactor factor to inflate observed variance when adapting.
**/
public AdaptiveNormalProposal( int length, double mean, double var, double inflationFactor, PRNG prng  )
{
    proposal = new NormalProposal(length, mean, var, prng );
    this.inflationFactor = inflationFactor;
    this.prng = prng;
}


// line 20 "AdaptiveNormalProposal.jweb"
    
// line 101 "AdaptiveNormalProposal.jweb"
public void adapt( MultiState mstate )
{

  if(DEBUG)  System.err.print("Adapting.");

  int dim = ((NormalProposal) proposal).dim();

  MultiDoubleState states = new MultiDoubleState(mstate);
    
  double[]   mean = states.mean();
  double[][] var  = states.correctedVar();

  if(DEBUG) System.err.println("var[0][0]=" + var[0][0] );

  /* inflate the covariance matrix by the specified factor */
  if(inflationFactor != 1.0)
  {
      for(int i=0; i<dim; i++)
          for(int j=0; j<dim; j++)
              var[i][j] = var[i][j] * inflationFactor;
  }

  boolean success = false; 
  int tries = 0;
  while(!success && tries < 10 )
    {
      try 
	{
	  ((NormalProposal) proposal).setCovariance(var);
	  success = true;
	}
      catch ( RuntimeException re )
	{
	  success = false;
	  tries++;

	  for(int i=0; i < dim; i++)
	    var[i][i] *= 1.05;
	}
      }


  ((NormalProposal) proposal).setMean(mean);
}      
// line 21 "AdaptiveNormalProposal.jweb"
    
// line 150 "AdaptiveNormalProposal.jweb"
static public void main( String[] argv )
{
    if(argv.length < 3) 
    {
        System.err.println("Usage: AdaptaveNormalProposal [numSamples] [offDiagonalCovariance] [debug]");
        return;
    }

    double rho = Double.parseDouble( argv[1] );

    double[][] var = {{1.0,rho,rho},{rho,1.0,rho},{rho,rho,1.0}}; 
    double[]   mean = {0.0,0.0,0.0};
    Double[]   start = new Double[]{ new Double(0.),
                                     new Double(0.),
                                     new Double(0.)};
    
    PRNG prng = new CollingsPRNG( (new CollingsPRNGAdministrator()).registerPRNGState() );
    
    AdaptiveNormalProposal example = new AdaptiveNormalProposal(  mean, var, 1.0, prng );

    example.DEBUG = Boolean.valueOf(argv[2]).booleanValue();


     for(int outer=0; outer< Integer.parseInt(argv[0]); outer++)
     {
         Double[] retval = (Double[]) example.proposal.generate( start );
         double[] tmp = new double[3];

         for(int i=0; i<retval.length; i++)
             {
                 System.out.print( retval[i] + " " );
                 tmp[i] = retval[i].doubleValue();
             }

         System.out.println( ((NormalProposal) example.proposal).PDF( retval ) );
     }

}

// line 22 "AdaptiveNormalProposal.jweb"
}
