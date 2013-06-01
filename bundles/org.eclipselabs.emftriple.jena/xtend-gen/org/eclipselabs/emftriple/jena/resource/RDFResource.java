package org.eclipselabs.emftriple.jena.resource;

import com.google.common.base.Objects;
import com.hp.hpl.jena.rdf.model.Model;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter.Loadable;
import org.eclipse.emf.ecore.resource.URIConverter.Saveable;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipselabs.emftriple.jena.io.RDFReader;
import org.eclipselabs.emftriple.jena.io.XMLWriter;
import org.eclipselabs.emftriple.jena.map.EObjectMapper;

@SuppressWarnings("all")
public class RDFResource extends ResourceImpl {
  public RDFResource() {
  }
  
  public RDFResource(final URI uri) {
    super(uri);
  }
  
  protected void doLoad(final InputStream inputStream, final Map<? extends Object,? extends Object> options) throws IOException {
    if ((inputStream instanceof Loadable)) {
      ((Loadable) inputStream).loadResource(this);
    } else {
      EObjectMapper _eObjectMapper = new EObjectMapper();
      final EObjectMapper mapper = _eObjectMapper;
      Model _read = this.read(inputStream);
      Map<? extends Object,? extends Object> _xifexpression = null;
      boolean _equals = Objects.equal(options, null);
      if (_equals) {
        Map<Object,Object> _emptyMap = Collections.<Object, Object>emptyMap();
        _xifexpression = _emptyMap;
      } else {
        _xifexpression = options;
      }
      mapper.from(_read, this, _xifexpression);
    }
  }
  
  protected void doSave(final OutputStream outputStream, final Map<? extends Object,? extends Object> options) throws IOException {
    if ((outputStream instanceof Saveable)) {
      ((Saveable) outputStream).saveResource(this);
    } else {
      EObjectMapper _eObjectMapper = new EObjectMapper();
      final EObjectMapper mapper = _eObjectMapper;
      Map<? extends Object,? extends Object> _xifexpression = null;
      boolean _equals = Objects.equal(options, null);
      if (_equals) {
        Map<Object,Object> _emptyMap = Collections.<Object, Object>emptyMap();
        _xifexpression = _emptyMap;
      } else {
        _xifexpression = options;
      }
      Model _to = mapper.to(this, _xifexpression);
      this.write(outputStream, _to);
    }
  }
  
  protected Model read(final InputStream stream) {
    Model _read = RDFReader.read(stream, null);
    return _read;
  }
  
  protected Model write(final OutputStream stream, final Model graph) {
    Model _write = XMLWriter.write(stream, graph, null);
    return _write;
  }
}
