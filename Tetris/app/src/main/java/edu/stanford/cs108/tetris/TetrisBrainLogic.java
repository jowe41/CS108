package edu.stanford.cs108.tetris;

public class TetrisBrainLogic extends TetrisLogic {
    private DefaultBrain brain;
    private boolean UsedBrain;
    private Brain.Move bestMove;

    public TetrisBrainLogic(TetrisUIInterface uiInterface) {
        super(uiInterface);
        brain = new DefaultBrain();
    }

    public void UseDefaultBrain (boolean BrainOn){
        UsedBrain = BrainOn;
    }

    @Override
    protected void tick (int verb){
        if (UsedBrain && verb == DOWN && currentPiece != null) {
            board.undo();
            bestMove = brain.bestMove(board, currentPiece, HEIGHT, bestMove);
            if (bestMove != null) {
                if (!bestMove.piece.equals(currentPiece)) {
                    super.tick(ROTATE);
                }
                if (bestMove.x > currentX) {
                    super.tick(RIGHT);
                } else if (bestMove.x < currentX) {
                    super.tick(LEFT);
                }
            }
        }
        super.tick(verb);
    }

}
