package com.graphaware.neo4j.triggers.domain;

import com.graphaware.neo4j.triggers.definition.Definition;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public abstract class AbstractTrigger {

    private final SpelExpressionParser expressionParser;
    private final String statement;
    private final String expressionString;
    private final Expression expression;

    public AbstractTrigger(String inclusionExpression, String statement) {
        this.expressionParser = new SpelExpressionParser();
        this.expressionString = inclusionExpression;
        if (null == inclusionExpression || "".equals(inclusionExpression)) {
            expression = null;
        } else {
            expression = expressionParser.parseExpression(inclusionExpression);
        }
        this.statement = statement;
    }

    protected SpelExpressionParser expressionParser() {
        return expressionParser;
    }

    public String getStatement() {
        return statement;
    }

    public Expression getExpression() {
        return expression;
    }

    public String getExpressionString() {
        return expressionString;
    }
}
