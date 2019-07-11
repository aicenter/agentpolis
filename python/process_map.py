import argparse
import os
import subprocess
import time
import codecs
import sys
import platform
import roadmaptools.clean_geojson
from roadmaptools import simplify_graph, estimate_speed_from_osm, calculate_curvature, \
    export_nodes_and_id_maker, prepare_geojson_to_agentpolisdemo, download_map, inout
from roadmaptools import osmtogeojson


def clean_geojson_files(input_filename, output_filename):
    print_info('Cleaning geoJSON... ', end='')
    start_time = time.time()
    roadmaptools.clean_geojson.clean_geojson_files(input_filename, output_filename)
    print_info('done. (%.2f secs)' % (time.time() - start_time))


def estimate_travel_speed(input_filename, output_filename):
    print_info('Estimating travel speed (using max speed)... ', end='')
    start_time = time.time()

    input_stream = codecs.open(input_filename, encoding='utf8')
    output_stream = open(output_filename, 'w')

    geojson_file = estimate_speed_from_osm.load_geojson(input_stream)
    geojson_out = estimate_speed_from_osm.get_geojson_with_speeds(geojson_file)
    estimate_speed_from_osm.save_geojson(geojson_out, output_stream)
    input_stream.close()
    output_stream.close()

    print_info('done. (%.2f secs)' % (time.time() - start_time))


def compute_edge_curvatures(input_filename, output_filename):
    print_info('Computing average edge curvatures... ', end='')
    start_time = time.time()

    input_stream = codecs.open(input_filename, encoding='utf8')
    output_stream = open(output_filename, 'w')

    geojson_file = calculate_curvature.load_geojson(input_stream)
    geojson_out = calculate_curvature.get_geojson_with_curvature(geojson_file)
    calculate_curvature.save_geojson(geojson_out, output_stream)
    input_stream.close()
    output_stream.close()

    print_info('done. (%.2f secs)' % (time.time() - start_time))


def generate_ids(input_filename, output_filename):
    print_info('Generating ids... ', end='')
    start_time = time.time()

    input_stream = codecs.open(input_filename, encoding='utf8')
    output_stream = open(output_filename, 'w')

    geojson_file = calculate_curvature.load_geojson(input_stream)
    points = export_nodes_and_id_maker.export_points_to_geojson(geojson_file)
    # if you want, you can save points here: export_nodes_and_id_maker.save_geojson(points,file_stream)
    geojson_out = export_nodes_and_id_maker.get_geojson_with_unique_ids(geojson_file)
    export_nodes_and_id_maker.save_geojson(geojson_out, output_stream, is_formated=False)
    input_stream.close()
    output_stream.close()

    print_info('done. (%.2f secs)' % (time.time() - start_time))


def generate_agentpolisdemo_files(input_filename, output_filename_edges, output_filename_nodes):
    print_info('Preparing files for agentpolis-demo... ', end='')
    start_time = time.time()

    geojson_file = roadmaptools.inout.load_geojson(input_filename)
    nodes, edges = prepare_geojson_to_agentpolisdemo.get_nodes_and_edges_for_agentpolisdemo(geojson_file)
    roadmaptools.inout.save_geojson(nodes, output_filename_edges)
    roadmaptools.inout.save_geojson(edges, output_filename_nodes)

    print_info('done. (%.2f secs)' % (time.time() - start_time))


def print_err(*args, **kwargs):
    pass
    print(*args, file=sys.stderr, **kwargs)


def print_info(*args, **kwargs):
    pass
    print(*args, **kwargs)


def get_all_params_osmfilter(config_file):
    loc_commands = ''
    for line in config_file:
        if line[0] != '#':
            loc_commands += line.replace('\n', ' ')
    return loc_commands


def get_args():
    parser = argparse.ArgumentParser()
#    parser.add_argument('MAP', type=str, action='store', help='input map in OSM format')
    parser.add_argument('OUTDIR', type=str, action='store', help='the directory with resulting geojson files')
    parser.add_argument('--version', action='version', version='%(prog)s 0.1')
    return parser.parse_args()


def convert_osm_to_geojson(input_file, output_file):
    print_info('Converting from OSM format to geoJSON... ', end='')
    start_time = time.time()

    geojson_file = osmtogeojson.convert_osmtogeojson(input_file)
    f = open(output_file, 'w')
    osmtogeojson.save_geojson(geojson_file, f)
    f.close()

    print_info('done. (%.2f secs)' % (time.time() - start_time))


