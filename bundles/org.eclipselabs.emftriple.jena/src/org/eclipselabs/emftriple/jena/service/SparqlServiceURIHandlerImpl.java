package org.eclipselabs.emftriple.jena.service;

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
 * 
 * @author ghillairet
 * @since 0.9.0
 */
public class SparqlServiceURIHandlerImpl 
	extends ETripleURIHandlerImpl {

	@Override
	public boolean canHandle(URI uri) {
		return "sparql".equalsIgnoreCase(uri.scheme());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.ETripleURIHandlerImpl#getDataSource(org.eclipse.emf.common.util.URI)
	 */
	@Override
	protected IDataSource<?, ?> getDataSource(URI uri) {
		Map<String, Object> options = StoreOptionsRegistry.INSTANCE.get(getStoreName(uri));
		
		String url = (String) options.get(ETripleOptions.OPTION_DATASOURCE_LOCATION);
		
		if (url != null) {
			return new ServiceDataSource(url);
		}
		
		return null;
	}
	
	@Override
	public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException {
		IDataSource<?, ?> dataSource = getDataSource(uri);
		
		if (dataSource != null) {
			return new JenaInputStream(uri, options, dataSource);
		}
		return null;
	}
	
	@Override
	public OutputStream createOutputStream(URI uri, Map<?, ?> options) throws IOException {
		IDataSource<?, ?> dataSource = getDataSource(uri);
		
		if (dataSource != null) {
			return new JenaOutputStream(uri, options, dataSource);
		}
		return null;
	}
}
