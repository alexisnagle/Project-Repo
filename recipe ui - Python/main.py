from recipeProcessor import RecipeProcessor
from recipeUI import RecipeUI
import warnings


def main():
    warnings.filterwarnings("ignore")  # ignore date parser warning

    # read json file, create recipe objects, print tabulated report

    recipe_processor = RecipeProcessor()
    recipe_processor.load_recipes("recipes.json")
    recipe_processor.tabulate_recipes()

    # print out ui

    recipe_ui = RecipeUI()
    recipe_ui.layout_ui(recipe_processor.get_recipes())

    # extra credit portion

    search = input("Enter your search field: ")
    recipe_ui = RecipeUI()
    recipe_ui.bonus_ui(search, recipe_processor.get_recipes())


main()
