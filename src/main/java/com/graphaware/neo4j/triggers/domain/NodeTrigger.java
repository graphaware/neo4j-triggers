package com.graphaware.neo4j.triggers.domain;

import com.graphaware.common.log.LoggerFactory;
import com.graphaware.common.representation.GraphDetachedNode;
import com.graphaware.neo4j.triggers.definition.Definition;
import org.neo4j.graphdb.Node;
import org.neo4j.logging.Log;

public class NodeTrigger extends AbstractTrigger {

    private static final Log LOG = LoggerFactory.getLogger(NodeTrigger.class);

    public NodeTrigger(String expression, String statement) {
        super(expression, statement);
    }

    public static NodeTrigger fromDefinition(Definition definition) {
        return new NodeTrigger(definition.getCondition(), definition.getStatement());
    }

    public boolean satisfies(Node node) {
        if (null == getExpression()) {
            return false;
        }

        GraphDetachedNode detachedNode = new GraphDetachedNode(node);
        try {
            return (Boolean) getExpression().getValue(detachedNode);
        } catch (Exception e) {
            LOG.error("Invalid condition expression {}", getExpressionString());
            return false;
        }
    }
}
