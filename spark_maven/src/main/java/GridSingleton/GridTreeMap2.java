package GridSingleton;

import java.util.SortedMap;
import java.util.TreeMap;

public class GridTreeMap2 {
	public SortedMap<Integer, Grid> grids = new TreeMap<Integer, Grid>();
	private static GridTreeMap2 singleton = new GridTreeMap2();

	private GridTreeMap2() {

		double stepLat = 2.5;
		double stepLng = 5;
		double ymin = -180;
		double xmin = -90;
		int len = 0;

		for (int i = 0; i < 5184; i++) {
			Grid grid = new Grid();
			grid.setLatMin(ymin);
			grid.setLatMax(ymin + stepLat);
			grid.setLongMin(xmin);
			grid.setLongMax(xmin + stepLng);
			grid.setNumPx(i);
			xmin += stepLng;
			len++;
			if (len % 72 == 0) {
				ymin += stepLat;
				xmin = -180;
			}
			grids.put(i, grid);

		}

	}

	public static GridTreeMap2 getInstance() {
		return singleton;
	}

}
