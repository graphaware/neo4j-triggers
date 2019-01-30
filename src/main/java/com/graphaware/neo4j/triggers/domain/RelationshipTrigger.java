package com.graphaware.neo4j.triggers.domain;

import com.graphaware.common.log.LoggerFactory;
import com.graphaware.common.representation.GraphDetachedRelationship;
import com.graphaware.neo4j.triggers.definition.Definition;
import org.neo4j.graphdb.Relationship;
import org.neo4j.logging.Log;

public class RelationshipTrigger extends AbstractTrigger {

    private static final Log LOG = LoggerFactory.getLogger(RelationshipTrigger.class);

    public RelationshipTrigger(String inclusionExpression, String statement) {
        super(inclusionExpression, statement);
    }

    public static RelationshipTrigger fromDefinition(Definition definition) {
        return new RelationshipTrigger(definition.getCondition(), definition.getStatement());
    }

    public boolean satisfies(Relationship relationship) {
        if (null == getExpression()) {
            return false;
        }

        GraphDetachedRelationship detachedRelationship = new GraphDetachedRelationship(relationship);
        try {
            return (Boolean) getExpression().getValue(detachedRelationship);
        } catch (Exception e) {
            LOG.error("Invalid condition expression {}", getExpressionString());
            return false;
        }
    }
}
