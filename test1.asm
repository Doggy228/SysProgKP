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
	EnvBuf dd 1024 dup(?)
	EnvSp dd 0
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
	;Save prev & init new env VarTable.
	mov ebx,offset[EnvBuf]
	add ebx,EnvSp
	mov eax,MemSp
	mov [ebx],eax
	add EnvSp,4
	add MemSp,4
	;Set param[n] value
	mov ecx,[ebp+12]
	;Access var[n]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,4
	mov [ebx],ecx
	;Body proc start
	;Save prev & init new env VarTable.
	mov ebx,offset[EnvBuf]
	add ebx,EnvSp
	mov eax,MemSp
	mov [ebx],eax
	add EnvSp,4
	add MemSp,4
L1:
	;COMMENT:  Condition check
	;IF Statement begin
	;BoolGreater start
	;Access var[n]
	mov ebx,offset[MemBuf]
	push eax
	mov eax,offset[EnvBuf]
	add eax,EnvSp
	sub eax,4
	add ebx,[eax]
	pop eax
	sub ebx,4
	mov eax,[ebx]
	cmp eax,1 ;Constant
	jg L3
	;Access var[_tmp1]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,4
	mov dword ptr [ebx],0
	jmp L4
L3:
	;Access var[_tmp1]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,4
	mov dword ptr [ebx],1
L4:
	;BoolGreater end
	;Access var[_tmp1]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,4
	cmp dword ptr [ebx],0
	je L5
	;IF True
	;Save prev & init new env VarTable.
	mov ebx,offset[EnvBuf]
	add ebx,EnvSp
	mov eax,MemSp
	mov [ebx],eax
	add EnvSp,4
	add MemSp,20
L6:
	;COMMENT:  Recursive call
	;Return from proc[fibonacci] begin
	;ArithPlus begin
	;Call proc[fibonacci] begin
	;ArithMinus begin
	;Access var[n]
	mov ebx,offset[MemBuf]
	push eax
	mov eax,offset[EnvBuf]
	add eax,EnvSp
	sub eax,8
	add ebx,[eax]
	pop eax
	sub ebx,4
	mov eax,[ebx]
	sub eax,1 ;Constant
	;Access var[_tmp1]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,4
	mov [ebx],eax
	;ArithMinus end
	;Access var[_tmp1]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,4
	push [ebx]
	;Access var[_tmp2]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,8
	push ebx
	call MY_fibonacci
	;Call proc[fibonacci] end
	;Call proc[fibonacci] begin
	;ArithMinus begin
	;Access var[n]
	mov ebx,offset[MemBuf]
	push eax
	mov eax,offset[EnvBuf]
	add eax,EnvSp
	sub eax,8
	add ebx,[eax]
	pop eax
	sub ebx,4
	mov eax,[ebx]
	sub eax,2 ;Constant
	;Access var[_tmp3]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,12
	mov [ebx],eax
	;ArithMinus end
	;Access var[_tmp3]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,12
	push [ebx]
	;Access var[_tmp4]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,16
	push ebx
	call MY_fibonacci
	;Call proc[fibonacci] end
	;Access var[_tmp2]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,8
	mov eax,[ebx]
	;Access var[_tmp4]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,16
	add eax,[ebx]
	;Access var[_tmp5]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,20
	mov [ebx],eax
	;ArithPlus end
	;Access var[_tmp5]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,20
	mov eax,[ebx]
	mov ebx,[ebp+8]
	mov [ebx],eax
	;Destroy tree env VarTable & restore prev.
	mov ebx,offset[EnvBuf]
	add ebx,EnvSp
	sub ebx,12
	push eax
	mov eax,[ebx]
	mov MemSp,eax
	pop eax
	sub EnvSp,12
	pop ebp
	ret 8
	;Return from proc[fibonacci] end
L7:
	;Destroy env VarTable & restore prev.
	sub EnvSp,4
	sub MemSp,20
L5:
	;IF Statement end
	;COMMENT:  For n=0 or n=1
	;Return from proc[fibonacci] begin
	;Access var[n]
	mov ebx,offset[MemBuf]
	push eax
	mov eax,offset[EnvBuf]
	add eax,EnvSp
	sub eax,4
	add ebx,[eax]
	pop eax
	sub ebx,4
	mov eax,[ebx]
	mov ebx,[ebp+8]
	mov [ebx],eax
	;Destroy tree env VarTable & restore prev.
	mov ebx,offset[EnvBuf]
	add ebx,EnvSp
	sub ebx,8
	push eax
	mov eax,[ebx]
	mov MemSp,eax
	pop eax
	sub EnvSp,8
	pop ebp
	ret 8
	;Return from proc[fibonacci] end
L2:
	;Destroy env VarTable & restore prev.
	sub EnvSp,4
	sub MemSp,4
MY_fibonacci endp
main:
	;Save prev & init new env VarTable.
	add MemSp,20
L8:
	;COMMENT:  Index in sequence Fibonacci
	;Set var[element] begin
	mov eax,22 ;Constant
	;Access var[element]
	mov ebx,offset[MemBuf]
	add ebx,MemSp
	sub ebx,4
	mov [ebx],eax
	;COMMENT:  Calc value
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
	;COMMENT:  Print seq. index 
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
	;COMMENT:  Print value
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
L9:
	;Destroy env VarTable & restore prev.
	sub MemSp,20
end main
