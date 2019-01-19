import java.io.*;
import java.lang.reflect.Type;
import java.util.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class Main {
    private static List<List<String>> listOfData = new ArrayList<>();
    private static byte[] dataInBytes;
    private static List<List<String>> resultList = new ArrayList<>();
    private static StringBuilder currentCombinationOfLines = new StringBuilder();
    private static int currentSumAndXorOfLines;

    private static void readData() {
        Gson gson = new Gson();
        try (Reader reader = new FileReader("src/input.json")) {
            Type listType = new TypeToken<List<List<String>>>(){}.getType();
            listOfData = gson.fromJson(reader, listType);

            Iterator iterator = listOfData.iterator();
            while (iterator.hasNext()) {
                List<String> temp = (List<String>)iterator.next();
                if (temp.contains(null)) {
                    if (Collections.frequency(temp, null) == temp.size()) {
                        iterator.remove();
                    }
                } else {
                    resultList.add(temp);
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void convertDataToBytes() {
        dataInBytes = new byte[listOfData.size()];
        for (int i = 0; i < listOfData.size(); i++) {
            for (int j = 0; j < listOfData.get(i).size(); j++) {
                if (listOfData.get(i).get(j) != null) {
                    dataInBytes[i] += Math.pow(2, listOfData.get(i).size() - 1 - j);
                }
            }
        }
    }

    private static void writeToResult() {
        List<String> sublist = new ArrayList<>();
        String[] strings = currentCombinationOfLines.toString().split(" ");
        for (int i = 0; i < listOfData.get(i).size(); i++) {
            for (String string : strings) {
                if (listOfData.get(Integer.parseInt(string)).get(i) != null) {
                    sublist.add(listOfData.get(Integer.parseInt(string)).get(i));
                }
            }
        }
        resultList.add(sublist);
    }

    private static void searchCombinations(byte[] input, int index) {
        if (currentCombinationOfLines.length() == 0) {
            currentCombinationOfLines.append(index);
        } else {
            currentCombinationOfLines.append(" ");
            currentCombinationOfLines.append(index);
        }
        if (currentSumAndXorOfLines == 0) {
            currentSumAndXorOfLines = input[index];
        }
        for (int i = index + 1; i < input.length; i++) {
            if (((currentSumAndXorOfLines) ^ ((int)input[i])) == ((currentSumAndXorOfLines) + ((int)input[i]))) {
                currentSumAndXorOfLines += input[i];
                if (currentSumAndXorOfLines == (Math.pow(2, listOfData.get(i).size()) - 1)) {
                    currentCombinationOfLines.append(" ");
                    currentCombinationOfLines.append(i);
                    writeToResult();
                } else {
                    searchCombinations(input, i);
                }
                currentCombinationOfLines.delete(currentCombinationOfLines.lastIndexOf(" "), currentCombinationOfLines.length());
                currentSumAndXorOfLines -= input[i];
            }
        }
    }
    private static void writeToFile() {
        Gson gson = new Gson();
        try (Writer writer = new FileWriter("src/output.json")) {
            gson.toJson(resultList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        readData();

        convertDataToBytes();

        for (int i = 0; i < dataInBytes.length - 1; i++) {
            currentCombinationOfLines.delete(0, currentCombinationOfLines.length());
            currentSumAndXorOfLines = 0;
            searchCombinations(dataInBytes, i);
        }

        writeToFile();

        System.out.println(resultList);
    }
}
