import sqlite3
from doctor import Doctor


class DoctorProcessor():
    def __init__(self):
        self.doctor_list = []

    def process_database(self):
        connection = sqlite3.connect("cse2050_healthfirst_db.db")
        cursor = connection.cursor()
        for row in cursor.execute("SELECT full_name, specialty, gender  FROM mlb_doctors"):
            name = row[0].split(",")[0].split(" ")
            first_name = name[0]
            last_name = name[len(name) - 1]
            doctor = Doctor(first_name, last_name, row[1], row[2])
            self.doctor_list.append(doctor)
        connection.close()
        return self.doctor_list
