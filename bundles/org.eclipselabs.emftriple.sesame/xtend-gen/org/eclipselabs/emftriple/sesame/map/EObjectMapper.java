package org.eclipselabs.emftriple.sesame.map;

import com.google.common.base.Objects;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.InputOutput;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipselabs.emftriple.resource.RDFResource;
import org.eclipselabs.emftriple.sesame.map.Deserializer;
import org.eclipselabs.emftriple.sesame.map.NamedGraphSerializer;
import org.eclipselabs.emftriple.sesame.map.Serializer;
import org.openrdf.model.Graph;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.GraphImpl;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.RDFWriter;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.StatementCollector;

@SuppressWarnings("all")
public class EObjectMapper {
  public void write(final OutputStream stream, final RDFResource resource, final Map<? extends Object,? extends Object> options) {
    Graph _to = this.to(resource, options);
    this.write(stream, _to);
  }
  
  public void write(final OutputStream stream, final Graph graph) {
    final RDFWriter writer = Rio.createWriter(RDFFormat.RDFXML, stream);
    try {
      writer.startRDF();
      final Procedure1<Statement> _function = new Procedure1<Statement>() {
          public void apply(final Statement it) {
            try {
              writer.handleStatement(it);
            } catch (Throwable _e) {
              throw Exceptions.sneakyThrow(_e);
            }
          }
        };
      IterableExtensions.<Statement>forEach(graph, _function);
      writer.endRDF();
    } catch (final Throwable _t) {
      if (_t instanceof RDFHandlerException) {
        final RDFHandlerException e = (RDFHandlerException)_t;
        e.printStackTrace();
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
  }
  
  public Graph to(final Resource resource, final Map<? extends Object,? extends Object> options) {
    ValueFactoryImpl _instance = ValueFactoryImpl.getInstance();
    ArrayList<Statement> _newArrayList = CollectionLiterals.<Statement>newArrayList();
    GraphImpl _graphImpl = new GraphImpl(_instance, _newArrayList);
    Graph _to = this.to(_graphImpl, resource, options);
    return _to;
  }
  
  public Graph to(final Graph graph, final Resource resource, final Map<? extends Object,? extends Object> options) {
    Graph _to = this.to(graph, resource, options, false);
    return _to;
  }
  
  public Graph to(final Graph graph, final Resource resource, final Map<? extends Object,? extends Object> options, final boolean named) {
    Graph _xblockexpression = null;
    {
      Serializer _switchResult = null;
      boolean _matched = false;
      if (!_matched) {
        if (true) {
          _matched=true;
          NamedGraphSerializer _namedGraphSerializer = new NamedGraphSerializer();
          _switchResult = _namedGraphSerializer;
        }
      }
      if (!_matched) {
        Serializer _serializer = new Serializer();
        _switchResult = _serializer;
      }
      final Serializer serializer = _switchResult;
      InputOutput.<Serializer>println(serializer);
      Graph _xifexpression = null;
      boolean _equals = Objects.equal(graph, null);
      if (_equals) {
        ValueFactoryImpl _instance = ValueFactoryImpl.getInstance();
        ArrayList<Statement> _newArrayList = CollectionLiterals.<Statement>newArrayList();
        GraphImpl _graphImpl = new GraphImpl(_instance, _newArrayList);
        _xifexpression = _graphImpl;
      } else {
        _xifexpression = graph;
      }
      Graph _to = serializer.to(resource, _xifexpression);
      _xblockexpression = (_to);
    }
    return _xblockexpression;
  }
  
  public void from(final InputStream stream, final Resource resource, final Map<? extends Object,? extends Object> options) {
    try {
      final RDFParser parser = Rio.createParser(RDFFormat.RDFXML);
      LinkedHashModel _linkedHashModel = new LinkedHashModel();
      final LinkedHashModel graph = _linkedHashModel;
      StatementCollector _statementCollector = new StatementCollector(graph);
      final StatementCollector collector = _statementCollector;
      parser.setRDFHandler(collector);
      try {
        URI _uRI = resource.getURI();
        String _string = _uRI.toString();
        parser.parse(stream, _string);
        this.from(graph, resource, options);
      } catch (final Throwable _t) {
        if (_t instanceof IOException) {
          final IOException e = (IOException)_t;
          throw e;
        } else if (_t instanceof RDFParseException) {
          final RDFParseException e_1 = (RDFParseException)_t;
          throw e_1;
        } else if (_t instanceof RDFHandlerException) {
          final RDFHandlerException e_2 = (RDFHandlerException)_t;
          throw e_2;
        } else {
          throw Exceptions.sneakyThrow(_t);
        }
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public void from(final Model graph, final Resource resource, final Map<? extends Object,? extends Object> options) {
    Deserializer _deserializer = new Deserializer();
    final Deserializer deserializer = _deserializer;
    deserializer.from(graph, resource);
  }
}
