package birc.grni.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import birc.grni.gui.ResultDialog;
import birc.grni.util.exception.BadInputFormatException;

public class CommonUtil {
	
	/**
	 * generate random double matrix
	 * @param rows
	 * @param columns
	 * @return
	 */
	public static ArrayList<ArrayList<Double>> randn(int rows, int columns){
		
		/* random matrix*/
		ArrayList<ArrayList<Double>> randomMatrix = new ArrayList<ArrayList<Double>>();
		
		/* generate random matrix*/
		Random random = new Random();
		for(int i = 0; i< rows; i++) {
			ArrayList<Double> oneRow = new ArrayList<Double>();
			for(int j = 0; j< columns; j++) {
				oneRow.add(2*random.nextDouble()-1); /* between -1 and 1*/
			}
			randomMatrix.add(oneRow);
		}
		
		return randomMatrix;
	}
	
	/**
	 * generate random float (single) matrix
	 * @param rows
	 * @param columns
	 * @return
	 */
	public static ArrayList<ArrayList<Float>> randnSingle(int rows, int columns){
		
		/* random matrix*/
		ArrayList<ArrayList<Float>> randomMatrix = new ArrayList<ArrayList<Float>>();
		
		/* generate random matrix*/
		Random random = new Random();
		for(int i = 0; i< rows; i++) {
			ArrayList<Float> oneRow = new ArrayList<Float>();
			for(int j = 0; j< columns; j++) {
				oneRow.add(2*random.nextFloat()-1); /* between -1 and 1*/
			}
			randomMatrix.add(oneRow);
		}
		
		return randomMatrix;
	}
	
	/**
	 * Validate number of input arguments and return an appropriate error message
	 * @param low
	 * @param high
	 * @param n
	 * @return
	 */
	public static String nargchk(int low, int high, int n) {
		if(n < low)
			return "Not enough input arguments.";
		else if(n > high)
			return "Too many input arguments.";
		else
			return "";
	}
	
	/**
	 * 
	 * @param low
	 * @param high
	 * @param n
	 * @param returnType
	 * @return
	 */
//	public static Object nargchk(int low, int high, int n, String returnType) {
//		if(returnType.equalsIgnoreCase("string"))
//			return nargchk(low, high, n);
//		else if(returnType.equalsIgnoreCase("struct")) {
//			if (n < low)
//				return new MessageStruct("0", "Not enough input arguments.");
//			else if (n> high)
//				return new MessageStruct("1", "Too many input arguments.");
//			else
//				return new MessageStruct();
//		}
//		else {
//			System.out.println("No such return type in nargchk.");
//			return null;
//		}
//	}
	
	/**
	 * print the error message
	 */
	public static void error() {
		
	}
	
	/**
	 * Return a list of numbers from @param low to @param high (both inclusive)
	 * @param low
	 * @param high
	 * @return
	 */
	public static ArrayList<Integer> range(int low, int high) {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for(int i=low;i<=high;i++) {
			list.add(i);
		}
		return list;
	}
	
	/**
	 * transfer a double precision vector to a single precision one
	 * @param doubleVector an ArrayList object contains the double precision vector
	 * @return
	 */
	public static ArrayList<Float> doubleToSingleVector(ArrayList<Double> doubleVector) {
		ArrayList<Float> singleVector = new ArrayList<Float>();
		for(Double dNumber : doubleVector)
			singleVector.add((dNumber.floatValue()));
		return singleVector;
	}
	
	/**
	 * transfer a double precision vector to a single precision one
	 * @param doubleVector a real array, can use basic type(int, float, double, long) elements
	 * @return
	 */
	public static float[] doubleToSingleVector(double[] doubleVector, int length) {
		float[] singleVector = new float[length];
		for(int i = 0;i < length; i++)
			singleVector[i] = (float) doubleVector[i];
		return singleVector;
	}
	
