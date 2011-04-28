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
package com.emftriple.transform.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.eclipse.emf.common.util.URI;

import com.emf4sw.rdf.Literal;
import com.emf4sw.rdf.Node;
import com.emf4sw.rdf.Resource;
import com.emf4sw.rdf.Triple;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public final class GetUtil {

	public static String getValue(Node node) {
		if (node instanceof Literal) {
			return ((Literal) node).getLexicalForm();
		}
		if (node instanceof Resource) {
			return ((Resource) node).getURI();
		}
		return null;
	}
	
	public static String getValue(List<Triple> values, String lang) {
		for (Triple node: values) {
			if (node.getObject() instanceof Literal) {
				if (((Literal) node.getObject()).getLang().equals(lang)) {
					return ((Literal) node.getObject()).getLexicalForm();
				}
			}
		}
		return getValue(values.get(0).getObject());
	}
	
	public static URI getURI(String key) {
		return URI.createURI(key);
	}
	
	public static URI getURI(Resource from) {
		checkNotNull(from);
		checkNotNull(from.getURI());

		return URI.createURI(from.getURI());
	}
	
	public static URI getURI(Node node) {
		checkArgument(node instanceof Resource);
			
		return getURI(((Resource)node));
	}
	
}
