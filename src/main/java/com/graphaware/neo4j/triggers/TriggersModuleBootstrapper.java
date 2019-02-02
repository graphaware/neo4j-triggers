package com.graphaware.neo4j.triggers;

import com.graphaware.common.log.LoggerFactory;
import com.graphaware.neo4j.triggers.config.TriggersConfiguration;
import com.graphaware.runtime.module.BaseRuntimeModuleBootstrapper;
import com.graphaware.runtime.module.RuntimeModule;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.logging.Log;

import java.util.Map;

public class TriggersModuleBootstrapper extends BaseRuntimeModuleBootstrapper<TriggersConfiguration> {

    private static final Log LOG = LoggerFactory.getLogger(TriggersModuleBootstrapper.class);

    private static final String FILE = "file";
    private static final String QUERIES_DIR = "queries";

    @Override
    protected TriggersConfiguration defaultConfiguration() {
        return TriggersConfiguration.defaultConfiguration();
    }

    @Override
    protected RuntimeModule doBootstrapModule(String moduleId, Map<String, String> config, GraphDatabaseService database, TriggersConfiguration configuration) {
        if (configExists(config, FILE)) {
            String triggersFile = config.get(FILE);
            LOG.info("Triggers file set to %s", triggersFile);
            configuration = configuration.withFile(triggersFile);
        }

        if (configExists(config, QUERIES_DIR)) {
            String queriesDir = config.get(QUERIES_DIR);
            LOG.info("Queries path set to %s", queriesDir);
            configuration = configuration.withQueries(queriesDir);
        }

        return new TriggersModule(moduleId, database, configuration);
    }
}
