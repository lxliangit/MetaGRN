

// line 218 "LocallyAdaptiveNormalProposal.jweb"
  package org.omegahat.Simulation.MCMC.Proposals;


// line 224 "LocallyAdaptiveNormalProposal.jweb"
  import org.omegahat.Simulation.MCMC.*;

  import org.omegahat.Probability.Distributions.*;
  import org.omegahat.Simulation.RandomGenerators.*;

//  import org.omegahat.GUtilities.Distance;

//   import Jama.*;



// line 14 "LocallyAdaptiveNormalProposal.jweb"
public class LocallyAdaptiveNormalProposal extends AdaptiveMultiKernelProposal 
{

    
// line 27 "LocallyAdaptiveNormalProposal.jweb"
protected double inflationFactor = 1.0;

protected boolean DEBUG = false;

protected int     numNeighbors = -1; // anything less than 1 means "all"

protected int     method = MultiDoubleState.Mahalanobis;  // use Mahalanobis distance to decide nearest neighbors
// line 18 "LocallyAdaptiveNormalProposal.jweb"
    
// line 38 "LocallyAdaptiveNormalProposal.jweb"
public double getInflationFactor() { return this.inflationFactor; }
public double setInflationFactor( double factor ) { return this.inflationFactor = factor; }

public boolean getDEBUG() { return this.DEBUG; }
public boolean setDEBUG( boolean flag ) { return this.DEBUG = flag; }

public int    getDistanceMethod() { return this.method; }
public int    setDistanceMethod( int method ) { return this.method=method; }

// line 19 "LocallyAdaptiveNormalProposal.jweb"
    
// line 52 "LocallyAdaptiveNormalProposal.jweb"
/**
 * Constructor for normal increment proposal with specified
 * covariance matrix, no variance inflation 
 * @param var variance matrix
**/
public LocallyAdaptiveNormalProposal( int numStates, int numNeighbors, double[][] var, PRNG prng )
{
    this(numStates, numNeighbors, var, 1.0, prng );
}

/** Constructor for normal increments with specified covariance matrix.
 * @param var   variance matrix
**/
public LocallyAdaptiveNormalProposal( int numStates,  int numNeighbors, double[][] var, double inflationFactor , PRNG prng )
{
    double[] mean0 = new double[var.length];
    for(int i=0; i<mean0.length;i++) mean0[i]=0.0;

    //    kernels = new NormalMetropolisProposal[numStates];
    kernels = new NormalProposal[numStates];
    for(int i=0; i < numStates; i++)
        {
            //            kernels[i] = new NormalMetropolisProposal(var, prng );
            kernels[i] = new NormalProposal(mean0, var, prng );
        }
    this.inflationFactor = inflationFactor;
    this.prng = prng;
    this.numNeighbors = numNeighbors;

}

/** Constructor for a sperical independent standard normal increments
 *  No variance inflation.
 *  @param length number of dimensions
**/
public LocallyAdaptiveNormalProposal( int numStates,  int numNeighbors, int length , PRNG prng )
{
    this(numStates, numNeighbors, length, 1.0, prng );
}



/** Constructor for a spherical independent standard normal increments 
 * @param length number of dimensions
 * @param inflationFactor factor to inflate observed variance when adaptng.
**/
public LocallyAdaptiveNormalProposal( int numStates,  int numNeighbors, int length, double inflationFactor , PRNG prng )
{
    double[] mean0 = new double[length];
    for(int i=0; i<mean0.length;i++) mean0[i]=0.0;

    //    kernels = new NormalMetropolisProposal[numStates];
    kernels = new NormalProposal[numStates];
    for(int i=0; i < numStates; i++)
        {
            //            kernels[i] = new NormalMetropolisProposal( length, prng );
            kernels[i] = new NormalProposal(length, prng );
        }

    this.inflationFactor = inflationFactor;
    this.prng = prng; 
    this.numNeighbors = numNeighbors;
}


// line 20 "LocallyAdaptiveNormalProposal.jweb"
    
// line 121 "LocallyAdaptiveNormalProposal.jweb"
public void adapt( MultiState mstate, int which )
{
  MultiDoubleState states = new MultiDoubleState(mstate);

  int dim = ((NormalProposal) kernels[0]).dim();

  if(DEBUG)  System.err.print("Adapting.");

    

  for(int outer=0; outer < mstate.size(); outer++)
  {
      double[][] var; //= states.nearestNeighborVar ( outer, numNeighbors, method );
      double[]  mean = states.nearestNeighborMean( outer, numNeighbors, method );

      if(numNeighbors <= ((dim*(dim+1))/2) )
	  var = states.nearestNeighborDiagVar ( outer, numNeighbors, method );
      else
	  var = states.nearestNeighborVar ( outer, numNeighbors, method );
      
      //      for(int i=0; i<dim; i++)
      //	  var[i][i] /= 2.0;

      ((NormalProposal) kernels[outer]).setCovariance(var );
      ((NormalProposal) kernels[outer]).setMean      (mean);


//       //      double optimalAdjust = Math.pow( 2. / ( (1. + 2. * (double) dim ) * (double) states.size() ) ,
//       //                                    2./ (4.+ (double) dim ) );
//       //((double) mstate.size()) / ((double) numNeighbors) ; 

//       double optimalAdjust = 1.0;
//       //double neighborAdjust = ((double) states.size()) / ((double) numNeighbors) ; 
//       double neighborAdjust = 1.0;
      
      
//       /* inflate the covariance matrix by the specified factor */
//       for(int i=0; i<dim; i++)
//           {
//               for(int j=0; j<dim; j++)
//                   {
//                    if( numNeighbors < (dim+1) )
//                        var[i][j] = (i==j) ? var[i][j] * optimalAdjust * neighborAdjust * inflationFactor : 0.0;
//                    else
//                        var[i][j] = var[i][j] * optimalAdjust * neighborAdjust * inflationFactor;
//                   }
//           }
  }
}      
// line 21 "LocallyAdaptiveNormalProposal.jweb"
    
// line 174 "LocallyAdaptiveNormalProposal.jweb"
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
    
    LocallyAdaptiveNormalProposal example = new LocallyAdaptiveNormalProposal( 10, 0, var, 1.0, prng );

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

// line 22 "LocallyAdaptiveNormalProposal.jweb"
}
