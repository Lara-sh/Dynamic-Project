package application;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.util.Duration;
public class Game extends Application {

	ArrayC AC = new ArrayC();
	BorderPane borderGame = new BorderPane();
	private Scene scene1;
	Button startBTK = new Button("Start");
	VBox player1CoinsBox = new VBox(10);
	VBox player2CoinsBox = new VBox(10);
	HBox coinCircles = new HBox(10);
	private int player1Total = 0;
	private int player2Total = 0;
	private String player1Name = "Player 1"; 
	private String player2Name = "Player 2";
	
	public void start(Stage primaryStage) {
		// Setup the main menu scene with layout and controls
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(20));
		grid.setVgap(10);
		grid.setHgap(10);
		grid.setAlignment(Pos.CENTER);
		grid.setStyle("-fx-background-color: lightgray;");

		// Image setup
		Image image = new Image("file:///C:/Users/ASUS/Desktop/Algo/Project/src/ProPhoto.png");
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(100);
		imageView.setPreserveRatio(true);
		grid.add(imageView, 1, 0);

		// Labels and input fields
		Label welcomeL = new Label("Welcome to the Optimal Game Strategy");
		welcomeL.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
		grid.add(welcomeL, 0, 1, 3, 1);

		Label chooseL = new Label("Please Choose the way you want to play:");
		grid.add(chooseL, 0, 2, 3, 1);

		Button bt1 = new Button("Manual");
		Button bt2 = new Button("Random");
		Button bt3 = new Button("Read from file");

		grid.add(bt1, 0, 3);
		grid.add(bt2, 1, 3);
		grid.add(bt3, 2, 3);

		Label p1Label = new Label("Player 1 Name:");
		TextField p1Text = new TextField();
		p1Text.setPromptText("Player 1");

		Label p2Label = new Label("Player 2 Name:");
		TextField p2Text = new TextField();
		p2Text.setPromptText("Player 2");

		grid.add(p1Label, 0, 4);
		grid.add(p1Text, 1, 4);
		grid.add(p2Label, 0, 5);
		grid.add(p2Text, 1, 5);

		Label resultLabel = new Label();
		grid.add(resultLabel, 0, 7, 3, 1);

		Button playbtk = new Button("Let's go to play");
		playbtk.setStyle("-fx-font-size: 14px; -fx-pref-width: 120px; -fx-pref-height: 40px;");
		grid.add(playbtk, 0, 6, 3, 1);

		// Manual button action
		bt1.setOnAction(event -> {
			TextInputDialog num = new TextInputDialog();
			num.setTitle("Manual Entry");
			num.setContentText("Enter the number of coins (must be even):");

			num.showAndWait().ifPresent(numC -> {
				try {
					int Coins = Integer.parseInt(numC.trim());
					if (Coins <= 0 || Coins % 2 != 0) {
						resultLabel.setText("Please enter an even number of coins.");
						return;
					}

					TextInputDialog dd = new TextInputDialog();
					dd.setTitle("Manual Entry");
					dd.setContentText("Enter the values of " + Coins + " coins separated by spaces:");

					dd.showAndWait().ifPresent(input -> {
						try {
							String[] Con = input.trim().split("\\s+");
							if (Con.length != Coins) {
								resultLabel.setText("Please enter exactly " + Coins + " coin values.");
								return;
							}

							// Set coins in ArrayC instance
							int[] CA = new int[Coins];
							for (int i = 0; i < Coins; i++) {
								CA[i] = Integer.parseInt(Con[i]);
							}
							AC.setCoins(CA);

							resultLabel.setText("Coins entered: " + Arrays.toString(AC.getCoins()));
						} catch (NumberFormatException e) {
							resultLabel.setText("Please enter valid integers for coins.");
						}
					});

				} catch (NumberFormatException e) {
					resultLabel.setText("Please enter a valid integer for the number of coins.");
				}
			});
		});

