package hu.ait.crypto;

import hu.ait.crypto.storage.UploadManager;
import hu.ait.crypto.storage.UploadTask;

public class Main {

    public static void main(String[] args) {
        AppClient ac = new AppClient();
        UploadTask task = new UploadTask("Maverick.docx", "example/dir");
        UploadManager manager = new UploadManager(ac);
        try {
            manager.upload(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
