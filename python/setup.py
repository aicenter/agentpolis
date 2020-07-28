import setuptools
from setuptools import setup

setup(
	name='agentpolis',
	version='0.1.1',
	description='service scripts for agentpolis simulation framework',
	author='David Fiedler',
	author_email='david.fido.fiedler@gmail.com',
	license='MIT',
	packages=setuptools.find_packages(),
	install_requires=['roadmaptools==4.0.0','fconfig', 'tqdm', 'typing'],
	python_requires='>=3.6'
)
