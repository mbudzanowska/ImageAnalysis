import java.awt.Dimension;

import javax.swing.JFrame;;

public class Main {

	public static void main(String[] args) {

		Image i1 = new Reader().readImageHaraff("keys.png");
		Image i2 = new Reader().readImageHaraff("keys2.png");

		JFrame frame = new JFrame();
		// Visualisator v = new Visualisator(i1, i2, new
		// NearestNeighbourAlgorithm(i1, i2).getCoupleKeyPoints());
		Visualisator v = new Visualisator(i1, i2, new RANSAC(i1, i2).getCoupleKeyPoints());
		Dimension dim = v.getDimension();
		frame.setContentPane(v);
		frame.setVisible(true);
		frame.setSize(dim.width, dim.height);
	}

}
