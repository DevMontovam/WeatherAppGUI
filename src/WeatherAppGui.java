import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.json.simple.JSONObject;

public class WeatherAppGui extends JFrame{
    private JSONObject weatherData;
    private JLabel temperatureText;
    private JLabel weatherConditionImage;
    private JLayeredPane layeredPane;
    private JLabel weatherConditionWallpaper;
    private JLabel weatherConditionDesc;
    private JLabel humidityText;
    private JLabel windspeedText;

    public WeatherAppGui(){
        super("Weather App");

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setSize(450, 650);

        setLocationRelativeTo(null);

        setLayout(null);

        setResizable(false);

        addGUIComponents();

        try {
            updateWeather();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addGUIComponents(){        
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 450, 650);
        add(layeredPane);
        System.out.println(layeredPane.getWidth() + " " + layeredPane.getHeight());
        weatherConditionWallpaper = new JLabel(loadImage(""));
        weatherConditionWallpaper.setBounds(0, 0, 450, 650);
        layeredPane.add(weatherConditionWallpaper, JLayeredPane.DEFAULT_LAYER);
        
        RoundedPlaceHolderTextField searchTextField = new RoundedPlaceHolderTextField("Digite aqui ");
        searchTextField.setBounds(15, 15, 405, 45);
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));
        layeredPane.add(searchTextField, JLayeredPane.PALETTE_LAYER);

        weatherConditionImage = new JLabel(loadImage(""));
        weatherConditionImage.setBounds(0,125, 450, 217);
        layeredPane.add(weatherConditionImage, JLayeredPane.PALETTE_LAYER); 

