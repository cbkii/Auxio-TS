import os
import sys

def get_imports(directory):
    for root, dirs, files in os.walk(directory):
        for file in files:
            if file.endswith(".kt"):
                with open(os.path.join(root, file), 'r') as f:
                    for line in f:
                        if line.startswith("import "):
                            print(line.strip())

if __name__ == "__main__":
    get_imports(sys.argv[1])
