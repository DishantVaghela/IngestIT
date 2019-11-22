package com.vihit.ingestit.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ConnectionsModel implements Serializable {

	private static final long serialVersionUID = -8097152119796968921L;
	public ArrayList<Connection> connections;

	public ArrayList<Connection> getConnections() {
		return connections;
	}

	public void setConnections(ArrayList<Connection> connections) {
		this.connections = connections;
	}

	@Override
	public String toString() {
		return "ConnectionsModel [connections=" + connections + "]";
	}
	
	
}
