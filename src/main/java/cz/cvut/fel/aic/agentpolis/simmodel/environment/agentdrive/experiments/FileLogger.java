package cz.cvut.fel.aic.agentpolis.simmodel.environment.agentdrive.experiments;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Logs specified information about experiment to text file.
 * File has format for easy plotting with R
 *
 * Created by wmatex on 3.12.14.
 */
public class FileLogger {
    private static final String VALUE_SEPARATOR = ":";
    private static final String DIRECTORY = "data";
    private static final String SUFFIX = ".R";
    private final File file;

    private final String filename;
    private BufferedWriter writer;

    private List<Double> x = new ArrayList<Double>();
    private List<Double> y = new ArrayList<Double>();
    private List<Double> y_alt = new ArrayList<Double>();

    public FileLogger(String prefix) {
        Format formatter = new SimpleDateFormat("dd-MM-yyyy_HH.mm.ss");
        String suffix = formatter.format(new Date());
        filename = prefix + "_" + suffix;
        file = new File(DIRECTORY + "/" + filename + SUFFIX);
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write("# Experiment date: " + suffix);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            writer = null;
        }
    }

    public FileLogger addHeaderValue(String key, Double val) {
        return addHeaderValue(key, val.toString());
    }

    public FileLogger addHeaderValue(String key, Integer val) {
        return addHeaderValue(key, val.toString());
    }

    public FileLogger addHeaderValue(String key, String value) {
        try {
            writer.write("# "+key+VALUE_SEPARATOR+" "+value);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
        }
        return this;
    }

    public FileLogger addHeaderLine(String line) {
        try {
            writer.write("# "+line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
        }
        return this;
    }

    public FileLogger addPoint(double x, double y) {
        this.x.add(x);
        this.y.add(y);
        return this;
    }

    public FileLogger addAltPoint(double y) {
        this.y_alt.add(y);
        return this;
    }

    public void plot() {
        try {
            writer.write("x <- c(");
            String sep = "";
            for (int i = 0; i < x.size(); ++i) {
                writer.write(sep + x.get(i));
                sep = ", ";
            }
            writer.write(")");
            writer.newLine();

            writer.write("y <- c(");
            sep = "";
            for (int i = 0; i < y.size(); ++i) {
                writer.write(sep + y.get(i));
                sep = ", ";
            }
            writer.write(")");
            writer.newLine();

            writer.write("# y_alt <- c(");
            sep = "";
            for (int i = 0; i < y_alt.size(); ++i) {
                writer.write(sep + y_alt.get(i));
                sep = ", ";
            }
            writer.write(")");
            writer.newLine();

            writer.write("pdf('"+filename+".pdf')");
            writer.newLine();
            writer.write("plot(x, y, type=\"b\", col=\"blue\")");
            writer.newLine();
            writer.write("dev.off()");
            writer.newLine();

            x.clear();
            y.clear();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {

        }
    }

    public void flush() {
        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {

        }
    }
}
