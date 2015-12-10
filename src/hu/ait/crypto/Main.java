package hu.ait.crypto;


import com.microsoft.azure.storage.StorageException;
import hu.ait.crypto.storage.*;
import hu.ait.crypto.security.*;

import java.net.URISyntaxException;
import java.util.List;

public class Main {

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

        // command line arguments
        if (args.length == 0 ) {
            System.out.println("Usage: specify an argument -u or -d or -l or -k for upload, download, list, and new key generation.");
        } else {
            TaskManager manager = new TaskManager(ac);

            if (args[0].equals("-u")) {
                System.out.println("Command line arg: " + args[0]);
                try {
                    manager.setEncryptionManager(new EncryptionManager());
                    UploadTask task1 = manager.createUploadTask(args[1],
                            "example/");
                    manager.upload(task1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (args[0].equals("-d")) {
                // TODO complete download 
                System.out.println("Command line arg: " + args[0]);
                try {
                    DownloadTask task1 = new DownloadTask(ac,
                            "example/fun2.txt",
                            "config/noexist");
                    manager.download(task1);


                    List<byte[]> ivList = manager.getTaskIv(task1);
                    if (task1.isFile()) {
                        manager.setDecryptionManager(new DecryptionManager(
                                ivList.get(0))
                        );
                        manager.getDecryptionManager().decryptFile("config/noexist/shangd.jpg");
                    } else {
                        // TODO
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
            }
        }






//        if (args.length > 0) {
//            try {
//                firstArg = Integer.parseInt(args[0]);
//            } catch (NumberFormatException e) {
//                System.err.println("Argument" + args[0] + " must be an integer.");
//                System.exit(1);
//            }
//        }
    }
}
