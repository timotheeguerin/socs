package main;

import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Engine {

	private int userId;

	Scanner scanIn = new Scanner(System.in);

	public void start() {
		homepage();
	}

	private void homepage() {
		System.out.println("Welcome to to best cooking website in the world!");
		System.out.println("================================================");
		int choice = printOptions(new String[] { "Signin", "Sign up", "Quit" });
		if (choice == 1) { // Sign in
			signin();
		} else if (choice == 2) { // Sign up
			signup();
		} else if (choice == 3) { // Quit
			System.out.println("Why do you leave us!");
			scanIn.close();
			System.exit(0);
		}
		mainMenu();
	}

	private void mainMenu() {
		System.out.println("\n\n");
		System.out.println("================================================");
		System.out.println("Welcome back user " + userId);
		
		while(true) {
			System.out.println("================================================");
			
			int choice = printOptions(new String[] { "Search by recipe name",
					"Search by ingredients","Our most rated recipes", "My recipes","Quit" });
			if (choice == 1) { // Search by name
				searchByRecipeName();
			} else if (choice == 2) { // Search by ingredients
				searchByRecipeIngredient();
			} else if (choice == 3) { // Search by ingredients
				recipeList(DBProcess.getMostLikedRecipes());
			} else if (choice == 4) { // Search by ingredients
				recipeList(DBProcess.getUserRecipe(userId));
			} else if (choice == 5) { // Quit
				System.out.println("Why do you leave us!");
				scanIn.close();
				System.exit(0);
			}
		}
	}

	private void searchByRecipeName() {

		System.out.println("================================================");
		System.out.println("\t Search recipe by name");
		System.out.println("================================================");
		String query = scanIn.next();
		List<String> recipes = DBProcess.findRecipes(query);
		recipeList(recipes);

	}

	private void searchByRecipeIngredient() {

		System.out.println("================================================");
		System.out.println("\t Search recipe by ingredients");
		System.out.println("================================================");
		String query = scanIn.next();
		List<String> recipes = DBProcess.findRecipesByIngredients(query
				.split(","));
		recipeList(recipes);

	}

	private void recipeList(List<String> recipes) {
		int choice = printOptions(recipes.toArray(new String[recipes.size()]));
		String recipeName = recipes.get(choice - 1);
		displayRecipe(recipeName);
	}

	private void displayRecipe(String recipeName) {

		HashMap<String, String> recipe = DBProcess.getRecipe(recipeName);
		System.out.println();
		System.out.println("================================================");
		System.out.println("\t" + recipe.get("name"));
		System.out.println("================================================");
		System.out.printf("%-20s: %s\n", "Preparation time",
				recipe.get("preparation_time"));
		System.out.printf("%-20s: %s\n", "Cooking time",
				recipe.get("cooking_time"));
		System.out.println(recipe.get("preparation"));

		int choice = printOptions(new String[] { "Rate this recipe",
				"Back to menu" });
		if (choice == 1) { // Search by name
			rateRecipe(recipe);
		} else if (choice == 2) { // Search by ingredients
			return;
		}
	}

	private void rateRecipe(HashMap<String, String> recipe) {

		System.out.println();
		System.out.println("================================================");
		System.out.println("\t Rate " + recipe.get("name"));
		System.out.println("================================================");
		int choice = printOptions(new String[] { "Like", "Dislike" });
		int rating = 0;
		if (choice == 1) {
			rating = 1;
		} else if (choice == 2) {
			rating = -1;
		}

		DBProcess.userRateRecipe(rating, userId, Integer.parseInt(recipe.get("id")));
		
		System.out.println("Recipe rated!");
	}

	private void signin() {

		System.out.println();
		System.out.println("================================================");
		System.out.println("\t LOGIN");
		System.out.println("================================================");
		String email, password;
		while (true) {
			System.out.print("Email: ");
			email = scanIn.next();
			System.out.print("Password: ");
			password = scanIn.next();
			userId = DBProcess.loginUser(email, password);
			if (userId != -1) {
				break;
			}
		}
	}

	private void signup() {
		System.out.println();
		System.out.println("================================================");
		System.out.println("\t SIGN UP");
		System.out.println("================================================");
		String email, password;
		while (true) {
			System.out.print("Email: ");
			email = scanIn.next();
			System.out.print("Password: ");
			password = scanIn.next();

			if (DBProcess.signUp(email, password)) {
				userId = DBProcess.loginUser(email, password);
				break;
			}
		}
	}

	private int printOptions(String[] options) {
		System.out.println("\nWhat do you want to do?");
		for (int i = 0; i < options.length; i++) {
			System.out.println((i + 1) + ".\t" + options[i]);
		}

		int result = 0;
		while (!(result > 0 && result <= options.length)) {
			System.out.print("Choice: ");
			result = scanIn.nextInt();
		}
		return result;
	}
}
