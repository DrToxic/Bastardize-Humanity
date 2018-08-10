If debug = True Then Print "SAVING CONFIG..."

If FileType(cfgFile$) = 1 Then
	CopyFile(cfgFile$,CurrentDir$()+"Config.backup.cfg")
	DeleteFile(cfgFile$)
EndIf

writeOut = WriteFile(cfgFile$)
	WriteLine(writeOut,"----------=----------------")
	WriteLine(writeOut,"OPTION    = SETTING        ")
	WriteLine(writeOut,"----------=----------------")
	WriteLine(writeOut,"Width     = "+GadgetWidth(mainWindow))
	WriteLine(writeOut,"Height    = "+GadgetHeight(mainWindow))
	WriteLine(writeOut,"ServerPort= "+serverPort%)
	WriteLine(writeOut,"aiPlayer  = "+aiPlayer$)
	WriteLine(writeOut,"StartOnRun= "+startOnRun)
	WriteLine(writeOut,"NSFW      = "+NSFW)
	
	WriteLine(writeOut,"----------=----------------")
CloseFile(writeOut)