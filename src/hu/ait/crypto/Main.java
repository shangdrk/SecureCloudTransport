package hu.ait.crypto;


import hu.ait.crypto.storage.*;
import hu.ait.crypto.security.*;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        AppClient ac = new AppClient();

        try {
            TaskManager manager = new TaskManager(ac);
            manager.setEncryptionManager(new EncryptionManager());
            UploadTask task1 = manager.createUploadTask("config/shangd.jpg",
                    "example/dir");
            manager.upload(task1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
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
                manager.getDecryptionManager().decryptFile("config/noexist/shangd.jpg");
            } else {
                // TODO
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



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

        // command line arguments to upload/download a file
//        if (args[0].length == 0) {System.out.println("Please specify an argument -u or -d or -l for upload, download, and list")}
//
//
//        if (args[0].equals("-u")) {
//            // TODO establish upload
//            System.out.println("Command line arg: " + args[0]);
//        } else if (args[0].equals("-d")) {
//            // TODO establish download
//            System.out.println("Command line arg: " + args[0]);
//        } else if (args[0].equals("-l")) {
//            // TODO list files in the blob
//            manager.listFiles();
//        }

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
