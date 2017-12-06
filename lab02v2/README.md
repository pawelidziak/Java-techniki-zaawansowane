# Lab 02 - Custom Class Loader
Paweł Idziak

## Info
Napisz program pozwalający załadować własnym ładowaczem klas klasy posiadające metodę o wskazanej sygnaturze oraz uruchomić tę metodę dla każdego znalezionego przypadku. 

Założenia:
1. kod bajtowy ładowanych klas powinien być umieszczany w dedykowanym do tego celu katalogu.
2. przynajmniej jedna z ładowanych klas powinna być zależna od innej ładowanej klasy (chodzi o przetestowanie resolve).
3. we wskazanej sygnaturze metody oprócz nazwy mogą pojawić się typy podstawowe, typ String oraz tablice jednowymiarowe typów podstawowych i typu String
4. na interfejsie programu powinny pojawiać się załadowane klasy oraz wszystkie oferowane przez nie metody (z metodami o wskazanych interfejsach).
4. w ładowanych klasach powinien istnieć konstruktor bezargumentowy.
5. załadowane klasy powinno dać się wyładować (czego potwierdzenie powinno być widoczne w jconsole).
