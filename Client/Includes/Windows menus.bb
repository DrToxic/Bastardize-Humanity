;Debug code
If debug = True Then
	Print "Creating main window: "+mainWidth+", "+mainHeight
	Print "Using desktop width and height: "+ClientWidth(Desktop())+", "+ClientHeight(Desktop())
EndIf


;Create the window
mainWindow	= CreateWindow ("Blank app", ClientWidth(Desktop())/2-mainWidth/2, ClientHeight(Desktop())/2-mainWidth/2, mainWidth, mainHeight, 0, 15)

If debug = True Print "Creating menus"



menu = WindowMenu (mainWindow)
	main 	=	CreateMenu ("Main",0,menu)
	connect	=	CreateMenu ("Connect to server",1,main)

UpdateWindowMenu(mainWindow)