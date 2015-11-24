package hu.ait.crypto;

import hu.ait.crypto.storage.DownloadTask;
import hu.ait.crypto.storage.TaskManager;

public class Main {

    public static void main(String[] args) {
        AppClient ac = new AppClient();
        /*try {
            UploadTask task1 = new UploadTask("Certificate.pdf",
                    "example/dir/subdir");
            UploadTask task2 = new UploadTask("confirmation.pdf",
                    "example/dir/subdir");
            TaskManager manager = new TaskManager(ac);
            manager.upload(task1);
            manager.upload(task2);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        try {
            DownloadTask task1 = new DownloadTask(ac,
                    "example/dir/subdir/Certificate.pdf",
                    "config/noexist");
            TaskManager manager = new TaskManager(ac);
            manager.download(task1);
            /*CloudBlobContainer container = ac.getCloudBlobClient()
                    .getContainerReference("example");
            CloudBlockBlob blob = container.getBlockBlobReference("dir/subdir/Certificate.pdf");
            blob.download(new FileOutputStream("config/Certificate.pdf"));*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
