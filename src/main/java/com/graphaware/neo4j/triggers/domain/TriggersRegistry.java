package com.graphaware.neo4j.triggers.domain;

import com.graphaware.neo4j.triggers.definition.TriggersDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TriggersRegistry {

    private final List<NodeTrigger> onNodeCreated;

    private final List<NodeTrigger> onNodeUpdated;

    private final List<NodeTrigger> onNodeDeleted;

    public TriggersRegistry(List<NodeTrigger> onNodeCreated, List<NodeTrigger> onNodeUpdated, List<NodeTrigger> onNodeDeleted) {
        this.onNodeCreated = onNodeCreated;
        this.onNodeUpdated = onNodeUpdated;
        this.onNodeDeleted = onNodeDeleted;
    }

    public static TriggersRegistry fromDefinition(TriggersDefinition triggersDefinition) {
        return TriggersRegistry.defaultInstance()
                .triggersOnNodesCreated(triggersDefinition.getNodesCreated().stream().map(NodeTrigger::fromDefinition).collect(Collectors.toList()))
                .triggersOnNodesUpdated(triggersDefinition.getNodesUpdated().stream().map(NodeTrigger::fromDefinition).collect(Collectors.toList()))
                .triggersOnNodesDeleted(triggersDefinition.getNodesDeleted().stream().map(NodeTrigger::fromDefinition).collect(Collectors.toList()))
                ;
    }

    public static TriggersRegistry defaultInstance() {
        return new TriggersRegistry(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public TriggersRegistry triggersOnNodesCreated(List<NodeTrigger> triggers) {
        return new TriggersRegistry(triggers, getOnNodeUpdated(), getOnNodeDeleted());
    }

    public TriggersRegistry triggersOnNodesUpdated(List<NodeTrigger> triggers) {
        return new TriggersRegistry(getOnNodeCreated(), triggers, getOnNodeDeleted());
    }

    public TriggersRegistry triggersOnNodesDeleted(List<NodeTrigger> triggers) {
        return new TriggersRegistry(getOnNodeCreated(), getOnNodeUpdated(), triggers);
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
}
