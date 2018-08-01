;if we're debugging, print the output.
If debug = True Print "LOADING CONFIG"

;Load options from a fixed Width config file.

;Defaults, if the config file fails.
mainWidth	= 640
mainHeight	= 480
serverIP$	= "127.0.0.1"
serverPort%	= 40000
playername$ = Rand(0,2)

;Default player name... let's make it a bit random :)
Select playername$
	Case 0 playername$ = "Player_"+Rand(0,99999)
	Case 1 playername$ = "User_"+Rand(0,99999)
	Case 2 playername$ = "unknown_"+Rand(0,99999)
End Select
If debug = True Then Print "player name: "+playername$
;----------------------------------

config = ReadFile("Config.cfg")
If config = 0 Then
	If debug = True Then Print "config not found, using defaults."

Else
	While Not Eof(config)
	temp$ = ReadLine(config)



	Select Upper$(Left(temp$,10));First 10 characters are the OPTION Code.
		;Config Filters -- try to Load these from the file.
		Case "WIDTH     " mainWidth = Mid$(temp$,13)	: If debug = True Then Print "LOADING CONFIG: using line: WIDTH      = "+mainWidth
		Case "HEIGHT    " mainHeight = Mid$(temp$,13)	: If debug = True Then Print "LOADING CONFIG: using line: HEIGHT     = "+mainHeight
		Case "PLAYERNAME" playername$ = Mid$(temp$,13)	: If debug = True Then Print "LOADING CONFIG: using line: PLAYERNAME = "+playername$
		Case "SERVERIP  " serverIP$ = Mid$(temp$,13)	: If debug = True Then Print "LOADING CONFIG: using line: SERVERIP   = "+serverIP$
		Case "SERVERPORT" serverPort% = Mid$(temp$,13)	: If debug = True Then Print "LOADING CONFIG: using line: SERVERPORT = "+serverPort%
		;---------------------------------------------
		
		;If we find anything we can't use... ignore it.
		Default If debug = True Print "LOADING CONFIG: trash line: "+temp$
		;Yes, that's how we deal with human-readable headers.
	End Select
	Wend
	
	If debug = True Then
		Print "CONFIG LOADED!"
	EndIf
;	If debug = True Then Stop
EndIf