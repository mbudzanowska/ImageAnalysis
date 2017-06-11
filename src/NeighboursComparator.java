import java.util.Comparator;

public class NeighboursComparator implements Comparator<NeighbourPoints> {

	@Override
	public int compare(NeighbourPoints point_1st, NeighbourPoints point_2nd) {

		if (point_1st.getDistance() > point_2nd.getDistance())
			return 1;
		else if (point_1st.getDistance() < point_2nd.getDistance())
			return -1;
		else
			return 0;
	}

}