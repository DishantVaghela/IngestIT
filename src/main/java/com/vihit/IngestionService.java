package com.vihit;

import com.vihit.IngestionModel;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class IngestionService {

    private ThreadPoolExecutor ingestionThreadPool;
    private LinkedBlockingQueue<Runnable> linkedQueue;
    private String sourcePath = null;
    private IngestionModel configModel = null;
    private Thread pollingThread;

    public static void main(String[] args) {

        IngestionService ingestionService = new IngestionService();
        ingestionService.sourcePath = "/home/vihit/Desktop/ingestion/test/";
        try {
            ingestionService.startService();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    private void startService() throws Exception {
        init();

        pollingThread = new Thread(new IngestionPoller());
        pollingThread.setPriority(6);
        pollingThread.start();

        linkedQueue = new LinkedBlockingQueue<Runnable>();
        ingestionThreadPool = new ThreadPoolExecutor(2, 2,
                3600000L, TimeUnit.SECONDS, linkedQueue);

    }

    private void init() {
        configModel = (IngestionModel) readConfiguration("/home/vihit/Desktop/ingestion/test/ingestion.json");
        System.out.println(configModel);
    }

    private class IngestionPoller implements Runnable
    {
        @Override
        public void run() {
            try{
                while(true)
                {
                    prepareDivideIngest();
                    System.out.println("Will be back in a minute");
                    Thread.sleep(60000);
                }
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void prepareDivideIngest()
    {
        List<Path> fileList = listFiles(configModel.getInputPath(), configModel.getFileNameRule());
        int maxIngestionSize = 10;
        List<Path>  mainSubList = fileList.subList(0,maxIngestionSize);
        for(Path file:mainSubList)
        {
            System.out.println("Picking up "+file.getFileName());
        }
        int totalSubList = 2;
        int subListSize = maxIngestionSize/totalSubList;

        for(int i=0; i< totalSubList; i++) {
            List<Path> subList = fileList.subList(subListSize * i, subListSize + subListSize * i);
            for(Path file:subList)
            {
                System.out.println("Files in List "+i+" Name:- "+file.getFileName());
            }
            ingestionThreadPool.execute(new HDFSIngestionProcess(subList, configModel));
        }
    }

    public static Object readConfiguration(String path)
    {
        Object configModel = null;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            String fileData = new String(Files.readAllBytes(Paths.get(path)));
            configModel = gson.fromJson(fileData,IngestionModel.class);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }

        return configModel;
    }

    public static List<Path> listFiles(String inputPath, final String fileNameRules)
    {
        List<Path> fileList = null;
        try{
             fileList = Files.walk(Paths.get(inputPath)).filter(f->Files.isRegularFile(f) && f.getFileName().toString().matches(fileNameRules)).collect(Collectors.toList());
        }catch(Exception ex){

        }
        return fileList;
    }


}
