//
////nameOfCSVFile = "fareevasionlog.csv"
//
//
////outputFilesAndProcessingScripts = [:]
////outputFilesAndProcessingScripts["src/eventcounter/Overview.groovy"] = "ResultsOverview.csv"
////outputFilesAndProcessingScripts["src/eventcounter/EvasionOverview.groovy"] = "EvasionOverview.csv"
//
//
//
//nameOfExperimentsDir = "experiments"
//
////nameOfOverviewResults = "ResultsOverview.csv"
////nameOfEvasionOverviewResults = "EvasionOverview.csv"
//
//File file = new File(nameOfExperimentsDir);
//subDirs = []
//subDirsOfExperiments = new ArrayList<File>()
//
//class CsvNameAndSubDirs{
//	public csvName
//	public dirs = []
//	public scriptsAndOutpuFileName
//}
//
//int iter = 0
//file.listFiles().each {
//
//	subDirsOfExperiments.add(it)
//
//
//	def config = new ConfigSlurper().parse(new File(it,'config/config.groovy').toURL())
//
//	csvNameAndSubDirs = new CsvNameAndSubDirs(csvName:config.pathToCSVEventLogFile)
//	csvNameAndSubDirs.scriptsAndOutpuFileName = config.postprocessingScriptsAndTheirNameOfOuputFile
//
//	new File(it,"results").listFiles().each {
//		if(it.isDirectory()){
//			csvNameAndSubDirs.dirs.add(it.getPath())
//
//		}
//	}
//
//	subDirs[iter++] = csvNameAndSubDirs
//
//
//
//}
//
//subDirs.each {  csvNameAndSubDirs ->
//
//	csvNameAndSubDirs.dirs.each { pathToSubDir ->
//	String inputFile =  pathToSubDir + "/" + csvNameAndSubDirs.csvName
//
//	csvNameAndSubDirs.scriptsAndOutpuFileName.each { processingStript,outputFile ->
//		String outputFileForScript = pathToSubDir  + "/" + outputFile
//		run( new File(processingStript), [inputFile, outputFileForScript]as String[] )
//
//	}
//	}
//
//}
//
//
//
//
//subDirsOfExperiments.each { dir ->
//	dirs = new ArrayList<String>()
//
//	File f = new File(dir,"results");
//	f.listFiles().each {
//		if(it.isDirectory()){
//			dirs.add(it.getPath())
//		}
//	}
//
//	def config = new ConfigSlurper().parse(new File(dir,'config/config.groovy').toURL())
//
//	config.postprocessingScriptsAndTheirNameOfOuputFile.each { processingStript,outputFile ->
//		writeToFileAndAddId(f.getPath(),outputFile,dirs)
//	}
//}
//
//
//def writeToFileAndAddId(pathToResultFile, nameOfResultFile,	subDirs){
//
//
//	BufferedWriter write = new BufferedWriter(new FileWriter(new File(pathToResultFile+ File.separator +nameOfResultFile)))
//	subDirs.each {  pathOfSubDir ->
//
//		String inFile = pathOfSubDir + File.separator + nameOfResultFile
//		File outFile = new File(inFile)
//
//		boolean isHead = true;
//
//		id = pathOfSubDir.substring(pathOfSubDir.lastIndexOf(File.separator) + 1)
//
//		outFile.each {
//
//			if(isHead){
//				write.write(  " Scenario id," + it )
//				isHead = false
//			}else{
//				write.write( id + "," + it )
//			}
//
//
//			write.newLine()
//		}
//
//		write.newLine()
//
//
//	}
//
//	write.close()
//
//}
//
//println("Finished concatenation")