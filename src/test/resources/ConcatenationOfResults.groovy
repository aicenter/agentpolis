
nameOfCSVFile = "fareevasionlog.csv"
outputFilesAndProcessingScripts = [:]
outputFilesAndProcessingScripts["ResultsOverview.csv"] = "src/eventcounter/Overview.groovy"
outputFilesAndProcessingScripts["EvasionOverview.csv"] = "src/eventcounter/EvasionOverview.groovy"


nameOfExperimentsDir = "experiments"

//nameOfOverviewResults = "ResultsOverview.csv"
//nameOfEvasionOverviewResults = "EvasionOverview.csv"

File file = new File(nameOfExperimentsDir);
subDirs = new ArrayList<String>()
subDirsOfExperiments = new ArrayList<File>()

int iter = 0
file.listFiles().each {

	subDirsOfExperiments.add(it)


	new File(it,"results").listFiles().each {
		if(it.isDirectory()){
			subDirs[iter++] = it.getPath()

		}
	}




}

subDirs.each {  pathToSubDir ->
	String inputFile =  pathToSubDir + "/" + nameOfCSVFile
	outputFilesAndProcessingScripts.each { outputFile, processingStript ->
		String outputFileForScript = pathToSubDir  + "/" + outputFile
		run( new File(processingStript), [inputFile, outputFileForScript]as String[] )

	}
}




subDirsOfExperiments.each { dir ->
	dirs = new ArrayList<String>()

	File f = new File(dir,"results");
	f.listFiles().each {
		if(it.isDirectory()){
			dirs.add(it.getPath())			
		}
	}

	outputFilesAndProcessingScripts.each { outputFile, processingStript ->
		writeToFileAndAddId(f.getPath(),outputFile,dirs)
	}
}


def writeToFileAndAddId(pathToResultFile, nameOfResultFile,	subDirs){


	BufferedWriter write = new BufferedWriter(new FileWriter(new File(pathToResultFile+ File.separator +nameOfResultFile)))
	subDirs.each {  pathOfSubDir ->

		String inFile = pathOfSubDir + File.separator + nameOfResultFile		
		File outFile = new File(inFile)

		boolean isHead = true;

		id = pathOfSubDir.substring(pathOfSubDir.lastIndexOf(File.separator) + 1)

		outFile.each {

			if(isHead){
				write.write(  " Scenario id," + it )
				isHead = false
			}else{
				write.write( id + "," + it )
			}


			write.newLine()
		}

		write.newLine()


	}

	write.close()

}

println("Finished concatenation")