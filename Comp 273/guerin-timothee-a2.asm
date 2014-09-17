
    	    
#########################################################################
#					 				#
#		text segment		       	 			#
#                           	                			#
#                             						#
#	s0: remeber the pivot						#
#	s1: list1 ref							#
#       s2: list2                          				#
#	s3: list3							#
#									#
#	s4: list1 length						#
#       s5: list2                          				#
#	s6: list3							#
#									#
#	v0: returning value 						#
#	a0 -  the value k						#		
# 	a1 -  the starting address of the array that contains L		#
# 	a2 -  the size of the list, i.e. | L |				#
#########################################################################

.text		
    
findRankKf: 		#SAVE ALL REGISTER WE USE
			sub $sp, $sp,  32# allocate stack space: 1 values * 4 bytes each
			sw $s0, 0($sp)
			sw $s1, 4($sp)
			sw $s2, 8($sp)
			sw $s3, 12($sp)
			sw $s4, 16($sp)
			sw $s5, 20($sp)
			sw $s6, 24($sp)
			sw $ra, 28($sp)

			#INITIALSE REGISTER TO FIND THE LENGHT OF ALL LIST
			li $t0, 1
			beq $a2, $t0, sizeOne 		#Check if the size of the array is 1
			lw $s0, ($a1)			#if not load the first element and set it to the pivot
		
			move $t0,  $a1			#t0 contains the iterator of list
			li $t1,  0			#t1 containt lenght of list1
			li $t2,  0			#t2 containt lenght of list2
			li $t3,  0			#t3 containt lenght of list3
			li $t6, 0			#t3 counter
				
findLenghtLoop: 	lw $t4, ($t0)
			beq $t6, $a2, endFindLenghtLoop
			
			slt $t5,$t4,$s0 			#if v0 < s2 set 1, pivot > list[i]
   	 		beq $t5, $zero, pivotInfOrEquLen 	#If condition fails
   	 		addi $t1, $t1, 1				#increment the size of list1
   	 		j incrementItLen
   	 		
pivotInfOrEquLen:	beq $s0, $t4	pivotequLen		#if pivot == 	list[i]
			addi $t3,$t3, 1				#increment the size of list3
			j incrementItLen

pivotequLen:		addi $t2, $t2,1				#increment the size of list2
			j incrementItLen
			
incrementItLen: 	addi $t0,$t0, 4				#increment the iterator
			addi $t6,$t6 1				#increment the counter
			j findLenghtLoop	
				
endFindLenghtLoop:	#FIND TEHE ADRESS OF LIST1
			sll $s1, $a2, 2     		#store in t3 the adress of the element we want to change place
    			add $s1, $a1,$s1   		#get the adress of the last element
    			addi $s1, $s1, 4		#increment of 1 to get the next free space
    			
    			#FIND TEHE ADRESS OF LIST2
    			sll $s2, $t1, 2     		#store in t3 the adress of the element we want to change place
    			add $s2, $s1,$s2   		#get the adress of the last element
    			addi $s2, $s2, 4		#increment of 1 to get the next free space
    			
    			#FIND TEHE ADRESS OF LIST3
    			sll $s3, $t2, 2     		#store in t3 the adress of the element we want to change place
    			add $s3, $s2,$s3   		#get the adress of the last element
    			addi $s3, $s3, 4		#increment of 1 to get the next free space

			#SAVE THE LENGHT
			move $s4, $t1
			move $s5, $t2
			move $s6, $t3
			
			
			#RESET COUNTERS
			move $t0,  $a1			#t0 contains the iterator of list
			move $t1,  $s1			#t1 containt the iterator of list1
			move $t2,  $s2			#t2 containt the iterator of list2
			move $t3,  $s3			#t3 containt the iterator of list3
			li $t6, 0			#t3 counter
			
createListLoop:		lw $t4, ($t0)				#load the element list[i]
			beq $t6, $a2, endCreateListLoop		#check if we reach end of loop
			
			slt $t5,$t4,$s0 			#if t4 < s2 set 1, pivot > list[i]
   	 		beq $t5, $zero, pivotInfOrEquCr		#If condition fails
   	 		sw $t4, ($t1)
   	 		addi $t1, $t1, 4				#increment the iterator of list1
   	 		j incrementItCr
   	 		
pivotInfOrEquCr:	beq $s0, $t4	pivotequCr		#if pivot == 	list[i] goto pivotequCr
			sw $t4, ($t3)
			addi $t3, $t3, 4				#increment the iterator of list3
			j incrementItCr

pivotequCr:		sw $t4, ($t2)
			addi $t2, $t2, 4				#increment the iterator of list2
			j incrementItCr
			
incrementItCr: 		addi $t0,$t0,  4				#increment the iterator of list
			addi $t6,$t6 1				#increment the counter
			j createListLoop	
				
endCreateListLoop:	slt $t0,$a0,$s4 			#if a0 < s4 set 1, K < list1.length
   	 		beq $t0, $zero, kNotInfToL1		#If condition fails
   	 		
   	 		#Copy the temporary array into the fixed one
   	 		li $t0, 0		#$t0 save the counter
   	 		move $t1, $s1		#t1 iterator of the temp array
   	 		move $t2, $a1		#t2 iteragor of the saved array
   	 		
copyArray1:		beq $t0, $s4, endCopyArray1	#Check reach end of array
   	 		lw $t3, ($t1)			#Load the value in the list3
   	 		sw $t3, ($t2)			#Store it at the begining
   	 		addi $t1, $t1, 4		#increment itrators and counters
			addi $t2, $t2, 4
			addi $t0, $t0, 1
			j copyArray1
endCopyArray1:		
			move $a2, $s4
			
			jal findRankKf				#call the function
			j endFunction
			
kNotInfToL1:		add $t1 , $s4, $s5
			slt $t0,$a0,$t1			#if a0 < t1 set 1, K < list1.length + list1.length
   	 		beq $t0, $zero, kNotInfToL1plusL2	#If condition fails
			move $v0, $s0
			j endFunction
			
kNotInfToL1plusL2:	sub $a0, $a0, $s4 		#k - list1.length					
			sub $a0, $a0, $s5		#k - list2.length			
			
			#Copy the temporary array into the fixed one
   	 		li $t0, 0		#$t0 save the counter
   	 		move $t1, $s3		#t1 iterator of the temp array
   	 		move $t2, $a1		#t2 iteragor of the saved array
   	 		
copyArray2:		beq $t0, $s6, endCopyArray2	#Check reach end of array
   	 		lw $t3, ($t1)			#Load the value in the list3
   	 		sw $t3, ($t2)			#Store it at the begining
   	 		addi $t1, $t1, 4		#increment itrators and counters
			addi $t2, $t2, 4
			addi $t0, $t0, 1
			j copyArray2
endCopyArray2:		
			move $a2, $s6
							
			jal findRankKf		#Call the function recursivly
			j endFunction
			
sizeOne:
			lw $v0, ($a1)	
		
endFunction:
			#RELOAD ALL REGISTER WE USE
			lw $s0, 0($sp)
			lw $s1, 4($sp)
			lw $s2, 8($sp)
			lw $s3, 12($sp)
			lw $s4, 16($sp)
			lw $s5, 20($sp)
			lw $s6, 24($sp)
			lw $ra, 28($sp)
			addi $sp, $sp,  32# allocate stack space: 1 values * 4 bytes each
			
			
			jr $ra
			
