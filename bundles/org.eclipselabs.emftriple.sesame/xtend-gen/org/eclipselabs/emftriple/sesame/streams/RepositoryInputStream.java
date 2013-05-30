package org.eclipselabs.emftriple.sesame.streams;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter.Loadable;
import org.openrdf.repository.Repository;

@SuppressWarnings("all")
public class RepositoryInputStream extends InputStream implements Loadable {
  protected final Repository repository;
  
  protected final URI uri;
  
  protected final Map<? extends Object,? extends Object> options;
  
  public RepositoryInputStream(final Repository repository, final URI uri, final Map<? extends Object,? extends Object> options) {
    this.uri = uri;
    this.options = options;
    this.repository = repository;
  }
  
  public int read() throws IOException {
    return 0;
  }
  
  public void loadResource(final Resource resource) throws IOException {
    UnsupportedOperationException _unsupportedOperationException = new UnsupportedOperationException("TODO: auto-generated method stub");
    throw _unsupportedOperationException;
  }
}
