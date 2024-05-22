package uk.ac.york.student.game;

import com.badlogic.gdx.scenes.scene2d.ui.Tree;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Scanner;
import java.io.FileNotFoundException;

//Compare current score to top 10 previous scores
//If better ask for name and save

//File setup
//position:name:score

/**
 * The Leaderboard class add the functionality to save, load, and update the leaderboard
 * It includes methods to create, write, read, and save the leaderboard.
 * Also methods to add a score, and convert the leaderboard to text to be used.
 */
public class Leaderboard {
    /**
     * The filePath and fileName of the leaderboard.
     * Ensures the code knows where to save and find the leaderboard
     */
    final static String filePath = "leaderboard.txt";
    /**
     * Boolean value to keep track of whether the player has saved the current score to the leaderboard
     * Ensures the player can't save the same score twice {@link uk.ac.york.student.screens.EndScreen}
     */
    public static boolean scoreSaved = false;

    /**
     * The ScoreSubmission class encapsulates all information related to submitting a score to the leaderboard
     */
    static public class ScoreSubmission {
        String name;
        float score;
        public ScoreSubmission(String name, float score) {
            this.name = name;
            this.score = score;
        }
    }

    /**
     * Hashmap to contain each leaderboard entry and their current position on the leaderboard
     */
    static HashMap<Integer, ScoreSubmission> leaderboard;

    /**
     * Creates a leaderboard text file at the specified filepath.
     * If a file already exits it does nothing.
     *
     * Catches IOException if an error occurs in the creation of the leaderboard
     * and outputs a debug message
     */
    public static void createLeaderboard() {
        try {
            File file = new File(filePath);
            System.out.println(file.getAbsolutePath());

            if (file.createNewFile()) {
                System.out.println("Leaderboard created: " + file.getName());
            } else {
                System.out.println("Leaderboard already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred when creating the leaderboard.");
        }
    }

    /**
     * Writes a string to the leaderboard
     *
     * @param leaderboardText The current leaderboard in text form to be sent to the file
     * Catches IOException if an error occurs and outputs a debug message
     */
    public static void writeLeaderboard(String leaderboardText) {
        try {
            FileWriter myWriter = new FileWriter(filePath);
            myWriter.write(leaderboardText);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred when writing to the file.");
        }
    }

    /**
     * Reads the leaderboard and adds each score to the leaderboard hashmap
     *
     * Catches FileNotFoundException if no file is found and outputs a debug message
     */
    public static void readLeaderboard() {
        //Creates a new hashmap to add to
        leaderboard = new HashMap<>();

        try {
            File myObj = new File(filePath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] info = data.split(" ");
                try {
                    //Creates a new score submission with the text and adds it with a key being the leaderboard position
                    leaderboard.put(Integer.parseInt(info[0]), new ScoreSubmission(info[1], Float.parseFloat(info[2])));
                } catch(Exception e) {
                    System.out.println("Error occurred when reading from the leaderboard");
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("The file could not be found.");
        }
    }

    /**
     * Adds a score to the leaderboard and updates the value of every key so that the positions on the leaderboard are correct
     *
     * @param scoreSubmission
     */
    public static void addScore(ScoreSubmission scoreSubmission) {
        //Iterates over every score from lowest to highest and moves them down if the new score is greater
        for (int i = leaderboard.size(); i > 0; i--) {
            if (leaderboard.get(i).score > scoreSubmission.score) {
                leaderboard.put(i+1, new ScoreSubmission(scoreSubmission.name, scoreSubmission.score));
                break;
            }
            leaderboard.put(i+1, leaderboard.get(i));
            //If the score is the highest score it is put in the first position
            if (i == 1) leaderboard.put(i, new ScoreSubmission(scoreSubmission.name, scoreSubmission.score));
        }
        //Adds the score anyway if the leaderboard is empty
        if (leaderboard.isEmpty()) {
            leaderboard.put(1, new ScoreSubmission(scoreSubmission.name, scoreSubmission.score));
        }
    }

    /**
     * Converts the leaderboard hashmap to text and returns it
     *
     * @return the leaderboard in text format
     */
    public static String leaderboardToText() {
        String leaderboardText = "";
        for (int i = 1; i <= Math.min(leaderboard.size(), 10); i++) {
            //Adds the positon, name, and score seperated by a whitespace
            leaderboardText += i + " " + leaderboard.get(i).name + " " + leaderboard.get(i).score + "\n";
        }

        return leaderboardText;
    }

    /**
     * Encapsulates the methods required to get a text version of the leaderboard
     *
     * @return the leaderboard in text format
     */
    public static String getLeaderboard() {
        createLeaderboard();
        readLeaderboard();
        return leaderboardToText();
    }

    /**
     * Encapsulated the methods required to save a score to the leaderboard file
     *
     * @param name Name of the player saving the score
     * @param score Score they achieved
     */
    public static void saveScore(String name, float score) {

        //Remove spaces which the leaderboard system uses to separate the position, name, and score
        name = name.replaceAll("\\s", "");
        //Ensures an empty name doesn't get sent to the file
        if (name.isEmpty()) {
            name = "Unknown";
        }
        createLeaderboard();
        readLeaderboard();
        addScore(new ScoreSubmission(name, score));
        String newLeaderboard = leaderboardToText();
        writeLeaderboard(newLeaderboard);
    }
}

