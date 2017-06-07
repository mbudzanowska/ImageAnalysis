public class Point {

	private double x;
	private double y;
	private Integer[] features;

	public Point(double x, double y, Integer[] features2) {
		this.x = x;
		this.y = y;
		this.features = features2;
	}
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Integer[] getFeatures() {
		return features;
	}
}
