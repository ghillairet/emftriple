package org.eclipselabs.emftriple.jena.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipselabs.emftriple.ETripleOptions;
import org.eclipselabs.emftriple.ETripleURIHandlerImpl;
import org.eclipselabs.emftriple.StoreOptionsRegistry;
import org.eclipselabs.emftriple.jena.JenaInputStream;
import org.eclipselabs.emftriple.jena.JenaOutputStream;

/**
 * 
 * @author ghillairet
 * @since 0.9.0
 */
public class FileURIHandlerImpl 
	extends ETripleURIHandlerImpl {

	@Override
	public boolean canHandle(URI uri) {
		return "rdf".equalsIgnoreCase(uri.scheme());
	}
	
	@Override
	public InputStream createInputStream(URI uri, Map<?, ?> options) throws IOException {
		return new JenaInputStream(uri, options, getDataSource(uri));
	}
	
	@Override
	public OutputStream createOutputStream(URI uri, Map<?, ?> options) throws IOException {
		return new JenaOutputStream(uri, options, getDataSource(uri));
	}
	
	protected FileDataSource getDataSource(URI uri) {
		Map<String, Object> opts = StoreOptionsRegistry.INSTANCE.get(getStoreName(uri));
		String location = (String) opts.get(ETripleOptions.OPTION_DATASOURCE_LOCATION);
		
		return FileUtil.getModel(location, (Map<?, ?>) opts);
	}
}
