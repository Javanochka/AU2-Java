package ru.spbau.nikiforovskaya.logger;

import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Class, which helps to log game results into statistics table.
 * Only finished games are counted.
 * Statistics only for current session.
 */
public class Logger {

    private static ArrayList<LogInfo> allRecords = new ArrayList<>();

    /**
     * Adds record into statistics table
     * @param gameType a type of a game played
     * @param gameLevel a level, if played with bot
     * @param whoIsTheUser who was the user, if played with bot
     * @param gameResult game result. Who won or draw.
     */
    public static void addRecord(String gameType, String gameLevel,
                                 String whoIsTheUser, String gameResult) {
        allRecords.add(new LogInfo(gameType, gameLevel, whoIsTheUser, gameResult));
    }

    /**
     * Return collection of records in the statistics table.
     * @return collection of records in the statistics table.
     */
    public static Collection<LogInfo> getRecords() {
        return allRecords;
    }

    /** Class storing values for statistics table. */
    public static class LogInfo {

        private static final DateTimeFormatter DATE_FORMAT =
                DateTimeFormatter.ofPattern("MM/dd HH:mm");

        public final SimpleStringProperty date;
        public final SimpleStringProperty gameType;
        public final SimpleStringProperty gameLevel;
        public final SimpleStringProperty whoIsTheUser;
        public final SimpleStringProperty gameResult;

        /**
         * Creates a new info item.
         * @param gameType a type of a game played
         * @param gameLevel a level, if played with bot
         * @param whoIsTheUser who was the user, if played with bot
         * @param gameResult game result. Who won or draw.
         */
        public LogInfo(String gameType, String gameLevel, String whoIsTheUser, String gameResult) {
            date = new SimpleStringProperty(DATE_FORMAT.format(LocalDateTime.now()));
            this.gameType = new SimpleStringProperty(gameType);
            this.gameLevel = new SimpleStringProperty(gameLevel);
            this.whoIsTheUser = new SimpleStringProperty(whoIsTheUser);
            this.gameResult = new SimpleStringProperty(gameResult);
        }

        /**
         * Returns date of played game.
         * @return date of played game.
         */
        public String getDate() {
            return date.get();
        }

        /**
         * Returns type (single game or hot seat one) of played game.
         * @return type of played game.
         */
        public String getGameType() {
            return gameType.get();
        }

        /**
         * Returns level of played game.
         * "easy" for easy, "hard" for hard, "-" for hot seat game.
         * @return level of played game.
         */
        public String getGameLevel() {
            return gameLevel.get();
        }

        /**
         * Returns who user played for in the game. (x or o)
         * @return who user played for in the game.
         */
        public String getWhoIsTheUser() {
            return whoIsTheUser.get();
        }

        /**
         * Returns the result of the game.
         * @return the result of the game.
         */
        public String getGameResult() {
            return gameResult.get();
        }
    }
}
