/* 

#Filename: rscpu4.java

#Author: Seth Roller

#Date: 3/21/19

#Problem: Will be implementing the AND, 
XOR, OR, and NOT instructions. 

*/

import java.io.*;
import java.util.*;

public class rscpu4
{

    //global variables of all the registers
    //within the rscpu

    static short AR = 0;
    static short PC = 0;

    static byte AC = 0;
    static byte R = 0;
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
	System.out.println("rscpu4 written by Seth Roller");
	System.out.println("This program will have the AND, "
			   + "XOR, ");
	System.out.println("OR and NOT instructions implemented.");
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
		    LDAC();
		    break;
		case (byte)(0b00000010):
		    STAC();
		    break;
		case (byte)(0b00000011):
		    MVAC();
		    break;
		case (byte)(0b00000100):
		    MOVR();
		    break;
		case (byte)(0b00000101):
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
		    ADD();
		    break;
		case (byte)(0b00001001):
		    SUB();
		    break;
		case (byte)(0b00001010):
		    INAC();
		    break;
		case (byte)(0b00001011):
		    CLAC();
		    break;
		case (byte)(0b00001100):
		    AND();
		    break;
		case (byte)(0b00001101):
		    OR();
		    break;
		case (byte)(0b00001110):
		    XOR();
		    break;
		case (byte)(0b00001111):
		    NOT();
		    break;
		case (byte)(0b00010010):
		    RL();
		    break;
		case (byte)(0b00010011):
		    RR();
		    break;
		case (byte)(0b00010100):
		    LSL();
		    break;
		case (byte)(0b00010101):
		    LSR();
		    break;
		case (byte)(0b00010110):
		    MVI();
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

    public static void LDAC() {
	DR = (byte)memory[AR]; 
	PC += (byte)1;
	AR += (byte)1;
	// print out values of LDAC1
	System.out.println("LDAC instruction");
	System.out.printf("LDAC1: DR = %d PC = %d AR = %d",
			  (int)(DR & (int)(0xff)), PC, AR);
	System.out.println();
	
	TR = DR;
	DR = (byte)memory[AR];
	PC += (byte)1;
	//print out values of LDAC2
	System.out.printf("LDAC2: TR = %d DR = %d AR = %d PC = %d",
			  (int)(TR & (int)(0xff)), 
			  (int)(DR & (int)(0xff)), AR, PC);
	System.out.println();

	AR = (short)(0x0000 | ((short)DR & 0x00ff));
	AR = (short)(AR | (((short)TR << 7) & 0xff00));
	
	//print out values of LDAC3
	System.out.printf("LDAC3: AR = %d", AR);
	System.out.println();

	DR = (byte)memory[AR];
	
	//print out values of LDAC4
	System.out.printf("LDAC4: DR = %d", (int)(DR & (int)(0xff)));
	System.out.println();

	AC = DR;

	//print out values of LDAC5
	System.out.printf("LDAC5: AC = %d", (int)(AC & (int)(0xff)));
	System.out.println();
	 
    }

    public static void STAC() {
	DR = (byte)memory[AR];
        PC += (byte)1;
        AR += (byte)1;
	//print out values of STAC1

	System.out.println("STAC instruction");
        System.out.printf("STAC1: DR = %d PC = %d AR = %d",
			  (int)(DR & (int)(0xff))
			  , PC, AR);
        System.out.println();

        TR = DR;
        DR = (byte)memory[AR];
        PC += (byte)1;
	// print out values of STAC2

        System.out.printf("STAC2: TR = %d DR = %d AR = %d PC = %d",
			  (int)(TR & (int)(0xff)),
                          (int)(DR & (int)(0xff)),
			  AR, PC);
	System.out.println();

        AR = (short)(0x0000 | ((short)DR & 0x00ff));
        AR = (short)(AR | (((short)TR << 7) & 0xff00));
	
	//print out values of STAC3
	
        System.out.printf("STAC3: AR = %d M[AR] = %d", 
			  AR, (int)(memory[AR] & (int)(0xff)));
        System.out.println();

	memory[AR] = (byte)AC;

	//print out values of STAC4

	System.out.printf("STAC4: AC = %d M[AR] = %d", 
			  (int)(AC & (int)(0xff)),
			  (int)(memory[AR] & (int)(0xff)));
	System.out.println();

    }

