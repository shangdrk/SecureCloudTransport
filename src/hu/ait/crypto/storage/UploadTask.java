package hu.ait.crypto.storage;

import hu.ait.crypto.Utility;

import java.io.*;

public class UploadTask {

    private String containerName;
    private FileInputStream fis;
    private String fileName;
    private long fileLength;
    private String fileAbsolutePath;
    private String cloudPath;

    public UploadTask(String fromPath, String toPath)
            throws FileNotFoundException {

        File file = new File(fromPath);

        if (!file.exists()) {
            throw new FileNotFoundException();
        }

        fileLength = file.length();
        fileAbsolutePath = file.getAbsolutePath();
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Utility.handleException(e, getClass());
        }
        fileName = file.getName();
        containerName = Utility.inferContainerNameFromPath(toPath);
        cloudPath = Utility.inferCloudPathFromPath(toPath, fileName);
    }

    public String getContainerName() {
        return containerName;
    }

    public FileInputStream getFileInputStream() {
        return fis;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileLength() {
        return fileLength;
    }

    public String getFileAbsolutePath() {
        return fileAbsolutePath;
    }

    public String getCloudPath() {
        return cloudPath;
    }
}
