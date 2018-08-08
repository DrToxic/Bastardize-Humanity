If debug = True Then Print "Creating main window: "+mainWidth+", "+mainHeight+"Using desktop width and height: "+ClientWidth(Desktop())+", "+ClientHeight(Desktop())

If bastardMode = 1 Then
	mainWindow = CreateWindow ("Bastardize Humanity", ClientWidth(Desktop())/2-mainWidth/2, ClientHeight(Desktop())/2-mainWidth/2, mainWidth, mainHeight, 0, 15)
Else
	mainWindow = CreateWindow ("Cards Against Humanity - Network Edition", ClientWidth(Desktop())/2-mainWidth/2, ClientHeight(Desktop())/2-mainWidth/2, mainWidth, mainHeight, 0, 15)
EndIf

If debug = True Print "Creating main menus"



menu = WindowMenu (mainWindow)

If bastardMode = 1 Then
	main	=	CreateMenu ("Bastard Server Options",0,menu)
	connect	=	CreateMenu ("Contact Hell",1,main)
				CreateMenu ("Configure Ouija Board...",2,main)
Else
	main	=	CreateMenu ("Server Options",0,menu)
	connect	=	CreateMenu ("Connect to server",1,main)
				CreateMenu ("Set Server IP and Port",2,main)
EndIf
	UpdateWindowMenu(mainWindow)
