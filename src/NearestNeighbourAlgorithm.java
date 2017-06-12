import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NearestNeighbourAlgorithm extends AlgorithmsBasics {

	// BORDERER - on the same image
	// NEIGHBOUR - on second image

	private static int NEIGBOUR_SIZE;
	private static int ACCEPTED_THRESHOLD;

	public NearestNeighbourAlgorithm(Image image_1st, Image image_2nd) {
		NearestNeighbourAlgorithm.image_1st = image_1st;
		NearestNeighbourAlgorithm.image_2nd = image_2nd;
		features_number = image_1st.getFeaturesNumber();
		NEIGBOUR_SIZE = (int) (0.02 * Math.min(image_1st.getPointsNumber(), image_2nd.getPointsNumber()));
		ACCEPTED_THRESHOLD = (int) (0.3 * NEIGBOUR_SIZE);
	}

	public List<NeighbourPoints> getCoupleKeyPoints() {

		processImage(image_1st, image_2nd);
		processImage(image_2nd, image_1st);

		System.out.println("First image key points: " + image_1st.getPointsNumber());
		System.out.println("Second image key points: " + image_2nd.getPointsNumber());

		List<NeighbourPoints> key_points = findAllPossibleCouples();
		System.out.println("Number of possible neighbour points: " + key_points.size());

		List<NeighbourPoints> consistent_points = discardBelowThresholdCouples(key_points);
		System.out.println("Number of consistent key points: " + consistent_points.size());
		return consistent_points;
	}

	public void processImage(Image image, Image second_image) {
		setAllBorderers(image);
		setAllClosestPointsOnSecondImage(image, second_image);
	}

	private void setAllBorderers(Image image) {
		for (Point point : image.getPoints())
			setPointBorderers(point, image);
	}

	private void setPointBorderers(Point point, Image image) {
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

	private List<NeighbourPoints> discardBelowThresholdCouples(List<NeighbourPoints> couple_key_points) {

		List<NeighbourPoints> key_points = new ArrayList<NeighbourPoints>();

		for (NeighbourPoints couple_points : couple_key_points) {
			List<Point> second_point_borderers = couple_points.getSecondPoint().getBorderers();
			int consistent_points = 0;
			for (Point first_point_borderers : couple_points.getFirstPoint().getBorderers()) {
				if (second_point_borderers.contains(first_point_borderers.getNearest_neighbour()))
					consistent_points++;
			}
			if (consistent_points >= ACCEPTED_THRESHOLD)
				key_points.add(couple_points);
		}
		return key_points;
	}
}
