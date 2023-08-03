package org.uberfire.commons.regex.util;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
public class AntPathMatcher {
    public static final String DEFAULT_PATH_SEPARATOR = "/";
    private String pathSeparator = DEFAULT_PATH_SEPARATOR;
    public void setPathSeparator( final String pathSeparator ) {
        this.pathSeparator = pathSeparator != null ? pathSeparator : DEFAULT_PATH_SEPARATOR;
    }
    public boolean isPattern( final String path ) {
        return path.indexOf( '*' ) != -1 || path.indexOf( '?' ) != -1;
    }
    public boolean match( final String pattern,
                          final String path ) {
        return doMatch( pattern, path, true );
    }
    public boolean matchStart( final String pattern,
                               final String path ) {
        return doMatch( pattern, path, false );
    }
    protected boolean doMatch( String pattern,
                               String path,
                               boolean fullMatch ) {
        if ( path.startsWith( this.pathSeparator ) != pattern.startsWith( this.pathSeparator ) ) {
            return false;
        }
        String[] pattDirs = tokenizeToStringArray( pattern, this.pathSeparator );
        String[] pathDirs = tokenizeToStringArray( path, this.pathSeparator );
        int pattIdxStart = 0;
        int pattIdxEnd = pattDirs.length - 1;
        int pathIdxStart = 0;
        int pathIdxEnd = pathDirs.length - 1;
        while ( pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd ) {
            String patDir = pattDirs[ pattIdxStart ];
            if ( "**".equals( patDir ) ) {
                break;
            }
            if ( !matchStrings( patDir, pathDirs[ pathIdxStart ] ) ) {
                return false;
            }
            pattIdxStart++;
            pathIdxStart++;
        }
        if ( pathIdxStart > pathIdxEnd ) {
            if ( pattIdxStart > pattIdxEnd ) {
                return pattern.endsWith( this.pathSeparator ) ? path.endsWith( this.pathSeparator ) : !path
                        .endsWith( this.pathSeparator );
            }
            if ( !fullMatch ) {
                return true;
            }
            if ( pattIdxStart == pattIdxEnd && pattDirs[ pattIdxStart ].equals( "*" )
                    && path.endsWith( this.pathSeparator ) ) {
                return true;
            }
            for ( int i = pattIdxStart; i <= pattIdxEnd; i++ ) {
                if ( !pattDirs[ i ].equals( "**" ) ) {
                    return false;
                }
            }
            return true;
        } else if ( pattIdxStart > pattIdxEnd ) {
            return false;
        } else if ( !fullMatch && "**".equals( pattDirs[ pattIdxStart ] ) ) {
            return true;
        }
        while ( pattIdxStart <= pattIdxEnd && pathIdxStart <= pathIdxEnd ) {
            String patDir = pattDirs[ pattIdxEnd ];
            if ( patDir.equals( "**" ) ) {
                break;
            }
            if ( !matchStrings( patDir, pathDirs[ pathIdxEnd ] ) ) {
                return false;
            }
            pattIdxEnd--;
            pathIdxEnd--;
        }
        if ( pathIdxStart > pathIdxEnd ) {
            for ( int i = pattIdxStart; i <= pattIdxEnd; i++ ) {
                if ( !pattDirs[ i ].equals( "**" ) ) {
                    return false;
                }
            }
            return true;
        }
        while ( pattIdxStart != pattIdxEnd && pathIdxStart <= pathIdxEnd ) {
            int patIdxTmp = -1;
            for ( int i = pattIdxStart + 1; i <= pattIdxEnd; i++ ) {
                if ( pattDirs[ i ].equals( "**" ) ) {
                    patIdxTmp = i;
                    break;
                }
            }
            if ( patIdxTmp == pattIdxStart + 1 ) {
                pattIdxStart++;
                continue;
            }
            int patLength = patIdxTmp - pattIdxStart - 1;
            int strLength = pathIdxEnd - pathIdxStart + 1;
            int foundIdx = -1;
            strLoop:
            for ( int i = 0; i <= strLength - patLength; i++ ) {
                for ( int j = 0; j < patLength; j++ ) {
                    String subPat = pattDirs[ pattIdxStart + j + 1 ];
                    String subStr = pathDirs[ pathIdxStart + i + j ];
                    if ( !matchStrings( subPat, subStr ) ) {
                        continue strLoop;
                    }
                }
                foundIdx = pathIdxStart + i;
                break;
            }
            if ( foundIdx == -1 ) {
                return false;
            }
            pattIdxStart = patIdxTmp;
            pathIdxStart = foundIdx + patLength;
        }
        for ( int i = pattIdxStart; i <= pattIdxEnd; i++ ) {
            if ( !pattDirs[ i ].equals( "**" ) ) {
                return false;
            }
        }
        return true;
    }
    private boolean matchStrings( String pattern,
                                  String str ) {
        char[] patArr = pattern.toCharArray();
        char[] strArr = str.toCharArray();
        int patIdxStart = 0;
        int patIdxEnd = patArr.length - 1;
        int strIdxStart = 0;
        int strIdxEnd = strArr.length - 1;
        char ch;
        boolean containsStar = false;
        for ( char c : patArr ) {
            if ( c == '*' ) {
                containsStar = true;
                break;
            }
        }
        if ( !containsStar ) {
            if ( patIdxEnd != strIdxEnd ) {
                return false; 
            }
            for ( int i = 0; i <= patIdxEnd; i++ ) {
                ch = patArr[ i ];
                if ( ch != '?' ) {
                    if ( ch != strArr[ i ] ) {
                        return false;
                    }
                }
            }
            return true; 
        }
        if ( patIdxEnd == 0 ) {
            return true; 
        }
        while ( ( ch = patArr[ patIdxStart ] ) != '*' && strIdxStart <= strIdxEnd ) {
            if ( ch != '?' ) {
                if ( ch != strArr[ strIdxStart ] ) {
                    return false;
                }
            }
            patIdxStart++;
            strIdxStart++;
        }
        if ( strIdxStart > strIdxEnd ) {
            for ( int i = patIdxStart; i <= patIdxEnd; i++ ) {
                if ( patArr[ i ] != '*' ) {
                    return false;
                }
            }
            return true;
        }
        while ( ( ch = patArr[ patIdxEnd ] ) != '*' && strIdxStart <= strIdxEnd ) {
            if ( ch != '?' ) {
                if ( ch != strArr[ strIdxEnd ] ) {
                    return false;
                }
            }
            patIdxEnd--;
            strIdxEnd--;
        }
        if ( strIdxStart > strIdxEnd ) {
            for ( int i = patIdxStart; i <= patIdxEnd; i++ ) {
                if ( patArr[ i ] != '*' ) {
                    return false;
                }
            }
            return true;
        }
        while ( patIdxStart != patIdxEnd && strIdxStart <= strIdxEnd ) {
            int patIdxTmp = -1;
            for ( int i = patIdxStart + 1; i <= patIdxEnd; i++ ) {
                if ( patArr[ i ] == '*' ) {
                    patIdxTmp = i;
                    break;
                }
            }
            if ( patIdxTmp == patIdxStart + 1 ) {
                patIdxStart++;
                continue;
            }
            int patLength = patIdxTmp - patIdxStart - 1;
            int strLength = strIdxEnd - strIdxStart + 1;
            int foundIdx = -1;
            strLoop:
            for ( int i = 0; i <= strLength - patLength; i++ ) {
                for ( int j = 0; j < patLength; j++ ) {
                    ch = patArr[ patIdxStart + j + 1 ];
                    if ( ch != '?' ) {
                        if ( ch != strArr[ strIdxStart + i + j ] ) {
                            continue strLoop;
                        }
                    }
                }
                foundIdx = strIdxStart + i;
                break;
            }
            if ( foundIdx == -1 ) {
                return false;
            }
            patIdxStart = patIdxTmp;
            strIdxStart = foundIdx + patLength;
        }
        for ( int i = patIdxStart; i <= patIdxEnd; i++ ) {
            if ( patArr[ i ] != '*' ) {
                return false;
            }
        }
        return true;
    }
    public String extractPathWithinPattern( String pattern,
                                            String path ) {
        final String[] patternParts = tokenizeToStringArray( pattern, this.pathSeparator );
        final String[] pathParts = tokenizeToStringArray( path, this.pathSeparator );
        final StringBuilder buffer = new StringBuilder();
        int puts = 0;
        for ( int i = 0; i < patternParts.length; i++ ) {
            final String patternPart = patternParts[ i ];
            if ( ( patternPart.indexOf( '*' ) > -1 || patternPart.indexOf( '?' ) > -1 ) && pathParts.length >= i + 1 ) {
                if ( puts > 0 || ( i == 0 && !pattern.startsWith( this.pathSeparator ) ) ) {
                    buffer.append( this.pathSeparator );
                }
                buffer.append( pathParts[ i ] );
                puts++;
            }
        }
        for ( int i = patternParts.length; i < pathParts.length; i++ ) {
            if ( puts > 0 || i > 0 ) {
                buffer.append( this.pathSeparator );
            }
            buffer.append( pathParts[ i ] );
        }
        return buffer.toString();
    }
    public static String[] tokenizeToStringArray( String str,
                                                  String delimiters ) {
        if ( str == null ) {
            return null;
        }
        final StringTokenizer st = new StringTokenizer( str, delimiters );
        final List<String> tokens = new ArrayList<String>();
        while ( st.hasMoreTokens() ) {
            final String token = st.nextToken().trim();
            if ( token.length() > 0 ) {
                tokens.add( token );
            }
        }
        return tokens.toArray( new String[ tokens.size() ] );
    }
}