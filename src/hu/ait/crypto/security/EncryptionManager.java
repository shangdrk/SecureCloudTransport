package hu.ait.crypto.security;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import java.io.*;
import java.security.InvalidKeyException;

public class EncryptionManager {

    public static final String ENCRYPTION_SPECS = "AES/CBC/PKCS5Padding";

    private SecretKey key;
    private Cipher cipher;
    private File encryptedTemp;
    private byte[] iv;

    public EncryptionManager() {

        try {
            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream("config/key.txt")
            );
            key = (SecretKey) ois.readObject();

            cipher = Cipher.getInstance(ENCRYPTION_SPECS);
        } catch (FileNotFoundException e) {
            // TODO
        } catch (IOException e) {
            // TODO
        } catch (ClassNotFoundException e) {
            // TODO
        } catch (Exception e) {
            // TODO
        }

        if (cipher != null && key != null) {
            try {
                cipher.init(Cipher.ENCRYPT_MODE, key);
                iv = cipher.getIV();
            } catch (InvalidKeyException e) {
                // TODO
            }
        }
    }

    public void encryptFile(String filename) {
        try {
            CipherOutputStream cipherWriter = new CipherOutputStream(
                    new FileOutputStream(
                            getEncryptionTemp(filename)
                    ), cipher
            );
            FileInputStream fileReader = new FileInputStream(filename);

            int aByte;
            while ((aByte = fileReader.read()) != -1) {
                cipherWriter.write(aByte);
            }

            cipherWriter.close();
            fileReader.close();
        } catch (FileNotFoundException e) {
            // TODO
        } catch (IOException e) {
            // TODO
        }
    }

    public File getEncryptionTemp(String filename) {
        File temp = null;
        File current = new File(filename);
        try {
            if (current.getParent() != null) {
                temp = File.createTempFile(current.getName(),
                        null, new File(current.getParent()));
            } else {
                temp = File.createTempFile(current.getName(),
                        null, new File(System.getProperty("user.dir")));
            }
            encryptedTemp = temp;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return temp;
    }

    public byte[] getIv() {
        return iv;
    }

    public File getEncryptedTemp() {
        File temp = encryptedTemp;
        encryptedTemp = null;

        return temp;
    }

    public void cleanUp(String fileName) {
        File garbage = new File(fileName);
        garbage.delete();
    }
}
