package hu.ait.crypto;


public class Main {

    public static void main(String[] args) {
        AppClient ac = new AppClient();

        /*try {
            TaskManager manager = new TaskManager(ac);
            manager.setEncryptionManager(new EncryptionManager());
            UploadTask task1 = manager.createUploadTask("config/shangd.jpg",
                    "example/dir");
            manager.upload(task1);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        /*try {
            DownloadTask task1 = new DownloadTask(ac,
                    "example/dir/shangd.jpg4947572406554475133.tmp",
                    "config/noexist");
            TaskManager manager = new TaskManager(ac);
            manager.download(task1);

            List<byte[]> ivList = manager.getTaskIv(task1);
            if (task1.isFile()) {
                manager.setDecryptionManager(new DecryptionManager(
                        ivList.get(0))
                );
                manager.getDecryptionManager().decryptFile(task1);
            } else {
                // TODO
            }

        } catch (Exception e) {
            e.printStackTrace();
        }*/

        /*try {
            byte[] test = new byte[] {
                    127, -17, -65, 111, 89, -17, 33, 97
            };
            for (int i=0; i<test.length; i++) {
                System.out.print(test[i]+" ");
            }
            System.out.println("\n");

            String str = java.util.Base64.getEncoder().encodeToString(test);
            byte[] test2 = Base64.getDecoder().decode(str);
            for (int i=0; i<test2.length; i++) {
                System.out.println(test2[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
