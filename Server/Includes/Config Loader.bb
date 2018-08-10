	;Defaults, if the config file fails.
	mainWidth	= 640
	mainHeight	= 480
	chatWidth	= 640
	chatHeight	= 360
	bastardmode = False
	serverPort	= 40000
Global	startOnRun	= 0
Global	aiPlayer$	= "Rando Cardrissian"
	;----------------------------------



If debug = True Print "Looking for "+cfgFile$


Select FileType(cfgFile$)
	Case 1
	;Load options from a fixed Width config file.
	
	config = ReadFile(cfgFile$)
	If config = 0 Then
		If debug = True Then Print "config not found, using defaults."
	
	Else
		While Not Eof(config)
		temp$ = ReadLine(config)
	
	
	
		Select Upper(Left(temp$,10));First 10 characters are the OPTION Code.
			;Config Filters -- try to Load these from the file.
			Case "WIDTH     " mainWidth = Mid$(temp$,13)	: If debug = True Then Print "LOADING CONFIG: using line: WIDTH      = "+mainWidth
			Case "HEIGHT    " mainHeight = Mid$(temp$,13)	: If debug = True Then Print "LOADING CONFIG: using line: HEIGHT     = "+mainHeight
			Case "SERVERPORT" serverPort% = Mid$(temp$,13)	: If debug = True Then Print "LOADING CONFIG: using line: SERVERPORT = "+serverPort%
			Case "AIPLAYER  " aiPlayer$ = Mid$(temp$,13)	: If debug = True Then Print "LOADING CONFIG: using line: AIPLAYER   = "+aiPlayer$
			Case "STARTONRUN" startOnRun = Mid$(temp$,13)	: If debug = True Then Print "LOADING CONFIG: using line: STARTONRUN = "+startOnRun
			Case "NSFW      " bastardmode = Mid$(temp$,13)	: If debug = True Then Print "LOADING CONFIG: using line: NSFW       = "+bastardMode
	
			;---------------------------------------------
			
			;If we find anything we can't use... ignore it.
			Default If debug = True Print "LOADING CONFIG: trash line: "+temp$
			;Yes, that's how we deal with human-readable headers.
		End Select
		Wend
		CloseFile(config)
		If debug = True Then
			Print "CONFIG LOADED!"
		EndIf
	;	If debug = True Then Stop
	EndIf
	Case 0
		Notify "Persistance Configuration file (Config.cfg) not found."+Chr$(13)+"Default values loaded, Persistance Disabled."
	Case -1
		Notify "Something's wrong with the persistance configuration file (Config.cfg)."+Chr$(13)+"It looks like a directory."+Chr$(13)+"To the rest of you, that's a Folder."
End Select