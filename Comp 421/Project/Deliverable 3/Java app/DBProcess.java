package main;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;


public class DBProcess {
	public static SQLConnection sql = new SQLConnection("bendodev.no-ip.org",
			"comp421project", "comp421", "bitoku");

	static boolean firstResult(ResultSet rs) {
		try {

			if (!rs.next()) {
				return false;
			}

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

	}

	public static int loginUser(String email, String pass) {
		List<HashMap<String, String>> rs = sql.ExecuteQuery(
				"SELECT id FROM user WHERE email = ? AND password = ?", email,
				pass);

		if (rs.size() < 1) {
			return -1;
		}

		int id;
		id = Integer.parseInt(rs.get(0).get("id"));
		return id;
	}

	public static boolean existsUser(String email) {
		List<HashMap<String, String>> rs = sql.ExecuteQuery(
				"SELECT id FROM user WHERE email = ?", email);

		if (rs.size() < 1) {
			return false;
		}

		int id;
		id = Integer.parseInt(rs.get(0).get("id"));

		return (id != -1);
	}

	public static int getNextId(String tableName) {

		List<HashMap<String, String>> rs = sql
				.ExecuteQuery("SELECT MAX(id) as max_val FROM `" + tableName
						+ "`");

		if (rs.size() < 1) {
			return -1;
		}

		// debugPrint(rs);

		int id;
		id = Integer.parseInt(rs.get(0).get("max_val"));
		return id + 1;
	}

	public static boolean signUp(String email, String password) {
		if (!existsUser(email)) {
			int rs = sql
					.ExecuteUpdate(
							"INSERT INTO user (id,email,password,guidemode,level_id,info) VALUES (?,?,?,0,1,'')",
							String.valueOf(getNextId("user")), email, password);
			return true;
		}
		return false;
	}

	static void debugPrint(List<HashMap<String, String>> list) {
		System.out.println("Printing shit of size :" + list.size());
		for (HashMap<String, String> map : list) {
			for (Entry<String, String> e : map.entrySet()) {
				System.out.printf("%20s", e.getValue());

			}
			System.out.println();
		}
	}

	static List<String> findRecipes(String arg) {
		List<HashMap<String, String>> resultSet = sql
				.ExecuteQuery("SELECT name FROM recipe WHERE name LIKE '" + arg
						+ "%'");
		List<String> list = new ArrayList<String>();

		for (HashMap<String, String> map : resultSet) {
			list.add(map.get("name"));
		}

		return list;
	}

	static List<String> getUserRecipe(int userId) {
		List<HashMap<String, String>> resultSet = sql.ExecuteQuery(
				"SELECT name FROM recipe WHERE user_id = ?",
				String.valueOf(userId));
		List<String> list = new ArrayList<String>();

		for (HashMap<String, String> map : resultSet) {
			list.add(map.get("name"));
		}

		return list;
	}

	static List<String> getMostLikedRecipes() {
		List<HashMap<String, String>> resultSet = sql
				.ExecuteQuery("SELECT name FROM recipe ORDER BY rating desc LIMIT 3");
		List<String> list = new ArrayList<String>();

		for (HashMap<String, String> map : resultSet) {
			list.add(map.get("name"));
		}

		return list;
	}

	static HashMap<String, String> getRecipe(String arg) {
		List<HashMap<String, String>> resultSet = sql
				.ExecuteQuery("SELECT * FROM recipe WHERE name LIKE '" + arg
						+ "%'");
		HashMap<String, String> row = resultSet.get(0);

		return row;
	}

	static List<String> findRecipesByIngredients(String[] ingredientList) {

		String ingredientListAsString = "";

		for (int i = 0; i < ingredientList.length; i++) {
			ingredientListAsString += "'" + ingredientList[i] + "'";
			if (i != ingredientList.length - 1) {
				ingredientListAsString += ",";
			}
		}

		List<HashMap<String, String>> resultSet = sql.ExecuteQuery(
				"SELECT r.`name` FROM recipe r "
						+ "JOIN recipe_ingredient ri ON ri.recipe_id = r.id "
						+ "JOIN ingredient i ON i.id = ri.ingredient_id "
						+ "WHERE i.`name` in (" + ingredientListAsString
						+ " ) " + "GROUP BY r.`id` "
						+ "HAVING COUNT(r.`id`) >= ?",
				String.valueOf(ingredientList.length));

		List<String> list = new ArrayList<String>();

		for (HashMap<String, String> map : resultSet) {
			list.add(map.get("name"));
		}

		return list;
	}

	static void userRateRecipe(int rating,int userId, int recipeId) {
		List<HashMap<String,String>> resultSet = sql.ExecuteQuery(
				"SELECT * FROM user_recipe_rating WHERE user_id= ? AND recipe_id = ?",
				String.valueOf(userId),String.valueOf(recipeId));
		
		if(resultSet.size() == 0) {
			sql.ExecuteUpdate("INSERT INTO user_recipe_rating (rating,user_id,recipe_id) VALUE (?,?,?)",String.valueOf(rating),String.valueOf(userId),String.valueOf(recipeId));
		}
		else {
			sql.ExecuteUpdate("UPDATE user_recipe_rating SET rating = ? WHERE user_id = ? AND recipe_id = ? ",String.valueOf(rating),String.valueOf(userId),String.valueOf(recipeId));
		}
	}
	
}
