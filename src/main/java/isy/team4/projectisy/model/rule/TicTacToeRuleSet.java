package isy.team4.projectisy.model.rule;

import isy.team4.projectisy.model.player.IPlayer;
import isy.team4.projectisy.util.Board;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public final class TicTacToeRuleSet implements IRuleSet {
    private Board oldBoard;

    private Board newBoard;

    @Override
    public Integer getMinPlayerSize() {
        return 2;
    }

    @Override
    public Integer getMaxPlayerSize() {
        return 2;
    }

    @Override
    public char[] getAllowedInitials() {
        return new char[]{'X', 'O'};
    }

    @Override
    public Board getStartingBoard() {
        return new Board(3, 3);
    }

    @Override
    public void setTurn(Board board, Board newBoard) {
        this.oldBoard = board;
        this.newBoard = newBoard;
    }

    @Override
    public boolean isLegal() {
        return this.newBoard.getTotalMovesMade() > this.oldBoard.getTotalMovesMade();
    }

    @Override
    public Board handleBoard(Board board) {
        return null;
    }

    @Override
    public boolean isWon() {
        return this.winningSet() != null;
    }

    private IPlayer[] winningSet() {
        Board board = this.newBoard;
        IPlayer[][] data = this.newBoard.getData();
        IPlayer[][] rotatedData = this.newBoard.getRotatedData();
        IPlayer[] output = null;

        // Check if horizontal or vertical has matching symbols
        for (int i = 0; i < board.getHeight(); i++) {
            if (this.hasAllSymbols(data[i])) {
                output = data[i];
            } else if (this.hasAllSymbols(rotatedData[i])) {
                output = rotatedData[i];
            }
        }

        IPlayer[] leftDiagMoves = new IPlayer[]{data[0][0], data[1][1], data[2][2]};
        IPlayer[] rightDiagMoves = new IPlayer[]{data[2][0], data[1][1], data[0][2]};

        // Check if diagonal has matching symbols.
        if (this.hasAllSymbols(leftDiagMoves)) {
            output = leftDiagMoves;
        } else if (this.hasAllSymbols(rightDiagMoves)) {
            output = rightDiagMoves;
        }

        return output;
    }

    private boolean hasAllSymbols(IPlayer[] moves) {
        Stream<IPlayer> stream = Arrays.stream(moves);
        return stream.noneMatch(Objects::isNull) && stream.map(IPlayer::getInitial).distinct().count() == 1;
    }

    @Override
    public IPlayer getWinningPlayer() throws NullPointerException {
        IPlayer[] winningSet = this.winningSet();

        if (winningSet != null) {
            return winningSet[0];
        }

        throw new NullPointerException();
    }

    @Override
    public boolean isDraw() {
        return ! this.isWon() &&  this.newBoard
                .getFlatData()
                .noneMatch(Objects::isNull);
    }
}
