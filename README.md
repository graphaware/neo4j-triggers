GraphAware Neo4j Triggers
=========================

EXPERIMENTAL MODULE

[![Build Status](https://travis-ci.org/graphaware/neo4j-triggers.png)](https://travis-ci.org/graphaware/neo4j-triggers) | <a href="http://graphaware.com/products/" target="_blank">Products</a> | <a href="http://products.graphaware.com" target="_blank">Downloads</a> | <a href="http://graphaware.com/site/triggers/latest/apidocs/" target="_blank">Javadoc</a> | Latest Release: 3.5.5.53.1


GraphAware Triggers is a simple library that automatically executes defined Cypher statements during Transaction Events.

## Setup

Download the following two `.jar` files and install them in the `plugins` directory of your Neo4j installation

* [GraphAware Framework](https://products.graphaware.com/?dir=framework-server-community)
* [Triggers Module](https://products.graphaware.com/?dir=triggers)

Make sure to download the versions that are compatible with your Neo4j installation, for example the Triggers Module in
version 3.5.4.53.1 is compatible with Neo4j 3.5.4 and the framework 3.5.4.53, the last bit specifies the version of the module
itself.

Next, add the following configuration in your `neo4j.conf` file to register the Triggers module:

```
com.graphaware.runtime.enabled=true

# TR becomes the module ID (you will need to use this ID in other config below):
com.graphaware.module.TR.1=com.graphaware.neo4j.triggers.TriggersModuleBootstrapper
# Where the trigger file is located, full path or path relative to the /conf directory of Neo4j
com.graphaware.module.TR.file=triggers.json

dbms.security.procedures.whitelist=ga.triggers.*
```

Using GraphAware Triggers
-------------------------

The triggers are defined in a simple JSON file, for example :

```json
{
  "nodes_created": [
    {
      "condition": "hasLabel('Document')",
      "statement": "MATCH (n) WHERE id(n) = $id SET n.createdAt = timestamp()"
    },
    {
      "condition": "hasLabel('NE_Company')",
      "statement": "MATCH (n) WHERE id(n) = $id MERGE (e:Entity:Company {value: n.value}) MERGE (n)-[:REFERS_TO]->(e)"
    }
  ],
  "relationships_created": [
    {
      "condition": "isType('FATHER_OF')",
      "statement": "MATCH (from)-[r]->(to) WHERE id(r) = $id MERGE (to)-[:CHILD_OF]->(from)"
    }
  ],
  "relationships_updated": [
    {
      "condition": "isType('SIMILAR_TO') && hasProperty('relevance')",
      "statement": "MATCH (from)-[r]->(to) WHERE id(r) = $id MATCH (to)<-[sim:SIMILAR_TO]-(conn) WITH to, sum(r.relevance) AS total SET to.totalInfluence = total"
    }
  ]
}
```

The definitions file lets you define for which events, with which condition a trigger will occur, and the Cypher statement the trigger will
execute.

Automatically, the graph object ID of the transaction element, a `node` or a `relationship`, is given as the `id` parameter to the statement.

The different events currently supported are

* `nodes_created`
* `nodes_updated`
* `nodes_deleted`
* `relationships_created`
* `relationships_updated`
* `relationships_deleted`

The Expressions for the condition follow the conditions available in the [GraphAware Common Inclusion Policies](https://github.com/graphaware/neo4j-framework/tree/master/common#inclusion-policies

### Reloading the definition

While the database is running, you can change your JSON file and reload the definiton with the following procedure :

```
CALL ga.triggers.reload()
```

### Using query files

Writing a long cypher query on a single line for complying with the JSON specification can be cumbersome. That's why we allow
users to write their queries in distinct `.cypher` or `.cyp` files and reference them in the definition.

For example, with the following file structure of neo4j :

```
$NEO4J_HOME
    - bin /
    - certificates /
    - conf /
        ...
        - neo4j.conf
        - triggers.json
        - queries /
            - add-type-as-label.cypher
```

And the content of `add-type-as-label.cypher` being :

```
CALL apoc.create.addLabels(id(n), [n.type])
YIELD node RETURN node
```

You can use the query written in this file in your `triggers.json` definition as follows :

```
{
  "nodes_created": [
    {
      "condition": "hasLabel('Node')",
      "namedQuery": "add-type-as-label"
    }
  ]
}
```

You will need to specify the directory holding the queries by adding the following line to your Neo4j configuration :

```
com.graphaware.module.TR.queries=queries/
```

#### Note on Versioning Scheme

The version number has two parts. The first four numbers indicate compatibility with Neo4j GraphAware Framework.
 The last number is the version of the Triggers library. For example, version 3.5.1.53.1 is version 1 of the Triggers library
 compatible with GraphAware Neo4j Framework 3.5.4.53.

License
-------

Copyright (c) 2013-2019 GraphAware

GraphAware is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program.
If not, see <http://www.gnu.org/licenses/>.
