# Generated automatically from Makefile.in by configure.
# compilation and packing of the native library for esd
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
vpath %.class $(OBJDIR)/org/tritonus/lowlevel/esd

LIBBASENAME=libesdtritonus
LIBDEST=$(LIBBASENAME).so.1.0

JAVAH = javah -force -classpath $(OBJDIR)


OBJ=\
	org_tritonus_lowlevel_esd_EsdRecordingStream.o \
	org_tritonus_lowlevel_esd_EsdStream.o \
	org_tritonus_lowlevel_esd_EsdSample.o

JNICLASSFILES=\
	EsdRecordingStream.class \
	EsdStream.class \
	EsdSample.class

JNIHEADERS:=$(OBJ:.o=.h)

JNICLASSES=\
	org.tritonus.lowlevel.esd.EsdRecordingStream \
	org.tritonus.lowlevel.esd.EsdStream \
	org.tritonus.lowlevel.esd.EsdSample

#----------------- rules -------------------

.PHONY: all
all: $(LIBDEST)

$(LIBDEST):	$(OBJ)
	$(CC) -shared -Wl,-soname,$(LIBBASENAME).so.1 -o $(LIBDEST) $(OBJ) -lesd
	-ln -sf $(LIBBASENAME).so.1.0 $(LIBBASENAME).so.1
	-ln -sf $(LIBBASENAME).so.1 $(LIBBASENAME).so

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
