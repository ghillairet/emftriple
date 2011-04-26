package com.emftriple.datasources;

import java.util.Properties;

import com.google.inject.Module;

/**
 * 
 * @author <a href="mailto:g.hillairet at gmail.com">Guillaume Hillairet</a>
 * @since 0.7.0
 */
public interface IDataSourceFactoryModule extends Module {
	
	Properties getProperties();
	
	Class<? extends IDataSource> getDataSourceClass();
	
}
