;Enable debugging to console window.
Global debug = False
If CommandLine$() = "/debug" Then debug = True

;configure the variables.
Include "includes\config loader.bb"
AppTitle "Bastard Server"

Type client
	Field uid$
	Field stream
End Type

Global received_stream
Global c.client
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
						msg_new$ = "SRVBYE"
						c.client = First client
						For c.client = Each client
							WriteLine c\stream,msg_new$
						Next
					

				Default
			End Select
		Case $4001
	End Select
If MenuChecked(svrRun) = True Then
;;;;;;;;;;;;;;;Reading messages from network;;;;;;;;;;;;;

;;;;;;;;;;;;;;;accept new client
message$ = ""
strStream = AcceptTCPStream(svrGame)
If strStream Then
	received_stream = strStream
	If ReadAvail(strStream) Then message$ = ReadLine$(strstream)
EndIf

;;;;;;;;;;;;;;;existing clients
For c.client = Each client
	If ReadAvail(c\stream) Then
		received_stream = c\stream
		the_user$ = c\uid$
		message$ = ReadLine$(c\stream)
		Exit
	EndIf
Next

If message$ <> 0 Then ;we recieved a message
	header$ = Mid$(message$,1,3)
	Select header$
		Case "UID" ;username
			the_user$ = Mid(message$,4)
			c.client = New client
			c\uid = the_user$
			c\stream = strstream
			
			WriteLine c\stream, "SRV_ID"+c\stream
			msg_new$ = the_user$+" has connected!"
			send_message(msg_new$)
		
		Case "MSG"
			msg_new$ = the_user$+","+Mid$(message$,4)
			send_message (msg_new$)
		
		Case "BYE"
			msg_new$ = the_user$+" has disconnected!"
			send_message(msg_new$)
	End Select
EndIf

EndIf
Forever


Function serverBYE()
	msg_new$ = "SRVBYE"
	If debug = True Print "Stopping server"
		c.client = First client
		For c.client = Each client
			WriteLine c\stream,msg_new$
		Next
	CloseTCPServer(svrGame)
End Function

Function send_message(msg_new$)

	If received_stream <> 0 Then
		Print msg_new$
	EndIf
	
	;notifyes the message to all the logged clients, but the sender
	c.client = First client
	For c.client = Each client
		If c\stream <> received_stream Then
			WriteLine c\stream,msg_new$
		EndIf
	Next

received_stream = 0


End Function