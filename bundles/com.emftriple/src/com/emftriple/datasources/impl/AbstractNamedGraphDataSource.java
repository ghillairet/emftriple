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
package com.emftriple.datasources.impl;

import org.eclipse.emf.common.util.URI;

import com.emftriple.datasources.INamedGraphDataSource;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.5.5
 */
public abstract class AbstractNamedGraphDataSource extends AbstractDataSource implements INamedGraphDataSource {

	protected AbstractNamedGraphDataSource(String name) {
		super(name);
	}

	protected String graphQuery(URI graphURI) {
		return "CONSTRUCT { ?s ?p ?o } WHERE { GRAPH <" + graphURI.toString()+ "> { ?s ?p ?o} }";
	}
}
