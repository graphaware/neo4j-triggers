package com.graphaware.neo4j.triggers;

import com.graphaware.neo4j.triggers.config.TriggersConfiguration;
import com.graphaware.runtime.GraphAwareRuntime;
import com.graphaware.runtime.GraphAwareRuntimeFactory;
import org.junit.Test;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import static org.junit.Assert.assertTrue;

public class TriggersIntegrationTest extends IntegrationTest {


    @Test
    public void shouldWorkWithEmptyDefinition() {
        clearDb();
        initModuleWithConfig("integration/triggers-empty.json");
        createOneNode();
        try (Transaction tx = database.beginTx()) {
            Result result = database.execute("MATCH (n) RETURN n");
            assertTrue(result.hasNext());
            tx.success();
        }
    }

    @Test
    public void basicTriggerOnNodeCreationTest() {
        clearDb();
        initModuleWithConfig("integration/triggers-basic.json");
        createOneNode();
        try (Transaction tx = database.beginTx()) {
            Result result = database.execute("MATCH p=(n)-[r]->(x) RETURN p");
            assertTrue(result.hasNext());
            tx.success();
        }
    }

    @Test
    public void multiTriggersTest() {
        clearDb();
        initModuleWithConfig("integration/triggers-multi.json");
        createOneNode();
        try (Transaction tx = database.beginTx()) {
            Result result = database.execute("MATCH p=(n)-[r:KNOWS]->(x) RETURN p");
            assertTrue(result.hasNext());

            Result result2 = database.execute("MATCH p=(n)-[r:CONNECTS_TO]->(x) RETURN p");
            assertTrue(result2.hasNext());
            tx.success();
        }
    }

    private void createOneNode() {
        try (Transaction tx = database.beginTx()) {
            database.execute("CREATE (n:Node)");
            tx.success();
        }
    }

}
