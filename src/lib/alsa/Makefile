# Generated automatically from Makefile.in by configure.
# compilation and packing of the native library for alsa
#

SHELL = /bin/sh

prefix=/usr/local/java/jdk1.2.2
JAVADIR=$(prefix)
OSNAME=linux
ARCHNAME=i386
CC=gcc
CPPFLAGS=-Wall -I$(JAVADIR)/include -I$(JAVADIR)/include/$(OSNAME) -fPIC -D_REENTRANT
INSTALL = /usr/bin/install -c
INSTALL_PROGRAM = ${INSTALL}
INSTALL_DATA = ${INSTALL} -m 644
INSTALL_SCRIPT = ${INSTALL_PROGRAM}
LN_S=ln -s

JAVAEXTPATH=$(JAVADIR)/jre/lib/ext
JAVAEXTLIBPATH=$(JAVAEXTPATH)/$(ARCHNAME)

OBJDIR=../../..
vpath %.class $(OBJDIR)/org/tritonus/lowlevel/alsa

LIBBASENAME=libtritonusalsa
LIBDEST=$(LIBBASENAME).so.1.0
# TODO:
LIBDEST_ABSOLUTE=$(LIBDEST)
LIBS=-lasound

JAVAH = javah -force -classpath $(OBJDIR)

OBJ=\
	org_tritonus_lowlevel_alsa_ASequencer0.o
JNICLASSFILES=\
	ASequencer0.class
JNIHEADERS:=$(OBJ:.o=.h)

JNICLASSES=\
	org.tritonus.lowlevel.alsa.ASequencer0

#----------------- rules -------------------

.PHONY: all
all: $(LIBDEST)

$(LIBDEST):	$(OBJ)
	$(CC) -shared -Wl,-soname,$(LIBBASENAME).so.1 -o $(LIBDEST) $(OBJ) $(LIBS)
	$(LN_S) -f $(LIBBASENAME).so.1.0 $(LIBBASENAME).so.1
	$(LN_S) -f $(LIBBASENAME).so.1 $(LIBBASENAME).so

$(OBJ): $(JNIHEADERS)

$(JNIHEADERS): $(JNICLASSFILES)
	rm -f $(JNIHEADERS)
	$(JAVAH) -d . $(JNICLASSES)

.PHONY: install
install:
	mkdir -p $(JAVAEXTLIBPATH)
	$(INSTALL_PROGRAM) $(LIBDEST) $(JAVAEXTLIBPATH)
	(cd $(JAVAEXTLIBPATH) && ln -sf $(LIBBASENAME).so.1.0 $(LIBBASENAME).so.1)
	(cd $(JAVAEXTLIBPATH) && ln -sf $(LIBBASENAME).so.1 $(LIBBASENAME).so)

.PHONY: install-link
install-link:
	mkdir -p $(JAVAEXTLIBPATH)
	(cd  $(JAVAEXTLIBPATH) && $(LN_S) $(LIBDEST_ABSOLUTE))
	(cd $(JAVAEXTLIBPATH) && ln -sf $(LIBBASENAME).so.1.0 $(LIBBASENAME).so.1)
	(cd $(JAVAEXTLIBPATH) && ln -sf $(LIBBASENAME).so.1 $(LIBBASENAME).so)

.PHONY: clean
clean:
	rm -rf $(LIBDEST) $(JNIHEADERS) *.o *~
