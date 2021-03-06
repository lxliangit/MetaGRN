

// line 378 "ReadData.jweb"
package org.omegahat.GUtilities;


// line 384 "ReadData.jweb"
import java.util.Vector;
import java.io.*;
import org.merlin.io.*;



// line 8 "ReadData.jweb"
public class ReadData
{
    
// line 20 "ReadData.jweb"
    /* none */

// line 11 "ReadData.jweb"
    
// line 25 "ReadData.jweb"
    
    /* none */

// line 12 "ReadData.jweb"
    
// line 33 "ReadData.jweb"
    /* none */

// line 13 "ReadData.jweb"
    
// line 39 "ReadData.jweb"
/** Read numeric data from a file and return as a java.util.Vector; 
 *
 *  @param filename path of file containing data
 *  @param messages show debugging/status messages on stderr
 *
 *  @return data read
 */

// static public Vector readDataAsVector( String filename, 
// 				       boolean messages) // show messages on stdout
//      throws java.io.IOException
// {
//     /* Temporary vector because we don't know how much data we have */
//     Vector tmp = new Vector();
//    
//     /* Open the input file */
//     BufferedReader B = new BufferedReader (new FileReader (filename));
//     StreamTokenizer T = new StreamTokenizer (B);
//     T.parseNumbers();           /* please parse doubles */
//     T.slashStarComments(true);  /* Ignore comments */
//     T.slashSlashComments(true); /* Ignore comments */
//   
//     /* Read file looking for numbers */
//     int token;
//     while( (token=T.nextToken()) != T.TT_EOF )
//         {
//             switch( token ) {
//             case T.TT_NUMBER:
//                 tmp.addElement(new Double(T.nval));
// 		//	    default: 
// 		//		throw new IOException("Non-numeric data in file.");
//             }
//         }
//   
//     if(messages)
//       {
//         System.err.println( T.lineno() + " lines read, " + 
//                             tmp.size() + " values found. ");
//       }
//
//     return tmp;
// }

static public Vector readDataAsVector( String filename, 
 				       boolean messages) // show messages on stdout
    throws java.io.IOException
{
    int lines = 0;
    int doubles = 0;

    /* Temporary vector because we don't know how much data we have */
    Vector tmp = new Vector();
    
    /* Open the input file */    
    DataReader data = new DataReader (new FileReader (filename) );
    data.setLineMode(true);

    /* Read the contents */
    while (!data.peekEOF ()) 
	{
	    lines++;
	    while ( !data.skipEOL () ) 
		{
		    doubles++;
		    tmp.addElement(new Double( data.readDouble() ));
		}
	}

    if(messages)
	System.err.println("Read " + doubles + " values on " + lines + " lines from \"" + filename + "\"" );

    return tmp;
}
    
/** Read numeric data from a file and return as a square matrix;
 *
 *  @param filename path of file containing data
 *  @param byRow <code>true</code>: data should be read in by row, <code>false</code> data should be read in by column.
 *  @param messages show debugging/status messages on stderr
 *
 *  @return data matrix
 */
static public double[][] readDataAsSquareMatrix( String filename, 
					  boolean byRow,    // true =  data arranged by row
				                           // false = by column
					  boolean messages) // show messages on stdout
    throws java.io.IOException
{
    
    Vector tmp = readDataAsVector( filename, byRow );

    // check if data has proper length //
    double sqrtRead = Math.sqrt(tmp.size());

    if( (double) ( (int) sqrtRead ) != sqrtRead )
        throw new RuntimeException(" Data length (" + tmp.size() + ") is not consistent" + 
                                   " with a square matrix.");
    
    int numRows = (int) sqrtRead;

    /* Copy the data into the retval array */
    double[][] retval = new double[numRows][numRows];
    int index = 0;
    for(int i=0; i < numRows; i++)
      for(int j=0; j < numRows; j++)
        {
          if( byRow )
            retval[i][j] = ((Double) tmp.elementAt(index)).doubleValue();
          else
            retval[j][i] = ((Double) tmp.elementAt(index)).doubleValue();
	  index++;
        }

    
    return retval;
}


/** Read numeric data from a file and return as a square matrix;
 *
 *  @param filename path of file containing data
 *  @param numColumns number of columns for returned matrix
 *  @param byRow <code>true</code>: data should be read in by row, <code>false</code> data should be read in by column.
 *  @param messages show debugging/status messages on stderr
 *
 *  @return data matrix
 */
static public double[][] readDataAsColumnMatrix( String filename, 
						 int    numColumns,// number of columns
						 boolean byRow,    // true =  data arranged by row
                        					   // false = by column
						 boolean messages) // show messages on stdout
    throws java.io.IOException
{
    
    Vector tmp = readDataAsVector( filename, byRow );

    // check if data has proper length //
    int numRows = tmp.size() / numColumns;

    if( numRows * numColumns != tmp.size() )
        throw new RuntimeException(" Data length (" + tmp.size() + ") is not consistent" + 
                                   " with a matrix having " + numColumns + " columns.");
    

    /* Copy the data into the retval array */
    double[][] retval = new double[numRows][numColumns];
    int index = 0;
    if(byRow)
      {
	for(int i=0; i < numRows; i++)
	    for(int j=0; j < numColumns; j++)
		{
		    retval[i][j] = ((Double) tmp.elementAt(index)).doubleValue();
		    index++;
		}
      }
    else
      {
	for(int j=0; j < numColumns; j++)
	  for(int i=0; i < numRows; i++)
		{
		    retval[i][j] = ((Double) tmp.elementAt(index)).doubleValue();
		    index++;
		}
      }

    return retval;
}


/** Read numeric data from a file and return as a square matrix;
 *
 *  @param filename path of file containing data
 *  @param numRows number of rows for returned matrix
 *  @param byRow <code>true</code>: data should be read in by row, <code>false</code> data should be read in by column.
 *  @param messages show debugging/status messages on stderr
 *
 *  @return data matrix
 */
static public double[][] readDataAsRowMatrix( String filename, 
					      int    numRows,   // number of rows
					      boolean byRow,    // true =  data arranged by row
                        			        	// false = by column
					      boolean messages) // show messages on stdout
    throws java.io.IOException
{
    
    Vector tmp = readDataAsVector( filename, byRow );

    // check if data has proper length //
    int numColumns = tmp.size() / numRows;

    if( numRows * numColumns != tmp.size() )
        throw new RuntimeException(" Data length (" + tmp.size() + ") is not consistent" + 
                                   " with a matrix having " + numRows + " rows.");
    

    /* Copy the data into the retval array */
    double[][] retval = new double[numRows][numColumns];
    int index = 0;
    if(byRow)
      {
	for(int i=0; i < numRows; i++)
	    for(int j=0; j < numColumns; j++)
		{
		    retval[i][j] = ((Double) tmp.elementAt(index)).doubleValue();
		    index++;
		}
      }
    else
      {
	for(int j=0; j < numColumns; j++)
	  for(int i=0; i < numRows; i++)
		{
		    retval[i][j] = ((Double) tmp.elementAt(index)).doubleValue();
		    index++;
		}
      }

    return retval;
}

/** Read numeric data from a file and return as a square matrix;
 *
 *  @param filename path of file containing data
 *  @param numRows number of rows for returned matrix
 *  @param numColumns number of columns for returned matrix
 *  @param byRow <code>true</code>: data should be read in by row, <code>false</code> data should be read in by column.
 *  @param messages show debugging/status messages on stderr
 *
 *  @return data matrix
 */
static public double[][] readDataAsColumnMatrix( String filename, 
						 int    numRows,   // number of rows
						 int    numColumns,// number of rows
						 boolean byRow,    // true =  data arranged by row
                        					   // false = by column
						 boolean messages) // show messages on stdout
    throws java.io.IOException
{
    
    Vector tmp = readDataAsVector( filename, byRow );

    // check if data has proper length //
    if( numRows * numColumns != tmp.size() )
        throw new RuntimeException(" Data length (" + tmp.size() + ") is not consistent" + 
                                   " with a matrix having " + numRows + " rows and " 
				   + numColumns + " columns.");
    

    /* Copy the data into the retval array */
    double[][] retval = new double[numRows][numColumns];
    int index = 0;
    if(byRow)
      {
	for(int i=0; i < numRows; i++)
	    for(int j=0; j < numColumns; j++)
		{
		    retval[i][j] = ((Double) tmp.elementAt(index)).doubleValue();
		    index++;
		}
      }
    else
      {
	for(int j=0; j < numColumns; j++)
	  for(int i=0; i < numRows; i++)
		{
		    retval[i][j] = ((Double) tmp.elementAt(index)).doubleValue();
		    index++;
		}
      }

    return retval;
}



// line 14 "ReadData.jweb"
    
// line 324 "ReadData.jweb"
static public void main(String[] argv ) throws java.io.IOException
{
    if ( argv.length < 2 ) 
	{
	    System.err.println("Usage: ReadData [filename:String] [numRows:int] [numCols:int] [ByRow:any]");
	    System.err.println("       numRows or numColumns set less than 1 indicates to use data to determine size. ");
	    System.err.println("       presence of anthing in postion ByRow requests data to be read by row.");
	    return;
	}

    String filename = argv[0];
    int    numRows  = Integer.parseInt(argv[1]);
    int    numColumns  = Integer.parseInt(argv[2]);
    boolean byRow = true;

    double[][] data;

    if( argv.length < 3 ) byRow = true;

    if( numRows < 1 && numColumns < 1 )
    {
	Vector tmp = readDataAsVector( filename, true );
	data = new double[1][tmp.size()];
	for(int i=0; i < tmp.size(); i++)
	    data[0][i] = ((Double) tmp.get(i)).doubleValue();
    }
    else if (numColumns < 1)
    {
	data = readDataAsRowMatrix( filename, numRows, byRow, true );
    }
    else if (numRows < 1)
    {
 	data = readDataAsColumnMatrix( filename, numColumns, byRow, true );
    }
    else
 	data = readDataAsColumnMatrix( filename, numRows, numColumns, byRow, true );

    for(int i=0; i < data.length; i++)
    {
	System.out.print("[ ");
	for(int j=0; j < data[i].length; j++)
	    System.out.print( data[i][j] + " ");
	System.out.println("]");
    }

    return;
}

    

// line 15 "ReadData.jweb"
}
