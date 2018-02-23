package GridSingleton;

import java.util.ArrayList;
import java.util.SortedMap;
import java.util.TreeMap;

import com.sun.tools.javac.util.List;

public class GridTreeMap3 {

	public SortedMap<Integer, Grid> grids = new TreeMap<Integer, Grid>();
	private static GridTreeMap3 singleton = new GridTreeMap3();

	private GridTreeMap3() {

		double stepLat = 1.25;
		double stepLng = 2.5;
		double ymin = 0;
		double xmin = 0;
		int len = 0;
		for (int i = 10368; i < 20736; i++) {
			Grid grid = new Grid();
			grid.setLatMin(ymin);
			grid.setLatMax(ymin + stepLat);
			grid.setLongMin(xmin);
			grid.setLongMax(xmin + stepLng);
			grid.setNumPx(i);
			xmin += stepLng;
			len++;
			if (len % 144 == 0) {
				ymin += stepLat;
				xmin = -180;
			}
			grids.put(i, grid);

		}

	}

	public static GridTreeMap3 getInstance() {
		return singleton;
	}
}
