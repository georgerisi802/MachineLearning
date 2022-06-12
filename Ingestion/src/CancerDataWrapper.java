import java.io.*;
import java.io.BufferedReader;
import java.util.Queue;
import java.util.LinkedList;

public class CancerDataWrapper {
	
//#### DATA ####
	
	
	// Basic data parameters
		public int numRows;		// Total number of rows
		public int numCols;		// Total number of columns 
		public int numClasses;	// Number of classes
		// Number of instances of each class (maybe store the row of each instance in a queue 
		// ... to distribute to the folds).
	// Headers
		public ColumnHeader[] header; 	// Array of structs containing label & statistics
	// Data body
		public ClassifierDataSet data;
	// Separate structs for each fold
		public ClassifierDataSet fold1;
		public ClassifierDataSet fold2;
		public ClassifierDataSet fold3;
		public ClassifierDataSet fold4;
		public ClassifierDataSet fold5;
		
		
// #### Methods ####
	
	/**
	 * Constructor
	 */
	public CancerDataWrapper(int numCols, int numRows, String fileName) {
		this.numCols = numCols;
		this.numRows = numRows;
		
		this.header = new ColumnHeader[numCols - 1]; 		// -1 dont need header for class
		this.data = new ClassifierDataSet(numCols, numRows);
		
		setHeaderLabels();
		readData(fileName);

		// fold();
	}
	
	/**
	 * Method to load from file
	 * 
	 * Perform any necessary tasks unique to dataset like renaming column headers, performing log 
	 * transformations, or substituting one value name for another
	 */
	private void readData(String fileName) {
		Queue<Integer[]> missingDataQueue = new LinkedList<>();
		
		try(BufferedReader input = new BufferedReader(new FileReader(fileName))){
			String[] line;
			String str;
			
			for(int row=0; row<numRows; row++) {
				line = input.readLine().split(",");
				
				if(line.length != numCols) throw new IOException("A line in " + fileName 
						+ " is the wrong size");
				for(int col=0; col<numCols - 1; col++) {		// -1 because separate step for the class column
					str = line[col];
					
					if(str.equals("?") || str.equals("NaN")) {
						missingDataQueue.add(new Integer[]{col, row});	// empty data will be filled in later
						this.data.features[col][row] = Double.MAX_VALUE;
					}
					else {
						this.data.features[col][row] = Double.valueOf(str);
						this.header[col].stats.addValue(Double.valueOf(str));
					}
				}
				data.trueClass[row] = Double.valueOf(line[numCols - 1]);		// Last col of line is class
			}
		}
		catch(IOException e) {System.out.println(e);}
		
		replaceMissing(missingDataQueue);
	}
	
	/**
	 * Handle Missing Data (imputation)
	 * 
	 * Replace "?" and "NaN" with mean of the feature (column)
	 * @param q
	 */
	private void replaceMissing(Queue<Integer[]> q) {
		
		while(!q.isEmpty()) {
			Integer[] location = q.remove();
			this.data.features[location[0]][location[1]] = this.header[location[0]].stats.getMean();
		}
	}
	
	private void setHeaderLabels() {
		header[0] = new ColumnHeader("Sample Code");
		header[1] = new ColumnHeader("Clump Thickness");
		header[2] = new ColumnHeader("Uniformity of Cell Size");
		header[3] = new ColumnHeader("Uniformity of Cell Shape");
		header[4] = new ColumnHeader("Marginal Adhesion");
		header[5] = new ColumnHeader("Single Epithelial Cell Size");
		header[6] = new ColumnHeader("Bare Nuclei");
		header[7] = new ColumnHeader("Bland Chromatin");
		header[8] = new ColumnHeader("Normal Nucleoli");
		header[9] = new ColumnHeader("Mitosis");
	}
	
	// Handle Categorical Data (can be subdidvided into groups)
		// Ordinal data can be encoded (as 0 ,1, 2) for example in a way that preserves the natural 
		// ...relationship
		
		// Nominal data using one-hot encoding
	
	
	// Discretization (using binning)
		// Must support both equal-width and Equal-frequency discretization given an argument for 
		// ...number of bins
	
	
	// Standardization (using z-score)
		// Find mu (mean) and sigma (standard deviation) for each feature using the training data only.
		// Could handle missing data here (???)
		// Apply z(x)  = (x-mu)/(sigma) to both the training and the test data set
	public void zStandardize() {
		for(int col = 1; col<numCols - 1; col++) {	// doesn't start at 1 becuase that is an ID field
			for(Double elem : data.features[col]) {
				elem = (elem - this.header[col].stats.getMean())/this.header[col].stats.getStandardDeviation();
			}
		}
	}
	
	/**
	 * Folding for Cross-validation
	 * 
	 * Split 80/20
	 * Split the 80 into 5 folds (stratified)
	 * For classification, each fold should stratified by the y class label 
	 */
	private void fold() {
		
	}
	
	/**
	 * To String
	 * 
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		for(ColumnHeader hdr : header) {
			sb.append(hdr.label + "\t");
		}
		sb.append("Class");
		sb.append("\n");
		for(ColumnHeader hdr : header) {
			sb.append(hdr.stats.getMean() + "\t\t");
		}
		sb.append("\n");
		for(int row=0; row<numRows;row++) {
			for(int col=0; col<numCols - 1; col++) {
				sb.append(this.data.features[col][row] + "\t\t");
			}
			sb.append(this.data.trueClass[row]);
			sb.append("\n");
		}
		return sb.toString();
	}
}
