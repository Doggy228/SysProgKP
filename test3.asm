.586
.model flat, stdcall
option casemap :none
include kernel32.inc
include user32.inc
includelib kernel32.lib
includelib user32.lib
.data
	Caption db "Message", 0
	TextDword db 20 dup(0)
	MemBuf dd 16384 dup(?)
	MemSp dd 0
.code
;Standart proc[print]
MY_print proc
	push ebp
	mov ebp,esp
	mov eax,[ebp+12]
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
	mov dword ptr [ebp+8],0
	pop ebp
	ret 8
MY_print endp
main:
	;Init Var tables.
	add MemSp,8
L1:
	;Set var[element] begin
	mov eax,5 ;Constant
	;Access var[element]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,4
	mov [ebx],eax
	;Call proc[print] begin
	;Access var[element]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,4
	push [ebx]
	;Access var[_tmp1]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,8
	push ebx
	call MY_print
	;Call proc[print] end
L2:
	;Destroy Var tables.
	sub MemSp,8
end main
