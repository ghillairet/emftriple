package org.eclipselabs.emftriple.jena.io;

import com.google.common.base.Objects;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import java.io.InputStream;
import org.eclipselabs.emftriple.RDFFormat;

@SuppressWarnings("all")
public class RDFReader {
  public static Model read(final InputStream stream, final RDFFormat format) {
    Model _createDefaultModel = ModelFactory.createDefaultModel();
    Model _read = RDFReader.read(stream, _createDefaultModel, format);
    return _read;
  }
  
  public static Model read(final InputStream stream, final Model graph, final RDFFormat format) {
    Model _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(format,RDFFormat.TURTLE)) {
        _matched=true;
        RDFReader.readTurtle(stream, graph);
      }
    }
    if (!_matched) {
      if (Objects.equal(format,RDFFormat.NTRIPLES)) {
        _matched=true;
        RDFReader.readNTriples(stream, graph);
      }
    }
    if (!_matched) {
      Model _readXML = RDFReader.readXML(stream, graph);
      _switchResult = _readXML;
    }
    return _switchResult;
  }
  
  private static void readNTriples(final InputStream stream, final Model graph) {
    com.hp.hpl.jena.rdf.model.RDFReader _reader = graph.getReader("N-TRIPLES");
    _reader.read(graph, stream, null);
  }
  
  private static void readTurtle(final InputStream stream, final Model graph) {
    com.hp.hpl.jena.rdf.model.RDFReader _reader = graph.getReader("TURTLE");
    _reader.read(graph, stream, null);
  }
  
  private static Model readXML(final InputStream stream, final Model graph) {
    Model _read = graph.read(stream, null);
    return _read;
  }
}
