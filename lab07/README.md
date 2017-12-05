# Lab 07 - MBean / MXBean
Paweł Idziak

## Info
Napisz program, który będzie symulował działanie systemu biletowego stosowanego w obsłudze klientów. Program ma generować kolejne numery dla zadanych kategorii spraw. Niech lista kategorii spraw będzie parametrem programu. Niech algorytm generowania kolejnego numeru będzie konfigurowany (na zasadzie przyznawania priorytetów). 

Zanurz w programie agenta obsługującego ziarenko, które pozwali na zmianę parametrów programu (listy kategorii spraw oraz priorytetów). Ziarenko to powinno również generować notyfikacje przy każdej zmianie parametrów inicjowanej przez użytkownika aplikacji (nie powinno tych notyfikacji wysyłać, jeśli zmiana parametrów odbywać się będzie przez nie same).
Podsumowując, ziarenko powinno posiadać/obsługiwać:
- właściwość (pozwalającą na ustawianie/odczytywanie kategorii spraw),
- metodę zmieniającą priorytety spraw,
- notyfikację.

