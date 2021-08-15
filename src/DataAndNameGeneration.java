import java.io.*;
import java.util.*;

public class DataAndNameGeneration extends Utilities {
    // Global variables for filenames.
    static String fileName = "rawdata.txt";
    static String dataFile = "data.txt";
    static String nameFile = "name.txt";
    static String normalizedOutput = "normalizeddata.txt";


    public static void generateNameAndDataFile() {
        ArrayList<Data> dataObjectArrayList = new ArrayList<>();
        // Print message as confirmation for selection.
        System.out.println("Generating data file and name file...");

        // Try/catch block for file validation.
            try {
                // Create a new Scanner object to read in the file.
                Scanner scanner = new Scanner(new File(fileName));

                // The first line of the format specifies column names for data,
                // so only the first line is read.
                String firstLine = scanner.nextLine();

                // Split strings in the first line and add them to a list.
                // Regex to remove all whitespace.
                // The format in the subject outline has inconsistent spacing between items,
                // so this is the most effective way to account for that.
                String[] name = firstLine.split("\\s+");

                // Iterate through strings in the list.
                for (String columnName : name) {

                    // Create new data object using the name of the column.
                    Data newData = new Data(columnName);

                    // Add the new data object
                    dataObjectArrayList.add(newData);
                }

                // Read the next line to get attributeType of data column.
                String secondLine = scanner.nextLine();

                // Every second line of the specified format in the subject outline is blank,
                // so we need to account for blank lines.
                // Check if the current line is empty.
                if (secondLine.isEmpty()) {
                    do {
                        // Skip the empty line.
                        secondLine = scanner.nextLine();
                    }
                    // Exit loop when line is not empty.
                    while (secondLine.isEmpty());
                }

            // Split strings and add to list.
            String[] type = secondLine.split("\\s+");

            // Data objects have already been created and added to the dataObjectArrayList,
            // now iterate through the ArrayList and add the second line of rawdata.txt
            // to the corresponding Data objects as the dataAttributeType.
            // init count at 0 (start index of ArrayList)
            int i = 0;
            // Iterate through each string in the list that was created from the second line.
            for (String s : type) {

                // Get the corresponding data object at current index of arrayList.
                Data currentData = dataObjectArrayList.get(i);

                // Set the dataAttributeType of the current data object based on
                // the corresponding string from secondline of rawdata.txt.
                currentData.setDataAttributeType(s);

                // Increment counter.
                i++;
            }

            // Do-while loop to iterate through the rest of the file as there is an unknown number of data lines.
            do {
                // Get next line from scanner and assign to String "line".
                String line = scanner.nextLine();
                // Set index count to 0.
                int j = 0;
                // Check if the line is empty as format specified in subject outline has blank lines.
                if (line.isEmpty()) {
                    do {
                        // Skip the empty line.
                        line = scanner.nextLine();
                    }
                    // Exit loop when line is not empty.
                    while (line.isEmpty());
                }
                // Create string list with all the data from the line that was just read, removing any whitespace.
                String[] data = line.split("\\s+");

                // Get the individual strings from the list.
                for (String s : data) {
                    // Get data object at current index count.
                    Data currentData = dataObjectArrayList.get(j);
                    // Add data string from current index to the current Data object.
                    currentData.addData(s);
                    // Increment counter.
                    j++;
                }
            }
            // Post check to stop at end of file.
            while (scanner.hasNextLine());
        }
        // Catch block in case file doesn't exist.
        catch (FileNotFoundException e) {
            throw new RuntimeException(
                    "Error! \"" + fileName + "\" not found.", e);
        }
        // Call valueCheck() to iterate through the dataObjectArrayList and check for missing values.
        valueCheck(dataObjectArrayList);

        // Iterate through dataObjectArrayList,
        // remove any dataObject that returns true from domainCheck method.
        dataObjectArrayList.removeIf(DataAndNameGeneration::domainCheck);

        writeToFile(dataFile, buildData(dataObjectArrayList));
        writeToFile(nameFile, buildName(dataObjectArrayList));

        // Print menu options again to allow next step, normalization operation.
        Task1.mainMenu();
    }

    public static void valueCheck(ArrayList<Data> dataObjectArrayList) {
        // Iterate through all Data objects in the ArrayList<Data> dataObjectArrayList.
        for (Data dataObject : dataObjectArrayList) {
            // Create ArrayList<String> and fetch the data for each Data object.
            ArrayList<String> stringArrayList = dataObject.getData();

            // For loop to iterate through the ArrayList<String> for each object.
            for (int i = 0; i < stringArrayList.size(); i++) {
                // Get each individual string from the ArrayList at the current index.
                String currentData = stringArrayList.get(i);
                // Check for missing data value as assigned by "?"
                if (currentData.matches("[?]")) {
                    // If a missing value is found, then iterate through the ArrayList<Data> dataObjectArrayList
                    // again and remove the values from every other data object instance at the corresponding index.
                    for (Data data : dataObjectArrayList) {
                        // Remove data at current index.
                        data.removeData(i);
                    }
                }
            }
        }
    }

}
