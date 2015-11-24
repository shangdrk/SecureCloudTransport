package hu.ait.crypto.storage;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import hu.ait.crypto.AppClient;
import hu.ait.crypto.Utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class DownloadTask {

    private URI cloudPath;

    private String containerName;
    private CloudBlobContainer container;
    private boolean isFile;
    private boolean isDirectory;
    private List<FileOutputStream> outputStreams;
    private List<CloudBlockBlob> cloudFiles;

    public DownloadTask(AppClient client, String fromPath,
                        String toPath) throws FileNotFoundException {
        containerName = Utility.inferContainerNameFromPath(fromPath);
        String cloudPathNoContainerName = Utility
                .deleteContainerNameFromPath(fromPath);
        try {
            container = client.getCloudBlobClient()
                    .getContainerReference(containerName);
            if (!container.exists()) {
                throw new FileNotFoundException();
            }

            String UriAuthority = String.format("%1$s.blob.core.windows.net",
                    client.getAccountName());
            cloudPath = new URI(Utility.CONNECTION_SCHEME, UriAuthority,
                    "/".concat(fromPath), null, null);

            if (container.getBlockBlobReference(cloudPathNoContainerName)
                    .exists()) {
                isFile = true;
            } else {
                isDirectory = true;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("The download path you specified is incorrect");
            System.exit(0);
        } catch (StorageException e) {
            e.printStackTrace();
            System.out.println("An error occurred on the storage");
            System.exit(0);
        }

        File localFilePath = new File(toPath);
        if (!localFilePath.exists()) {
            boolean dirsMade = localFilePath.mkdirs();
            if (!dirsMade) {
                System.out.println("Failed to create user specified path " +
                        toPath);
                System.exit(1);
            }
        }

        cloudFiles = new ArrayList<>();
        outputStreams = new ArrayList<>();
        try {
            if (isFile) {
                cloudFiles.add(container.getBlockBlobReference(
                        cloudPathNoContainerName));
                String cloudFileName = Utility.inferCloudFileNameFromPath(
                        cloudFiles.get(0).getName()
                );
                outputStreams.add(new FileOutputStream(
                        toPath.concat("/" + cloudFileName)
                ));
            } else {

            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("The download path you specified is incorrect");
            System.exit(0);
        } catch (StorageException e) {
            e.printStackTrace();
            System.out.println("An error occurred on the storage");
            System.exit(0);
        }
    }

    public URI getCloudPath() {
        return cloudPath;
    }

    public String getContainerName() {
        return containerName;
    }

    public CloudBlobContainer getContainer() {
        return container;
    }

    public boolean isFile() {
        return isFile;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public List<FileOutputStream> getOutputStreams() {
        return outputStreams;
    }

    public List<CloudBlockBlob> getCloudFiles() {
        return cloudFiles;
    }
}
