package com.graphaware.neo4j.triggers.definition;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphaware.common.log.LoggerFactory;
import org.neo4j.logging.Log;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class DefinitionFileReader {

    private static final Log LOG = LoggerFactory.getLogger(DefinitionFileReader.class);
    private static final String NEO4j_HOME = "unsupported.dbms.directories.neo4j_home";
    private static final String NEO4j_CONF_DIR = "conf";

    public static TriggersDefinition loadTriggersDefinition(String filePath, Map<String, String> config) {
        try {
            ClassPathResource classPathResource = new ClassPathResource(filePath);
            String file = null;
            if(classPathResource.exists()){
                file = classPathResource.getFile().getAbsolutePath();
            } else if (filePath.contains(File.separator)){
                file = config.get(NEO4j_HOME) + File.separator + filePath;
            } else {
                file = config.get(NEO4j_HOME) + File.separator + NEO4j_CONF_DIR + File.separator + filePath;
            }
            LOG.info("Using triggers definition file at path " + file);
            System.out.println("Using triggers definition file at path " + file);

            return new ObjectMapper().readValue(new File(file), TriggersDefinition.class);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read json definition file", e);
        }
    }

}
