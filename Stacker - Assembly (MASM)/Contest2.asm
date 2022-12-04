.386
.model flat, stdcall
.stack 4096
ExitProcess proto, dwExitCode:dword
INCLUDE Irvine32.inc

.data
numberBlocks DWORD 3; current number of blocks in the row
numBlocksMult DWORD 3; current number of blocks to be multiplied by 4
ypos DWORD 34; current y position
currXPos DWORD 54; current x position
printXPos DWORD 54; "Fake" x position for drawing boxes.Should be same as currXPos as start
printOffset SDWORD 0; offset used for box printing
prevXPos DWORD 54; x position of the previous row
firstXPos DWORD ?
xmin DWORD 52; minimum x position - edge of gameboard
maxLength DWORD 7; width of the game board
delayVal DWORD  160; delay value - affects speed of game
countOuter DWORD ? ; storage for loop counter
count DWORD ? ; storage for loop counter
winner DWORD ? ; 0 if player lost, 1 if player wins
MAX = 15; max chars to read
playerName BYTE MAX + 1 DUP(? ); stores player name
playerNameLength DWORD ?
level BYTE 1
levelCMP BYTE ? ;use for comparison so it can be set to 0 after comparison
levelMsg BYTE "Level ", 0
leaderboardMsg BYTE "Leaderboard",0
leaderboardHeader BYTE "Name", 11 DUP(" "), "Score", 0
leaderboadEmpty BYTE 30 DUP(" "),0
rulesMsg BYTE "Press the SPACE BAR to stop the blocks, reach the top to win!", 0
nameMsg BYTE "Please enter your name: ", 0
lostMSG BYTE "You made it to level ", 0
wonMSG BYTE "You Won level ", 0
winLine BYTE  10 DUP(" "), "WINNER!", 0
line BYTE 28 DUP("="), 0
newHighScore BYTE 0
stacker1 BYTE " _______ _________ _______  _______  _        _______  _______", 0Ah, 28 DUP(" "),
"(  ____ \\__   __/(  ___  )(  ____ \| \    /\(  ____ \(  ____ )", 0Ah, 28 DUP(" "),
"| (    \/   ) (   | (   ) || (    \/|  \  / /| (    \/| (    )|", 0Ah, 28 DUP(" "),
"| (_____    | |   | (___) || |      |  (_/ / | (__    | (____)|", 0Ah, 28 DUP(" "), 0
stacker2 BYTE "(_____  )   | |   |  ___  || |      |   _ (  |  __)   |     __)", 0Ah, 28 DUP(" "),
"      ) |   | |   | (   ) || |      |  ( \ \ | (      | (\ (   ", 0Ah, 28 DUP(" "),
"/\____) |   | |   | )   ( || (____/\|  /  \ \| (____/\| ) \ \__", 0Ah, 28 DUP(" "),
"\_______)   )_(   |/     \|(_______/|_/    \/(_______/|/   \__/", 0
gameover1 BYTE " _______  _______  _______  _______  _______           _______  _______", 0Ah, 24 DUP(" "),
		     "(  ____ \(  ___  )(       )(  ____ \(  ___  )|\     /|(  ____ \(  ____ )", 0Ah, 24 DUP(" "),
			"| (    \/| (   ) || () () || (    \/| (   ) || )   ( || (    \/| (    )|", 0Ah, 24 DUP(" "),
			"| |      | (___) || || || || (__    | |   | || |   | || (__    | (____)|", 0Ah, 24 DUP(" "), 0
gameover2	BYTE	"| | ____ |  ___  || |(_)| ||  __)   | |   | |( (   ) )|  __)   |     __)", 0Ah, 24 DUP(" "),
			"| | \_  )| (   ) || |   | || (      | |   | | \ \_/ / | (      | (\ (   ", 0Ah, 24 DUP(" "),
			"| (___) || )   ( || )   ( || (____/\| (___) |  \   /  | (____/\| ) \ \__", 0Ah, 24 DUP(" "),
			"(_______)|/     \||/     \|(_______/(_______)   \_/   (_______/|/   \__/", 0
