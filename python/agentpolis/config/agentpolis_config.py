
import fconfig.configuration

from fconfig.config import Config


import roadmaptools.config.roadmaptools_config
from roadmaptools.config.roadmaptools_config import RoadmaptoolsConfig


class AgentpolisConfig(Config):

    def __init__(self, properties: dict = None):
        self.data_dir = properties.get("data_dir")
        self.map_dir = properties.get("map_dir")
        self.delete_temporary_files = properties.get("delete_temporary_files")
        self.skip_temporary_files = properties.get("skip_temporary_files")
        self.raw_filepath = properties.get("raw_filepath")
        self.simplified_filepath = properties.get("simplified_filepath")
        self.cleaned_filepath = properties.get("cleaned_filepath")
        self.sanitized_filepath = properties.get("sanitized_filepath")
        self.file_with_computed_parameters_filepath = properties.get("file_with_computed_parameters_filepath")
        self.nodes_filepath = properties.get("nodes_filepath")
        self.edges_filepath = properties.get("edges_filepath")
        self.adj_matrix_filepath = properties.get("adj_matrix_filepath")
        self.dm_filepath = properties.get("dm_filepath")
        self.station_locations_filepath = properties.get("station_locations_filepath")
        self.area_name = properties.get("area_name")
        self.roadmaptools = RoadmaptoolsConfig(properties.get("roadmaptools"))
        roadmaptools.config.roadmaptools_config.config = self.roadmaptools
        self.map_envelope = [array_member for array_member in properties.get("map_envelope")]
        pass


config: AgentpolisConfig = fconfig.configuration.load((RoadmaptoolsConfig, 'roadmaptools'), (AgentpolisConfig, None))