		// Random button action
		bt2.setOnAction(event -> {
		    // First dialog to get the number of coins
		    TextInputDialog dd1 = new TextInputDialog();
		    dd1.setTitle("Random Coin Generation");
		    dd1.setContentText("Enter the number of coins (must be even):");

		    dd1.showAndWait().ifPresent(input -> {
		        try {
		            int numCoins = Integer.parseInt(input.trim());
		            if (numCoins <= 0 || numCoins % 2 != 0) {
		                resultLabel.setText("Please enter an even number of coins.");
		                return;
		            }

		            // Second dialog to get the range for the coin values
		            TextInputDialog dd2 = new TextInputDialog();
		            dd2.setTitle("Coin Number Range");
		            dd2.setContentText("Enter the range for the coin numbers (e.g., 1 to 50):");

		            dd2.showAndWait().ifPresent(rangeInput -> {
		                try {
		                    // Parse the range input
		                    String[] range = rangeInput.trim().split("\\s+to\\s+");
		                    if (range.length != 2) {
		                        resultLabel.setText("Invalid range format. Please use 'X to Y'.");
		                        return;
		                    }

		                    int minValue = Integer.parseInt(range[0].trim());
		                    int maxValue = Integer.parseInt(range[1].trim());

		                    if (minValue >= maxValue) {
		                        resultLabel.setText("Invalid range. Minimum value must be less than maximum.");
		                        return;
		                    }

		                    // Generate random coins within the given range
		                    Random random = new Random();
		                    int[] coinsArray = new int[numCoins];
		                    for (int i = 0; i < numCoins; i++) {
		                        coinsArray[i] = minValue + random.nextInt(maxValue - minValue + 1);
		                    }

		                    // Update the ArrayC with the generated coins
		                    AC.setCoins(coinsArray);
		                    resultLabel.setText("Random coins: " + Arrays.toString(AC.getCoins()));
		                } catch (NumberFormatException e) {
		                    resultLabel.setText("Please enter a valid number for the range.");
		                }
		            });

		        } catch (NumberFormatException e) {
		            resultLabel.setText("Please enter a valid integer for the number of coins.");
		        }
		    });
		});


		// File reading button action
		bt3.setOnAction(event -> {
		    FileChooser fileChooser = new FileChooser();
		    fileChooser.setTitle("Read From File");
		    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

		    File file = fileChooser.showOpenDialog(primaryStage);
		    if (file != null) {
		        try {
		            // Read all lines from the file
		            List<String> lines = Files.readAllLines(file.toPath());
		            
		            // First line: the number of coins (length)
		            int length = Integer.parseInt(lines.get(0).trim());

		            // Validate that the length is even
		            if (length % 2 != 0) {
		                resultLabel.setText("The number of coins must be an even number.");
		                return;
		            }

		            // Second line: the actual coins
		            String[] coinStrings = lines.get(1).split(" ");
		            int[] coinsFromFile = new int[coinStrings.length];

		            // Parse the coins into an integer array
		            for (int i = 0; i < coinStrings.length; i++) {
		                coinsFromFile[i] = Integer.parseInt(coinStrings[i].trim());
		            }

		            // Validate the number of coins
		            if (coinsFromFile.length == length) {
		                // Successfully loaded the correct number of coins
		                resultLabel.setText("File loaded successfully with coins: " + Arrays.toString(coinsFromFile));
		                AC.setCoins(coinsFromFile);  // Update the ArrayC instance with the coins
		            } else if (coinsFromFile.length > length) {
		            	 // If there are more coins than expected, take only the first 'length' coins
		                coinsFromFile = Arrays.copyOfRange(coinsFromFile, 0, length);
		                resultLabel.setText("File contains more coins than expected" + Arrays.toString(coinsFromFile));
		                AC.setCoins(coinsFromFile);  // Update the ArrayC instance with the coins
		            } else {		               
		                resultLabel.setText("Did not match");
		            }

		        } catch (IOException e) {
		            resultLabel.setText("Error reading the file.");
		        } catch (NumberFormatException e) {
		            resultLabel.setText("Error: Invalid number format in the file.");
		        } catch (IndexOutOfBoundsException e) {
		            resultLabel.setText("Error: File format is incorrect.");
		        }
		    }
		});