	/**
	 * transfer a double precision matrix to a single precision one
	 * @param doubleMatrix
	 * @return
	 */
	public static ArrayList<ArrayList<Float>> doubleToSingleMatrix(ArrayList<ArrayList<Double>> doubleMatrix) {
		ArrayList<ArrayList<Float>> singleMatrix = new ArrayList<ArrayList<Float>>();
		for(ArrayList<Double> doubleOneRow : doubleMatrix) {
			singleMatrix.add(doubleToSingleVector(doubleOneRow));
		}
		return singleMatrix;
	}
	
	public static float[][] doubleToSingleMatrix(double[][] doubleMatrix, int row, int column) {
		float[][] singleMatrix = new float[row][];
		for(int i = 0; i< row; i++)
			singleMatrix[i] = doubleToSingleVector(doubleMatrix[i], column);
		return singleMatrix;
	}
	
	/**
		return a double precision matrix of all zeros
		@param row
		@param column
		@return double precision matrix of all zeros
	 */
	public static double[][] zeros(int row, int column)
	{
		double[][] zeroMatrix = new double[row][column];
		for(int i = 0; i< row; i++)
			for(int j = 0; j< column; j++)
				zeroMatrix[i][j] = 0;
			
		return zeroMatrix;
	}
	
	/**
		return a double precision matrix of all ones
		@param row
		@param column
		@return double precision matrix of all zeros
	*/
	public static double[][] ones(int row, int column)
	{
		double[][] zeroMatrix = new double[row][column];
		for(int i = 0; i< row; i++)
			for(int j = 0; j< column; j++)
				zeroMatrix[i][j] = 1;
			
		return zeroMatrix;
	}
	
	public static ArrayList<ArrayList<Double>> zeroMean(ArrayList<ArrayList<Double>> matrix)
	{
		int row = matrix.size();
		int column = matrix.get(0).size();
		ArrayList<ArrayList<Double>> resultMatrix = new ArrayList<ArrayList<Double>>();
		for(int i = 0; i< row; i++)
			for(int j = 0; j< column; j++)
			{
				// jth (j start from 0) column
				ArrayList<Double> oneColumn = new ArrayList<Double>();
				for(int k = 0; k< row; k++)
					oneColumn.add(matrix.get(k).get(j));
				resultMatrix.add(new ArrayList<Double>());
				resultMatrix.get(i).add(
						(matrix.get(i).get(j) - Statistic.meanDoubleArrayList(oneColumn)) / (Math.sqrt(row-1) * Statistic.unbiasedStdDoubleArrayList(oneColumn))
					);
			}
		return resultMatrix;
	}
	
	public static double[][] zeroMean(double[][] matrix)
	{
		int row = matrix.length;
		int column = matrix[0].length;
		double[][] resultMatrix = new double[row][column];
		for(int i = 0; i< row; i++)
			for(int j = 0; j< column; j++)
			{
				// jth (j start from 0) column
				ArrayList<Double> oneColumn = new ArrayList<Double>();
				for(int k = 0; k< row; k++)
					oneColumn.add(matrix[k][j]);
				resultMatrix[i][j] = (matrix[i][j] - Statistic.meanDoubleArrayList(oneColumn)) / (Math.sqrt(row-1) * Statistic.unbiasedStdDoubleArrayList(oneColumn));
			}
		return resultMatrix;
	}
	
	public static void copyFile(File source, File dest) throws IOException {
	    InputStream is = null;
	    OutputStream os = null;
	    try {
	        is = new FileInputStream(source);
	        os = new FileOutputStream(dest);
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } finally {
	        is.close();
	        os.close();
	    }
	}
	
