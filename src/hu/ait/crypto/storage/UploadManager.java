package hu.ait.crypto.storage;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.BlobContainerPermissions;
import com.microsoft.azure.storage.blob.BlobContainerPublicAccessType;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import hu.ait.crypto.AppClient;
import hu.ait.crypto.Utility;

import java.io.IOException;
import java.net.URISyntaxException;

public class UploadManager {

    private AppClient client;
    private BlobContainerPermissions permissions;

    public UploadManager(AppClient client) {
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
}
