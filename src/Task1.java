import java.io.*;
import java.util.*;

/**
 *
 * @author Lyndon Foster
 */
public class Task1 {
    public static String menuOptions = ("""
                1. Generate data file and name file
                2. Normalized data file
                3. Exit program""");

    /**
     * @param args not used.
     */
    public static void main(String[] args) {

        // Print user prompt.
        System.out.println("*****Welcome to Data Pre-Processing Program*****\n"+menuOptions);

        mainMenu();
    }

    public static void mainMenu(){
        // bool for input validation loop.
        boolean loop = true;

        // Create new Scanner object for user input.
        Scanner input = new Scanner(System.in);

        // While loop for input validation.
        while (loop) {

            // Used switch statement here given we have numerical options that will run different processes,
            // as opposed to using an if/else structure.
            switch (input.nextLine()) {

                // Call function to generate data file and name file.
                case "1" -> {
                    DataAndNameGeneration.generateNameAndDataFile();
                }

                // Call function to generate normalizeddata.txt file from data.txt
                case "2" -> {
                    Normalization.generateNormalizedData();
                }

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
}