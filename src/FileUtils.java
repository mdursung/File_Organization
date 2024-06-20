import java.io.File;

class FileUtils {

    public static File createFile(String... path) throws Exception {
        File file = new File(String.join("/", path));
        if (!file.exists()) {
            if (file.getParentFile().mkdirs() || file.createNewFile()) return file;
            throw new Exception("file could not be created!");
        }
        return file;
    }

    public static File createFolder(String... path) throws Exception {
        File folder = new File(String.join("/", path));
        if (!folder.exists()) {
            if (folder.mkdirs()) return folder;
            throw new Exception("folder could not be created!");
        }
        return folder;
    }

    public static boolean checkFolders() {

        File file = new File("Processed");
        File file2 = new File("Code");
        File file3 = new File("Index");
        File file4 = new File("Unprocessed-Passwords");
        boolean flag = false;
        if (file4.exists()) {
            flag = (file4.list().length != 0);
        }

        return !file.exists() || !file2.exists() || !file3.exists() || flag;

    }

    public static void createAllFs() throws Exception {

        FileUtils.createFolder("Code/");
        FileUtils.createFolder("Index/");
        FileUtils.createFolder("Index/257/");
        FileUtils.createFolder("Processed/");
        FileUtils.createFile("Index/257/0.txt");
        FileUtils.createFile("Processed/passwords.txt");
    }

    public static void moveFiles(String fileName) {

        File f = new File("Unprocessed-Passwords/" + fileName);
        f.renameTo(new File("Processed/" + fileName));
    }
}

