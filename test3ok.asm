.586
.model flat, stdcall
option casemap :none
include C:\masm32\include\kernel32.inc
include C:\masm32\include\user32.inc


includelib C:\masm32\lib\kernel32.lib
includelib C:\masm32\lib\user32.lib


.data
    Caption db "Message", 0
    TextDword db 20 dup(0)

    fmt db "%s",10,0
    hello db "Hello world!",0
.code
main:
    mov eax,1202345
    mov edx,0
    push dx
    mov cx,1
    mov ebx,10
LL1:    
    mov edx,0
    div ebx
    add dx,'0'
    push dx
    inc cx
    cmp eax,0
    jne LL1
    mov ebx,offset[TextDword]
LL2:    
    pop ax
    mov [ebx],al
    inc ebx
    dec cx
    cmp cx,0
    jne LL2
    invoke MessageBoxA, 0, ADDR TextDword, ADDR Caption, 0
    invoke ExitProcess, 0      
end main
