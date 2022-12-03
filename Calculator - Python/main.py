from calculator_layout import CalculatorLayout
from PyQt5.QtWidgets import QApplication
import sys


def main():

    app = QApplication(sys.argv)
    calculator = CalculatorLayout()
    calculator.show()
    app.exec()


main()
