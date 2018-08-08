If debug = True Then
	Print "Creating main window: "+mainWidth+", "+mainHeight
	Print "Using desktop width and height: "+ClientWidth(Desktop())+", "+ClientHeight(Desktop())
EndIf


If bastardMode = 1 Then
	chatWindow	= CreateWindow  ("Bastard Operators from Hell", ClientWidth(Desktop())/2-chatWidth/2, ClientHeight(Desktop())-400, chatWidth, chatHeight, 0, 1)

	bestLabel1	= CreateLabel	("Funniest fucker so far: ",5,5,200,17,chatWindow,3)
	bestLabel2	= CreateLabel	("We blacked 'em with: "+bestQuestion$,5,25,chatWidth-15,20,chatWindow,3)
	bestLabel3	= CreateLabel	("and the daft twat "+bestAnswerName$+" said: "+bestAnswer$+" for "+bestPoints+" Awesome Points",5,45,chatWidth-15,20,chatWindow,3)
	
	playLabel1	= CreateLabel  ("Name",0,80,200,17,chatWindow,3)
	playLabel2	= CreateLabel	("Status",210,80,200,17,chatWindow,3)
	playLabel3	= CreateLabel	("Awesome Points",420,80,200,17,chatWindow,3)


Else
	chatWindow	= CreateWindow	("Chat Window", ClientWidth(Desktop())/2-chatWidth/2, ClientHeight(Desktop())-400, chatWidth, chatHeight, 0, 1)

	bestLabel1	= CreateLabel	("All Time best Set",5,5,200,17,chatWindow,3)
	bestLabel2	= CreateLabel	("We asked: "+bestQuestion$,5,25,chatWidth-15,20,chatWindow,3)
	bestLabel3	= CreateLabel	("and they said: "+bestAnswerName$+"): "+bestAnswer$+" for "+bestPoints+" Awesome Points",5,45,chatWidth-15,20,chatWindow,3)
	
	playLabel1	= CreateLabel  ("Name",0,80,200,17,chatWindow,3)
	playLabel2	= CreateLabel	("Status",210,80,200,17,chatWindow,3)
	playLabel3	= CreateLabel	("Awesome Points",420,80,200,17,chatWindow,3)
	

EndIf

	playerList1	= CreateListBox(0,100,200,150,chatWindow)
	playerList2 = CreateListBox(210,100,200,150,chatWindow)
	playerList3	= CreateListBox(420,100,200,150,chatWindow)
