/**
 * 
 */
package org.eclipselabs.emftriple.sesame.nat;

import java.io.File;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipselabs.emftriple.ETripleOptions;
import org.eclipselabs.emftriple.ETripleURIHandlerImpl;
import org.eclipselabs.emftriple.StoreOptionsRegistry;
import org.eclipselabs.emftriple.datasources.IDataSource;
import org.openrdf.repository.Repository;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.sail.nativerdf.NativeStore;

/**
 * @author ghillairet
 *
 */
public class SesameNativeURIHandlerImpl 
	extends ETripleURIHandlerImpl {

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.impl.URIHandlerImpl#canHandle(org.eclipse.emf.common.util.URI)
	 */
	@Override
	public boolean canHandle(URI uri) {
		return "native".equalsIgnoreCase(uri.scheme());
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
				File file = new File(dir);
				String indexes = "spoc,posc,cosp";
				
				dataSource = new SesameNative(new SailRepository(new NativeStore(file, indexes)));
			
			} else if (options.containsKey(ETripleOptions.OPTION_DATASOURCE_OBJECT)) {
				
				Object obj = options.get(ETripleOptions.OPTION_DATASOURCE_OBJECT);
				
				if (obj instanceof Repository) {
					Repository repository = (Repository) obj;
					
					if (!mapOfDataSources.containsKey(repository)) {
						dataSource = new SesameNative(repository);
						mapOfDataSources.put(repository, dataSource);
					} else {
						dataSource = (SesameNative) mapOfDataSources.get(repository);
					}
				}
			} else {
				
				dataSource = new SesameNative(new SailRepository(new NativeStore()));
				
			}
		}
		
		return dataSource;
	}

	
}
