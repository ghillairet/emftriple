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
package com.emftriple.jena.util;

import java.util.Iterator;

import com.hp.hpl.jena.query.Dataset;

public class JenaUtil {
	public static Iterable<String> getNamedGraphs(final Dataset dataSet) {
		return new Iterable<String>() {
			@Override
			public Iterator<String> iterator() {
				return dataSet.listNames();
			}
		};
	}
}
