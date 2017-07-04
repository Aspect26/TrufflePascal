package cz.cuni.mff.d3s.trupple.main;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.vm.PolyglotEngine;
import cz.cuni.mff.d3s.trupple.language.PascalLanguage;

import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;

public class TicTacToeMain {

    public static void main(String[] args) throws Exception {
        Source source = Source.newBuilder(new File(args[0])).mimeType(PascalLanguage.MIME_TYPE).build();
        Game game = new Game(source);
        try {
            game.startGame();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            System.out.print("Game over.");
        }
    }

    private static class Game {

        private static final int EMPTY = 0;
        private static final int PLAYER = 1;
        private static final int AI = 2;

        private int[][] data = {{EMPTY,EMPTY,EMPTY},{EMPTY,EMPTY,EMPTY},{EMPTY,EMPTY,EMPTY}};

        private Scanner input = new Scanner(System.in);
        private PrintStream output = System.out;

        private PolyglotEngine engine;
        private PolyglotEngine.Value parsedAIScript;

        private Game(Source aiSource) {
            initPolyglot(aiSource);
        }

        void startGame() {
            print();
            while (true) {
                playerMove();
                print();
                if (isOver()) {
                    playerWins();
                    break;
                }

                if (isFull()) {
                    tie();
                    break;
                }

                aiMove();
                print();
                if (isOver()) {
                    aiWins();
                    break;
                }
            }
        }

        private void initPolyglot(Source aiSource) {
            this.engine = PolyglotEngine.newBuilder().setErr(System.err).build();
            this.parsedAIScript = engine.eval(aiSource);
        }

        private void tie() {
            this.output.println("It is a draw!");
        }

        private void aiWins() {
            this.output.println("You lost! Ouch...");
        }

        private void aiMove() {
            this.output.println("AI's turn:");
            PolyglotEngine.Value result = this.parsedAIScript.execute(data[0][0], data[0][1], data[0][2], data[1][0],
                    data[1][1], data[1][2], data[2][0], data[2][1], data[2][2]);
            int position = result.as(Integer.class);
            if (!setCell(position / 3, position % 3, AI)) {
                throw new RuntimeException("The AI script returned invalid slot!");
            }
        }

        private void playerWins() {
            this.output.println("You won! Yay!");
        }

        private void playerMove() {
            this.output.print("Your turn. Please choose row and column (e.g. 1 2): ");
            int row = this.input.nextInt();
            int column = this.input.nextInt();
            while (!setCell(row - 1, column - 1, PLAYER)) {
                row = this.input.nextInt();
                column = this.input.nextInt();
            }
        }

        private void print() {
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    switch(data[i][j]) {
                        case EMPTY: this.output.print((char)0x25A1); break;
                        case PLAYER: this.output.print((char)0x2713); break;
                        case AI: this.output.print('X'); break;
                    }
                }
                this.output.println();
            }
        }

        private boolean isOver() {
            for (int i = 0; i < 3; ++i) {
                if (checkRow(i) || checkColumn(i)) {
                    return true;
                }
            }
            return (checkFirstDiagonal() || checkSecondDiagonal());
        }

        private boolean isFull() {
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    if (data[i][j] == EMPTY) {
                        return false;
                    }
                }
            }

            return true;
        }

        private boolean setCell(int row, int column, int value) {
            if (row < 0 || row > 2 || column < 0 || column > 2) {
                this.output.println("Selected position is invalid!");
                return false;
            }
            if (data[row][column] != EMPTY) {
                this.output.println("Selected position is already taken!");
                return false;
            }
            data[row][column] = value;
            return true;
        }

        private boolean checkRow(int i) {
            return data[i][0] != EMPTY && (data[i][0] == data[i][1] && data[i][1] == data[i][2]);
        }

        private boolean checkColumn(int i) {
            return data[0][i] != EMPTY && (data[0][i] == data[1][i] && data[1][i] == data[2][i]);
        }

        private boolean checkFirstDiagonal() {
            return data[0][0] != EMPTY && (data[0][0] == data[1][1] && data[1][1] == data[2][2]);
        }

        private boolean checkSecondDiagonal() {
            return data[2][0] != EMPTY && (data[2][0] == data[1][1] && data[1][1] == data[0][2]);
        }

    }

}
