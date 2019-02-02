package com.graphaware.neo4j.triggers;

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

    @Test
    public void testRelationshipCreateTrigger() {
        clearDb();
        initModuleWithConfig("integration/triggers-rels.json");
        try (Transaction tx = database.beginTx()) {
            database.execute("CREATE (p:Person {name:'Alessandro'})-[:FATHER_OF]->(:Person {name: 'Flavia'})");
            tx.success();
        }

        try (Transaction tx = database.beginTx()) {
            Result result2 = database.execute("MATCH p=(:Person {name:'Flavia'})-[:CHILD_OF]->(:Person {name:'Alessandro'}) RETURN p");
            assertTrue(result2.hasNext());
            tx.success();
        }
    }

    @Test
    public void testRelationshipUpdateTrigger() {
        clearDb();
        initModuleWithConfig("integration/triggers-rels.json");
        try (Transaction tx = database.beginTx()) {
            database.execute("CREATE (b:Book)-[:SIMILAR_TO {relevance: 0.2}]->(to:Book {name: 'Graph-Based Machine Learning'})");
            tx.success();
        }

        try (Transaction tx = database.beginTx()) {
            database.execute("MATCH (b:Book)-[r:SIMILAR_TO {relevance: 0.2}]->(to:Book {name: 'Graph-Based Machine Learning'}) SET r.relevance = 0.8");
            tx.success();
        }

        try (Transaction tx = database.beginTx()) {
            Result result2 = database.execute("MATCH p=(b:Book)-[r:SIMILAR_TO]->(to:Book {name: 'Graph-Based Machine Learning', totalInfluence:0.8 }) RETURN p");
            assertTrue(result2.hasNext());
            tx.success();
        }
    }

    @Test
    public void testLoadQueriesFromFile() {
        clearDb();
        initModuleWithConfig("integration/triggers-with-query-files.json", "integration/queries/");
        try (Transaction tx = database.beginTx()) {
            database.execute("CREATE (n:Node)");
            tx.success();
        }

        try (Transaction tx = database.beginTx()) {
            Result result = database.execute("MATCH (n:Node:AutoUpdated) RETURN n");
            assertTrue(result.hasNext());
            tx.success();
        }
    }

    @Test
    public void reloadTest() {
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
        try (Transaction tx = database.beginTx()) {
            database.execute("CALL ga.triggers.reload()");
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
