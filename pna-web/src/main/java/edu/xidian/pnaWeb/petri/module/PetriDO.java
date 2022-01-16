package edu.xidian.pnaWeb.petri.module;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Arrays;

/**
 * @Description
 * @Author He
 * @Date 2021/10/5 17:36
 */
public class PetriDO {
	private int[][] preMatrix;
	private int[][] postMatrix;
	private int[] marking;
	private int[] resPlaces;
	private String webJson;

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		} else if (o != null && this.getClass() == o.getClass()) {
			PetriDO petriDO = (PetriDO)o;
			return Arrays.equals(this.marking, petriDO.marking);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return Arrays.hashCode(this.marking);
	}
	PetriDO() {
	}
	PetriDO(final int[][] preMatrix, final int[][] postMatrix, final int[] marking, final int[] resPlaces, final String webJson) {
		this.preMatrix = preMatrix;
		this.postMatrix = postMatrix;
		this.marking = marking;
		this.resPlaces = resPlaces;
		this.webJson = webJson;
	}

	public static PetriDO.PetriDOBuilder builder() {
		return new PetriDO.PetriDOBuilder();
	}

	public String toString() {
		return "PetriDO(preMatrix=" + Arrays.deepToString(this.getPreMatrix()) + ", postMatrix=" + Arrays.deepToString(this.getPostMatrix()) + ", marking=" + Arrays.toString(this.getMarking()) + ", resPlaces=" + Arrays.toString(this.getResPlaces()) + ", webJson=" + this.getWebJson() + ")";
	}

	public int[][] getPreMatrix() {
		return this.preMatrix;
	}

	public int[][] getPostMatrix() {
		return this.postMatrix;
	}

	public int[] getMarking() {
		return this.marking;
	}

	public int[] getResPlaces() {
		return this.resPlaces;
	}


	public String getWebJson() {
		return this.webJson;
	}

	public void setPreMatrix(final int[][] preMatrix) {
		this.preMatrix = preMatrix;
	}

	public void setPostMatrix(final int[][] postMatrix) {
		this.postMatrix = postMatrix;
	}

	public void setMarking(final int[] marking) {
		this.marking = marking;
	}

	public void setResPlaces(final int[] resPlaces) {
		this.resPlaces = resPlaces;
	}


	public void setWebJson(final String webJson) {
		this.webJson = webJson;
	}

	public static class PetriDOBuilder {
		private int[][] preMatrix;
		private int[][] postMatrix;
		private int[] marking;
		private int[] resPlaces;
		private ReachGraphInfo reachGraphInfo;
		private String webJson;

		PetriDOBuilder() {
		}

		public PetriDO.PetriDOBuilder preMatrix(final int[][] preMatrix) {
			this.preMatrix = preMatrix;
			return this;
		}

		public PetriDO.PetriDOBuilder postMatrix(final int[][] postMatrix) {
			this.postMatrix = postMatrix;
			return this;
		}

		public PetriDO.PetriDOBuilder marking(final int[] marking) {
			this.marking = marking;
			return this;
		}

		public PetriDO.PetriDOBuilder resPlaces(final int[] resPlaces) {
			this.resPlaces = resPlaces;
			return this;
		}

		public PetriDO.PetriDOBuilder reachGraphInfo(final ReachGraphInfo reachGraphInfo) {
			this.reachGraphInfo = reachGraphInfo;
			return this;
		}

		public PetriDO.PetriDOBuilder webJson(final String webJson) {
			this.webJson = webJson;
			return this;
		}

		public PetriDO build() {
			return new PetriDO(this.preMatrix, this.postMatrix, this.marking, this.resPlaces, this.webJson);
		}

		public String toString() {
			return "PetriDO.PetriDOBuilder(preMatrix=" + Arrays.deepToString(this.preMatrix) + ", postMatrix=" + Arrays.deepToString(this.postMatrix) + ", marking=" + Arrays.toString(this.marking) + ", resPlaces=" + Arrays.toString(this.resPlaces) + ", reachGraphInfo=" + this.reachGraphInfo + ", webJson=" + this.webJson + ")";
		}
	}
}