def filter_osm_file(input_file, output_file):
    """ Downloads (and compiles) osmfilter tool from web and
    calls that osmfilter to only filter out only the road elements.
    """
    print_info('Filtering OSM file... ', end='')
    start_time = time.time()

    # determine if osmfilter is installed, otherwise download it
    my_platform = platform.system()  # get system info. Values: 'Linux', 'Windows'
    if my_platform == 'Linux':  # check if osmfilter is downloaded
        executable = 'osmfilter'
        command = './osmfilter'

        if not os.path.exists(executable):
            print_info('Downloading and compiling osmfilter... ')
            os.system('wget -O - http://m.m.i24.cc/osmfilter.c |cc -x c - -O3 -o osmfilter')

    elif my_platform == 'Windows':
        executable = 'osmfilter.exe'
        command = 'osmfilter.exe'

        if not os.path.exists(executable):
            print_info('Downloading and compiling osmfilter... ')
            try:
                subprocess.call(['wget', 'http://m.m.i24.cc/osmfilter.exe', '--no-check-certificate'])
            except OSError as e:
                if e.errno == os.errno.ENOENT:
                    print_err('wget not found! Please, install it.')  # handle file not found error
                else:
                    raise  # something else went wrong while trying to run `wget`

    else:
        print_err('OSM filtering not implemented for platform: %s. ' % my_platform)

    # run the binary
    if os.path.exists(executable):
        params = '--keep="highway=motorway =motorway_link =trunk =trunk_link =primary =primary_link =secondary' \
                 ' =secondary_link =tertiary =tertiary_link =unclassified =unclassified_link =residential =residential_link' \
                 ' =living_street" --drop="access=no"'

        filter_command = '%s %s %s > %s' % (command, input_file, params, output_file)
        os.system(filter_command)
    else:
        print_info('Osmfilter not available. Exiting.')
        exit(1)

    print_info('done. (%.2f secs)' % (time.time() - start_time))


if __name__ == '__main__':

    overall_start_time = time.time()
    print_info('Map preprocessing started.\n')

    # parse arguments
    args = get_args()

    # check if the input file exists
#    if not os.path.exists(args.MAP):
#       print_err('The input file %s does not exist!' % args.MAP)
#        exit(1)

    # check if the data folder exists, otherwise create the folder
    if not os.path.isdir(args.OUTDIR):
        os.makedirs(args.OUTDIR)
        print_info('Created directory %s.' % args.OUTDIR)

    # filter OSM elements, keep only roads
#    filter_osm_file(args.MAP, os.path.join(args.OUTDIR, 'output.osm'))

    # convert road network data from osm to geojson
#    convert_osm_to_geojson(os.path.join(args.OUTDIR, 'output.osm'), os.path.join(args.OUTDIR, 'output.geojson'))

    # download_map.download_cities([(49.94, 14.22, 50.17, 14.71)], os.path.join(args.OUTDIR, 'output.geojson'));
    #
    # # remove all unused features from map
    # clean_geojson_files(os.path.join(args.OUTDIR, 'output.geojson'), os.path.join(args.OUTDIR, 'output-cleaned.geojson'))

    # # simplify edges of graph
    # roadmaptools.simplify_graph.simplify_geojson(os.path.join(args.OUTDIR, 'output-cleaned.geojson'), os.path.join(args.OUTDIR, 'output-simplified.geojson'))
    #
    # # get speed from OSM data, if missing use heuristic
    # estimate_travel_speed(os.path.join(args.OUTDIR, 'output-simplified.geojson'), os.path.join(args.OUTDIR, 'output-speeds.geojson'))
    #
    # # add avarage value of curvature in single edge
    # compute_edge_curvatures(os.path.join(args.OUTDIR, 'output-speeds.geojson'), os.path.join(args.OUTDIR, 'output-curvatures.geojson'))

    # # extract all intersections of edges in file, validation of geojson file, (optional) 2.param=formated output file
    # generate_ids(os.path.join(args.OUTDIR, 'output-curvatures.geojson'), os.path.join(args.OUTDIR, 'output-result.geojson'))

    # extract the largest strongly connected component; create simple di-graph from multi-digraph
    generate_agentpolisdemo_files(os.path.join(args.OUTDIR, 'output-curvatures.geojson'), os.path.join(args.OUTDIR, 'edges.geojson'), os.path.join(args.OUTDIR, 'nodes.geojson'))

    # remove temporary files
    #output.osm
    for f in ['output.geojson', 'output-cleaned.geojson', 'output-simplified.geojson',
              'output-speeds.geojson', 'output-curvatures.geojson']:
        os.remove(os.path.join(args.OUTDIR, f))

    print_info('\nProcessing finished in %.2f secs.' % (time.time() - overall_start_time))
