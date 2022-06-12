
public class ClassifierDataSet {
	public Double[][] features;	// 2D array of all the supplied feature data
	public Double[] trueClass;		// column of supplied classes
	
	public ClassifierDataSet(int numCols, int numRows) {
		this.features = new Double[numCols -1][numRows];		// -1 because class column is separate
		this.trueClass = new Double[numRows];					// class column
	}
}
