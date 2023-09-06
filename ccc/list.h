#ifndef LIST_H
#define LIST_H

typedef void *List;

extern List cons(List car, List cdr);
extern List car(List list);
extern List cdr(List list);
extern void freelist(List list);
extern void freedata(List list);

#endif
