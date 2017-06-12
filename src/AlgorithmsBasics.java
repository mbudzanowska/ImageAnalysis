import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlgorithmsBasics {

	protected static Image image_1st;
	protected static Image image_2nd;
	protected static int features_number;
	protected static int NEIGBOUR_SIZE;
	protected static int ACCEPTED_THRESHOLD;


	public AlgorithmsBasics(Image image_1st, Image image_2nd) {
		AlgorithmsBasics.image_1st = image_1st;
		AlgorithmsBasics.image_2nd = image_2nd;
		features_number = image_1st.getFeaturesNumber();
		NEIGBOUR_SIZE = (int) (0.02 * Math.min(image_1st.getPointsNumber(), image_2nd.getPointsNumber()));
		ACCEPTED_THRESHOLD = (int) (0.3 * NEIGBOUR_SIZE);
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

	protected void setPointBorderers(Point point, Image image) {
		List<NeighbourPoints> borderers = new ArrayList<NeighbourPoints>();
		for (Point image_point : image.getPoints()) {
			borderers.add(new NeighbourPoints(point, image_point, getDistance(point, image_point)));
		}
		Collections.sort(borderers, new NeighboursComparator());
	
		borderers = borderers.subList(1, NEIGBOUR_SIZE + 1);
	
		for (NeighbourPoints borderer : borderers) {
			point.addBorderer(borderer.getSecondPoint());
		}
	}

}