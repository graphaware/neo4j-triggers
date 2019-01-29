package com.graphaware.neo4j.triggers.definition;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Definition {

    private String condition;

    private String statement;

    public Definition() {
    }

    public String getCondition() {
        return condition;
    }

    public String getStatement() {
        return statement;
    }
}
