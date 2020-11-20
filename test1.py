def fibonacci(n):
    if n > 1:
        return fibonacci(n-1)+fibonacci(n-2)
    return n

element = 22
value = fibonacci(element)
print(element)
print(value)