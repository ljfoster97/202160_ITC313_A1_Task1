import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 *
 * @author Lyndon Foster
 */
public class Task1_2 {

    // Create main ArrayList to store each line as an ArrayList of strings.
    // private static ArrayList<String[]> dataArrayList = new ArrayList<>();
    private static List<String[]> dataArrayList = new ArrayList<>(Collections.emptyList());

    // Global variables for filenames.
    static String fileName = "rawdata.txt";
    static String dataOutput = "data.txt";
    static String nameOutput = "name.txt";
    static String normalizedOutput = "normalizeddata.txt";

    /**
     * @param args not used.
     */
    public static void main(String[] args) {

        boolean loop = true;

        // Create new Scanner object for user input.
        Scanner input = new Scanner(System.in);

        // Print user prompt.
        System.out.println("""
                *****Welcome to Data Pre-Processing Program*****
                                
                1. Generate data file and name file
                2. Normalized data file
                3. Exit program""");

        // While loop for input validation.
        while (loop) {

            // Used switch statement here given we have numerical options that will run different processes,
            // as opposed to using an if/else structure.
            switch (input.nextLine()) {

                // Call readFile() function to generate data file and name file.
                case "1" -> readFile();

                // Call function to generate normalizeddata.txt file from data.txt
                case "2" -> System.out.println("normalized");

                // Break loop and exit program.
                case "3" -> {
                    System.out.println("Goodbye.");
                    loop = false;
                }

                // Any input other than 1,2,3 prints error message,
                // but allows the user to input again without restarting.
                default -> System.out.println("Error! Invalid input.");
            }
        }
    }

    /**
     * Method to read in raw data file,
     * create ArrayList of data objects to process the data,
     * then format and write data from these objects to file.
     * Could potentially add
     */
    public static void readFile() {
        // Print message as confirmation for user input confirmation.
        System.out.println("Generating data file...");

        // Try/catch block in case of FileNotFoundException.
        try {
            // Create new scanner object to read file.
            Scanner scanner = new Scanner(new File(fileName));

            // Loop through the file starting at first line.
            do {
                // Get current line from scanner.
                String line = scanner.nextLine();
                // Check to see if the line is empty.
                if (line.isEmpty()) {
                    // Skip empty lines.
                    do {
                        // Get the next line if current line is empty.
                        line = scanner.nextLine();
                    }
                    // Post check.
                    while (line.isEmpty());
                }
                // Add the ArrayList of the current line to the main dataArrayList.
                dataArrayList.add(line.split("\\s+"));
            }
            // End loop when last line of file has been read.
            while (scanner.hasNextLine());


        }
        // Catch block in case file doesn't exist.
        catch (FileNotFoundException e) {
            throw new RuntimeException(
                    "Error! \"" + fileName + "\" not found.", e);
        }

        for (String[] d : dataArrayList)
            System.out.println(Arrays.toString(d) + "\n");
        dataArrayList.removeIf(Task1_2::valueCheck);

        for (String[] d : dataArrayList)
            System.out.println(Arrays.toString(d) + "\n");


        domainCheck(dataArrayList);

    }

    public static boolean valueCheck(String[] d) {
        boolean missingVal = false;
        for (String s : d) {

            if (String.valueOf(s).matches("[?]")) {
                missingVal = true;
            }
        }
        return missingVal;
    }

    public static boolean domainCheck(List<String[]> dataArrayList) {
        boolean duplicateDomain =  false;
            List<String> list = new ArrayList<>();
//            System.out.println("current:"+Arrays.toString(dataArrayList);
            for (String[] line: dataArrayList) {
                System.out.println("line"+ Arrays.toString(line));
                for (String s: line) {
                    list.add(s);
                    System.out.println("string:"+s);
                }
                System.out.println(list);
            }
            for (int i = 0; i < (dataArrayList.get(i).length); i++) {
                String s = list.get(i);
                System.out.println("list:"+s);

            }
      return duplicateDomain;
    }


}

