package chess;

import boardGame.Board;
import chess.pieces.Rook;

public class ChessMatch {
    private Board board; //tabuleiro

    public ChessMatch(){
        board = new Board(8, 8);
        InicialSetup();
    }

    //Retorna a posicao de tds as pecas
    public ChessPiece[][] getPieces(){
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];

        for(int i = 0; i < board.getRows(); i++){
            for(int j = 0; j < board.getColumns(); j++){
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return mat;
    }

    //DEFINE A POSICAO DA NOVA PECA NO FORMATO LETRA+NUMERO
    private void placeNewPiece(char column, int row, ChessPiece piece){
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
    }

    //INICIA AS PECAS NO TABULEIRO
    private void InicialSetup(){
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
    }
}
