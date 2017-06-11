import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

public class Image {

	private List<Point> points;
	private int pointsNumber;
	private int featuresNumber;
	private java.awt.Image image;

	public Image(List<Point> points, int pointsNumber, int featuresNumber, String imageName) {

		super();
		this.points = points;
		this.pointsNumber = pointsNumber;
		this.featuresNumber = featuresNumber;

		try {
			setImage(ImageIO.read(new File("images/" + imageName)));
		} catch (IOException e) {
			System.out.println("b³¹d odczytu obazu");
			e.printStackTrace();
		}
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

	public java.awt.Image getImage() {
		return image;
	}

	private void setImage(java.awt.Image image) {
		this.image = image;
	}

}
