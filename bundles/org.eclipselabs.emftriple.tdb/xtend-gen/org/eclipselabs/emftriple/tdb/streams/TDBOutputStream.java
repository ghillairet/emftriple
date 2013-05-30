package org.eclipselabs.emftriple.tdb.streams;

import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.ReadWrite;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.IOException;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipselabs.emftriple.jena.map.EObjectMapper;
import org.eclipselabs.emftriple.jena.streams.RDFOutputStream;

@SuppressWarnings("all")
public class TDBOutputStream extends RDFOutputStream {
  private Dataset dataset;
  
  public TDBOutputStream(final Dataset dataset, final URI uri, final Map<? extends Object,? extends Object> options) {
    super(uri, options);
    this.dataset = dataset;
  }
  
  public void saveResource(final Resource resource) throws IOException {
    final String namedGraphURI = this.uri.toString();
    this.dataset.begin(ReadWrite.WRITE);
    try {
      boolean _containsNamedModel = this.dataset.containsNamedModel(namedGraphURI);
      boolean _not = (!_containsNamedModel);
      if (_not) {
        Model _createDefaultModel = ModelFactory.createDefaultModel();
        this.dataset.addNamedModel(namedGraphURI, _createDefaultModel);
      }
      final Model model = this.dataset.getNamedModel(namedGraphURI);
      EObjectMapper _eObjectMapper = new EObjectMapper();
      final EObjectMapper mapper = _eObjectMapper;
      mapper.to(model, resource, this.options);
      this.dataset.commit();
    } finally {
      this.dataset.end();
    }
  }
}