highscore1 BYTE "         _________ _______           _______  _______  _______  _______  _______ ", 0Ah, 19 DUP(" "),
			"|\     /|\__   __/(  ____ \|\     /|(  ____ \(  ____ \(  ___  )(  ____ )(  ____ \", 0Ah, 19 DUP(" "),
			"| )   ( |   ) (   | (    \/| )   ( || (    \/| (    \/| (   ) || (    )|| (    \/", 0Ah, 19 DUP(" "),
			"| (___) |   | |   | |      | (___) || (_____ | |      | |   | || (____)|| (__    ", 0Ah, 19 DUP(" "), 0
highscore2 BYTE "|  ___  |   | |   | | ____ |  ___  |(_____  )| |      | |   | ||     __)|  __)   ", 0Ah, 19 DUP(" "),
			"| (   ) |   | |   | | \_  )| (   ) |      ) || |      | |   | || (\ (   | (      ", 0Ah, 19 DUP(" "),
			"| )   ( |___) (___| (___) || )   ( |/\____) || (____/\| (___) || ) \ \__| (____/\", 0Ah, 19 DUP(" "),
			"|/     \|\_______/(_______)|/     \|\_______)(_______/(_______)|/   \__/(_______/", 0
creditsText BYTE "BY ALEXIS NAGLE, JOHN SELLOCK", 0
borderWarning BYTE "[Adjust top window border to fit fully with this row on the bottom!] ", 0
fileName BYTE "Stacker.txt", 0
scoreFile BYTE "Scores.txt", 0
BUFSIZE = 5000
buffer BYTE BUFSIZE DUP(?)
scoreBuffer BYTE BUFSIZE DUP(?)
bytesRead DWORD ?
fileHandle HANDLE ?
scoredata BYTE 10 DUP(15 DUP(?)), 0
finalScoreData BYTE 10 DUP(15 DUP(?)), 0
prevIndex DWORD 0
offsetIndex DWORD 0
outFileBufferSize DWORD 0
index BYTE 0
indexArray BYTE 10 DUP(?)
temp BYTE ?

consoleHandle DWORD ?
cursorInfo CONSOLE_CURSOR_INFO <10, FALSE>
multipliedBlockNum DWORD 3; The number of blocks multiplied by 4 (for graphical display)

.code
main PROC
INVOKE GetStdHandle, STD_OUTPUT_HANDLE
mov consoleHandle, eax
INVOKE SetConsoleCursorInfo, consoleHandle, ADDR cursorInfo; pass a pointer to CONSOLE_CURSOR_INFO structure

.IF level == 1
	call WelcomeScreen   ; Displays Title Screen, only if on first leveln
	; open fileand read for leaderboard
	lea EDX, scoreFile
	call openInputFile
	mov fileHandle, eax
	call readScoreFile
	mov eax, fileHandle
	call CloseFile
.ENDIF

call BeginTransitionAnimation
call ScreenDisplaySetup
call writeScores; writes leaderboard to the screen

jmp FirstLine; Print base row for game to begin


; NewLine: Make additional row
NewLine::
dec ypos; decrease the y position to move up to next level
dec ypos
sub delayVal, 5; decrease the delay to speed up the game

FirstLine :
call clearLine
call Print; print the next level

; MoveBlocks: move the row of blocks leftand right
MoveBlocks :

; Calculate number of movements needed to the right
mov ecx, maxLength
add ecx, xmin
sub ecx, numberBlocks
sub ecx, currXPos

; for some reason if ecx is 0 it still executes the following loop "Move Right" and infinite number of times
.IF ecx == 0
	jmp skipRightMoves
.ENDIF

; Move the row to the right
MoveRight :
	call readKeyPress; check for key press
	mov count, ecx; Could replace this with push instruction
	add currXPos, 1; increase x position
	add printOffset, 4
	call Print; print blocks at new x position
	mov ecx, count; Could replace this with pop instruction
	loop MoveRight

SkipRightMoves :

; Calculate number of movements needed to the left
mov ecx, maxLength
sub ecx, numberBlocks

