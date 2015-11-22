package hu.ait.crypto;

import hu.ait.crypto.storage.UploadManager;
import hu.ait.crypto.storage.UploadTask;

public class Main {

    public static void main(String[] args) {
        AppClient ac = new AppClient();
        try {
            UploadTask task = new UploadTask("config/client-config.txt",
                    "example/dir");
            UploadManager manager = new UploadManager(ac);
            manager.upload(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
