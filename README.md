#  Dobrita George-Madalin - 324CAb
##  Tema1 - POO - VideosDB

In rezolvarea problemei am folosit TreeMap daca am avut nevoie de anumite date ordonate dupa nume, nu numai dupa valoare si LinkedHashMap daca am avut nevoie ca datele sa fie ordonate dupa ordinea de aparitie in baza de date.

### Comenzile:
Implementate prin functii statice in clasele Command si CommandOperations.
Din lista de user il caut pe acela cu username-ul dat ca parametru fiecarei functii. In final, functiile returneaza un mesaj de success sau de eroare care este afisat impreuna cu id-ul actiunii in output.
#### Favorite:
Daca in lista de filme favorite a unui user exista deja filmul ce trebuie adaugat la favorite se afiseaza un mesaj de eroare, in caz contrar adaug filmul in lista de favorite a userului si se returneaza un mesaj de succes.
#### View:
Daca in istoricul unui user exista deja un filmul ce trebuie adaugat in istoric, se incrementeaza numarul de view-uri al filmului, in caz contrat se adauga filmul in istoric cu 1 view si se returneaza un mesaj de success in ambele cazuri
#### Rating:
##### Movie:
Daca user-ul a dat deja rating la film sau filmul nu este in istoricul userului se afiseaza un mesaj de eroare, altfel se adauga user-ul si rating-ul in istoricul rating-urilor filmului respectiv si se afiseaza un mesaj de succes.
##### Serial:
Daca user-ul a dat deja rating la un sezon din acel serial sau serialul nu este in istoricul userului se afiseaza un mesaj de eroare, altfel se adauga user-ul si rating-ul in istoricul rating-urilor sezonului respectiv al serialului si se afiseaza un mesaj de succes.
            
### Query-uri:
Implementate prin functii statice in clasele Query si QueryOperations.
In final, functiile returneaza un mesaj de forma "Query result :[*list*]", unde *list* este o lista(goala sau nu) de actori/filme/seriale/useri din care se aleg doar un anumit numar de entry-uri pentru a fi afisate in ordine crescatoare sau descrescatoare.
#### Actori:
##### Average:
Se creeaza un TreeMap sortat dupa rating-uri care contine actori si rating-urile lor, iar *list* este lista cheilor din Map.
##### Awards:
Se creeaza un TreeMap sortat dupa numarul total de premii care contine actori si o clasa cu o lista de premii si numarul total de premii, iar *list* este lista cheilor din Map.
##### FilterDescription:
Se creeaza un *list* format din numele actorilor pentru care toate cuvintele date ca parametru se gasesc in descrierea actorului(CASE INSENSITIVE).
#### Filme/Seriale: Rating/Favorite/Longest/MostViewed
Se creeaza un TreeMap sortat dupa valoare care are numele unui film/serial dintr-un anumit an si dintr-un anumit gen drept cheie si ca valoare rating-ul filmului/numarul de aparitii in lista de favorite a useriilor/durata totala/numarul de view-uri, iar *list* este lista cheilor din Map.
#### Useri: Ratings
Se creeaza un TreeMap sortat dupa valoare ce contine username-ul si numarul de rating-uri ale fiecarui user, iar *list* este lista cheilor din Map.
            
### Recomandari:
Implementare prin functii statice in clasele Recommendation si RecommendationOperations.
In final, functiile returneaza un mesaj de for "XRecommendation result: [*video*]" sau "XRecommendation cannot be applied!" pentru primele 4 recomandari si "XRecommendation result: [*list*]", unde *list* este o lista(goala sau nu) de filme si seriale returnat pentru ultima recomandare(iar X este strategia de recomandare).
#### Standard:
Se returneaza primul video nevazut din baza de date pentru un anumit user.
#### BestUnseen:
Se creeaza un LinkedHashMap sortat dupa rating descrescator de nume de filme si seriale nevazute de user si rating-ul corespunzator si se afiseaza primul video din lista cheilor, daca aceasta nu este goala.
#### PREMIUM:
##### Popular:
Se creeaza un TreeMap sortat descrescator dupa valoare ce contine fiecare gen si numarul de view-uri al acestuia. Se afiseaza primul video nevazut din primul gen in care exista cel putin un video nevazut de catre user.
##### Favorite:
Se creeaza un LinkedHashMap sortat descrecator dupa valoare ce contine fiecare film si serial si numarul de dati in care apare in listele de filme favorite ale userilor si se afiseaza primul video nevazut.
##### Search:
Se creeaza un TreeMap cu toate filmele si serialele dintr-un gen fiind ordonate crescator dupa rating, iar *list* este multimea de chei din Map pentru care filmul este nevazut.
