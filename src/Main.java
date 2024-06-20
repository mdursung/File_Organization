public class Main {

    public static void main(String[] args) throws Exception {

        if (FileUtils.checkFolders()) {
            long start = System.currentTimeMillis();
            ReadUtils.readFiles();
            long end = System.currentTimeMillis();
            System.out.println("All files were indexed in " + (end - start) / 1000 + " seconds.");
        }

        try {
            SearchUtils.search();
        } catch (Exception e) {
            System.out.println("There is an error in Files and Folders, please re-index all passwords.");
        }
    }
}

