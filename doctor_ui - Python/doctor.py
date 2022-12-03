class Doctor():
    def __init__(self, first_name, last_name, specialty, gender):
        self.first_name  = first_name
        self.last_name = last_name
        self.specialty = specialty
        self.gender = gender
        
    def get_first_name(self):
        return self.first_name

    def get_last_name(self):
        return self.last_name

    def get_specialty(self):
        return self.specialty

    def get_gender(self):
        return self.gender