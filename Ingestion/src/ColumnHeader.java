import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class ColumnHeader{
	public String label;
	public DescriptiveStatistics stats;
	
	public ColumnHeader() {
		this.label = new String();
		this.stats = new DescriptiveStatistics();
	}
	
	public ColumnHeader(String l) {
		this.label = new String(l);
		this.stats = new DescriptiveStatistics();
	}
}
