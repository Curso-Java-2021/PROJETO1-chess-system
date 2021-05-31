package boardGame;

public class Board {
    private int rows;
    private int columns;
    //Matriz de pecas
    private Piece[][] pieces; 

    public Board(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        pieces = new Piece[rows][columns];
    }

    public int getRows() {
        return this.rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return this.columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public Piece piece(int row, int column){ //determina posicao por coordenadas
        return pieces[row][column];
    }
    
    public Piece piece(Position position){ //determina posicao pela posicao
        return pieces[position.getRow()][position.getColumn()];
    }
}
