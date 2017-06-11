import javax.swing.JComponent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Random;

public class Visualisator extends JComponent {

	Image image_1;
	Image image_2;
	List<NeighbourPoints> neighbourPoints;
	int width;
	int height;
	int image_1_width;

	public Visualisator(Image image_1, Image image_2, List<NeighbourPoints> neighbourPoints) {
		this.neighbourPoints = neighbourPoints;
		this.image_1 = image_1;
		this.image_2 = image_2;
		image_1_width = image_1.getImage().getWidth(null);
		width = image_1_width + image_2.getImage().getWidth(null);
		height = Math.max(image_1.getImage().getHeight(null), image_2.getImage().getHeight(null));

		this.setSize(width, height);
	}

	public void paintComponent(Graphics g) {

		g = (Graphics2D) g;

		g.drawImage(image_1.getImage(), 0, 0, null);
		g.drawImage(image_2.getImage(), image_1_width, 0, null);

		if (neighbourPoints != null)
			for (NeighbourPoints nb : neighbourPoints) {
				g.setColor(getRandomColor());
				g.drawLine((int) (nb.getFirstPoint().getX()), (int) nb.getFirstPoint().getY(),
						(int) (nb.getSecondPoint().getX()) + image_1_width, (int) (nb.getSecondPoint().getY()));
			}
	}

	private Color getRandomColor() {
		Random rand = new Random();
		float r = (float) (rand.nextFloat() / 2f + 0.5);
		float g = (float) (rand.nextFloat() / 2f + 0.5);
		float b = (float) (rand.nextFloat() / 2f + 0.5);
		return new Color(r, g, b);
	}

	public Dimension getDimension() {
		return new Dimension(width, height);
	}

}
