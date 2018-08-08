LATEX_ENGINE=pdflatex

all:
	$(LATEX_ENGINE) main.tex
	bibtex main
	$(LATEX_ENGINE) main.tex
	rm main.aux
	rm main.bbl
	rm main.blg
	rm main.idx
	rm main.lof
	rm main.log
	rm main.lot
	rm main.out
	rm main.run.xml
	rm main.toc
	rm main-blx.bib
