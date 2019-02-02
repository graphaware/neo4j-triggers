MATCH (n) WHERE id(n) = {id}
SET n:AutoUpdated, n.lastUpdated = timestamp()