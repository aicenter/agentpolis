package cz.cvut.fel.aic.agentpolis.simulator.logger.subscriber;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.eventbus.Subscribe;
import com.opencsv.CSVWriter;

import cz.cvut.fel.aic.agentpolis.siminfrastructure.logger.LogItem;
import cz.cvut.fel.aic.agentpolis.simulator.creator.SimulationFinishedListener;

/**
 * 
 * The instance of class subscribes log items from event bus and writes allowed
 * log items into csv file.
 * 
 * 
 * @author Zbynek Moler
 * 
 */
public class CSVLogSubscriber implements SimulationFinishedListener {

    private static final Logger LOGGER = Logger.getLogger(CSVLogSubscriber.class);

    private final ImmutableSet<Class<? extends LogItem>> logItemClasses;
    private final Map<String, Integer> headerWithHeaderIndexMap;
    private final int numOfCSVItems;
    private final CSVWriter csvWrite;

    private CSVLogSubscriber(ImmutableSet<Class<? extends LogItem>> logItemClasses,
            Map<String, Integer> headerWithHeaderIndexMap, int numOfCSVItems, CSVWriter csvWrite) {
        super();
        this.logItemClasses = logItemClasses;
        this.headerWithHeaderIndexMap = headerWithHeaderIndexMap;
        this.numOfCSVItems = numOfCSVItems;
        this.csvWrite = csvWrite;
    }

    @Subscribe
    public void logObject(Object logItem) {
        Class<?> logItemClass = logItem.getClass();
        if (logItemClasses.contains(logItemClass)) {
            writeLogItem(logItem, logItemClass);
        }
    }

    private void writeLogItem(Object logItem, Class<?> logItemClass) {

        String[] csvRow = new String[numOfCSVItems];

        String logItemClassName = logItemClass.getSimpleName();
        for (Field field : logItemClass.getFields()) {

            int headerIndex = headerWithHeaderIndexMap
                    .get(createHeaderName(logItemClassName, field));

            csvRow[headerIndex] = getFieldValue(logItem, field);

        }

        csvWrite.writeNext(csvRow);
    }

    private String getFieldValue(Object logItem, Field field) {
        Object fieldValue = null;
        try {
            fieldValue = field.get(logItem);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            LOGGER.warn(e);
        }
        return fieldValue != null ? fieldValue.toString() : "";
    }

    @Override
    public void simulationFinished() {
        closecsvWrite();

    }

    public void closecsvWrite() {
        try {
            csvWrite.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static CSVLogSubscriber newInstance(
            final ImmutableSet<Class<? extends LogItem>> allowedlogItemClasses,
            final File pathToCSVFile) throws IOException {

        checkNotNull(allowedlogItemClasses);
        checkNotNull(pathToCSVFile);

        CSVWriter csvWriter = new CSVWriter(new FileWriter(pathToCSVFile), ';');

        Map<String, Integer> headerWithHeaderIndexMap = Maps.newHashMap();
        List<String> csvHeader = Lists.newArrayList();
        int headerIndex = 0;

        for (Class<? extends LogItem> logItemClass : allowedlogItemClasses) {
            String logItemClassName = logItemClass.getSimpleName();
            for (Field field : logItemClass.getFields()) {
                String headerItem = createHeaderName(logItemClassName, field);
                headerWithHeaderIndexMap.put(headerItem, headerIndex);
                csvHeader.add(headerIndex++, headerItem);
            }
        }

        csvWriter.writeNext(csvHeader.toArray(new String[csvHeader.size()]));

        return new CSVLogSubscriber(allowedlogItemClasses, headerWithHeaderIndexMap, headerIndex,
                csvWriter);
    }

    private static String createHeaderName(String logItemClassName, Field field) {
        return logItemClassName + '.' + field.getName();
    }

}
