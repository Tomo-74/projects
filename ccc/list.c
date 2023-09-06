#include <stdio.h>
#include <stdlib.h>

#include "list.h"
#include "error.h"

typedef struct {
  List car;	// value of node
  List cdr;	// pointer to next node
} *Pair;

extern List cons(List car, List cdr) {
  Pair p=(Pair)malloc(sizeof(*p));
  if (!p)
    ERROR("malloc() failed.");
  p->car=car;
  p->cdr=cdr;
  return p;
}

extern List car(List list) {
  return ((Pair)list)->car;
}

extern List cdr(List list) {
  return ((Pair)list)->cdr;
}

extern void freelist(List list) {
  if (list) {
    freelist(((Pair)list)->cdr);
    free(list);
  }
}

extern void freedata(List list) {
  if (list) {
    freedata(((Pair)list)->cdr);
    free(((Pair)list)->car);
    free(list);
  }
}
