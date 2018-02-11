LATEX_ENGINE=pdflatex
OUTPUT_DIRECTORY=build
MAIN_FILE=main.tex

all: build

clean:
	rm -rf $(OUTPUT_DIRECTORY)

build: prepare
	$(LATEX_ENGINE) -output-directory=$(OUTPUT_DIRECTORY) $(MAIN_FILE)

prepare:
	mkdir -p $(OUTPUT_DIRECTORY)
