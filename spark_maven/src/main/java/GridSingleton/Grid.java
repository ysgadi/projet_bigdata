package GridSingleton;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

public class Grid implements Serializable {

	public Double latMin;
	public Double latMax;
	public Double longMin;
	public Double longMax;
	public int numPx;

	public int getNumPx() {
		return numPx;
	}

	public void setNumPx(int numPx) {
		this.numPx = numPx;
	}

	public Double getLatMin() {
		return latMin;
	}

	public void setLatMin(Double latMin) {
		this.latMin = latMin;
	}

	public Double getLatMax() {
		return latMax;
	}

	public void setLatMax(Double latMax) {
		this.latMax = latMax;
	}

	public Double getLongMin() {
		return longMin;
	}

	public void setLongMin(Double longMin) {
		this.longMin = longMin;
	}

	public Double getLongMax() {
		return longMax;
	}

	public void setLongMax(Double longMax) {
		this.longMax = longMax;
	}

	@Override
	public String toString() {
		return "Grid [latMin=" + latMin + ", latMax=" + latMax + ", longMin=" + longMin + ", longMax=" + longMax + "]";
	}

	public Grid() {
		super();
	}

}
