import java.util.*;

class SpreadCalc {
    final String[][] data;
    final Double[][] auxilaryResult;

    public static void main(String[] args) {
        final SpreadCalc calc = new SpreadCalc();
        calc.readInput();
        calc.calc();
        calc.writeOutput();
    }

    public SpreadCalc() {
        this.data = new String[26][99];
        this.auxilaryResult = new Double[26][99];
    }

    private void readInput() {
        Scanner scanner = new Scanner(System.in);

        int row, col;
        row = 0;

        while (scanner.hasNext()) {
            col = 0;
            for (String cell : scanner.next().split(",")) {
                this.data[row][col] = cell.trim();
                tryParse(this.data[row][col], row, col);
                col++;
            }
            row++;
        }

        scanner.close();
    }

    private void tryParse(String s, int row, int col) {
        try {

            this.auxilaryResult[row][col] = Double.parseDouble(s);
        } catch (Exception e) {
            // System.out.println("Cannot parse a double from: " + s + "\n" + e);
        }
    }

    private void calc() {
        for (int i = 0; i < this.data.length; i++) {
            for (int j = 0; j < this.data[i].length; j++) {
                final String cell = this.data[i][j];
                // no more data starting from this cell
                if (cell == null) {
                    break;
                }
                if (this.auxilaryResult[i][j] != null || cell.charAt(0) == '\'') continue;
                if (cell.charAt(0) != '=') {
                    System.err.println("Invalid Cell at: (" + i + ", " + j + ")");
                    System.err.println("Value: " + cell);
                } else {
                    // first char always going to be '=', can ignore
                    this.auxilaryResult[i][j] = eval(cell.substring(1));
                }
            }
        }
    }

    private static Double eval(String s) {
        Set<Character> literalChars = new HashSet<>(Arrays.asList('0', '1', '2', '3', '4', '5',
                '6', '7', '8', '9', '.'));

        Double currNum = 0.0;
        Double prevNum = 0.0;
        char prevOp = '+';
        Double result = 0.0;

        for (int i = 0; i < s.length(); i++) {
            // TODO handle reference

            // TODO still need to consider 10e-1
            int j = i;
            while (literalChars.contains(s.charAt(i))) {
                j++;
            }
            if (j > i) {
                currNum = Double.parseDouble(s.substring(i, j));
                i = j;
            }

            // recursively evaluate expression inside parenthesis
            if (s.charAt(i) == '(') {
                int openParenCounter = 1;
                j = i + 1;
                while (openParenCounter > 0) {
                    if (s.charAt(j) == '(') {
                        openParenCounter++;
                    } else if (s.charAt(j) == ')') {
                        openParenCounter--;
                    }
                    j++;
                }

                currNum = eval(s.substring(i + 1, j));
                i = j;
            }

            if (prevOp == '+' || prevOp == '-') {
                result += prevNum;
                prevNum = currNum * (prevOp == '+' ? 1 : -1);
            } else if (prevOp == '*') {
                prevNum *= currNum;
            } else if (prevOp == '/') {
                prevNum = prevNum / currNum;
            }

            prevOp = s.charAt(i);
            currNum = 0.0;
        }

        return result + prevNum;
    }

    private void writeOutput() {
        for (int i = 0; i < this.data.length; i++) {
            final List<String> row = new ArrayList<>();
            for (int j = 0; j < this.data[i].length; j++) {
                // no more data starting from this cell
                if (this.data[i][j] == null) break;

                if (this.auxilaryResult[i][j] != null) {
                    row.add(String.format("%.02f", this.auxilaryResult[i][j]));
                } else {
                    row.add(this.data[i][j]);
                }
            }
            if (!row.isEmpty()) {
                System.out.println(String.join(",", row));
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder b = new StringBuilder();
        b.append("----- CALC -----\n");
        b.append("data:\n");
        b.append(Arrays.deepToString(this.data) + "\n");
        b.append("auxilaryResult:\n");
        b.append(Arrays.deepToString(this.auxilaryResult) + "\n");
        b.append("----- CALC -----\n");

        return b.toString();
    }
}
