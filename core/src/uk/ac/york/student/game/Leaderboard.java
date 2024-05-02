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

public class Leaderboard {
    static public class ScoreSubmission {
        String name;
        float score;
        public ScoreSubmission(String name, float score) {
            this.name = name;
            this.score = score;
        }
    }

    static HashMap<Integer, ScoreSubmission> leaderboard;
    public static void createLeaderboard() {
        try {
            File file = new File("Leaderboard.txt");
            System.out.println(file.getAbsolutePath());

            if (file.createNewFile()) {
                System.out.println("Leaderboard created: " + file.getName());
            } else {
                System.out.println("Leaderboard already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    public static void writeLeaderboard(String leaderboardText) {
        try {
            FileWriter myWriter = new FileWriter("leaderboard.txt");
            myWriter.write(leaderboardText);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
        }
    }

    public static void readLeaderboard() {
        leaderboard = new HashMap<>();

        try {
            File myObj = new File("Leaderboard.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] info = data.split(" ");
                try {
                    leaderboard.put(Integer.parseInt(info[0]), new ScoreSubmission(info[1], Float.parseFloat(info[2])));
                } catch(Exception e) {
                    System.out.println("Error occurred when reading from the leaderboard");
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
        }
    }

    public static void addScore(ScoreSubmission scoreSubmission) {
        for (int i = leaderboard.size(); i > 0; i--) {
            if (leaderboard.get(i).score > scoreSubmission.score) {
                leaderboard.put(i+1, new ScoreSubmission(scoreSubmission.name, scoreSubmission.score));
                break;
            }
            leaderboard.put(i+1, leaderboard.get(i));
            if (i == 1) leaderboard.put(i, new ScoreSubmission(scoreSubmission.name, scoreSubmission.score));
        }
        if (leaderboard.isEmpty()) {
            leaderboard.put(1, new ScoreSubmission(scoreSubmission.name, scoreSubmission.score));
        }
    }

    public static String leaderboardToText() {
        String leaderboardText = "";
        for (int i = 1; i <= Math.min(leaderboard.size(), 10); i++) {
            leaderboardText += i + " " + leaderboard.get(i).name + " " + leaderboard.get(i).score + "\n";
        }

        return leaderboardText;
    }


    public static String getLeaderboard() {
        createLeaderboard();
        readLeaderboard();
        return leaderboardToText();
    }

    public static void saveScore(String name, float score) {
        if (name.isEmpty()) {
            name = "Unknown";
        }
        createLeaderboard();
        readLeaderboard();
        addScore(new ScoreSubmission(name, score));
        String newLeaderboard = leaderboardToText();
        writeLeaderboard(newLeaderboard);
    }

    public static void main(String[] args) {
    }
}

