/* 

#Filename: rscpue.java

#Author: Seth Roller

#Date: 4/17/19

#Problem: Will be implementing 16 bit 
addresses while maintaining backwards
compatability 

*/

import java.io.*;
import java.util.*;

public class rscpuecache
{

    //global variables of all the registers
    //within the rscpu

    static short AR = 0;
    static short PC = 0;

    //change AC and R to shorts
    static short AC = 0;
    static short R = 0;
    static byte flag = 0;
    
    static byte DR = 0;
    static byte IR = 0;
    static byte TR = 0;

    static int lines = 0;
    static byte[] memory = new byte[65536];
    
    public static void main(String args[])
	throws FileNotFoundException
    {
	//create a scanner to read in the file
	Scanner sc = new Scanner(System.in);
	String filename;
	
	System.out.println();
	System.out.println("rscpue written by Seth Roller");
	System.out.println("Will be implementing 16 bit "
			   + "addresses while maintaining\n"
			   + "backwards compatability.");
	System.out.println();
	System.out.print("Enter the file containing the program: ");
	
	filename = sc.nextLine();
	File input = new File(filename);
	Scanner reader = new Scanner(input);

	System.out.println();
	System.out.println("The file " + filename +
			   " opened");
	

	// while the reader has something next to read
	while (reader.hasNext())
	    {
	    //store as a string first
	    char hold;
	    char hold1;

	    char[] store = new char[2];
	    
	    // do charAt and then check if number vs letter
	    // for numbers just subtract by zero
	    
	    String temp = reader.next();
	    hold = temp.charAt(0);
	    hold1 = temp.charAt(1);

	    if (hold >= 'a')
		store[0] = (char)(hold - 'a' + 10);
	    else
		store[0] = (char)(hold - '0');
	    
	    if (hold1 >= 'a')
		store[1] = (char)(hold1 - 'a' + 10);
	    else
		store[1] = (char)(hold1 - '0');

	    byte one = (byte)store[0];
	    byte two = (byte)store[1];

	    int convert = (one << 4) | two;
	    byte insertt = (byte)(convert & (byte)(0b11111111));

	    memory[lines] = insertt;
	    lines++;

	    }
	   
	System.out.println("end of file reached");
		
	// calls fetch and performs a switch statement
	// on the global variable IR

	while (PC < lines)
	    {
		fetch(memory[PC]);
		
		switch(IR) {
		    
		case (byte)(0b00000000):
		    NOP();
		    break;
		case (byte)(0b00000001):
		    LDAC8();
		    break;
		case (byte)(0b00000010):
		    STAC8();
		    break;
		case (byte)(0b00000011):
		    MVAC8();
		    break;
		case (byte)(0b00000100):
		    MOVR8();
		    break;
		case (byte)(0b00000101):
		    System.out.println("JUMP instruction");
		    JUMP();
		    break;
		case (byte)(0b00000110):
		    JMPZ();
		    break;
		case (byte)(0b00000111):
		    JPNZ();
		    break;
		case (byte)(0b00010000):
		    JMPC();
		    break;
		case (byte)(0b00010001):
		    JV();
		    break;
		case (byte)(0b00010111):
		    JN();
		    break;
		case (byte)(0b00001000):
		    System.out.println("ADD8 Instruction");
		    ADD8();
		    break;
		case (byte)(0b00001001):
		    SUB8();
		    break;
		case (byte)(0b00001010):
		    INAC8();
		    break;
		case (byte)(0b00001011):
		    CLAC8();
		    break;
		case (byte)(0b00001100):
		    AND8();
		    break;
		case (byte)(0b00001101):
		    OR8();
		    break;
		case (byte)(0b00001110):
		    XOR8();
		    break;
		case (byte)(0b00001111):
		    NOT8();
		    break;
		case (byte)(0b00010010):
		    RL8();
		    break;
		case (byte)(0b00010011):
		    RR8();
		    break;
		case (byte)(0b00010100):
		    LSL8();
		    break;
		case (byte)(0b00010101):
		    LSR8();
		    break;
		case (byte)(0b00010110):
		    MVI8();
		    break;
		case (byte)(0b10000001):
		    LDAC16();
		    break;
		case (byte)(0b10000010):
		    STAC16();
		    break;
		case (byte)(0b10000011):
		    MVAC16();
		    break;
		case (byte)(0b10000100):
		    MOVR16();
		    break;
		case (byte)(0b10001000):
		    System.out.println("ADD16 Instruction");
		    ADD16();
		    break;
		case (byte)(0b10001001):
		    SUB16();
		    break;
		case (byte)(0b10001010):
		    INAC16();
		    break;
		case (byte)(0b10001011):
		    CLAC16();
		    break;
		case (byte)(0b10001100):
		    AND16();
		    break;
		case (byte)(0b10001101):
		    OR16();
		    break;
		case (byte)(0b10001110):
		    XOR16();
		    break;
		case (byte)(0b10001111):
		    NOT16();
		    break;
		case (byte)(0b10010010):
		    RL16();
		    break;
		case (byte)(0b10010011):
		    RR16();
		    break;
		case (byte)(0b10010100):
		    LSL16();
		    break;
		case (byte)(0b10010101):
		    LSR16();
		    break;
		case (byte)(0b10010110):
		    MVI16();
		    break;
		case (byte)(0b11111111):
		    HALT();
		    break;
		default:
		    System.exit(1);
		    break;
		
		}

		//print out the following info after every
		//instruction

		if ((byte)(IR & (byte)0x80) == (byte)(0x80) &&
		    (byte)IR != (byte)(0xff)) {
		
		    System.out.printf("Instruction execution complete:");
		    System.out.printf(" AC=%d R=%d ZCVN=%d%d%d%d ",
				      (int)(AC & (int)(0xffff)),
				      (int)(R & (int)(0xffff)),
				      (byte)((flag & (byte)0x08) >> 3),
				  (byte)((flag & (byte)0x04) >> 2),
				      (byte)((flag & (byte)0x02) >> 1),
				      (byte)(flag & (byte)0x01));
		    System.out.printf("AR=%d PC=%d DR=%d", AR, PC
				      , (int)(DR & (int)(0xff)));
		    System.out.println();
		}

		else {
		    System.out.printf("Instruction execution complete:");
		    System.out.printf(" AC=%d R=%d ZCVN=%d%d%d%d ",
				      (int)(AC & (int)(0xff)),
				      (int)(R & (int)(0xff)),
				      (byte)((flag & (byte)0x08) >> 3),
				  (byte)((flag & (byte)0x04) >> 2),
				      (byte)((flag & (byte)0x02) >> 1),
				      (byte)(flag & (byte)0x01));
		    System.out.printf("AR=%d PC=%d DR=%d", AR, PC
				      , (int)(DR & (int)(0xff)));
		    System.out.println();
		}
		
		// if HALT is executed exit after
		// the execution statement
		if (IR == (byte)(0xff))
		    System.exit(1);
	    }
    }
    

