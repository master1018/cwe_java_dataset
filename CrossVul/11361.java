package org.codehaus.plexus.util.cli;
import org.codehaus.plexus.util.Os;
import org.codehaus.plexus.util.StringUtils;
import org.codehaus.plexus.util.cli.shell.BourneShell;
import org.codehaus.plexus.util.cli.shell.CmdShell;
import org.codehaus.plexus.util.cli.shell.CommandShell;
import org.codehaus.plexus.util.cli.shell.Shell;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
public class Commandline
    implements Cloneable
{
    protected static final String OS_NAME = "os.name";
    protected static final String WINDOWS = "Windows";
    protected Vector arguments = new Vector();
    protected Map envVars = Collections.synchronizedMap( new LinkedHashMap() );
    private long pid = -1;
    private Shell shell;
    protected String executable;
    private File workingDir;
    public Commandline( String toProcess, Shell shell )
    {
        this.shell = shell;
        String[] tmp = new String[0];
        try
        {
            tmp = CommandLineUtils.translateCommandline( toProcess );
        }
        catch ( Exception e )
        {
            System.err.println( "Error translating Commandline." );
        }
        if ( ( tmp != null ) && ( tmp.length > 0 ) )
        {
            setExecutable( tmp[0] );
            for ( int i = 1; i < tmp.length; i++ )
            {
                createArgument().setValue( tmp[i] );
            }
        }
    }
    public Commandline( Shell shell )
    {
        this.shell = shell;
    }
    public Commandline( String toProcess )
    {
        setDefaultShell();
        String[] tmp = new String[0];
        try
        {
            tmp = CommandLineUtils.translateCommandline( toProcess );
        }
        catch ( Exception e )
        {
            System.err.println( "Error translating Commandline." );
        }
        if ( ( tmp != null ) && ( tmp.length > 0 ) )
        {
            setExecutable( tmp[0] );
            for ( int i = 1; i < tmp.length; i++ )
            {
                createArgument().setValue( tmp[i] );
            }
        }
    }
    public Commandline()
    {
        setDefaultShell();
    }
    public long getPid()
    {
        if ( pid == -1 )
        {
            pid = Long.parseLong( String.valueOf( System.currentTimeMillis() ) );
        }
        return pid;
    }
    public void setPid( long pid )
    {
        this.pid = pid;
    }
    public class Marker
    {
        private int position;
        private int realPos = -1;
        Marker( int position )
        {
            this.position = position;
        }
        public int getPosition()
        {
            if ( realPos == -1 )
            {
                realPos = ( getLiteralExecutable() == null ? 0 : 1 );
                for ( int i = 0; i < position; i++ )
                {
                    Arg arg = (Arg) arguments.elementAt( i );
                    realPos += arg.getParts().length;
                }
            }
            return realPos;
        }
    }
    private void setDefaultShell()
    {
        if ( Os.isFamily( Os.FAMILY_WINDOWS ) )
        {
            if ( Os.isFamily( Os.FAMILY_WIN9X ) )
            {
                setShell( new CommandShell() );
            }
            else
            {
                setShell( new CmdShell() );
            }
        }
        else
        {
            setShell( new BourneShell() );
        }
    }
    public Argument createArgument()
    {
        return this.createArgument( false );
    }
    public Argument createArgument( boolean insertAtStart )
    {
        Argument argument = new Argument();
        if ( insertAtStart )
        {
            arguments.insertElementAt( argument, 0 );
        }
        else
        {
            arguments.addElement( argument );
        }
        return argument;
    }
    public Arg createArg()
    {
        return this.createArg( false );
    }
    public Arg createArg( boolean insertAtStart )
    {
        Arg argument = new Argument();
        if ( insertAtStart )
        {
            arguments.insertElementAt( argument, 0 );
        }
        else
        {
            arguments.addElement( argument );
        }
        return argument;
    }
    public void addArg( Arg argument )
    {
        this.addArg( argument, false );
    }
    public void addArg( Arg argument, boolean insertAtStart )
    {
        if ( insertAtStart )
        {
            arguments.insertElementAt( argument, 0 );
        }
        else
        {
            arguments.addElement( argument );
        }
    }
    public void setExecutable( String executable )
    {
        shell.setExecutable( executable );
        this.executable = executable;
    }
    public String getLiteralExecutable()
    {
        return executable;
    }
    public String getExecutable()
    {
        String exec = shell.getExecutable();
        if ( exec == null )
        {
            exec = executable;
        }
        return exec;
    }
    public void addArguments( String[] line )
    {
        for ( int i = 0; i < line.length; i++ )
        {
            createArgument().setValue( line[i] );
        }
    }
    public void addEnvironment( String name, String value )
    {
        envVars.put( name, value );
    }
    public void addSystemEnvironment()
        throws Exception
    {
        Properties systemEnvVars = CommandLineUtils.getSystemEnvVars();
        for ( Iterator i = systemEnvVars.keySet().iterator(); i.hasNext(); )
        {
            String key = (String) i.next();
            if ( !envVars.containsKey( key ) )
            {
                addEnvironment( key, systemEnvVars.getProperty( key ) );
            }
        }
    }
    public String[] getEnvironmentVariables()
        throws CommandLineException
    {
        try
        {
            addSystemEnvironment();
        }
        catch ( Exception e )
        {
            throw new CommandLineException( "Error setting up environmental variables", e );
        }
        String[] environmentVars = new String[envVars.size()];
        int i = 0;
        for ( Iterator iterator = envVars.keySet().iterator(); iterator.hasNext(); )
        {
            String name = (String) iterator.next();
            String value = (String) envVars.get( name );
            environmentVars[i] = name + "=" + value;
            i++;
        }
        return environmentVars;
    }
    public String[] getCommandline()
    {
        final String[] args = getArguments();
        String executable = getLiteralExecutable();
        if ( executable == null )
        {
            return args;
        }
        final String[] result = new String[args.length + 1];
        result[0] = executable;
        System.arraycopy( args, 0, result, 1, args.length );
        return result;
    }
    public String[] getShellCommandline()
    {
        verifyShellState();
        return (String[]) getShell().getShellCommandLine( getArguments() ).toArray( new String[0] );
    }
    public String[] getArguments()
    {
        Vector result = new Vector( arguments.size() * 2 );
        for ( int i = 0; i < arguments.size(); i++ )
        {
            Argument arg = (Argument) arguments.elementAt( i );
            String[] s = arg.getParts();
            if ( s != null )
            {
                for ( int j = 0; j < s.length; j++ )
                {
                    result.addElement( s[j] );
                }
            }
        }
        String[] res = new String[result.size()];
        result.copyInto( res );
        return res;
    }
    public String toString()
    {
        return StringUtils.join( getShellCommandline(), " " );
    }
    public int size()
    {
        return getCommandline().length;
    }
    public Object clone()
    {
        Commandline c = new Commandline( (Shell) shell.clone() );
        c.executable = executable;
        c.workingDir = workingDir;
        c.addArguments( getArguments() );
        return c;
    }
    public void clear()
    {
        executable = null;
        workingDir = null;
        shell.setExecutable( null );
        shell.clearArguments();
        arguments.removeAllElements();
    }
    public void clearArgs()
    {
        arguments.removeAllElements();
    }
    public Marker createMarker()
    {
        return new Marker( arguments.size() );
    }
    public void setWorkingDirectory( String path )
    {
        shell.setWorkingDirectory( path );
        workingDir = new File( path );
    }
    public void setWorkingDirectory( File workingDirectory )
    {
        shell.setWorkingDirectory( workingDirectory );
        workingDir = workingDirectory;
    }
    public File getWorkingDirectory()
    {
        File workDir = shell.getWorkingDirectory();
        if ( workDir == null )
        {
            workDir = workingDir;
        }
        return workDir;
    }
    public Process execute()
        throws CommandLineException
    {
        verifyShellState();
        Process process;
        String[] environment = getEnvironmentVariables();
        File workingDir = shell.getWorkingDirectory();
        try
        {
            if ( workingDir == null )
            {
                process = Runtime.getRuntime().exec( getCommandline(), environment, workingDir );
            }
            else
            {
                if ( !workingDir.exists() )
                {
                    throw new CommandLineException( "Working directory \"" + workingDir.getPath()
                        + "\" does not exist!" );
                }
                else if ( !workingDir.isDirectory() )
                {
                    throw new CommandLineException( "Path \"" + workingDir.getPath()
                        + "\" does not specify a directory." );
                }
                process = Runtime.getRuntime().exec( getCommandline(), environment, workingDir );
            }
        }
        catch ( IOException ex )
        {
            throw new CommandLineException( "Error while executing process.", ex );
        }
        return process;
    }
    private void verifyShellState()
    {
        if ( shell.getWorkingDirectory() == null )
        {
            shell.setWorkingDirectory( workingDir );
        }
        if ( shell.getOriginalExecutable() == null )
        {
            shell.setExecutable( executable );
        }
    }
    public Properties getSystemEnvVars()
        throws Exception
    {
        return CommandLineUtils.getSystemEnvVars();
    }
    public void setShell( Shell shell )
    {
        this.shell = shell;
    }
    public Shell getShell()
    {
        return shell;
    }
    public static String[] translateCommandline( String toProcess )
        throws Exception
    {
        return CommandLineUtils.translateCommandline( toProcess );
    }
    public static String quoteArgument( String argument )
        throws CommandLineException
    {
        return CommandLineUtils.quote( argument );
    }
    public static String toString( String[] line )
    {
        return CommandLineUtils.toString( line );
    }
    public static class Argument
        implements Arg
    {
        private String[] parts;
        public void setValue( String value )
        {
            if ( value != null )
            {
                parts = new String[] { value };
            }
        }
        public void setLine( String line )
        {
            if ( line == null )
            {
                return;
            }
            try
            {
                parts = CommandLineUtils.translateCommandline( line );
            }
            catch ( Exception e )
            {
                System.err.println( "Error translating Commandline." );
            }
        }
        public void setFile( File value )
        {
            parts = new String[] { value.getAbsolutePath() };
        }
        public String[] getParts()
        {
            return parts;
        }
    }
}
