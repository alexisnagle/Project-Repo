from doctor_portal import DoctorPortal
from doctor_processor import DoctorProcessor
from PyQt5.QtWidgets import QApplication
import sys


def main():
    doctor_processor = DoctorProcessor()
    doctor_list = doctor_processor.process_database()


    app = QApplication(sys.argv)
    portal = DoctorPortal(doctor_list)
    portal.show()
    app.exec()


main()
