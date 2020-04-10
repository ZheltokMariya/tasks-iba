import java.io.IOException;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        System.out.println("Json flattener with using libraries");
        JsonFlattenerWithLibraries jsonFlattenerWithLibraries = new JsonFlattenerWithLibraries();

        try {
            System.out.println(jsonFlattenerWithLibraries.deserializeJsonIntoFlatMap(JsonFlattenerWithLibraries.SOURCE_PATH));
            jsonFlattenerWithLibraries
                    .serializeMapIntoJson(JsonFlattenerWithLibraries.SOURCE_PATH, JsonFlattenerWithLibraries.DESTINATION_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Json flattener");
        String json = WorkWithFile.readJsonFromFile();
        Map<String, String> map = JsonFlattener.convertJsonIntoFlatMap(json);
        System.out.println(map.toString());
        WorkWithFile.writeIntoJsonFile(map);
    }
}