    public static void fetch(byte ref) {
	//contained fetch in one function
	//through several assignments and
	//print statements
	
	AR = PC;
	System.out.printf("fetch1: AR = %d", AR);
	System.out.printf(" PC = %d", PC);
	System.out.println();
	DR = ref;
	PC += (byte)1;
	System.out.printf("fetch2: DR = %d",
			  (int)(DR & (int)(0xff)));
	System.out.printf(" PC = %d", PC);
	System.out.println();
	IR = DR;
	AR = PC;
	System.out.printf("fetch3: IR = %d ",
			  (int)(IR & (int)(0xff)));
	System.out.printf(" AR = %d", AR);
	System.out.println();
	
    }
    
    public static void NOP() {
	// you got here, and it does nothing
	System.out.println("NOP instruction");
    }

    public static void HALT() {
	//this halts the reading
	System.out.println("HALT instruction");
    }

    public static void LDAC8() {
	DR = (byte)memory[AR];
	PC += (byte)1;
	AR += (byte)1;
	// print out values of LDAC1
	System.out.println("LDAC8 instruction");
	System.out.printf("LDAC8-1: DR = %d PC = %d AR = %d",
			  (int)(DR & (int)(0xff)), PC, AR);
	System.out.println();
	
	TR = DR;
	DR = (byte)memory[AR];
	PC += (byte)1;
	//print out values of LDAC2
	System.out.printf("LDAC8-2: TR = %d DR = %d AR = %d PC = %d",
			  (int)(TR & (int)(0xff)), 
			  (int)(DR & (int)(0xff)), AR, PC);
	System.out.println();

	AR = (short)(0x0000 | ((short)DR & 0x00ff));
	AR = (short)(AR | (((short)TR << 7) & 0xff00));
	
	//print out values of LDAC3
	System.out.printf("LDAC8-3: AR = %d", AR);
	System.out.println();

	DR = (byte)memory[AR];
	
	//print out values of LDAC4
	System.out.printf("LDAC8-4: DR = %d", (int)(DR & (int)(0xff)));
	System.out.println();

	AC = (short)((short)(AC & (short)0xff00) | (short)(DR & 0x00ff));
	//print out values of LDAC5
	System.out.printf("LDAC8-5: AC = %d", (int)(AC & (int)(0xff)));
	System.out.println();
	 
    }

    //finished
    public static void LDAC16() {
	DR = (byte)memory[AR];
	PC += (byte)1;
	AR += (byte)1;
	// print out values of LDAC1
	System.out.println("LDAC16 instruction");
	System.out.printf("LDAC16-1: DR = %d PC = %d AR = %d",
			  (int)(DR & (int)(0xff)), PC, AR);
	System.out.println();
	
	TR = DR;
	DR = (byte)memory[AR];
	PC += (byte)1;
	//print out values of LDAC2
	System.out.printf("LDAC16-2: TR = %d DR = %d AR = %d PC = %d",
			  (int)(TR & (int)(0xff)), 
			  (int)(DR & (int)(0xff)), AR, PC);
	System.out.println();

	AR = (short)(0x0000 | ((short)DR & 0x00ff));
	AR = (short)(AR | (((short)TR << 7) & 0xff00));
	
	//print out values of LDAC3
	System.out.printf("LDAC16-3: AR = %d", AR);
	System.out.println();

	DR = (byte)memory[AR];
	
	//print out values of LDAC4
	System.out.printf("LDAC16-4: DR = %d", (int)(DR & (int)(0xff)));
	System.out.println();


	AC = (short)((short)(AC & (short)0x00ff) |
		     (short)((short)DR & 0x00ff) << 8);

	//print out values of LDAC5
	System.out.printf("LDAC16-5: AC = %d", (int)(AC & (int)(0xff)));
	System.out.println();

	AR += (byte)1;
	DR = (byte)memory[AR];

	AC = (short)((short)(AC & (short)0xff00)
		     | (short)((short)DR & (short)0x00ff));

	//print out values of LDAC6
	System.out.printf("LDAC16-6: DR = %d AC = %d AR = %d",
			  (int)(DR & (int)0xff),(int)(AC & (int)0xffff),
			  AR);
	System.out.println();
    }

    //finished
    public static void STAC8() {
	DR = (byte)memory[AR];
        PC += (byte)1;
        AR += (byte)1;
	//print out values of STAC1

	System.out.println("STAC8 instruction");
        System.out.printf("STAC8-1: DR = %d PC = %d AR = %d",
			  (int)(DR & (int)(0xff))
			  , PC, AR);
        System.out.println();

        TR = DR;
        DR = (byte)memory[AR];
        PC += (byte)1;
	// print out values of STAC2

        System.out.printf("STAC8-2: TR = %d DR = %d AR = %d PC = %d",
			  (int)(TR & (int)(0xff)),
                          (int)(DR & (int)(0xff)),
			  AR, PC);
	System.out.println();

        AR = (short)(DR & (short)0x00ff);
        AR = (short)(AR | (((short)TR << 8) & 0xff00));
	
	//print out values of STAC3
	
        System.out.printf("STAC8-3: AR = %d M[AR] = %d", 
			  AR, (int)(memory[AR] & (int)(0xff)));
        System.out.println();

	memory[AR] = (byte)(AC & (short)0x00ff);

	//print out values of STAC4

	System.out.println(Integer.toBinaryString(AC & 0xFFFF));
	System.out.printf("STAC8-4: AC = %d M[AR] = %d", 
			  (int)(AC & (int)(0xff)),
			  (int)(memory[AR] & (int)(0xff)));
	System.out.println();

    }

