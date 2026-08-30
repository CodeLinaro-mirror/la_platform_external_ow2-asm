package org.objectweb.asm;

/** Exception thrown when a limit set by {@link ClassWriter#setComputeLimits} is exceeded. */
public final class LimitExceededException extends RuntimeException {

  private static final long serialVersionUID = -1007650817078992929L;

  /**
   * Constructs a new {@link LimitExceededException}.
   *
   * @param message details about the exceeded limit.
   */
  public LimitExceededException(final String message) {
    super(message);
  }
}
