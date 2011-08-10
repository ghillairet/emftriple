/**
 * 
 */
package org.eclipselabs.emftriple.sesame.mem;

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
public class SesameMemoryURIHandlerImpl 
	extends ETripleURIHandlerImpl {

	/* (non-Javadoc)
	 * @see org.eclipse.emf.ecore.resource.impl.URIHandlerImpl#canHandle(org.eclipse.emf.common.util.URI)
	 */
	@Override
	public boolean canHandle(URI uri) {
		return "mem".equalsIgnoreCase(uri.scheme());
	}
	
	/* (non-Javadoc)
	 * @see org.eclipselabs.emftriple.ETripleURIHandlerImpl#getDataSource(org.eclipse.emf.common.util.URI)
	 */
	@Override
	protected IDataSource<?, ?> getDataSource(URI uri) {
		Map<String, Object> options = StoreOptionsRegistry.INSTANCE.get(getStoreName(uri));
		
		if (options != null) {
			if (options.containsKey(ETripleOptions.OPTION_DATASOURCE_LOCATION)) {
				String dir = (String) options.get(ETripleOptions.OPTION_DATASOURCE_LOCATION);
				File file = new File(dir);
				String indexes = "spoc,posc,cosp";
				return new SesameMemory(new SailRepository(new NativeStore(file, indexes)));
			} else if (options.containsKey(ETripleOptions.OPTION_DATASOURCE_OBJECT)) {
				Repository repository = (Repository) options.get(ETripleOptions.OPTION_DATASOURCE_LOCATION);
				return new SesameMemory(repository);
			} else {
				return new SesameMemory(new SailRepository(new NativeStore()));
			}
		}
		return null;
	}

	
}
