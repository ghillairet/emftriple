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

/**
 * The abstract class {@link AbstractDataSource} provides abstract implementation of {@link IDataSource}. This 
 * class needs to be extended to support specific RDF stores.
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.8.0
 * 
 * @param <G> abstract type for RDF Graph
 * @param <T> abstract type for RDF Triple
 * @param <N> abstract type for RDF Node
 * @param <U> abstract type for RDF URI
 * @param <L> abstract type for RDF Literal
 */
public abstract class AbstractDataSource<G, T>
	implements IDataSource<G, T> {
	
	private boolean isConnected = false;
	
	protected AbstractDataSource() {}
	
	protected void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}
	
	@Override
	public abstract void connect();
	
	@Override
	public abstract void disconnect();
	
	@Override
	public boolean isConnected() {
		return isConnected;
	};
	
}
