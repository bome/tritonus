# Generated automatically from Makefile.in by configure.
# compilation and packing for alsamididump
#

SHELL = /bin/sh

prefix=/usr/local

CC = gcc
INSTALL = /usr/bin/install -c
INSTALL_PROGRAM = ${INSTALL}
INSTALL_DATA = ${INSTALL} -m 644
INSTALL_SCRIPT = ${INSTALL_PROGRAM}


#%.class: %.java
#	export CLASSPATH=.:src:$$CLASSPATH; $(JAVAC) -d $(DESTDIR) $<



PROGRAM = alsamidiout
OBJ = alsamidiout.o
SRC = alsamidiout.c
LIBS = -lasound 

srcdir=.
DISTFILES=COPYING Makefile.in README configure.in configure util $(SRC)
SUBDIRS=@SUBDIRS@

#----------------- rules -------------------

.PHONY: all
all: $(PROGRAM)

$(PROGRAM): $(OBJ)
	$(CC) $< $(LIBS) -o $(PROGRAM)


doc:	all 
	$(MAKE) -C doc

test:	all 
	$(MAKE) -C test

.PHONY: install
install: all
	$(INSTALL_DATA) $(LIBDEST) $(JAVAEXTPATH)
	@for d in $(SUBDIRS); do $(MAKE) -C $$d install; done

.PHONY: install-link
install-link:
	(cd  $(JAVAEXTLIBPATH) && $(LN_S) $(LIBDEST_ABSOLUTE))
	@for d in $(SUBDIRS); do $(MAKE) -C $$d install-link; done

.PHONY: clean
clean:
	rm -rf $(PROGRAMS) *.o *~
	@for d in $(SUBDIRS); do $(MAKE) -C $$d clean; done

.PHONY: veryclean
veryclean: clean
	rm -rf TAGS

.PHONY:tags
tags: TAGS

TAGS: $(SRCTMP)
	etags $(SRCTMP)


dist:
	echo $(PROGRAM)-`cat version` >.fname
	-rm -rf `cat .fname`
	mkdir `cat .fname`
	dst=`cat .fname`; for f in $(DISTFILES); do \
	ln $(srcdir)/$$f $$dst/$$f || { echo copying $$f; \
	cp -pr $(srcdir)/$$f $$dst/$$f; } \
	done
	tar czhf `cat .fname`.tar.gz `cat .fname`
	-rm -rf `cat .fname` .fname

devdist:
	echo $(PROGRAM)-`date +%Y-%m-%d` >.fname
	-rm -rf `cat .fname`
	mkdir `cat .fname`
	dst=`cat .fname`; for f in $(DISTFILES); do \
	ln $(srcdir)/$$f $$dst/$$f || { echo copying $$f; \
	cp -pr $(srcdir)/$$f $$dst/$$f; } \
	done
	tar czhf `cat .fname`.tar.gz `cat .fname`
	-rm -rf `cat .fname` .fname

devdist-upload: devdist
	echo $(PROGRAM)-`date +%Y-%m-%d.tar.gz` >.fname
	scp `cat .fname` pfisterer@tritonus.sourceforge.net:/home/groups/ftp/pub/tritonus/`cat .fname`
	-rm -f .fname