    public static void MVAC() {
	TR = DR;
	DR = AC;
	R = DR;
	System.out.println("MVAC instruction");
	System.out.printf("MVAC R = %d", (int)(R & (int)(0xff)));
	System.out.println();
	// assign DR back to original
	DR = TR;
    }

    public static void MOVR() {
	TR = DR;
	DR = R;
	AC = DR;
	System.out.println("MOVR instruction");
	System.out.printf("MOVR AC = %d", (int)(AC & (int)(0xff)));
	System.out.println();
	// assign DR back to original 
	DR = TR;
    }

    public static void JUMP() {
	System.out.println("Made it to JUMP");
	System.exit(1);
    }

    public static void JMPZ() {
	System.out.println("Made it to JMPZ");
	System.exit(1);
    }

    public static void JPNZ() {
	System.out.println("Made it to JPNZ");
	System.exit(1);
    }

    public static void JMPC() {
	System.out.println("Made it to JMPC");
	System.exit(1);
    }

    public static void JV() {
	System.out.println("Made it to JV");
	System.exit(1);
    }

    public static void JN() {
	System.out.println("Made it to JN");
	System.exit(1);
    }

    public static void ADD() {
	System.out.println("Made it to ADD");
	System.exit(1);
    }

    public static void SUB() {
	System.out.println("Made it to SUB");
	System.exit(1);
    }

    public static void INAC() {
	TR = AC;
	//TR will hold the old contents to check for
	//overflow 
	AC = (byte)(AC + (byte)1); // incrememnt

	// Z flag check
	if (AC == (byte)0)
	    flag = (byte)(flag | (0b00001000));
	else
	    flag = (byte)(flag & (0b11110111));

	// N flag check
	if ((byte)(AC & (byte)(0x80)) == (byte)(0x80)) 
	    flag = (byte)(flag | (byte)(0x01));

	else
	    flag = (byte)(flag & (byte)(0b00001110));

	// C flag check
	// check if the most significant bit is 1
	// in old contents stored in TR
	// if so and the new result has a 0 in the
	// significant bit the carry flag is raised
	if ((byte)((TR & 0x80) >> 7) == (byte)1 &&
	    (byte)((AC & 0x80) >> 7) == (byte)0)
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
		if ((byte)(flag & (byte)(0x04)) == (AC & (byte)(0x80)))
		    flag = (byte)(flag & (byte)(0b00001101));
		else
		    flag = (byte)(flag | (byte)(0x02));
		
	    }
	
