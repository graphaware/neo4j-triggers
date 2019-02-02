package com.graphaware.neo4j.triggers.definition;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphaware.common.log.LoggerFactory;
import org.apache.commons.io.FilenameUtils;
import org.neo4j.logging.Log;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefinitionFileReader {

    private static final Log LOG = LoggerFactory.getLogger(DefinitionFileReader.class);
    private static final String NEO4j_HOME = "unsupported.dbms.directories.neo4j_home";
    private static final String NEO4j_CONF_DIR = "conf";
    private static final List<String> CYPHER_EXTENSIONS = Arrays.asList("cypher", "cyp");

    public static TriggersDefinition loadTriggersDefinition(String filePath, Map<String, String> config) {
        try {
            ClassPathResource classPathResource = new ClassPathResource(filePath);
            String file;
            if(classPathResource.exists()){
                file = classPathResource.getFile().getAbsolutePath();
            } else if (filePath.contains(File.separator)){
                file = config.get(NEO4j_HOME) + File.separator + filePath;
            } else {
                file = config.get(NEO4j_HOME) + File.separator + NEO4j_CONF_DIR + File.separator + filePath;
            }
            LOG.info("Using triggers definition file at path " + file);

            return new ObjectMapper().readValue(new File(file), TriggersDefinition.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read json definition file", e);
        }
    }

    public static Map<String, String> loadQueries(String queriesDirPath, Map<String, String> config) {
        Map<String, String> queries = new HashMap<>();
        try {
            ClassPathResource classPathResource = new ClassPathResource(queriesDirPath);
            String file;
            if(classPathResource.exists()){
                file = classPathResource.getFile().getAbsolutePath();
            } else if (queriesDirPath.contains(File.separator)){
                file = config.get(NEO4j_HOME) + File.separator + queriesDirPath;
            } else {
                file = config.get(NEO4j_HOME) + File.separator + NEO4j_CONF_DIR + File.separator + queriesDirPath;
            }
            LOG.info("Reading query files from " + file);

            Files.list(Paths.get(file)).forEach(f -> {
                try {
                    if (CYPHER_EXTENSIONS.contains(FilenameUtils.getExtension(f.toString()))) {
                        queries.put(FilenameUtils.getBaseName(f.toString()), new String(Files.readAllBytes(f)));
                    }
                } catch (Exception e) {
                    LOG.error(e.getMessage());
                }
            });
        } catch (IOException e) {
            throw new RuntimeException("Unable to read queries directory", e);
        }

        return queries;
    }

}
