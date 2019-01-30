GraphAware Neo4j Triggers
=========================

EXPERIMENTAL MODULE


GraphAware Triggers is a simple library that automatically executes defined Cypher statements during Transaction Events.

Getting the Software
--------------------

### Server Mode

When using Neo4j in the <a href="http://docs.neo4j.org/chunked/stable/server-installation.html" target="_blank">standalone server</a> mode,
you will need the <a href="https://github.com/graphaware/neo4j-framework" target="_blank">GraphAware Neo4j Framework</a> and GraphAware Neo4j Expire .jar files (both of which you can <a href="http://products.graphaware.com/" target="_blank">download here</a>) dropped
into the `plugins` directory of your Neo4j installation. After changing a few lines of config (read on) and restarting Neo4j, the module will do its magic.

Setup and Configuration
--------------------

### Server Mode

First, please make sure that the framework is configured by adding `dbms.thirdparty_jaxrs_classes=com.graphaware.server=/graphaware` to `conf/neo4j.conf`,
as described <a href="https://github.com/graphaware/neo4j-framework#server-mode" target="_blank">here</a>.

And add this configuration to register the Triggers module:

```
com.graphaware.runtime.enabled=true

# TR becomes the module ID (you will need to use this ID in other config below):
com.graphaware.module.TR.1=com.graphaware.neo4j.triggers.TriggersModuleBootstrapper
# Where the trigger file is located, full path or path relative to the /conf directory of Neo4j
com.graphaware.module.TR.file=triggers.json
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

The definitons file lets you define for which events, with which condition a trigger will occur, and the Cypher statement the trigger will
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

#### Note on Versioning Scheme

The version number has two parts. The first four numbers indicate compatibility with Neo4j GraphAware Framework.
 The last number is the version of the Triggers library. For example, version 3.5.1.53.1 is version 1 of the Triggers library
 compatible with GraphAware Neo4j Framework 3.5.1.53.

License
-------

Copyright (c) 2018 GraphAware

GraphAware is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program.
If not, see <http://www.gnu.org/licenses/>.