; Move the row to the left
MoveLeft :
	call readKeyPress; check for key press
	mov count, ecx
	sub currXPos, 1; decrease x position
	sub printOffset, 4
	call Print; print blocks at new x position
	mov ecx, count
	loop MoveLeft

jmp MoveBlocks

invoke ExitProcess, 0
main ENDP

; ---------------------------------------------------- -
WelcomeScreen PROC
;
; Displays Welcome Screenand allows the player to pick a difficulty
; Recieves:
; Returns:
; ---------------------------------------------------- -
; set color
mov eax, white + (lightBlue * 16)
call SetTextColor
call Clrscr

; print stacker heading
mov dl, 28
mov dh, 9
call Gotoxy
mov edx, OFFSET stacker1
call WriteString
mov edx, OFFSET stacker2
call WriteString


; Warns the player to adjust their window size to fit the game
mov dl, 28
mov dh, 41
call Gotoxy
mov edx, OFFSET borderWarning
call WriteString


; print name input message
mov dl, 28
mov dh, 19
call Gotoxy
mov edx, OFFSET nameMsg
call WriteString
mov  edx, OFFSET playerName
mov  ecx, MAX
call ReadString
mov playerNameLength, eax



ret
WelcomeScreen ENDP

; ---------------------------------------------------- -
BeginTransitionAnimation PROC
;
; Displays the animation that plays before game loads
; Recieves:
; Returns:
; ---------------------------------------------------- -

mov dh, 0		; row
mov dl, 0		; column

mov eax, black + (black * 16)
.IF numberBlocks < 1
	.IF newHighscore == 1
		mov eax, green + (green * 16)
	.ELSE
		mov eax, red + (red * 16)
	.ENDIF
.ENDIF


call SetTextColor
mov al, " "

mov ecx, 60					; Printing 75 columns of 2 characters width
animationXLoop:
	call Gotoxy
	mov bl, dl				; Store a copy of the X for animationLeftXLoop
	push ecx					; Save animationRightXLoop counter
	mov ecx, 21				; Printing a column 15 characters tall
	animationRightYLoop:
		call Gotoxy
		call WriteChar
		call WriteChar
		inc dh
	loop animationRightYLoop
	push edx

	mov dl, 118
	sub dl, bl; Calculates correct rightward horizontal position of this column
	mov ecx, 21
	animationLeftYLoop:
		call Gotoxy
		call WriteChar
		call WriteChar
		inc dh
	loop animationLeftYLoop
	pop edx

	pop ecx					; Restore animationRightXLoop counter
	
	add dl, 2					; Move to next column
	mov dh, 0

	mov eax, 20				; Delay printing of next column for visual clarity
	call Delay

loop animationXLoop


.IF numberBlocks < 1
	.IF newHighscore == 1
		mov eax, white + (green * 16)
		call SetTextColor
		; print gameover screen
		call clrscr
		mov dl, 19
		mov dh, 9
		call Gotoxy
		mov edx, OFFSET highscore1
		call WriteString
		mov edx, OFFSET highscore2
		call WriteString
	.ELSE
		mov eax, white + (red * 16)
		call SetTextColor
		; print gameover screen
		call clrscr
		mov dl, 24
		mov dh, 9
		call Gotoxy
		mov edx, OFFSET gameover1
		call WriteString
		mov edx, OFFSET gameover2
		call WriteString
	.ENDIF
.ELSE
	mov eax, white + (black * 16)
	call SetTextColor
	; print stacker heading
	call clrscr
	mov dl, 28
	mov dh, 9
	call Gotoxy
	mov edx, OFFSET stacker1
	call WriteString
	mov edx, OFFSET stacker2
	call WriteString

	mov dl, 45
	mov dh, 20
	call Gotoxy
	mov edx, OFFSET creditsText

	call WriteString
	mov eax, 1200
	call Delay

	call Clrscr; Clear any leftover blue screen characters
.ENDIF

ret
BeginTransitionAnimation ENDP

