If CommandLine$() = "/debug" Then debug = True 
Include "includes\config loader.bb"

Include "includes\mainWindow.bb"
Include "includes\chatWindow.bb"

timer = CreateTimer(25)


Repeat
	Select WaitEvent()
		Case $101 ; keydown
			Select EventData()
				Case 59
					Notify "help"
				Default
			End Select
		Case $401
			Select EventSource() ;buttons
				Default
			End Select
		Case $803 ; window close button
			Select EventSource()
				Default
					If MenuChecked(connect) = True Then
						WriteLine strmClient,"BYE"
						CloseTCPStream(strmClient)
					EndIf
					Exit
			End Select
		Case $1001 ; menu items from includes\windows.bb
			Select EventData()
				Case 1 ;"Connect to Server"
						If MenuChecked(connect) = False Then
							If debug = True Then Print "Attempting to connect to server: "+serverIP$+":"+serverPort%
							strmClient = OpenTCPStream(serverIP$,serverPort%)

							If strmClient <>0 Then
								SetStatusText(mainWindow,"Connected to server!")
;								AddGadgetItem(playerList,playerName)
								If debug = True Print "Connection established."
								CheckMenu(connect) : UpdateWindowMenu(mainWindow)
								WriteLine strmClient,"UID"+playername$
								If debug = True Print "Sent playername: "+playername$
							Else
								SetStatusText(mainWindow,"Failed to connect to server.")
								If debug = True Print "Failed to connect to server."
							EndIf
						Else
							If debug = True Then Print "Closing connection to server"
							WriteLine strmClient,"BYE"
							CloseTCPStream(strmClient)
							UncheckMenu(connect) : UpdateWindowMenu(mainWindow)
						EndIf
				Case 2 ;"Set server IP and Port"
				Case 3 ;"Disconnect from server"
				Case 4 ;"Disconnect and Quit"
					Exit
				Default
			End Select
	End Select
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;Network code
If MenuChecked(connect) Then
	If ReadAvail (strmClient)  Then
		message$ = ReadLine$ (strmClient)

		codeOne$ = Upper(Mid$(message$,1,3))
		codeTwo$ = Upper(Mid$(message$,4,3))
		
		If debug = True Then Print "Received a message. "+message$
		
		;checks if is a message from the server
		Select codeOne$
			Case "SVR"
				If debug = True Then Print "Got SerVeR code."+codeOne$
			;select the second part of the header
				Select codeTwo$
				
					Case "_ID"
						;our unique ID for the session.
						client_stream = Mid$(message$,7)
						If debug = True Then Print "Got client_stream: " + client_stream
					
					Case "BYE"
						;server disconnected !
						CloseTCPStream(strmClient)
						UncheckMenu(connect) : UpdateWindowMenu (mainWindow)
						SetStatusText(mainWindow, "Server closed connection - Disconnected")
						Notify "The server has disconnected: "+Mid$(message$,7)

					Case "NEW"
						If debug = True Then Print "Creating player list. "+Mid$(message$,8)
						Select Mid$(message$,7,1)
							Case 1
								AddGadgetItem(playerList1,Mid$(message$,8))
							Case 2
								AddGadgetItem(playerList2,Mid$(message$,8))
							Case 3
								AddGadgetItem(playerList3,Mid$(message$,8))
						End Select
	
					Case "CLR"
						If debug = True Then Print "Clearing player list. Receiving new list!"
						If CountGadgetItems(playerList1) > 0 Then ClearGadgetItems(playerList1)
						If CountGadgetItems(playerList2) > 0 Then ClearGadgetItems(playerList2)
						If CountGadgetItems(playerList3) > 0 Then ClearGadgetItems(playerList3)

					Case "DIS"
						If debug = True Then Print Mid$(message$,7)+" has disconnected from the session."
					Default
				End Select

			Case "MSG"
				Print "<" + Mid$(4,message$) + ">"
					;Default If debug = True Then Print " - Unknown message from the server - "
		End Select
			
		Else
			;shows the message	
					
	EndIf
	
EndIf


Forever