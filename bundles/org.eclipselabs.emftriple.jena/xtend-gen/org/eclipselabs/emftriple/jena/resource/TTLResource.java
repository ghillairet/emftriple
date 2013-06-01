package org.eclipselabs.emftriple.jena.resource;

import com.hp.hpl.jena.rdf.model.Model;
import java.io.InputStream;
import java.io.OutputStream;
import org.eclipse.emf.common.util.URI;
import org.eclipselabs.emftriple.RDFFormat;
import org.eclipselabs.emftriple.jena.io.RDFReader;
import org.eclipselabs.emftriple.jena.io.XMLWriter;
import org.eclipselabs.emftriple.jena.resource.RDFResource;

@SuppressWarnings("all")
public class TTLResource extends RDFResource {
  public TTLResource() {
    super();
  }
  
  public TTLResource(final URI uri) {
    super(uri);
  }
  
  protected Model write(final OutputStream stream, final Model graph) {
    Model _write = XMLWriter.write(stream, graph, RDFFormat.TURTLE);
    return _write;
  }
  
  protected Model read(final InputStream stream) {
    Model _read = RDFReader.read(stream, RDFFormat.TURTLE);
    return _read;
  }
}
