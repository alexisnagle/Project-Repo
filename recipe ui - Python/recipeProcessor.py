from recipe import Recipe
import json


class RecipeProcessor:

    # initialize recipe processor with empty list
    def __init__(self):
        self.recipe_list = []

    # process json file and create recipe objects
    def load_recipes(self, json_file):
        # open json file
        f = open(json_file, encoding='utf-8')
        data = json.load(f)

        # extract necessary data
        for i in data:
            name = i['name']
            cook_time = i['cookTime']
            prep_time = i['prepTime']
            yield_amount = i['recipeYield']
            url = i['image']
            if cook_time == '':
                cook_time = 'PT0H0M'
            if prep_time == '':
                prep_time = 'PT0H0M'

            # create recipe objects
            recipe = Recipe(name, cook_time, prep_time, yield_amount, url)
            self.recipe_list.append(recipe)
        f.close()

    # return list of recipe objects
    def get_recipes(self):
        return self.recipe_list

    # print list of recipe objects in a tabulated format
    def tabulate_recipes(self):
        print(f"{'Name':60}{'Prep Time':15}{'Cook Time':15}{'Yield':15}")
        for recipe in self.recipe_list:
            print(f"{recipe.get_name()[:50]:60}{recipe.get_prep_time():15}{recipe.get_cook_time():15}{recipe.get_yield():15}")
