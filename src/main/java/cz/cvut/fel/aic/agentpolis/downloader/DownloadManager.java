package cz.cvut.fel.aic.agentpolis.downloader;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class DownloadManager {

    public static final int MAX_CONCURRENT_DOWNLOADS = 2;

    private ExecutorService exec;
    @Inject
    public DownloadManager() {
        this.exec = Executors.newFixedThreadPool(MAX_CONCURRENT_DOWNLOADS);
    }

    public Future<?> submit(DownloadTask downloadTask) {
        return exec.submit(downloadTask);
    }
}
