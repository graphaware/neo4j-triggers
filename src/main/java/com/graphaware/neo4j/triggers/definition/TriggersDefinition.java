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

    @JsonProperty("relationships_created")
    private List<Definition> relationshipsCreated = new ArrayList<>();

    @JsonProperty("relationships_updated")
    private List<Definition> relationshipsUpdated = new ArrayList<>();

    @JsonProperty("relationships_deleted")
    private List<Definition> relationshipsDeleted = new ArrayList<>();

    public TriggersDefinition() {
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

    public List<Definition> getRelationshipsCreated() {
        return relationshipsCreated;
    }

    public List<Definition> getRelationshipsUpdated() {
        return relationshipsUpdated;
    }

    public List<Definition> getRelationshipsDeleted() {
        return relationshipsDeleted;
    }
}
