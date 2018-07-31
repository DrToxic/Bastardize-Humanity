Print "Looking for Questions..."
qCards = ReadDir("Questions")
i = 0
NextFile(qCards)
NextFile(qCards)
While MoreFiles(qCards)
	i = i + 1
	Print "> "+NextFile(qCards)
Wend
Print""
Print "Found "+i+" files!"
Dim qFiles(i)
NextFile(qCards)
NextFile(qCards)

For n = 1 To i
	qFiles(i) = NextFile(qCards)
Next
Print ""
Print "Looking for Answers..."
aCards = ReadDir("Answers")
i = 0
NextFile(aCards)
NextFile(aCards)
While MoreFiles(aCards)
	i = i + 1
	Print "> "+NextFile(aCards)
Wend
Print ""
Print "Found "+i+" files!"
Dim aFiles(i)
NextFile(aCards)
NextFile(aCards)

For n = 1 To i
	aFiles(i) = NextFile(aCards)
Next

AppTitle "CHAT SERVER"

Global received_stream
Const k_space = 57
Const k_esc = 1

;creates a type of client with two items: client name, and client ID stream
Type client
	Field uid$
	Field stream
End Type

;declare a global type variable for the client
Global c.client

;sets the port where to listen from, and where the client can connect to.
port = 40000 ;was 1024, but sometime is used by nowadays pc.


;Create a server and listen for client connections
svrGame=CreateTCPServer(port)

;checks if the server was successfully created; if not, ends the program
If svrGame<>0 Then 
Print "Server started successfully - listening on port " + port
Else
Print "Server failed to start on port " + port
End
End If

;=====================================================================

;Main loop until ESC is pressed
While Not KeyDown(k_esc)
message$ = ""

;checks if there is a NEW client connection
strStream=AcceptTCPStream(svrGame)

If strStream Then ;a new client has joined the chat; his ID stream is strStream

;memorize the current connection stream
received_stream = strStream

;reads info from the client just connected (see tcp_client.bb)
If ReadAvail(strStream) Then
message$ =  ReadLine$(strStream)
EndIf

Else 

;there are NOT new connections; so checks if there are incoming data from clients
For c.client = Each client
If ReadAvail(c\stream) Then
	;Print "incoming message from client: " + c\UID
	received_stream = c\stream
	the_user$ = c\uid$
	message$ =  ReadLine$(c\stream)
	Exit
EndIf
Next


EndIf


If message$ <> "" Then ;a message has arrived, or a new client has joined the chat

	;processes the data from the client:

	;checks the header of the message
	header$ = Mid$(message$,1,3)
	
	;tests the header
	Select 	header$
	
	Case "UID" ;it's a NEW client connection
		
		;extracts the user ID
		the_user$ = Mid$(message$,4)
		
		;creates a new client type structure and memorize userid and stream number
		c.client = New client
		c\uid = the_user$
		c\stream = strstream
		
		;notifyes to the client his stream number
		WriteLine c\stream, "SRV_ID" + c\stream
				
		;broadcasts the new client connection
		msg_new$ = "<" + the_user$ + "> connected !"
		notify_message(msg_new$)
		
	Case "MSG" ;it's a message from a client
		

		;broadcasts the user message
		msg_new$ = "<" + the_user$ + ">  " + Mid$(message,4)
		notify_message(msg_new$)
		
	Case "ESC";a client has disconnected from the chat
	
		;extracts the client ID stream that was sent from the client in the message
		client_stream = Mid(message$,4)
	
		;locate the client name from the list of the client
		For c.client = Each client
			
			If c\stream = client_stream Then
				
				;retrieve the client id
				the_user$ = c\uid	
				
				;delete the client
				Delete c
				
				;exit for
				Exit
			EndIf
		Next

		;broadcasts the message
		msg_new$ = "<" + the_user$ + ">  Disconnected"
		notify_message(msg_new$)
		
	End Select

	;small pause
;	Delay 20
	
EndIf

;if space bar pressed, then input a message
If KeyDown(k_space)
	
	;waits until space bar is released
	While KeyDown(k_space)
	Wend

	;ask for a message to send
	message$ = Input$("Your message: ")
			
	;prepare the message to be sent
	msg_new$ =	"<server> " + message$
	
	;broadcasts the message
	notify_message(msg_new$)
	
EndIf



Wend

;quitting
;sends a logoff message to all the client
notify_message("SRVBYE")
Delay 20

End

;===============================
Function notify_message(msg_new$)
;===============================

;notifyes the message on the server itself

;shows only client messages
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