import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class Task3 extends Application {
    private Map<String, Double> menu = new HashMap<>();
    private Map<String, Spinner<Integer>> orderItems = new HashMap<>();
    private TextArea receiptArea;
    private boolean discountApplied = false;
    private final double DISCOUNT_PERCENT = 0.15; // 15% скидка

    @Override
    public void start(Stage primaryStage) {
        try {
            Image icon = new Image(getClass().getResourceAsStream("/icons/icon3.png"));
            primaryStage.getIcons().add(icon);
        } catch (Exception e) {
            System.out.println("Не удалось загрузить иконку: " + e.getMessage());
        }

        // Меню
        menu.put("🍲 Суп", 250.0);
        menu.put("🥗 Салат", 180.0);
        menu.put("🍖 Стейк", 550.0);
        menu.put("🍝 Паста", 320.0);
        menu.put("🍰 Десерт", 200.0);
        menu.put("🥤 Напиток", 120.0);

        // Панель меню
        VBox menuBox = new VBox(10);
        menuBox.setPadding(new Insets(15));
        menuBox.setStyle("-fx-background-color: #FFF8F0; -fx-border-color: #FFA07A; -fx-border-radius: 5;");

        for (String item : menu.keySet()) {
            CheckBox checkBox = new CheckBox(item);
            checkBox.setStyle("-fx-text-fill: #556B2F; -fx-font-weight: bold;");

            Spinner<Integer> spinner = new Spinner<>(0, 10, 0);
            spinner.setDisable(true);
            spinner.setMaxWidth(80);
            spinner.setStyle("-fx-base: #98FB98;");

            checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> {
                spinner.setDisable(!newVal);
                if (newVal && spinner.getValue() == 0) {
                    spinner.getValueFactory().setValue(1);
                }
            });

            HBox itemBox = new HBox(10, checkBox, new Label("Кол-во:"), spinner);
            menuBox.getChildren().add(itemBox);
            orderItems.put(item, spinner);
        }

        // Кнопки
        Button calculateButton = new Button("Рассчитать заказ");
        calculateButton.setStyle("-fx-base: #FF6347; -fx-text-fill: white; -fx-font-weight: bold;");

        Button discountButton = new Button("Применить скидку 15%");
        discountButton.setStyle("-fx-base: #9370DB; -fx-text-fill: white; -fx-font-weight: bold;");

        // Настройка области чека
        receiptArea = new TextArea();
        receiptArea.setEditable(false);
        receiptArea.setStyle("-fx-control-inner-background: #F0FFF0; -fx-font-family: 'Arial'; -fx-font-size: 14;");

        // Обработчики событий
        calculateButton.setOnAction(e -> calculateOrder());
        discountButton.setOnAction(e -> {
            discountApplied = !discountApplied;
            discountButton.setText(discountApplied ? "Отменить скидку" : "Применить скидку 15%");
            discountButton.setStyle(discountApplied ?
                    "-fx-base: #FF4500; -fx-text-fill: white;" :
                    "-fx-base: #9370DB; -fx-text-fill: white;");
            calculateOrder();
        });

        // Панель кнопок
        HBox buttonBox = new HBox(15, calculateButton, discountButton);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);

        // Основной контейнер
        VBox root = new VBox(15,
                new Label("Меню ресторана:"),
                menuBox,
                buttonBox,
                receiptArea
        );
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #FFE4B5, #FFDEAD);");

        Scene scene = new Scene(root, 500, 600);
        primaryStage.setTitle("Ресторанный заказ");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void calculateOrder() {
        double total = 0.0;
        StringBuilder receipt = new StringBuilder("///// Ваш Заказ /////\n\n");

        for (Map.Entry<String, Spinner<Integer>> entry : orderItems.entrySet()) {
            String item = entry.getKey();
            int quantity = entry.getValue().getValue();

            if (quantity > 0) {
                double price = menu.get(item);
                double itemTotal = price * quantity;
                total += itemTotal;

                receipt.append(String.format("%-15s %2d x %6.2f = %7.2f руб.\n",
                        item, quantity, price, itemTotal));
            }
        }

        // Применяем скидку если активирована
        if (discountApplied && total > 0) {
            double discountAmount = total * DISCOUNT_PERCENT;
            receipt.append(String.format("\n%-15s %13.2f руб.", "Скидка 15%:", -discountAmount));
            total -= discountAmount;
        }

        receipt.append(String.format("\n\n%-15s %13.2f руб.", "Итого:", total));
        receiptArea.setText(receipt.toString());
    }

    public static void main(String[] args) {
        launch(args);
    }
}