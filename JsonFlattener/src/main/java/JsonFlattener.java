import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonFlattener {

    public static Map<String, String> convertJsonIntoFlatMap(String json){
        if (json == null){
            return new LinkedHashMap<>();
        }
        String jsonString = json.replace(" ", "").replace("\n", "");

        Map<String, String> map = new LinkedHashMap<>();
        LinkedList<String> keyList = new LinkedList<>();

        String separators = "(\\[|\\]|\\{|\\}|,|:|\")";
        Pattern keyPattern = Pattern.compile("(,?\\{?\"[^\"]*\":\\{?\\[?)");
        Pattern valuePattern = Pattern.compile("(:\"[^\"]*\",?}?)");
        Matcher keyMatcher = keyPattern.matcher(jsonString);
        Matcher valueMatcher = valuePattern.matcher(jsonString);

        while (keyMatcher.find()){
            keyList.add(keyMatcher.group().replaceAll("([,{\":])", ""));

            if (keyMatcher.group().contains("[")){
                if (json.substring(keyMatcher.end(), keyMatcher.end()+1).equals("]")) {
                    keyList.removeLast();
                    continue;
                }
                String substringWithArray = jsonString.substring(keyMatcher.start());
                String substringWithOnlyArrayElements = jsonString.substring(keyMatcher.end());
                int arrayIndex = 0;
                Pattern arrayNestedElementPattern = Pattern.compile("(:\"[^\"]*\",?}*,?\\{?]?}?)");
                Pattern arrayElementPattern = Pattern.compile("(\"[^\"]*\",?]?}?)");
                Matcher arrayNestedElementMatcher = arrayNestedElementPattern.matcher(substringWithArray);
                Matcher arrayElementMatcher = arrayElementPattern.matcher(substringWithOnlyArrayElements);
                String keyForArrayIndex = keyMatcher.group().replaceAll(separators, "");

                keyList.removeLast();
                keyList.add(keyForArrayIndex + "[" + arrayIndex + "]");

                if (substringWithOnlyArrayElements.charAt(0) != '{'){
                    while (true){
                        if (arrayElementMatcher.find()) {
                            map.put(getKeyForFlatMap(keyList), arrayElementMatcher.group().replaceAll(separators, ""));
                            keyList.removeLast();
                            if (arrayElementMatcher.group().substring(arrayElementMatcher.group().length()-1).equals("}")){
                                keyList.removeLast();
                            }
                            if (arrayElementMatcher.group().contains("]")){
                                break;

                            }
                            arrayIndex++;
                            keyList.add(keyForArrayIndex + "[" + arrayIndex + "]");
                        }
                    }
                }
                else {
                    while (true) {
                        if (keyMatcher.find()) {
                            keyList.add(keyMatcher.group().replaceAll(separators, ""));
                        }

                        if (!keyMatcher.group().substring(keyMatcher.group().length() - 1).equals("{")) {
                            if (arrayNestedElementMatcher.find()) {
                                map.put(getKeyForFlatMap(keyList), arrayNestedElementMatcher.group().replaceAll(separators, ""));
                                keyList.removeLast();
                            }
                            if (arrayNestedElementMatcher.group().contains("}")) {
                                removeFreeKeysFromList(arrayElementMatcher, keyList);
                                if (arrayNestedElementMatcher.group().contains("]")) {
                                    break;
                                }
                                if (arrayNestedElementMatcher.group().contains("},{")) {
                                    arrayIndex++;
                                    keyList.add(keyForArrayIndex + "[" + arrayIndex + "]");
                                }
                            }
                        }
                        else{
                            Pattern emptyBracketsPattern = Pattern.compile("(}*,?\\{?]?}?)");
                            Matcher emptyBracketsMatcher = emptyBracketsPattern.matcher(json.substring(keyMatcher.end()));
                            if (emptyBracketsMatcher.find()){
                                removeFreeKeysFromList(emptyBracketsMatcher, keyList);
                                if (emptyBracketsMatcher.group().contains("]")) {
                                    break;
                                }
                                if (emptyBracketsMatcher.group().contains("},{")) {
                                    arrayIndex++;
                                    keyList.add(keyForArrayIndex + "[" + arrayIndex + "]");
                                }
                            }
                        }
                    }
                }

            }
            else {
                if (!keyMatcher.group().substring(keyMatcher.group().length() - 1).equals("{")){
                    if (valueMatcher.find()) {
                        map.put(getKeyForFlatMap(keyList), valueMatcher.group().replaceAll(separators, ""));
                        keyList.removeLast();
                        if (valueMatcher.group().substring(valueMatcher.group().length() - 1).equals("}")) {
                            keyList.removeLast();
                        }
                    }
                }
                else {
                    if (jsonString.substring(keyMatcher.end(), keyMatcher.end()+1).equals("}")){
                        keyList.removeLast();
                    }
                }
            }
        }

        return map;
    }

    private static String getKeyForFlatMap(LinkedList<String> keyList){
        StringBuilder key = new StringBuilder();
        for (String s : keyList) {
            key.append(s + ".");
        }
        int index = key.lastIndexOf(".");
        key.deleteCharAt(index);
        return key.toString();
    }

    private static void removeFreeKeysFromList(Matcher matcher, LinkedList<String> keyList){
        int numberOfFreeKeys = (int) matcher.group().codePoints().filter(ch -> ch == '}').count();
        for (int i = 0; i < numberOfFreeKeys; i++) {
            keyList.removeLast();
        }
    }
}
