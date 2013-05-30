package org.eclipselabs.emftriple.tdb.handlers;

import com.google.common.base.Objects;
import com.hp.hpl.jena.query.Dataset;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;
import org.eclipselabs.emftriple.tdb.streams.TDBInputStream;
import org.eclipselabs.emftriple.tdb.streams.TDBOutputStream;

@SuppressWarnings("all")
public class TDBHandler extends URIHandlerImpl {
  private final Dataset dataset;
  
  public TDBHandler(final Dataset dataset) {
    this.dataset = dataset;
  }
  
  public boolean canHandle(final URI uri) {
    return true;
  }
  
  public OutputStream createOutputStream(final URI uri, final Map<? extends Object,? extends Object> options) throws IOException {
    TDBOutputStream _xblockexpression = null;
    {
      boolean _equals = Objects.equal(this.dataset, null);
      if (_equals) {
        IOException _iOException = new IOException("Dataset must be defined");
        throw _iOException;
      }
      Map<? extends Object,? extends Object> _xifexpression = null;
      boolean _equals_1 = Objects.equal(options, null);
      if (_equals_1) {
        Map<Object,Object> _emptyMap = Collections.<Object, Object>emptyMap();
        _xifexpression = _emptyMap;
      } else {
        _xifexpression = options;
      }
      TDBOutputStream _tDBOutputStream = new TDBOutputStream(this.dataset, uri, _xifexpression);
      _xblockexpression = (_tDBOutputStream);
    }
    return _xblockexpression;
  }
  
  public InputStream createInputStream(final URI uri, final Map<? extends Object,? extends Object> options) throws IOException {
    TDBInputStream _xblockexpression = null;
    {
      boolean _equals = Objects.equal(this.dataset, null);
      if (_equals) {
        IOException _iOException = new IOException("Dataset must be defined");
        throw _iOException;
      }
      Map<? extends Object,? extends Object> _xifexpression = null;
      boolean _equals_1 = Objects.equal(options, null);
      if (_equals_1) {
        Map<Object,Object> _emptyMap = Collections.<Object, Object>emptyMap();
        _xifexpression = _emptyMap;
      } else {
        _xifexpression = options;
      }
      TDBInputStream _tDBInputStream = new TDBInputStream(this.dataset, uri, _xifexpression);
      _xblockexpression = (_tDBInputStream);
    }
    return _xblockexpression;
  }
}
