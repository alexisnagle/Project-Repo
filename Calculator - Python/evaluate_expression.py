from stack import Stack


class EvaluateExpression:
    def __init__(self, expression):
        self.answer = ''
        self.evaluate(expression)

    def evaluate(self, expression):
        equation = expression.split(" ")
        numbers = Stack()
        operands = Stack()
        for value in equation:
            if self.is_float(value):
                numbers.push(float(value))
            elif value == "(":
                operands.push(value)
            elif value == ")":
                operator = operands.pop()
                if operator != '(':
                    num1 = numbers.pop()
                    num2 = numbers.pop()
                    result = self.operation(operator, num1, num2)
                    numbers.push(result)
                    operands.pop()  # pop '('
            elif value == "+" or value == '-' or value == "*" or value == '/':
                if (not operands.is_empty()) and (operands.peek() != '('):
                    that_op = self.precedence(operands.peek())
                    this_op = self.precedence(value)
                    if that_op >= this_op:
                        operator = operands.pop()
                        num1 = numbers.pop()
                        num2 = numbers.pop()
                        result = self.operation(operator, num1, num2)
                        numbers.push(result)
                operands.push(value)
        while not operands.is_empty():
            operator = operands.pop()
            num1 = numbers.pop()
            num2 = numbers.pop()
            result = self.operation(operator, num1, num2)
            numbers.push(result)
        if numbers.get_size() != 1:
            self.answer = None
        else:
            self.answer = numbers.pop()

    def is_float(self, value):
        try:
            float(value)
            return True
        except ValueError:
            return False

    def precedence(self, operation):
        if operation == "*" or operation == "/":
            return 2
        if operation == "+" or operation == "-":
            return 1

    def operation(self, operation, num1, num2):
        if num1 is None or num2 is None:
            return None
        if operation == "*":
            return num2 * num1
        if operation == "/":
            if num1 == 0:
                return None
            return num2 / num1
        if operation == "+":
            return num2 + num1
        if operation == "-":
            return num2 - num1

    def get_answer(self):
        return self.answer
