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
package com.emftriple.sesame;

import java.util.Properties;

import com.emftriple.datasources.IDataSource;
import com.emftriple.datasources.IDataSourceFactory;
import com.emftriple.datasources.IDataSourceFactoryModule;
import com.google.inject.AbstractModule;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public class SesameModule extends AbstractModule implements IDataSourceFactoryModule {

	private Properties properties;
	private Class<? extends IDataSource> kind;
	
	public SesameModule() {}
	
	public SesameModule(Class<? extends IDataSource> kind, Properties properties) {
		this.kind = kind;
		this.properties = properties;
	}
	
	@Override
	protected void configure() {
		bind(IDataSourceFactory.class)
			.to(SesameDataSourceFactory.class);
	}

	@Override
	public Properties getProperties() {
		return properties;
	}

	@Override
	public Class<? extends IDataSource> getDataSourceClass() {
		return kind;
	}

}
