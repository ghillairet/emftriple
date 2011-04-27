/**
 * Copyright (c) 2009 L3i ( http://l3i.univ-larochelle.fr ).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html.
 */
package com.emftriple.sesame;

import java.util.Map;

import com.emftriple.datasources.IDataSource;
import com.emftriple.datasources.IDataSourceFactory;
import com.emftriple.sesame.http.SesameHTTP;
import com.emftriple.sesame.mem.SesameMem;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.6.0
 */
public class SesameDataSourceFactory implements IDataSourceFactory {
	
	public static final String SESAME_NATIVE_CLASS_NAME = SesameNative.class.getName();
	
	public static final String SESAME_MEM_CLASS_NAME = SesameMem.class.getName();
	
	public static final String SESAME_HTTP_CLASS_NAME = SesameHTTP.class.getName();

	SesameDataSourceFactory() {
		super();
	}

	@Override
	public boolean canCreate(Map<String, Object> options) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IDataSource create(Map<String, Object> options) {
		// TODO Auto-generated method stub
		return null;
	}
	
//	@Override
//	public boolean canCreate(DataSourceBuilder config) {
//		if (!config.getFactory().equals(SesameDataSourceFactory.class.getName())) {
//			return Boolean.FALSE;
//		}
//		if (config.getClass_().equals(SESAME_MEM_CLASS_NAME)) {
//			return canCreateSesameMem(config);
//		}
//		else if (config.getClass_().equals(SESAME_NATIVE_CLASS_NAME)) {
//			return canCreateSesameNative(config);
//		}
//		else if (config.getClass_().equals(SESAME_HTTP_CLASS_NAME)) {
//			return canCreateHttpRepository(config);
//		}
//		return Boolean.FALSE;
//	}
//
//	private boolean canCreateSesameNative(DataSourceBuilder config) {
//		return config.getName() != null && config.getUrl() != null;
//	}
//
//	private boolean canCreateSesameMem(DataSourceBuilder config) {
//		return config.getName() != null;
//	}
//
//	private boolean canCreateHttpRepository(DataSourceBuilder config) {
//		return config.getName() != null && config.getUrl() != null;
//	}
//
//	@Override
//	public IDataSource create(DataSourceBuilder config) {
//		IDataSource dataSource = null;
//		if (config.getClass_().equals(SESAME_MEM_CLASS_NAME)) {
//			dataSource = createSesameMem(config);
//		}
//		else if (config.getClass_().equals(SESAME_NATIVE_CLASS_NAME)) {
//			dataSource = createSesameNative(config);
//		}
//		else if (config.getClass_().equals(SESAME_HTTP_CLASS_NAME)) {
//			return createHttpRepository(config);
//		}
//		return dataSource;
//	}
//
//	private IDataSource createSesameMem(DataSourceBuilder config) {
//		final Repository repository;
//		if (config.getUrl() == null) {
//			repository = new SailRepository( new MemoryStore() );
//		} else {
//			File dataDir = new File(config.getUrl());
//			repository = new SailRepository( new MemoryStore(dataDir) );
//		}
//		try {
//			repository.initialize();
//		} catch (RepositoryException e1) {
//			e1.printStackTrace();
//		}
//		
//		return new SesameMem(config.getName(), repository);
//	}
//	
//	private IDataSource createSesameNative(DataSourceBuilder config) {
//		File dataDir = new File(config.getUrl());
//		Repository repository = new SailRepository(new NativeStore(dataDir));
//		try {
//			repository.initialize();
//		} catch (RepositoryException e1) {
//			e1.printStackTrace();
//		}
//	
//		return new SesameNative(config.getName(), repository);
//	}
//	
//	private IDataSource createHttpRepository(DataSourceBuilder config) {
//		final Repository repository = new HTTPRepository(config.getUrl());
//		try {
//			repository.initialize();
//		} catch (RepositoryException e1) {
//			e1.printStackTrace();
//		} 
//		
//		return new SesameHTTP(config.getUrl(), repository);
//	}
	
}
