# Lab 04 - Tic Tac Toe

## Info
Zadanie polegało na implementacji gry w kółko i krzyżyk z użyciem adnotacji. Klasy (TTLLogic) wraz z metodami odpowiedzialnymi za logikę gry w kółko i krzyżyk posiadają odpowiednie adnotacje: Strategia (dla klasy), Poziom (dla metody). Natomiast klasy odpowiadające za interakcję z użytkownikiem (lab04FX) wczytują klasy z logiką gry za pomocą URLClassLoader'a, rozpoznają odpowiednie adnotacje oraz wywołują metodę z daną adnotacją.

# Strategie i poziomy

### DEFENSE:

#### EASY:
    zaczyna od (0,0) i leci po kolei (wiersze, pozniej kolumny) jak pole jest wolne to zwraca ten punkt

#### MID: 
	randomowo strzela po planszy, jak jest wolne to zwraca ten punkt

#### HARD: 
	wybiera pierwszy znaleziony punkt gracza i próbuje go zablokować w kolejności D, G, L, P.


### ATTACK:

#### EASY:
	zaczyna od (0,0) i leci po kolei (wiersze, pozniej kolumny). jesli w danym wierszu jest pole przeciwnika to zwieksza poziom kolumny
	
#### MID:
	wybiera punkt na srodku (mniej wiecej), jesli jest pusty to go zwraca, jesli nie to leci: wiersz, kolumna

#### HARD:
	wybiera punkt na srodku (mniej wiecej), jesli jest pusty to go zwraca, jesli nie to: wybiera pierwszy znaleziony punkt komputera i próbuje go rozwinąć w kolejności D, G, L, P.
