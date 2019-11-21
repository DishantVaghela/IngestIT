package com.vihit.ingestit.model;

import java.io.Serializable;
import java.util.ArrayList;

public class IngestionModel implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<Pipeline> pipelines;

	public ArrayList<Pipeline> getPipelines() {
		return pipelines;
	}

	public void setPipelines(ArrayList<Pipeline> pipelines) {
		this.pipelines = pipelines;
	}

	@Override
	public String toString() {
		return "IngestionModel [pipelines=" + pipelines + "]";
	}

}
