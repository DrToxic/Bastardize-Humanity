;Debug code
If debug = True Then
	Print "Creating main window: "+mainWidth+", "+mainHeight
	Print "Using desktop width and height: "+ClientWidth(Desktop())+", "+ClientHeight(Desktop())
EndIf


;Create the window
mainWindow	= CreateWindow ("Blank app", ClientWidth(Desktop())/2-mainWidth/2, ClientHeight(Desktop())/2-mainWidth/2, mainWidth, mainHeight, 0, 15)
mainPanel	= CreatePanel  (GadgetWidth(mainWindow)-200, 0, 200, GadgetHeight(mainWindow), mainWindow, 1)
playLabel	= CreateLabel  ("Player List",0,0,200,30,mainPanel,3)
playerList	= CreateListBox(0,35,200,200,mainPanel)

If debug = True Print "Creating menus"



menu = WindowMenu (mainWindow)
	main 	=	CreateMenu ("Main",0,menu)
	connect	=	CreateMenu ("Connect to server",1,main)

UpdateWindowMenu(mainWindow)