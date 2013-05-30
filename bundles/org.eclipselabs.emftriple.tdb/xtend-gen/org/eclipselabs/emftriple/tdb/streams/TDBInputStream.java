package org.eclipselabs.emftriple.tdb.streams;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Model;
import java.io.IOException;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipselabs.emftriple.jena.map.EObjectMapper;
import org.eclipselabs.emftriple.jena.streams.RDFInputStream;

@SuppressWarnings("all")
public class TDBInputStream extends RDFInputStream {
  private final Dataset dataset;
  
  public TDBInputStream(final Dataset dataset, final URI uri, final Map<? extends Object,? extends Object> options) {
    super(uri, options);
    this.dataset = dataset;
  }
  
  public void loadResource(final Resource resource) throws IOException {
    final String namedGraphURI = this.uri.toString();
    this.dataset.begin(ReadWrite.READ);
    try {
      boolean _containsNamedModel = this.dataset.containsNamedModel(namedGraphURI);
      if (_containsNamedModel) {
        final Model model = this.dataset.getNamedModel(namedGraphURI);
        EObjectMapper _eObjectMapper = new EObjectMapper();
        final EObjectMapper mapper = _eObjectMapper;
        mapper.from(model, resource, this.options);
      }
    } finally {
      this.dataset.end();
    }
  }
}