    //finished
    public static void STAC16() {
	DR = (byte)memory[AR];
        PC += (byte)1;
        AR += (byte)1;
	//print out values of STAC1

	System.out.println("STAC16 instruction");
        System.out.printf("STAC16-1: DR = %d PC = %d AR = %d",
			  (int)(DR & (int)(0xff))
			  , PC, AR);
        System.out.println();

        TR = DR;
        DR = (byte)memory[AR];
        PC += (byte)1;
	// print out values of STAC2

        System.out.printf("STAC16-2: TR = %d DR = %d AR = %d PC = %d",
			  (int)(TR & (int)(0xff)),
                          (int)(DR & (int)(0xff)),
			  AR, PC);
	System.out.println();

        AR = (short)(0x0000 | ((short)DR & 0x00ff));
        AR = (short)(AR | (((short)TR << 7) & 0xff00));
	
	//print out values of STAC3
	
        System.out.printf("STAC16-3: AR = %d M[AR] = %d", 
			  AR, (int)(memory[AR] & (int)(0xff)));
        System.out.println();

	memory[AR] = (byte)(((short)(AC & (short)0xff00) >> 8)
			    & (short)0x00ff);

	//print out values of STAC4

	System.out.printf("STAC16-4: AR = %d M[AR] = %d", 
			  AR, (int)(memory[AR] & (int)(0xff)));
	System.out.println();

	AR += (byte)1;
        memory[AR] = (byte)(AC & (short)0x00ff);

	System.out.printf("STAC16-5: AR = %d M[AR] = %d", 
			  AR, (int)(memory[AR] & (int)(0xff)));
	System.out.println();

	
    }

    //finished
    public static void MVAC8() {
	R = (short)(AC & (short)(0x00ff));
	System.out.println("MVAC8 instruction");
	System.out.printf("MVAC8-1 R = %d", (int)(R & (int)(0xff)));
	System.out.println();
	
    }

    //finished
    public static void MVAC16() {
	R = AC;
        System.out.println("MVAC16 instruction");
        System.out.printf("MVAC16-1 R = %d", (int)(R & (int)(0xffff)));
        System.out.println();

    }

    //finished
    public static void MOVR8() {
	AC = (short)(R & (short)0x00ff);
	System.out.println("MOVR8 instruction");
	System.out.printf("MOVR8-1 AC = %d", (int)(AC & (int)(0xff)));
	System.out.println();

    }

    //finished
    public static void MOVR16() {
        AC = R;
        System.out.println("MOVR16 instruction");
        System.out.printf("MOVR16-1 AC = %d", (int)(AC & (int)(0xff)));
        System.out.println();

    }

    public static void JUMP() {
	DR = (byte)memory[AR];
        PC += (byte)1;
        AR += (byte)1;

        System.out.printf("JUMP1: DR = %d PC = %d AR = %d",
                          (int)(DR & (int)(0xff)), PC, AR);
        System.out.println();

        TR = DR;
        DR = (byte)memory[AR];
        PC += (byte)1;

	System.out.printf("JUMP2: TR = %d DR = %d AR = %d PC = %d",
                          (int)(TR & (int)(0xff)),
                          (int)(DR & (int)(0xff)), AR, PC);
        System.out.println();

        PC = (short)(0x0000 | ((short)DR & 0x00ff));
        PC = (short)(PC | (((short)TR << 7) & 0xff00));
	
	System.out.printf("JUMP3: PC = %d", PC);
	System.out.println();
    }

    public static void JMPZ() {
	System.out.println("JMPZ instruction");
	if ((byte)(flag & (byte)(0x08)) == (byte)(0x08))
	    JUMP();
	else {
	    PC += (byte)2;
	}
    }

    public static void JPNZ() {
	System.out.println("JPNZ instruction");
        if ((byte)(flag & (byte)(0x08)) == (byte)(0x00))
            JUMP();
	else {
	    PC += (byte)2;
	}
    }

    public static void JMPC() {
	System.out.println("JPMC instruction");
        if ((byte)(flag & (byte)(0x04)) == (byte)(0x04))
            JUMP();
	else {
	    PC += (byte)2;
	}
    }

    public static void JV() {
	System.out.println("JV instruction");
        if ((byte)(flag & (byte)(0x02)) == (byte)(0x02))
            JUMP();
	else {
	    PC += (byte)2;
	}
    }

    public static void JN() {
	System.out.println("JN instruction");
        if ((byte)(flag & (byte)(0x01)) == (byte)(0x01))
            JUMP();
	else {
	    PC += (byte)2;
	}
    }

