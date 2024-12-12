package org.example.weathr.View;

import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.weathr.Controllers.ParseLocation;
import org.example.weathr.Controllers.ParseWeather;
import org.example.weathr.Data.Exceptions.ReceivingWeatherException;
import org.example.weathr.Data.Exceptions.RetrivingException;
import org.example.weathr.Data.LocationData;
import org.example.weathr.Data.PropertyLoader;
import org.example.weathr.Data.WeatherData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class WeatherViewController implements Initializable {
    public Label resultLabel;
    public ScrollPane scrollPaneResults;
    public TextField sityNameField;
    public Label tempLable;
    public Label feelsLikeLable;
    public Label tempMinLable;
    public Label tempMaxLable;
    public Label humidityLable;
    public Label pressureLable;
    public Label windLable;
    public Label visibilityLable;
    public Label PrecipitationLable;
    public ScrollPane forecastPanel;
    public Label sityLable;
    public ImageView mainIconView;
    PropertyLoader propertyLoader;

    private static final Logger logger = LoggerFactory.getLogger(WeatherViewController.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            propertyLoader = new PropertyLoader();
            String sityName = propertyLoader.getSityName();
            String sityCountry = propertyLoader.getCountry();
            double lat = Double.parseDouble(propertyLoader.getLat());
            double lon = Double.parseDouble(propertyLoader.getLon());
            forecastPanel.setContent(loadWeather(lat, lon, sityName, sityCountry));
        } catch (IOException e) {
            throw new RetrivingException("Unable to retrieve previous city data: " + e.getMessage());
        }
    }

    public void onSityTextInput() throws IOException {
        String sityName = sityNameField.getText();
        if(Objects.equals(sityName, "")){
            return;
        }

        ParseLocation pl = new ParseLocation();
        List<LocationData> locations =  pl.parseCityLocation(sityName);

        HBox hb = new HBox();

        for (LocationData location : locations) {
            Button sityButton = getSityButton(location);
            hb.getChildren().add(sityButton);
        }
        resultLabel.setVisible(true);
        scrollPaneResults.setContent(hb);

        if(locations.isEmpty()){
            Label l = new Label("Городов не найдено");
            scrollPaneResults.setContent(l);
        }
    }

    private Button getSityButton(LocationData location) {
        Button sityButton = new Button(location.getName() + " " +location.getCountry());
        sityButton.setOnAction(event -> {
        try {
            forecastPanel.setContent(loadWeather(location.getLat(), location.getLon(), location.getName(), location.getCountry()));
            propertyLoader.setNewValue(location.getName(), location.getCountry(), String.valueOf(location.getLat()), String.valueOf(location.getLon()));
        } catch (IOException e) {
            throw new ReceivingWeatherException("Error receiving weather data: " + e.getMessage());
        }
        });
        return sityButton;
    }

    private HBox loadWeather(double lat, double lon, String sityName, String sityCountry) throws IOException {
        ParseWeather pw = new ParseWeather();
        WeatherData weathers = pw.getCurrentWeather(lat, lon);
        tempLable.setText("Температура: " + weathers.getTemp() + "°C");
        feelsLikeLable.setText("По ощущениям: " + weathers.getFeelsLike() + "°C");
        tempMinLable.setText("Мин. температура: " + weathers.getTempMin() + "°C");
        tempMaxLable.setText("Макс. температура: " + weathers.getTempMax() + "°C");
        humidityLable.setText("Влажность: " + weathers.getHumidity() + "%");
        pressureLable.setText("Давление: " + weathers.getPressure());
        windLable.setText(weathers.getDeg() + " ветер " + weathers.getWindSpeed() + " м/с");
        visibilityLable.setText("Видимость: " + ((int) weathers.getVisibility()) + " км");

        if (weathers.getRain() != null && weathers.getClouds() != null) {
            PrecipitationLable.setText("Осадки: Дождь:" + weathers.getRain().getH1() + " мм/ч Снег:" + weathers.getClouds().getAll());
        }
        if (weathers.getRain() != null) {
            PrecipitationLable.setText("Осадки: Дождь:" + weathers.getRain().getH1() + " мм/ч");
        }
        if (weathers.getClouds() != null) {
            PrecipitationLable.setText("Осадки: Снег: " + weathers.getClouds().getAll() + " мм/ч");
        }

        sityLable.setText(sityName + " " + sityCountry);
        sityLable.setVisible(true);
        mainIconView.setImage(new Image("https://openweathermap.org/img/wn/" + weathers.getIcon() + "@2x.png"));

        HBox hb = new HBox();
        List<WeatherData> weatherDataShortList = pw.getWeatherForecast(lat, lon);

        for (int i = 1; i < weatherDataShortList.size(); i++) {
            VBox wdsvb = getForecastWeatherBox(weatherDataShortList, i);
            hb.getChildren().add(wdsvb);
        }
        logger.info("Загружена информация о погоде для города: {} {}", sityName, sityCountry);
        return hb;
    }

    private static VBox getForecastWeatherBox(List<WeatherData> weatherDataShortList, int i) {
        Label date = new Label("Дата: " + weatherDataShortList.get(i).getDate().substring(0, 10));
        Label status = new Label(weatherDataShortList.get(i).getStatus());
        Image icon = new Image("https://openweathermap.org/img/wn/" + weatherDataShortList.get(i).getIcon() + "@2x.png");
        ImageView iconView = new ImageView(icon);
        iconView.setFitHeight(40);
        iconView.setFitWidth(40);
        Label avgTemp = new Label("Средняя температура: " + weatherDataShortList.get(i).getTemp());
        Label minTemp = new Label("Минимальная температура: " + weatherDataShortList.get(i).getTempMin());
        Label maxTemp = new Label("Максимальная температура: " + weatherDataShortList.get(i).getTempMax());
        VBox wdsvb = new VBox(date, status, iconView, avgTemp, minTemp, maxTemp);
        wdsvb.setPadding(new Insets(0, 10, 0, 10));
        wdsvb.setStyle("-fx-background-color: #bbbbbb;");
        return wdsvb;
    }
}
