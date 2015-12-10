package hu.ait.crypto;


public class Main {

    public static void main(String[] args) {
        AppClient ac = new AppClient();

        /*try {
            TaskManager manager = new TaskManager(ac);
            manager.setEncryptionManager(new EncryptionManager());
            UploadTask task1 = manager.createUploadTask("reallyFun.txt",
                    "example/dir");
            manager.upload(task1);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        /*try {
            TaskManager manager = new TaskManager(ac);
            DownloadTask task1 = manager.createDownloadTask(ac,
                    "example/fun3.txt",
                    "config/noexist");
            manager.download(task1);

            List<byte[]> ivList = manager.getTaskIv(task1);
            if (task1.isFile()) {
                manager.setDecryptionManager(new DecryptionManager(
                        ivList.get(0))
                );
                manager.getDecryptionManager().decryptFile(task1.getFiles()
                        .get(0).getPath());

                manager.cleanUpBlobSpace(task1.getContainer(), task1);
                manager.getDecryptionManager().cleanUp(task1.getFiles().get(0));
            } else {
                // handle directory case
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
