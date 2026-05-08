package api.utils;

import java.nio.file.Files;
import java.nio.file.Paths;

public class GraphQLQueryLoader {

    /**
     * Load GraphQL query/mutation from file
     * @param fileName nama file (tanpa .graphql), contoh: "get_user"
     * @return isi file sebagai String
     */
    public static String load(String fileName) throws Exception {
        // Asumsikan file disimpan di src/test/resources/graphql/
        String path = "src/test/resources/graphql/" + fileName + ".graphql";
        return new String(Files.readAllBytes(Paths.get(path)));
    }
}