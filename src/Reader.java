import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Reader {

	String fileName;

	public Reader(String name) {
		fileName = name;
	}

	public Image readImage() {

		int featuresNumber = 0;
		int pointsNumber = 0;
		List<Point> points = null;

		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));

			featuresNumber = Integer.parseInt(in.readLine());
			pointsNumber = Integer.parseInt(in.readLine());
			points = new ArrayList<Point>();

			for (int i = 0; i < pointsNumber; i++) {
				points.add(processLine(in.readLine()));
			}

		} catch (FileNotFoundException e) {
			System.out.println("B³¹d odczytu");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("B³¹d linii");
			e.printStackTrace();
		}

		return new Image(points, pointsNumber, featuresNumber);
	}

	public static void main(String[] args) {
		Reader r = new Reader("nyan.png.haraff.sift");
		r.readImage();
	}

	private Point processLine(String line) {

		List<Integer> list = new ArrayList<Integer>();
		Scanner scanner = new Scanner(line);
		double x = scanner.nextDouble();
		double y = scanner.nextDouble();

		for (int i = 0; i < 3; i++) {
			scanner.nextDouble();
		}

		while (scanner.hasNextInt())
			list.add(scanner.nextInt());
		Integer[] features = list.toArray(new Integer[list.size()]);
		return new Point(x, y, features);
	}
}
