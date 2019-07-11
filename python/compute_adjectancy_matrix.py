
import roadmaptools.adjectancy

nodes_path = r'C:\AIC data\Shared\amod-data\VGA Evaluation\maps/nodes.geojson'
edges_path = r'C:\AIC data\Shared\amod-data\VGA Evaluation\maps/edges.geojson'
out_path = r'C:\AIC data\Shared\amod-data\VGA Evaluation\maps/adj.csv'

roadmaptools.adjectancy.create_adj_matrix(nodes_path, edges_path, out_path)