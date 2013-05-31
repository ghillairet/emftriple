package org.eclipselabs.emftriple.sesame.resource;

import com.google.common.base.Objects;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter.Loadable;
import org.eclipse.emf.ecore.resource.URIConverter.Saveable;
import org.eclipselabs.emftriple.resource.RDFResource;
import org.eclipselabs.emftriple.sesame.map.EObjectMapper;

@SuppressWarnings("all")
public class SesameResource extends RDFResource {
  public SesameResource() {
  }
  
  public SesameResource(final URI uri) {
    super(uri);
  }
  
  protected void doLoad(final InputStream inputStream, final Map<? extends Object,? extends Object> options) throws IOException {
    if ((inputStream instanceof Loadable)) {
      ((Loadable) inputStream).loadResource(this);
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
      mapper.from(inputStream, this, _xifexpression);
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
      mapper.write(outputStream, this, _xifexpression);
    }
  }
}
