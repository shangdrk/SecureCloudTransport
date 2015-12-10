package hu.ait.crypto;


import com.microsoft.azure.storage.StorageException;
import hu.ait.crypto.storage.*;
import hu.ait.crypto.security.*;

import java.net.URISyntaxException;
import java.util.List;



public class Main {

    public static final String USAGE = "User interface:\n" +
            "\n" +
            "(SecureCloudTransport = SCT)\n" +
            "\n" +
            "Case 1: Upload a file\n" +
            "SCT -u \"file/path/example.txt\"\n" +
            "\n" +
            "Case 2: Download a file\n" +
            "SCT -d example.txt \"my/file/path/\"\n" +
            "\n" +
            "Case 3: List files on the cloud\n" +
            "SCT -l\n" +
            "\n" +
            "Case 4: Generate a new key\n" +
            "SCT -k\n";

    public static void main(String[] args) {
        AppClient ac = new AppClient();

//        try {
//            TaskManager manager = new TaskManager(ac);
//            manager.setEncryptionManager(new EncryptionManager());
//            UploadTask task1 = manager.createUploadTask("config/shangd.jpg",
//                    "example/dir");
//            manager.upload(task1);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        try {
//            DownloadTask task1 = new DownloadTask(ac,
//                    "example/dir/shangd.jpg4947572406554475133.tmp",
//                    "config/noexist");
//            TaskManager manager = new TaskManager(ac);
//            manager.download(task1);
//
//
//
//            List<byte[]> ivList = manager.getTaskIv(task1);
//            if (task1.isFile()) {
//                manager.setDecryptionManager(new DecryptionManager(
//                        ivList.get(0))
//                );
//                manager.getDecryptionManager().decryptFile("config/noexist/shangd.jpg");
//            } else {
//                // TODO
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }



        // command line arguments
        if (args.length == 0) {
            System.out.println(USAGE);
        } else {
            TaskManager manager = new TaskManager(ac);

            if (args.length == 2 && args[0].equals("-u")) {
                System.out.println("Command line arg: " + args[0]);
                try {
                    manager.setEncryptionManager(new EncryptionManager());
                    UploadTask task1 = manager.createUploadTask(args[1],
                            "example/");
                    manager.upload(task1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (args.length == 3 && args[0].equals("-d")) {
                // TODO complete download
                System.out.println("Command line arg: " + args[0]);
                try {

                    DownloadTask task1 = new DownloadTask(ac,
                            "example/" + args[1],
                            args[2] //"config/noexist"
                    );
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
                }

            } else if (args[0].equals("-l")) {

                try {
                    manager.listFiles();
                } catch (StorageException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            } else if (args[0].equals("-k")) {
                // TODO Generate new key here
            } else {
                System.out.println("Unexpected input. Check usage:");
                System.out.println(USAGE);
            }
        }
    }
}