    //finished
    public static void ADD8() {

	//C flag check
	if ((short)((short)(AC >> 7) & (short)0x0001) == (short)0x0001 &&
	    (short)((short)(R >> 7) & (short)0x0001) == (short)0x0001)
            flag = (byte)(flag | (byte)(0b00000100));
	
	else {
	    //System.printf("%d %d", (byte)((TR >> 7) & (byte)0x01),
			  
	    if ((short)((short)((short)(AC & (short)0x00ff) >> 7) &
			(short)0x0001)== (short)0x0001 &&
		((short)((short)(R & (short)0x00ff) >> 7)
		 & (short)0x0001) == (short)0x0000)
		flag = (byte)(flag | (byte)(0b00000100));
	    else if ((short)((short)((short)(AC & (short)0x00ff) >> 7)
			     & (short)1) == (short)0
		     && ((short)((short)((short)(R & (short)0x00ff) >> 7)
				 & (short)1) == (short)1)
		     && ((short)(AC & 0x00ff) != (short)0))
		flag = (byte)(flag | (byte)(0b00000100));
	    
	    
	    else
		flag = (byte)(flag & (byte)(0b00001011));
	    
	}

	TR = (byte)(AC & (short)0x00ff);
	TR = (byte)(TR + (byte)(R & (short)0x00ff));
	
	//V flag check
	if ((byte)(AC & (short)(0x0080)) == (byte)(R & (short)0x0080)) {
	    if ((byte)(AC & (short)(0x0080)) >> 7 !=
		(byte)((TR & (byte)0x80)>> 7))
		flag = (byte)(flag | (byte)(0x02));
	    else
		flag = (byte)(flag & (byte)(0b00001101));
	}

        else {
            //if ((byte)(flag & (byte)(0x04)) == (AC & (byte)(0x80)))
	    flag = (byte)(flag & (byte)(0b00001101));
            
        }
	
	AC = (short)((short)(AC & (short)0xff00) |
		     (short)((short)TR & (short)0x00ff));

	//Z flag check
	if ((byte)(AC & (short)(0x00ff)) == (byte)(0x00))
            flag = (byte)(flag | (0b00001000));
        else
            flag = (byte)(flag & (0b11110111));

	//N flag check
	if ((short)(AC & (short)(0x0080)) == (short)(0x0080))
            flag = (byte)(flag | (byte)(0x01));
        else
            flag = (byte)(flag & (byte)(0b00001110));

	System.out.printf("AC = %d ZCVN = %d%d%d%d",
                          (int)(AC & (int)(0xff)),
                          (byte)((flag & (byte)0x08) >> 3),
                          (byte)((flag & (byte)0x04) >> 2),
                          (byte)((flag & (byte)0x02) >> 1),
                          (byte)(flag & (byte)0x01));
	System.out.println();
    }

    //finished
    public static void ADD16() {

	TR = (byte)((short)(AC & (short)0xff00) >> 8);
	//C flag check
	if ((short)((short)(AC >> 15) & (short)0x0001) == (short)1 &&
	    (short)((short)(R >> 15) & (short)0x0001) == (short) 1)
            flag = (byte)(flag | (byte)(0b00000100));
	
	else {
	    //System.printf("%d %d", (byte)((TR >> 7) & (byte)0x01),
			  
	    if ((short)((short)(AC >> 15) & (short)0x0001) == (short)1 &&
		(short)((R >> 15) & (short)1) == (short)0)
		flag = (byte)(flag | (byte)(0b00000100));
	    else if ((short)((short)(AC >> 15) & (short)1) == (short)0 &&
		     (short)((short)(R >> 15) & (short)1) == (short)1)
		flag = (byte)(flag | (byte)(0b00000100));
	    
	    
	    else
		flag = (byte)(flag & (byte)(0b00001011));
	    
	}
	
	AC = (short)(AC + R);
	
	//Z flag check
	if (AC == (short)0)
            flag = (byte)(flag | (0b00001000));
        else
            flag = (byte)(flag & (0b11110111));

	//N flag check
	if ((short)(AC & (short)(0x8000)) == (short)(0x8000))
            flag = (byte)(flag | (byte)(0x01));
        else
            flag = (byte)(flag & (byte)(0b00001110));

	//V flag check
        if ((short)((short)(TR & (byte)0x80) & (short)0x00ff)
	    == ((short)(R >>8) & (short)0x0080)) {
	    if (((short)((short)(TR & (byte)0x80) >> 7) & (short)0x00ff)
		!=(short)((short)(AC & (short)0x8000) >> 15))
		flag = (byte)(flag | (byte)(0x02));
	    else
		flag = (byte)(flag & (byte)(0b00001101));
	}

        else
	    flag = (byte)(flag & (byte)(0b00001101));
			   
	System.out.printf("AC = %d ZCVN = %d%d%d%d",
                          (int)(AC & (int)(0xffff)),
                          (byte)((flag & (byte)0x08) >> 3),
                          (byte)((flag & (byte)0x04) >> 2),
                          (byte)((flag & (byte)0x02) >> 1),
                          (byte)(flag & (byte)0x01));
	System.out.println();
    }

    //finished
    public static void SUB8() {
	System.out.println("SUB8 Instruction");
	R = (short)((short)(R & (short)0xff00) |
		    ((short)(~R) & (short)0x00ff));
	R = (short)(R + (short)0x0001);
	ADD8();
	R = (short)((short)(R & (short)0xff00) |
		    ((short)(~R) & (short)0x00ff));
	R = (short)(R + (short)0x0001);
	
    }

    //finished
    public static void SUB16() {
	System.out.println("SUB16 Instruction");
	R = (short)(~R);
	R = (short)(R + (short)0x0001);
	ADD16();
	R = (short)(~R);
	R = (short)(R + (short)0x0001);
	
    }

