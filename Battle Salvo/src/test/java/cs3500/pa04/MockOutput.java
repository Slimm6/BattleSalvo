package cs3500.pa04;

import java.io.OutputStream;

/**
 * An output stream for testing.
 */
public class MockOutput extends OutputStream implements Appendable {
  /**
   * Contents of this output stream.
   */
  private final StringBuilder output;

  /**
   * Creates a new empty mock output stream.
   */
  public MockOutput() {
    output = new StringBuilder();
  }

  @Override
  public synchronized void write(int b) {
    output.append((char) b);
  }

  @Override
  public Appendable append(CharSequence csq) {
    output.append(csq);
    return this;
  }

  @Override
  public Appendable append(CharSequence csq, int start, int end) {
    output.append(csq, start, end);
    return this;
  }

  @Override
  public Appendable append(char c) {
    output.append(c);
    return this;
  }

  @Override
  public String toString() {
    return output.toString();
  }
}
