/*******************************************************************************
 * Copyright (c) 2011 Guillaume Hillairet.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Guillaume Hillairet - initial API and implementation
 *******************************************************************************/
package org.eclipselabs.emftriple.jena.file;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.eclipselabs.emftriple.ETripleOptions;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.util.FileManager;

public class FileUtil {
	public static final String OPTION_RDF_FORMAT = "OPTION_RDF_FORMAT";
	private static FileManager manager = FileManager.get();
	static {
		manager.setModelCaching(true);
	}

	static FileDataSource getModel(String resourceURI, Map<?,?> options) {
		String url = (String) options.get(ETripleOptions.OPTION_DATASOURCE_LOCATION);
		if (url == null){
			url = resourceURI;
		}
		
		String fileFormat = null;
		if (options.containsKey(OPTION_RDF_FORMAT)) {
			fileFormat = (String) options.get(OPTION_RDF_FORMAT);
		}

		return new FileDataSource(url, fileFormat);
	}

	public static Model getModel(String fileLocation, String fileFormat) {
		Model model = manager.getFromCache(fileLocation);
		if (model == null) {
			File file = new File(fileLocation);
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (!file.isFile()) {
				throw new IllegalArgumentException(fileLocation+" is not a file.");
			}
			model = manager.loadModel(fileLocation, fileFormat);
		}
		
		return model;
	}
}
