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


#### Note on Versioning Scheme

The version number has two parts. The first four numbers indicate compatibility with Neo4j GraphAware Framework.
 The last number is the version of the Expire library. For example, version 2.3.3.37.1 is version 1 of the Expire library
 compatible with GraphAware Neo4j Framework 2.3.3.37.

Setup and Configuration
--------------------

### Server Mode

First, please make sure that the framework is configured by adding `dbms.thirdparty_jaxrs_classes=com.graphaware.server=/graphaware` to `conf/neo4j.conf`,
as described <a href="https://github.com/graphaware/neo4j-framework#server-mode" target="_blank">here</a>.

And add this configuration to register the Triggers module:

```
com.graphaware.runtime.enabled=true

# TR becomes the module ID (you will need to use this ID in other config below):
com.graphaware.module.TR.1=com.graphaware.neo4j.expire.TriggersModuleBootstrapper
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
      "condition": "hasLabel('Node')",
      "statement": "MATCH (n) WHERE id(n) = $id CREATE (other:Node) MERGE (other)-[:CONNECTS_TO]->(n)"
    },
    {
      "condition": "hasLabel('Node')",
      "statement": "MATCH (n) WHERE id(n) = $id CREATE (p:Person) MERGE (p)-[:KNOWS]->(n)"
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

The Expressions for the condition follow the conditions available in the [GraphAware Common Inclusion Policies](https://github.com/graphaware/neo4j-framework/tree/master/common#inclusion-policies

License
-------

Copyright (c) 2018 GraphAware

GraphAware is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License
as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
You should have received a copy of the GNU General Public License along with this program.
If not, see <http://www.gnu.org/licenses/>.
