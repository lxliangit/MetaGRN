

// line 209 "AdaptiveNormalMetropolisProposal.jweb"
  package org.omegahat.Simulation.MCMC.Proposals;


// line 215 "AdaptiveNormalMetropolisProposal.jweb"
    import org.omegahat.Simulation.MCMC.*;

  import org.omegahat.Probability.Distributions.*;
  import org.omegahat.Simulation.RandomGenerators.*;
//   import Jama.*;



// line 14 "AdaptiveNormalMetropolisProposal.jweb"
public class AdaptiveNormalMetropolisProposal extends AdaptiveProposal
{

    
// line 27 "AdaptiveNormalMetropolisProposal.jweb"
protected double     inflationFactor = 1.0;

protected boolean DEBUG = false;

protected double[][] minVar;
protected double[][] maxVar;

// line 18 "AdaptiveNormalMetropolisProposal.jweb"
    
// line 37 "AdaptiveNormalMetropolisProposal.jweb"
    
public double getInflationFactor() { return inflationFactor; }
public double setInflationFactor( double in ) { return inflationFactor = in; }


public double[][] setMinVar( double[][] var ) { return this.minVar = var;  }
public double[][] setMaxVar( double[][] var ) { return this.maxVar = var;  }

// line 19 "AdaptiveNormalMetropolisProposal.jweb"
    
// line 50 "AdaptiveNormalMetropolisProposal.jweb"
/** Constructor for normal increment proposal with specified
 * covariance matrix, no variance inflation @param var variance matrix
**/
    public AdaptiveNormalMetropolisProposal( double[][] var, PRNG prng )
{
    this(var, 1.0, prng );
}

/** Constructor for normal increments with specified covariance matrix.
 * @param var   variance matrix
**/
public AdaptiveNormalMetropolisProposal( double[][] var, double inflationFactor, PRNG prng )
{
    proposal =  new NormalMetropolisProposal( var, prng );
    this.inflationFactor = inflationFactor;
}

/** Constructor for a independent normal increntens (sam variances for each dimension).  
    No variance inflation.
 * The diagonal elements of the covariance matrix will be set to the specified value, with off diagonals set to 0
 * @param length number of dimensions
 * @param var    diagnonal values for covariance matrix (off diagonals are set to 0)
**/
public AdaptiveNormalMetropolisProposal( int length, double var, PRNG prng )
{
    this( length, var, 1.0, prng );
}



/** Constructor for a spherical normal increments with the same  variance for each dimension.  
 * The diagonal elements of the covariance matrix will be set to the specified value, with off diagonals set to 0
 * @param length number of dimensions
 * @param var    diagnonal values for covariance matrix (off diagonals are set to 0)
 * @param inflationFactor factor to inflate observed variance when adapting.
**/
public AdaptiveNormalMetropolisProposal( int length, double var, double inflationFactor, PRNG prng )
{
    proposal = new NormalMetropolisProposal( length, var, prng );
    this.inflationFactor = inflationFactor;
}


// line 20 "AdaptiveNormalMetropolisProposal.jweb"
    
// line 97 "AdaptiveNormalMetropolisProposal.jweb"
public void adapt( MultiState mstate )
{
  int dim = ((NormalMetropolisProposal) proposal).dim();
  
  MultiDoubleState states;
  if(mstate instanceof MultiDoubleState)
    states = (MultiDoubleState) mstate;
  else
    states = new MultiDoubleState(mstate);
    
  double[][] var  = states.correctedVar();

  double optimalAdjust = 2.38 * 2.38 / dim;

  //
  // Adjust the covariance matrix by inflatings and enforcing specified bounds
  //
  for(int i=0; i<dim; i++)
  {
      for(int j=0; j<dim; j++)
          {
	      /* inflate the covariance matrix by the specified factor */
              var[i][j] = var[i][j] * optimalAdjust * inflationFactor;

	      // Enforce minimum bounds
	      if( (minVar!=null) && Math.abs(var[i][j]) < minVar[i][j] )
		  if(var[i][j] >= 0.0 )
		      var[i][j] = minVar[i][j];
		  else
		      var[i][j] = -minVar[i][j];

	      // Enforce maximum bounds
	      if( (minVar!=null) && Math.abs(var[i][j]) > maxVar[i][j] )
		  if(var[i][j] >= 0.0 )
		      var[i][j] = maxVar[i][j];
		  else
		      var[i][j] = -maxVar[i][j];

          }
  }

  boolean sucess = false;
  int     count  = 0;
  while(!sucess && count++ < 20 )
  {
    sucess = true;
    try 
      { 
	((NormalMetropolisProposal) proposal).setCovariance(var);
	sucess = true;
      }
    catch (RuntimeException ex)
      {
	sucess = false;

	for(int i=0; i < var.length; i++)
	  var[i][i] *= 1.05;
      }
  }
   
  if( count >= 20) throw new RuntimeException("Unable to generate invertible covariance matrix.");

  return;

}      

// line 21 "AdaptiveNormalMetropolisProposal.jweb"
    
// line 167 "AdaptiveNormalMetropolisProposal.jweb"
static public void main( String[] argv )
{
    if(argv.length < 3) 
    {
        System.err.println("Usage: AdaptaveNormalProposal [numSamples] [offDiagonalCovariance] [debug]");
        return;
    }

    double rho = Double.parseDouble( argv[1] );

    double[][] var = {{1.0,rho,rho},{rho,1.0,rho},{rho,rho,1.0}}; 
    Double[]   start = new Double[]{ new Double(0.),
                                     new Double(0.),
                                     new Double(0.)};
    
    PRNG prng = new CollingsPRNG( (new CollingsPRNGAdministrator()).registerPRNGState() );
    
    AdaptiveNormalMetropolisProposal example = new AdaptiveNormalMetropolisProposal( var, 1.0, prng );

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

         System.out.println( ((NormalMetropolisProposal) example.proposal).PDF( retval ) );
     }

}

// line 22 "AdaptiveNormalMetropolisProposal.jweb"
}
