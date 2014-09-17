
#################################################
#					 	#
#     	 	data segment			#
#						#
#################################################

.data  
    
    prompt:	.asciiz "Input your text: "
    promptMaxLen:	.asciiz "Input the maximum lenght  '.: "
    promptNumStr: .asciiz "Number of string is:"
    promptNumber: .asciiz ".)"
    promptChangeOrder: .asciiz "MOVE TO FRONT STRING (ENTER NUMBER)"
    promptQuit: .asciiz "BYE FOR NOW"
    promptLine: .asciiz " \n"
    strs: .space 5000
    array:  .word 20
    	    .align 2	
#################################################
#					 	#
#		text segment		        #
#                           	                #
#	s0: counter				#
#       s1: ref array                           #
#       s2: lenght maxiumum	                #
#################################################

.text		
       	.globl main 
main:		        # execution starts here
    li $s0,0        #Clear the $s0 register that will contains the counter
    la $s1, array  #get the adress of the first element
	
    
    
#Get the user to input max lenght
maxLenInputLoop:
    
    	la $a0,promptMaxLen	#Display Message
	li $v0,4
	syscall		
    

    	li $v0, 5 #Ask for input there
    	syscall   
   	 li $t1,100
    	slt $t0, $t1,$v0 #if v0 > 100 set 1: linmit the size to 100
    	bne $t0, $zero, maxLenInputLoop #If condition fails
    	move $s2,$v0 #Set the maximum value into register s2
   
   
   	la $t6,strs
    
userInputLoop: 
    	beq $s0,20,endInput #if we get to the max number of string stop the input loop
    
    	#Prompt to the user the text
	la $a0,prompt	#put string address into a0
	li $v0,4	#system call to print
	syscall		# a string
    
    
    	#Read the user text
	#la $a0,strs	#put strs address into a0
    
    	#GET USER INPUT
    	#t2: index of the last string
    	#t0: adress where to put the new element
    	#t5: save the lenght of the previous input
    	#t6: pointer to the adress current string adress
    
    	
    	add $t6, $t6,$t5    #move to the next free adress
    	move $a0, $t6
    
    	#GetInput
        li $v0,8	#system call to print
        move $a1, $s2
        addi $a1,$a1,1
        syscall		#get the user input
        
        
        
        #############################################################
        #                FIND THE LENGHT OF THE STRING              #
        #############################################################
        
        subi $t3,$s2,1
       
        move $t2,$a0	# t2 points to the string
        li $t1,0	# t1 holds the count
        findLen:	lb $t0,($t2)	# get a byte from string
        
                    beqz $t0,lenEnd	# zero means end of string
                    beq $t1,$t3,reachEnd
                    
                    add $t1,$t1,1	# increment count
                    add $t2,$t2,1	# move pointer one character
                    j findLen	# go round the loop again
                    
        reachEnd:
        	lb $t7,($t2)
        	
            	li $t4,10
            	sb $t4,($t2) 	#set the last element to be a '\n'
            	addi $t1,$t1,1	# increment count          
            
            	
            	beq $t7,10, lenEnd #CHeck if the last character is a \n and if so dont put it again and dont goto the line again
              	#Goto the line
    		la $a0,promptLine
    		li $v0,4
    		syscall  

        lenEnd:
        
        beq $t1,1 ,endInput 	#if the string is empty, check 1 as the /n take one space
        addi $t5, $t1,1 		#set t5 with the input len + 1 to count the null byte
        
        #Print the user input again
        
        move $a0, $t6
        li $v0,4		#system call to print
        syscall			# a string
        
        sll $t0, $s0, 2     
        add $t0, $s1,$t0    	#get the adress of first free space      
        
        sw $t6, ($t0)     	 #save the reference to the begining the input      
        add $s0,$s0,1     	#increment the counter
        j userInputLoop
        
 
        
  
endInput:

    #Display number of string msg
    la $a0,promptNumStr	
    li $v0,4
    syscall	
    
    #Display number of string value
    move $a0,$s0	
    li $v0,1
    syscall	   
    
    #Goto the line
    la $a0,promptLine
    li $v0,4
    syscall	


displayAndChangeLoop:

    	li $t0,0	    #t0 holds the count
    	la $t1,array    #t1 point to the adress in the array
    
displayInputLoop:
    	beq $t0, $s0,endDisplayInputLoop
    
   	 #Display number of the input
    	move $a0,$t0	
    	li $v0,1
    	syscall	
    
   	#Display the '.)' sign
    	la $a0,promptNumber
    	li $v0,4
    	syscall
    
   	 #Display the string
    	lw $a0,($t1)	
    	li $v0,4
    	syscall
    	
    
    	addi $t0,$t0,1	# increment count
    	addi $t1,$t1,4	# move pointer one character
    
    	j displayInputLoop
    
    
endDisplayInputLoop:   
    
    

   	 #Display the question
    	la $a0,promptChangeOrder
    	li $v0,4
   	syscall
    
    	#Ask for input there
   	li	$v0, 5 
    	syscall 
    
   	 slt $t0,$v0,$s0 		#if v0 < s0 set 1
   	 beq $t0, $zero, quit 		#If condition fails
    	
    
    	
    
   	 #initialise registers
   	 move $t0, $v0			#t0 holds the index of the element we want to change place
   	 li $t1,0	    		#t1 holds the count
   	 move $t2, $s1   		#t2 point to the first element of the array now
         
    	sll $t5, $t0, 2     		#store in t3 the adress of the element we want to change place
    	add $t5, $s1,$t5   		#get the adress of the element
    	lw $t3, 0($t5)     		#get the reference to the previous adress
    	
    	add $t0,$t0,1
    	
    	changePlace:
    		beq $t1, $t0,endChangePlace #if we reach the end
    		lw $t4, ($t2) #load the adress store at t0
    		sw $t3, ($t2)
    		move $t3,$t4
    		addi $t2, $t2, 4
    		addi $t1,$t1,1
    		j changePlace
  
    
	endChangePlace:
    	j displayAndChangeLoop	
    
    
    	quit:
    	
    	la $a0,promptQuit
    	li $v0,4
   	syscall
    	
    
    	# Exit
    	li $v0,10
	syscall		



		
##
## end
