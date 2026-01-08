Felhasználói kézikönyv
---
A program egy könyvtárkezelő alkalmazás, amely lehetővé teszi a felhasználók számára:
Könyvek hozzáadását, törlését, szerkesztését, szűrését és a könyvtár elemeinek betöltését fájlból. Rendelések kezelését, beleértve új könyvek rendelését, meglévő könyvek kiválasztását, a rendelések megtekintését. Könyvek és rendelések mentését fájlba, az adott felhasználó adatainak megjelenítését, továbbá a felhasználói adatok kezelését, adminisztrációs jogokkal.

Indításkor megjelenik egy bejelentkezési képernyő: Meg kell adni a felhasználónevet és a jelszót (A felhasználói adatok a users.txt fájlban találhatók.)
A rendszer adminisztrátor felhasználója edina, érdemes így belépni, ugyanis egy-két funkcióhoz később is szükséges lesz az adminisztrátor jelszavára. Ha helytelenek az adatok a program nem enged be.

Főmenü funkciói
---
File menü
--
Load from File: Könyvek betöltése TXT fájlból.
Load from JSON: Könyvek betöltése JSON fájlból.
Save to JSON: Könyvek mentése JSON fájlba.

Books menü
--
Add Book: Új könyv hozzáadása.
Új könyv hozzáadása esetén töltsd ki a cím, szerző, év (szám) és műfaj mezőket.
Egy könyv egy-két adata (az év kivételével) lehet üres, amelyet később tudunk módosítani.
Delete Book: Egy kiválasztott könyv törlése a listából.
Filter Books: Könyvek keresése cím, szerző, műfaj vagy év alapján.
Szűrés után a könyvek listája frissül. A frissített listából vissza tudunk lépni az eredeti táblázatba a reset table menüparanccsal, illetve úgy is, ha a felugró ablakban azt az opciót választjuk.
Edit Book: Egy kiválasztott könyv adatait módosíthatod.	

Orders menü
--
View Orders: Az összes rendelés megtekintése táblázatos formában.
Save Orders to JSON: Rendelések mentése JSON fájlba.
Order New Book: Egy olyan könyv rendelését teszi lehetővé, amely nem szerepel az adatbázisban.
Order Book from the table: A táblázatban szereplő könyvek közül lehet választani és leadni a rendelést.

User Management (csak az adminisztrátor felhasználónak)
--
Ezt a felületet csak az adminisztrátor felhasználó (edina) tudja használni, ugyanis, bármely ez alatti fül használatához szükséges megadni az adminisztrátor jelszavát.
Add User: Új felhasználó hozzáadása a rendszerhez.
Delete User: Felhasználó törlése.
Modify User: Egy meglévő felhasználó adatainak módosítása.

My Account
--
View account details: Jelenlegi felhasználó adatainak megjelenítésére használhatjuk.

Exit
--
Exit now: Kilépés a programból.
Cancel: Ha mégsem szeretnénk kilépni.

