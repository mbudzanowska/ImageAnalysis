import java.util.ArrayList;
import java.util.List;


public class AlgorithmsBasics {

	protected static Image image_1st;
	protected static Image image_2nd;
	protected static int features_number;

	public AlgorithmsBasics() {
		super();
	}

	protected void setAllClosestPointsOnSecondImage(Image image_1st, Image image_2nd) {
		for (Point point : image_1st.getPoints())
			point.setNearest_neighbour(getClosestPointOnImage(point, image_2nd));
	}

	private Point getClosestPointOnImage(Point point, Image image) {
		Point closest = null;
		double distance = Double.POSITIVE_INFINITY;
	
		for (Point image_point : image.getPoints()) {
			double computed_distance = getEuclidDistance(point, image_point);
			if (computed_distance < distance) {
				distance = computed_distance;
				closest = image_point;
			}
		}
		return closest;
	}

	private double getEuclidDistance(Point a, Point b) {
		Integer[] a_features = a.getFeatures();
		Integer[] b_features = b.getFeatures();
		double distance = 0;
		for (int i = 0; i < features_number; i++)
			distance += Math.pow(a_features[i] - b_features[i], 2);
		distance = Math.sqrt(distance);
		return distance;
	}

	protected List<NeighbourPoints> findAllPossibleCouples() {
		List<NeighbourPoints> couple_key_points = new ArrayList<NeighbourPoints>();
		for (Point point_1st : image_1st.getPoints()) {
			for (Point point_2nd : image_2nd.getPoints()) {
				if (point_1st.getNearest_neighbour() == point_2nd && point_2nd.getNearest_neighbour() == point_1st)
					couple_key_points.add(new NeighbourPoints(point_1st, point_2nd));
			}
		}
		return couple_key_points;
	}

	protected double getDistance(Point a, Point b) {
		return Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
	}

}