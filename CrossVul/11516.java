package com.browserup.bup.rest.validation.util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class MessageSanitizer {
  public static final char BEGIN_CHAR = '{';
  public static final char END_CHAR = '}';
  public static final char EL_DESIGNATOR = '$';
  public static final char ESCAPE_CHARACTER = '\\';
  private static final Pattern ESCAPE_PATTERN = Pattern.compile( "([\\" + ESCAPE_CHARACTER + BEGIN_CHAR + END_CHAR + EL_DESIGNATOR + "])" );
  private MessageSanitizer() {
  }
  public static String escape(String message) {
    if ( message == null ) {
      return null;
    }
    return ESCAPE_PATTERN.matcher( message ).replaceAll( Matcher.quoteReplacement( String.valueOf( ESCAPE_CHARACTER ) ) + "$1" );
  }
}
