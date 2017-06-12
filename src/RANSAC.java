import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import Jama.*;

public class RANSAC extends AlgorithmsBasics {

	// private static final Transformation transformation_type =
	// Transformation.AFFINE;
	private static final Transformation transformation_type = Transformation.PERSPECTIVE;
	private static final int ITERATIONS_NUMBER = 1000;
	private static final double MAX_ERROR = 20;
	private static double r;
	private static double R;

	public RANSAC(Image image_1st, Image image_2nd) {
		super(image_1st, image_2nd);
		R = 0.3 * Math.max(Math.max(image_1st.getImage().getHeight(null), image_2nd.getImage().getHeight(null)),
				Math.max(image_1st.getImage().getWidth(null), image_2nd.getImage().getWidth(null)));
		r = R / 30;
	}

	public List<NeighbourPoints> getCoupleKeyPoints() {

		setAllClosestPointsOnSecondImage(image_1st, image_2nd);
		setAllClosestPointsOnSecondImage(image_2nd, image_1st);

		System.out.println("First image key points: " + image_1st.getPointsNumber());
		System.out.println("Second image key points: " + image_2nd.getPointsNumber());

		List<NeighbourPoints> key_points = findAllPossibleCouples();
		System.out.println("Number of possible neighbour points: " + key_points.size());

		double[][] model = chooseBestModel(key_points);

		List<NeighbourPoints> consistent_points = getAllPointsWithModel(model, key_points);
		System.out.println("Number of consistent key points: " + consistent_points.size());
		return consistent_points;
	}

	private List<NeighbourPoints> getAllPointsWithModel(double[][] model, List<NeighbourPoints> key_points) {

		List<NeighbourPoints> consistent_poinst = new ArrayList<NeighbourPoints>();
		for (NeighbourPoints pair : key_points) {
			Point counted_point = countPointWithModel(model, pair.getFirstPoint());
			if (getDistance(counted_point, pair.getSecondPoint()) < MAX_ERROR)
				consistent_poinst.add(new NeighbourPoints(pair.getFirstPoint(), counted_point));
		}
		return consistent_poinst;
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
				// randomly_chosen_points = choosePointsSemiRandomly(key_points,
				// 3);
				randomly_chosen_points = choosePointsRandomly(key_points, 3);
				model = calculateAffineModel(randomly_chosen_points);
				break;
			}
			case PERSPECTIVE: {
				// randomly_chosen_points = choosePointsSemiRandomly(key_points,
				// 4);
				randomly_chosen_points = choosePointsRandomly(key_points, 4);
				model = calculatePerspectiveModel(randomly_chosen_points);
				break;
			}
			}
			for (NeighbourPoints neighbour_point : key_points) {
				double error = countError(model, neighbour_point);

				if (error < MAX_ERROR)
					score++;
			}

			if (score > best_score) {
				// System.out.println("NEW MODDE");
				best_score = score;
				best_model = model;
			}
		}

		return best_model;
	}

	private double countError(double[][] model, NeighbourPoints key_points) {
		Point a = countPointWithModel(model, key_points.getFirstPoint());
		return getDistance(a, key_points.getSecondPoint());
	}

	private Point countPointWithModel(double[][] model, Point point) {

		double x_val = model[0][0] * point.getX() + model[0][1] * point.getY() + model[0][2];
		double y_val = model[1][0] * point.getX() + model[1][1] * point.getY() + model[1][2];
		double z_val = model[2][0] * point.getX() + model[2][1] * point.getY() + model[2][2];

		x_val = x_val / z_val;
		y_val = y_val / z_val;
		return new Point(x_val, y_val, null);
	}

	private double[][] calculateAffineModel(NeighbourPoints[] chosen_points) {

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
		Matrix A;
		try {
			A = m1.inverse().times(m2);
		} catch (RuntimeException ex) {
			A = new Matrix(9, 1);
		}

		double[][] result = { { A.get(0, 0), A.get(1, 0), A.get(2, 0) }, { A.get(3, 0), A.get(4, 0), A.get(5, 0) },
				{ 0, 0, 1 } };

		return result;
	}

	private double[][] calculatePerspectiveModel(NeighbourPoints[] chosen_points) {

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

		Matrix A;
		try {
			A = m1.inverse().times(m2);
		} catch (RuntimeException ex) {
			A = new Matrix(9, 1);
		}

		double[][] result = { { A.get(0, 0), A.get(1, 0), A.get(2, 0) }, { A.get(3, 0), A.get(4, 0), A.get(5, 0) },
				{ A.get(6, 0), A.get(7, 0), 1 } };

		return result;
	}

	private NeighbourPoints[] choosePointsRandomly(List<NeighbourPoints> key_points, int number) {
		NeighbourPoints[] randomly_chosen_points = new NeighbourPoints[number];
		Random random = new Random();
		for (int i = 0; i < number; i++)
			randomly_chosen_points[i] = key_points.get(random.nextInt(key_points.size()));
		return randomly_chosen_points;
	}

	private NeighbourPoints[] choosePointsSemiRandomly(List<NeighbourPoints> key_points, int number) {
		NeighbourPoints[] randomly_chosen_points = new NeighbourPoints[number];
		Random random = new Random();
		randomly_chosen_points[0] = key_points.get(random.nextInt(key_points.size()));
		Point first_point = randomly_chosen_points[0].getFirstPoint();
		setPointBorderers(first_point, image_1st);

		List<Point> borderers = first_point.getBorderers();
		// System.out.println(borderers.size());

		for (int i = 1; i < number; i++) {
			do {
				randomly_chosen_points[i] = findKeyPoints(borderers.get(random.nextInt(borderers.size())), key_points);
				if (randomly_chosen_points[i] != null) {
					double distance = getDistance(randomly_chosen_points[i].getFirstPoint(),
							randomly_chosen_points[0].getFirstPoint());
					distance = Math.pow(distance, 2);
					if (distance < Math.pow(r, 2) || distance > Math.pow(R, 2))
						randomly_chosen_points[i] = null;
				}
			} while (randomly_chosen_points[i] == null);
		}
		return randomly_chosen_points;
	}

	private NeighbourPoints findKeyPoints(Point point, List<NeighbourPoints> key_points) {
		NeighbourPoints neighbour_points = null;
		for (NeighbourPoints points : key_points)
			if (points.getFirstPoint() == point)
				neighbour_points = points;
		return neighbour_points;
	}
}
