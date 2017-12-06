# Lab 06 - JJS
Paweł Idziak

## Polecenie
Napisz program w języku JS interpretowanym przez silnik Nashorn, który wyświetli okienko z jakimś obrazkiem. Użytkownik aplikacji powinien mieć możliwość wybrania algorytmu, jakim wyświetlany obrazek zostanie przetworzony. Okienko, obrazek i inne elementy interfejsu użytkownika powinny być klasami Javy (czyli w skrypcie JS trzeba odwołać się do Javowych klas i obiektów). Wspomniane algorytmy przetwarzania powinny być napisane w języku JS i przechowywane w plikach (nazwa pliku może pojawiać się na interfejsie użytkownika jako nazwa wybieranego przez niego algorytmu).
Algorytmy te powinny przyjmować postać funkcji operujących na obiektach Javy (przekazanych z kodu aplikacji stworzonego w JS).

http://www.oracle.com/technetwork/articles/java/jf14-nashorn-2126515.html
https://www.n-k.de/riding-the-nashorn/



## Info
Aplikacja przedstawia połączenie Javy z JavaScript. Aplikacja startuje z skryptu JS, który wywołuje kalasę Javy odpowiedzialną za wyświetlenia okienka. W okienku mamy prosty edytor HTML. Metody zmieniające tekst odpowiadają skryptom napisanym w JS. Po kliknięciu w "STYLE!" z poziomy klasy Javy zostaje wywołany skrypt JS. 