    //finished
    public static void INAC8() {
	TR = (byte)(AC & (short)0x00ff);
	//TR will hold the old contents to check for
	//overflow

	TR = (byte)(TR + (byte)1); // incrememnt

	// C flag check
	// check if the most significant bit is 1
	// in old contents stored in TR
	// if so and the new result has a 0 in the
	// significant bit the carry flag is raised
	if ((byte)((AC & (short)0x0080) >> 7) == (byte)1 &&
	    (byte)((TR & 0x80) >> 7) == (byte)0)
	    flag = (byte)(flag | (byte)(0b00000100));
	else
	    flag = (byte)(flag & (byte)(0b00001011));

	// V flag check
	// check to see if most significant bit is one
	// because you cant get a overflow if adding pos
	// to neg
	if ((byte)(AC & (short)(0x0080)) == (byte)(0x80))
	    flag = (byte)(flag & (byte)(0b00001101));
	
	// else the original AC was positive
	else
	    {
		// if the carry is equal to most significant bit
		// in the new result then set V flag to 0
		if ((byte)(flag & (byte)(0x04)) == (TR & (byte)(0x80)))
		    flag = (byte)(flag & (byte)(0b00001101));
		else
		    flag = (byte)(flag | (byte)(0x02));
		
	    }

	AC = (short)((short)(AC & (short)0xff00) | ((short)TR & 0x00ff));
	
	// Z flag check
	if ((byte)(AC & (short)(0x00ff)) == (byte)(0x00))
	    flag = (byte)(flag | (0b00001000));
	else
	    flag = (byte)(flag & (0b11110111));

	// N flag check
	if ((short)(AC & (short)(0x0080)) == (short)(0x0080)) 
	    flag = (byte)(flag | (byte)(0x01));
	else
	    flag = (byte)(flag & (byte)(0b00001110));

	
	System.out.println("INAC8 instruction");
	System.out.printf("AC = %d ZCVN = %d%d%d%d",
			  (int)(AC & (int)(0xff)),
			  (byte)((flag & (byte)0x08) >> 3),
			  (byte)((flag & (byte)0x04) >> 2),
			  (byte)((flag & (byte)0x02) >> 1),
			  (byte)(flag & (byte)0x01));
	System.out.println();
    }
    //finished
    public static void INAC16() {
	TR = (byte)((short)(AC & (short)0xff00) >> 8);
	//TR will hold the old contents to check for
	//overflow

	AC = (short)(AC + (short)1); // incrememnt

	
	// Z flag check
	if (AC == (short)0)
	    flag = (byte)(flag | (0b00001000));
	else
	    flag = (byte)(flag & (0b11110111));

	// N flag check
	if ((short)(AC & (short)(0x8000)) == (short)(0x8000)) 
	    flag = (byte)(flag | (byte)(0x01));

	else
	    flag = (byte)(flag & (byte)(0b00001110));

	// C flag check
	// check if the most significant bit is 1
	// in old contents stored in TR
	// if so and the new result has a 0 in the
	// significant bit the carry flag is raised
	if ((byte)((TR & 0x80) >> 7) == (byte)1 &&
	    (short)((AC & 0x0080) >> 7) == (byte)0)
	    flag = (byte)(flag | (byte)(0b00000100));
	else
	    flag = (byte)(flag & (byte)(0b00001011));

	// V flag check
	// check to see if most significant bit is one
	// because you cant get a overflow if adding pos
	// to neg
	if ((byte)(TR & (byte)(0x80)) == (byte)(0x80))
	    flag = (byte)(flag & (byte)(0b00001101));
	
	// else the original AC was positive
	else
	    {
		// if the carry is equal to most significant bit
		// in the new result then set V flag to 0
		if ((byte)(flag & (byte)(0x04)) ==
		    (byte)(AC & (short)(0x0080)))
		    flag = (byte)(flag & (byte)(0b00001101));
		else
		    flag = (byte)(flag | (byte)(0x02));
		
	    }

	System.out.println("INAC instruction");
	System.out.printf("AC = %d ZCVN = %d%d%d%d",
			  (int)(AC & (int)(0xffff)),
			  (byte)((flag & (byte)0x08) >> 3),
			  (byte)((flag & (byte)0x04) >> 2),
			  (byte)((flag & (byte)0x02) >> 1),
			  (byte)(flag & (byte)0x01));
	System.out.println();
    }

    //finished
    public static void CLAC8() {
	AC = (short)(AC & (short)0xff00);
	//reset flags

	flag = (byte)(flag & (byte)(0b00000000));
	flag = (byte)(flag | (byte)(0b00001000));
	
	System.out.println("CLAC8 instruction");
	System.out.printf("AC = %d ZCVN = %d%d%d%d",
			  (int)(AC & (int)(0xff)),
			  (byte)((flag & (byte)0x08) >> 3),
			  (byte)((flag & (byte)0x04) >> 2),
			  (byte)((flag & (byte)0x02) >> 1),
			  (byte)(flag & (byte)0x01));
	System.out.println();
    }

    //finished
    public static void CLAC16() {
        AC = (short)0;

        flag = (byte)(flag & (byte)(0b00000000));
	flag = (byte)(flag | (byte)(0b00001000));
	
        System.out.println("CLAC16 instruction");
        System.out.printf("AC = %d ZCVN = %d%d%d%d",
			  (int)(AC & (int)(0xffff)),
                          (byte)((flag & (byte)0x08) >> 3),
                          (byte)((flag & (byte)0x04) >> 2),
                          (byte)((flag & (byte)0x02) >> 1),
                          (byte)(flag & (byte)0x01));
        System.out.println();
    }

    //finished
    public static void AND8() {
	AC = (short)((short)(AC & (short)0xff00)
		     | (short)(R & (short)0x00ff));
	
	//Z flag check
	if ((byte)(AC & (short)(0x00ff)) == (byte)(0x00))
            flag = (byte)(flag | (0b00001000));
        else
            flag = (byte)(flag & (0b11110111));

	//N flag check
	if ((short)(AC & (short)(0x0080)) == (short)(0x0080))
            flag = (byte)(flag | (byte)(0x01));

        else
            flag = (byte)(flag & (byte)(0b00001110));

		
	System.out.println("AND8 instruction");
        System.out.printf("AC = %d ZCVN = %d%d%d%d",
			  (int)(AC & (int)(0xff)),
                          (byte)((flag & (byte)0x08) >> 3),
	                  (byte)((flag & (byte)0x04) >> 2),
                          (byte)((flag & (byte)0x02) >> 1),
                          (byte)(flag & (byte)0x01));
        System.out.println();
    }

    //finished
    public static void AND16() {
	AC = (short)(AC & R);
	
	//Z flag check
	if (AC == (short)0)
            flag = (byte)(flag | (0b00001000));
        else
            flag = (byte)(flag & (0b11110111));

	//N flag check
	if ((short)(AC & (short)(0x8000)) == (short)(0x8000))
            flag = (byte)(flag | (byte)(0x01));

        else
            flag = (byte)(flag & (byte)(0b00001110));

	System.out.println("AND16 instruction");
        System.out.printf("AC = %d ZCVN = %d%d%d%d",
			  (int)(AC & (int)(0xffff)),
                          (byte)((flag & (byte)0x08) >> 3),
	                  (byte)((flag & (byte)0x04) >> 2),
                          (byte)((flag & (byte)0x02) >> 1),
                          (byte)(flag & (byte)0x01));
        System.out.println();
    }

