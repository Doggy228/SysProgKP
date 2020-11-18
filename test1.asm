.586
.model flat stdcall
option casemap :none
.data
MemBuf dd 16384 dup(?)
MemSp dd 0
.code
main:
L1:
;Standart proc[print]
MY_print proc
	push ebp
	mov ebp,esp
	mov eax,[ebp+12]
	invoke MessageBoxA, 0, eax, ADDR Caption, 0
	mov [ebp+8],0
	pop ebp
	ret 8
MY_print endp
;User defined proc[print]
MY_fibonacci proc
	push ebp
	mov ebp,esp
	;Init Var tables.
	add MemSp,20
	;Set param[n] value
	mov eax,[ebp+12]
	;Access var[n]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,4
	mov [ebx],eax
	;Body proc start
	;Init Var tables.
	add MemSp,4
L3:
	;IF Statement begin
	;BoolGreater start
	;Access var[n]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,4
	mov eax,[ebx]
	cmp eax,2 ;Constant
	jg L5
	;Access var[_tmp1]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,4
	mov [ebx],0
	goto L6
L5:
	;Access var[_tmp1]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,4
	mov [ebx],1
L6:
	;BoolGreater end
	;Access var[_tmp1]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,4
	cmp [ebx],0
	je L7
	;IF True
	;Init Var tables.
	add MemSp,20
L8:
	;Return from proc[fibonacci] begin
	;ArithPlus begin
	;Call proc[fibonacci] begin
	;ArithMinus begin
	;Access var[n]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,4
	mov eax,[ebx]
	sub eax,1 ;Constant
	;Access var[_tmp1]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,4
	mov [ebx],eax
	;ArithMinus end
	;Access var[_tmp1]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,4
	push [ebx]
	;Access var[_tmp2]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,8
	push ebx
	call MY_fibonacci
	;Call proc[fibonacci] end
	;Call proc[fibonacci] begin
	;ArithMinus begin
	;Access var[n]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,4
	mov eax,[ebx]
	sub eax,2 ;Constant
	;Access var[_tmp3]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,12
	mov [ebx],eax
	;ArithMinus end
	;Access var[_tmp3]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,12
	push [ebx]
	;Access var[_tmp4]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,16
	push ebx
	call MY_fibonacci
	;Call proc[fibonacci] end
	;Access var[_tmp2]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,8
	mov eax,[ebx]
	;Access var[_tmp4]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,16
	add eax,[ebx]
	;Access var[_tmp5]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,20
	mov [ebx],eax
	;ArithPlus end
	;Access var[_tmp5]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,20
	mov eax,[ebx]
	mov [ebp+8],eax
	pop ebp
	ret 8
	;Return from proc[fibonacci] end
L9:
	;Destroy Var tables.
	sub MemSp,20
L7:
	;IF Statement end
	;Return from proc[fibonacci] begin
	;Access var[n]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,4
	mov eax,[ebx]
	mov [ebp+8],eax
	pop ebp
	ret 8
	;Return from proc[fibonacci] end
L4:
	;Destroy Var tables.
	sub MemSp,4
MY_fibonacci endp
	;Set var[element] begin
	mov eax,5 ;Constant
	;Access var[element]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,8
	mov [ebx],eax
	;Call proc[fibonacci] begin
	;Access var[element]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,8
	push [ebx]
	;Access var[_tmp1]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,16
	push ebx
	call MY_fibonacci
	;Call proc[fibonacci] end
	;Set var[value] begin
	;Access var[_tmp1]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,16
	mov eax,[ebx]
	;Access var[value]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,12
	mov [ebx],eax
	;Call proc[print] begin
	;Access var[value]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,12
	push [ebx]
	;Access var[_tmp2]
	mov ebx,[MemBuf]
	add ebx,MemSp
	sub ebx,20
	push ebx
	call MY_print
	;Call proc[print] end
L2:
end main
