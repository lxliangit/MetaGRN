

// line 223 "LocallyAdaptiveNormalKernelProposal.jweb"
  package org.omegahat.Simulation.MCMC.Proposals;


// line 229 "LocallyAdaptiveNormalKernelProposal.jweb"
  import org.omegahat.Simulation.MCMC.*;

  import org.omegahat.Probability.Distributions.*;
  import org.omegahat.Simulation.RandomGenerators.*;

//  import org.omegahat.GUtilities.Distance;

//   import Jama.*;



// line 14 "LocallyAdaptiveNormalKernelProposal.jweb"
public class LocallyAdaptiveNormalKernelProposal extends AdaptiveMultiKernelProposal 
{

    
// line 27 "LocallyAdaptiveNormalKernelProposal.jweb"
protected double inflationFactor = 1.0;

protected boolean DEBUG = false;

protected int     numNeighbors = -1; // anything less than 1 means "all"

protected int     method = MultiDoubleState.Mahalanobis;  // use Mahalanobis distance to decide nearest neighbors
// line 18 "LocallyAdaptiveNormalKernelProposal.jweb"
    
// line 38 "LocallyAdaptiveNormalKernelProposal.jweb"
public double getInflationFactor() { return this.inflationFactor; }
public double setInflationFactor( double factor ) { return this.inflationFactor = factor; }

public boolean getDEBUG() { return this.DEBUG; }
public boolean setDEBUG( boolean flag ) { return this.DEBUG = flag; }

public int    getDistanceMethod() { return this.method; }
public int    setDistanceMethod( int method ) { return this.method=method; }

// line 19 "LocallyAdaptiveNormalKernelProposal.jweb"
    
// line 52 "LocallyAdaptiveNormalKernelProposal.jweb"
/**
 * Constructor for normal increment proposal with specified
 * covariance matrix, no variance inflation 
 * @param var variance matrix
**/
public LocallyAdaptiveNormalKernelProposal( int numStates, int numNeighbors, double[][] var, PRNG prng )
{
    this(numStates, numNeighbors, var, 1.0, prng );
}

/** Constructor for normal increments with specified covariance matrix.
 * @param var   variance matrix
**/
public LocallyAdaptiveNormalKernelProposal( int numStates,  int numNeighbors, double[][] var, double inflationFactor , PRNG prng )
{
    kernels = new NormalMetropolisProposal[numStates];
    for(int i=0; i < numStates; i++)
        {
            kernels[i] = new NormalMetropolisProposal(var, prng );
        }
    this.inflationFactor = inflationFactor;
    this.prng = prng;
    this.numNeighbors = numNeighbors;

}

/** Constructor for a sperical independent standard normal increments
 *  No variance inflation.
 *  @param length number of dimensions
**/
public LocallyAdaptiveNormalKernelProposal( int numStates,  int numNeighbors, int length , PRNG prng )
{
    this(numStates, numNeighbors, length, 1.0, prng );
}



/** Constructor for a spherical independent standard normal increments 
 * @param length number of dimensions
 * @param inflationFactor factor to inflate observed variance when adaptng.
**/
public LocallyAdaptiveNormalKernelProposal( int numStates,  int numNeighbors, int length, double inflationFactor , PRNG prng )
{
    kernels = new NormalMetropolisProposal[numStates];
    for(int i=0; i < numStates; i++)
        {
            kernels[i] = new NormalMetropolisProposal( length, prng );
        }

    this.inflationFactor = inflationFactor;
    this.prng = prng; 
    this.numNeighbors = numNeighbors;
}


// line 20 "LocallyAdaptiveNormalKernelProposal.jweb"
    
// line 111 "LocallyAdaptiveNormalKernelProposal.jweb"
public void adapt( MultiState mstate, int which )
{

  int dim = ((NormalMetropolisProposal) kernels[0]).dim();

  if(numNeighbors<2) numNeighbors = mstate.size();

  if(DEBUG)  System.err.print("Adapting.");

  MultiDoubleState states = new MultiDoubleState(mstate);
    

  for(int outer=0; outer < mstate.size(); outer++)
  {
      double[][] var = states.nearestNeighborVar ( outer, numNeighbors, method );
      double[]  mean = states.nearestNeighborMean( outer, numNeighbors, method );

      
      if(true)
	  {
	  if(numNeighbors <= ((dim*(dim+1))/2) )
	      {
		  for(int i=0; i<dim; i++)
		      var[i][i] *= 1.05;
	      }
	  }
      else
	  {
	  if(numNeighbors <= ((dim*(dim+1))/2) )
	      {
		  for(int i=0; i<dim; i++)
		  for(int j=0; j<dim; j++)
		      {
			  if(i!=j) var[i][j] = 0.0;
		      }
	      }
	  }
      
      ((NormalMetropolisProposal) kernels[outer]).setCovariance(var );
      ((NormalMetropolisProposal) kernels[outer]).setMean      (mean);


//       //      double optimalAdjust = Math.pow( 2. / ( (1. + 2. * (double) dim ) * (double) states.size() ) ,
//       //				       2./ (4.+ (double) dim ) );
//       //((double) mstate.size()) / ((double) numNeighbors) ; 

//       double optimalAdjust = 1.0;
//       //double neighborAdjust = ((double) states.size()) / ((double) numNeighbors) ; 
//       double neighborAdjust = 1.0;
      
      
//       /* inflate the covariance matrix by the specified factor */
//       for(int i=0; i<dim; i++)
//           {
//               for(int j=0; j<dim; j++)
//                   {
// 		      if( numNeighbors < (dim+1) )
// 			  var[i][j] = (i==j) ? var[i][j] * optimalAdjust * neighborAdjust * inflationFactor : 0.0;
// 		      else
// 			  var[i][j] = var[i][j] * optimalAdjust * neighborAdjust * inflationFactor;
//                   }
//           }
  }
}      
// line 21 "LocallyAdaptiveNormalKernelProposal.jweb"
    
// line 179 "LocallyAdaptiveNormalKernelProposal.jweb"
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
    
    LocallyAdaptiveNormalKernelProposal example = new LocallyAdaptiveNormalKernelProposal( 10, 0, var, 1.0, prng );

    example.DEBUG = Boolean.valueOf(argv[2]).booleanValue();


     for(int outer=0; outer< Integer.parseInt(argv[0]); outer++)
     {
         Double[] retval = (Double[]) ((NormalMetropolisProposal) example.kernels[0]).generate( start );
         double[] tmp = new double[3];

         for(int i=0; i<retval.length; i++)
             {
                 System.out.print( retval[i] + " " );
                 tmp[i] = retval[i].doubleValue();
             }

         System.out.println( ((MVNormal) example.kernels[0]).PDF( retval ) );
     }

}

// line 22 "LocallyAdaptiveNormalKernelProposal.jweb"
}