	/**
	 * copy all dependent libraries used by our own native libraries into correct location
	 */
	public static void copyDependentLibs() throws IOException, MalformedURLException, URISyntaxException, ClassNotFoundException {
		/*Get path of current executable jar file itself*/
        String resourceName = "/"+ CommonUtil.class.getName().replace('.', '/').concat(".class");
        URL resource = CommonUtil.class.getClass().getResource(resourceName);
        String urlPath = resource.getPath();
        String jarPath = urlPath.split("\\!", 2)[0];
		
        String normalJarPath = null;
		URL url = new URL(jarPath);
        normalJarPath = new File(new URI(url.toExternalForm())).getAbsolutePath();
        
        if(normalJarPath == null)
			System.err.println("Failed to get current excuted jar file path!");
		else {
			String parentFolderOfJar = new File(normalJarPath).getParent(); 
			/* copy all dependent library files to destination*/
			JarFile jarFile = new JarFile(normalJarPath);
			Enumeration<JarEntry> entrySet = jarFile.entries();
			while(entrySet.hasMoreElements()) {
				JarEntry currentEntry = entrySet.nextElement();
				String entryPath = "/" + currentEntry.getName();
				int lastIndex = entryPath.lastIndexOf("/");
				String dependentLibName = entryPath.substring(lastIndex + 1);
				if(entryPath.startsWith(GLOBALVAR.dependentLibsFolder) && !currentEntry.isDirectory()) {
					copyFileFromJar(entryPath, parentFolderOfJar + "/" + dependentLibName);
					//CommonFunction.dependentLibs.add(parentFolderOfJar + "/" + dependentLibName);
				}
			}
			jarFile.close();
		}
	}
	
	public static void copyNativeLib(String absolutePath) {
		 URL nativeLibraryURL = Class.class.getClass().getResource(absolutePath);
		 MyFileUtils.copyResourcesRecursively(nativeLibraryURL, new File("."));
	}
	
	public static void copyFileFromJar(String targetFilePath, String destFilePath) {
		InputStream stream = Class.class.getResourceAsStream(targetFilePath);
	    if (stream == null) {
	    	System.err.println("Failed to load file from jar");
	    }
	    OutputStream resStreamOut;
	    int readBytes;
	    byte[] buffer = new byte[4096];
	    try {
	        resStreamOut = new FileOutputStream(new File(destFilePath));
	        while ((readBytes = stream.read(buffer)) > 0) {
	            resStreamOut.write(buffer, 0, readBytes);
	        }
	        stream.close();
	        resStreamOut.close();
	    } catch (IOException e1) {
	        e1.printStackTrace();
	    }
	}
	
	/**
	 * 
	 * @param relativePath the path relative to our program jar
	 */
	public static void loadJarNativeLib(String relativePath) {
		Logging.logger.log(Level.INFO, "Beginning of loadJarNativeLib.");
	    try
	    {
		    Logging.logger.log(Level.INFO, "Begin loading temp library file: " + relativePath + ".");
	    	
		    java.io.InputStream in = Class.class.getClass().getResourceAsStream(relativePath);
	    	byte[] buffer = new byte[1024];
		    int read = -1;
		    /* extract the dll file name*/
		    File dllFile = new File(relativePath);
		    String prefix = dllFile.getName();	
		    
		    File tmpFolder = new File(GLOBALVAR.tmpDir);
		    if(!tmpFolder.exists())
		    	tmpFolder.mkdir();
		    File tempNativeLibFile = new File(tmpFolder.getAbsoluteFile() + "/" + dllFile.getName());
		    
		    /* copy the original dll file into our own temp folder*/
		    java.io.FileOutputStream fos = new FileOutputStream(tempNativeLibFile);
		    while((read = in.read(buffer)) != -1) {
		        fos.write(buffer, 0, read);
		    }
		    fos.close();
		    in.close();
		    
		    
		    /* load temp file as dll*/
		    try {
		    	System.load(tempNativeLibFile.getAbsolutePath());
		    } catch (Exception ex) {
		    	Logging.logger.log(Level.SEVERE, ex.getMessage(), ex);
		    } finally {
		    	Logging.logger.log(Level.INFO, "Loading temp library file successful.");
		    }
	    }
	    catch(IOException e)
	    {
	    	Logging.logger.log(Level.SEVERE, e.getMessage());
	    	e.printStackTrace();
	    }

	    Logging.logger.log(Level.FINE, "End of loadJarNativeLib.");
	}

