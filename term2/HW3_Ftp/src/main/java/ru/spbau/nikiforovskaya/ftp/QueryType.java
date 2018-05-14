package ru.spbau.nikiforovskaya.ftp;

/**
 * Enum for query type.
 * Can be LIST or GET.
 * Also there are useful functions to use the enum.
 */
public enum QueryType {
    LIST, GET;

    /**
     * Transforms int into QueryType
     * @param x an int to transform
     * @return QueryType value, corresponding to the given int.
     */
    public static QueryType fromInt(int x) {
        switch (x) {
            case 1:
                return LIST;
            case 2:
                return GET;
        }
        return null;
    }

    /**
     * Transforms String into QueryType
     * @param x a String to transform
     * @return QueryType value, corresponding to the given String.
     */
    public static QueryType fromString(String x) {
        switch (x) {
            case "list":
                return LIST;
            case "get":
                return GET;
        }
        return null;
    }

    /**
     * Transforms QueryType into int.
     * @return int value, corresponding to this QueryType.
     */
    public int getInt() {
        switch (this) {
            case LIST:
                return 1;
            case GET:
                return 2;
        }
        return -1;
    }
}
