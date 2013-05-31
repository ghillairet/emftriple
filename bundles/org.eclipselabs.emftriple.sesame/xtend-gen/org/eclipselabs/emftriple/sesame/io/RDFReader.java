package org.eclipselabs.emftriple.sesame.io;

import com.google.common.base.Objects;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.emf.common.util.URI;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipselabs.emftriple.RDFFormat;
import org.openrdf.model.Model;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.rio.helpers.StatementCollector;

@SuppressWarnings("all")
public class RDFReader {
  public static Model read(final InputStream stream, final RDFFormat format, final URI uri) {
    LinkedHashModel _linkedHashModel = new LinkedHashModel();
    Model _read = RDFReader.read(stream, _linkedHashModel, format, uri);
    return _read;
  }
  
  public static Model read(final InputStream stream, final Model graph, final RDFFormat format, final URI uri) {
    RDFParser _reader = RDFReader.getReader(format);
    Model _doRead = RDFReader.doRead(_reader, stream, graph, uri);
    return _doRead;
  }
  
  private static RDFParser getReader(final RDFFormat format) {
    RDFParser _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(format,org.openrdf.rio.RDFFormat.TURTLE)) {
        _matched=true;
        RDFParser _createParser = Rio.createParser(org.openrdf.rio.RDFFormat.TURTLE);
        _switchResult = _createParser;
      }
    }
    if (!_matched) {
      if (Objects.equal(format,org.openrdf.rio.RDFFormat.NTRIPLES)) {
        _matched=true;
        RDFParser _createParser_1 = Rio.createParser(org.openrdf.rio.RDFFormat.NTRIPLES);
        _switchResult = _createParser_1;
      }
    }
    if (!_matched) {
      RDFParser _createParser_2 = Rio.createParser(org.openrdf.rio.RDFFormat.RDFXML);
      _switchResult = _createParser_2;
    }
    return _switchResult;
  }
  
  private static Model doRead(final RDFParser reader, final InputStream stream, final Model graph, final URI uri) {
    try {
      Model _xblockexpression = null;
      {
        StatementCollector _statementCollector = new StatementCollector(graph);
        final StatementCollector collector = _statementCollector;
        reader.setRDFHandler(collector);
        try {
          String _string = uri.toString();
          reader.parse(stream, _string);
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
        _xblockexpression = (graph);
      }
      return _xblockexpression;
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
