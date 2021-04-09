#
# Copyright (c) 2021 Czech Technical University in Prague.
#
# This file is part of Agentpolis project.
# (see https://github.com/aicenter/agentpolis).
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Lesser General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public License
# along with this program. If not, see <http://www.gnu.org/licenses/>.
#
import setuptools
from setuptools import setup

setup(
	name='agentpolis',
	version='0.2.0',
	description='service scripts for agentpolis simulation framework',
	author='David Fiedler',
	author_email='david.fido.fiedler@gmail.com',
	license='LGPL',
	packages=setuptools.find_packages(),
	url = 'https://github.com/aicenter/agentpolis',
	install_requires=['roadmaptools==4.1.0','fconfig', 'tqdm', 'typing'],
	python_requires='>=3.6',
	package_data={'agentpolis.resources': ['*.cfg']}
)
