package com.graphaware.neo4j.triggers.definition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TriggersDefinition {

    @JsonProperty("nodes_created")
    private List<Definition> nodesCreated = new ArrayList<>();

    @JsonProperty("nodes_updated")
    private List<Definition> nodesUpdated = new ArrayList<>();

    @JsonProperty("nodes_deleted")
    private List<Definition> nodesDeleted = new ArrayList<>();

    public TriggersDefinition() {
    }

    public TriggersDefinition(List<Definition> nodesCreated, List<Definition> nodesUpdated, List<Definition> nodesDeleted) {
        this.nodesCreated = nodesCreated;
        this.nodesUpdated = nodesUpdated;
        this.nodesDeleted = nodesDeleted;
    }

    public List<Definition> getNodesCreated() {
        return nodesCreated;
    }

    public List<Definition> getNodesUpdated() {
        return nodesUpdated;
    }

    public List<Definition> getNodesDeleted() {
        return nodesDeleted;
    }
}
