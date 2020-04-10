import java.io.*;
import java.util.Map;

public class WorkWithFile {
    public static final String NAME_OF_SOURCE_FILE = "./src/main/resources/example.json";
    public static final String NAME_OF_RESULT_FILE ="./src/main/resources/result.json";

    public static void writeIntoJsonFile(Map<String, String> map) {
        FileWriter fileWriter = null;
        StringBuilder jsonString = new StringBuilder();
        try{
            fileWriter = new FileWriter(NAME_OF_RESULT_FILE);
            jsonString.append("{\n");
            map.entrySet().stream().forEach(e -> jsonString
                    .append("\"")
                    .append(e.getKey())
                    .append("\" : \"")
                    .append(e.getValue())
                    .append("\",\n"));
            int index = jsonString.lastIndexOf(",\n");
            jsonString.replace(index, index+1, "\n}");
            fileWriter.append(jsonString.toString());
        }catch (IOException e){
            System.err.println("Write error " + e);
        }finally {
            if (fileWriter != null){
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    System.err.println("Stream close error " + e);
                }
            }
        }
    }

    public static String readJsonFromFile() {
        BufferedReader bufferedReader = null;
        String row;
        StringBuilder data = new StringBuilder();

        try {
            bufferedReader = new BufferedReader(new FileReader(NAME_OF_SOURCE_FILE));
            while ((row = bufferedReader.readLine()) != null) {
                data.append(row);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found " + e);
        }catch (IOException e){
            System.err.println("Read error " + e);
        }finally {
            if (bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    System.err.println("Stream close error " + e);
                }
            }
        }
        return data.toString();
    }
}
