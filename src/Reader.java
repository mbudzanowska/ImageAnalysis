import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Reader {

	public Reader() {
	}

	public Image readImageHaraff(String fileName) {

		int featuresNumber = 0;
		int pointsNumber = 0;
		List<Point> points = null;

		try {
			BufferedReader in = new BufferedReader(new FileReader("haraffs/" + fileName + ".haraff.sift"));

			featuresNumber = Integer.parseInt(in.readLine());
			pointsNumber = Integer.parseInt(in.readLine());
			points = new ArrayList<Point>();

			for (int i = 0; i < pointsNumber; i++) {
				points.add(processLine(in.readLine()));
			}
			in.close();

		} catch (FileNotFoundException e) {
			System.out.println("B³¹d odczytu");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("B³¹d linii");
			e.printStackTrace();
		}

		return new Image(points, pointsNumber, featuresNumber, fileName);
	}

	private Point processLine(String line) {

		List<Integer> list = new ArrayList<Integer>();
		Scanner scanner = new Scanner(line);
		double x = Double.parseDouble(scanner.next());
		double y = Double.parseDouble(scanner.next());

		for (int i = 0; i < 3; i++) {
			scanner.next();
		}

		while (scanner.hasNextInt())
			list.add(scanner.nextInt());
		scanner.close();
		Integer[] features = list.toArray(new Integer[list.size()]);

		return new Point(x, y, features);
	}
}