; ---------------------------------------------------- -
ScreenDisplaySetup PROC
;
; Stes up display screen including gameboard outlineand sidebars
; Recieves:
; Returns:
; ---------------------------------------------------- -
mov eax, white + (black * 16)
call SetTextColor
;call Clrscr

; Print Sidebar -- stored in file at fileName
mov EDX, OFFSET fileName
call openInputFile
mov fileHandle, eax
mov  eax, fileHandle
mov  edx, OFFSET buffer
mov  ecx, BUFSIZE
call ReadFromFile
mov  bytesRead, eax
mov eax, fileHandle
call CloseFile
mov eax, lightblue + (black * 16)
call SetTextColor
mov dh, 0
mov dl, 0
call Gotoxy
mov  edx, OFFSET buffer
call WriteString

; Outline Gameboard with gray blocks
mov eax, lightgray + (lightgray * 16)
call SetTextColor
mov al, " "
mov dh, 4
.WHILE dh <= 35
mov dl, 45
call Gotoxy
call WriteChar
.IF dh == 4
mov ecx, 28
l1:
call WriteChar
loop l1
.ELSEIF dh == 35
mov ecx, 28
l2 :
	call WriteChar
	loop l2
	.ENDIF
	mov dl, 74
	call Gotoxy
	call WriteChar

	inc dh
	.ENDW

	; print  rules
	mov eax, white + (black * 16)
	call SetTextColor
	mov dl, 31
	mov dh, 0
	call Gotoxy
	mov edx, OFFSET rulesMsg
	call WriteString

	; print level
	mov dl, 56
	mov dh, 2
	call Gotoxy
	mov edx, OFFSET levelMsg
	call WriteString
	movzx ax, level
	call WriteDec

	; Print win Line
	mov dl, 46
	mov dh, 5
	call Gotoxy
	mov edx, OFFSET winLine
	call WriteString
	mov dl, 46
	mov dh, 6
	call Gotoxy
	mov edx, OFFSET line
	call WriteString

	ret
	ScreenDisplaySetup ENDP

; ---------------------------------------------------- -
  Print PROC
;
; Print out blocks onto the current row
; Recieves:
; Returns:
; ---------------------------------------------------- -

mov ebx, numberBlocks
mov eax, 4
mul ebx
mov numBlocksMult, eax; Calculates number of display "blocks" in a row

mov dh, BYTE PTR ypos
mov dl, BYTE PTR printXPos

add dl, BYTE PTR printOffset; Makes the row display left or right by 4.

call Gotoxy

; ---------- -
; Prints bottom layer of each set of blocks
; ---------- -

; Clear out 4 spaces before the row of blocks
sub dl, 4
call Gotoxy
mov al, " "
mov ecx, 4
loopXBefore:
call WriteChar
loop loopXBefore

; Print out blocks in the row
mov    eax, black + (lightBlue * 16)
call    SetTextColor; set color for the blocks
mov al, "_"
mov ecx, numBlocksMult; ***Should be number of blocks * 4
L1:; Loop for specified number of blocks
call WriteChar; Print a block
loop L1

; Clear out 4 spaces after the row of blocks
mov    eax, white + (black * 16)
call    SetTextColor; set color back to white on black
mov al, " "
mov ecx, 4
loopXAfter:
call WriteChar
loop loopXAfter

; ---------- -
; Prints top layer of each set of blocks
; ---------- -

dec dh
; Clear out 4 spaces before the row of blocks
call Gotoxy
mov al, " "
mov ecx, 4

loopXBeforeRow2:
call WriteChar
loop loopXBeforeRow2

mov    eax, white + (lightBlue * 16)
call    SetTextColor; set color for the blocks
mov al, " "
mov ecx, numBlocksMult
L2 :
call WriteChar
loop L2

; Clear out 4 spaces after the row of blocks
mov    eax, white + (black * 16)
call    SetTextColor; set color back to white on black
mov al, " "
mov ecx, 4
loopXAfterRow2:
call WriteChar
loop loopXAfterRow2

inc dh

