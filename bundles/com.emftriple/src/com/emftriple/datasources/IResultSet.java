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
package com.emftriple.datasources;

import java.util.Iterator;

import com.emftriple.datasources.IResultSet.Solution;

/**
 * Wrapper class for ResultSet.
 * 
 * ResultSet corresponds to Select query result.
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public interface IResultSet<N, U, L> extends Iterator<Solution<N, U, L>> {

	public static interface Solution<N, U, L> {
		
		N get(String varName);
		
		boolean isResource(String varName);
		
		U getResource(String varName);
		
		boolean isLiteral(String varName);
		
		L getLiteral(String varName);
		
		Iterable<String> getSolutionNames();
		
	}
	
}
