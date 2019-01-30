package com.graphaware.neo4j.triggers.executor;

import com.graphaware.common.log.LoggerFactory;
import com.graphaware.common.util.Change;
import com.graphaware.neo4j.triggers.domain.AbstractTrigger;
import com.graphaware.neo4j.triggers.domain.NodeTrigger;
import com.graphaware.neo4j.triggers.domain.RelationshipTrigger;
import com.graphaware.neo4j.triggers.domain.TriggersRegistry;
import com.graphaware.tx.event.improved.api.ImprovedTransactionData;
import org.neo4j.graphdb.*;
import org.neo4j.logging.Log;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

public class TriggersExecutor {

    private static final Log LOG = LoggerFactory.getLogger(TriggersExecutor.class);

    private final GraphDatabaseService database;
    private final TriggersRegistry triggersRegistry;

    public TriggersExecutor(GraphDatabaseService database, TriggersRegistry triggersRegistry) {
        this.database = database;
        this.triggersRegistry = triggersRegistry;
    }

    public void handleTransaction(ImprovedTransactionData transactionData) {
        handleCreatedNodes(transactionData.getAllCreatedNodes());
        handleUpdatedNodes(transactionData.getAllChangedNodes().stream().map(Change::getCurrent).collect(Collectors.toList()));
        handleDeletedNodes(transactionData.getAllDeletedNodes());
        handleCreatedRelationships(transactionData.getAllCreatedRelationships());
        handleUpdatedRelationships(transactionData.getAllChangedRelationships().stream().map(Change::getCurrent).collect(Collectors.toList()));
        handleDeletedRelationships(transactionData.getAllDeletedRelationships());
    }

    public void handleCreatedNodes(Collection<Node> nodes) {
        for (Node node : nodes) {
            for (NodeTrigger trigger : triggersRegistry.getOnNodeCreated()) {
                if (trigger.satisfies(node)) {
                    executeTrigger(node, trigger);
                }
            }
        }
    }

    public void handleUpdatedNodes(Collection<Node> nodes) {
        for (Node node : nodes) {
            for (NodeTrigger trigger : triggersRegistry.getOnNodeUpdated()) {
                if (trigger.satisfies(node)) {
                    executeTrigger(node, trigger);
                }
            }
        }
    }

    public void handleDeletedNodes(Collection<Node> nodes) {
        for (Node node : nodes) {
            for (NodeTrigger trigger : triggersRegistry.getOnNodeDeleted()) {
                if (trigger.satisfies(node)) {
                    executeTrigger(node, trigger);
                }
            }
        }
    }

    public void handleCreatedRelationships(Collection<Relationship> relationships) {
        for (Relationship relationship : relationships) {
            for (RelationshipTrigger trigger : triggersRegistry.getOnRelationshipCreated()) {
                if (trigger.satisfies(relationship)) {
                    executeTrigger(relationship, trigger);
                }
            }
        }
    }

    public void handleUpdatedRelationships(Collection<Relationship> relationships) {
        for (Relationship relationship : relationships) {
            for (RelationshipTrigger trigger : triggersRegistry.getOnRelationshipUpdated()) {
                if (trigger.satisfies(relationship)) {
                    executeTrigger(relationship, trigger);
                }
            }
        }
    }

    public void handleDeletedRelationships(Collection<Relationship> relationships) {
        for (Relationship relationship : relationships) {
            for (RelationshipTrigger trigger : triggersRegistry.getOnRelationshipDeleted()) {
                if (trigger.satisfies(relationship)) {
                    executeTrigger(relationship, trigger);
                }
            }
        }
    }

    private void executeTrigger(Entity entity, AbstractTrigger trigger) {
        try (Transaction tx = database.beginTx()) {
            Map<String, Object> parameters = Collections.singletonMap("id", entity.getId());
            database.execute(trigger.getStatement(), parameters);
            tx.success();
        } catch (Exception e) {
            LOG.error("Error during trigger {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