mov eax, lightgray + (lightgray * 16)
call SetTextColor;
mov ecx, 2
redrawBoundry:
mov dl, 45
call Gotoxy
call WriteChar
mov dl, 74
call Gotoxy
call WriteChar
dec dh
loop redrawBoundry
mov eax, white + (black * 16)
call SetTextColor;

mov eax, delayVal
call Delay; delay program to give the user a chance to react


ret
Print ENDP

; ---------------------------------------------------- -
ClearLine PROC
;
; Clear the current row of the gameboard
; Recieves:
; Returns:
; ---------------------------------------------------- -
mov dh, BYTE PTR ypos
mov dl, BYTE PTR xmin
sub dl, 8
call Gotoxy; Go to the first position of the gameboard in the row
mov al, " "
mov ecx, maxLength
add ecx, 24
L1 :; Loop for the width of the gameboard
call WriteChar; Replace the character with a space
loop L1

; Clears top half of blocks
dec dh
call Gotoxy
mov ecx, maxLength
add ecx, 24
L2 :
call WriteChar
loop L2
inc dh

ret
ClearLine ENDP

; ---------------------------------------------------- -
readKeyPress PROC
;
; Check if a key, the spacebar, is pressed
; Recieves:
; Returns:
; ---------------------------------------------------- -
call ReadKey; read in key pressed
.IF al == 020h; check if the key matches a press to the space bar
call stopBlocks; since the space bar was pressed, stop the blocks from moving
.ENDIF
ret
readKeyPress ENDP

; ---------------------------------------------------- -
stopBlocks PROC
;
; Stop the blocks from moving, Update variables with positionsand reprint line accordingly
; Recieves:
; Returns:
; ---------------------------------------------------- -
.IF ypos >= 34; if stopping the first row, keep where its at an adjust values accordingly
mov eax, currXPos
mov prevXPos, eax
mov firstXPos, eax
.IF eax < 54
	sub eax, 54

	mov ebx, 4
	mul ebx
	add printXPos, eax
	.ELSE
	mov ebx, 54
	sub ebx, eax
	mov eax, ebx

	mov ebx, 4
	mul ebx
	sub printXPos, eax
	.ENDIF
	.ENDIF

	; calculate: numberBlocks = numberBlocks - abs(prevXPos - currXPos)
	mov eax, prevXPos
	.IF eax > currXPos
	mov ebx, prevXPos
	sub ebx, currXPos
	.ELSE
	mov ebx, currXPos
	sub ebx, prevXPos
	.ENDIF

	.IF ebx > numberBlocks
	mov numberBlocks, 0
	.ELSE
	sub numberBlocks, ebx
	.ENDIF

	; if there are no blocks left, end the game - the user lost
	.IF numberBlocks <= 0
	mov ebx, 0
	mov winner, ebx
	call EndGame
	.ENDIF

	; calculate: prevXPos = max(prevXPos, currXPos)
	.IF currXPos > eax
	mov ebx, currXPos
	mov prevXPos, ebx
	.ENDIF

	; calculate: currXPos = prevXPos
	mov ebx, prevXPos
	mov currXPos, ebx

	.IF ypos <= 6; check if the y position has reached the top of the gameboard
	mov ebx, 1
	mov winner, ebx
	call EndGame; end the game, the user won
	.ENDIF

	call ClearLine; clear the line of blocks on the screen
	mov printOffset, 0

	mov eax, currXPos
	sub eax, firstXPos; This difference is the number of offsets
	jz stopBlocksExit; Skips calculating offset if zero

	.IF eax > 100
	NEG eax
	.ENDIF

	mov ecx, eax
	NewOffset :
add printOffset, 4
loop NewOffset

stopBlocksExit :

; decrease automatically to 2 blocks on line 4 and 1 block on line 10
.IF ypos == 26
.IF numberBlocks > 2
dec numberBlocks
.ENDIF
.ELSEIF ypos == 14
.IF numberBlocks > 1
dec numberBlocks
.ENDIF
.ENDIF

call Print; print the same line again without the blocks that were missed

jmp NewLine; move up to next line
ret
stopBlocks ENDP

