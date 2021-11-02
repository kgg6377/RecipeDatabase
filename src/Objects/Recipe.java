package Objects;

import java.util.ArrayList;

public class Recipe {
    private final User createdBy;
    private final String creationDate;
    private final String NAME;
    private final String steps;
    private final ArrayList<Integer> ingredientArrayList;

    private Difficulty difficulty;
    private String description;
    private int cookTime;
    private int servings;
    private int rating;
    private ArrayList<Category> categoryArrayList;


    public Recipe(String name, ArrayList<Integer> ingredientArrayList,
                  String steps, User createdBy, String creationDate) {
        this.NAME = name;
        this.ingredientArrayList = ingredientArrayList;
        this.categoryArrayList = null;
        this.steps = steps;
        this.createdBy = createdBy;
        this.creationDate = creationDate;
        this.difficulty = Difficulty.MEDIUM;
        this.description = null;
        this.cookTime = 0;
        this.servings = 0;
        this.rating = 0;
    }

    // Modifiers
    public void setCategoryArrayList(ArrayList<Category> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    // Accessors
    public Difficulty getDifficulty() {
        return difficulty;
    }

    public String getNAME() {
        return NAME;
    }

    public String getSteps() {
        return steps;
    }

    public String getDescription() {
        return description;
    }

    public int getCookTime() {
        return cookTime;
    }

    public int getRating() {
        return rating;
    }

    public int getServings() {
        return servings;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public ArrayList<Category> getCategoryArrayList() {
        return categoryArrayList;
    }

    public ArrayList<Integer> getIngredientArrayList() {
        return ingredientArrayList;
    }

}