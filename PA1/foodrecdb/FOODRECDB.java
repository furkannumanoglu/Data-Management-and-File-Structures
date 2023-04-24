package ceng.ceng351.foodrecdb;
import java.sql.*;
import java.util.ArrayList;

public class FOODRECDB implements IFOODRECDB{

    private static String user = "e2448710";
    private static String password = "hklVcp-2QiBTHAsN";
    private static String database = "db2448710";
    private static String host = "momcorp.ceng.metu.edu.tr";
    private static int port = 8080;

    private static Connection connection;

    @Override
    public void initialize() {
        String url = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?useSSL=false";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection =  DriverManager.getConnection(url, this.user, this.password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int createTables() {
        int number_of_tables_created = 0;
        String query_create_MenuItems = "CREATE TABLE IF NOT EXISTS MenuItems (" +
                "itemID INT NOT NULL, " +
                "itemName VARCHAR(40), " +
                "cuisine VARCHAR(20), " +
                "price INT, " +
                "PRIMARY KEY (itemID));";

        String query_create_Ingredients = "CREATE TABLE IF NOT EXISTS Ingredients(" +
                "ingredientID INT NOT NULL, " +
                "ingredientName VARCHAR(40), " +
                "PRIMARY KEY (ingredientID));";

        String query_create_Includes = "CREATE TABLE IF NOT EXISTS Includes(" +
                "itemID INT NOT NULL, " +
                "ingredientID INT NOT NULL, " +
                "FOREIGN KEY (itemID) REFERENCES MenuItems(itemID)" +
                "ON DELETE CASCADE, " +
                "FOREIGN KEY (ingredientID) REFERENCES Ingredients(ingredientID)" +
                "ON DELETE CASCADE, " +
                "PRIMARY KEY(itemID, ingredientID));";

        String query_create_Ratings = "CREATE TABLE IF NOT EXISTS Ratings(" +
                "ratingID INT NOT NULL, " +
                "itemID INT, " +
                "rating INT, " +
                "ratingDate DATE, " +
                "FOREIGN KEY (itemID) REFERENCES MenuItems(itemID) ON DELETE CASCADE, " +
                "PRIMARY KEY (ratingID, itemID));";

        String query_create_DietaryCategories = "CREATE TABLE IF NOT EXISTS DietaryCategories(" +
                "ingredientID INT NOT NULL, " +
                "dietaryCategory VARCHAR(20) NOT NULL, " +
                "FOREIGN KEY (ingredientID) REFERENCES Ingredients(ingredientID) ON DELETE CASCADE, " +
                "PRIMARY KEY (ingredientID, dietaryCategory));";

        //MenuItems
        try{
            Statement statement = this.connection.createStatement();
            statement.executeUpdate(query_create_MenuItems);
            number_of_tables_created++;
            statement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        // Ingredients Table
        try{
            Statement statement = this.connection.createStatement();
            statement.executeUpdate(query_create_Ingredients);
            number_of_tables_created++;
            statement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        // Includes Table
        try{
            Statement statement = this.connection.createStatement();
            statement.executeUpdate(query_create_Includes);
            number_of_tables_created++;
            statement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        // Ratings Table
        try{
            Statement statement = this.connection.createStatement();
            statement.executeUpdate(query_create_Ratings);
            number_of_tables_created++;
            statement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        // DietaryCategories Table
        try{
            Statement statement = this.connection.createStatement();
            statement.executeUpdate(query_create_DietaryCategories);
            number_of_tables_created++;
            statement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return number_of_tables_created;
    }

    @Override
    public int dropTables() {
        int number_of_tables_dropped = 0;

        String query_drop_MenuItems = "DROP TABLE IF EXISTS MenuItems;";
        String query_drop_Ingredients = "DROP TABLE IF EXISTS Ingredients;";
        String query_drop_Includes = "DROP TABLE IF EXISTS Includes;";
        String query_drop_Ratings = "DROP TABLE IF EXISTS Ratings;";
        String query_drop_DietaryCategories = "DROP TABLE IF EXISTS DietaryCategories;";

        // DietaryCategories Table
        try{
            Statement statement = this.connection.createStatement();
            statement.executeUpdate(query_drop_DietaryCategories);
            number_of_tables_dropped++;
            statement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        // Ratings Table
        try{
            Statement statement = this.connection.createStatement();
            statement.executeUpdate(query_drop_Ratings);
            number_of_tables_dropped++;
            statement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        // Includes Table
        try{
            Statement statement = this.connection.createStatement();
            statement.executeUpdate(query_drop_Includes);
            number_of_tables_dropped++;
            statement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        // Ingredients Table
        try{
            Statement statement = this.connection.createStatement();
            statement.executeUpdate(query_drop_Ingredients);
            number_of_tables_dropped++;
            statement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        //MenuItems
        try{
            Statement statement = this.connection.createStatement();
            statement.executeUpdate(query_drop_MenuItems);
            number_of_tables_dropped++;
            statement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        return number_of_tables_dropped;
    }

    @Override
    public int insertMenuItems(MenuItem[] items) {

        int record = 0;
        for (int i = 0; i < items.length; i++)
        {
            try {
                MenuItem m = items[i];
                PreparedStatement st=this.connection.prepareStatement("insert into MenuItems values(?,?,?,?)");
                st.setInt(1, m.getItemID());
                st.setString(2, m.getItemName());
                st.setString(3, m.getCuisine());
                st.setInt(4, m.getPrice());

                st.executeUpdate();
                st.close();
                record++;
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return record;
    }

    @Override
    public int insertIngredients(Ingredient[] ingredients) {

        int record = 0;
        for (int i = 0; i < ingredients.length; i++)
        {
            try {
                Ingredient g = ingredients[i];
                PreparedStatement st=this.connection.prepareStatement("insert into Ingredients values(?,?)");
                st.setInt(1, g.getIngredientID());
                st.setString(2, g.getIngredientName());

                st.executeUpdate();
                st.close();
                record++;
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return record;
    }

    @Override
    public int insertIncludes(Includes[] includes) {

        int record = 0;
        for (int i = 0; i < includes.length; i++)
        {
            try {
                Includes inc = includes[i];
                PreparedStatement st=this.connection.prepareStatement("insert into Includes values(?,?)");
                st.setInt(1, inc.getItemID());
                st.setInt(2, inc.getIngredientID());

                st.executeUpdate();
                st.close();
                record++;
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return record;
    }

    @Override
    public int insertDietaryCategories(DietaryCategory[] categories) { 

        int record = 0;
        for (int i = 0; i < categories.length; i++)
        {
            try {
                DietaryCategory cat = categories[i];
                PreparedStatement st=this.connection.prepareStatement("insert into DietaryCategories values(?,?)");
                st.setInt(1, cat.getIngredientID());
                st.setString(2, cat.getDietaryCategory());

                st.executeUpdate();
                st.close();
                record++;

            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return record;
    }

    @Override
    public int insertRatings(Rating[] ratings) {

        int record = 0;
        for (int i = 0; i < ratings.length; i++)
        {
            try {
                Rating rat = ratings[i];
                PreparedStatement st=this.connection.prepareStatement("insert into Ratings values(?,?,?,?)");
                st.setInt(1, rat.getRatingID());
                st.setInt(2, rat.getItemID());
                st.setInt(3,rat.getRating());
                st.setString(4, rat.getRatingDate());

                st.executeUpdate();
                st.close();
                record++;

            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return record;

    }

    @Override
    public MenuItem[] getMenuItemsWithGivenIngredient(String name) {

        ArrayList<MenuItem> tmp_array = new ArrayList<MenuItem>();
        ResultSet res;

        String query = "SELECT DISTINCT m.itemID, m.itemName, m.cuisine, m.price " +
                "FROM MenuItems m , Ingredients g , Includes c " +
                "WHERE m.itemID = c.itemID AND g.ingredientID = c.ingredientID " +
                    "AND g.ingredientName = '" + name+
                "' ORDER BY m.itemID ASC;";

        try{
            Statement statement = this.connection.createStatement();
            res = statement.executeQuery(query);

            while(res.next()){
                int itemID = res.getInt("itemID");
                String itemName = res.getString("itemName");
                String cuisine = res.getString("cuisine");
                int price = res.getInt("price");
                tmp_array.add(new MenuItem(
                        itemID, itemName, cuisine, price));
            }
            statement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        MenuItem[] m_array =
                new MenuItem[tmp_array.size()];
        for(int i = 0; i < tmp_array.size(); i++){
            m_array[i] = tmp_array.get(i);
        }
        return m_array;
    }

    @Override
    public MenuItem[] getMenuItemsWithoutAnyIngredient() { //!!

        ArrayList<MenuItem> tmp_array = new ArrayList<>();
        ResultSet res;

        String query = "SELECT m.itemID, m.itemName, m.cuisine, m.price " +
                "FROM MenuItems m " +
                "LEFT JOIN Includes inc ON m.itemID = inc.itemID " +
                "WHERE inc.itemID IS NULL " +
                "ORDER BY m.itemID ASC;";

        try{
            Statement statement = this.connection.createStatement();
            res = statement.executeQuery(query);

            while(res.next()){
                int item_ID = res.getInt("itemID");
                String item_name = res.getString("itemName") ;
                String cuisine_ = res.getString("cuisine");
                int price_ = res.getInt("price");
                tmp_array.add(new MenuItem(
                        item_ID, item_name, cuisine_, price_));
            }

            statement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        MenuItem[] r_array =
                new MenuItem[tmp_array.size()];

        for(int i = 0; i < tmp_array.size(); i++){
            r_array[i] = tmp_array.get(i);
        }

        return r_array;

    }

    @Override
    public Ingredient[] getNotIncludedIngredients() {

        ArrayList<Ingredient> tmp_array = new ArrayList<>();
        ResultSet res;

        String query = "SELECT i.ingredientID, i.ingredientName " +
                "FROM Ingredients i " +
                "LEFT JOIN Includes c ON i.ingredientID = c.ingredientID " +
                "WHERE c.ingredientID IS NULL " +
                "ORDER BY i.ingredientID ASC;";

        try{
            Statement statement = this.connection.createStatement();
            res = statement.executeQuery(query);

            while(res.next()){
                int ingredientID = res.getInt("ingredientID");
                String ingredientName = res.getString("ingredientName");
                tmp_array.add(new Ingredient(
                        ingredientID, ingredientName));
            }
            statement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        Ingredient[] r_array =
                new Ingredient[tmp_array.size()];

        for(int i = 0; i < tmp_array.size(); i++){
            r_array[i] = tmp_array.get(i);
        }

        return r_array;
    }

    @Override
    public MenuItem getMenuItemWithMostIngredients() {

        ResultSet res;

        String query = "SELECT m.itemID, m.itemName, m.cuisine, m.price " +
                "FROM MenuItems m, Includes i " +
                "WHERE m.itemID = i.itemID " +
                "GROUP BY m.itemID " +
                "ORDER BY COUNT(m.itemID) DESC " +
                "LIMIT 0,1;";

        try{
            Statement statement = this.connection.createStatement();
            res = statement.executeQuery(query);

            if (!res.next()) {
                statement.close();
            } else {
                int itemID = res.getInt("itemID");
                String itemName = res.getString("itemName");
                String cuisine = res.getString("cuisine");
                int price = res.getInt("price");

                return new MenuItem(itemID, itemName, cuisine, price);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }



        return null;
    }

    @Override
    public QueryResult.MenuItemAverageRatingResult[] getMenuItemsWithAvgRatings() { //left join kullanmadan da olur gibi

        ArrayList<QueryResult.MenuItemAverageRatingResult> tmp_array = new ArrayList<>();
        ResultSet res;

        String query = "SELECT MenuItems.itemID, MenuItems.itemName, AVG(Ratings.rating) as avgRating " +
                "FROM MenuItems " +
                "LEFT OUTER JOIN Ratings ON MenuItems.itemID = Ratings.itemID " +
                "GROUP BY MenuItems.itemID, Ratings.itemID " +
                "ORDER BY avgRating DESC ;";

        try{
            Statement statement = this.connection.createStatement();
            res = statement.executeQuery(query);

            while(res.next()){
                String itemID = res.getString("itemID");
                String itemName = res.getString("itemName");
                String avgRating = res.getString("avgRating");
                tmp_array.add(new QueryResult.MenuItemAverageRatingResult(
                        itemID, itemName, avgRating));
            }

            statement.close();

        }catch (SQLException e) {
            e.printStackTrace();
        }


        QueryResult.MenuItemAverageRatingResult[] r_arr =
                new QueryResult.MenuItemAverageRatingResult[tmp_array.size()];

        for(int i = 0; i < tmp_array.size(); i++){
            r_arr[i] = tmp_array.get(i);
        }

        return r_arr;

    }

    @Override
    public MenuItem[] getMenuItemsForDietaryCategory(String category) {

        ArrayList<MenuItem> tmp_array = new ArrayList<>();
        ResultSet res;

        String query = "SELECT DISTINCT m.itemID, m.itemName, m.cuisine, m.price "+
                "FROM MenuItems m , Includes i , DietaryCategories d "+
                "WHERE m.itemID = i.itemID AND d.ingredientID = i.ingredientID " +
                    "AND d.dietaryCategory = '"+ category +
                "' GROUP BY m.itemID "+
                "HAVING COUNT(m.itemID) = " +
                    "(SELECT COUNT(*) " +
                    "FROM Includes i1 " +
                    "WHERE m.itemID = i1.itemID) "+
                "ORDER BY m.itemID ASC;";

        try{
            Statement statement = this.connection.createStatement();
            res = statement.executeQuery(query);

            while(res.next()){
                int item_ID = res.getInt("itemID");
                String item_name = res.getString("itemName") ;
                String cuisine_ = res.getString("cuisine");
                int price_ = res.getInt("price");
                tmp_array.add(new MenuItem(
                        item_ID, item_name, cuisine_, price_));
            }

            statement.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }

        MenuItem[] r_array =
                new MenuItem[tmp_array.size()];

        for(int i = 0; i < tmp_array.size(); i++){
            r_array[i] = tmp_array.get(i);
        }

        return r_array;

    }

    @Override
    public Ingredient getMostUsedIngredient() {

        ResultSet res;
        String query = "SELECT i.ingredientID, i.ingredientName " +
                "FROM Ingredients i " +
                "GROUP BY i.ingredientID " +
                "ORDER BY COUNT(i.ingredientID) DESC " +
                "LIMIT 0,1;";

        try{
            Statement statement = this.connection.createStatement();
            res = statement.executeQuery(query);

            if(res.next()){
                int ingID = res.getInt("ingredientID");
                String ingName = res.getString("ingredientName");

                return new Ingredient(ingID, ingName);

            }
            statement.close();

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public QueryResult.CuisineWithAverageResult[] getCuisinesWithAvgRating() {

        ArrayList<QueryResult.CuisineWithAverageResult> tmp_array = new ArrayList<>();
        ResultSet res;

        String query = "SELECT m.cuisine, AVG(r.rating) AS averageRating " +
            "FROM MenuItems m " +
                "LEFT JOIN Ratings r ON m.itemID = r.itemID " +
            "GROUP BY m.cuisine " +
            "ORDER BY averageRating DESC;";

        try{
            Statement statement = this.connection.createStatement();
            res = statement.executeQuery(query);

            while(res.next()){
                String cuisine = res.getString("cuisine");
                String avgRating = res.getString("averageRating");

                tmp_array.add(new QueryResult.CuisineWithAverageResult(
                        cuisine, avgRating));
            }

            statement.close();

        }catch (SQLException e) {
            e.printStackTrace();
        }


        QueryResult.CuisineWithAverageResult[] r_arr =
                new QueryResult.CuisineWithAverageResult[tmp_array.size()];

        for(int i = 0; i < tmp_array.size(); i++){
            r_arr[i] = tmp_array.get(i);
        }

        return r_arr;
    }

    @Override
    public QueryResult.CuisineWithAverageResult[] getCuisinesWithAvgIngredientCount() {

        ArrayList<QueryResult.CuisineWithAverageResult> tmp_array = new ArrayList<>();
        ResultSet res;

        String query = "SELECT cuisine, AVG(ing_count) AS avg_Count " +
                "FROM (" +
                    "SELECT mm.cuisine, COUNT(i.ingredientID) AS ing_count " +
                    "FROM MenuItems mm " +
                    "LEFT JOIN Includes i ON mm.itemID = i.itemID " +
                    "GROUP BY mm.itemID " +
                    ") s " +
                "GROUP BY cuisine " +
                "ORDER BY avg_Count DESC;";

        try{
            Statement statement = this.connection.createStatement();
            res = statement.executeQuery(query);

            while(res.next()){
                String cuisine = res.getString("cuisine");
                String avgcount = res.getString("avg_Count");

                tmp_array.add(new QueryResult.CuisineWithAverageResult(
                        cuisine, avgcount));
            }

            statement.close();

        }catch (SQLException e) {
            e.printStackTrace();
        }

        QueryResult.CuisineWithAverageResult[] r_arr =
                new QueryResult.CuisineWithAverageResult[tmp_array.size()];

        for(int i = 0; i < tmp_array.size(); i++){
            r_arr[i] = tmp_array.get(i);
        }

        return r_arr;
    }

    @Override
    public int increasePrice(String ingredientName, String increaseAmount) {

        int record = 0;

        String query = "UPDATE MenuItems SET MenuItems.price = price + " +increaseAmount+
                " WHERE MenuItems.itemID IN " +
                "(SELECT c.itemID " +
                "FROM Ingredients g, Includes c " +
                "WHERE g.ingredientName = '" + ingredientName +
                "' AND c.ingredientID = g.ingredientID);";

        try{
            Statement statement = this.connection.createStatement();
            record = statement.executeUpdate(query);
            statement.close();

        }catch (SQLException e) {
            e.printStackTrace();
        }

        return record;

    }

    @Override
    public Rating[] deleteOlderRatings(String date) {

        ArrayList<Rating> tmp_array = new ArrayList<>();
        ResultSet res;

        String query = "SELECT r.ratingID, r.itemID, r.rating , r.ratingDate " +
                "FROM Ratings r " +
                "WHERE r.ratingDate < '" + date +
                "' ORDER BY r.ratingID ASC;";

        String del_op = "DELETE Ratings " +
                "FROM Ratings " +
                "WHERE Ratings.ratingDate < '" + date +
                "' ;";

        try{
            Statement statement = this.connection.createStatement();
            res = statement.executeQuery(query);

            while(res.next()){
                int rating_ID = res.getInt("ratingID");
                int item_ID = res.getInt("itemID");
                int rat = res.getInt("rating");
                String datte = res.getString("ratingDate");
                tmp_array.add(new Rating(
                        rating_ID, item_ID, rat, datte));
            }

            statement.close();

        }catch (SQLException e) {
            e.printStackTrace();
        }

        try{
            Statement statement = this.connection.createStatement();

            statement.executeUpdate(del_op);
        }catch (SQLException e) {
            e.printStackTrace();
        }

        Rating[] r_array =
                new Rating[tmp_array.size()];

        for(int i = 0; i < tmp_array.size(); i++){
            r_array[i] = tmp_array.get(i);
        }

        return r_array;

    }
}
