import java.util.ArrayList;
import java.util.List;

public class Point {

	private double x;
	private double y;
	private Integer[] features;
	private List<Point> borderers;
	private Point nearest_neighbour;

	public Point(double x, double y, Integer[] features) {
		this.x = x;
		this.y = y;
		this.features = features;
		borderers = new ArrayList<Point>();
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

	public void addBorderer(Point p) {
		borderers.add(p);
	}

	public List<Point> getBorderers() {
		return borderers;
	}

	public Point getNearest_neighbour() {
		return nearest_neighbour;
	}

	public void setNearest_neighbour(Point nearest_neighbour) {
		this.nearest_neighbour = nearest_neighbour;
	}
}
