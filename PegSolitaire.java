import java.util.*;
import java.io.*;

public class PegSolitaire {
	private boolean[] board;
	private final int SIZE = 49;
	private final int NUM_MOVES = 76;
	private final move[] MOVELIST = new move[NUM_MOVES];

	public static void main(String[] args) throws FileNotFoundException {
		//boolean[] board1 = readBoard(args[0]);
		PegSolitaire peg = new PegSolitaire();
		peg.readBoard("board.txt"); peg.readMoves();
		ArrayList<move> mSeq = new ArrayList<move>();

//		peg.display(mSeq);
//		peg.printMoveList(mSeq);
//		peg.printBoard();

		if (peg.solve(mSeq)) {
			System.out.println("Board can be reduced to a single peg");
			peg.display(mSeq);
		}
		else
			System.out.println("Board cannot be reduced to a single peg");
	}

	public PegSolitaire() throws FileNotFoundException {
		readMoves();
	}

	public PegSolitaire(boolean[] board) throws FileNotFoundException  {
		this.board = board;
		readMoves();
	}

	public PegSolitaire copy (PegSolitaire peg) {
		this.board = new boolean[peg.board.length];
		for (int i = 0; i < peg.board.length; i++) {
			this.board[i] = peg.board[i];
		}
		return this;
	}

	public boolean[] getBoard() {
		return board;
	}

//	public PegSolitaire() {
//		this.readMoves();
//	}

    public void printMoveList(ArrayList<move> mLst) {
		if (mLst.size() == 0)
			System.out.print("List empty");
    	for (int j = 0; j < mLst.size(); j++)
    		mLst.get(j).printMove();
    }

    public void printBoard() {
    	for (int j = 0; j < board.length; j++)
    		if (board[j])
    			System.out.print(j + " ");
    	System.out.println("");
    }

	public void readBoard(String file1) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(file1));

		board = new boolean[this.SIZE];
		for (int j = 0; j < this.SIZE; j++)
			board[j] = false;
		while (scanner.hasNextInt()) {
			int x = scanner.nextInt();
			board[x] = true;
		}
	}

	public void readMoves() throws FileNotFoundException {
		Scanner scanner = new Scanner(new File("moves.txt"));
		int indx = 0;
		while(scanner.hasNextInt()) {
          int from = scanner.nextInt();
          int mid = scanner.nextInt();
          int to = scanner.nextInt();
          move m = new move(from, mid, to);
          MOVELIST[indx++] = m;
		}
	}



	public ArrayList<Integer> nextMoves() {
		//generates the list of possible moves that can be made on the current board
        ArrayList<Integer> temp = new ArrayList<Integer>();
        for (int j = 0; j < MOVELIST.length; j++)
          if (valid(j))
          	temp.add(j);
        return temp;
	}

	public boolean valid(int j) {
		//if MOVELIST[j] is a valid move in the current board, return true
		move m = MOVELIST[j];
		if (board[m.getFrom()] && board[m.getMid()] && ! board[m.getTo()])
			return true;
		else 
			return false;
	}

	public void makeMove(int k) {
		// performs move j on the current board and returns a new board
		move m = MOVELIST[k];
		board[m.getFrom()] = false;
		board[m.getMid()] = false;
		board[m.getTo()] = true;
	}

	public void undoMove(int k) {
		move m = MOVELIST[k];
		board[m.getFrom()] = true;
		board[m.getMid()] = true;
		board[m.getTo()] = false;
	}

	public boolean solved() {
		// checks if the current board represents a solved board
		int count = 0; int indx = 0;
		while (indx < SIZE) {
           if (board[indx++])
           	count++;
           if (count > 1)
           	return false;
		}
        return true;
	}

    public boolean leaf() {
    	//checks if the board is a leaf node
    	if (nextMoves().isEmpty()) 
    		return true;
    	else
    		return false;
    }

	public boolean solve(ArrayList<move> mSeq) {
		// solves the current board and returns solution in mSeq
		if (solved())
			return true;
		if (leaf())
			return false;
		ArrayList<Integer> allMoves = nextMoves();
		for (int j = 0; j < allMoves.size(); j++) {
			makeMove(allMoves.get(j));
			if (solve(mSeq)) {
				mSeq.add(MOVELIST[allMoves.get(j)]);
				return true;
			} else {
				undoMove(allMoves.get(j));
			}
         }
        return false;
	}

	public void display(ArrayList<move> mSeq) {
		for (int j = mSeq.size() - 1; j >= 0; j--) {
			mSeq.get(j).printMove();
		}
	}
}

class move {

	private int from; private int mid; private int to;
	move(int from, int mid, int to) {
		this.from = from;
		this.mid = mid;
		this.to = to;
	}

	public void printMove() {
		System.out.println(getFrom() + "," + getMid() +  "," + getTo());
	}

	int getFrom() {
		return from;
	}

	int getMid() {
		return mid;
	}

	int getTo() {
		return to;
	}
}
















