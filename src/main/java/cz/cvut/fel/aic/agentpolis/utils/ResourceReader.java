/*
 * Copyright (c) 2021 Czech Technical University in Prague.
 *
 * This file is part of Agentpolis project.
 * (see https://github.com/aicenter/agentpolis).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package cz.cvut.fel.aic.agentpolis.utils;

import java.io.InputStream;
import java.net.URL;

/**
 * 
 * The util provides for read resource from JAR file
 * 
 * @author Zbynek Moler
 *
 */
public final class ResourceReader {

	private ResourceReader() {
	}

	public static final InputStream getResourceAsStream(String relativePath) {
		return ResourceReader.class.getResourceAsStream(relativePath);
	}

	
	/**
	 * Converts a package path to the corresponding file URL
	 * @param packagePath Absolute or relative package path. Note that an absolute package path has to start with a slash
	 * @return URL of the located source.
	 */
	public static final URL getPathToResource(String packagePath) {
		return ResourceReader.class.getResource(packagePath);
	}
	
	/**
	 * Converts a package path to the corresponding absolute file path. 
	 * @param packagePath Absolute or relative package path. Note that an absolute package path has to start with a slash.
	 * @return Absolute file path.
	 */
	public static String getAbsoultePathToResource(String packagePath){
		String path = getPathToResource(packagePath).getPath();
		return path.replaceFirst("^\\/(.:\\/)", "$1"); // this removes the leading slash on Windows
	}
}
