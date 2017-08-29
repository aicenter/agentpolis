package cz.cvut.fel.aic.agentpolis.downloader;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class DownloadTask implements Runnable {

    private URL url;
    private File file;

    public DownloadTask(URL url, File file) {
        this.url = url;
        this.file = file;
    }

    @Override
    public void run() {
        file.getParentFile().mkdirs();
            try {
                downloadFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private void downloadFile() throws IOException {
        URLConnection urlConnection = url.openConnection();
        urlConnection.addRequestProperty("User-Agent", "Agentpolis");

        InputStream inputStream = urlConnection.getInputStream();
        ReadableByteChannel rbc = Channels.newChannel(inputStream);
        FileOutputStream fos = new FileOutputStream(file);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }
}
