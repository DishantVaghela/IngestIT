package com.vihit.ingestit.util;

import com.vihit.ingestit.model.*;

public class CommonResourceManager {

	private static final String MODULE = "COMMON-RESOURCE-MANAGER";
	private static CommonResourceManager INSTANCE;
	private static ConnectionsModel connectionModel;
	
	private CommonResourceManager() {

	}

	public static CommonResourceManager getInstance() {
		if (INSTANCE != null)
			return INSTANCE;
		else
			return new CommonResourceManager();
	}

	public ConnectionsModel getConnectionModel() {
		return connectionModel;
	}

	public void setConnectionModel(ConnectionsModel connectionModel) {
		this.connectionModel = connectionModel;
	}
	
}
