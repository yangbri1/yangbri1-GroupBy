
import Util.ConnectionUtil;
import Util.FileUtil;
import org.junit.*;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Lab3Test {
    private static Connection conn;

    @Test
    public void problem1Test() {
        String sql = FileUtil.parseSQLFile("src/main/lab3.sql");
        Map<String, Long> counts = new HashMap<>();
        try {
            Connection connection = ConnectionUtil.getConnection();
            Statement s = connection.createStatement();
            ResultSet rs =s.executeQuery(sql);

            while(rs.next()) {
                Object o1 = rs.getObject(1);
                Object o2 = rs.getObject(2);
                String artist;
                long val;
                if(o1.getClass().equals(String.class)){
                    artist = (String) o1;
                    val = (Integer) o2;
                }else{
                    artist = (String) o2;
                    val = (Integer) o1;
                }
                counts.put(artist, val);
            }
        } catch (SQLException e) {
            Assert.fail(e.getMessage());
        }
        Assert.assertNull("Records not fulfilling the HAVING requirement should be excluded. ",
                counts.get("Peter Gabriel"));
        long actual2 = counts.get("Phil Collins");
        long expected2 = 299;
        Assert.assertEquals("The max should match the expected value.",
                actual2, expected2);
        long actual3 = counts.get("Kate Bush");
        long expected3 = 306;
        Assert.assertEquals("The max should match the expected value.",
                actual3, expected3);

    }


    @BeforeClass
    public static void getConnection() {
        conn = ConnectionUtil.getConnection();
    }


    @Before
    public void setup() {


        try {

            String createTable = "CREATE TABLE song (" +
                    "id SERIAL PRIMARY KEY," +
                    "artist VARCHAR(255)," +
                    "song VARCHAR(255)," +
                    "runtime integer"+
                    ");";
            PreparedStatement createTableStatement = conn.prepareStatement(createTable);
            createTableStatement.executeUpdate();

            String insertData = "INSERT INTO song (artist, song, runtime) VALUES" +
                    "('Peter Gabriel', 'Solsbury Hill', 261)," +
                    "('Peter Gabriel', 'In your eyes', 323)," +
                    "('Phil Collins', 'In the air tonight', 299)," +
                    "('Kate Bush', 'Wuthering Heights', 244)," +
                    "('Kate Bush', 'Babooshka', 206)," +
                    "('Kate Bush', 'Cloudbusting', 306);";
            PreparedStatement insertDataStatement = conn.prepareStatement(insertData);
            insertDataStatement.executeUpdate();

        } catch(SQLException e) {
            Assert.fail(e.getMessage());
        }
    }

    @After
    public void cleanup() {
        try {
            conn = ConnectionUtil.getConnection();

            String dropTable = "DROP TABLE IF EXISTS song";
            PreparedStatement createTableStatement = conn.prepareStatement(dropTable);
            createTableStatement.executeUpdate();

        } catch(SQLException e) {
        }
    }

    @AfterClass
    public static void closeConnection() {
        try {
            conn.close();
        } catch(SQLException e) {
        }

    }
}
