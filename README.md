# Licitatie
Partea de back-end a Proiectului Colictiv cu tema "Licitatie"

Orice pull request trebuie, pe langa functionalitate, sa aiba si testele aferente (atat unittest cat si integration test).

Iteratii:

-    Functionalitate de login, sign-up si verificare cont prin mail 
-    Pagina principala unde se pot vedea ultimele licitatii adaugate 
-    Creerea paginii utilizatorului 
-    Posibilitatea de a adauga credite in cont 
-    Cautarea dupa nume, locati (separat si impreuna) 
-    Creerea licitatiei, vizualizarea unei licitatii (descriere, imagini, comentarii, suma curenta si posibilitatea de a licita) 
-    Creerea de comentarii la licitatii. 
-    Inchiderea de licitatie (lucru care sa se reflecte in pagina acesteia) 
-    Adaugarea ratingului la un utilizator, daca scade sub 3 nu mai poate creea licitatii, daca scade sub 2 I se va bloca contul temporar 
 
Some rules:

- Numele branchului sa contina id-ul taskului din trello (id-ul poate fi gasit in URL-ul din trello in momentul in care deschideti pentru vizualizare detalii). E.g.: (1)_log_in
- Un branch sa nu rezolve mai mult de un task de acolo.
- Daca un task vi se pare prea mare, sau ati reusit sa realizati o parte din el (CARE SE POATE INTEGRA IN PROIECT) sa anuntati acest lucru in descrierea taskului.
- Fiecare functionalitate nou adaugata, metoda, etc (cu exceptia gett-erelor si setterelor) sa fie insotite de comentatii care sa explice se face si cum(stanging the obvious sometimes, but still), si de teste.
- De preferat fiecare branch sa porneasca din master pentru a evita pe cat posibil conflictele
- Daca gasiti o problema in cod, semnalati-o in Trello, sa stim toti despre ce e vorba, pentru a avita conflictele si sa lucreze 5 persoane pe o linie.
- Sa se faca code review la fiecare pull request, fara un numar de approve-uri nu se va merge-ui
