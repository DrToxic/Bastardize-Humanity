If debug = True Then
	Print "Creating main window: "+mainWidth+", "+mainHeight
	Print "Using desktop width and height: "+ClientWidth(Desktop())+", "+ClientHeight(Desktop())
EndIf

;create the window, choose a mode, update the window.

	chatWindow	= CreateWindow	("Chat Window", ClientWidth(Desktop())/2-chatWidth/2, ClientHeight(Desktop())-400, chatWidth, chatHeight, 0, 15)

	bestLabel1	= CreateLabel	("All Time best Set",5,5,200,17,chatWindow,3)
	bestLabel2	= CreateLabel	("We asked: "+bestQuestion$,5,25,chatWidth-15,20,chatWindow,3)
	bestLabel3	= CreateLabel	("and they said: "+bestAnswerName$+"): "+bestAnswer$+" for "+bestPoints+" Awesome Points",5,45,chatWidth-15,20,chatWindow,3)
	
	playLabel1	= CreateLabel  ("Name",0,80,200,17,chatWindow,3)
Global	playLabel2	= CreateLabel	("Status",210,80,200,17,chatWindow,3)
Global	playLabel3	= CreateLabel	("Awesome Points",420,80,200,17,chatWindow,3)

Global	playerList1	= CreateListBox(0,100,200,150,chatWindow)
Global	playerList2 = CreateListBox(210,100,200,150,chatWindow)
Global	playerList3	= CreateListBox(420,100,200,150,chatWindow)



If bastardMode = 1 Then
SetGadgetText(chatWindow,"Bastard Operators from Hell")

SetGadgetText(bestLabel1,"Funniest fucker so far: Nobody..")
SetGadgetText(bestLabel2,"We got offensive with: Nothing yet...")
SetGadgetText(bestLabel3,"and the daft twat said: Nothing yet...")

EndIf