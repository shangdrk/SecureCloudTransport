package hu.ait.crypto.security;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

public class DecryptionManager {

    public static final String DECRYPTION_SPECS = "AES/CBC/PKCS5Padding";

    private SecretKey key;
    private Cipher cipher;

    public DecryptionManager(byte[] iv) {
        try {
            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream("config/key.txt")
            );
            key = (SecretKey) ois.readObject();

            cipher = Cipher.getInstance(DECRYPTION_SPECS);
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
                cipher.init(Cipher.DECRYPT_MODE, key,
                        new IvParameterSpec(iv)
                );
            } catch (InvalidKeyException e) {
                // TODO
            } catch (InvalidAlgorithmParameterException e) {
                // TODO
            }
        }
    }

    public void decryptFile(String filename) {
        try {
            CipherInputStream cipherReader = new CipherInputStream(
                    new FileInputStream(filename), cipher
            ); // _~message
            FileOutputStream fileWriter = new FileOutputStream(
                    finalizeFilename(filename)
            ); // message.txt

            int aByte;
            while ((aByte = cipherReader.read()) != -1) {
                fileWriter.write(aByte);
            }

            fileWriter.close();
            cipherReader.close();
        } catch (FileNotFoundException e) {
            // TODO
        } catch (IOException e) {
            // TODO
        }
    }

    public String finalizeFilename(String filename) {
        // TODO
        return "shangd.jpg";
    }
}
