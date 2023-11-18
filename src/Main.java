import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    private static final List<String> NUMBERS = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    private static final List<String> SYMBOLS = List.of("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z");

    private static final Random random = new Random();
    private static final Scanner scanner = new Scanner(System.in);

    private static int counter = 1;

    public static void main(String[] args) {
        try {
            int length = askForLengthOfSecret();
            int range = askForRangeOfPossibleSymbols();
            if (range < length) {
                String message = String.format("Error: it's not possible to generate a code with a length of %d with %d unique symbols.", length, range);
                throw new RuntimeException(message);
            }

            List<String> generatedSecret = generateRandomSecret(length, range);
            System.out.println("Okay, let's start a game!");
            boolean isGuessed = false;
            while (!isGuessed) {
                System.out.printf("Turn %d:\n", counter);
                List<String> userInput = readUserInput();
                int bulls = 0;
                int cows = 0;
                for (int i = 0; i < generatedSecret.size(); i++) {
                    String currentGuess = userInput.get(i);
                    String currentSecret = generatedSecret.get(i);
                    if (currentGuess.equals(currentSecret)) {
                        bulls++;
                    } else if (generatedSecret.contains(currentGuess)) {
                        cows++;
                    }
                }

                String bullString;
                if (bulls == 0) {
                    bullString = "";
                } else if (bulls == 1) {
                    bullString = "1 bull";
                } else {
                    bullString = String.format("%d bulls", bulls);
                }
                String cowString;
                if (cows == 0) {
                    cowString = "";
                } else if (cows == 1) {
                    cowString = "and 1 cow";
                } else {
                    cowString = String.format("and %d cows", cows);
                }

                if (bulls > 0 || cows > 0) {
                    System.out.printf("Grade: %s %s\n", bullString, cowString);
                } else {
                    System.out.println("Grade: None.");
                }
                isGuessed = generatedSecret.equals(userInput);
                counter++;
            }

            System.out.println("Congratulations! You guessed the secret code.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static int askForRangeOfPossibleSymbols() {
        System.out.println("Input the number of possible symbols in the code:");
        int range;
        String next = null;
        try {
            next = scanner.nextLine();
            range = Integer.parseInt(next);
        } catch (NumberFormatException e) {
            throw new RuntimeException(String.format("Error: \"%s\" isn't a valid number.", next));
        }
        if (range > SYMBOLS.size() + NUMBERS.size()) {
            throw new RuntimeException("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
        } else if (range < 0) {
            throw new RuntimeException("Error: minimum number of possible symbols in the code is 0.");
        }

        return range;
    }

    private static int askForLengthOfSecret() {
        System.out.println("Please, enter the secret code's length:");
        int length;
        String next = null;
        try {
            next = scanner.nextLine();
            length = Integer.parseInt(next);
        } catch (NumberFormatException e) {
            throw new RuntimeException(String.format("Error: \"%s\" isn't a valid number.", next));
        }

        if (length > NUMBERS.size() + SYMBOLS.size() || length <= 0) {
            String errorMessage = String.format("Error: can not create secret with lenght %d", length);
            throw new RuntimeException(errorMessage);
        }

        return length;
    }

    private static List<String> readUserInput() {
        List<String> userInput = new LinkedList<>();
        for (char c : scanner.next().toCharArray()) {
            userInput.add(String.valueOf(c));
        }

        return userInput;
    }

    private static List<String> generateRandomSecret(int length, int range) {
        List<String> newSecret = new LinkedList<>();
        List<String> list = new LinkedList<>(NUMBERS);
        list.addAll(SYMBOLS.subList(0, Math.abs(11 - range)));
        while (newSecret.size() < length) {
            int randomIndex = random.nextInt(list.size());
            String sign = list.get(randomIndex);
            if ((newSecret.isEmpty() && sign.equals("0")) || newSecret.contains(sign)) {
                continue;
            }

            newSecret.add(sign);
        }

        StringBuilder stars = new StringBuilder(newSecret.size());
        newSecret.forEach(c -> stars.append("*"));
        System.out.printf("The secret is prepared: %s (0-9, %s-%s).\n", stars, SYMBOLS.get(0), SYMBOLS.get(Math.abs(11 - range)));
        return newSecret;
    }
}
