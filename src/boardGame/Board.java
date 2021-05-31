package boardGame;

//TABULEIRO
public class Board {
    private int rows;
    private int columns;
    //Matriz de pecas
    private Piece[][] pieces; 

    public Board(int rows, int columns) {
        if(rows < 1 || columns < 1){
            throw new BoardException("Error creating board: there must be at least 1 row and 1 column");
        }
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];
    }

    public int getRows() {
        return this.rows;
    }

    public int getColumns() {
        return this.columns;
    }

    public Piece piece(int row, int column){ //determina posicao por coordenadas
        if(!positionExists(row, column)){
            throw new BoardException("Position not on the board");
        }
        
        return pieces[row][column];
    }
    
    public Piece piece(Position position){ //determina posicao pela posicao
        if(!positionExists(position)){
            throw new BoardException("Position not on the board");
        }
        
        return pieces[position.getRow()][position.getColumn()];
    }

    //COLOCA PECA PIECE NA POSICAO POSITION
    public void placePiece(Piece piece, Position position){
        if(thereIsAPiece(position)){
            throw new BoardException("There is already a piece on position " + position);
        }
        
        pieces[position.getRow()][position.getColumn()] = piece;
        piece.position = position;
    }

    //REMOVE A PECA
    public Piece removePiece(Position position){
        if(!positionExists(position)){
            throw new BoardException("Position not on the board");
        }
        if(piece(position) == null){
            return null;
        }
        Piece aux = piece(position);
        aux.position = null;
        pieces[position.getRow()][position.getColumn()] = null;

        return aux;
    }

    //POSICAO EXISTE?
    public boolean positionExists(int row, int column){ //aux
        return row >= 0 && row < rows && column >= 0 && column < columns;
    }
    public boolean positionExists(Position position){
        return positionExists(position.getRow(), position.getColumn());
    }

    //PECA EXISTE?
    public boolean thereIsAPiece(Position position){
        if(!positionExists(position)){
            throw new BoardException("Position not on the board");
        }
        
        return piece(position) != null;
    }
}
