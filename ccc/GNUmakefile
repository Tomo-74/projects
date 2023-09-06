prog?=$(basename $(notdir $(PWD)))
objs:=$(addsuffix .o,$(basename $(wildcard *.c)))

defines+=-D_GNU_SOURCE
ccflags+=-g -Wall -MMD $(defines)
ldflags+=-g -Wl,-Map=$@.map

.SUFFIXES:

%.o: %.c ; gcc -o $@ -c $< $(ccflags)
%.i: %.c ; gcc -o $@ -E $< $(defines)
%.s: %.c ; gcc -o $@ -S $< $(defines)
%.l: %.c ; gcc -c $< -Wa,-adhlns=$@ -fverbose-asm $(defines)

$(prog): $(objs) ; gcc -o $@ $^ $(ldflags)

.PHONY: clean run valgrind

clean: ; rm -f $(prog) *.o *.d *.[isl] *.map

test:=echo -e "Hello\nworld!" |

run:      $(prog) ; $(test) ./$< $(args)
valgrind: $(prog) ; $(test) $@ --leak-check=full ./$< $(args)

sinclude *.d
