;Enable debugging to console window.
Global debug = False
If CommandLine$() = "/debug" Then debug = True

;configure the variables.
Include "includes\config loader.bb"
AppTitle "Bastard Server"
Type client
	Field uid$
	Field stream
	Field name$
	Field ready ; -1 = NOT AVAILABLE 0 = NOT READY ; 1 = READY ; 2 = SPECTATE
	Field points% ; Cards Against Humanity: Awesome Points
	Field card0$ ; player cards.
	Field card1$ ; Keeping these stored server side.
	Field card2$
	Field card3$
	Field card4$
	Field card5$
	Field card6$
	Field card7$
	Field card8$
	Field card9$
	Field cardA$
	Field cardB$
	Field cardC$
	Field cardD$
	Field CardE$ ; Yes, up to 16 cards. unless I read the rules badly wrong...
	Field cardF$
End Type

	Dim player$(32)

Global players = 0
Dim player$(players)
Global received_stream
Global c.client

;Make the AI player. Officially "Rando Cardrissian".
Dim aiCards$(4)
randoScore = 0
;We'll use him to play if people wish for it.

timer = CreateTimer(25)

Include "includes\windows menus.bb"

Repeat
	Select WaitEvent()
		Case $101 ; keydown
			Select EventData()
				Case 59 ;F1
					Notify "help"
				Default
			End Select

		Case $401
			Select EventSource() ;buttons
				Case playerList
					SetStatusText(mainWindow, SelectedGadgetItem(playerList)+": "+GadgetItemText$(playerList,SelectedGadgetItem(playerList)))
				Default
			End Select

		Case $803 ; window close button
			Select EventSource()
				Default 
					If svrGame <>0 Then serverBYE()
					Exit
			End Select

		Case $1001 ; menu items from includes\windows.bb
			Select EventData()

				Case 1
					If MenuChecked(svrRun) = False Then
						If debug = True Print "Attempt to start server"
						svrGame = CreateTCPServer(serverPort)
						If svrGame <>0 Then
							SetStatusText(mainWindow,"Server is running")
						EndIf
						CheckMenu(svrRun)
					ElseIf MenuChecked(svrRun) = True Then
						serverBYE()
						SetStatusText(mainWindow,"Server is Stopped")
						UncheckMenu(svrRun)
					EndIf
					UpdateWindowMenu(mainWindow)

				Case 2 
						c.client = First client
						For c.client = Each client
							WriteLine c\stream,"SVRBYE"
						Next


				Default
			End Select
		Case $4001
			Default
	End Select



If MenuChecked(svrRun) = True Then
;;;;;;;;;;;;;;;Reading messages from network;;;;;;;;;;;;;
	message$ = "" ;prepare to get a message
;	received_stream = ""
	
	
;;;;;;;;;;;;;;;Accept new connection
	strStream = AcceptTCPStream(svrGame)
	If strStream Then
		received_stream = strStream
		;reads info from the client just connected (see tcp_client.bb)
		If ReadAvail(strStream) Then message$ = ReadLine$(strstream)
	EndIf
	
	
	;;;;;;;;;;;Existing clients
	For c.client = Each client
		If ReadAvail(c\stream) Then
			received_stream = c\stream		;We got this message
			the_user$ = c\name$				;Message was sent by
			message$ = ReadLine$(c\stream)	;The message contents
			Exit
		EndIf
	Next
	
	If message$ <> 0 Then					;If we recieved a message
		header$ = Mid$(message$,1,3)		;Find out what to do with it using the 3 character header code
	
		Select header$
			Case "UID" ;we receive a username
;			Stop
			players = 0
				c.client = First client
				For c.client = Each client
					If debug = True Then Print "Checking client "+players+" for details..."
					If Not c\stream = 0 Then
						If debug = True Then Print "Client in use. checking for next...
						players = players + 1
					EndIf
				Next
				If debug = True Then Print "Creating new client (Received UID: "+Mid(message$,4)+")"
				the_user$ = Mid(message$,4)
				c.client = New client
				c\name$ = the_user$ : c\stream = strstream : c\ready = 0
				WriteLine c\stream, "SVR_ID" + c\stream
				updatePlayers(c.client)

			Case "MSG"
				msg_new$ = the_user$+","+Mid$(message$,4)
				send_message (msg_new$)

			Case "PLY" ; A game Control message.
				

			Case "BYE"
				msg_new$ = the_user$+" has disconnected!"
				send_message(msg_new$)
			Default
				;debug = True Then Print "Received nonsense. What is "+header$+"?"
		End Select
	EndIf

EndIf 
Forever


Function serverBYE()

	If debug = True Print "Stopping server"
	c.client = First client

	For c.client = Each client
		WriteLine c\stream,"SVRBYE"
	Next

	CloseTCPServer(svrGame)

End Function

Function send_message(msg_new$)

	If received_stream <> 0 Then

	EndIf
	
	;notifyes the message to all the logged clients, but the sender
	c.client = First client

	For c.client = Each client
		If c\stream <> received_stream Then
			WriteLine c\stream,msg_new$
		EndIf
	Next


End Function

Function updatePlayers(c.client)

Notify CountGadgetItems(playerList)

While Not CountGadgetItems(playerList) = 0

	RemoveGadgetItem(playerList,0)

Wend

	Dim player$(players+5)

	c.client = First client
	players = 0
	For c.client = Each client
		player$(players) = c\name$
		AddGadgetItem(playerList,c\name$)
		If debug = True Then Print "Processed: "+ player$(players)
		players = players + 1
	Next

	If debug = True Then
		Print "List of players:"
		For i = 0 To players
			Print player$(i)
		Next
	EndIf
	
	c.client = First client

	For c.client = Each client
		WriteLine c\stream,"SVRCLR"

		For i = 0 To players-1
			WriteLine c\stream,"SVRNEW"+player$(i)
		Next
	Next

End Function