; ---------------------------------------------------- -
EndGame PROC
;
; End the game
; Recieves:
; Returns:
; ---------------------------------------------------- -
; move to middle of the screen, print win or lost

mov dh, 10
mov dl, 52
call Gotoxy

.IF winner == 1
	mov eax, white + (green * 16)
	call SetTextColor
	mov edx, OFFSET wonMSG
	call WriteString
	movzx ax, level
	call WriteDec
	call ResetValues
	movzx eax, level
	mov ebx, 5
	mul ebx
	sub delayVal, eax			;decrease speed by 5 for each level increase
	inc level; if the player won, increase the level number
	mov eax, 500
	call Delay
	call main	;reset to play next level of the game 

.ELSE
	call editScores; updates finalScoreData

	; write finalScoreData to the file
	lea EDX, scoreFile
	call CreateOutputFile
	mov fileHandle, eax
	lea edx, finalScoreData
	mov ecx, outFileBufferSize
	call WriteToFile
	mov eax, fileHandle
	call CloseFile

	call BeginTransitionAnimation
	mov dh, 30
	mov dl, 47
	call Gotoxy
	.IF newHighscore == 1
		mov eax, white + (green * 16)
	.ELSE
		mov eax, white + (red * 16)
	.ENDIF
	call SetTextColor
	mov edx, OFFSET lostMsg
	call WriteString
	movzx ax, level
	call WriteDec
.ENDIF

mov dl, 0
mov dh, 44
call Gotoxy
call ExitProcess
ret
EndGame ENDP

; ---------------------------------------------------- -
ResetValues PROC
;
; Resets all changed variables to their initial value
; Recieves:
; Returns:
; ---------------------------------------------------- -
mov eax, 3
mov numberBlocks, eax
mov numBLocksMult, eax
mov multipliedBlockNum, eax

mov eax, 34
mov ypos, eax

mov eax, 54
mov currXpos, eax
mov printXpos, eax
mov prevXpos, eax

mov eax, 0
mov printOffset, eax

mov eax, 160
mov delayVal, eax

ret
ResetValues ENDP

readScoreFile PROC
	mov  eax, fileHandle
	mov  edx, OFFSET scoreBuffer
	mov  ecx, BUFSIZE
	call ReadFromFile
	mov  bytesRead, eax

	; iterate through buffer until we find a comma, store into scoreData
	mov ecx, bytesRead
	mov esi, 0
	mov edi, 0

	LoopChars :
		CMP ecx, 0; compare if the string is empty so stop the loop
		JE exitLoop
		mov al, scoreBuffer[esi]; passing the characters to al for space; checking
		CMP al, 44
		JE CommaFound

		mov scoredata[esi], al; if not space then copy the character to; singleWords array
		INC esi
		INC index
		DEC ecx
		JMP LoopChars
	CommaFound :
		mov bl, index; if sapce occur pass the index to bl
		inc bl
		mov indexArray[edi], bl; save the index in indexArray
		INC edi
		INC esi
		INC index
		DEC ecx
		JMP LoopChars
	exitLoop:
	ret
readScoreFile ENDP

writeScores PROC
	mov eax, white + (black * 16)
	call SetTextColor
	call clearLeaderboardArea
	mov dh, 14
	mov dl, 90
	call Gotoxy
	mov edx, OFFSET leaderboardMsg
	call WriteString
	mov dh, 15
	mov dl, 85
	call Gotoxy
	mov edx, OFFSET leaderboardHeader
	call WriteString

	; prints out whats stored in score data
	mov esi, 0
	mov ecx, 9
	mov dh, 16
	mov dl, 85
	call Gotoxy
	mov edx, OFFSET scoredata
	call Writestring

	WriteNext :
		; if esi odd add space if esi even add newline
		test esi, 1
		jnp odd
		mov dl, 100
		jmp writeToScreen
		odd :
		mov dl, 85
		writeToScreen :
		mov eax, esi
		inc eax
		mov bl, 2
		div bl
		add al, 16
		mov dh, al
		call Gotoxy

		movzx edx, indexArray[esi]
		add edx, OFFSET scoredata
		call Writestring
		inc esi
		loop WriteNext
	ret
