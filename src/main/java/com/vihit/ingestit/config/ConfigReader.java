package com.vihit.ingestit.config;

import java.nio.file.NoSuchFileException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vihit.ingestit.model.ConnectionsModel;
import com.vihit.ingestit.model.IngestionModel;
import com.vihit.ingestit.util.Logger;

public class ConfigReader {

	private static String MODULE = "CONFIG-READER";

	public static IngestionModel readIngestionModel(String path) {
		IngestionModel configModel = null;
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		try {
			String fileData = new String(Files.readAllBytes(Paths.get(path)));
			configModel = (IngestionModel) gson.fromJson(fileData, IngestionModel.class);
		} catch (NoSuchFileException e) {
			Logger.logError(MODULE, "The configuration file does not exist on the given path. " + e.toString());
		} catch (Exception e) {
			Logger.logError(MODULE, e.toString());
		}
		System.out.println(configModel);
		return configModel;
	}

	public static ConnectionsModel readConnectionModel(String path) {
		ConnectionsModel configModel = null;
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		try {
			String fileData = new String(Files.readAllBytes(Paths.get(path)));
			configModel = (ConnectionsModel) gson.fromJson(fileData, ConnectionsModel.class);
		} catch (NoSuchFileException e) {
			Logger.logError(MODULE, "The configuration file does not exist on the given path. " + e.toString());
		} catch (Exception e) {
			Logger.logError(MODULE, e.toString());
		}
		System.out.println(configModel);
		return configModel;
	}
}
