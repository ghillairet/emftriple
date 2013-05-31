package org.eclipselabs.emftriple.sesame.streams;

import info.aduna.iteration.Iterations;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter.Loadable;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipselabs.emftriple.sesame.map.EObjectMapper;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;

@SuppressWarnings("all")
public class RepositoryInputStream extends InputStream implements Loadable {
  protected final Repository repository;
  
  protected final URI uri;
  
  protected final Map<? extends Object,? extends Object> options;
  
  public RepositoryInputStream(final Repository repository, final URI uri, final Map<? extends Object,? extends Object> options) {
    this.uri = uri;
    this.options = options;
    this.repository = repository;
  }
  
  public int read() throws IOException {
    return 0;
  }
  
  public void loadResource(final Resource resource) throws IOException {
    try {
      final String namedGraphURI = this.uri.toString();
      EObjectMapper _eObjectMapper = new EObjectMapper();
      final EObjectMapper mapper = _eObjectMapper;
      final RepositoryConnection connection = this.repository.getConnection();
      try {
        URIImpl _uRIImpl = new URIImpl(namedGraphURI);
        final RepositoryResult<Statement> stmts = connection.getStatements(null, null, null, true, _uRIImpl);
        LinkedHashModel _linkedHashModel = new LinkedHashModel();
        final LinkedHashModel graph = Iterations.<Statement, RepositoryException, LinkedHashModel>addAll(stmts, _linkedHashModel);
        mapper.from(graph, resource, this.options);
        stmts.close();
      } finally {
        connection.close();
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
