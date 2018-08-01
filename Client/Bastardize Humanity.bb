If CommandLine$() = "/debug" Then debug = True
Include "includes\config loader.bb"
Include "includes\windows menus.bb"
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
					If strmClient<>0 Then
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
		If debug = True Then Print "RECEIVED MESSAGE: "+message$
			
		;checks if is a message from the server
		If Mid$(message$,1,3) = "SRV" Then
			
			;select the second part of the header
			what$ = Mid$(message$,4,3)
			Select what$
			
				Case "_ID"
					;our unique ID for the session.
					client_stream = Mid$(message$,7)
					If debug = True Then Print "client_stream = " + client_stream
				
				Case "BYE"
					;server disconnected !
					Notify "The server has disconnected: "+Mid$(message$,7)
					UncheckMenu(connect) : UpdateWindowMenu (mainWindow)
					SetStatusText(mainWindow, "Server closed connection - Disconnected")
					CloseTCPStream(strmClient)
					Default Print " - Unknown message from the server - "
			
			End Select
			
		Else
	
			;shows the message	
			Print "<" + message$ + ">"
					
		EndIf
		
	EndIf
EndIf

Forever