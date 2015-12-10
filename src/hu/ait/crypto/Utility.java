package hu.ait.crypto;

public final class Utility {

    public static final String CONNECTION_SCHEME = "https";

    public static final String STORAGE_UPLOADTASK = "hu.ait.crypto.storage" +
            ".UploadTask";
    public static final String STORAGE_UPLOADMANAGER = "hu.ait.crypto.storage" +
            ".TaskManager";

    public static String inferContainerNameFromPath(String path)
            throws IllegalArgumentException {
        String[] temp = path.split("/");

        if (!path.contains("/") || temp[0] == null
                || temp[0].equals("")) {
            throw new IllegalArgumentException();
        } else {
            return temp[0];
        }
    }

    public static String inferCloudPathFromPath(String path, String fileName)
            throws IllegalArgumentException {
        if (!path.contains("/")) {
            throw new IllegalArgumentException();
        } else {
            int separatorIndex = path.indexOf("/");
            return (path.charAt(path.length()-1) == '/') ?
                    path.substring(separatorIndex+1).concat(fileName) :
                    path.substring(separatorIndex+1).concat("/" + fileName);
        }
    }

    public static String inferCloudFileNameFromPath(String path) {
        int lastDelimiter = path.lastIndexOf("/");
        return path.substring(lastDelimiter+1);
    }

    public static String deleteContainerNameFromPath(String path) {
        int firstDelimiter = path.indexOf("/", 1);
        return path.substring(firstDelimiter+1);
    }

    public static String getTempCloudFileName(String fromPath) {
        String result = deleteContainerNameFromPath(fromPath);
        int separator = result.lastIndexOf(".");
        if (separator != -1) {
            return result.substring(0, separator).concat("_temp")
                    .concat(result.substring(separator, result.length()));
        } else {
            return result.concat("_temp");
        }
    }

    public static String getTempCloudPath(String fromPath) {
        int separator = fromPath.lastIndexOf(".");
        if (separator != -1) {
            return fromPath.substring(0, separator).concat("_temp")
                    .concat(fromPath.substring(separator, fromPath.length()));
        } else {
            return fromPath.concat("_temp");
        }
    }

    public static void handleException(Exception e, Class c) {
        switch (e.toString()) {
            case "java.io.FileNotFoundException":
                if (c.getName().equals(STORAGE_UPLOADTASK)) {
                    System.out.println("The file path format is wrong, or the" +
                            " file does not exist.");
                    System.exit(0);
                }
                break;
            case "java.io.IOException":
                if (c.getName().equals(STORAGE_UPLOADMANAGER)) {
                    System.out.println("The file cannot be found");
                    System.exit(0);
                }
                break;
            default:
                System.out.println("An unknown exception occurred.");
                e.printStackTrace();
                System.exit(1);
        }
    }
}
