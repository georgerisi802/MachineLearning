import java.io.*;

public class NullModelTester {
	
	public static void main(String[] args) {
		
		//NullClassificationModel ncm = new NullClassificationModel();
		
		CancerDataWrapper cd = new CancerDataWrapper(11, 699, 
				"/home/george/repos/MachineLearning/DataSets/Classification/BreastCancer/breast-cancer-wisconsin.data");
		
		try(FileWriter out = new FileWriter(new File("ndfsd.txt"))) {
			
			cd.zStandardize();
			
			out.write(cd.toString());
			
		}
		catch(IOException e) {}
	}
}
