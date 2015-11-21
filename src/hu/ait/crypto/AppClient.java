package hu.ait.crypto;

import java.io.*;

public class AppClient {

    private static final String CLIENT_CONFIG = "config/client-config.txt";

    private boolean initialUse;
    private String accountName;
    private String accountKey;

    public AppClient() {

        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(CLIENT_CONFIG)
            );
            initialUse = Boolean.parseBoolean(br.readLine());
            accountName = br.readLine();
            accountKey = br.readLine();
        } catch (FileNotFoundException e) {
            System.out.println("Missing configuration file!");
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Configuration file is corrupted");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountKey() {
        return accountKey;
    }
}
