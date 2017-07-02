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
        PolyglotEngine engine = PolyglotEngine.newBuilder().setErr(System.err).build();

        PolyglotEngine.Value v = engine.eval(source);
        v.execute();
        engine.dispose();
    }

    private static class Game {

        private static final int EMPTY = 0;
        private static final int PLAYER = 1;
        private static final int AI = 2;
        private int[][] data = {{EMPTY,EMPTY,EMPTY},{EMPTY,EMPTY,EMPTY},{EMPTY,EMPTY,EMPTY}};
        private Scanner input = new Scanner(System.in);
        private PrintStream output = System.out;

        public void startGame() {
            while (true) {
                print();

                playerMove();
                if (isOver()) {
                    playerWins();
                    break;
                }

                aiMove();
                if (isOver()) {
                    aiWins();
                    break;
                }
            }
        }

        private void aiWins() {
            // TODO: implement
        }

        private void aiMove() {
            // TODO: implement
        }

        private void playerWins() {
            this.output.println("You won! Yay!");
        }

        private void playerMove() {
            this.output.println("Your turn. Please choose row and column (e.g. 1 2): ");
            int row = this.input.nextInt();
            int column = this.input.nextInt();
            while (this.data[row][column] != EMPTY) {
                this.output.println("Selected field is already taken, please choose another: ");
                row = this.input.nextInt();
                column = this.input.nextInt();
            }

            this.data[row][column] = PLAYER;
        }

        private void print() {
            for (int i = 0; i < 3; ++i) {
                for (int j = 0; j < 3; ++j) {
                    this.output.print(data[i][j]);
                }
                this.output.println();
            }
        }

        private boolean isOver() {
            // TODO: implement
            return false;
        }

    }

}
