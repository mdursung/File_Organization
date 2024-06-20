import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

class ReadUtils {

    public static HashMap<String, BufferedWriter> writerHashMap = new HashMap<>();

    public static int[] getIndex() throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader("Index/257/0.txt"));
        String line;
        int i = 0;
        int[] limitArray = new int[257];

        while ((line = reader.readLine()) != null) {
            limitArray[i++] = Integer.parseInt(line);
        }
        return limitArray;
    }

    public static HashSet<String> getPasswords() throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader("Processed/passwords.txt"));
        String line;
        HashSet<String> passwords = new HashSet<>();
        while ((line = reader.readLine()) != null) {
            passwords.add(line.trim());
        }
        return passwords;
    }

    public static void readOneFile(String fileName, HashSet<String> passwordHashSet, int[] limitArray) throws Exception {

        String line;
        BufferedReader reader = new BufferedReader(new FileReader("Unprocessed-Passwords/" + fileName));
        while ((line = reader.readLine()) != null) {
            if (!passwordHashSet.contains(line.trim())) { //
                passwordHashSet.add(line.trim());

                String firstChar = String.valueOf((int) line.charAt(0)); //ascii kodunu alır
                String path = firstChar + "." + limitArray[Integer.parseInt(firstChar)] / 10000;
                FileUtils.createFolder("Index/" + firstChar);

                if (writerHashMap.get(path) == null) { //oluşmuş hiç dosya var mı diye kontrol ediyor
                    writerHashMap.put(path, new BufferedWriter(new FileWriter(
                            "Index/" + firstChar + "/" + (limitArray[Integer.parseInt(firstChar)] / 10000) + ".txt", true)));
                }

                writerHashMap.get(path).write(line + "|" +
                        HashUtils.hashFunc("1", line) + "|" +
                        HashUtils.hashFunc("2", line) + "|" +
                        HashUtils.hashFunc("3", line) + "|" +
                        fileName + "\n");
                writerHashMap.get(path).flush();
                limitArray[Integer.parseInt(firstChar)]++;
            }
        }
    }

    public static void readFiles() throws Exception {

        FileUtils.createAllFs();

        BufferedWriter writer = new BufferedWriter(new FileWriter("Index/257/0.txt"));
        HashSet<String> passwords = getPasswords();
        int[] limitArray = getIndex();

        for (String fileName : new File("Unprocessed-Passwords").list()) {
            readOneFile(fileName, passwords, limitArray);
            FileUtils.moveFiles(fileName);
        }

        for (int line : limitArray) {
            writer.write(line + "\n");
            writer.flush();
        }

        BufferedWriter writer2 = new BufferedWriter(new FileWriter("Processed/passwords.txt"));
        for (String line : passwords) {
            writer2.write(line + "\n");
            writer2.flush();
        }
    }
}

