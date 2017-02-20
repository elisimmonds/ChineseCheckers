// Author: Eli Simmonds
// Lab07

import java.util.*;
import java.io.*;
import java.lang.management.*;

class PegSolitaireRunnable implements Runnable {
	private PegSolitaire peg;
	private static ArrayList<Thread> threads = new ArrayList<Thread>();
	private static ArrayList<PegSolitaire> childBoards = new ArrayList<PegSolitaire>();
	private boolean solved;
	private ArrayList<move> win = new ArrayList<move>();

	public PegSolitaireRunnable() throws FileNotFoundException {
		this.solved = false;
		this.peg = new PegSolitaire();
	}

	public PegSolitaireRunnable(PegSolitaire peg) {
		this.peg = peg; // given Runnables peg
		this.solved = false;
	}

	public void run () {
		ArrayList<move> mSeq = new ArrayList<move>();   // will be populated with moves

		try {
			if (peg.solve(mSeq)) {
				if (!solved) {
					solved = true;
					System.out.println("Board can be reduced to one peg");
				}
				peg.display(mSeq);
				win = mSeq;
				for (int i = 0; i < threads.size(); i++) {
					threads.get(i).stop();
				}
				
			}
			else {
				System.out.println("No solution");
			}
		}
		catch (StackOverflowError t) {
			System.out.println("StackOverFlowError caught");
		}
	}



public static void main (String [] args) throws FileNotFoundException {
	System.out.println("Multi Threaded Version");
	PegSolitaireRunnable pegRun = new PegSolitaireRunnable();
	pegRun.peg.readBoard(args[0]);
	pegRun.peg.readMoves();
	ArrayList<Integer> moves = pegRun.peg.nextMoves();
	ArrayList<Thread> localThread = new ArrayList<Thread>();

	for (int i = 0; i < moves.size(); i++) {
		PegSolitaire ps = new PegSolitaire();
		ps.copy(pegRun.peg);
		ps.makeMove(moves.get(i));
		PegSolitaireRunnable tempPeg = new PegSolitaireRunnable(ps);
		Thread t1 = new Thread(tempPeg);
		pegRun.threads.add(t1);
		localThread.add(t1);
		t1.start();
		pegRun.threads = localThread;
	}
	long multiCpuTime = pegRun.getCpuTime()/1000000;
	System.out.println("Cpu time for multithreaded version = " + multiCpuTime);

	//Single Threaded Version
	System.out.println("Single Threaded Version");
	PegSolitaireRunnable singleThread = new PegSolitaireRunnable();
	singleThread.peg.readBoard(args[0]);
	singleThread.peg.readMoves();
	ArrayList<move> mSeq = new ArrayList<move>();

	if (singleThread.peg.solve(mSeq)) {
		System.out.println("Board can be reduced to a single peg");
		singleThread.peg.display(mSeq);
	}
	else
		System.out.println("Board cannot be reduced to a single peg");

	long singleCpuTime = singleThread.getCpuTime()/1000000;
	System.out.println("Cpu time for single threaded version = " + singleCpuTime);
	double c = ((double)singleCpuTime/multiCpuTime);
	System.out.println("Multi Threaded version is " + c + " times faster than Single Threaded version");
}

public long getCpuTime () {
	ThreadMXBean bean = ManagementFactory.getThreadMXBean();
	return bean.isCurrentThreadCpuTimeSupported() ?
		bean.getCurrentThreadCpuTime() : 0L;
	}
};

