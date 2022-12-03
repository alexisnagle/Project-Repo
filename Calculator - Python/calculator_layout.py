from functools import partial
from evaluate_expression import EvaluateExpression
from PyQt5.QtWidgets import (QApplication, QWidget, QPushButton, QGridLayout, QLineEdit)


class CalculatorLayout(QWidget):
    def __init__(self):
        super().__init__()
        self.setup_ui()
        self.equation = ''

    def setup_ui(self):
        grid = QGridLayout()
        self.setLayout(grid)
        self.text_field = QLineEdit("0")
        self.text_field.setReadOnly(True)
        grid.addWidget(self.text_field, 0, 0, 1, 4)

        button_labels = ['Cls', ' ( ', ' ) ', 'Close',
                         '7', '8', '9', ' / ',
                         '4', '5', '6', ' * ',
                         '1', '2', '3', ' - ',
                         '0', '.', '=', ' + ']

        button_positions = [(i, j) for i in range(1, 6) for j in range(4)]
        for position, button_label in zip(button_positions, button_labels):
            if button_label == '':
                continue
            button = QPushButton(button_label)
            button.clicked.connect(partial(self.clicked_on, button_label))
            grid.addWidget(button, *position)

        self.move(300, 150)
        self.setWindowTitle('Alexis Nagle\'s Calculator')

    def clicked_on(self, button_label):
        if button_label == '=':
            evaluation = EvaluateExpression(self.equation)
            answer = evaluation.get_answer()
            if answer is None:
                answer = 'Invalid Input'
            self.equation = ''
            self.text_field.setText(str(answer))
        elif button_label == 'Cls':
            self.equation = ''
            self.text_field.setText('0')
        elif button_label == 'Close':
            self.close()
        else:
            self.equation = self.equation + button_label
            self.text_field.setText(self.equation)
