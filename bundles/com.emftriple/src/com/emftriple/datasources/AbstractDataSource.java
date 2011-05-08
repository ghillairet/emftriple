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



/**
 * {@link AbstractDataSource}
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.5.5
 */
public abstract class AbstractDataSource<G, T, N, U, L> 
	implements IDataSource<G, T, N, U, L> {
	
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