    //finished
    public static void OR8() {
	AC = (short)(AC | (short)(R & (short)0x00ff));
	
	//Z flag check
	if ((byte)(AC & (short)(0x00ff)) == (byte)(0x00))
            flag = (byte)(flag | (0b00001000));
        else
            flag = (byte)(flag & (0b11110111));

	//N flag check
        if ((short)(AC & (short)(0x0080)) == (short)(0x0080)) 
            flag = (byte)(flag | (byte)(0x01));
        else
            flag = (byte)(flag & (byte)(0b00001110));

	System.out.println("OR8 instruction");
        System.out.printf("AC = %d ZCVN = %d%d%d%d",
			  (int)(AC & (int)(0xff)),
                          (byte)((flag & (byte)0x08) >> 3),
	                  (byte)((flag & (byte)0x04) >> 2),
                          (byte)((flag & (byte)0x02) >> 1),
                          (byte)(flag & (byte)0x01));
        System.out.println();
    }

    //finished
    public static void OR16() {
	AC = (short)(AC | R);
	
	//Z flag check
	if (AC == (short)0)
            flag = (byte)(flag | (0b00001000));
        else
            flag = (byte)(flag & (0b11110111));

	//N flag check
        if ((short)(AC & (short)(0x8000)) == (short)(0x8000))
            flag = (byte)(flag | (byte)(0x01));

        else
            flag = (byte)(flag & (byte)(0b00001110));

	System.out.println("OR16 instruction");
        System.out.printf("AC = %d ZCVN = %d%d%d%d",
			  (int)(AC & (int)(0xfff)),
                          (byte)((flag & (byte)0x08) >> 3),
	                  (byte)((flag & (byte)0x04) >> 2),
                          (byte)((flag & (byte)0x02) >> 1),
                          (byte)(flag & (byte)0x01));
        System.out.println();
    }

    //finished
    public static void XOR8() {
	TR = (byte)(AC & (short)0x00ff);
	TR = (byte)(TR ^ (byte)(R & (short)0x00ff));
	AC = (short)(AC & 0xff00);
	AC = (short)(AC | (short)((short)TR & 0x00ff));
	
	//Z flag check
	if ((byte)(AC & (short)(0x00ff)) == (byte)(0x00))
            flag = (byte)(flag | (0b00001000));
        else
            flag = (byte)(flag & (0b11110111));

	//N flag check
        if ((short)(AC & (short)(0x0080)) == (short)(0x0080))
            flag = (byte)(flag | (byte)(0x01));
        else
            flag = (byte)(flag & (byte)(0b00001110));

	System.out.println("XOR8 instruction");
        System.out.printf("AC = %d ZCVN = %d%d%d%d",
			  (int)(AC & (int)(0xff)),
                          (byte)((flag & (byte)0x08) >> 3),
	                  (byte)((flag & (byte)0x04) >> 2),
                          (byte)((flag & (byte)0x02) >> 1),
                          (byte)(flag & (byte)0x01));
        System.out.println();
    }

    //finished
    public static void XOR16() {
	AC = (short)(AC ^ R);
	//Z flag check
	if (AC == (short)0)
            flag = (byte)(flag | (0b00001000));
        else
            flag = (byte)(flag & (0b11110111));

	//N flag check
        if ((short)(AC & (short)(0x8000)) == (short)(0x8000)) 
            flag = (byte)(flag | (byte)(0x01));

        else
            flag = (byte)(flag & (byte)(0b00001110));


	System.out.println("XOR16 instruction");
        System.out.printf("AC = %d ZCVN = %d%d%d%d",
			  (int)(AC & (int)(0xffff)),
                          (byte)((flag & (byte)0x08) >> 3),
	                  (byte)((flag & (byte)0x04) >> 2),
                          (byte)((flag & (byte)0x02) >> 1),
                          (byte)(flag & (byte)0x01));
        System.out.println();
    }

    //finished
    public static void NOT8() {
	//this is the old top half
	TR = (byte)((short)(AC >> 8) & (short)0x00ff);
	AC = (short)(~AC);
	AC = (short)(AC & 0x00ff);
	AC = (short)(AC | ((short)TR << 8));
	
	//Z flag check
	if ((byte)(AC & (short)(0x00ff)) == (byte)(0x00))
            flag = (byte)(flag | (0b00001000));
        else
            flag = (byte)(flag & (0b11110111));

	//N flag check
	if ((short)(AC & (short)(0x0080)) == (short)(0x0080))
            flag = (byte)(flag | (byte)(0x01));
        else
            flag = (byte)(flag & (byte)(0b00001110));

	System.out.println("NOT8 instruction");
        System.out.printf("AC = %d ZCVN = %d%d%d%d",
			  (int)(AC & (int)(0xff)),
                          (byte)((flag & (byte)0x08) >> 3),
	                  (byte)((flag & (byte)0x04) >> 2),
                          (byte)((flag & (byte)0x02) >> 1),
                          (byte)(flag & (byte)0x01));
        System.out.println();
    }

