import java.util.List;

public class Image {

	private List<Point> points;
	private int pointsNumber;
	private int featuresNumber;

	public Image(List<Point> points, int pointsNumber, int featuresNumber) {

		super();
		this.points = points;
		this.pointsNumber = pointsNumber;
		this.featuresNumber = featuresNumber;
	}

	public List<Point> getPoints() {
		return points;
	}

	public int getPointsNumber() {
		return pointsNumber;
	}

	public int getFeaturesNumber() {
		return featuresNumber;
	}

}
