package hu.ait.crypto.storage;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;
import hu.ait.crypto.AppClient;
import hu.ait.crypto.Utility;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class TaskManager {

    private AppClient client;
    private BlobContainerPermissions permissions;

    public TaskManager(AppClient client) {
        this.client = client;
        permissions = new BlobContainerPermissions();
        permissions.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);
    }

    public void uploadToRoot(UploadTask task) {}

    public void uploadTextToRoot(UploadTask task) {}

    public void upload(UploadTask task)
            throws URISyntaxException, StorageException {
        CloudBlobContainer container = client.getCloudBlobClient()
                .getContainerReference(task.getContainerName());
        container.createIfNotExists();
        container.uploadPermissions(permissions);

        CloudBlockBlob blob = container.getBlockBlobReference(
                task.getCloudPath()
        );

        try {
            blob.upload(task.getFileInputStream(), task.getFileLength());
        } catch (IOException e) {
            Utility.handleException(e, getClass());
        }
    }

    public void uploadText(UploadTask task) {}

    public void downloadToRoot(DownloadTask task) {}

    public void downloadTextToRoot(DownloadTask task) {}

    public void download(DownloadTask task)
            throws StorageException {
        if (task.isFile()) {
            CloudBlockBlob singleTask = task.getCloudFiles().get(0);
            FileOutputStream fos = task.getOutputStreams().get(0);

            singleTask.download(fos);
        } else if (task.isDirectory()) {
            List<CloudBlockBlob> tasks = task.getCloudFiles();
            List<FileOutputStream> streams = task.getOutputStreams();

            for (int i=0; i<tasks.size(); i++) {
                tasks.get(i).download(streams.get(i));
            }
        }
    }

    public void downloadText(DownloadTask task) {}

    public void listFiles() throws StorageException, URISyntaxException {
        CloudBlobClient cbClient = client.getCloudBlobClient();

        for (CloudBlobContainer container : cbClient.listContainers()) {
            listFiles(container.getName());
        }
    }

    public void listFiles(String containerName)
            throws IllegalArgumentException, StorageException,
            URISyntaxException {

        CloudBlobContainer container = client.getCloudBlobClient()
                .getContainerReference(containerName);
        if (!container.exists()) {
            throw new IllegalArgumentException();
        }
        for (ListBlobItem item : container.listBlobs()) {
            if (item instanceof CloudBlockBlob) {
                System.out.println(((CloudBlockBlob) item).getName());
            } else if (item instanceof CloudBlobDirectory) {
                listFiles(containerName,
                        // TODO
                        ((CloudBlobDirectory) item).getPrefix());
            }
        }
    }

    public void listFiles(String containerName, String dir) {

    }
}
