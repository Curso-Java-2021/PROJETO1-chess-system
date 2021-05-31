package chess;

import boardGame.Board;
import boardGame.Piece;
import boardGame.Position;
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

    //TIRA A PECA DA ORIGEM E COLOCA NO DESTINO
    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition){
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();

        valideSourcePosition(source);

        Piece capturedPiece = makeMove(source, target);

        return (ChessPiece) capturedPiece;
    }

    //MOVE A PECA PREDADORA E CAPTURA A PRESA
    private Piece makeMove(Position source, Position target){
        Piece p = board.removePiece(source);
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        return capturedPiece;
    }

    //VALIDA A POSICAO DE ORIGEM
    private void valideSourcePosition(Position position){
        if(!board.thereIsAPiece(position)){
            throw new ChessException("There is no piece on source position");
        }
    }

    //DEFINE A POSICAO DA NOVA PECA NO FORMATO LETRA+NUMERO
    private void placeNewPiece(char column, int row, ChessPiece piece){
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
    }

    //INICIA AS PECAS NO TABULEIRO
    private void InicialSetup(){
        placeNewPiece('a', 1, new Rook(board, Color.BLACK));
    }
}