	/**
	 *	Stable array sort, can keep positions of elements in original array, like what Matlab "sort" does	
	 *	@param array a n * 2 array, n is the number of elements need to be sort, the second column keeps positions of elements in original array
	 *	@return
	 */
	public static double[][] mySort(double[][] unSortedArray, boolean asc/* ascend or not*/) {
        if (unSortedArray.length == 1) {
            return unSortedArray;
        } else {
            double[][] arrayL = new double[unSortedArray.length / 2][];
            double[][] arrayR = new double[unSortedArray.length - unSortedArray.length / 2][];
            int center = unSortedArray.length / 2;
            for (int i = 0; i < center; i++) {
                arrayL[i] = unSortedArray[i];
            }
            for (int i = center, j = 0; i < unSortedArray.length; i++, j++) {
                arrayR[j] = unSortedArray[i];
            }
 
            double[][] sortedArrayL=mySort(arrayL, asc);
            double[][] sortedArrayR=mySort(arrayR, asc);
            double[][] sortedArray = mergeTwoList(sortedArrayL, sortedArrayR, asc);
            return sortedArray;
        }
    }
 
	/* merge two list*/
    private static double[][] mergeTwoList(double[][] arrayL, double[][] arrayR, boolean asc/* ascend or not*/) {
        int i = 0, j = 0;
        double[][] sortedArray = new double[arrayL.length + arrayR.length][];
        int foot = 0;
        
        if(asc)
        {
        	while (i < arrayL.length && j < arrayR.length) {
                if (arrayL[i][0] <= arrayR[j][0]) {
                    sortedArray[foot] = arrayL[i];
                    i++;
                } else {
                    sortedArray[foot] = arrayR[j];
                    j++;
                }
                foot++;
            }
        }
        else
        {
        	while (i < arrayL.length && j < arrayR.length) {
                if (arrayL[i][0] >= arrayR[j][0]) {
                    sortedArray[foot] = arrayL[i];
                    i++;
                } else {
                    sortedArray[foot] = arrayR[j];
                    j++;
                }
                foot++;
            }
        }
        
        if (i == arrayL.length) {
            while (j < arrayR.length) {
                sortedArray[foot++] = arrayR[j++];
            }
        } else { // j==arrayR.length
            while (i < arrayL.length) {
                sortedArray[foot++] = arrayL[i++];
            }
        }
        return sortedArray;
    }
    
