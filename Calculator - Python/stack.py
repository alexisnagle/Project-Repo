class Stack:
    def __init__(self):
        self._items = []

    # repr - Returns the canonical string representation of the object.
    def __repr__(self):
        my_str = ""
        if self.get_size() > 0:
            for i in range(self.get_size() - 1, -1, -1):
                my_str = my_str + self._items[i]
                if i > 0:
                    my_str = my_str + ", "
        return my_str

    # str - Creates a new string object from the given object.
    def __str__(self):
        return " --> ".join(reversed(self._items))  # one-liner for the code above

    def push(self, item):
        self._items.append(item)

    def pop(self):
        if self.get_size() > 0:
            index = self.get_size() - 1  # self._items[-1] works, but I don’t usually recommend negative indices
            item = self._items[index]
            del self._items[index]
            return item
        return None  # or raise Exception("Popping from an Empty Stack")

    def is_empty(self):
        return self._items == []

    def get_size(self):
        return len(self._items)

    def peek(self):
        if self.is_empty():
            raise Exception("Peeking into an empty stack")
        if self.get_size() > 0:
            return self._items[self.get_size() - 1]