    //finished
     public static void NOT16() {
	 AC = (short)(~AC);
	
	//Z flag check
	if (AC == (short)0)
            flag = (byte)(flag | (0b00001000));
        else
            flag = (byte)(flag & (0b11110111));

	//N flag check
	if ((short)(AC & (short)(0x8000)) == (short)(0x8000))
            flag = (byte)(flag | (byte)(0x01));
        else
            flag = (byte)(flag & (byte)(0b00001110));

	System.out.println("NOT16 instruction");
        System.out.printf("AC = %d ZCVN = %d%d%d%d",
			  (int)(AC & (int)(0xffff)),
                          (byte)((flag & (byte)0x08) >> 3),
	                  (byte)((flag & (byte)0x04) >> 2),
                          (byte)((flag & (byte)0x02) >> 1),
                          (byte)(flag & (byte)0x01));
        System.out.println();
    }
    //finished
    public static void RL8() {

	//C flag set
	if ((short)(AC & (short)(0x0080)) == (short)(0x0080))
	    flag = (byte)(flag | (byte)(0x04));
	else
	    flag = (byte)(flag & (byte)(0b00001011));

	TR = (byte)(AC & (short)0x00ff);
	TR = (byte)(TR << 1);
	
	if ((short)(AC & (short)(0x0080)) == (short)(0x0080))
	    TR = (byte)(TR | (byte)(0x01));

	AC = (short)((AC & (short)0xff00) |
		     (short)((short)TR & (short)0x00ff));
	
	//Z flag check
	if ((byte)(AC & (short)(0x00ff)) == (byte)(0x00))
            flag = (byte)(flag | (0b00001000));
        else
            flag = (byte)(flag & (0b00000111));

	//N flag check
	if ((short)(AC & (short)(0x0080)) == (short)(0x0080)) 
            flag = (byte)(flag | (byte)(0x01));
        else
            flag = (byte)(flag & (byte)(0b00001110));


	System.out.println("RL8 instruction");
        System.out.printf("AC = %d ZCVN = %d%d%d%d",
                          (int)(AC & (int)(0xff)),
                          (byte)((flag & (byte)0x08) >> 3),
                          (byte)((flag & (byte)0x04) >> 2),
                          (byte)((flag & (byte)0x02) >> 1),
                          (byte)(flag & (byte)0x01));
        System.out.println();
    }

    //finished
    public static void RL16() {
	
	TR = (byte)((short)(AC & (short)0xff00) >> 8);
	//C flag set
	if ((short)(AC & (short)(0x8000)) == (short)(0x8000))
	    flag = (byte)(flag | (byte)(0x04));
	else
	    flag = (byte)(flag & (byte)(0b00001011));

	AC = (short)(AC << 1);
	
	if ((byte)(TR & (byte)(0x80)) == (byte)(0x80))
	    AC = (short)(AC | (short)(0x0001));
	
	//Z flag check
	if (AC == (short)0)
            flag = (byte)(flag | (0b00001000));
        else
            flag = (byte)(flag & (0b00000111));

	//N flag check
	if ((short)(AC & (short)(0x8000)) == (short)(0x8000)) 
            flag = (byte)(flag | (byte)(0x01));
        else
            flag = (byte)(flag & (byte)(0b00001110));


	System.out.println("RL16 instruction");
        System.out.printf("AC = %d ZCVN = %d%d%d%d",
                          (int)(AC & (int)(0xffff)),
                          (byte)((flag & (byte)0x08) >> 3),
                          (byte)((flag & (byte)0x04) >> 2),
                          (byte)((flag & (byte)0x02) >> 1),
                          (byte)(flag & (byte)0x01));
        System.out.println();
    }

    //finished
    public static void RR16() {
	TR = (byte)(AC & (short)0x00ff);
	AC = (short)(AC >> 1);
	AC = (short)(AC & (short)(0x7fff));
	if ((byte)(TR & (byte)(0x01)) == (byte)0x01)
	    AC = (short)(AC | (short)(0x8000));
	
	//Z flag check
	if (AC == (short)0)
            flag = (byte)(flag | (0b00001000));
        else
            flag = (byte)(flag & (0b00000111));

	//N flag check
	if ((short)(AC & (short)(0x8000)) == (short)(0x8000))
            flag = (byte)(flag | (byte)(0x01));
        else
            flag = (byte)(flag & (byte)(0b00001110));

	//C flag set
	if ((byte)(TR & (byte)(0x01)) == (byte)0x01)
	    flag = (byte)(flag | (byte)(0x04));
	else
	    flag = (byte)(flag & (byte)(0b00001011));


	System.out.println("RR16 instruction");
        System.out.printf("AC = %d ZCVN = %d%d%d%d",
                          (int)(AC & (int)(0xffff)),
                          (byte)((flag & (byte)0x08) >> 3),
                          (byte)((flag & (byte)0x04) >> 2),
                          (byte)((flag & (byte)0x02) >> 1),
                          (byte)(flag & (byte)0x01));
        System.out.println();
    }

    //finished
    public static void RR8() {

	//C flag set
	if ((byte)(AC & (short)(0x0001)) == (byte)1)
	    flag = (byte)(flag | (byte)(0x04));
	else
	    flag = (byte)(flag & (byte)(0b00001011));
	
	TR = (byte)(AC & (short)0x00ff);
	TR = (byte)(TR >> 1);
	TR = (byte)(TR & (0b01111111));
	if ((byte)(AC & (short)(0x0001)) == (byte)1)
	    TR = (byte)(TR | (byte)(0x80));

	AC = (short)((AC & (short)0xff00) |
		     (short)((short)(TR & (short)0x00ff)));
	
	//Z flag check
	if ((short)(AC & (short)0x00ff) == (short)0x0000)
            flag = (byte)(flag | (0b00001000));
        else
            flag = (byte)(flag & (0b00000111));

	//N flag check
	if ((short)(AC & (short)(0x0080)) == (short)(0x0080))
            flag = (byte)(flag | (byte)(0x01));
        else
            flag = (byte)(flag & (byte)(0b00001110));


	System.out.println("RR8 instruction");
        System.out.printf("AC = %d ZCVN = %d%d%d%d",
                          (int)(AC & (int)(0xff)),
                          (byte)((flag & (byte)0x08) >> 3),
                          (byte)((flag & (byte)0x04) >> 2),
                          (byte)((flag & (byte)0x02) >> 1),
                          (byte)(flag & (byte)0x01));
        System.out.println();
    }

