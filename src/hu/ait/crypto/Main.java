package hu.ait.crypto;

import java.io.FileNotFoundException;
import java.io.StreamCorruptedException;

public class Main {

    public static void main(String[] args) {
        try {
            AppClient ac = new AppClient();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (StreamCorruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
