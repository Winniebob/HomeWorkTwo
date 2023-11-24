

import java.util.Random;
import java.util.Scanner;



public class Program {

    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = '0';
    private static final char DOT_EMPTY = '*';

    private static final int WIN_COUNT = 4;

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    private static char[][] field;
    private static int fieldSizeX;
    private static int fieldSizeY;


    public static void main(String[] args) {
        while (true) {
            initialize();
            printField();
            while (true) {
                humanTurn();
                printField();
                if (checkGameState(DOT_HUMAN, "Вы победили!"))
                    break;
                aiTurn();
                printField();
                if (checkGameState(DOT_AI, "Победил компьютер!"))
                    break;
            }
            System.out.print("Желаете сыграть еще раз? (1 - да, 0 - нет): ");
            if (!scanner.next().equals("1"))
                break;
        }
    }

    private static void initialize() {
        System.out.println("------ " + "Welcome to game!" + " ------");
        fieldSizeX = 5;
        fieldSizeY = 5;
        field = new char[fieldSizeY][fieldSizeX];
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                field[y][x] = DOT_EMPTY;
            }
        }
    }

    private static void printField() {
        for (int i = 0; i < fieldSizeX * 2 + 2; i++) {
            System.out.print("-");
        }
        System.out.println();
        System.out.print(" ");
        for (int i = 0; i < fieldSizeX; i++) {
            System.out.print(" " + (i + 1));
        }
        System.out.println(" ");

        for (int y = 0; y < fieldSizeY; y++) {
            System.out.print(y + 1 + "|");
            for (int x = 0; x < fieldSizeX; x++) {
                System.out.print(field[y][x] + "|");
            }
            System.out.println();
        }
        for (int i = 0; i < fieldSizeX * 2 + 2; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    private static boolean checkGameState(char symbol, String message) {
        if (checkWin(symbol)) {
            System.out.println(message);
            return true;
        }
        if (isDraw()) {
            System.out.println("Ничья!");
            return true;
        }
        return false;
    }

    private static boolean isDraw() {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (field[y][x] == DOT_EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean checkWin(char symbol) {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (checkLine(x, y, 1, 0, symbol) || // проверка горизонтальной линии
                        checkLine(x, y, 1, 1, symbol) || // проверка диагональной линии вправо
                        checkLine(x, y, 0, 1, symbol) || // проверка вертикальной линии
                        checkLine(x, y, 1, -1, symbol)) { // проверка диагональной линии влево
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean checkLine(int x, int y, int offsetX, int offsetY, char symbol) {
        int endX = x + (WIN_COUNT - 1) * offsetX;
        int endY = y + (WIN_COUNT - 1) * offsetY;
        if (endX >= fieldSizeX || endY >= fieldSizeY || endX < 0 || endY < 0) {
            return false;
        }
        for (int i = 0; i < WIN_COUNT; i++) {
            if (field[y + i * offsetY][x + i * offsetX] != symbol) {
                return false;
            }
        }
        return true;
    }

    private static void humanTurn() {
        int x, y;
        do {
            System.out.print("Введите координаты хода Х и Y (от 1 до " + fieldSizeX + "): ");
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        } while (!isValidCell(x, y) || !isEmptyCell(x, y));
        field[y][x] = DOT_HUMAN;
    }

    private static void aiTurn() {
        int x, y;

        // Ищем свободные ячейки для победы компьютера
        for (y = 0; y < fieldSizeY; y++) {
            for (x = 0; x < fieldSizeX; x++) {
                if (isEmptyCell(x, y)) {
                    field[y][x] = DOT_AI;
                    if (checkWin(DOT_AI)) {
                        return;
                    }
                    field[y][x] = DOT_EMPTY;
                }
            }
        }

        // Ищем свободные ячейки для блокировки хода игрока
        for (y = 0; y < fieldSizeY; y++) {
            for (x = 0; x < fieldSizeX; x++) {
                if (isEmptyCell(x, y)) {
                    field[y][x] = DOT_HUMAN;
                    if (checkWin(DOT_HUMAN)) {
                        field[y][x] = DOT_AI;
                        return;
                    }
                    field[y][x] = DOT_EMPTY;
                }
            }
        }

        // Если ни одна из вышеперечисленных ситуаций не возникла, делаем случайный ход
        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        } while (!isEmptyCell(x, y));
        field[y][x] = DOT_AI;
    }

    private static boolean isValidCell(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    private static boolean isEmptyCell(int x, int y) {
        return field[y][x] == DOT_EMPTY;
    }
}