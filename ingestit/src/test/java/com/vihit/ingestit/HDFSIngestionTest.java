package com.vihit.ingestit;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.hadoop.hdfs.MiniDFSCluster;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileUtil;

public class HDFSIngestionTest {

	static MiniDFSCluster hdfsCluster = null;
	static Path basePath = Paths.get("src/test/resources/hdfsIngestionTest");
	static File baseDir = null;

	@BeforeClass
	public static void setup() throws Exception {

		Configuration conf = new Configuration();
		try {
			baseDir = Files.createTempDirectory(basePath, "hdfs_test").toFile().getAbsoluteFile();
			conf.set(MiniDFSCluster.HDFS_MINIDFS_BASEDIR, baseDir.getAbsolutePath());
			hdfsCluster = (new MiniDFSCluster.Builder(conf)).build();
			HDFSIngestion.fs = hdfsCluster.getFileSystem();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void ingestFileTest() throws IOException {
		Path sampleFile = Paths.get(basePath.toString() + "/sampleFile.txt");
		Path hdfsOutputPath = Paths.get("/output/sampleHDFSFile.txt");
		Files.createFile(sampleFile);

		HDFSIngestion.ingestFile(sampleFile, hdfsOutputPath);
		Boolean flag = HDFSIngestion.fs.exists(new org.apache.hadoop.fs.Path("/output/sampleHDFSFile.txt"));
		Files.deleteIfExists(Paths.get(basePath.toString() + "/sampleFile.txt"));

		assert (flag);
	}

	@Test
	public void archiveFileTest() throws IOException {
		Path sampleFile = Paths.get(basePath.toString() + "/sampleFile.txt");
		String archivePath = basePath.toString() + "/archive";
		Files.createFile(sampleFile);

		HDFSIngestion.archiveFile(sampleFile, archivePath);
		Boolean flag = Files.exists(Paths.get(archivePath));
		Files.deleteIfExists(Paths.get(archivePath + "/sampleFile.txt"));
		Files.deleteIfExists(Paths.get(archivePath));

		assert (flag);
	}

	@AfterClass
	public static void tearDown() {
		try {
			HDFSIngestion.fs.close();
			hdfsCluster.shutdown();
			FileUtil.fullyDelete(baseDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
