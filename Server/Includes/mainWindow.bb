;Debug code
If debug = True Then
	Print "Creating main window: "+mainWidth+", "+mainHeight
	Print "Using desktop width and height: "+ClientWidth(Desktop())+", "+ClientHeight(Desktop())
EndIf


;Create the window
Global mainWindow = CreateWindow ("Bastard Server", ClientWidth(Desktop())/2-mainWidth/2, ClientHeight(Desktop())/2-mainWidth/2, mainWidth, mainHeight, 0, 15)

;Create the Chat Panel
If debug = True Print "Creating main menus"

menu = WindowMenu (mainWindow)
	main	=	CreateMenu ("Server",0,menu)
	svrRun	= 	CreateMenu ("Run server",1,main)
	save	=	CreateMenu ("Save Config on Exit",2,main)

If FileType(cfgFile$) = 1 Then 	CheckMenu(save)
If startOnRun = 1 Then CheckMenu(svrRun)
svrGame = CreateTCPServer(serverPort)
UpdateWindowMenu(mainWindow)