    //finished
    public static void LSL8() {

	//C flag
	if ((short)(AC & (short)(0x0080)) == (short)(0x0080))
            flag = (byte)(flag | (byte)(0x04));
        else
            flag = (byte)(flag & (byte)(0b00001011));

	TR = (byte)(AC & (short)0x00ff);
	TR = (byte)(TR << 1);
	
	AC = (short)((AC & (short)0xff00) |
		     (short)((short)TR & (short)0x00ff));

	//Z flag
	if ((byte)(AC & (short)(0x00ff)) == (byte)(0x00))
            flag = (byte)(flag | (0b00001000));
        else
            flag = (byte)(flag & (0b00000111));

	//N flag
	if ((short)(AC & (short)(0x0080)) == (short)(0x0080))
            flag = (byte)(flag | (byte)(0x01));
        else
            flag = (byte)(flag & (byte)(0b00001110));

	
	System.out.println("LSL8 instruction");
        System.out.printf("AC = %d ZCVN = %d%d%d%d",
                          (int)(AC & (int)(0xff)),
                          (byte)((flag & (byte)0x08) >> 3),
                          (byte)((flag & (byte)0x04) >> 2),
                          (byte)((flag & (byte)0x02) >> 1),
                          (byte)(flag & (byte)0x01));
        System.out.println();
    }

    //finished
    public static void LSL16() {
	//C flag
	if ((short)(AC & (short)(0x8000)) == (short)(0x8000))
            flag = (byte)(flag | (byte)(0x04));
        else
            flag = (byte)(flag & (byte)(0b00001011));
	
	AC = (short)(AC << 1);
	
	//Z flag
	if (AC == (short)0)
            flag = (byte)(flag | (0b00001000));
        else
            flag = (byte)(flag & (0b00000111));

	//N flag
	if ((short)(AC & (short)(0x8000)) == (short)(0x8000))
            flag = (byte)(flag | (byte)(0x01));
        else
            flag = (byte)(flag & (byte)(0b00001110));

	System.out.println("LSL16 instruction");
        System.out.printf("AC = %d ZCVN = %d%d%d%d",
                          (int)(AC & (int)(0xffff)),
                          (byte)((flag & (byte)0x08) >> 3),
                          (byte)((flag & (byte)0x04) >> 2),
                          (byte)((flag & (byte)0x02) >> 1),
                          (byte)(flag & (byte)0x01));
        System.out.println();
    }

    //finished
    public static void LSR8() {
	TR = (byte)(AC & (short)0x00ff);
	TR = (byte)(TR >>> 1);
	TR = (byte)(TR & (0b01111111));

	//C flag set
	if ((byte)(AC & (short)(0x0001)) == (byte)1)
	    flag = (byte)(flag | (byte)(0x04));
	else
	    flag = (byte)(flag & (byte)(0b00001011));

	
	AC = (short)((AC & (short)0xff00) |
		     (short)((short)TR & (short)0x00ff));
	
	//Z flag check
	if ((byte)(AC & (short)(0x00ff)) == (byte)(0x00))
            flag = (byte)(flag | (0b00001000));
        else
            flag = (byte)(flag & (0b00000111));

	//N flag check
	if ((short)(AC & (short)(0x0080)) == (short)(0x0080)) 
            flag = (byte)(flag | (byte)(0x01));
        else
            flag = (byte)(flag & (byte)(0b00001110));

	System.out.println("LSR8 instruction");
        System.out.printf("AC = %d ZCVN = %d%d%d%d",
                          (int)(AC & (int)(0xff)),
                          (byte)((flag & (byte)0x08) >> 3),
                          (byte)((flag & (byte)0x04) >> 2),
                          (byte)((flag & (byte)0x02) >> 1),
                          (byte)(flag & (byte)0x01));
        System.out.println();
    }

    //finished
    public static void LSR16() {

	//C flag set
	if ((byte)(AC & (short)(0x0001)) == (byte)1)
	    flag = (byte)(flag | (byte)(0x04));
	else
	    flag = (byte)(flag & (byte)(0b00001011));

	AC = (short)(AC >>> 1);
	AC = (short)(AC & (short)(0x7fff));
		
	//Z flag check
	if (AC == (short)0x0000)
            flag = (byte)(flag | (0b00001000));
        else
            flag = (byte)(flag & (0b00000111));

	//N flag check
	if ((short)(AC & (short)(0x8000)) == (short)(0x8000)) 
            flag = (byte)(flag | (byte)(0x01));
        else
            flag = (byte)(flag & (byte)(0b00001110));

	System.out.println("LSR16 instruction");
        System.out.printf("AC = %d ZCVN = %d%d%d%d",
                          (int)(AC & (int)(0xffff)),
                          (byte)((flag & (byte)0x08) >> 3),
                          (byte)((flag & (byte)0x04) >> 2),
                          (byte)((flag & (byte)0x02) >> 1),
                          (byte)(flag & (byte)0x01));
        System.out.println();
    }

    //finished
    public static void MVI8() {
	DR = (byte)memory[AR];
        PC += (byte)1;
        AR += (byte)1;
	
	//print out values of MVI1
	System.out.println("MVI8 instruction");
        System.out.printf("MVI8-1: DR = %d PC = %d AR = %d",
			  (int)(DR & (int)(0xff)),
			  PC, AR);
        System.out.println();
	
	AC = (short)((short)(AC & (short)0xff00) |
		     (short)((short)DR & (short)0x00ff));
	
	
	//print out values of MVI2
	System.out.printf("MVI8-2: AC = %d", (int)(AC & (int)(0xff)));
	System.out.println();
    }

    //finished
    public static void MVI16() {
        DR = (byte)memory[AR];
        PC += (byte)1;
        AR += (byte)1;
	
        //print out values of MVI1                                          
        System.out.println("MVI16 instruction");
        System.out.printf("MVI16-1: DR = %d PC = %d AR = %d",
                          (int)(DR & (int)(0xff)),
                          PC, AR);
        System.out.println();


	AC = (short)((short)(AC & (short)0x00ff) |
                     (short)((short)DR & (short)0x00ff) << 8);

	DR = (byte)memory[AR];
	PC += (byte)1;
	AR += (byte)1;

	//print out values of MVI1                                          
        System.out.printf("MVI16-2: DR = %d PC = %d AR = %d",
                          (int)(DR & (int)(0xff)),
                          PC, AR);
        System.out.println();

	AC = (short)((short)(AC & (short)0xff00) |
                     (short)((short)DR & (short)0x00ff));
	

        //print out values of MVI2                                         
        System.out.printf("MVI16-3: AC = %d", (int)(AC & (int)(0xffff)));
        System.out.println();
    }
    
}
