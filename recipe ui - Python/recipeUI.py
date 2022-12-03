from ezgraphics import GraphicsImage, GraphicsWindow
import re


class RecipeUI:

    # initialize UI by setting up window
    def __init__(self):
        self.GAP = 50
        self.MAX_WIDTH = 980
        self.setup_window()

    def setup_window(self):
        self.win = GraphicsWindow(self.MAX_WIDTH, 1000)
        self.canvas = self.win.canvas()
        self.win.setTitle(f"{'Alexis Nagle Recipe Viewer':^285}")

    # iterate through recipe objects and print the first 16 to a screen
    def layout_ui(self, recipes):
        num_pictures = 16
        if len(recipes) < 16:
            num_pictures = len(recipes)
        if len(recipes) > 0:
            x = 15
            y = 30
            max_y = 0

            recipe = recipes[0]
            pic = GraphicsImage(recipe.get_image())
            self.canvas.drawImage(x, y, pic)
            self.show_recipe_desc(recipe, x, y + pic.height())

        for i in range(1, num_pictures):
            recipe = recipes[i]

            max_y = max(max_y, pic.height())
            previous = pic
            pic = GraphicsImage(recipe.get_image())
            x = x + previous.width() + self.GAP
            if x + pic.width() < self.MAX_WIDTH:
                self.canvas.drawImage(x, y, pic)
                self.show_recipe_desc(recipe, x, y + pic.height())
            else:
                x = 15
                y = y + max_y + self.GAP
                max_y = 0
                self.canvas.drawImage(x, y, pic)
                self.show_recipe_desc(recipe, x, y + pic.height())
        self.win.wait()

    # print recipe object by displaying image, name, cook time, and prep time
    def show_recipe_desc(self, recipe, x, y):
        txt = "Name: " + (recipe.get_name()[:25]) + "\nPrep Time: " + recipe.get_prep_time() \
              + "\nCook Time: " + recipe.get_cook_time()
        self.canvas.setColor('black')
        self.canvas.drawText(x, y, txt)

    # based upon search keyword, find and display corresponding recipes
    def bonus_ui(self, search, recipes):
        search_recipes = []
        for recipe in recipes:
            recipe_name = re.sub(r'[^a-zA-Z]', ' ', recipe.get_name())
            if search.lower() in recipe_name[:25].lower():
                search_recipes.append(recipe)
        self.canvas.setFill('black')
        self.canvas.drawRect(5, 5, 990, 20)

        if len(search_recipes) < 16:
            count = len(search_recipes)
        else:
            count = 16
        txt = 'Displaying ' + str(count) + '/' + str(len(search_recipes)) + ' recipe(s) for \"' + search + '\"'
        self.win.setTitle(f'{txt:^285}')
        self.canvas.setColor('white')
        self.canvas.drawText(15, 10, txt)
        self.layout_ui(search_recipes)