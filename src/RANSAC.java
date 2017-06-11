import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import Jama.*;

public class RANSAC {

	private static Image image_1st;
	private static Image image_2nd;
	private static int features_number;
	private static final Transformation transformation_type = Transformation.AFFINE;
	private static final int ITERATIONS_NUMBER = 20;
	private static final int SAMPLES_NUMBER = 5;

	public RANSAC(Image image_1st, Image image_2nd) {

		RANSAC.image_1st = image_1st;
		RANSAC.image_2nd = image_2nd;
		features_number = image_1st.getFeaturesNumber();
	}

	public List<NeighbourPoints> getCoupleKeyPoints() {

		setAllClosestPointsOnSecondImage(image_1st, image_2nd);
		setAllClosestPointsOnSecondImage(image_2nd, image_1st);

		System.out.println("First image key points: " + image_1st.getPointsNumber());
		System.out.println("Second image key points: " + image_2nd.getPointsNumber());

		List<NeighbourPoints> key_points = findAllPossibleCouples();
		System.out.println("Number of possible neighbour points: " + key_points.size());

		return consistent_points;
	}

	private void setAllClosestPointsOnSecondImage(Image image_1st, Image image_2nd) {
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

	private List<NeighbourPoints> findAllPossibleCouples() {
		List<NeighbourPoints> couple_key_points = new ArrayList<NeighbourPoints>();
		for (Point point_1st : image_1st.getPoints()) {
			for (Point point_2nd : image_2nd.getPoints()) {
				if (point_1st.getNearest_neighbour() == point_2nd && point_2nd.getNearest_neighbour() == point_1st)
					couple_key_points.add(new NeighbourPoints(point_1st, point_2nd));
			}
		}
		return couple_key_points;
	}

	private double[][] chooseBestModel(List<NeighbourPoints> key_points) {

		double[][] best_model = null;
		double best_score = 0;

		for (int i = 0; i < ITERATIONS_NUMBER; i++) {
			double[][] model = null;
			NeighbourPoints[] randomly_chosen_points;
			int score = 0;

			switch (transformation_type) {
			case AFFINE: {
				randomly_chosen_points = chosePointsRandomly(key_points, 3);
				model = calculateAffineModel(randomly_chosen_points);
				break;
			}
			case PERSPECTIVE: {
				randomly_chosen_points = chosePointsRandomly(key_points, 4);
				model = calculatePerspectiveModel(randomly_chosen_points);
				break;
			}
			}

			for (NeighbourPoints neighbour_point : key_points) {
				double error = countError(model, neighbour_point);
				if (error < max_error) // what the fuck is max error
					score++;
			}

			if (score > best_score) {
				best_score = score;
				best_model = model;
			}
		}

		return best_model;
	}

	private double countError(double[][] model, NeighbourPoints key_points) {
		// TODO Auto-generated method stub
		return 0;
	}

	private Matrix calculateAffineModel(NeighbourPoints[] chosen_points) {

		double x1 = chosen_points[0].getFirstPoint().getX();
		double x2 = chosen_points[1].getFirstPoint().getX();
		double x3 = chosen_points[2].getFirstPoint().getX();
		double y1 = chosen_points[0].getFirstPoint().getY();
		double y2 = chosen_points[1].getFirstPoint().getY();
		double y3 = chosen_points[2].getFirstPoint().getY();

		double u1 = chosen_points[0].getSecondPoint().getX();
		double u2 = chosen_points[1].getSecondPoint().getX();
		double u3 = chosen_points[2].getSecondPoint().getX();
		double v1 = chosen_points[0].getSecondPoint().getY();
		double v2 = chosen_points[1].getSecondPoint().getY();
		double v3 = chosen_points[2].getSecondPoint().getY();

		double[][] image_1_matrix = { { x1, y1, 1, 0, 0, 0 }, { x2, y2, 1, 0, 0, 0 }, { x3, y3, 1, 0, 0, 0 },
				{ 0, 0, 0, x1, y1, 1 }, { 0, 0, 0, x2, y2, 1 }, { 0, 0, 0, x3, y3, 1 } };

		double[][] image_2_matrix = { { u1 }, { u2 }, { u3 }, { v1 }, { v2 }, { v3 } };

		Matrix m1 = new Matrix(image_1_matrix);
		Matrix m2 = new Matrix(image_2_matrix);
		
		Matrix A = m1.inverse().times(m2);

		double [][] result = {A.
		return 
	}

	private Matrix calculatePerspectiveModel(NeighbourPoints[] chosen_points) {

		double x1 = chosen_points[0].getFirstPoint().getX();
		double x2 = chosen_points[1].getFirstPoint().getX();
		double x3 = chosen_points[2].getFirstPoint().getX();
		double x4 = chosen_points[3].getFirstPoint().getX();
		double y1 = chosen_points[0].getFirstPoint().getY();
		double y2 = chosen_points[1].getFirstPoint().getY();
		double y3 = chosen_points[2].getFirstPoint().getY();
		double y4 = chosen_points[3].getFirstPoint().getY();

		double u1 = chosen_points[0].getSecondPoint().getX();
		double u2 = chosen_points[1].getSecondPoint().getX();
		double u3 = chosen_points[2].getSecondPoint().getX();
		double u4 = chosen_points[3].getSecondPoint().getX();
		double v1 = chosen_points[0].getSecondPoint().getY();
		double v2 = chosen_points[1].getSecondPoint().getY();
		double v3 = chosen_points[2].getSecondPoint().getY();
		double v4 = chosen_points[3].getSecondPoint().getY();

		double[][] image_1_matrix = { { x1, y1, 1, 0, 0, 0, -u1 * x1, -u1 * y1 },
				{ x2, y2, 1, 0, 0, 0, -u2 * x2, -u2 * y2 }, { x3, y3, 1, 0, 0, 0, -u3 * x3, -u3 * y3 },
				{ x4, y4, 1, 0, 0, 0, -u4 * x4, -u4 * y4 }, { 0, 0, 0, x1, y1, 1, -v1 * x1, -v1 * y1 },
				{ 0, 0, 0, x2, y2, 1, -v2 * x2, -v2 * y2 }, { 0, 0, 0, x3, y3, 1, -v3 * x3, -v3 * y3 },
				{ 0, 0, 0, x3, y3, 1, -v4 * x4, -v4 * y4 } };

		double[][] image_2_matrix = { { u1 }, { u2 }, { u3 }, { u4 }, { v1 }, { v2 }, { v3 }, { v4 } };

		Matrix m1 = new Matrix(image_1_matrix);
		Matrix m2 = new Matrix(image_2_matrix);

		return m1.inverse().times(m2);
	}

	private NeighbourPoints[] chosePointsRandomly(List<NeighbourPoints> key_points, int number) {
		NeighbourPoints[] randomly_chosen_points = new NeighbourPoints[number];
		Random random = new Random();
		for (int i = 0; i < number; i++)
			randomly_chosen_points[i] = key_points.get(random.nextInt(key_points.size()));
		return randomly_chosen_points;
	}

}
