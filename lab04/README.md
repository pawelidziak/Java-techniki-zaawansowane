## DEFENSE:

### EASY:
    zaczyna od (0,0) i leci po kolei (wiersze, pozniej kolumny) jak pole jest wolne to zwraca ten punkt

### MID: 
	randomowo strzela po planszy, jak jest wolne to zwraca ten punkt

### HARD: 
	wybiera pierwszy znaleziony punkt gracza i próbuje go zablokować w kolejności D, G, L, P.


## ATTACK:

### EASY:
	zaczyna od (0,0) i leci po kolei (wiersze, pozniej kolumny). jesli w danym wierszu jest pole przeciwnika to zwieksza poziom kolumny
	
### MID:
	wybiera punkt na srodku (mniej wiecej), jesli jest pusty to go zwraca, jesli nie to leci: wiersz, kolumna

### HARD:
	wybiera punkt na srodku (mniej wiecej), jesli jest pusty to go zwraca, jesli nie to: wybiera pierwszy znaleziony punkt komputera i próbuje go rozwinąć w kolejności D, G, L, P.
