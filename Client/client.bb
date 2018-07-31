;=========== CLIENT CODE =======================
;TCP Client program
;by Sergio Marcello
;semar63@hotmail.com
;http://www.sergiomarcello.com

;the port which to connect to - the server and the client should communicate on the same port value
port = 40000 ;was 1024, but sometime is used by nowadays pc.

;sets the graphics to a windowed scaled mode
;Graphics 640,480,16,3
AppTitle "CHAT CLIENT"

;the channel stream that will be assigned from the server once connected.
Global client_stream

;const for keyboard space and esc keys
Const k_space = 57
Const k_esc = 1

;ask for a nickname
;client_id$ = Input$("Enter your nickname: ")
client_id$ = "testuser"
;if no nickame then end
If client_id$ = "" Then End

;attempts to open a socket (stream) on the remote server, with the default port = port
;========= ATTENTION ========================
;here the IP address should be the one of the server !!!
;right now this value is just to allow testing on a local machine. Prompt the user to
;type in the right ip address, if you want a remote connection !!!
strmClient=OpenTCPStream("127.0.0.1",port)

;checks if the stream connection was successfully estabilished
If strmClient<>0 Then 
Print "Client successfully connected"
Else
Print "Server failed to connect - quitting."
WaitKey 
End
End If





;first, write the client nickname to the server
msg$ = "UID" + client_id$
WriteLine strmClient,msg$
Print "Sent: "+msg$
Stop





;until ESC is pressed
While Not KeyDown(k_esc)

;if space bar pressed, then input a message
If KeyDown(k_space)
	
	;waits until space bar is released
	While KeyDown(k_space)
	Wend

	;ask for a message to send
	message$ = Input$("Your message: ")
			
	;writes the message on the stream channel - that is, sends a message to the server
	msg$ =	"MSG" + message$	
	WriteLine strmClient, msg$
EndIf

;listen for incoming messages

;if a message is queued then retrieves it
If ReadAvail (strmClient)  Then

	;gets the message
	message$ = ReadLine$ (strmClient)
		
	;checks if is a message from the server
	If Mid$(message$,1,3) = "SRV" Then
		
		;select the second part of the header
		what$ = Mid$(message$,4,3)
		Select what$
		
			Case "_ID"
				;the server has sent this client unique ID stream
				client_stream = Mid$(message$,7)
				;Print "client_stream = " + client_stream
			
			Case "BYE"
				;server disconnected !
			;	Color 255,0,0
				Print " - The server has disconnected - press esc key to quit - "
			;	Color 255,255,255
				
			Default
				;unknown message
				Print " - Unknown message from the server - "
		
		End Select
		
	Else
		;it's a simple broadcast message
		;prints in red color a disconnection message
		If Instr(Upper(message$),Upper("disconnected")) Then
	;		Color 255,0,0
		EndIf
		
		;shows the message	
		Print "<" + message$ + ">"
	;	Color 255,255,255
				
	EndIf
	
EndIf

;small delay
Delay 30

Wend

;logging out from server
msg$ = "ESC" + client_stream
WriteLine strmClient,msg$
Delay 30

End