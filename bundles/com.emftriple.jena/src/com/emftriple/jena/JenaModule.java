/**
 * Copyright (c) 2009 L3i ( http://l3i.univ-larochelle.fr ).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package com.emftriple.jena;

import java.util.Properties;

import com.emf4sw.rdf.resource.TTLResource;
import com.emf4sw.rdf.resource.impl.TTLResourceImpl;
import com.emftriple.datasources.IDataSource;
import com.emftriple.datasources.IDataSourceFactory;
import com.emftriple.datasources.IDataSourceFactoryModule;
import com.google.inject.AbstractModule;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public class JenaModule extends AbstractModule implements IDataSourceFactoryModule {

	private Properties properties;
	private Class<? extends IDataSource> kind;

	public JenaModule() {}
	
	public JenaModule(Class<? extends IDataSource> kind, Properties properties) {
		this.kind = kind;
		this.properties = properties;
	}
	
	@Override
	protected void configure() {
		bind(IDataSourceFactory.class)
			.to(JenaDataSourceFactory.class);
		bind(TTLResource.class)
			.to(TTLResourceImpl.class);
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
