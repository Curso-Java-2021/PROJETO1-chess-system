package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardGame.Board;
import boardGame.Piece;
import boardGame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
    private Board board; //tabuleiro
    private int turn; //turno
    private Color currentPlayer; //jogador atual
    private boolean check;
    private boolean checkMate;

    private List <Piece> piecesOnTheBoard = new ArrayList<>();
    private List <Piece> capturedPieces = new ArrayList<>();

    public ChessMatch(){
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        check = false;
        InicialSetup();
    }

    public boolean getCheck(){
        return check;
    }

    public boolean getCheckMate(){
        return checkMate;
    }

    public int getTurn() {
        return this.turn;
    }

    public Color getCurrentPlayer() {
        return this.currentPlayer;
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

    //MOSTRA OS MOVIMENTOS POSSIVEIS PARA O USUARIO
    public boolean[][] possibleMoves(ChessPosition sourcePosition){
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);

        return board.piece(position).possibleMoves();
    }

    //TIRA A PECA DA ORIGEM E COLOCA NO DESTINO
    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition){
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();

        validateSourcePosition(source);
        validateTargetPosition(source, target);

        Piece capturedPiece = makeMove(source, target);

        if(testCheck(currentPlayer)){
            undoMove(source, target, capturedPiece);
            throw new ChessException("You can't put yourself in check!");
        }

        check = (testCheck(opponent(currentPlayer))) ? true : false;

        if(testCheckMate(opponent(currentPlayer))){
            checkMate = true;
        }
        else{
            nextTurn();
        }

        return (ChessPiece) capturedPiece;
    }

    //MOVE A PECA PREDADORA E CAPTURA A PRESA
    private Piece makeMove(Position source, Position target){
        Piece p = board.removePiece(source);
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);
        if(capturedPiece != null){
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }
        return capturedPiece;
    }

    //DESFAZ O MOVIMENTO EM CASO DE AUTO-XEQUE
    private void undoMove(Position source, Position target, Piece capturedPiece){
        Piece p = board.removePiece(target);
        board.placePiece(p, source);
        if(capturedPiece != null){
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }
    }

    //VALIDA A POSICAO DE ORIGEM
    private void validateSourcePosition(Position position){
        if(!board.thereIsAPiece(position)){
            throw new ChessException("There is no piece on source position");
        }
        if(currentPlayer != ((ChessPiece)board.piece(position)).getColor()){
            throw new ChessException("The chosen piece is not yours!");
        }
        if(!board.piece(position).isThereAnyPossibleMove()){
            throw new ChessException("There is no possible moves for the chosen piece");
        }
    }

    //VALIDA A POSICAO DE DESTINO
    private void validateTargetPosition(Position source, Position target){
        if(!board.piece(source).possibleMove(target)){
            throw new ChessException("The chosen piece can't move to target position");
        }
        
    }

    //CONFIGURA O PROX TURNO
    private void nextTurn(){
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    //DEFINE A COR DO OPONENTE
    private Color opponent(Color color){
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    //ENCONTRA O REI DO JOGADOR
    private ChessPiece king(Color color){
        List <Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
        for(Piece p : list){
            if(p instanceof King){
                return (ChessPiece)p;
            }
        }
        throw new IllegalStateException("There is no " + color + " king on the board");
    }

    //VARRE AS PECAS ADVERSARIAS E VERIFICA SE HA XEQUE
    private boolean testCheck (Color color){
        Position kingPosition = king(color).getChessPosition().toPosition();
        List <Piece> opponetPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());

        for(Piece p : opponetPieces){
            boolean[][] mat = p.possibleMoves();
            if(mat[kingPosition.getRow()][kingPosition.getColumn()]){
                return true;
            }
        }
        return false;
    }

    //VARRE AS PECAS ALIADAS E VERIFICA SE HA XEQUE MATE
    private boolean testCheckMate (Color color){
        if(!testCheck(color)){
            return false;
        }

        List <Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
        for(Piece p : list){
            boolean[][] mat = p.possibleMoves();
            for(int i = 0; i < board.getRows(); i++){
                for(int j = 0; j < board.getColumns(); j++){
                    if(mat[i][j]){
                        Position source = ((ChessPiece)p).getChessPosition().toPosition();
                        Position target = new Position(i, j);

                        Piece capturedPiece = makeMove(source, target);

                        boolean testCheck = testCheck(color);

                        undoMove(source, target, capturedPiece);

                        if(!testCheck){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    //DEFINE A POSICAO DA NOVA PECA NO FORMATO LETRA+NUMERO
    private void placeNewPiece(char column, int row, ChessPiece piece){
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    //INICIA AS PECAS NO TABULEIRO
    private void InicialSetup(){
        //Pecas pretas
        placeNewPiece('a', 1, new Rook(board, Color.BLACK));
        placeNewPiece('e', 1, new King(board, Color.BLACK));
        placeNewPiece('h', 1, new Rook(board, Color.BLACK));

        //Pecas brancas
        placeNewPiece('a', 8, new Rook(board, Color.WHITE));
        placeNewPiece('e', 8, new King(board, Color.WHITE));
        placeNewPiece('h', 8, new Rook(board, Color.WHITE));
    }
}
