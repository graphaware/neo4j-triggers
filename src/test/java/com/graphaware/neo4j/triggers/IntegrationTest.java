package com.graphaware.neo4j.triggers;

import com.graphaware.neo4j.triggers.config.TriggersConfiguration;
import com.graphaware.runtime.GraphAwareRuntime;
import com.graphaware.runtime.GraphAwareRuntimeFactory;
import org.junit.After;
import org.junit.Before;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

public abstract class IntegrationTest {

    protected GraphDatabaseService database;

    @Before
    public void setUp() {
        database = new TestGraphDatabaseFactory().newImpermanentDatabase();
    }

    @After
    public void shutdown() {
        database.shutdown();
    }

    protected void initModuleWithConfig(String config) {
        GraphAwareRuntime runtime = GraphAwareRuntimeFactory.createRuntime(database);
        runtime.registerModule(new TriggersModule("TR", database, TriggersConfiguration.defaultConfiguration().withFile(config)));
        runtime.start();
        runtime.waitUntilStarted();
    }

    protected void clearDb() {
        try (Transaction tx = database.beginTx()) {
            database.execute("MATCH (n) DETACH DELETE n");
            tx.success();
        }
    }
}
