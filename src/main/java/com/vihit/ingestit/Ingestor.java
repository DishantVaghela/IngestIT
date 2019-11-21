package com.vihit.ingestit;

import java.util.ArrayList;

import com.vihit.ingestit.model.*;
import com.vihit.ingestit.pipeline.LocalToHDFS;
import com.vihit.ingestit.util.CommonResourceManager;
import com.vihit.ingestit.util.Logger;

public class Ingestor {

	private static final String MODULE = "INGESTOR";
	ConnectionsModel connectionModel;

	public Ingestor() {
		this.connectionModel = CommonResourceManager.getInstance().getConnectionModel();
	}

	public void executeIngestionModel(IngestionModel ingestionModel) {
		ArrayList<Pipeline> pipelines = ingestionModel.getPipelines();
		pipelines.forEach(pipeline -> executePipeline(pipeline));
	}

	public void executePipeline(Pipeline pipeline) {
		switch (pipeline.getIn().get("type").toLowerCase()) {
		case "localfs":
			switch (pipeline.getOut().get("type").toLowerCase()) {
			case "hdfs":
				new LocalToHDFS(pipeline).executePipeline();
				break;
			default : Logger.logError(MODULE, "Unsupported Pipeline");
			}
			break;
		}
	}

}
