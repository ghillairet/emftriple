package org.eclipselabs.emftriple.jena.tdb;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipselabs.emftriple.ETripleOptions;
import org.eclipselabs.emftriple.ETripleURIHandlerImpl;
import org.eclipselabs.emftriple.StoreOptionsRegistry;
import org.eclipselabs.emftriple.datasources.IDataSource;
import org.eclipselabs.emftriple.jena.JenaInputStream;
import org.eclipselabs.emftriple.jena.JenaOutputStream;


/**
 * Handles request to TDB URI.
 * 
 * examples URIs:
 *  - tdb://store_name?uri=http://www.example.org/persons/1
 *  - tdb://store_name?graph=http://ex.org/data/1
 *  - tdb://store_name?uri=http://www.example.org/persons/1&graph=http://ex.org/data/1
 *  
 * @author ghillairet
 * @since 0.9.0
 */
public class TDBURIHandlerImpl extends ETripleURIHandlerImpl {
	
	@Override
	public boolean canHandle(URI uri) {
		return "tdb".equalsIgnoreCase(uri.scheme());
	}
	
	@Override
	public OutputStream createOutputStream(URI uri, Map<?, ?> options) throws IOException {
		IDataSource<?,?> dataSource = getDataSource(uri);
		
		return new JenaOutputStream(uri, options, dataSource);
	}
	
	protected IDataSource<?, ?> getDataSource(URI uri) {
		IDataSource<?,?> dataSource = null;
		
		if (StoreOptionsRegistry.INSTANCE.containsKey(getStoreName(uri))) {
			Map<String, Object> options = StoreOptionsRegistry.INSTANCE.get(getStoreName(uri));
			
			if (options.containsKey(ETripleOptions.OPTION_DATASOURCE_LOCATION)) {
				String location = (String) options.get(ETripleOptions.OPTION_DATASOURCE_LOCATION);
				dataSource = new TDBDataSource(location);
			} else {
				dataSource = new TDBDataSource();
			}
		}
		
		return dataSource;
	}

	@Override
	public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException {
		IDataSource<?,?> dataSource = getDataSource(uri);
		
		return new JenaInputStream(uri, options, dataSource);
	}
}
