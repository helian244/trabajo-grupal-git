
public class GameState {
    public enum Status {
        PLAYING, CHECK, CHECKMATE, STALEMATE
    }

    private final Board board;
    private PieceColor currentTurn;
    private Status status;
    private int moveCount;

    public GameState() {
        board = new Board();
        currentTurn = PieceColor.WHITE;
        status = Status.PLAYING;
        moveCount = 0;
    }

    public Board getBoard() {
        return board;
    }

    public PieceColor getCurrentTurn() {
        return currentTurn;
    }

    public Status getStatus() {
        return status;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public boolean tryMove(Position from, Position to) {
        if (status == Status.CHECKMATE || status == Status.STALEMATE)
            return false;
        Piece piece = board.getPiece(from);
        if (piece == null || piece.getColor() != currentTurn)
            return false;

        boolean moved = board.movePiece(from, to);
        if (!moved)
            return false;

        moveCount++;

        // Handle pawn promotion automatically (promote to Queen)
        Position promo = board.getPawnPromotion(currentTurn);
        if (promo != null)
            board.promotePawn(promo, currentTurn);

        // Switch turn
        currentTurn = currentTurn.opposite();

        // Update status
        updateStatus();
        return true;
    }
}