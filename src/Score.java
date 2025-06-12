package src;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Score {
    private int score;
    private static final String HIGH_SCORE_FILE = "highscore.dat";

    // Constructor
    public Score() {
        this.score = 0;
    }

    // Increase score
    public void increaseScore() {
        this.score++;

    }

    // Reset score
    public void resetScore() {
        this.score = 0;

    }

    // Return the current score
    public int getScore() {
        return this.score;

    }

    // Get high scores
    public List<Integer> getHighScores() {
        List<Integer> scores = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(HIGH_SCORE_FILE))) {
            scores = (List<Integer>) ois.readObject();
        } catch (FileNotFoundException e) {
            // If the file doesn't exist, return an empty list
        } catch (IOException | ClassNotFoundException e) {

            e.printStackTrace();

        }
        return scores;

    }

    // Save new score
    public void saveNewScore() {
        List<Integer> scores = getHighScores();
        scores.add(this.getScore());
        sortAndSaveScores(scores);

    }

    // Sort and save high scores
    private void sortAndSaveScores(List<Integer> scores) {

        int n = scores.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - 1 - i; j++) {
                if (scores.get(j) < scores.get(j + 1)) {
                    // Swap scores[j] and scores[j + 1]
                    int temp = scores.get(j);
                    scores.set(j, scores.get(j + 1));
                    scores.set(j + 1, temp);
                }
            }
        }

        // Limit to top 10 scores
        if (scores.size() > 10) {
            scores = new ArrayList<>(scores.subList(0,10));
        }

        // Save scores to file
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(HIGH_SCORE_FILE))) {
            oos.writeObject(scores);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}