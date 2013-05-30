package org.eclipselabs.emftriple.sesame.handlers;

import com.google.common.base.Objects;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;
import org.eclipselabs.emftriple.sesame.streams.RepositoryInputStream;
import org.eclipselabs.emftriple.sesame.streams.RepositoryOutputStream;
import org.openrdf.repository.Repository;

@SuppressWarnings("all")
public class RepositoryHandler extends URIHandlerImpl {
  private final Repository repository;
  
  public RepositoryHandler(final Repository repository) {
    this.repository = repository;
  }
  
  public boolean canHandle(final URI uri) {
    return true;
  }
  
  public OutputStream createOutputStream(final URI uri, final Map<? extends Object,? extends Object> options) throws IOException {
    RepositoryOutputStream _xblockexpression = null;
    {
      boolean _equals = Objects.equal(this.repository, null);
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
      RepositoryOutputStream _repositoryOutputStream = new RepositoryOutputStream(this.repository, uri, _xifexpression);
      _xblockexpression = (_repositoryOutputStream);
    }
    return _xblockexpression;
  }
  
  public InputStream createInputStream(final URI uri, final Map<? extends Object,? extends Object> options) throws IOException {
    RepositoryInputStream _xblockexpression = null;
    {
      boolean _equals = Objects.equal(this.repository, null);
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
      RepositoryInputStream _repositoryInputStream = new RepositoryInputStream(this.repository, uri, _xifexpression);
      _xblockexpression = (_repositoryInputStream);
    }
    return _xblockexpression;
  }
}
