/* 

#Filename: rscpu1.java

#Author: Seth Roller

#Date: 2/21/19

#Problem: Will be setting up the structure 
for the remaining programs. Will specificly
finish the NOOP and HALT instructions

*/

import java.io.*;
import java.util.*;

public class rscpu1 {

    //global variables of all the reigsters
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
    
    static ArrayList<Byte> memory = new ArrayList<Byte> (); 
    
    public static void main(String args[])
	throws FileNotFoundException
    {
	//create a scanner to read in the file
	Scanner sc = new Scanner(System.in);
	String filename;
	
	System.out.println();
	System.out.println("rscpu1 written by Seth Roller");
	System.out.println("This program will have the HALT and "
			   + "NOOP instructions implemented.");
	System.out.println();
	System.out.print("Enter the file containing the program: ");
	
	filename = sc.nextLine();
	File input = new File(filename);
	Scanner reader = new Scanner(input);

	System.out.println();
	System.out.println("The file " + filename +
			   " opened properly, off to "
			   + "the races");

	// while the reader has something next to read
	while (reader.hasNext()) {
	    //store as a string first
	    //then parse to a int through the dexadecimal base 
	    String temp = reader.next();
	    Integer store = Integer.parseInt(temp, 16);

	    // if this is greater than 127 then the
	    // number is technically negative due to
	    // two's complement
	    if (store > 127) {
		store = -(store -128);
	    }

	    // will had to the arraylist of bytes and increase lines
	    byte insertt = Byte.valueOf(Integer.toString(store));
	    memory.add(insertt);
	    ++lines;
	}

	System.out.println("End of file reached");

	// calls fetch and performs a switch statement
	// on the global variable IR
	for (int i = 0; i < memory.size(); i++) {
            fetch(memory.get(i));
	    
	    switch(IR) {

	    case (byte)0:
		NOP();
		break;
	    case (byte)1:
		LDAC();
		break;
	    case (byte)2:
		STAC();
		break;
	    case (byte)3:
		MVAC();
		break;
	    case (byte)4:
		MOVR();
		break;
	    case (byte)5:
		JUMP();
		break;
	    case (byte)6:
		JMPZ();
		break;
	    case (byte)7:
		JPNZ();
		break;
	    case (byte)8:
		JMPC();
		break;
	    case (byte)9:
		JV();
		break;
	    case (byte)10:
		JN();
		break;
	    case (byte)11: 
		ADD();
		break;
	    case (byte)12:
		SUB();
		break;
	    case (byte)13:
		INAC();
		break;
	    case (byte)14:
		CLAC();
		break;
	    case (byte)15:
		AND();
		break;
	    case (byte)16:
		OR();
		break;
	    case (byte)17:
		XOR();
		break;
	    case (byte)18:
		NOT();
		break;
	    case (byte)19:
		RL();
		break;
	    case (byte)20:
		RR();
		break;
	    case (byte)21:
		LSL();
		break;
	    case (byte)22:
		LSR();
		break;
	    case (byte)23:
		MVI();
		break;
	    case (byte)-127:
		HALT();
		break;
		
	    }

	    //print out the following info after every
	    //instruction

	    System.out.printf("Instruction execution complete:");
	    System.out.printf(" AC = %d R = %d ZCVN = %04d ", AC, R, flag);
	    if (DR == -127)
		System.out.printf("AR = %d PC = %d DR = 255", AR, PC);
	    else
		System.out.printf("AR = %d PC = %d DR = %d", AR, PC, DR);
	    System.out.println();

	    // if HALT is executed exit after the execution statement
	    if (IR == -127)
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
	if (DR == (byte)-127)
	    System.out.printf("fetch2: DR = 255 ");
	else
	    System.out.printf("fetch2: DR = %d", DR);
	System.out.printf(" PC = %d", PC);
	System.out.println();
	IR = DR;
	AR = PC;
	if (IR == (byte)-127)
	    System.out.printf("fetch3: IR = 255 ");
	else
	    System.out.printf("fetch3: IR = %d ", IR);
	System.out.printf(" AR = %d", AR);
	System.out.println();
	
	
    }
    
    public static void NOP() {
	// you got here, and it does nothing
	System.out.println("no op instruction");
    }

    public static void HALT() {
	//this halts the reading
	System.out.println("halt instruction");
    }

    public static void LDAC() {
	System.out.println("Made it to LDAC");
    }

    public static void STAC() {
	System.out.println("Made it to STAC");
    }

    public static void MVAC() {
	System.out.println("Made it to MVAC");
    }

    public static void MOVR() {
	System.out.println("Made it to MOVR");
    }

    public static void JUMP() {
	System.out.println("Made it to JUMP");
    }

    public static void JMPZ() {
	System.out.println("Made it to JMPZ");
    }

    public static void JPNZ() {
	System.out.println("Made it to JPNZ");
    }

    public static void JMPC() {
	System.out.println("Made it to JMPC");
    }

    public static void JV() {
	System.out.println("Made it to JV");
    }

    public static void JN() {
	System.out.println("Made it to JN");
    }

    public static void ADD() {
	System.out.println("Made it to ADD");
    }

    public static void SUB() {
	System.out.println("Made it to SUB");
    }

    public static void INAC() {
	System.out.println("Made it to INAC");
    }

    public static void CLAC() {
	System.out.println("Made it to CLAC");
    }

    public static void AND() {
	System.out.println("Made it to AND");
    }

    public static void OR() {
	System.out.println("Made it to OR");
    }

    public static void XOR() {
	System.out.println("Made it to XOR");
    }

    public static void NOT() {
	System.out.println("Made it to NOT");
    }

    public static void RL() {
	System.out.println("Made it to RL");
    }

    public static void RR() {
	System.out.println("Made it to RR");
    }

    public static void LSL() {
	System.out.println("Made it to LSL");
    }

    public static void LSR() {
	System.out.println("Made it to LSR");
    }

    public static void MVI() {
	System.out.println("Made it to MVI");
    }    
    
}
