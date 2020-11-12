def fibonacci(n):
    if n > 2:
        return fibonacci(n-1)+fibonacci(n-2)
    return n

element = 5
value = fibonacci(element)
print(value)