;Enable debugging to console window.
Global debug = False
If CommandLine$() = "/debug" Then debug = True
;configure the variables.
Global cfgFile$ = CurrentDir$()+"Config.cfg"
Include "includes\config loader.bb"
AppTitle "Bastard Server"
Type client
	Field stream
	Field name$
	Field ready ; 0 = NOT AVAILABLE 1 = NOT READY ; 2 = READY ; 3 = SPECTATE
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

Include "includes\mainWindow.bb"
Include "includes\chatWindow.bb"

If startOnRun = 1 Then
	If svrGame <>0 Then
		SetStatusText(mainWindow,"Server is running")
		If debug = True Then Print "Server started successfully"
	Else
		SetStatusText(mainWindow,"Server failed to start, Is TCP port "+serverPort+" in use?")
		If debug = True Then Print "Server could not start on port "+serverPort+"."
	EndIf
EndIf

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
				Case playerList1
					SetStatusText(chatWindow, "Player: "+SelectedGadgetItem(playerList1)+": "+GadgetItemText$(playerList1,SelectedGadgetItem(playerList1))+" ("+GadgetItemText$(playerList2,SelectedGadgetItem(playerList1))+"): "+GadgetItemText$(playerList3,SelectedGadgetItem(playerList1))+" Awesome points")
					SelectGadgetItem(playerList2,SelectedGadgetItem(playerList1))
					SelectGadgetItem(playerList3,SelectedGadgetItem(playerList1))
				Case playerList2
					SetStatusText(chatWindow, "Player: "+SelectedGadgetItem(playerList2)+": "+GadgetItemText$(playerList1,SelectedGadgetItem(playerList2))+" ("+GadgetItemText$(playerList2,SelectedGadgetItem(playerList2))+"): "+GadgetItemText$(playerList3,SelectedGadgetItem(playerList2))+" Awesome points")
					SelectGadgetItem(playerList1,SelectedGadgetItem(playerList2))
					SelectGadgetItem(playerList3,SelectedGadgetItem(playerList2))
				Case playerList3
					SetStatusText(chatWindow, "Player: "+SelectedGadgetItem(playerList3)+": "+GadgetItemText$(playerList1,SelectedGadgetItem(playerList3))+" ("+GadgetItemText$(playerList2,SelectedGadgetItem(playerList3))+"): "+GadgetItemText$(playerList3,SelectedGadgetItem(playerList3))+" Awesome points")
					SelectGadgetItem(playerList1,SelectedGadgetItem(playerList3))
					SelectGadgetItem(playerList2,SelectedGadgetItem(playerList3))
				Default
			End Select

		Case $803 ; window close button
			Select EventSource()
				Default 
					If svrGame <>0 Then serverBYE()
					If MenuChecked(save) Then saveCFG()
					Exit
			End Select

		Case $1001 ; menu items
			Select EventData()

				Case 1
					If MenuChecked(svrRun) = False Then
						svrGame = CreateTCPServer(serverPort)
						If svrGame <>0 Then
							SetStatusText(mainWindow,"Server is running")
							CheckMenu(svrRun)
							If debug = True Then Print "Server started successfully"
						Else
							SetStatusText(mainWindow,"Server failed to start, Is TCP port "+serverPort+" in use?")
							If debug = True Then Print "Server could not start on port "+serverPort+"."
						EndIf
					ElseIf MenuChecked(svrRun) = True Then
						serverBYE()
						SetStatusText(mainWindow,"Server is Stopped")
						UncheckMenu(svrRun)
					EndIf
					UpdateWindowMenu(mainWindow)

				Case 2
					If MenuChecked(save) = True Then
						If FileType(cfgFile$) = 1 Then
							Select Proceed("Disabling configuration, Would you also like to delete it?",1)
								Case -1
									If debug = True Then Print "Erm... okay. Keeping persistance file enabled."
								Case 0
									CopyFile(cfgFile$,CurrentDir$()+"Config.old.cfg")
									DeleteFile(cfgFile$)
									If debug = True Then Print "Persistance file renamed."+Chr$(13)+"New file name: "+CurrentDir$()+"Config.old.cfg"
									UncheckMenu(save)
								Case 1
									If FileType(cfgFile$) = 1 Then DeleteFile(cfgFile$)
									If FileType(cfgFile$) = 1 Then
										Notify "Could not delete file."+Chr$(13)+cfgFile$
										If debug = True Then Print "could not delete: "+cfgFile$
									ElseIf FileType(cfgFile$) = 0 Then
										If debug = True Then Print "Persistance disabled and deleted"
									EndIf
									UncheckMenu(save)
							End Select
						EndIf
					Else
						Select Proceed("Enabling persistance, Would you like to check for an existing file?"+Chr$(13)+"This will save a .cfg file in the server directory.",0) 

							Case 0
								saveCFG()
								CheckMenu(save)
							Case 1
								If FileType(CurrentDir$()+"Config.old.cfg") = 1 Then
									CopyFile(CurrentDir$()+"Config.old.cfg",cfgFile$)
									DeleteFile(CurrentDir$()+"Config.old.cfg")
								Else
									Notify "Old config not found. Writing new config."
									saveCFG()
								EndIf
							CheckMenu(save)
							If debug = True Then Print "Persistance enabled!"
						End Select
					EndIf
					UpdateWindowMenu(mainWindow)

				Default
			End Select
		Case $4001
			Default
	End Select



