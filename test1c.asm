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
;User defined proc[fibonacci]
MY_fibonacci proc
	push ebp
	mov ebp,esp
	;Init Var tables.
	add MemSp,4
	;Set param[n] value
	mov eax,[ebp+12]
	;Access var[n]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,4
	mov [ebx],eax
	;Body proc start
	;Init Var tables.
	add MemSp,4
L1:
	;Return from proc[fibonacci] begin
	;ArithPlus begin
	;Access var[n]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,4
	mov eax,[ebx]
	add eax,4 ;Constant
	;Access var[_tmp1]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,4
	mov [ebx],eax
	;ArithPlus end
	;Access var[_tmp1]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,4
	mov eax,[ebx]
	mov ebx,[ebp+8]
	mov [ebx],eax
	;Destroy Var tables.
	sub MemSp,4
	pop ebp
	ret 8
	;Return from proc[fibonacci] end
L2:
	;Destroy Var tables.
	sub MemSp,4
MY_fibonacci endp
main:
;prog.env=20
	;Init Var tables.
	add MemSp,20
L3:
	;Set var[element] begin
	mov eax,5 ;Constant
	;Access var[element]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,4
	mov [ebx],eax
	;Call proc[fibonacci] begin
	;Access var[element]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,4
	push [ebx]
	;Access var[_tmp1]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,12
	push ebx
	call MY_fibonacci
	;Call proc[fibonacci] end
	;Set var[value] begin
	;Access var[_tmp1]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,12
	mov eax,[ebx]
	;Access var[value]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,8
	mov [ebx],eax
	;Call proc[print] begin
	;Access var[element]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,4
	push [ebx]
	;Access var[_tmp2]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,16
	push ebx
	call MY_print
	;Call proc[print] end
	;Call proc[print] begin
	;Access var[value]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,8
	push [ebx]
	;Access var[_tmp3]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,20
	push ebx
	call MY_print
	;Call proc[print] end
L4:
	;Destroy Var tables.
	sub MemSp,20
end main
