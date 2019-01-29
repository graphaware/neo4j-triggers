package com.graphaware.neo4j.triggers.executor;

import com.graphaware.common.log.LoggerFactory;
import com.graphaware.neo4j.triggers.domain.NodeTrigger;
import com.graphaware.neo4j.triggers.domain.TriggersRegistry;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.logging.Log;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class TriggersExecutor {

    private static final Log LOG = LoggerFactory.getLogger(TriggersExecutor.class);

    private final GraphDatabaseService database;
    private final TriggersRegistry triggersRegistry;

    public TriggersExecutor(GraphDatabaseService database, TriggersRegistry triggersRegistry) {
        this.database = database;
        this.triggersRegistry = triggersRegistry;
    }

    public void handleCreatedNodes(Collection<Node> nodes) {
        for (Node node : nodes) {
            for (NodeTrigger trigger : triggersRegistry.getOnNodeCreated()) {
                if (trigger.satisfies(node)) {
                    executeTriggerForNode(node, trigger);
                }
            }
        }
    }

    public void handleUpdatedNodes(Collection<Node> nodes) {
        for (Node node : nodes) {
            for (NodeTrigger trigger : triggersRegistry.getOnNodeUpdated()) {
                if (trigger.satisfies(node)) {
                    executeTriggerForNode(node, trigger);
                }
            }
        }
    }

    public void handleDeletedNodes(Collection<Node> nodes) {
        for (Node node : nodes) {
            for (NodeTrigger trigger : triggersRegistry.getOnNodeDeleted()) {
                if (trigger.satisfies(node)) {
                    executeTriggerForNode(node, trigger);
                }
            }
        }
    }

    private void executeTriggerForNode(Node node, NodeTrigger trigger) {
        try (Transaction tx = database.beginTx()) {
            Map<String, Object> parameters = Collections.singletonMap("id", node.getId());
            database.execute(trigger.getStatement(), parameters);
            tx.success();
        } catch (Exception e) {
            LOG.error("Error during trigger {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
