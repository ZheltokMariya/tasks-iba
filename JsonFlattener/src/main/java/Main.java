import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        JsonFlattener jsonFlattener = new JsonFlattener();

        try {
            System.out.println(jsonFlattener.deserializeJsonIntoFlatMap(JsonFlattener.SOURCE_PATH));
            jsonFlattener.serializeMapIntoJson(JsonFlattener.SOURCE_PATH, JsonFlattener.DESTINATION_PATH);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
