# Lab 08 - JAXB
Paweł Idziak

## Info
Napisz program pozwalający edytować i wyświetlać "ostylowane" oferty składane na giełdzie sprzętu narciarskiego. Implementacja powinna bazować na następujących założeniach:
1. Oferty powinny być zapisywane w plikach XML zgodnych z zaprojektowanym schematem XSD reprezentującym model oferty (numer oferty, typ sprzętu, opis sprzętu, cena, lokalizacja, dane sprzedającego, ważność oferty itp.).
2. Ze schematu XSD powinny być wygenerowane klasy Java pozwalające na zapis/odczyt danych do/z plików XML (klasy JAXB).
3. Aplikacja powinna umożliwić edytowanie, aktualizację oraz zapis ofert. Katalogiem składowania ofert (w formie plików XML o nazwach zawierających numer oferty) powinien być katalog "oferty".
4. W katalogu "transformacje" powinny być składowane arkusze XSLT. Arkusze styli mogą być edytowane za pomocą zewnętrznych narzędzimogą. Trafiają do katalogu "transformacje" w dowolnym momencie.
5. Aplikacja powinna umożliwić użytkownikowi wybór arkusza styli, jakimi można przetworzyć pliki XML danych. Efektem takiego przetwarzania powinien być dokument HTML wyświetlany w aplikacji, zawierający odpowiednio sformatowaną tabelkę.
