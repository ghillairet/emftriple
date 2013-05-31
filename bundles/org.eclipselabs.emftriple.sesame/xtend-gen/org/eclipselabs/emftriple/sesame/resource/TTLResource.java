package org.eclipselabs.emftriple.sesame.resource;

import java.io.OutputStream;
import org.eclipse.emf.common.util.URI;
import org.eclipselabs.emftriple.RDFFormat;
import org.eclipselabs.emftriple.sesame.io.RDFWriter;
import org.eclipselabs.emftriple.sesame.resource.RDFResource;
import org.openrdf.model.Model;

@SuppressWarnings("all")
public class TTLResource extends RDFResource {
  public TTLResource() {
    super();
  }
  
  public TTLResource(final URI uri) {
    super(uri);
  }
  
  protected void write(final OutputStream stream, final Model graph) {
    RDFWriter.write(stream, graph, RDFFormat.TURTLE);
  }
}
