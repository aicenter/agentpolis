import agentpolis.init as ap
import time
import os
import roadmaptools.download_map
import roadmaptools.clean_geojson
import roadmaptools.simplify_graph
import roadmaptools.sanitize
import roadmaptools.inout
import roadmaptools.compute_edge_parameters
import roadmaptools.export_nodes_and_id_maker
import roadmaptools.prepare_geojson_to_agentpolisdemo

from roadmaptools.printer import print_info

config = None

def _compute_edge_parameters():
	print_info('Estimating travel speed (using max speed)... ', end='')
	start_time = time.time()

	input_file = roadmaptools.inout.load_geojson(config.sanitized_filepath)
	feature_collection_with_speeds = roadmaptools.estimate_speed_from_osm.get_geojson_with_speeds(input_file)
	# points = roadmaptools.export_nodes_and_id_maker.export_points_to_geojson(feature_collection_with_speeds)
	# feature_collection_with_speeds_and_ids \
	# 	= roadmaptools.export_nodes_and_id_maker.get_geojson_with_unique_ids(feature_collection_with_speeds)
	roadmaptools.inout.save_geojson(feature_collection_with_speeds, config.file_with_computed_parameters_filepath)

	print_info('done. (%.2f secs)' % (time.time() - start_time))


def _save_map_for_ap():
	print_info('Preparing files for agentpolis-demo... ', end='')
	start_time = time.time()

	geojson_file = roadmaptools.inout.load_geojson(config.file_with_computed_parameters_filepath)
	edges, nodes = roadmaptools.prepare_geojson_to_agentpolisdemo.get_nodes_and_edges_for_agentpolisdemo(geojson_file)
	roadmaptools.inout.save_geojson(nodes, config.nodes_filepath)
	roadmaptools.inout.save_geojson(edges, config.edges_filepath)

	print_info('done. (%.2f secs)' % (time.time() - start_time))


def _prepare_map():
	# create map dir
	os.makedirs(config.map_dir)

	# # 1 download the map
	roadmaptools.download_map.download_cities([tuple(config.map_envelope.values())], config.raw_filepath)

	# # 2 cleanup
	roadmaptools.clean_geojson.clean_geojson_files(config.raw_filepath, config.cleaned_filepath)

	# # 3 simplification
	roadmaptools.simplify_graph.simplify_geojson(config.cleaned_filepath, config.simplified_filepath)

	# # 4 reduction to single component
	roadmaptools.sanitize.sanitize(config.simplified_filepath, config.sanitized_filepath)

	# 5 compute edge parameters
	roadmaptools.compute_edge_parameters.compute_edge_parameters(
		config.sanitized_filepath, config.file_with_computed_parameters_filepath)

	# 6 finalization: split to node and edges, node id and index generation
	_save_map_for_ap()


if __name__ == '__main__':
	config = ap.config
	_prepare_map()
