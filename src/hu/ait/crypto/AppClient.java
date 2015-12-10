package hu.ait.crypto;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.ListBlobItem;

import java.io.*;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

public class AppClient {

    private static final String CLIENT_CONFIG = "config/client-config.txt";
    private static final String DEFAULT_ENDPOINTS_PROTOCOL = "https";

    private boolean initialUse;
    private String accountName;
    private String accountKey;
    private CloudStorageAccount csAccount;
    private CloudBlobClient cbClient;

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

        String connectionString = "DefaultEndpointsProtocol=" +
                DEFAULT_ENDPOINTS_PROTOCOL + ";"
                + "AccountName=" + accountName + ";"
                + "AccountKey=" + accountKey;
        try {
            csAccount = CloudStorageAccount.parse(connectionString);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.exit(1);
        }

        cbClient = csAccount.createCloudBlobClient();
        // experimenting
        CloudBlobContainer container = null;
        try {
            container = cbClient.getContainerReference("flashy");

            for (ListBlobItem blobItem : container.listBlobs()) {
                System.out.println(blobItem.getUri());
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (StorageException e) {
            e.printStackTrace();
        }

    }

    public String getAccountName() {
        return accountName;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public CloudStorageAccount getCloudStorageAccount() {
        return csAccount;
    }

    public CloudBlobClient getCloudBlobClient() {
        return cbClient;
    }
}
