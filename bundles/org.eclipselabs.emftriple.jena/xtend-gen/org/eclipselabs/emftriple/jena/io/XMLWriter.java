package org.eclipselabs.emftriple.jena.io;

import com.google.common.base.Objects;
import com.hp.hpl.jena.rdf.model.Model;
import java.io.OutputStream;
import org.eclipselabs.emftriple.RDFFormat;

@SuppressWarnings("all")
public class XMLWriter {
  public static Model write(final OutputStream stream, final Model graph, final RDFFormat format) {
    Model _switchResult = null;
    boolean _matched = false;
    if (!_matched) {
      if (Objects.equal(format,RDFFormat.TURTLE)) {
        _matched=true;
        XMLWriter.writeTurtle(stream, graph);
      }
    }
    if (!_matched) {
      if (Objects.equal(format,RDFFormat.NTRIPLES)) {
        _matched=true;
        XMLWriter.writeNTriples(stream, graph);
      }
    }
    if (!_matched) {
      Model _writeXML = XMLWriter.writeXML(stream, graph);
      _switchResult = _writeXML;
    }
    return _switchResult;
  }
  
  private static void writeNTriples(final OutputStream stream, final Model model) {
    model.write(stream, "N-TRIPLES");
  }
  
  private static void writeTurtle(final OutputStream stream, final Model model) {
    model.write(stream, "TTL");
  }
  
  private static Model writeXML(final OutputStream stream, final Model model) {
    Model _write = model.write(stream);
    return _write;
  }
}
