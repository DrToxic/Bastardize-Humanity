;Debug code
If debug = True Then
	Print "Creating main window: "+mainWidth+", "+mainHeight
	Print "Using desktop width and height: "+ClientWidth(Desktop())+", "+ClientHeight(Desktop())
EndIf


;Create the window
mainWindow = CreateWindow ("Bastard Server", ClientWidth(Desktop())/2-mainWidth/2, ClientHeight(Desktop())/2-mainWidth/2, mainWidth, mainHeight, 0, 15)
mainPanel	= CreatePanel  (GadgetWidth(mainWindow)-200, 0, 200, GadgetHeight(mainWindow), mainWindow, 1)
playLabel	= CreateLabel  ("Player List",0,0,200,30,mainPanel,3)
Global playerList	= CreateListBox(0,35,200,200,mainPanel)



If debug = True Print "Creating menus"



menu = WindowMenu (mainWindow)
	main	=	CreateMenu ("Server",0,menu)
	svrRun	= 	CreateMenu ("Run server",1,main)
				CreateMenu ("srvbye",2,main)

UpdateWindowMenu(mainWindow)