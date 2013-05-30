package org.eclipselabs.emftriple.jena.streams;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter.Loadable;

@SuppressWarnings("all")
public class RDFInputStream extends InputStream implements Loadable {
  protected final URI uri;
  
  protected final Map<? extends Object,? extends Object> options;
  
  public RDFInputStream(final URI uri, final Map<? extends Object,? extends Object> options) {
    this.options = options;
    this.uri = uri;
  }
  
  public int read() throws IOException {
    return 0;
  }
  
  public void loadResource(final Resource resource) throws IOException {
  }
}