writeScores ENDP

; Still in progress 
editScores PROC
	; move new data into correct position in scoredata and write updated data into file
	; look at every other value(aka the scores) compare to newScore
	; if higher than new score write itand the corresponding player to the fileand loop again
	; if lower than new score write new scoreand player to the file, then write remaining players up to five
	call crlf
	call crlf
	mov al, level
	mov levelCMP, al
	mov ecx, 5	;5 players are stored
	mov esi, 0
	loopPlayers:
		cmp ecx, 0
		je endLoop
		mov eax, 5
		sub eax, ecx
		mov ebx, 2
		mul ebx		;access the score value for each player 
		sub eax, offsetIndex
		movzx edx, indexArray[eax]		;index of scoredate to look at
		mov bl, scoredata[edx]			;value at specified index
		.IF bl > 0
			sub bl, 48						;ascii to decimal
		.ENDIF
		
		mov bh, scoredata[edx+1]		;"" if single digit else second digit
		.IF bh > 0
			push eax
			mov al, bl
			mov bh, 10d
			mul bh
			mov bl, al
			pop eax
			sub bh, 48						;ascii to decimal
		.ENDIF
		add bl, bh
		cmp bl, levelCMP ;compare the score to the level the player is on
		jb below		;if the stored score is below the new players score
		jnb above		;if the stored score is above the new players score

		above:
		push ecx
		.IF eax > 0
			movzx ecx, indexArray[eax]
			sub ecx, prevIndex
		.ELSE
			movzx ecx, indexArray[0]
		.ENDIF
		mov edx, prevIndex
		printName:
			mov bl, scoredata[edx]
			.IF bl == 0
				mov bl, 2Ch			;seperate with commas
			.ENDIF
			mov finalScoreData[esi], bl		;copy into new memory location
			inc esi
			inc edx
			loop printName
		mov prevIndex, edx

		inc eax 
		movzx ecx, indexArray[eax]
		sub ecx, prevIndex
		mov edx, prevIndex
		printScore:
			mov bl, scoredata[edx]
			.IF bl == 0
				mov bl, 2Ch			;seperate with commas
			.ENDIF
			mov finalScoreData[esi], bl		;copy into new memory location
			inc esi
			inc edx
			loop printScore
		mov prevIndex, edx
		pop ecx
		dec ecx
		jmp loopPLayers

		below:
		.IF ecx == 5
			mov newHighScore, 1
		.ENDIF
		push ecx
		mov ecx, playerNameLength
		copyName:
			mov edx, playerNameLength
			sub edx, ecx
			mov bl, playerName[edx]
			mov finalScoreData[esi], bl
			inc esi
			loop copyName
		mov bl, 2Ch
		mov finalScoreData[esi], bl
		inc esi

		movzx eax, level
		; copy level to final data string
		copyLevel:
			cmp eax, 10
			jb exitCopyLevel
			mov ebx, 10d
			idiv ebx
			add edx, 48
			mov finalScoreData[esi], dl
			inc esi
			cmp eax,0
			jne copyLevel
		exitCopyLevel:
			add eax, 48
			mov finalScoreData[esi], al
			inc esi
			mov bl, 2Ch
			mov finalScoreData[esi], bl
			inc esi
		mov levelCMP, 0	; so it wont be reinserted again
		pop ecx
		dec ecx
		mov eax, 2
		mov offsetIndex, eax
		jmp loopPlayers
		endLoop:
		mov outFileBufferSize, esi
	ret
editScores ENDP

; -- Clear the Leaderboard area so we dont have mixed up strings printed
clearLeaderboardArea PROC
	mov ecx,5
	clearLeaderboardLines:
		mov dh, 16
		add dh, 5
		sub dh, cl
		mov dl, 85
		call Gotoxy
		mov edx, OFFSET leaderboadEmpty
		call WriteString
		loop clearLeaderboardLines
	ret
clearLeaderboardArea ENDP
END main