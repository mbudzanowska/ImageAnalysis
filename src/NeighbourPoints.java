
public class NeighbourPoints {

	private Point point_1st; // 1st image
	private Point point_2nd; // 1st or 2nd image (depend on use)
	private double distance;

	public NeighbourPoints(Point image_1_point, Point image_2_point) {
		this.point_1st = image_1_point;
		this.point_2nd = image_2_point;
		this.distance = 0;
	}

	public NeighbourPoints(Point image_1_point, Point image_2_point, double distance) {
		this.point_1st = image_1_point;
		this.point_2nd = image_2_point;
		this.distance = distance;
	}

	public Point getFirstPoint() {
		return point_1st;
	}

	public Point getSecondPoint() {
		return point_2nd;
	}

	public double getDistance() {
		return distance;
	}

}
