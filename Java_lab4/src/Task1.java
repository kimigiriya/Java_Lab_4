import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.text.Font;


public class Task1 extends Application {
    private boolean isForward = true; // Начальное положение

    @Override
    public void start(Stage primaryStage) {
        // Загрузка иконки
        try {
            Image icon = new Image(getClass().getResourceAsStream("/icons/icon1.png"));
            primaryStage.getIcons().add(icon);
        } catch (Exception e) {
            System.out.println("Не удалось загрузить иконку: " + e.getMessage());
        }
        // Поля для ввода с цветом
        TextField leftField = new TextField();
        leftField.setStyle("-fx-background-color: #FFF5E6; -fx-border-color: #FFAA80; -fx-border-radius: 5;");
        leftField.setFont(Font.font("Arial", 14));

        TextField rightField = new TextField();
        rightField.setStyle("-fx-background-color: #E6F7FF; -fx-border-color: #80C1FF; -fx-border-radius: 5;");
        rightField.setFont(Font.font("Arial", 14));

        // Кнопка с градиентом и анимацией
        Button switchButton = new Button("→");
        switchButton.setStyle("-fx-base: #FF8C66; -fx-font-size: 20; -fx-text-fill: white;");
        switchButton.setFont(Font.font("Arial", 20));

        // Фон для корневого элемента
        BackgroundFill bgFill = new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#FFEBCD")),
                        new Stop(1, Color.web("#FFDAB9"))),
                CornerRadii.EMPTY, Insets.EMPTY);

        switchButton.setOnAction(e -> {
            // Перекид справа налево
            if (isForward) {
                rightField.setText(leftField.getText());
                leftField.clear();
                switchButton.setText("←");
                switchButton.setStyle("-fx-base: #66B3FF; -fx-font-size: 20; -fx-text-fill: white;");
            }
            // Перекид слева направо
            else {
                leftField.setText(rightField.getText());
                rightField.clear();
                switchButton.setText("→");
                switchButton.setStyle("-fx-base: #FF8C66; -fx-font-size: 20; -fx-text-fill: white;");
            }
            isForward = !isForward; // Меняет сторону перекида
        });

        // Создание окна
        HBox root = new HBox(20, leftField, switchButton, rightField);
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(bgFill));
        root.setPadding(new Insets(20));

        Scene scene = new Scene(root, 500, 150);
        primaryStage.setTitle("Перекидыватель слов");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Открытие приложения
    public static void main(String[] args) {
        launch(args);
    }
}