	System.out.println("INAC instruction");
	System.out.printf("INAC AC = %d ZCVN = %d%d%d%d",
			  (int)(AC & (int)(0xff)),
			  (byte)((flag & (byte)0x08) >> 3),
			  (byte)((flag & (byte)0x04) >> 2),
			  (byte)((flag & (byte)0x02) >> 1),
			  (byte)(flag & (byte)0x01));
	System.out.println();
    }

    public static void CLAC() {
	AC = (byte)0;
	//reset flags
	flag = (byte)(flag | (byte)(0b00001000));
	flag = (byte)(flag & (byte)(0b00001000));
	System.out.println("CLAC instruction");
	System.out.printf("AC = %d ZCVN = %d%d%d%d", AC,
			  (byte)((flag & (byte)0x08) >> 3),
			  (byte)((flag & (byte)0x04) >> 2),
			  (byte)((flag & (byte)0x02) >> 1),
			  (byte)(flag & (byte)0x01));
	System.out.println();
    }

    public static void AND() {
	AC = (byte)(AC & R);
	//Z flag check
	if (AC == (byte)0)
            flag = (byte)(flag | (0b00001000));
        else
            flag = (byte)(flag & (0b11110111));

	//N flag check
	if ((byte)(AC & (byte)(0x80)) == (byte)(0x80))
            flag = (byte)(flag | (byte)(0x01));

        else
            flag = (byte)(flag & (byte)(0b00001110));

	System.out.println("AND instruction");
        System.out.printf("AC = %d ZCVN = %d%d%d%d",
			  (int)(AC & (int)(0xff)),
                          (byte)((flag & (byte)0x08) >> 3),
	                  (byte)((flag & (byte)0x04) >> 2),
                          (byte)((flag & (byte)0x02) >> 1),
                          (byte)(flag & (byte)0x01));
        System.out.println();
    }

    public static void OR() {
	AC = (byte)(AC | R);
	//Z flag check
	if (AC == (byte)0)
            flag = (byte)(flag | (0b00001000));
        else
            flag = (byte)(flag & (0b11110111));

	//N flag check
        if ((byte)(AC & (byte)(0x80)) == (byte)(0x80))
            flag = (byte)(flag | (byte)(0x01));

        else
            flag = (byte)(flag & (byte)(0b00001110));

	System.out.println("OR instruction");
        System.out.printf("AC = %d ZCVN = %d%d%d%d",
			  (int)(AC & (int)(0xff)),
                          (byte)((flag & (byte)0x08) >> 3),
	                  (byte)((flag & (byte)0x04) >> 2),
                          (byte)((flag & (byte)0x02) >> 1),
                          (byte)(flag & (byte)0x01));
        System.out.println();
    }

    public static void XOR() {
	AC = (byte)(AC ^ R);
	//Z flag check
	if (AC == (byte)0)
            flag = (byte)(flag | (0b00001000));
        else
            flag = (byte)(flag & (0b11110111));

	//N flag check
        if ((byte)(AC & (byte)(0x80)) == (byte)(0x80))
            flag = (byte)(flag | (byte)(0x01));

        else
            flag = (byte)(flag & (byte)(0b00001110));

	System.out.println("XOR instruction");
        System.out.printf("AC = %d ZCVN = %d%d%d%d",
			  (int)(AC & (int)(0xff)),
                          (byte)((flag & (byte)0x08) >> 3),
	                  (byte)((flag & (byte)0x04) >> 2),
                          (byte)((flag & (byte)0x02) >> 1),
                          (byte)(flag & (byte)0x01));
        System.out.println();
    }

    public static void NOT() {
	AC = (byte)(~AC);

	//Z flag check
	if (AC == (byte)0)
            flag = (byte)(flag | (0b00001000));
        else
            flag = (byte)(flag & (0b11110111));

	//N flag check
	if ((byte)(AC & (byte)(0x80)) == (byte)(0x80))
            flag = (byte)(flag | (byte)(0x01));
        else
            flag = (byte)(flag & (byte)(0b00001110));

	System.out.println("NOT instruction");
        System.out.printf("AC = %d ZCVN = %d%d%d%d",
			  (int)(AC & (int)(0xff)),
                          (byte)((flag & (byte)0x08) >> 3),
	                  (byte)((flag & (byte)0x04) >> 2),
                          (byte)((flag & (byte)0x02) >> 1),
                          (byte)(flag & (byte)0x01));
        System.out.println();
    }

    public static void RL() {
	System.out.println("Made it to RL");
	System.exit(1);
    }

    public static void RR() {
	System.out.println("Made it to RR");
	System.exit(1);
    }

    public static void LSL() {
	System.out.println("Made it to LSL");
	System.exit(1);
    }

    public static void LSR() {
	System.out.println("Made it to LSR");
	System.exit(1);
    }

    public static void MVI() {
	DR = (byte)memory[AR];
        PC += (byte)1;
        AR += (byte)1;

	//print out values of MVI1
	System.out.println("MVI instruction");
        System.out.printf("MVI1: DR = %d PC = %d AR = %d",
			  (int)(DR & (int)(0xff)),
			  PC, AR);
        System.out.println();
	
	AC = DR;    

	//print out values of MVI2
	System.out.printf("MVI2: AC = %d", (int)(AC & (int)(0xff)));
	System.out.println();
    }    
    
}