		// Action for "Let's go to play" button
		playbtk.setOnAction(event -> {
			player1Name = p1Text.getText().trim();
			player2Name = p2Text.getText().trim();

			// Ensure names are entered
			if (player1Name.isEmpty() || player2Name.isEmpty()) {
				resultLabel.setText("Please enter names for both players.");
				return;
			}

			// Ensure coin values are set
			if (AC.getCoins() == null || AC.length() == 0) {
				resultLabel.setText("Please select a coin configuration first.");
				return;
			}

			// Open the game play scene
			openGameScene(primaryStage);
		});
		// Setting and showing the primary scene
		scene1 = new Scene(grid, 600, 400);
		primaryStage.setScene(scene1);
		primaryStage.setTitle("Optimal Game Strategy");
		primaryStage.show();
	}

	private void openGameScene(Stage primStage) {

		borderGame.setPadding(new Insets(20));
		borderGame.setStyle("-fx-background-color: lightgray;");

		Label welcomeL = new Label("Let's Play ❤");
		welcomeL.setStyle(
				"-fx-font-weight: bold; -fx-font-size: 18px; -fx-font-family: 'Algerian'; -fx-text-fill: black;");

		// Main coin display area
		
		coinCircles.setAlignment(Pos.CENTER);

		// Player-specific coin boxes

		player1CoinsBox.setAlignment(Pos.CENTER_LEFT);

		player2CoinsBox.setAlignment(Pos.CENTER_RIGHT);

		Image images = new Image("file:/C:/Users/ASUS/Desktop/Algo/Project/src/VSBK.png");
		ImageView imageViews = new ImageView(images);
		imageViews.setFitWidth(300);
		imageViews.setPreserveRatio(true);
		borderGame.setCenter(imageViews);
		
		// Click counter for alternating coin placements
		final int[] clickCounter = { 0 };

		// Toggle group for game mode
		ToggleGroup Tgroup = new ToggleGroup();
		RadioButton radio1 = new RadioButton("Computer Play");
		RadioButton radio2 = new RadioButton("Players Play");

		radio1.setToggleGroup(Tgroup);
		radio2.setToggleGroup(Tgroup);
		radio1.setSelected(true);
		radio1.setStyle("-fx-font-size: 14px;");
		radio2.setStyle("-fx-font-size: 14px;");

		// Add coins to the main coinCircles HBox
		for (int coin : AC.getCoins()) {
		    Label coinLabel = new Label(String.valueOf(coin));
		    coinLabel.setStyle("-fx-font-size: 14px; " + "-fx-border-color: black; " + "-fx-background-color: yellow; "
		        + "-fx-text-fill: black; " + "-fx-border-radius: 25px; " + "-fx-background-radius: 25px; "
		        + "-fx-min-width: 50px; " + "-fx-min-height: 50px; " + "-fx-alignment: center;");

		    // Add click event to each coin
		    coinLabel.setOnMouseClicked(event -> {
		    	//players play
		        if (radio2.isSelected()) { // Only if "Players Play" mode is selected
		            // Check if the clicked coin is at the beginning or the end
		        	int index = coinCircles.getChildren().indexOf(coinLabel);
		        	if (index == 0 || index == coinCircles.getChildren().size() - 1) {  // Valid selection (first or last coin)
		        	    // Get the coin value (parse the coin label text)
		        	    int coinValue = Integer.parseInt(coinLabel.getText());

		        	    // Assign coin based on alternating clickCounter value
		        	    if (clickCounter[0] % 2 == 0) {  // Player 1's turn
		        	        player1CoinsBox.getChildren().add(coinLabel);  // Add the selected coin to Player 1's box
		        	        player1Total += coinValue;  // Update Player 1's total score
		        	    } else {  // Player 2's turn
		        	        player2CoinsBox.getChildren().add(coinLabel);  // Add the selected coin to Player 2's box
		        	        player2Total += coinValue;  // Update Player 2's total score
		        	    }

		        	    coinCircles.getChildren().remove(coinLabel);  // Remove the selected coin from the available coins
		        	    clickCounter[0]++;  // Increment counter to alternate turns

		        	    // Check if all coins are distributed
		        	    if (coinCircles.getChildren().isEmpty()) {
		        	        // Determine the winner
		        	        String winnerMessage;
		        	        if (player1Total > player2Total) {
		        	            winnerMessage = player1Name + " wins with " + player1Total + " points! 🎉🎊";
		        	        } else if (player2Total > player1Total) {
		        	            winnerMessage = player2Name + " wins with " + player2Total + " points! 🎉🎊";
		        	        } else {
		        	            winnerMessage = "It's a tie! Both players have " + player1Total + " points.";
		        	        }

		        	        // Display the result directly in the UI
		        	        Label player1ScoreLabel = new Label(player1Name + ": " + player1Total + " points");
		        	        player1ScoreLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: navy;");

		        	        Label player2ScoreLabel = new Label(player2Name + ": " + player2Total + " points");
		        	        player2ScoreLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: darkred;");

		        	        Label winnerLabel = new Label(winnerMessage);
		        	        winnerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: green;");

		        	        VBox resultBox = new VBox(10, player1ScoreLabel, player2ScoreLabel, winnerLabel);
		        	        resultBox.setAlignment(Pos.CENTER);
		        	        borderGame.setCenter(resultBox);  // Display result in the center pane
		        	    }
		        	}
		        }
		    });
		


			coinCircles.getChildren().add(coinLabel);
		}
		//computer play
		if (radio1.isSelected()) {
		    startBTK.setOnAction(ev -> {
		    	// Retrieve the coins array and calculate the DP table for optimal strategy
		    	int[] coins = AC.getCoins();
		    	int[][] dp = DPTable(coins);

		    	// Track the indexes for each player move
		    	int i = 0, j = coins.length - 1;
		    	int[] player1Total = {0};
		    	int[] player2Total = {0};
		    	boolean[] isPlayer1Turn = {true};

		    	// Sequential transition for animating each move
		    	SequentialTransition sequentialTransition = new SequentialTransition();

		    	// Container for coin labels (initial view)
		    	HBox coinCircles = new HBox(10);
		    	for (int coin : coins) {
		    	    Label coinLabel = new Label(String.valueOf(coin));
		    	    coinLabel.setStyle("-fx-font-size: 18px; -fx-border-color: black; -fx-background-color: yellow;"
		    	                     + "-fx-text-fill: black; -fx-border-radius: 25px; -fx-background-radius: 25px;"
		    	                     + "-fx-min-width: 50px; -fx-min-height: 50px; -fx-alignment: center;");
		    	    coinCircles.getChildren().add(coinLabel);
		    	}
		    	coinCircles.setAlignment(Pos.CENTER);
		    	// Loop for selecting coins based on the optimal strategy
		    	while (i <= j) {
		    	    final int currentI = i;  // Store final values for use inside lambda
		    	    final int currentJ = j;

		    	    // Determine the coin to select
		    	    final int coinValue;
		    	    final int coinIndex;  // Track the index of the coin to be updated visually

		    	    if (coins[i] + (i + 1 <= j - 1 ? Math.min(dp[i + 2][j], dp[i + 1][j - 1]) : 0) >=
		    	        coins[j] + (i <= j - 2 ? Math.min(dp[i][j - 2], dp[i + 1][j - 1]) : 0)) {
		    	        coinValue = coins[i];
		    	        coinIndex = currentI;
		    	        i++;
		    	    } else {
		    	        coinValue = coins[j];
		    	        coinIndex = currentJ;
		    	        j--;
		    	    }

		    	    // Pause for each move
		    	    PauseTransition pause = new PauseTransition(Duration.seconds(1));
		    	    pause.setOnFinished(e -> {
		    	        // Update the coin with an "X" in the coinCircles
		    	        Label selectedCoin = (Label) coinCircles.getChildren().get(coinIndex);
		    	       
		    	        selectedCoin.setText("X");
		    	        selectedCoin.setStyle("-fx-font-size: 18px; -fx-border-color: black; -fx-background-color: gray;"
		    	            + "-fx-text-fill: red; -fx-border-radius: 25px; -fx-background-radius: 25px;"
		    	            + "-fx-min-width: 50px; -fx-min-height: 50px; -fx-alignment: center;");
		    	        coinCircles.setAlignment(Pos.CENTER);
		    	       
		    	        // Create and style the coin label for the player
		    	        Label coinLabel = new Label(String.valueOf(coinValue));
		    	        coinLabel.setStyle("-fx-font-size: 14px; -fx-border-color: black; -fx-background-color: yellow;"
		    	                         + "-fx-text-fill: black; -fx-border-radius: 25px; -fx-background-radius: 25px;"
		    	                         + "-fx-min-width: 50px; -fx-min-height: 50px; -fx-alignment: center;");

		    	        // Add the coin to the correct player's box
		    	        if (isPlayer1Turn[0]) {
		    	            player1CoinsBox.getChildren().add(coinLabel);
		    	            player1Total[0] += coinValue;
		    	        } else {
		    	            player2CoinsBox.getChildren().add(coinLabel);
		    	            player2Total[0] += coinValue;
		    	        }
		    	        isPlayer1Turn[0] = !isPlayer1Turn[0];  // Alternate turns
		    	    });

		    	    sequentialTransition.getChildren().add(pause);
		    	}

		    	// Display results after all coins are chosen
		    	sequentialTransition.setOnFinished(e -> {
		    	    // Determine the winner message
		    	    String winnerMessage;
		    	    if (player1Total[0] > player2Total[0]) {
		    	        winnerMessage = player1Name + " wins with " + player1Total[0] + " points! 🎉🎊🎉🎊";
		    	    } else if (player2Total[0] > player1Total[0]) {
		    	        winnerMessage = player2Name + " wins with " + player2Total[0] + " points! 🎉🎊🎉🎊";
		    	    } else {
		    	        winnerMessage = "It's a tie! Both players have " + player1Total[0] + " points.";
		    	    }

		    	    // Update UI with scores and winner message
		    	    Label player1ScoreLabel = new Label(player1Name + ": " + player1Total[0] + " points");
		    	    player1ScoreLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: navy;");

		    	    Label player2ScoreLabel = new Label(player2Name + ": " + player2Total[0] + " points");
		    	    player2ScoreLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: darkred;");

		    	    Label winnerLabel = new Label(winnerMessage);
		    	    winnerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: green;");

		    	    VBox resultBox = new VBox(10, player1ScoreLabel, player2ScoreLabel, winnerLabel);
		    	    resultBox.setAlignment(Pos.CENTER);
		    	    borderGame.setCenter(resultBox);  // Display the result in the center of the BorderPane
		    	});

		    	// Add the initial coinCircles view to the scene (if not already added)
		    	borderGame.setTop(coinCircles);

		    	// Start the animation sequence
		    	sequentialTransition.play();

		    });


		
		// Top section layout
		VBox vb = new VBox(20, welcomeL, coinCircles);
		vb.setAlignment(Pos.CENTER);
		borderGame.setTop(vb);

		// Bottom section with Start, Restart, and Print DP buttons
		HBox VBottom = new HBox(20);
		VBottom.setAlignment(Pos.CENTER);

	
		
		Button restartBTK = new Button("Restart");
		/*restartBTK.setOnAction(event -> {
			//primStage.setScene(scene1);
		});*/
		restartBTK.setOnAction(event -> {
		    // Reset the player coin boxes
		    player1CoinsBox.getChildren().clear();
		    player2CoinsBox.getChildren().clear();

		    // Reset the click counter
		    clickCounter[0] = 0;

		    // Recreate the coinCircles HBox with the initial coins
		    coinCircles.getChildren().clear();
		    for (int coin : AC.getCoins()) {
		        Label coinLabel = new Label(String.valueOf(coin));
		        coinLabel.setStyle("-fx-font-size: 14px; " + "-fx-border-color: black; " + "-fx-background-color: yellow; "
		                + "-fx-text-fill: black; " + "-fx-border-radius: 25px; " + "-fx-background-radius: 25px; "
		                + "-fx-min-width: 50px; " + "-fx-min-height: 50px; " + "-fx-alignment: center;");

		        // Add click event to each coin
		        coinLabel.setOnMouseClicked(innerEvent -> {
		            if (radio2.isSelected()) { // Only if "Players Play" is selected
		                int index = coinCircles.getChildren().indexOf(coinLabel);
		                if (index == 0 || index == coinCircles.getChildren().size() - 1) {  // Valid selection
		                    int coinValue = Integer.parseInt(coinLabel.getText());  // Extract coin value
		                    Label newCoinLabel = new Label(coinLabel.getText());  // New label for the player box
		                    newCoinLabel.setStyle("-fx-font-size: 14px; " + "-fx-border-color: black; " + "-fx-background-color: yellow; "
		                            + "-fx-text-fill: black; " + "-fx-border-radius: 25px; " + "-fx-background-radius: 25px; "
		                            + "-fx-min-width: 50px; " + "-fx-min-height: 50px; " + "-fx-alignment: center;");

		                    if (clickCounter[0] % 2 == 0) {  // Player 1's turn
		                        player1CoinsBox.getChildren().add(newCoinLabel);
		                        player1Total += coinValue;  // Update Player 1's total
		                    } else {  // Player 2's turn
		                        player2CoinsBox.getChildren().add(newCoinLabel);
		                        player2Total += coinValue;  // Update Player 2's total
		                    }

		                    coinCircles.getChildren().remove(coinLabel);  // Remove the selected coin
		                    clickCounter[0]++;  // Increment counter

		                    // Check if all coins are distributed
		                    if (coinCircles.getChildren().isEmpty()) {
		                        // Determine the winner
		                        String winnerMessage;
		                        if (player1Total > player2Total) {
		                            winnerMessage = player1Name + " wins with " + player1Total + " points! 🎉🎊";
		                        } else if (player2Total > player1Total) {
		                            winnerMessage = player2Name + " wins with " + player2Total + " points! 🎉🎊";
		                        } else {
		                            winnerMessage = "It's a tie! Both players have " + player1Total + " points.";
		                        }

		                        // Display the result directly in the UI
		                        Label player1ScoreLabel = new Label(player1Name + ": " + player1Total + " points");
		                        player1ScoreLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: navy;");

		                        Label player2ScoreLabel = new Label(player2Name + ": " + player2Total + " points");
		                        player2ScoreLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: darkred;");

		                        Label winnerLabel = new Label(winnerMessage);
		                        winnerLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: green;");

		                        VBox resultBox = new VBox(10, player1ScoreLabel, player2ScoreLabel, winnerLabel);
		                        resultBox.setAlignment(Pos.CENTER);
		                        borderGame.setCenter(resultBox);  // Display result in the center pane
		                    }
		                }
		            }
		        });

		        coinCircles.getChildren().add(coinLabel);
		    }

		    // Reset the radio buttons and selection (if necessary)
		    radio1.setSelected(true); // or radio2.setSelected(true) based on default selection

		    // Reset the game UI elements or any other variables as needed
		    // Optionally you can also clear the results and scores if they are shown

		    // Clear the result (winner, scores, etc.)
		    borderGame.setCenter(null);  // Remove result view (winner, score labels)
		    Image imag = new Image("file:/C:/Users/ASUS/Desktop/Algo/Project/src/VSBK.png");
			ImageView imageVi = new ImageView(imag);
			imageVi.setFitWidth(300);
			imageVi.setPreserveRatio(true);
			borderGame.setCenter(imageVi);
		    // Move coins display to the top (coinCircles)
		    VBox vb3 = new VBox(20, welcomeL, coinCircles);  // Move coinCircles here
		    vb.setAlignment(Pos.CENTER);
		    borderGame.setTop(vb3);  // Place coins at the top of the BorderPane
		});



		GridPane grid = new GridPane();
		grid.setVgap(10);
		grid.setHgap(10);

		Button DPButton = new Button("Print DP table");
		DPButton.setOnAction(event -> {
			int[] coins = AC.getCoins();
			int[][] dp = DPTable(coins); // Get the DP table

			// Create a TextArea to display the DP table in the new scene
			StringBuilder dpTableString = new StringBuilder("DP Table:\n");
			for (int[] row : dp) {
				for (int value : row) {
					dpTableString.append(value).append("\t");
				}
				dpTableString.append("\n");
			}

			// Create a new scene with a TextArea to display the DP table
			TextArea dpTextArea = new TextArea(dpTableString.toString());
			dpTextArea.setEditable(false); // Make it non-editable
			dpTextArea.setWrapText(true);
			dpTextArea.setPrefSize(200, 100); // Set the size of the TextArea

			// Create a new scene to display the DP table
			StackPane dpPane = new StackPane();
			dpPane.getChildren().add(dpTextArea);
			Scene dpScene = new Scene(dpPane, 300, 200);

			// Set the new scene for the new window (or Stage)
			Stage dpStage = new Stage();
			dpStage.setTitle("DP Table");
			dpStage.setScene(dpScene);
			dpStage.show();

		});

		VBottom.getChildren().addAll(startBTK, restartBTK, DPButton);
		borderGame.setBottom(VBottom);

		// Left section with radio buttons, Player 1 label, and Player 1 coin box
		Label player1Label = new Label(player1Name);
		player1Label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: navy;");

		VBox radioBox = new VBox(30);
		radioBox.setAlignment(Pos.TOP_LEFT);
		radioBox.getChildren().addAll(radio1, radio2, player1Label, player1CoinsBox);

		borderGame.setLeft(radioBox);

		// Right section with Player 2 image and coin box
		VBox VRight = new VBox(20);
		Image image = new Image("file:/C:/Users/ASUS/Desktop/Algo/Project/src/card.png");
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(120);
		imageView.setPreserveRatio(true);


		Label player2Label = new Label(player2Name);
		player2Label.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: darkred;");

		VRight.setAlignment(Pos.TOP_RIGHT);
		VRight.getChildren().addAll(imageView, player2Label, player2CoinsBox);
		borderGame.setRight(VRight);
		
		
		
        Scene gameScene = new Scene(borderGame, 800, 600);
        primStage.setScene(gameScene);
        primStage.setTitle("Game");
        primStage.show();


	}
	}
	
	
	// Method to fill the DP table and return it
	public static int[][] DPTable(int[] coins) {
		int n = coins.length;
		int[][] dp = new int[n][n];

		// Fill the DP table using a bottom-up approach
		for (int k = 0; k < n; k++) {
			for (int i = 0, j = k; j < n; i++, j++) {
				int x = (i + 2 <= j) ? dp[i + 2][j] : 0;
				int y = (i + 1 <= j - 1) ? dp[i + 1][j - 1] : 0;
				int z = (i <= j - 2) ? dp[i][j - 2] : 0;
				dp[i][j] = Math.max(coins[i] + Math.min(x, y), coins[j] + Math.min(y, z));
			}
		}

		// Prepare the DP table as a string to display in the new scene
		StringBuilder dpTableString = new StringBuilder("DP Table:\n");
		for (int[] row : dp) {
			for (int value : row) {
				dpTableString.append(value).append("\t");
			}
			dpTableString.append("\n");
		}

		// Return the DP table
		return dp;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
