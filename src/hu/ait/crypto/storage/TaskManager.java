package hu.ait.crypto.storage;

import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.*;
import hu.ait.crypto.AppClient;
import hu.ait.crypto.Utility;
import hu.ait.crypto.security.DecryptionManager;
import hu.ait.crypto.security.EncryptionManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManager {

    private AppClient client;
    private BlobContainerPermissions permissions;

    private EncryptionManager encryptionManager;
    private DecryptionManager decryptionManager;

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

        if (encryptionManager != null) {
            HashMap<String, String> metaData = new HashMap<>();
            metaData.put("iv", java.util.Base64.getEncoder().encodeToString(
                    encryptionManager.getIv()
            ));
            blob.setMetadata(metaData);
            blob.uploadMetadata();

            cleanUpBlobSpace(container, task);
            encryptionManager.cleanUp(task.getFileAbsolutePath());
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

            for (int i = 0; i < tasks.size(); i++) {
                tasks.get(i).download(streams.get(i));
            }
        }
    }

    public void downloadText(DownloadTask task) {}

    public void listFiles() throws StorageException, URISyntaxException {
        CloudBlobClient cbClient = client.getCloudBlobClient();

        for (CloudBlobContainer container : cbClient.listContainers()) {
            System.out.println("Listing files in container " + "\""
                    + container.getName() + "\"...");
            listFiles(container.getName());
            System.out.println("");
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

    public void setEncryptionManager(EncryptionManager manager) {
        if (manager != null) {
            this.encryptionManager = manager;
        }
    }

    public void setDecryptionManager(DecryptionManager manager) {
        if (manager != null) {
            this.decryptionManager = manager;
        }
    }

    public DecryptionManager getDecryptionManager() {
        return decryptionManager;
    }

    public UploadTask createUploadTask(String fromPath, String toPath) {
        UploadTask task = null;

        try {
            if (encryptionManager != null) {
                encryptionManager.encryptFile(fromPath);
                task = new UploadTask(encryptionManager.getEncryptedTemp()
                        .getPath(), toPath);
                task.setActualName(new File(fromPath).getName());
            } else {
                System.out.println("Your file is currently not protected by " +
                        "crypto cipher.\n");
                task = new UploadTask(fromPath, toPath);
            }
        } catch (FileNotFoundException e) {
            // TODO
        }

        return task;
    }

    public DownloadTask createDownloadTask(AppClient client, String fromPath,
                                   String toPath) {

        DownloadTask task = null;
        try {
            // Assuming the task is a single file
            CloudBlobContainer container = client.getCloudBlobClient()
                    .getContainerReference(
                            Utility.inferContainerNameFromPath(fromPath)
                    );
            CloudBlockBlob oldBlob = container.getBlockBlobReference(
                    Utility.deleteContainerNameFromPath(fromPath)
            );
            CloudBlockBlob newBlob = container.getBlockBlobReference(
                    Utility.getTempCloudFileName(fromPath)
            );
            newBlob.startCopy(oldBlob);

            task = new DownloadTask(client, Utility.getTempCloudPath
                    (fromPath), toPath);
        } catch (StorageException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return task;
    }

    public List<byte[]> getTaskIv(DownloadTask task) {
        List<byte[]> ivList = new ArrayList<>();

        if (task.isFile()) {
            CloudBlockBlob singleTask = task.getCloudFiles().get(0);
            if (singleTask.getMetadata().containsKey("iv")) {
                byte[] iv = java.util.Base64.getDecoder().decode(
                        singleTask.getMetadata().get("iv")
                );
                ivList.add(iv);
            } else {
                ivList.add(null);
            }
        } else {
            List<CloudBlockBlob> tasks = task.getCloudFiles();
            for (CloudBlockBlob singleTask : tasks) {
                if (singleTask.getMetadata().containsKey("iv")) {
                    byte[] iv = java.util.Base64.getDecoder().decode(
                            singleTask.getMetadata().get("iv")
                    );
                    ivList.add(iv);
                } else {
                    ivList.add(null);
                }
            }
        }

        return ivList;
    }

    public void cleanUpBlobSpace(CloudBlobContainer container,
                                 UploadTask task) {
        try {
            CloudBlockBlob oldBlob = container.getBlockBlobReference(
                    task.getCloudPath()
            );
            CloudBlockBlob newBlob = container.getBlockBlobReference(
                    Utility.inferCloudPathFromPath(task.getToPath(),
                            task.getActualName())
            );
            newBlob.startCopy(oldBlob);
            oldBlob.deleteIfExists();
        } catch (StorageException e) {
            // TODO
        } catch (URISyntaxException e) {
            // TODO
        }
    }

    public void cleanUpBlobSpace(CloudBlobContainer container,
                                 DownloadTask task) {
        try {
            CloudBlockBlob oldBlob = container.getBlockBlobReference(
                    Utility.deleteContainerNameFromPath(
                            task.getCloudPath().getRawPath()
                    )
            );
            oldBlob.deleteIfExists();
        } catch (StorageException e) {
            // TODO
        } catch (URISyntaxException e) {
            // TODO
        }
    }
}
