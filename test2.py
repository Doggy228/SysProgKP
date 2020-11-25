def fibonacci(n):
    # Condition check
    if n > 1:
        return fibonacci(n-1)+fibonacci(n-2) # Recursive call
    return n                # For n=0 or n=1

2element = 22                # Index in sequence Fibonacci
value = fibonacci(element)  # Calc value
print(element)              # Print seq. index 
print(value)                # Print value