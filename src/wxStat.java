import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mz.jk.jsix.libs.XJava;
import de.mz.jk.plgs.data.Workflow;
import de.mz.jk.plgs.reader.WorkflowReader;

/** wxStat, , Dec 6, 2016
 * <h3>{@link wxStat}</h3>
 * @author jkuharev
 * @version Dec 6, 2016 3:09:21 PM
 */
public class wxStat
{
	static final String sFile = "file", sSample = "sample", sMode = "mode", sQueries = "queries", sPeptides = "peptides", sProteins = "proteins";
	static final String[] cols = { sFile, sSample, sMode, sQueries, sPeptides, sProteins };
	static final String sep = ",";

	public static Map<String, String> getStat(File xmlFile)
	{
		Map<String, String> map = new HashMap<>();
		map.put( "file", xmlFile.getName() );
		try
		{
			Workflow w = WorkflowReader.getWorkflow( xmlFile, true );
			map.put( sSample, w.acquired_name );
			map.put( sMode, w.acquisitionMode.toString() );
			map.put( sQueries, w.queryMasses.size() + "" );
			map.put( sPeptides, w.peptides.size() + "" );
			map.put( sProteins, w.proteins.size() + "" );
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return map;
	}

	public wxStat(String[] args)
	{
		if (args.length < 1)
		{
			printHelp();
		}
		List<Map<String, String>> maps = new ArrayList<Map<String, String>>();
		for ( String a : args )
		{
			File xmlFile = new File( a );
			if (xmlFile.exists())
			{
				Map<String, String> map = getStat( xmlFile );
				maps.add( map );
			}
		}
		if (maps.size() > 0) printStats( maps );
	}

	private void printHelp()
	{
		System.out.println( "--------------------------------------------------------------------------------" );
		System.out.println( "wxStat - PLGS Workflow XML Statistics Viewer." );
		System.out.println();
		System.out.println( "wxStat will read and summarize basic some information" );
		System.out.println( "about PLGS search result files & output them" );
		System.out.println( "to the standard output as Comma Separated Values." );
		System.out.println();
		System.out.println( "Usage:" );
		System.out.println();
		System.out.println( "java -jar wxStat.jar $1 $2 $3 $... $n" );
		System.out.println( "	$1 ... $n		represent valid file system paths" );
		System.out.println( "				to PLGS search result files in xml format." );
		System.out.println( "--------------------------------------------------------------------------------" );
	}

	public void printStats(List<Map<String, String>> maps)
	{
		String titleRow = XJava.joinArray( cols, sep );
		System.out.println( titleRow );
		for ( Map<String, String> map : maps )
		{
			List<String> vals = new ArrayList<>( cols.length );
			for ( String c : cols )
			{
				vals.add( map.containsKey( c ) ? map.get( c ) : "" );
			}
			String valRow = XJava.joinList( vals, sep );
			System.out.println( valRow );
		}
	}

	public static void main(String[] args)
	{
		new wxStat( args );
	}
}
