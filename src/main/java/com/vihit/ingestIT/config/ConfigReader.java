package com.vihit.ingestIT.config;

import java.nio.file.NoSuchFileException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vihit.ingestIT.model.IngestionModel;
import com.vihit.ingestIT.util.Logger;

public class ConfigReader {

	private static String MODULE = "CONFIG-READER";
	
	public static IngestionModel readIngestionModel(String path) {
		IngestionModel configModel = null;
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		try {
			String fileData = new String(Files.readAllBytes(Paths.get(path)));
			configModel = (IngestionModel) gson.fromJson(fileData, IngestionModel.class);
		} catch (NoSuchFileException e) {
			Logger.logError(MODULE, "The configuration file does not exist on the given path. "+e.toString());
		} 
		catch (Exception e) {
			Logger.logError(MODULE, e.toString());
		}

		return configModel;

	}
}
