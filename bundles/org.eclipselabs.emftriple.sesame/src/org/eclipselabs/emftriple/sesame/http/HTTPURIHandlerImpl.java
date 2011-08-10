/**
 * 
 */
package org.eclipselabs.emftriple.sesame.http;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipselabs.emftriple.ETripleOptions;
import org.eclipselabs.emftriple.ETripleURIHandlerImpl;
import org.eclipselabs.emftriple.StoreOptionsRegistry;
import org.eclipselabs.emftriple.datasources.IDataSource;
import org.openrdf.repository.Repository;
import org.openrdf.repository.http.HTTPRepository;

/**
 * @author ghillairet
 *
 */
public class HTTPURIHandlerImpl 
	extends ETripleURIHandlerImpl {
	
	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.impl.URIHandlerImpl#canHandle(org.eclipse.emf.common.util.URI)
	 */
	@Override
	public boolean canHandle(URI uri) {
		return "sail_http".equalsIgnoreCase(uri.scheme());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.ETripleURIHandlerImpl#getDataSource(org.eclipse.emf.common.util.URI)
	 */
	@Override
	protected IDataSource<?, ?> getDataSource(URI uri) {
		Map<String, Object> options = StoreOptionsRegistry.INSTANCE.get(getStoreName(uri));
		IDataSource<?, ?> dataSource = null;
		
		if (options != null) {
			if (options.containsKey(ETripleOptions.OPTION_DATASOURCE_LOCATION)) {
				
				String dir = (String) options.get(ETripleOptions.OPTION_DATASOURCE_LOCATION);
				
				dataSource = new SesameHTTP(new HTTPRepository(dir));
				
			} else if (options.containsKey(ETripleOptions.OPTION_DATASOURCE_OBJECT)) {
				
				Object obj = options.get(ETripleOptions.OPTION_DATASOURCE_OBJECT);
				
				if (obj instanceof Repository) {
					Repository repository = (Repository) obj;
					
					if (!mapOfDataSources.containsKey(repository)) {
						dataSource = new SesameHTTP(repository);
						mapOfDataSources.put(repository, dataSource);
					} else {
						dataSource = (SesameHTTP) mapOfDataSources.get(repository);
					}
				}
			}
		}
		
		return dataSource;
	}
	
}
