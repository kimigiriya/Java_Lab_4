import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.Stack;

public class Task4 extends Application {
    private TextField display;
    private boolean resetDisplay = false;
    private String currentExpression = "";

    private final LinearGradient MAIN_GRADIENT = new LinearGradient(
            0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0, Color.web("#6A5ACD")),
            new Stop(1, Color.web("#9370DB"))
    );

    @Override
    public void start(Stage primaryStage) {
        try {
            Image icon = new Image(getClass().getResourceAsStream("/icons/icon4.png"));
            primaryStage.getIcons().add(icon);
        } catch (Exception e) {
            System.out.println("Не удалось загрузить иконку: " + e.getMessage());
        }
        // Настройка текстового поля
        display = new TextField();
        display.setEditable(false);
        display.setFont(Font.font("Roboto", 32));
        display.setStyle("-fx-background-color: #F8F8FF; -fx-border-color: #D8BFD8; -fx-border-radius: 8;");
        display.setPrefHeight(90);
        display.setAlignment(Pos.CENTER_RIGHT);

        // Сетка для кнопок калькулятора
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(12);
        buttonGrid.setVgap(12);
        buttonGrid.setPadding(new Insets(15));

        // Сами кнопки
        String[][] buttonLabels = {
                {"(", ")", "⌫", "C"},
                {"7", "8", "9", "÷"},
                {"4", "5", "6", "×"},
                {"1", "2", "3", "-"},
                {"0", ".", "+", "="}
        };

        // Размещение кнопок в сетке
        for (int row = 0; row < buttonLabels.length; row++) {
            for (int col = 0; col < buttonLabels[row].length; col++) {
                Button button = createButton(buttonLabels[row][col]);
                buttonGrid.add(button, col, row);
            }
        }
        // Основной контейнер
        VBox root = new VBox(15, display, buttonGrid);
        root.setPadding(new Insets(25));
        root.setBackground(new Background(new BackgroundFill(MAIN_GRADIENT, CornerRadii.EMPTY, Insets.EMPTY)));

        // Сцена
        Scene scene = new Scene(root, 380, 550);
        primaryStage.setTitle("Калькулятор");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    // Отрисовка красивых кнопок
    private Button createButton(String text) {
        Button button = new Button(text);
        button.setFont(Font.font("Roboto", 22));
        button.setPrefSize(70, 70);
        button.setStyle(getButtonStyle(text));
        button.setOnAction(e -> handleButtonClick(text));
        return button;
    }

    private String getButtonStyle(String text) {
        String style = "-fx-background-radius: 35; -fx-border-radius: 35;";

        if (text.matches("[0-9.]")) {
            style += "-fx-background-color: #E6E6FA; -fx-text-fill: #483D8B;";
        } else if (text.equals("=")) {
            style += "-fx-background-color: #483D8B; -fx-text-fill: white;";
        } else {
            style += "-fx-background-color: #9370DB; -fx-text-fill: white;";
        }

        return style;
    }

    // Обработчик событий
    private void handleButtonClick(String buttonText) {
        switch (buttonText) {
            case "C":
                display.clear();
                currentExpression = "";
                resetDisplay = false;
                break;
            case "⌫":
                if (!display.getText().isEmpty()) {
                    String currentText = display.getText();
                    display.setText(currentText.substring(0, currentText.length() - 1));
                    if (!currentExpression.isEmpty()) {
                        currentExpression = currentExpression.substring(0, currentExpression.length() - 1);
                    }
                }
                break;
            case "=":
                calculateResult();
                resetDisplay = true;
                break;
            default:
                if (resetDisplay ) {
                    display.clear();
                    resetDisplay = false;
                }
                display.setText(display.getText() + buttonText);
                currentExpression += buttonText;
        }
    }
    // Вычисляет результат
    private void calculateResult() {
        try {
            String expression = currentExpression
                    .replace("×", "*")
                    .replace("÷", "/")
                    .replace(",", ".");

            double result = evaluateExpression(expression);

            if (result == (long) result) {
                display.setText(String.format("%d", (long) result));
            } else {
                display.setText(String.format("%s", result).replace(".", ","));
            }
            currentExpression = display.getText();

        } catch (Exception e) {
            display.setText("Ошибка");
            currentExpression = "";
        }
    }

    // Вычисление математический выражений
    private double evaluateExpression(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        try {

            for (int i = 0; i < expression.length(); i++) {
                char c = expression.charAt(i);

                if (Character.isDigit(c) || c == '.') {
                    StringBuilder numStr = new StringBuilder();
                    while (i < expression.length() &&
                            (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                        numStr.append(expression.charAt(i++));
                    }
                    i--;
                    numbers.push(Double.parseDouble(numStr.toString()));
                } else if (c == '(') {
                    operators.push(c);
                } else if (c == ')') {
                    while (!operators.isEmpty() && operators.peek() != '(') {
                        numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                    }
                    if (!operators.isEmpty()) {
                        operators.pop();
                    }else {
                        throw new IllegalArgumentException("Недопустимое выражение, несогласованные круглые скобки");
                    }
                } else if (isOperator(c)) {
                    while (!operators.isEmpty() && hasPrecedence(c, operators.peek())) {
                        numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
                    }
                    operators.push(c);
                } else if (Character.isWhitespace(c)){
                } else {
                    throw new IllegalArgumentException("Invalid character in expression: " + c);
                }
            }

            while (!operators.isEmpty()) {
                numbers.push(applyOperation(operators.pop(), numbers.pop(), numbers.pop()));
            }

            if (numbers.size() != 1) {
                throw new IllegalArgumentException("Invalid expression");
            }

            return numbers.pop();

        } catch (Exception e) {
            throw new IllegalArgumentException("Error during evaluation: " + e.getMessage());

        }
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private boolean hasPrecedence(char op1, char op2) {
        if (op2 == '(' || op2 == ')') return false;
        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) return false;
        return true;
    }

    private double applyOperation(char op, double b, double a) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/':
                if (b == 0) throw new ArithmeticException("Деление на ноль");
                return a / b;
            default: return 0;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}