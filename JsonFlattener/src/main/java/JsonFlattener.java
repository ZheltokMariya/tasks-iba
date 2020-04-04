import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.vault.support.JsonMapFlattener;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonFlattener {

    public static final String SOURCE_PATH  = "./src/main/resources/example.json";
    public static final String DESTINATION_PATH = "./src/main/resources/result.json";

    private ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> deserializeJsonIntoFlatMap(String path) throws IOException {
        File jsonFile = new File(path);
        if (jsonFile.exists() && jsonFile.isFile() && jsonFile.length() != 0){
            Map<String, Object> nestedMapOfJson = objectMapper
                    .readValue(new File(path),  new TypeReference<Map<String,Object>>(){});
            return JsonMapFlattener.flatten(nestedMapOfJson);
        }
        else {
            return new HashMap<String, Object>() { };
        }
    }

    public void serializeMapIntoJson(String sourcePath, String destinationPath) throws IOException{
        File jsonFile = new File(destinationPath);
        if (!jsonFile.exists()){
            jsonFile.createNewFile();
        }

        if (jsonFile.isFile()) {
            objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(new File(destinationPath), deserializeJsonIntoFlatMap(sourcePath));

        }
    }
}
