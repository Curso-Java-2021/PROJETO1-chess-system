package application;

import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import chess.ChessMatch;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.Color;

public class UI {
    // https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BRIGHT_CYAN = "\u001B[96m";
    public static final String ANSI_BRIGHT_YELLOW = "\u001B[93m";
    public static final String ANSI_PECAS_PRETAS = "\u001B[38;5;166m";
    public static final String ANSI_PECAS_BRANCAS = "\u001B[38;5;214m";

	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    
    //LIMPA A TELA
    // https://stackoverflow.com/questions/2979383/java-clear-the-console
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    //LE A POSICAO DO USUARIO
    public static ChessPosition readChessPosition(Scanner sc){
        try{
            String s = sc.nextLine();
            char column = s.charAt(0);
            int row = Integer.parseInt(s.substring(1));

            return new ChessPosition(column, row);
        }
        catch(RuntimeException e){
            throw new InputMismatchException("Error reading ChessPosition. Valid values are from a1 to h8");
        }
    }

    //
    public static void printMatch(ChessMatch chessMatch, List <ChessPiece> captured){
        printBoard(chessMatch.getPieces());
        System.out.println();
        printCapturedPieces(captured);
        System.out.println();
        System.out.println("Turn: " + chessMatch.getTurn());

        if(!chessMatch.getCheckMate()){
            System.out.print("Waiting player: "); 
            if(chessMatch.getCurrentPlayer() == Color.WHITE){ System.out.print(ANSI_PECAS_BRANCAS); }
            else { System.out.print(ANSI_PECAS_PRETAS); }
            System.out.println(chessMatch.getCurrentPlayer() + ANSI_RESET);

            if(chessMatch.getCheck()){
                System.out.println("CHECK!!");
            }
        }
        else{
            System.out.println();
            System.out.println("CHECKMATE!!");
            System.out.print("Winner: ");
            if(chessMatch.getCurrentPlayer() == Color.WHITE){ System.out.print(ANSI_PECAS_BRANCAS); }
            else { System.out.print(ANSI_PECAS_PRETAS); }
            System.out.println(chessMatch.getCurrentPlayer() + ANSI_RESET);
            System.out.println("Congratulations!");
        }
    }

    //PRINTA O TABULEIRO
    public static void printBoard(ChessPiece[][] pieces){
        System.out.print(ANSI_BLUE);
        System.out.println("  a b c d e f g h");
        System.out.print(ANSI_RESET);

        for(int i = 0; i < pieces.length; i++){
            System.out.print(ANSI_BLUE);
            System.out.print((8 - i) + " ");
            System.out.print(ANSI_RESET);
            for(int j = 0; j < pieces.length; j++){
                printPiece(pieces[i][j], false);
            }
            System.out.print(ANSI_BLUE);
            System.out.print((8 - i) + " ");
            System.out.print(ANSI_RESET);
            System.out.println();
        }
        System.out.print(ANSI_BLUE);
        System.out.println("  a b c d e f g h");
        System.out.print(ANSI_RESET);
    }

    //PRINTA O TABULEIRO
    public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves){
        System.out.print(ANSI_BLUE);
        System.out.println("  a b c d e f g h");
        System.out.print(ANSI_RESET);

        for(int i = 0; i < pieces.length; i++){
            System.out.print(ANSI_BLUE);
            System.out.print((8 - i) + " ");
            System.out.print(ANSI_RESET);
            for(int j = 0; j < pieces.length; j++){
                printPiece(pieces[i][j], possibleMoves[i][j]);
            }
            System.out.print(ANSI_BLUE);
            System.out.print((8 - i) + " ");
            System.out.print(ANSI_RESET);
            System.out.println();
        }
        System.out.print(ANSI_BLUE);
        System.out.println("  a b c d e f g h");
        System.out.print(ANSI_RESET);
    }

    //PRINTA AS PECAS DO TABULEIRO
    private static void printPiece(ChessPiece piece, boolean background){
        if(background){
            System.out.print(ANSI_BLUE_BACKGROUND);
        }
        if (piece == null) {
            System.out.print("-" + ANSI_RESET);
        }
        else {
            if (piece.getColor() == Color.WHITE) {
                System.out.print(ANSI_PECAS_BRANCAS + piece + ANSI_RESET);
            }
            else {
                System.out.print(ANSI_PECAS_PRETAS + piece + ANSI_RESET);
            }
        }
        System.out.print(" ");
    }

    //MOSTRANDO AS PECAS CAPTURADAS
    private static void printCapturedPieces(List <ChessPiece> captured){
        List <ChessPiece> white = captured.stream().filter(x -> x.getColor() == Color.WHITE).collect(Collectors.toList());
        List <ChessPiece> black = captured.stream().filter(x -> x.getColor() == Color.BLACK).collect(Collectors.toList());
        
        System.out.println("Captured pieces:");
        System.out.print(ANSI_PECAS_BRANCAS);
        System.out.print("White: ");
        System.out.println(Arrays.toString(white.toArray()));
        System.out.print(ANSI_RESET);
        System.out.print(ANSI_PECAS_PRETAS);
        System.out.print("Black: ");
        System.out.println(Arrays.toString(black.toArray()));
        System.out.print(ANSI_RESET);
    } 
}
