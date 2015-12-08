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
    public static byte[] iv;

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
                            getEncryptionTempName(filename)
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

    protected String getEncryptionTempName(String filename) {
        int lastDot = filename.lastIndexOf(".");
        if (lastDot >= 0) {
            return "_~".concat(filename.substring(0, lastDot));
        } else {
            return "_~".concat(filename);
        }
    }
}
