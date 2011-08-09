package org.eclipselabs.emftriple.jena.sdb;

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

import com.hp.hpl.jena.sdb.Store;

/**
 * 
 * @author ghillairet
 * @since 0.9.0
 */
public class SDBURIHandlerImpl 
	extends ETripleURIHandlerImpl {

	@Override
	public boolean canHandle(URI uri) {
		return "sdb".equalsIgnoreCase(uri.scheme());
	}
	
	@Override
	public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException {
		IDataSource<?, ?> dataSource = getDataSource(uri);
		
		if (dataSource != null) {
			return new JenaInputStream(uri, options, dataSource);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.impl.URIHandlerImpl#createOutputStream(org.eclipse.emf.common.util.URI, java.util.Map)
	 */
	@Override
	public OutputStream createOutputStream(URI uri, Map<?, ?> options) throws IOException {
		IDataSource<?, ?> dataSource = getDataSource(uri);
		
		if (dataSource != null) {
			return new JenaOutputStream(uri, options, dataSource);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.ETripleURIHandlerImpl#getDataSource(org.eclipse.emf.common.util.URI)
	 */
	@Override
	protected IDataSource<?, ?> getDataSource(URI uri) {
		Map<String, Object> options = StoreOptionsRegistry.INSTANCE.get(getStoreName(uri));
		
		if (options.containsKey(ETripleOptions.OPTION_DATASOURCE_OBJECT)) {
			Store store = (Store) options.get(ETripleOptions.OPTION_DATASOURCE_OBJECT);
			
			return new SDBDataSource(store);
		}
		
		return null;
	}
}
