package ch.heigvd.res.labio.impl.filters;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;



/**
 * This class transforms the streams of character sent to the decorated writer.
 * When filter encounters a line separator, it sends it to the decorated writer.
 * It then sends the line number and a tab character, before resuming the write
 * process.
 *
 * Hello\n\World -> 1\Hello\n2\tWorld
 *
 * @author Olivier Liechti
 */
public class FileNumberingFilterWriter extends FilterWriter {

  private static final Logger LOG = Logger.getLogger(FileNumberingFilterWriter.class.getName());

  public FileNumberingFilterWriter(Writer out) {
    super(out);
  }

  int numLigne = 0;
  int previous = 0;
  @Override
  public void write(String str, int off, int len) throws IOException {
    for (int i = off; i < off + len; i++) {
      write(str.charAt(i));
    }
  }

  @Override
  public void write(char[] cbuf, int off, int len) throws IOException {
    for (int i = 0; i < off + len; i++) {
      write(cbuf[i]);
    }
  }

  @Override
  public void write(int c) throws IOException {
    if (numLigne == 0 || previous == '\r' && c != '\n') {
      for (char ch : (++numLigne + "\t").toCharArray())
        super.write(ch);
    }
    super.write(c);
    if (c == '\n') {
      for (char ch : (++numLigne + "\t").toCharArray())
        super.write(ch);
    }
    previous = c;
  }

}