    public static InputData readInput(FileReader inputFileReader, boolean withHeader, boolean genesAreColumnHeader) 
    		throws BadInputFormatException, IOException{

    	Pattern inputLineNotHeaderFormatPattern = Pattern.compile("(\\-)?\\d+(\\.\\d+)?(\t|\\s(\\-)?\\d+(\\.\\d+)?)*");
    	Matcher inputLineNotHeaderFormatMatcher = null;
    	Pattern inputLineHeaderFormatPattern = Pattern.compile("\t\\S+(\t\\S+)*");
    	Matcher inputLineHeaderFormatMatcher = null;
    	
    	ArrayList<ArrayList<Double>> inputDataMatrix = new ArrayList<ArrayList<Double>>();
    	if(!withHeader)	//without header
    	{
    		if(genesAreColumnHeader)
    		{
    			BufferedReader brInputFile = new BufferedReader(inputFileReader);
    	    	String oneLine = "";
				while ((oneLine = brInputFile.readLine()) != null) {
					if(!oneLine.equals("")) {
						inputLineNotHeaderFormatMatcher = inputLineNotHeaderFormatPattern.matcher(oneLine);
						if(!inputLineNotHeaderFormatMatcher.matches()) {
							throw new BadInputFormatException("Input matrix data format is not correct");
						}

						ArrayList<Double> oneLineArrayList = new ArrayList<Double>();
						Scanner sc = new Scanner(oneLine);
						while(sc.hasNext()) {
							oneLineArrayList.add(sc.nextDouble());
						}
						inputDataMatrix.add(oneLineArrayList);
						sc.close();
					}
				}
				brInputFile.close();
				
				return new InputData(null, null, inputDataMatrix);
    		}
    		else
    		{
    			/* read data into a temporary matrix whose column header is time point and then convert the temporary matrix
    			 * into a matrix whose column header is gene name*/
    			ArrayList<ArrayList<Double>> tempInputDataMatrix = new ArrayList<ArrayList<Double>>();
    			BufferedReader brInputFile = new BufferedReader(inputFileReader);
    	    	String oneLine = "";
				while ((oneLine = brInputFile.readLine()) != null) {
					if(!oneLine.equals("")) {
						inputLineNotHeaderFormatMatcher = inputLineNotHeaderFormatPattern.matcher(oneLine);
						if(!inputLineNotHeaderFormatMatcher.matches()) {
							throw new BadInputFormatException("Input matrix data format is not correct");
						}

						ArrayList<Double> oneLineArrayList = new ArrayList<Double>();
						Scanner sc = new Scanner(oneLine);
						while(sc.hasNext()) {
							oneLineArrayList.add(sc.nextDouble());
						}
						tempInputDataMatrix.add(oneLineArrayList);
						sc.close();
					}
				}
				brInputFile.close();
				
				/* switch rows and columns*/
				for(int j = 0; j< tempInputDataMatrix.get(0).size(); j++)
				{
					ArrayList<Double> rowArrayList = new ArrayList<Double>();
					for(int i = 0; i< tempInputDataMatrix.size(); i++)
					{
						rowArrayList.add(tempInputDataMatrix.get(i).get(j));
					}
					inputDataMatrix.add(rowArrayList);
				}
				
				return new InputData(null, null, inputDataMatrix);
    		}
    	}
    	else //with header
    	{
    		if(genesAreColumnHeader)
    		{
    			BufferedReader brInputFile = new BufferedReader(inputFileReader);
    			/* get the column header*/
    			String columnHeaderLine = brInputFile.readLine();
    			inputLineHeaderFormatMatcher = inputLineHeaderFormatPattern.matcher(columnHeaderLine);
    			if(!inputLineHeaderFormatMatcher.matches()) {
    				throw new BadInputFormatException("Bad header format in the input");
    			}

    			String[] columnHeaderArray = columnHeaderLine.trim().split("\\s");
    			ArrayList<String> rowHeader = new ArrayList<String>();
    			/* store the data without header*/
    	    	String oneLine = "";
				while ((oneLine = brInputFile.readLine()) != null) {
					if(!oneLine.equals("")) {
						String[] oneLineArray = oneLine.split("\\s");

						if(oneLineArray.length != columnHeaderArray.length + 1) {
							throw new BadInputFormatException("Bad header format in the input");
						}

						/* store row header*/
						rowHeader.add(oneLineArray[0].trim());
						/* store data*/
						ArrayList<Double> oneLineArrayList = new ArrayList<Double>();
						for(int i = 1; i< oneLineArray.length; i++) 
						{
							oneLineArrayList.add(Double.parseDouble(oneLineArray[i].trim()));
						}
						inputDataMatrix.add(oneLineArrayList);
					}
				}
				brInputFile.close();
				
				/* real column header (from back to front)*/
				int columnHeaderLength = inputDataMatrix.get(0).size();
				ArrayList<String> columnHeader = new ArrayList<String>();
				for(int i = 0; i < columnHeaderLength; i++)
					columnHeader.add(0, columnHeaderArray[columnHeaderArray.length - 1 - i]);
				
				return new InputData(rowHeader, columnHeader, inputDataMatrix);
    		}
    		else
    		{
    			BufferedReader brInputFile = new BufferedReader(inputFileReader);
    			/* get the row header*/
    			String rowHeaderLine = brInputFile.readLine();
    			inputLineHeaderFormatMatcher = inputLineHeaderFormatPattern.matcher(rowHeaderLine);
    			if(!inputLineHeaderFormatMatcher.matches()) {
    				throw new BadInputFormatException("Bad header format in the input");
    			}
    			String[] rowHeaderArray = rowHeaderLine.trim().split("\\s");
    			ArrayList<String> columnHeader = new ArrayList<String>();
    			/* store the data without header*/
    			ArrayList<ArrayList<Double>> tempInputDataMatrix = new ArrayList<ArrayList<Double>>();
    			String oneLine = "";
    			while((oneLine = brInputFile.readLine()) != null) {
    				if(!oneLine.equals("")) {
	    				String[] oneLineArray = oneLine.split("\\s");
	    				
	    				if(oneLineArray.length != rowHeaderArray.length + 1) {
							throw new BadInputFormatException("Bad header format in the input");
						}

	    				/* store column header*/
	    				columnHeader.add(oneLineArray[0].trim());
	    				/* store data*/
	    				ArrayList<Double> oneLineArrayList = new ArrayList<Double>();
	    				for(int i = 1; i< oneLineArray.length; i++)
	    				{
	    					oneLineArrayList.add(Double.parseDouble(oneLineArray[i].trim()));
	    				}
	    				tempInputDataMatrix.add(oneLineArrayList);
    				}
    			}
    			brInputFile.close();
    			
    			/* switch*/
    			for(int j = 0; j< tempInputDataMatrix.get(0).size(); j++)
    			{
    				ArrayList<Double> rowArrayList = new ArrayList<Double>();
    				for(int i = 0; i< tempInputDataMatrix.size(); i++)
    					rowArrayList.add(tempInputDataMatrix.get(i).get(j));
    				inputDataMatrix.add(rowArrayList);
    			}
    				
    			int rowHeaderLength = inputDataMatrix.size();
    			ArrayList<String> rowHeader = new ArrayList<String>();
    			for(int i = 0; i < rowHeaderLength; i++)
    				rowHeader.add(0, rowHeaderArray[rowHeaderArray.length - 1 - i]);
    			
    			return new InputData(rowHeader, columnHeader, inputDataMatrix);
    		}
    	}
    }
    
