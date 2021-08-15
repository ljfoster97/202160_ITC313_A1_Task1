import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public abstract class Utilities {
    public static void writeToFile(String fileName, String data) {
        // Try/catch block for exception handling.
        // Created new bufferedwriter with filename.
        try (BufferedWriter out = new BufferedWriter(
                new FileWriter(fileName))) {
            // Print message as confirmation.
            System.out.println("Writing data to " + fileName + "...");
            // Write the data to file.
            out.write(data);
        } catch(IOException e) {
            System.out.println("Error! Writing to " + fileName + "failed.");
            e.printStackTrace();
        }
        // Print message as confirmation if successful.
        finally {
            System.out.println(fileName + " complete!");
        }
    }

    public static String buildData(ArrayList<Data> arrayList) {
        //        System.out.println(arrayList);
        // Create new StringBuilder object.
        StringBuilder stringBuilder = new StringBuilder();
        // Init counter.
        int i = 0;
        // Do while loop
        do {
            // Iterate through each dataObject in the ArrayList
            for (Data dataObject : arrayList) {
                ArrayList<String> stringArrayList;
                // Create a list of strings for the data of the current object.
                if (dataObject.isNormalized()) {
                    stringArrayList = dataObject.getNormalizedValues();
                }
                //
                //                else if (dataObject.isNormalized() && dataObject.getDataAttributeType().equals("c")){
                //                    System.out.println("categorical");
                //                    stringArrayList = dataObject.getData();
                //                    System.out.println(stringArrayList);
                //                }
                else {
                    stringArrayList = dataObject.getData();
                }
                // Append the string at index i for the current data object.
                if (! stringArrayList.isEmpty()) {
                    stringBuilder.append(stringArrayList.get(i)).append(" ");
                }
            }
            // Add a new line.
            stringBuilder.append("\n");
            // Increase counter.
            i++;
        }
        // Loop through entire arrayList size.
        while (i <= arrayList.size());
        // Return string that was created, ready to write to file.
        return String.valueOf(stringBuilder);
    }

    public static String buildName(ArrayList<Data> dataObjectArrayList) {
        // Create new StringBuilder.
        StringBuilder stringBuilder = new StringBuilder();
        // Iterate through arrayList
        for (Data dataObject : dataObjectArrayList) {
            // Init blank string for current column.
            String currentColumn = "";
            // Get the attribute type.
            String dataAttributeType = dataObject.getDataAttributeType();
            // Get the rest of the data.
            ArrayList<String> stringArrayList = dataObject.getData();
            if ((! dataObject.isNormalized())) {
                if (dataAttributeType.equals("n")) {
                    // Create ArrayList<Integer> to easily sort the numerical data.
                    ArrayList<Integer> numericalData = new ArrayList<>();
                    // Iterate through the data of the current column.
                    for (String s : stringArrayList) {
                        // Convert each string to an integer and add to the new ArrayList.
                        int i = Integer.parseInt(s);
                        numericalData.add(i);
                    }
                    // Call collections.sort() to quickly find minimum and maximum values.
                    Collections.sort(numericalData);
                    // minimum integer value of arraylist will be first index.
                    dataObject.setMinimumAttributeValue(numericalData.get(0));
                    // maximum integer value of arraylist will be last index,
                    // equivalent to the size of the arraylist - 1.
                    dataObject.setMaximumAttributeValue(numericalData.get(numericalData.size() - 1));
                    currentColumn = dataObject.toString();
                }
                //
                else if (dataAttributeType.equals("c")) {
                    findUniqueDomainValues(dataObject, stringArrayList);
                    currentColumn = dataObject.toString();
                }
                stringBuilder.append(currentColumn);
            } else if ((dataObject.isNormalized())) {
                ArrayList<String> normalizedValuesArrayList = dataObject.getNormalizedValues();
                if (dataAttributeType.equals("n")) {
                    currentColumn = dataObject.toString();
                }
                if (dataAttributeType.equals("n")
                        && dataObject.getMinimumAttributeValue() == dataObject.getMaximumAttributeValue()) {
                    continue;
                }
                // Otherwise if the attribute type is categorical.
                else if (dataAttributeType.equals("c")) {
                    findUniqueDomainValues(dataObject, normalizedValuesArrayList);
                    if (dataObject.getUniqueDomainValues() > 1) {
                        currentColumn = dataObject.toString();
                    } else {
                        break;
                    }
                }
                // Check if the list is empty or not. If the list is empty then normalization method found
                // repeating categorical domainvalues or equivalent numerical min max domain values,
                // so the attribute should be removed from newname.txt.
                if (! stringArrayList.isEmpty()) {
                    // Append the current attribute column to the StringBuilder.
                    stringBuilder.append(currentColumn);
                }
            }
        }
        return stringBuilder.toString();
    }

    public static void findUniqueDomainValues(Data dataObject, ArrayList<String> stringArrayList) {
        // Categorical type requires that we identify the number of unique attribute types.
        // Repeating attributes have already been previously removed from the arrayList.
        // HashSets are unable to contain duplicates, so a quick way to find the number
        // of uniquely occurring domain values is to add all the string values to a new HashSet and then
        // get the size of that HashSet.
        // There is probably a more resource efficient way to do this, but it's the simplest way that I
        // could think of.
        // GarbageCollector should get rid of the HashSet after operation.
        // Create a new HashSet<String> from the ArrayList of Strings.
        HashSet<String> stringHashSet = new HashSet<>(stringArrayList);
        // Create a new StringBuilder
        StringBuilder stringBuilder = new StringBuilder();
        // Iterate the stringHashSet to append the domain values to the string.
        for (String s : stringHashSet) {
            stringBuilder.append(s).append(",");
        }
        // Size of the hashset is representative of the number of uniquely occuring domain values.
        dataObject.setUniqueDomainValues(stringHashSet.size());
        // Store the list of uniquely occurring domain values for the associated attribute.
        dataObject.setDomainValues(stringBuilder.toString());
    }

    public static boolean domainCheck(Data dataObject) {
        // Declare bool for duplicate data as false.
        boolean duplicate = false;
        // Get List<String> from current dataObject.
        List<String> stringArrayList = dataObject.getData();
        // Create new hashmap to record incidence of each string.
        Map<String, Integer> counts = new HashMap<>();
        // Check each string in the List<String> for current dataObject.
        for (String s : stringArrayList) {
            // Check if the string has been added to the hashmap before.
            if (counts.containsKey(s)) {
                counts.put(s, counts.get(s) + 1);
            } else {
                // Increment count for repeated string.
                counts.put(s, 1);
            }
        }
        // Iterate through the hashmap
        for (Map.Entry<String, Integer> entry : counts.entrySet()) {
            // If the count of a particular string is identical to the length of the List,
            // then the string has been repeated for every entry in the list.
            if (entry.getValue() == stringArrayList.size()) {
                // Set duplicate to true.
                duplicate = true;
                break;
            }
        }
        // Return true or false for duplicate occurrence.
        return duplicate;
    }
}
