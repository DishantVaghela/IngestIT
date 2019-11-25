package com.vihit.ingestit.process;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import com.vihit.ingestit.model.ConnectionsModel;
import com.vihit.ingestit.model.Pipeline;
import com.vihit.ingestit.util.CommonResourceManager;
import com.vihit.ingestit.util.Logger;

public class KafkaFilesIngestion implements Runnable {

	private List<Path> fileList;
	private Pipeline pipeline;
	private static String MODULE = "KAFKA-FILES-INGESTION";
	public static AtomicLong filesIngested = new AtomicLong();
	public static AtomicLong filesErrored = new AtomicLong();
	public static AtomicLong recordsIngested = new AtomicLong();
	public static AtomicLong recordsErrored = new AtomicLong();
	private static Producer<String, byte[]> producer;

	private static Properties producerProperties = null;

	public KafkaFilesIngestion(List<Path> fileList, Pipeline pipeline) {
		this.fileList = fileList;
		this.pipeline = pipeline;
	}

	private Properties getProducerProperties(Map<String, String> properties) {
		Properties producerProperties = new Properties();
		properties.forEach((key, value) -> producerProperties.put(key, value));
		producerProperties.put("client.id", "IngestIT");
		producerProperties.put("key.serializer", StringSerializer.class.getName());
		producerProperties.put("value.serializer", ByteArraySerializer.class.getName());
		return producerProperties;
	}

	@Override
	public void run() {
		try {
			ConnectionsModel connectionModel = CommonResourceManager.getInstance().getConnectionModel();
			String outputConnection = pipeline.getOut().get("connection");
			producerProperties = getProducerProperties((connectionModel.connections.stream()
					.filter(conn -> conn.name.equalsIgnoreCase(outputConnection)).findFirst()).get().getProps());
			producer = new KafkaProducer<String, byte[]>(producerProperties);
			for (Path file : fileList) {
				readAndIngestFile(file, pipeline.getOut().get("topic"));
				archiveFile(file, pipeline.getIn().get("archive"));
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public static void readAndIngestFile(Path file, String topic) {
		try {
			Logger.logInfo(MODULE, "Ingesting file " + file.getFileName().toString() + " to " + topic);

			File inputFile = file.toFile();

			try (FileReader fileReader = new FileReader(inputFile);
					BufferedReader bufferedReader = new BufferedReader(fileReader)) {
				String line = bufferedReader.readLine();
				while (line != null) {
					recordsIngested.incrementAndGet();
					byte[] lineBytes = line.getBytes();
					ProducerRecord<String, byte[]> fileRecord = new ProducerRecord<String, byte[]>(topic, lineBytes);
					producer.send(fileRecord);
					line = bufferedReader.readLine();
				}
			} catch (IOException ioException) {
				Logger.logError(MODULE, "Exception occurred while processing file " + ioException.toString());
			} catch (Exception exception) {
				Logger.logError(MODULE, "Exception occurred while processing file " + exception.toString());
			}
			filesIngested.incrementAndGet();
		} catch (Exception ex) {
			filesErrored.incrementAndGet();
			ex.printStackTrace();
			Logger.logError(MODULE,
					"Error while ingesting file " + file.getFileName().toString() + " : " + ex.getMessage());
		}
	}

	public static void archiveFile(Path file, String archivePath) {
		try {
			Logger.logInfo(MODULE, "Archiving file " + file.getFileName().toString() + " to " + archivePath);
			FileUtils.moveFile(file.toFile(), Paths.get(archivePath + "/" + file.getFileName().toString()).toFile());
		} catch (Exception ex) {
			Logger.logError(MODULE,
					"Error while archiving file " + file.getFileName().toString() + " : " + ex.getMessage());
		}
	}
}
