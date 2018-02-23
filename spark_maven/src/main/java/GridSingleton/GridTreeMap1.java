package GridSingleton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

public class GridTreeMap1 implements Serializable {
	public SortedMap<Integer, Grid> grids = new TreeMap<Integer, Grid>();
	private static GridTreeMap1 singleton = new GridTreeMap1();

	private GridTreeMap1() {

		double stepLat = 5;
		double stepLng = 10;
		double ymin = -180;
		double xmin = -90;
		int len = 0;

		for (int i = 0; i < 1296; i++) {
			Grid grid = new Grid();
			grid.setLatMin(ymin);
			grid.setLatMax(ymin + stepLat);
			grid.setLongMin(xmin);
			grid.setLongMax(xmin + stepLng);
			grid.setNumPx(i);
			xmin += stepLng;
			len++;
			if (len % 36 == 0) {
				ymin += stepLat;
				xmin = -180;
			}
			grids.put(i, grid);

		}

	}

	public static GridTreeMap1 getInstance() {
		return singleton;
	}

}
