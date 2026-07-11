import os
import sys

def get_classes(directory):
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith(".kt"):
                with open(os.path.join(root, file), 'r') as f:
                    for line in f:
                        if "class Main" in line and "{" in line:
                            print(file, line.strip())

if __name__ == "__main__":
    get_classes(sys.argv[1])
