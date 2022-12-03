from isodate import parse_duration
from dateparser import parse
from PIL import Image
import urllib.request
import ssl
import re


class Recipe:

    # initialize recipe object
    def __init__(self, name, cook_time, prep_time, yield_amount, url):
        self.name = name
        self.cook_time = cook_time
        self.prep_time = prep_time
        self.yield_amount = yield_amount
        self.url = url

    # return name
    def get_name(self):
        return self.name

    # return formatted cook time
    def get_cook_time(self):
        try:
            duration = str(parse_duration(self.cook_time))
            return parse(duration).strftime("%H:%M")
        except:
            return self.cook_time

    # return formatted prep time
    def get_prep_time(self):
        try:
            duration = str(parse_duration(self.prep_time))
            return parse(duration).strftime("%H:%M")
        except:
            return self.prep_time

    # return yield amount
    def get_yield(self):
        return self.yield_amount

    def set_image(self, url):
        # save image as jpg
        recipe_name = re.sub(r'[^a-zA-Z]', '', self.name)
        jpg_name = recipe_name + '.jpg'
        ssl._create_default_https_context = ssl._create_unverified_context  # resolve error: 'urllib uncertified'
        urllib.request.urlretrieve(url, jpg_name)

        # reformat jpg to gif and resize
        scaled_width = 200
        img = Image.open(jpg_name)
        percent_width = (scaled_width / float(img.size[0]))
        h_size = int((float(img.size[1]) * float(percent_width)))
        img = img.resize((scaled_width, h_size), Image.ANTIALIAS)
        gif_name = recipe_name + '.gif'
        img.save(gif_name)
        self.gif = gif_name  # save gif to recipe object

    # return gif
    def get_image(self):
        self.set_image(self.url)
        return self.gif
