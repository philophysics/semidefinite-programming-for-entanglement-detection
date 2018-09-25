LATEX_ENGINE=pdflatex

all:
	$(LATEX_ENGINE) main.tex
	bibtex main
	$(LATEX_ENGINE) main.tex
	rm -f main.aux
	rm -f main.bbl
	rm -f main.blg
	rm -f main.idx
	rm -f main.lof
	rm -f main.log
	rm -f main.lot
	rm -f main.out
	rm -f main.run.xml
	rm -f main.toc
	rm -f main-blx.bib
