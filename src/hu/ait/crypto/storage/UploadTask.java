package hu.ait.crypto.storage;

import hu.ait.crypto.Utility;

import java.io.*;

public class UploadTask {

    private String containerName;
    private FileInputStream file;
    private String fileName;
    private long fileLength;
    private String fileCompletePath;
    private String cloudPath;

    public UploadTask(String fromPath, String toPath) {
        String cwd = System.getProperty("user.dir");
        fileCompletePath = cwd.concat(Utility.formatPath(fromPath));

        try {
            File tempFile = new File(fileCompletePath);
            fileLength = tempFile.length();
            file = new FileInputStream(tempFile);
        } catch (FileNotFoundException e) {
            Utility.handleException(e, getClass());
        }
        fileName = Utility.inferFileNameFromPath(fromPath);
        containerName = Utility.inferContainerNameFromPath(toPath);
        cloudPath = Utility.inferCloudPathFromPath(toPath, fileName);
    }

    public String getContainerName() {
        return containerName;
    }

    public FileInputStream getFile() {
        return file;
    }

    public String getFileName() {
        return fileName;
    }

    public long getFileLength() {
        return fileLength;
    }

    public String getFileCompletePath() {
        return fileCompletePath;
    }

    public String getCloudPath() {
        return cloudPath;
    }
}
