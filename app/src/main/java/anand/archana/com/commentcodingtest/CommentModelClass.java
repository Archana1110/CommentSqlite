package anand.archana.com.commentcodingtest;

public class CommentModelClass {
    public static final String TABLE_NAME = "Comments";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    private int id;
    private String comment;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_COMMENT+ " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public CommentModelClass() {
    }

    public CommentModelClass(int id, String comment, String timestamp) {
        this.id = id;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }


    public static String getTableName() {
        return TABLE_NAME;
    }

    public static String getColumnId() {
        return COLUMN_ID;
    }

    public static String getColumnComment() {
        return COLUMN_COMMENT;
    }

    public static String getColumnTimestamp() {
        return COLUMN_TIMESTAMP;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public static String getCreateTable() {
        return CREATE_TABLE;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
