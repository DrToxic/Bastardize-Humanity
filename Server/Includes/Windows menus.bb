;Debug code
If debug = True Then
	Print "Creating main window: "+mainWidth+", "+mainHeight
	Print "Using desktop width and height: "+ClientWidth(Desktop())+", "+ClientHeight(Desktop())
EndIf


;Create the window
mainWindow = CreateWindow ("Bastard Server", ClientWidth(Desktop())/2-mainWidth/2, ClientHeight(Desktop())/2-mainWidth/2, mainWidth, mainHeight, 0, 15)



If debug = True Print "Creating menus"



menu = WindowMenu (mainWindow)
	main	=	CreateMenu ("Server",0,menu)
	svrRun	= 	CreateMenu ("Run server",1,main)
				CreateMenu ("srvbye",2,main)

UpdateWindowMenu(mainWindow)