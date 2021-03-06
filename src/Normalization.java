import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Normalization extends Utilities {
    static String fileName = "rawdata.txt";
    static String dataFile = "data.txt";
    static String nameFile = "name.txt";
    static String normalizedDataFile = "normalizeddata.txt";
    static String newNameFile = "newname.txt";

    public static void generateNormalizedData() {

        // Init primary ArrayList to store Data objects.
        ArrayList<Data> normalizedDataObjectArrayList = new ArrayList<>();

        // Print confirmation message.
        System.out.println("Generating normalizeddata.txt file & newname.txt file...");

        // Try/catch block
        try {
            // Read in the name.txt file
            Scanner scanner = new Scanner(new File(nameFile));
            // do while loop to iterate through file line by line.
            do {
                // Scan next line.
                String line = scanner.nextLine();
                // new ArrayList, commas as delimiter for namefile.
                String[] data = line.split(",");
                // Create a new Data object, init with index 1 of current lines
                // String[] which should be the attribute name.
                Data newData = new Data(data[1]);
                // Set the Data objects attribute type to index 0 of current line String[].
                newData.setDataAttributeType(data[0]);
                if (newData.getDataAttributeType().equals("n")) {
                    //                System.out.println("numerical");
                    // Getting min and max attribute values using specific indexes like this
                    // Relies on having a correctly formatted file.
                    // There is likely a better way to do this that would account for an incorrectly formatted file.
                    newData.setMinimumAttributeValue(Double.parseDouble(data[2]));
                    newData.setMaximumAttributeValue(Double.parseDouble(data[3]));
                } else if (newData.getDataAttributeType().equals("c")) {
                    // It makes the most sense to determine the number of unique domain values from the value stored
                    // at the corresponding index of the specified format, as output by the initial data.txt and
                    // name.txt generation.
                    // However, this also means that if data.txt is modified to have identical domain values
                    // for a categorical attribute, then the repeating attributes will still appear
                    // in the normalizeddata.txt, even if they are found and removed from the newname.txt
                    // by Utilities.buildName();
                    // This unfortunately means calling domainCheck() again;
                    // so there is probably a more efficient way to do it.
                    newData.setUniqueDomainValues(Integer.parseInt(data[2]));
                    ArrayList<String> domainValues = new ArrayList<>(Arrays.asList(data).subList(3, data.length));
                    newData.setDomainValues(domainValues);
                    System.out.println("Normalizing Categorical");
                    System.out.println(Arrays.toString(data));
                }
                // Add the new Data object to the main ArrayList.
                normalizedDataObjectArrayList.add(newData);
            }
            // While loop ends at the end of the file.
            while (scanner.hasNextLine());
            // Close the nameFile.
            scanner.close();

            // Create new Scanner Object for the dataFile.
            Scanner scanner1 = new Scanner(new File(dataFile));
            // Do while loop to read file line by line.
            do {
                // Get line from scanner.
                String line = scanner1.nextLine();
                // Create new list of Strings from the line, stripping any whitespace.
                String[] data = line.split("\s+");
                // For loop to iterate through the list.
                for (int i = 0; i < data.length; i++) {
                    // Add the data lists that were read in from the dataFile to the corresponding data objects
                    // that were created and added to the main ArrayList from the nameFile.
                    try {
                        Data currentData = normalizedDataObjectArrayList.get(i);
                        currentData.addData(data[i]);
                    } catch(Exception e) {
                        System.out.println("Error! " + nameFile + " and " + dataFile
                                + " do not contain the same number of records.");
                    }
                }
            }
            // While loop ends at the end of the file.
            while (scanner1.hasNextLine());
        }
        // Catch block that prints message for FileNotFoundException and the stackTrace.
        catch(FileNotFoundException e) {
            System.out.println("Error! \"" + dataFile + "\" not found.");
            e.printStackTrace();
        }

        // Iterate through the ArrayList<Data>, calling domainCheck on each object and
        // remove objects that have repeating domain values.
        System.out.println("pre domain check");
        System.out.println(normalizedDataObjectArrayList);
        normalizedDataObjectArrayList.removeIf(DataAndNameGeneration::domainCheck);
        System.out.println("pre normalization:");
        System.out.println(normalizedDataObjectArrayList);

        // Call normalization function, parsing in the main ArrayList of data objects.
        normalization(normalizedDataObjectArrayList);
        System.out.println("post normalization");
        System.out.println(normalizedDataObjectArrayList);

        // Parse the now normalized ArrayList of DataObjects to the buildData function which returns a string.
        // Parse the return of buildData to the writeToFile function, specifying the required filename.
        writeToFile(normalizedDataFile, buildData(normalizedDataObjectArrayList));
        writeToFile(newNameFile, buildName(normalizedDataObjectArrayList));
    }

    public static void normalization(ArrayList<Data> normalizedDataObjectArrayList) {
        // For loop to iterate through the ArrayList<NormalizedData>
        for (Data dataObject : normalizedDataObjectArrayList) {
            // Set the flag to true at this point, so that other methods in Utilities can
            // differentiate between raw data and normalized data when building strings to write to file.
            dataObject.setNormalized(true);
            // Create new arraylist<String> to store the normalized values.
            ArrayList<String> normalizedValues = new ArrayList<>();
            // init variables.
            double normalizedValue;
            double attributeValue;
            // Get the minimum and maximum attribute values, and the attribute type.
            double minimumValue = dataObject.getMinimumAttributeValue();
            double maximumValue = dataObject.getMaximumAttributeValue();
            String dataAttributeType = dataObject.getDataAttributeType();

            // Check for equivalent min max attribute values, OR categorical attribute with repeating domainValues.
            if (dataAttributeType.equals("n") && maximumValue == minimumValue
                    || dataAttributeType.equals("c") && dataObject.getUniqueDomainValues() == 1) {
                // Beneficial to print a message here to indicate the anomaly in data.
                System.out.println("Redundant values found, removing...");
                // Add the empty initialized arraylist to the dataObject, otherwise a NullPointerException error will occur.
                // When the ArrayList<String> for this object is iterated through to build a string and write to output file,
                // buildNormalizedData() will just skip any empty ArrayList<String> and move to the next object.
                dataObject.setNormalizedValues(normalizedValues);
                // Skip current iteration, move to next object in the ArrayList<NormalizedData>.
                continue;
            }
            // Iterate through the individual strings.
                for (String attribute : dataObject.getData()) {
                    // need nested for loop for the rest, compare the min and max in a seperate outer loop.
                    if (dataAttributeType.equals("n") && maximumValue <= 1) {
                        normalizedValues.add(attribute);
                    }
                    // Check if the attribute is numerical to perform normalization operation.
                    else if (dataAttributeType.equals("n")) {
                        // Cast string to integer.
                        attributeValue = Double.parseDouble(attribute);
                        // Normalization operation on integers, cast to double.
                        normalizedValue = (attributeValue - minimumValue) / (maximumValue - minimumValue);
                        // Add the string value of the normalization operation to the new arraylist, format to 3 decimal places.
                        normalizedValues.add(String.format("%.3f", normalizedValue));
                    }
                    // Else if the attribute is categorical, just add to the list without any operation.
                    else if (dataAttributeType.equals("c")) {
                        normalizedValues.add(attribute);
                    }
                    // Add the newly created arraylist of normalizedValues to the dataObject.
                    dataObject.setNormalizedValues(normalizedValues);
                }
            }
        }

//    // Unfortunately this method is basically the same as the one as the one for generating the data.txt file.
//    // The other method could likely be reused without having to write these few lines again.
//    // I would have to combine NormalizedData object into Data object, or implement an interface for this function?
//    // I could not figure out how to do it more efficiently at this stage, and wasn't sure how much encapsulation
//    // would be considered best practice between the data and normalized data objects,
//    // or if I should have a single class for data.
//    // Try to merge with other function.
//    public static String buildNormalizedData(ArrayList<Data> arrayList) {
//        StringBuilder stringBuilder = new StringBuilder();
//        // Init counter outside loop.
//        int i = 0;
//        // Do while loop
//        do {
//            // Iterate through each dataObject in the ArrayList
//            for (Data dataObject : arrayList) {
//                // Create a list of strings for the data of the current object.
//                List<String> stringArrayList = dataObject.getNormalizedValues();
//                // If there were duplicate values or equivalent min/max attribute values found
//                // in normalization operation, the arrayList was initialized but will be empty.
//                // Skip any empty arraylists to avoid index out of bounds exception
//                // and only write the correct normalized values to file.
//                if (!stringArrayList.isEmpty()) {
//                    // Append the string at index i for the current data object.
//                    stringBuilder.append(stringArrayList.get(i)).append(" ");
//                }
//            }
//            // Add a new line.
//            stringBuilder.append("\n");
//            // Increment counter.
//            i++;
//        }
//        // Loop through entire arrayList size.
//        while (i <= arrayList.size());
//        // Return string that was created, ready to write to file.
//        return String.valueOf(stringBuilder);
//    }

//    // Try to merge with other function.
//    public static String buildNewName(ArrayList<Data> arrayList) {
//        // Create new StringBuilder.
//        StringBuilder stringBuilder = new StringBuilder();
//        // Iterate through each Data object in the main ArrayList
//        for (Data dataObject: arrayList) {
//            // Init empty string.
//            String currentColumn = "";
//            // Get the attribute type of the current Data object.
//            String dataAttributeType = dataObject.getDataAttributeType();
//            // Get the normalizedValues List<String> from the current Data object.
//            ArrayList<String> stringArrayList = dataObject.getNormalizedValues();
//            System.out.println(stringArrayList);
//            System.out.println(dataObject.getMinimumAttributeValue());
//            System.out.println(dataObject.getMaximumAttributeValue());
//            // If the attribute type of the current data object is numerical
//            if (dataAttributeType.equals("n")) {
//                currentColumn = dataObject.toString();
//            }
//            // Otherwise if the attribute type is categorical.
//            else if (dataAttributeType.equals("c")) {
//                System.out.println("Categorical pre");
//                System.out.println(dataObject);
//                System.out.println(stringArrayList);
////                DataAndNameGeneration.findUniqueDomainValues(dataObject, stringArrayList);
//                currentColumn = dataObject.toString();
//                System.out.println("cat post");
//                System.out.println(dataObject);
//                System.out.println(stringArrayList);
//                System.out.println(currentColumn);
//            }
//            // Check if the list is empty or not. If the list is empty then normalization method found
//            // repeating categorical domainvalues or equivalent numerical min max domain values,
//            // so the attribute should be removed from newname.txt.
//            if (!stringArrayList.isEmpty()) {
//                // Append the current attribute column to the StringBuilder.
//                stringBuilder.append(currentColumn);
//            }
//        }
//        // Return the final string from StringBuilder after every Data object in the primary ArrayList has been parsed.
//        return stringBuilder.toString();
//    }
}