.data
ZERO: 		.float 	0.0
ONE: 		.float 	1.0
TWO: 		.float 	2.0
EPSILON: 	.float 	0.0001 		# Value e in algorithm above.
INTERVAL: 	.float 	-2.1,3.7 	# Root is between these values.
POLYORDER:    	.word   3         	# Order of polynomial.
COEFFICIENTS:   .word 	2,0,1,-3 	# Integer coefficient of highest 
					# order comes first in list. 
promptLine: .asciiz " \n"					
					
#########################################################################
#					 				#
#		text segment		       	 			#

#########################################################################

.text		

	.globl main 
main:		        # execution starts here
	l.s $f12, TWO
	li $t0, 50
	la $t0, INTERVAL
	l.s $f12 , ($t0)
	l.s $f13, 4($t0)
	jal findRoot

	
	mov.s $f12, $f0
	li $v0, 2
	syscall

	# Exit
    	li $v0,10
	syscall		
	
#########################################################################
#			POWER FUNCTION					#
#		f12 argument x						#
#		a0 argument n						#
#									#		
#		f0 return result					#
#									#
#									#			
#########################################################################
power:
	#SAVE ALL REGISTER WE USE
	sub $sp, $sp,  4# allocate stack space: 1 values * 4 bytes each
	sw $ra, 0($sp)

	beq $a0, $zero, power0	#check if the power is 0 and so return 1
	subi $a0 , $a0, 1		#otherwise substract 1 to n
	
	jal power		#Call the function recusrively
	mul.s $f0 , $f12 , $f0	#Mutpliply x with the result of the function : x* power(x, n-1)
	j endPower		#Go to the end of the function
	
	power0:
	l.s $f0 , ONE
	
	endPower:
	lw $ra, 0($sp)
	addi $sp, $sp, 4 
	jr $ra
	
#########################################################################
#				END POWER				#
#########################################################################

#########################################################################
#			EVALUATEP FUNCTION				#
#		f12 argument x						#
#									#		
#		f0 return result					#
#									#
#		s0 coefficient counter					#
#		f20 result						#			
#########################################################################

evaluateP:
	#SAVE ALL REGISTER WE USE
	sub $sp, $sp,  12# allocate stack space: 1 values * 4 bytes each
	sw $s0 , 0($sp)
	s.s $f20 , 4($sp)
	sw $ra, 8($sp)
	
	
	#Initialise the register
	li $s0,  0
	l.s $f20,  ZERO

	evalLoop:
		
		
		#Call the function power(x, n), set the arguments
		lw $t0 , POLYORDER	#load the poly order
		
		#check if we reach the end of the polynome
		addi $t1, $t0, 1
		beq $s0 , $t1, endEvaluateP
		
		sub $a0 , $t0, $s0	#set the value of n (= POLYORDER - ORDER_PASSED)
		
		#$f12 is already x
		jal power
		
		la $t1, COEFFICIENTS	#load the coefficients
		sll $t2, $s0, 2     		#store in t1 the adress of the element we want to change place
    		add $t1, $t1,$t2   		#get the adress of the last element	
		lw $t3, ($t1)	#set the coeffictient in $f4
		
		mtc1 $t3, $f5
		cvt.s.w $f4, $f5
		
		mul.s $f4, $f4, $f0 #mutpliy with the result: a*x^n
		
		add.s $f20 , $f20, $f4 #Add the result of one power with 
		
		

		
		#increment counter
		addi $s0, $s0, 1
		j evalLoop
		
	endEvaluateP:
	mov.s $f0, $f20
	
	#Reload the registers
	lw $s0 , 0($sp)
	l.s $f20 , 4($sp)
	lw $ra, 8($sp)
	add $sp, $sp,  12
	
	jr $ra
	
#########################################################################
#				END EVALUATEP				#
#########################################################################	

#########################################################################
#			EVALUATEP FUNCTION				#
#		f12 argument x						#
#									#		
#		f0 return result					#
#									#
#		s0 coefficient counter					#
#		f20 midlle						#
#									#		
#########################################################################

findRoot:
		sub $sp, $sp,  24# allocate stack space: 6 values * 4 bytes each
		sw $s0 , 0($sp)
		s.s $f20 , 4($sp)
		s.s $f20 , 8($sp)
		s.s $f22 , 12($sp)
		s.s $f23 , 16($sp)
		sw $ra, 20($sp)
	
		#Save the two arguments inside registers
		mov.s $f22, $f12
		mov.s $f23, $f13 
	
	
		add.s $f20, $f22, $f23	#C = a+b
		l.s $f4, TWO		#Load 2 in register f4 to divide after by 2
		div.s $f20, $f20, $f4	#c = (a+b)/2
	
		#Call the function evaluateP(c)
		mov.s $f12, $f20
		jal evaluateP
		
		mov.s $f21, $f0		#save the result in a register
		 
		l.s $f5, ZERO

		
		c.eq.s $f21, $f5		#if(evaluateP(c) == 0) 
		bc1t rootAtC		#then goto rootAtC
	
	
		sub.s $f4 , $f22 , $f23
		#Set a-b < 0
		l.s $f5, ZERO
		c.lt.s $f4, $f5
		bc1f abpos	# if false skip next instruct
		sub.s $f4, $f5, $f4	#|a-b| = -(a-b)
	
	abpos:
		l.s $f5, EPSILON
		c.lt.s $f4, $f5		#if |a-b| < EPSILON
		bc1t rootAtC		#then goto rootAtC
	
		#Call the function evaluateP(a)
		mov.s $f12, $f22
		jal evaluateP
		
		mul.s $f4, $f0, $f21	#evaluateP(a)*evaluateP(c) = d
		
		
		l.s $f5, ZERO
		c.lt.s $f5, $f4		#if d > 0
		bc1t PaPcPos		#then goto PaPcPos
		mov.s $f12, $f22	#reload a in arguments register
		mov.s $f13, $f20	#b = c
		jal findRoot
		j endFindRoot
		
	PaPcPos:
		mov.s $f12, $f20	#reload a in arguments register
		mov.s $f13, $f23	#a = c
		
		jal findRoot
		j endFindRoot
	
	rootAtC:
		mov.s $f0, $f20	#return c
		j endFindRoot
		
	
	
endFindRoot:
		
		lw $s0 , 0($sp)
		l.s $f20 , 4($sp)
		l.s $f20 , 8($sp)
		l.s $f22 , 12($sp)
		l.s $f23 , 16($sp)
		lw $ra, 20($sp)
		add $sp, $sp,  24# allocate stack space: 6 values * 4 bytes each
	
		jr $ra	#return 

	



	
			
	
	
	
	
	

	
	
