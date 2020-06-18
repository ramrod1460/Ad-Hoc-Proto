package com.proto.solution;


import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.proto.solution.exception.BadFileException;
import com.proto.solution.util.ProtoUtil;

/**
 * Class to solve the AD-Hoc homework challenge for the Proto homework
 * 
 * @author User
 *
 */
@SuppressWarnings({"PMD.CommentRequired","PMD.LawOfDemeter","PMD.DoNotCallSystemExit","PMD.CommentSize","PMD.SystemPrintln","PMD.UseUtilityClass"})
public class Proto {

	public static void main(final String [] pArgs) throws FileNotFoundException, IOException, EOFException {

		// Ensure input file specified exists
		if ( pArgs.length == 0 ) {
			System.err.println("Binary input file must be specificed");
			System.exit(1);
		} else {
			final File binaryFile = new File(pArgs[0]);
			if ( ! binaryFile.exists() || ! binaryFile.isFile() || ! binaryFile.canRead()) {
				System.err.println("File specificed: "+pArgs[0]+" does not exist or is not readable.");
				System.exit(1);
			}
		}

		// Parse binary file
		try (
			InputStream dis = Files.newInputStream(Paths.get(pArgs[0])) ) {
			ProtoUtil.parse(dis);
		} catch(IOException e) {
			System.out.println("Complete with IO Exception error: "+e.getMessage());
		} catch(BadFileException e) {
			System.out.println("Complete with file error: "+e.getMessage());
		} 
		
		System.exit(0);
	}


}