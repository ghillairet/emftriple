package org.eclipselabs.emftriple.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;

@SuppressWarnings("all")
public abstract class RDFResource extends ResourceImpl {
  public RDFResource() {
  }
  
  public RDFResource(final URI uri) {
    super(uri);
  }
  
  protected void doLoad(final InputStream inputStream, final Map<? extends Object,? extends Object> options) throws IOException {
    super.doLoad(inputStream, options);
  }
  
  protected void doSave(final OutputStream outputStream, final Map<? extends Object,? extends Object> options) throws IOException {
    super.doSave(outputStream, options);
  }
}
