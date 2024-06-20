import java.io.*;
import java.util.*;


public class SearchUtils {

    static String line;
    static Random rand = new Random();
    static HashMap<String, BufferedReader> bufferedReaderHashMap = new HashMap<>();
    static HashMap<Integer, Integer> txtListHashMap = new HashMap<>();

    public static void createReaders() throws FileNotFoundException {

        String[] fileNames2 = new File("Index").list();

        for (String fileName2 : fileNames2) {
            String[] fileNames3 = new File("Index/" + fileName2).list();
            for (String fileName3 : fileNames3) {
                bufferedReaderHashMap.put((fileName2 + "." + fileName3), new BufferedReader(new FileReader("Index/" + fileName2 + "/" + fileName3)));
            }
            txtListHashMap.put(Integer.valueOf(fileName2), fileNames3.length);
        }
    }

    public static double averageSearchTimeCalculator() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader("Processed/passwords.txt"));
        List<String> passwordList = new ArrayList<>();
        reader.lines().forEach(passwordList::add);
        double averageSeekTime = 0;

        for (int i = 0; i < 3000; i++) {
            int randomNumber = rand.nextInt(passwordList.size());
            String password = passwordList.get(randomNumber);

            long averageStartTime = System.nanoTime();
            createReaders();
            searchOneTxt(password);
            averageSeekTime += System.nanoTime() - averageStartTime;
        }

        return ((averageSeekTime / 1000000.0) / 3000.0);
    }

    public static void search() throws Exception {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Average Search Time of 3000 password is: " + averageSearchTimeCalculator() + "ms");

        while (true) {
            createReaders();
            System.out.println("Please enter the password you wish to search: ");
            String password = scanner.nextLine();
            if (password.equals("I want to quit!")) {
                return;
            }

            long startTime = System.nanoTime();
            String answer = searchOneTxt(password);
            long elapsedTime = System.nanoTime() - startTime;

            if (answer != null) {
                System.out.println(answer);
                System.out.println("Time for searching: " + elapsedTime / 1000000.0 + " millisecond\nIf you want to quit, write \"I want to quit!\" please.");
            } else {
                writeNewPassword(password);
            }
        }

    }

    public static void writeNewPassword(String password) throws Exception {
        int[] limitArray = ReadUtils.getIndex();
        int asciiCode = password.charAt(0);
        BufferedWriter writer;

        if (asciiCode < 256) {
            FileUtils.createFolder("Index/" + asciiCode);
            FileUtils.createFile("Index/" + asciiCode + "/" + limitArray[asciiCode] / 10000 + ".txt");
            writer = new BufferedWriter(new FileWriter("Index/" + asciiCode + "/" + limitArray[asciiCode] / 10000 + ".txt", true));
            limitArray[Integer.parseInt(String.valueOf(asciiCode))]++;
        } else {
            FileUtils.createFolder("Index/256");
            FileUtils.createFile("Index/256/" + limitArray[256] / 10000 + ".txt");
            writer = new BufferedWriter(new FileWriter("Index/256/" + limitArray[256] / 10000 + ".txt", true));
            limitArray[256]++;
        }

        writer.write(password
                + "|" + HashUtils.hashFunc("1", password)
                + "|" + HashUtils.hashFunc("2", password)
                + "|" + HashUtils.hashFunc("3", password)
                + "|search\n");
        writer.flush();

        BufferedWriter writerArray = new BufferedWriter(new FileWriter("Index/257/0.txt"));
        for (int line : limitArray) {
            writerArray.write(line + "\n");
            writerArray.flush();
        }

        BufferedWriter writer2 = new BufferedWriter(new FileWriter("Processed/passwords.txt", true));
        writer2.write(password + "\n");
        writer2.flush();
        System.out.println("Your password has been written to the Index File.");
    }

    public static String searchOneTxt(String password, BufferedReader reader) throws Exception {
        while ((line = reader.readLine()) != null) {
            if (line.startsWith(password)) {
                return line;
            }
        }
        return null;
    }

    public static String searchOneTxt(String password) throws Exception {
        int asciiCode = password.charAt(0);
        String find = password + "|";
        try {
            if (asciiCode < 256) {
                for (int k = 0; k < txtListHashMap.get(asciiCode); k++) {
                    String answer = searchOneTxt(find, bufferedReaderHashMap.get(asciiCode + "." + k + ".txt"));
                    if (answer != null) {
                        return answer;
                    }
                }
            } else {
                for (int k = 0; k < txtListHashMap.get(256); k++) {
                    String answer = searchOneTxt(find, bufferedReaderHashMap.get(256 + "." + k + ".txt"));
                    if (answer != null) {
                        return answer;
                    }
                }
            }

        } catch (Exception e) {
            return null;
        }
        return null;
    }
}


