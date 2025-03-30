import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Task5 extends Application {
    private final Color[] colors = {
            Color.RED, Color.BLUE, Color.GREEN,
            Color.YELLOW, Color.WHITE, Color.BLACK,
            Color.PURPLE, Color.ORANGE
    };

    private final String[] colorNames = {
            "Красный", "Синий", "Зеленый",
            "Желтый", "Белый", "Черный",
            "Фиолетовый", "Оранжевый"
    };

    private final Rectangle[] flagStripes = new Rectangle[3];

    @Override
    public void start(Stage primaryStage) {
        try {
            Image icon = new Image(getClass().getResourceAsStream("/icons/icon5.png"));
            primaryStage.getIcons().add(icon);
        } catch (Exception e) {
            System.out.println("Не удалось загрузить иконку: " + e.getMessage());
        }
        // Инициализация полос флага
        for (int i = 0; i < flagStripes.length; i++) {
            flagStripes[i] = new Rectangle(250, 100);
            flagStripes[i].setFill(colors[i]);
            flagStripes[i].setStroke(Color.GRAY);
        }

        // Главный контейнер
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #F5F5F5;");

        // Панель флага
        HBox flagBox = new HBox(15);
        flagBox.setAlignment(Pos.CENTER);
        flagBox.getChildren().addAll(flagStripes);
        flagBox.setStyle("-fx-border-color: #696969; -fx-border-width: 3; -fx-border-radius: 10;");
        flagBox.setPadding(new Insets(20));

        // Панель выбора цветов
        GridPane colorGrid = new GridPane();
        colorGrid.setAlignment(Pos.CENTER);
        colorGrid.setHgap(30);
        colorGrid.setVgap(15);
        colorGrid.setPadding(new Insets(20));

        // Создаем группы переключателей
        ToggleGroup[] groups = new ToggleGroup[3];
        for (int i = 0; i < 3; i++) {
            groups[i] = new ToggleGroup();

            Label stripeLabel = new Label("Полоса " + (i+1) + ":");
            stripeLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #2F4F4F;");
            colorGrid.add(stripeLabel, i, 0);

            for (int j = 0; j < colors.length; j++) {
                RadioButton rb = new RadioButton(colorNames[j]);
                rb.setToggleGroup(groups[i]);
                rb.setUserData(new ColorData(i, j));
                rb.setStyle("-fx-font-size: 16;");
                if (j == i) rb.setSelected(true);

                // Обработчик
                rb.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
                    if (isSelected) {
                        ColorData data = (ColorData) rb.getUserData();
                        flagStripes[data.stripeIndex].setFill(colors[data.colorIndex]);
                    }
                });

                colorGrid.add(rb, i, j+1);
            }
        }

        // Информационная панель
        Label resultLabel = new Label("Выберите цвета для каждой полосы флага");
        resultLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: #4B0082;");

        VBox bottomPanel = new VBox(30, colorGrid, resultLabel);
        bottomPanel.setAlignment(Pos.CENTER);
        bottomPanel.setPadding(new Insets(20));

        root.setCenter(flagBox);
        root.setBottom(bottomPanel);

        Scene scene = new Scene(root, 1000, 700);
        primaryStage.setTitle("Дизайнер флагов");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    // Вспомогательный класс для хранения данных о цвете
    private static class ColorData {
        final int stripeIndex;
        final int colorIndex;

        ColorData(int stripeIndex, int colorIndex) {
            this.stripeIndex = stripeIndex;
            this.colorIndex = colorIndex;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}