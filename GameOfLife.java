import java.util.Arrays;
import java.util.Scanner;

public class GameOfLife {

    static Scanner sc = new Scanner(System.in);
    static int row;
    static int col;

    public static void main(String[] args) throws InterruptedException {

        System.out.print("Количество столбцов: ");
        row = sc.nextInt();
        System.out.print("Количество строк: ");
        col = sc.nextInt();

        String[][] arr = new String[row][col];

        for (int i=0; i<row; i++) {
            for (int j=0; j<col; j++) {
                arr[i][j] = "-";
            }
        }

        while (true) {
            System.out.print("(Введите \"-1\", чтобы закончить ввод)\nx=");
            int x = sc.nextInt();
            if (x==-1) {
                break;
            }
            System.out.print("y=");
            int y = sc.nextInt();
            if (x > row || y > col) {
                System.out.println("Координаты не соответствуют размеру матрицы");
                continue;
            }
            arr[x-1][y-1] = "*";
        }

        printArr(arr);

        System.out.println("Начало игры");

        String[][] arrOneStepAgo = new String[row][col];
        String[][] arrTwoStepAgo = new String[row][col];
        for (int step=1; step <=120; step++){
            System.out.println("Ход " + step);
            String[][] copiedArray = copy(arr);
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    int neighborsCount = getNeighborsCount(i, j, arr);

                    if (neighborsCount < 2 || neighborsCount > 3) {
                        copiedArray[i][j] = "-";
                    }
                    if (neighborsCount == 3) {
                        copiedArray[i][j] = "*";
                    }
                }
            }
            if (step>2) {
                arrTwoStepAgo = copy(arrOneStepAgo);
            }
            arrOneStepAgo = copy(arr);
            arr = copy(copiedArray);
            printArr(arr);
            if (isGameOut(arr, arrOneStepAgo, arrTwoStepAgo)) {
                break;
            };
            Thread.sleep(3000);
        }
    }

    private static int getNeighborsCount(int i, int j, String[][] arr) {
        int neighborsCount = 0;
        for (int neighborI = i -1; neighborI<= i +1; neighborI++) {
            for (int neighborJ = j -1; neighborJ<= j +1; neighborJ++) {
                if (neighborI == i && neighborJ == j) {
                    continue;
                }
                int tempI = neighborI == -1 ? row-1 :
                        neighborI == row ? 0 : neighborI;
                int tempJ = neighborJ == -1 ? col-1 :
                        neighborJ == col ? 0 : neighborJ;
                if (arr[tempI][tempJ].equals("*")) {
                    neighborsCount++;
                }
            }
        }
        return neighborsCount;
    }

    public static void printArr(String[][] arr) {
        for (int i=0; i<row; i++) {
            for (int j=0; j<col; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static String[][] copy(String[][] src) {
        if (src == null) {
            return null;
        }

        String[][] copy = new String[src.length][];
        for (int i = 0; i < src.length; i++) {
            copy[i] = Arrays.copyOf(src[i], src[i].length);
        }

        return copy;
    }

    public static boolean isGameOut (String[][] arr, String[][] arrOneStepAgo, String[][] arrTwoStepAgo) {
        boolean isHaveLife = false;
        boolean isArrCopiedOneStepAgoArr  = true;
        boolean isArrCopiedTwoStepAgoArray = true;
        for (int i=0; i<row; i++) {
            for (int j=0; j<col; j++) {
                isHaveLife |= arr[i][j].equals("*");
                isArrCopiedOneStepAgoArr &= arr[i][j].equals(arrOneStepAgo[i][j]);
                isArrCopiedTwoStepAgoArray &= arr[i][j].equals(arrTwoStepAgo[i][j]);
            }
        }

        if (!isHaveLife) {
            System.out.println("Конец игры! Жизни не осталось");
        } else if (isArrCopiedOneStepAgoArr) {
            System.out.println("Конец игры! Колония не измениться");
        } else if (isArrCopiedTwoStepAgoArray) {
            System.out.println("Конец игры! Цикл");
        }
        return !isHaveLife || isArrCopiedOneStepAgoArr || isArrCopiedTwoStepAgoArray;
    }
}