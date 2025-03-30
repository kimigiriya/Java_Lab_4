import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Task2 extends Application {
    @Override
    public void start(Stage primaryStage) {
        // Загрузка иконки
        try {
            Image icon = new Image(getClass().getResourceAsStream("/icons/icon2.png"));
            primaryStage.getIcons().add(icon);
        } catch (Exception e) {
            System.out.println("Не удалось загрузить иконку: " + e.getMessage());
        }
        // Виджеты с разными цветами
        Label label = new Label("Чтобы быть успешным нужно...[читать далее]");
        label.setStyle("-fx-text-fill: #5E2CA5; -fx-font-size: 14;");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        TextField textField = new TextField("Тут могла быть ваша реклама");
        textField.setStyle("-fx-background-color: #F0F8FF; -fx-border-color: #4682B4;");

        Button button = new Button("Just Button");
        button.setStyle("-fx-base: #20B2AA; -fx-text-fill: white;");

        ProgressBar progressBar = new ProgressBar(0.5);
        progressBar.setStyle("-fx-accent: #FF6347;");

        // Цветные чекбоксы
        CheckBox labelCheck = createStyledCheckbox("Показать метку", "#5E2CA5");
        CheckBox textFieldCheck = createStyledCheckbox("Показать поле ввода", "#4682B4");
        CheckBox buttonCheck = createStyledCheckbox("Показать кнопку", "#20B2AA");
        CheckBox progressCheck = createStyledCheckbox("Показать прогресс", "#FF6347");

        // Устанавливаем все чекбоксы в выбранное состояние
        labelCheck.setSelected(true);
        textFieldCheck.setSelected(true);
        buttonCheck.setSelected(true);
        progressCheck.setSelected(true);

        // Обработчики для чекбоксов
        labelCheck.selectedProperty().addListener((obs, oldVal, newVal) ->
                label.setVisible(newVal));
        textFieldCheck.selectedProperty().addListener((obs, oldVal, newVal) ->
                textField.setVisible(newVal));
        buttonCheck.selectedProperty().addListener((obs, oldVal, newVal) ->
                button.setVisible(newVal));
        progressCheck.selectedProperty().addListener((obs, oldVal, newVal) ->
                progressBar.setVisible(newVal));

        // Цветные панели для виджетов
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #F5F5F5;");

        root.getChildren().addAll(
                createWidgetBox(label, labelCheck),
                createWidgetBox(textField, textFieldCheck),
                createWidgetBox(button, buttonCheck),
                createWidgetBox(progressBar, progressCheck)
        );

        Scene scene = new Scene(root, 500, 250);
        primaryStage.setTitle("Управление виджетами");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HBox createWidgetBox(javafx.scene.Node widget, CheckBox checkBox) {
        HBox box = new HBox(15, widget, checkBox);
        box.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        return box;
    }

    private CheckBox createStyledCheckbox(String text, String color) {
        CheckBox cb = new CheckBox(text);
        cb.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");
        return cb;
    }

    public static void main(String[] args) {
        launch(args);
    }
}