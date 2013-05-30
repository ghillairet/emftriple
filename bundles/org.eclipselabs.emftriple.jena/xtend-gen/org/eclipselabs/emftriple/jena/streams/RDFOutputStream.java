package org.eclipselabs.emftriple.jena.streams;

import com.google.common.base.Objects;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter.Saveable;
import org.eclipselabs.emftriple.jena.map.EObjectMapper;

@SuppressWarnings("all")
public class RDFOutputStream extends ByteArrayOutputStream implements Saveable {
  protected final URI uri;
  
  protected final Map<? extends Object,? extends Object> options;
  
  protected Model model;
  
  public RDFOutputStream(final URI uri, final Map<? extends Object,? extends Object> options) {
    this.uri = uri;
    this.options = options;
  }
  
  public void saveResource(final Resource resource) throws IOException {
    EObjectMapper _eObjectMapper = new EObjectMapper();
    final EObjectMapper mapper = _eObjectMapper;
    Model _createDefaultModel = ModelFactory.createDefaultModel();
    this.model = _createDefaultModel;
    mapper.to(
      this.model, resource, 
      this.options);
  }
  
  public void close() throws IOException {
    boolean _notEquals = (!Objects.equal(this.model, null));
    if (_notEquals) {
    }
  }
}
