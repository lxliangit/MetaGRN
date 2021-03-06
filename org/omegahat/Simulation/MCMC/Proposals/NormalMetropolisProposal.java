

// line 197 "NormalMetropolisProposal.jweb"
  package org.omegahat.Simulation.MCMC.Proposals;


// line 203 "NormalMetropolisProposal.jweb"
    import org.omegahat.Simulation.MCMC.*;

  import org.omegahat.Probability.Distributions.*;
  import org.omegahat.Simulation.RandomGenerators.*;
// //  import Jama.*;
  import org.omegahat.GUtilities.ArrayTools;


// line 10 "NormalMetropolisProposal.jweb"
public class NormalMetropolisProposal extends NormalProposal implements SymmetricProposal
{

    
// line 25 "NormalMetropolisProposal.jweb"
public boolean DEBUG = false;

// line 14 "NormalMetropolisProposal.jweb"
    
// line 30 "NormalMetropolisProposal.jweb"
    
/* inherited */

// line 15 "NormalMetropolisProposal.jweb"
    
// line 38 "NormalMetropolisProposal.jweb"
/** Constructor for normal increments with identity covariance matrix.
 * @param length number of dimensions
**/
public NormalMetropolisProposal( int length, PRNG prng )
{
  super(length, prng);
  double[] mu = new double[length];
  double[][] sigma  = new double[length][length];

  for(int i=0; i < length; i++)
    {
      mu[i] = 0.0;
      for(int j=0; j < length; j++)
          sigma[i][j] = (i==j)?1.0:0.0;
    }
  
  setMean(mu);
  setCovariance(sigma);
  checkConformity();
}

/** Constructor for normal increments with specified covariance matrix.
 * @param var   variance matrix
**/
public NormalMetropolisProposal( double[][] var, PRNG prng)
{
  super(var.length, prng);
  double[] mu = new double[var.length];

  for(int i=0; i < var.length; i++)
    {
      mu[i] = 0.0;
    }
  
  setMean(mu);
  setCovariance(var);
  checkConformity();
}

/** Constructor for a spherical normal increments with variances for each dimension.  
 * The diagonal elements of the covariance matrix will be set to the specified value, with off diagonals set to 0
 * @param length number of dimensions
 * @param var    diagnonal values for covariance matrix (off diagonals are set to 0)
**/
public NormalMetropolisProposal( int length, double var, PRNG prng)
{
  this(length, prng);
  double[][] sigma  = new double[length][length];
  
  for(int i=0; i < length; i++)
  for(int j=0; j < length; j++)
    {
      sigma[i][j] = (i==j)?var:0.0;
    }
  
  setCovariance(sigma);
  checkConformity();

}


// line 16 "NormalMetropolisProposal.jweb"
    
// line 104 "NormalMetropolisProposal.jweb"
public Object generate( Object center )
{
    double[] mean = ArrayTools.Otod( center );

    setMean(mean);
    return super.generate();
}


// line 17 "NormalMetropolisProposal.jweb"
    
// line 135 "NormalMetropolisProposal.jweb"
public double conditionalPDF( Object state, Object conditionals ) 
{
	setMean(conditionals);
	return super.conditionalPDF(state, conditionals);
}

public double logConditionalPDF( Object state, Object conditionals ) 
{
	setMean(conditionals);
	return super.logConditionalPDF(state, conditionals);
}


// line 18 "NormalMetropolisProposal.jweb"
    
// line 117 "NormalMetropolisProposal.jweb"
    public double transitionProbability( Object from, Object to )
    {
	setMean(from);
	return super.transitionProbability( from, to );
    }

    public double logTransitionProbability( Object from, Object to )
    { 
	setMean(from);
	return super.logTransitionProbability( from, to );
    }
    

// line 19 "NormalMetropolisProposal.jweb"
    
// line 154 "NormalMetropolisProposal.jweb"
static public void main( String[] argv )
{
    if(argv.length < 3) 
    {
        System.err.println("Usage: NormalMetropolisProposal [numSamples] [offDiagonalCovariance] [debug]");
        return;
    }

    double rho = Double.parseDouble( argv[1] );

    double[][] var = {{1.0,rho,rho},{rho,1.0,rho},{rho,rho,1.0}}; 
    //    double[]   mean = {0.0,0.0,0.0};
    Double[]   start = new Double[]{ new Double(0.),
                                     new Double(0.),
                                     new Double(0.)};
    
    PRNG prng = new CollingsPRNG( (new CollingsPRNGAdministrator()).registerPRNGState() );
    
    NormalMetropolisProposal example = new NormalMetropolisProposal( var, prng );

    example.DEBUG = Boolean.valueOf(argv[2]).booleanValue();


     for(int outer=0; outer< Integer.parseInt(argv[0]); outer++)
     {
         double[] retval = (double[]) example.generate( start );
         double[] tmp = new double[3];

         for(int i=0; i<retval.length; i++)
             {
                 System.out.print( retval[i] + " " );
                 tmp[i] = retval[i];
             }

         System.out.println( example.PDF( retval ) );
     }

}

// line 20 "NormalMetropolisProposal.jweb"
}
