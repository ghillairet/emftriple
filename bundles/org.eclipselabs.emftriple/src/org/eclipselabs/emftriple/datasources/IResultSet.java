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
package org.eclipselabs.emftriple.datasources;

import java.util.Iterator;

import org.eclipselabs.emftriple.datasources.IResultSet.Solution;


/**
 * Wrapper class for ResultSet.
 * 
 * ResultSet corresponds to Select query result.
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public interface IResultSet extends Iterator<Solution> {
	
	public static interface Solution {
		
		<N> N get(String varName);
		
		boolean isResource(String varName);
		
		<U> U getResource(String varName);
		
		boolean isLiteral(String varName);
		
		<L> L getLiteral(String varName);
		
		Iterable<String> getSolutionNames();
		
	}
	
}
