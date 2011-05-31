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
package com.emftriple.jena.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.emftriple.util.ETripleOptions;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFReader;

public class FileUtil {
	static FileDataSource getModel(Map<?,?> options) {
		final String url = (String) options.get(ETripleOptions.OPTION_DATASOURCE_LOCATION);
		
		String fileFormat = null;
		if (options.containsKey(FileResourceImpl.OPTION_RDF_FORMAT)) {
			fileFormat = (String) options.get(FileResourceImpl.OPTION_RDF_FORMAT);
		}

		return new FileDataSource(url, fileFormat);
	}

	public static Model getModel(String fileLocation, String fileFormat) {
		final Model model = ModelFactory.createDefaultModel();
		final File file = new File(fileLocation);

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (fileFormat == null) {
			fileFormat = "RDF/XML-ABBREV";
			RDFReader reader = model.getReader();
			reader.setProperty("WARN_REDEFINITION_OF_ID","EM_IGNORE");
			try {
				reader.read(model, new FileInputStream(file), "");
			} catch (Exception e1) {
				reader = model.getReader("N3");
				reader.setProperty("WARN_REDEFINITION_OF_ID","EM_IGNORE");
				try {
					reader.read(model, new FileInputStream(file), "N3");
					fileFormat = "N3";
				} catch (Exception e2) {
					reader = model.getReader("N-TRIPLES");
					reader.setProperty("WARN_REDEFINITION_OF_ID","EM_IGNORE");
					try {
						reader.read(model, new FileInputStream(file), "N-TRIPLES");
						fileFormat = "N-TRIPLES";
					} catch (Exception e3) {

					}
				}
			}
		} else {
			InputStream stream = null;
			try {
				stream = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			if (fileFormat.contains("RDF/XML")) {
				model.getReader().read(model, stream, "");
			} else if (fileFormat == "N3") {
				model.getReader("N3").read(model, stream, "");
			} else if (fileFormat == "N-TRIPLES") {
				model.getReader("N-TRIPLES").read(model, stream, "");
			} else if (fileFormat == "TTL") {
				model.getReader("TTL").read(model, stream, "");
			}
		}
		
		return model;
	}
}