        temperatureText = new JLabel("");
        temperatureText.setBounds(0, 350, 450, 54);
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));
        temperatureText.setForeground(Color.WHITE);
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        layeredPane.add(temperatureText, JLayeredPane.PALETTE_LAYER); 

        weatherConditionDesc = new JLabel("");
        weatherConditionDesc.setBounds(0, 405, 450, 36);
        weatherConditionDesc.setFont(new Font("Dialog", Font.PLAIN, 32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        weatherConditionDesc.setForeground(Color.WHITE);
        layeredPane.add(weatherConditionDesc, JLayeredPane.PALETTE_LAYER);

        JLabel humidityImage = new JLabel(loadScaledImageButton("src/weather_images/derrubar.png", 74, 74));
        humidityImage.setBounds(15, 500, 74, 74);
        layeredPane.add(humidityImage, JLayeredPane.PALETTE_LAYER); 

        humidityText = new JLabel("");
        humidityText.setBounds(90, 500, 85, 55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        humidityText.setForeground(Color.WHITE);
        layeredPane.add(humidityText, JLayeredPane.PALETTE_LAYER); 

        JLabel windsppedImage = new JLabel(loadScaledImageButton("src/weather_images/vento.png", 74, 74));
        windsppedImage.setBounds(220, 500, 74, 74);
        layeredPane.add(windsppedImage, JLayeredPane.PALETTE_LAYER); 

        windspeedText = new JLabel("");
        windspeedText.setBounds(310, 500, 85, 55);
        windspeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        windspeedText.setForeground(Color.WHITE);
        layeredPane.add(windspeedText, JLayeredPane.PALETTE_LAYER); 

        JButton searchButton = new JButton(loadScaledImageButton("src/weather_images/lupa.png", 32, 32));
        searchButton.setOpaque(false);
        searchButton.setContentAreaFilled(false);
        searchButton.setBorderPainted(false);
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375, 20, 32, 32);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                String userInput = searchTextField.getText();

                if(userInput.replaceAll("\\s", "").length() <= 0)
                    return;

                weatherData = WeatherApp.getWeatherData(userInput);

                String weatherCondition = (String) weatherData.get("weather_condition");
                switch (weatherCondition) {
                    case "Clear":
                        weatherConditionImage.setIcon(loadImage("src/weather_images/clear.png"));
                        weatherConditionWallpaper.setIcon(loadScaledImage("src/weather_images/wallpapers/sunny_environment.jpg", layeredPane.getHeight()));
                        break;
                    case "Cloudy":
                        weatherConditionImage.setIcon(loadImage("src/weather_images/cloudy.png"));
                        weatherConditionWallpaper.setIcon(loadScaledImage("src/weather_images/wallpapers/cloudy_environment.jpg", layeredPane.getHeight()));
                        break;
                    case "Rain":
                        weatherConditionImage.setIcon(loadImage("src/weather_images/rain.png"));
                        weatherConditionWallpaper.setIcon(loadScaledImage("src/weather_images/wallpapers/rainy_environment.jpg", layeredPane.getHeight()));
                        break;
                    case "Snow":
                        weatherConditionImage.setIcon(loadImage("src/weather_images/snow.png"));
                        weatherConditionWallpaper.setIcon(loadScaledImage("src/weather_images/wallpapers/snowy_environment.jpg", layeredPane.getHeight()));
                        break;
                    default:
                        break;
                }

                double temperature = (double) weatherData.get("temperature");
                temperatureText.setText(temperature + " °C");

                weatherConditionDesc.setText(weatherCondition);

                long humidity = (long) weatherData.get("humidity");
                humidityText.setText("<html><b>Humidity</b> " + humidity + "% </html>");
                
                double windspeed = (double) weatherData.get("windspeed");                
                windspeedText.setText("<html><b>Windspeed</b> " + windspeed + "Km/h </html>");
            }
        });
        layeredPane.add(searchButton, JLayeredPane.PALETTE_LAYER); 
        layeredPane.setLayer(searchButton, layeredPane.getLayer(searchTextField) + 1);

    }

    private ImageIcon loadImage(String resourcePath) {

        try{
            BufferedImage image = ImageIO.read(new File(resourcePath));

            return new ImageIcon(image);
        }catch(IOException e){
            e.printStackTrace();
        }

        System.out.println("Could not find resource");
        return null;
    }

    private void updateWeather() throws IOException {
        String location = getUserLocation();

        JSONObject weatherData = WeatherApp.getWeatherData(location);

        String weatherCondition = (String) weatherData.get("weather_condition");
        switch (weatherCondition) {
            case "Clear":
                weatherConditionImage.setIcon(loadImage("src/weather_images/clear.png"));
                weatherConditionWallpaper.setIcon(loadScaledImage("src/weather_images/wallpapers/sunny_environment.jpg", layeredPane.getHeight()));
                break;
            case "Cloudy":
                weatherConditionImage.setIcon(loadImage("src/weather_images/cloudy.png"));
                weatherConditionWallpaper.setIcon(loadScaledImage("src/weather_images/wallpapers/cloudy_environment.jpg", layeredPane.getHeight()));
                break;
            case "Rain":
                weatherConditionImage.setIcon(loadImage("src/weather_images/rain.png"));
                weatherConditionWallpaper.setIcon(loadScaledImage("src/weather_images/wallpapers/rainy_environment.jpg", layeredPane.getHeight()));
                break;
            case "Snow":
                weatherConditionImage.setIcon(loadImage("src/weather_images/snow.png"));
                weatherConditionWallpaper.setIcon(loadScaledImage("src/weather_images/wallpapers/snowy_environment.jpg", layeredPane.getHeight()));
                break;
            default:
                break;
        }
        
        double temperature = (double) weatherData.get("temperature");
        long humidity = (long) weatherData.get("humidity");
        double windspeed = (double) weatherData.get("windspeed");

        temperatureText.setText(new DecimalFormat("#.##").format(temperature) + " °C");
        weatherConditionDesc.setText(weatherCondition);
        humidityText.setText("<html><b>Humidity</b> " + humidity + "% </html>");
        windspeedText.setText("<html><b>Windspeed</b> " + windspeed + "Km/h </html>");
    }

    private String getUserLocation() {
        Locale locale = Locale.getDefault();
        String country = locale.getDisplayCountry();
        return country;
    }

    private ImageIcon loadScaledImage(String imagePath, int targetHeight) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage();
    
        // Calcula a largura proporcional com base na altura desejada
        int newWidth = (targetHeight * icon.getIconWidth()) / icon.getIconHeight(); 
    
        // Redimensiona a imagem mantendo a proporção
        Image scaledImage = image.getScaledInstance(newWidth, targetHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private ImageIcon loadScaledImageButton(String imagePath, int width, int targetHeight) {
        ImageIcon icon = new ImageIcon(imagePath);
        Image image = icon.getImage();
        int newWidth = width;
        int newHeight = (newWidth * icon.getIconHeight()) / icon.getIconWidth(); // Mantém a proporção
        Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }
}