If MenuChecked(svrRun) = True Then
;;;;;;;;;;;;;;;Reading messages from network;;;;;;;;;;;;;
	message$ = "" ;prepare to get a message


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
		header$ = Left$(message$,3)			;Find out what to do with it using the 3 character header code

		Select header$
			Case "UID" ;we receive a Client ID
				clientFound = False
				c.client = First client
				For c.client = Each client
					If Mid$(message$,4) = c\name$ Then ;client exists. Set their new StreamID, Set them to "not ready" and continue.
						c\stream = strstream
						c\ready = 1
						clientFound = True
						If debug = True Then Print "Client has returned!"
					EndIf
				Next
				If clientFound = False Then ;the client doesn't yet exist. make one.
					If debug = True Then Print "Creating new client (Received UID: "+Mid$(message$,4)+")"
	
					the_user$ = Mid$(message$,4)
					c.client = New client
					c\name$ = the_user$ : c\stream = strstream : c\ready = 1
					WriteLine c\stream, "SVR_ID" + c\stream
				EndIf
				updatePlayers(c.client)


			Case "MSG"
				msg_new$ = the_user$+","+Mid$(message$,4)
				send_message (msg_new$)

			Case "PLY" ; A game Control message.
			

			Case "BYE"
				If debug = True Then Print the_user$+" has disconnected. Disabling client..."
				msg_new$ = the_user$+" has disconnected!"
				c.client = First client
					For c.client = Each client
						If c\stream = received_stream Then c\ready = 0
					Next
				send_message("SVRDIS"+the_user$)
				updatePlayers(c.client)
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

;	Notify CountGadgetItems(playerList)

	If CountGadgetItems(playerList1) > 0 Then ClearGadgetItems(playerList1)
	If CountGadgetItems(playerList2) > 0 Then ClearGadgetItems(playerList2)
	If CountGadgetItems(playerList3) > 0 Then ClearGadgetItems(playerList3)
	Dim player$(players+5)

	c.client = First client
	players = 0
	For c.client = Each client
		AddGadgetItem(playerList1,c\name$)
		Select c\ready
			Case 0
				AddGadgetItem(playerList2,"Disconnected")
			Case 1
				AddGadgetItem(playerList2,"Not Ready")
			Case 2
				AddGadgetItem(playerList2,"Ready")
			Case 3
				AddGadgetItem(playerList2,"Spectating")
		End Select
		AddGadgetItem(playerList3,c\points%)
		player$(players) = c\ready+c\name$
		players = players + 1
	Next


	If debug = True Then Print "Processed: "+ player$(players)

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

Function saveCFG()

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
End Function