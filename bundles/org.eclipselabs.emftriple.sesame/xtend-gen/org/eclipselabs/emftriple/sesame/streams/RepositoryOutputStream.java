package org.eclipselabs.emftriple.sesame.streams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter.Saveable;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipselabs.emftriple.sesame.map.EObjectMapper;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;

@SuppressWarnings("all")
public class RepositoryOutputStream extends ByteArrayOutputStream implements Saveable {
  protected final Repository repository;
  
  protected final URI uri;
  
  protected final Map<? extends Object,? extends Object> options;
  
  public RepositoryOutputStream(final Repository repository, final URI uri, final Map<? extends Object,? extends Object> options) {
    this.uri = uri;
    this.options = options;
    this.repository = repository;
  }
  
  public void saveResource(final Resource resource) throws IOException {
    try {
      final String namedGraphURI = this.uri.toString();
      EObjectMapper _eObjectMapper = new EObjectMapper();
      final EObjectMapper mapper = _eObjectMapper;
      LinkedHashModel _linkedHashModel = new LinkedHashModel();
      final LinkedHashModel graph = _linkedHashModel;
      mapper.to(graph, resource, this.options, false);
      final RepositoryConnection connection = this.repository.getConnection();
      connection.begin();
      try {
        URIImpl _uRIImpl = new URIImpl(namedGraphURI);
        connection.add(graph, _uRIImpl);
        connection.commit();
      } finally {
        connection.close();
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