    public static void resultPrinter(int network [][], int numberOfGenes, ArrayList<String> geneNames, boolean runByMeta, String metaNetworkReferenceResultFilePath, int[][] timeDelay)
	{	
		if(!runByMeta)
		{
			new ResultDialog(null, network, numberOfGenes, geneNames, timeDelay).setVisible(true);
			
		} else { 
			
			try {
				File resultFile = new File(metaNetworkReferenceResultFilePath);
//				resultFile.deleteOnExit();
				PrintStream resultFilePrinter = new PrintStream(resultFile);
				if(geneNames != null) {
					for(int m=0; m<numberOfGenes; m++)
					{
						for(int n=0; n<numberOfGenes; n++) 
						{   
							if(network[m][n] == 1) 
								resultFilePrinter.print(geneNames.get(m) + "\t" + geneNames.get(n) + "\t" + 1);
							else
		    				   	resultFilePrinter.print(geneNames.get(m) + "\t" + geneNames.get(n) + "\t" + 0);
							
							resultFilePrinter.println();
						}
					}
				} else {
					for(int m=0; m<numberOfGenes; m++)
					{
						for(int n=0; n<numberOfGenes; n++) 
						{   
							if(network[m][n] == 1) 
								resultFilePrinter.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 1);
							else
		    				   	resultFilePrinter.print("G" + (m+1) + "\t" + "G" + (n+1) + "\t" + 0);
							
							resultFilePrinter.println();
						}
					}
				}
				resultFilePrinter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    
   
}
