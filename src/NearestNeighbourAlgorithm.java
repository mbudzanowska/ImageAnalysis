import java.util.ArrayList;
import java.util.List;

public class NearestNeighbourAlgorithm {

	private Image image_1;
	private Image image_2;
	private static int features_number;
	List<NeighbourPoints> neighbour_points;

	public NearestNeighbourAlgorithm(Image image_1, Image image_2) {
		this.image_1 = image_1;
		this.image_2 = image_2;
		features_number = image_1.getFeaturesNumber();
		neighbour_points = new ArrayList<NeighbourPoints>();
	}

	public List<NeighbourPoints> getCoupleKeyPoints() {
		
		List<NeighbourPoints> image_1_neighbours = getAllClosestPoints(image_1, image_2);
		List<NeighbourPoints> image_2_neighbours = getAllClosestPoints(image_2, image_1);
		
		List<NeighbourPoints> couple_key_points = new ArrayList<NeighbourPoints>();
		
		for(NeighbourPoints neighbours_1: image_1_neighbours)
			for(NeighbourPoints neighbours_2: image_2_neighbours){
				if(neighbours_2.getImage1Point() == neighbours_1.getImage2Point()){
					couple_key_points.add(neighbours_1);
					break;
				}
			}
		return couple_key_points;
	}
	
	private List<NeighbourPoints> getAllClosestPoints(Image iamge_1, Image image_2){
		List<NeighbourPoints> closest_points = new ArrayList<NeighbourPoints>();
		for(Point point : image_1.getPoints()) 
			closest_points.add(new NeighbourPoints(point, getClosestPointOnSecondImage(point, image_2)));
		return closest_points;
	}

	private Point getClosestPointOnSecondImage(Point point, Image image) {

		Point closest = null;
		double distance = Double.POSITIVE_INFINITY;

		for (Point image_point : image.getPoints()){
			double computed_distance = getDistance(point, image_point);
			if(computed_distance < distance){
				distance = computed_distance;
				closest = image_point;
			}
		}
		return closest;
	}

	private double getDistance(Point a, Point b){
		 
		 Integer[] a_features = a.getFeatures();
		 Integer[] b_features = b.getFeatures();
		 double distance = 0;
		 for(int i = 0; i<features_number; i++) distance += Math.pow(a_features[i] - b_features[i], 2);
		 distance = Math.sqrt(distance);	
		 return distance;
	 }

}
