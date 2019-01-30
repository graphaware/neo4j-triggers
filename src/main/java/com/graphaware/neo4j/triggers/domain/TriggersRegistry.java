package com.graphaware.neo4j.triggers.domain;

import com.graphaware.neo4j.triggers.definition.TriggersDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TriggersRegistry {

    private final List<NodeTrigger> onNodeCreated;

    private final List<NodeTrigger> onNodeUpdated;

    private final List<NodeTrigger> onNodeDeleted;

    private final List<RelationshipTrigger> onRelationshipCreated;

    private final List<RelationshipTrigger> onRelationshipUpdated;

    private final List<RelationshipTrigger> onRelationshipDeleted;

    public TriggersRegistry(List<NodeTrigger> onNodeCreated,
                            List<NodeTrigger> onNodeUpdated,
                            List<NodeTrigger> onNodeDeleted,
                            List<RelationshipTrigger> onRelationshipCreated,
                            List<RelationshipTrigger> onRelationshipUpdated,
                            List<RelationshipTrigger> onRelationshipDeleted
    ) {
        this.onNodeCreated = onNodeCreated;
        this.onNodeUpdated = onNodeUpdated;
        this.onNodeDeleted = onNodeDeleted;
        this.onRelationshipCreated = onRelationshipCreated;
        this.onRelationshipUpdated = onRelationshipUpdated;
        this.onRelationshipDeleted = onRelationshipDeleted;
    }

    public static TriggersRegistry fromDefinition(TriggersDefinition triggersDefinition) {
        return TriggersRegistry.defaultInstance()
                .triggersOnNodesCreated(triggersDefinition.getNodesCreated().stream().map(NodeTrigger::fromDefinition).collect(Collectors.toList()))
                .triggersOnNodesUpdated(triggersDefinition.getNodesUpdated().stream().map(NodeTrigger::fromDefinition).collect(Collectors.toList()))
                .triggersOnNodesDeleted(triggersDefinition.getNodesDeleted().stream().map(NodeTrigger::fromDefinition).collect(Collectors.toList()))
                .triggersOnRelationshipsCreated(triggersDefinition.getRelationshipsCreated().stream().map(RelationshipTrigger::fromDefinition).collect(Collectors.toList()))
                .triggersOnRelationshipsUpdated(triggersDefinition.getRelationshipsUpdated().stream().map(RelationshipTrigger::fromDefinition).collect(Collectors.toList()))
                .triggersOnRelationshipsDeleted(triggersDefinition.getRelationshipsDeleted().stream().map(RelationshipTrigger::fromDefinition).collect(Collectors.toList()))
                ;
    }

    public static TriggersRegistry defaultInstance() {
        return new TriggersRegistry(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public TriggersRegistry triggersOnNodesCreated(List<NodeTrigger> triggers) {
        return new TriggersRegistry(triggers, getOnNodeUpdated(), getOnNodeDeleted(), getOnRelationshipCreated(), getOnRelationshipUpdated(), getOnRelationshipDeleted());
    }

    public TriggersRegistry triggersOnNodesUpdated(List<NodeTrigger> triggers) {
        return new TriggersRegistry(getOnNodeCreated(), triggers, getOnNodeDeleted(), getOnRelationshipCreated(), getOnRelationshipUpdated(), getOnRelationshipDeleted());
    }

    public TriggersRegistry triggersOnNodesDeleted(List<NodeTrigger> triggers) {
        return new TriggersRegistry(getOnNodeCreated(), getOnNodeUpdated(), triggers, getOnRelationshipCreated(), getOnRelationshipUpdated(), getOnRelationshipDeleted());
    }

    public TriggersRegistry triggersOnRelationshipsCreated(List<RelationshipTrigger> triggers) {
        return new TriggersRegistry(getOnNodeCreated(), getOnNodeUpdated(), getOnNodeDeleted(), triggers, getOnRelationshipUpdated(), getOnRelationshipDeleted());
    }

    public TriggersRegistry triggersOnRelationshipsUpdated(List<RelationshipTrigger> triggers) {
        return new TriggersRegistry(getOnNodeCreated(), getOnNodeUpdated(), getOnNodeDeleted(), getOnRelationshipCreated(), triggers, getOnRelationshipDeleted());
    }

    public TriggersRegistry triggersOnRelationshipsDeleted(List<RelationshipTrigger> triggers) {
        return new TriggersRegistry(getOnNodeCreated(), getOnNodeUpdated(), getOnNodeDeleted(), getOnRelationshipCreated(), getOnRelationshipUpdated(), triggers);
    }

    public List<NodeTrigger> getOnNodeCreated() {
        return onNodeCreated;
    }

    public List<NodeTrigger> getOnNodeUpdated() {
        return onNodeUpdated;
    }

    public List<NodeTrigger> getOnNodeDeleted() {
        return onNodeDeleted;
    }

    public List<RelationshipTrigger> getOnRelationshipCreated() {
        return onRelationshipCreated;
    }

    public List<RelationshipTrigger> getOnRelationshipUpdated() {
        return onRelationshipUpdated;
    }

    public List<RelationshipTrigger> getOnRelationshipDeleted() {
        return onRelationshipDeleted;
    }
}
