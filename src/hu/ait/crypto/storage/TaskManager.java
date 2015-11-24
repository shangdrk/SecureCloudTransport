package hu.ait.crypto.storage;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import hu.ait.crypto.AppClient;
import hu.ait.crypto.Utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;

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
            throws FileNotFoundException,
            URISyntaxException, StorageException {

        CloudBlobContainer container = client.getCloudBlobClient()
                .getContainerReference(task.getContainerName());
        task.getCloudFiles().get(0).download(task.getOutputStreams().get(0));
        try {
            System.out.println(task.getCloudFiles().get(0).exists());
            System.out.println(task.getOutputStreams().get(0).getFD());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                task.getOutputStreams().get(0).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void downloadText(DownloadTask task) {}
}
