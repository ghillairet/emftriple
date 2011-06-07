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

import java.util.Map;

import org.eclipse.emf.common.util.URI;

import com.emftriple.datasources.IDataSource;
import com.emftriple.jena.JenaResourceImpl;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Statement;

public class FileResourceImpl 
extends JenaResourceImpl {

	public static final String OPTION_RDF_FORMAT = "OPTION_RDF_FORMAT";

	public FileResourceImpl(URI uri) {
		super(uri);
	}

	@Override
	public IDataSource<Model, Statement, RDFNode, com.hp.hpl.jena.rdf.model.Resource, Literal> getDataSource(Map<?, ?> options) {
		return FileUtil.getModel(getURI().toString(), (Map<?, ?>) options);
	}

	@Override
	public IDataSource<Model, Statement, RDFNode, com.hp.hpl.jena.rdf.model.Resource, Literal> getDataSource() {
		return getDataSource(resourceSet.getLoadOptions());
	}
}
