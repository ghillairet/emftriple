package org.eclipselabs.emftriple.sesame.streams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.URIConverter.Saveable;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipselabs.emftriple.sesame.map.EObjectMapper;
import org.openrdf.model.impl.GraphImpl;
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
      GraphImpl _graphImpl = new GraphImpl();
      final GraphImpl graph = _graphImpl;
      mapper.to(graph, resource, this.options, true);
      final RepositoryConnection connection = this.repository.getConnection();
      try {
        URIImpl _uRIImpl = new URIImpl(namedGraphURI);
        connection.add(graph, _uRIImpl);
      } finally {
        connection.close();
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
