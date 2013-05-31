package org.eclipselabs.emftriple.sesame.io;

import com.google.common.base.Objects;
import java.io.OutputStream;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipselabs.emftriple.RDFFormat;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.Rio;

@SuppressWarnings("all")
public class RDFWriter {
  public static void write(final OutputStream stream, final Model graph, final RDFFormat format) {
    org.openrdf.rio.RDFWriter _writer = RDFWriter.getWriter(format, stream);
    RDFWriter.doWrite(_writer, graph);
  }
  
  private static org.openrdf.rio.RDFWriter getWriter(final RDFFormat format, final OutputStream stream) {
    org.openrdf.rio.RDFWriter _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(format,RDFFormat.TURTLE)) {
        _matched=true;
        org.openrdf.rio.RDFWriter _createWriter = Rio.createWriter(org.openrdf.rio.RDFFormat.TURTLE, stream);
        _switchResult = _createWriter;
      }
    }
    if (!_matched) {
      if (Objects.equal(format,RDFFormat.NTRIPLES)) {
        _matched=true;
        org.openrdf.rio.RDFWriter _createWriter_1 = Rio.createWriter(org.openrdf.rio.RDFFormat.NTRIPLES, stream);
        _switchResult = _createWriter_1;
      }
    }
    if (!_matched) {
      org.openrdf.rio.RDFWriter _createWriter_2 = Rio.createWriter(org.openrdf.rio.RDFFormat.RDFXML, stream);
      _switchResult = _createWriter_2;
    }
    return _switchResult;
  }
  
  private static void doWrite(final org.openrdf.rio.RDFWriter writer, final Model graph) {
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
}
