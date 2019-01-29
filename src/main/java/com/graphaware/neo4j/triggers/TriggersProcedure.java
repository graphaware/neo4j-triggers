package com.graphaware.neo4j.triggers;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.procedure.Context;
import org.neo4j.procedure.Procedure;

import java.util.stream.Stream;

import static com.graphaware.runtime.RuntimeRegistry.getStartedRuntime;

public class TriggersProcedure {

    @Context
    public GraphDatabaseService database;

    @Procedure(name = "ga.triggers.reload")
    public Stream<Result> reloadTriggersDefinition() {
        getModule().reloadTriggers();

        return Stream.of(successMessage());
    }

    private TriggersModule getModule() {
        return getStartedRuntime(database).getModule(TriggersModule.class);
    }

    private Result successMessage() {
        return new Result("SUCCESS");
    }

    public class Result {
        public String message;

        public Result(String message) {
            this.message = message;
        }
    }
}
