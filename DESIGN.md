# Bro-Cook

### Sketches UI
![alt text](https://github.com/tomdekr/project-01/blob/master/doc/sketchui-final.png)

#### Screen #1 (LoginActivity)
Dit is het login scherm van de applicatie en eveneens de LoginActivity, waar naarmate de gebruiker een account heeft en is ingelogd doorgestuurd wordt naar de volgende activity.

Benodige functies:
- Login 
- Signup
- Button click functie

#### Screen #2 (MainActivity)
Op de tweede schets zie je de MainActivity van de applicatie. Hier is het mogelijk om te zoeken naar recepten gebaseerd op ingredienten en allergien. De ingevulde tekst wordt gebruikt als parameters om een GET request te doen op de Yummly API. Op dit scherm/activiteit is het mogelijk om naar de gebruiker zijn eigen profiel of groep te gaan. Door te klikken op 'Create' wordt de GET request uitgevoerd en zal de user doorgestuurd worden naar scherm 3.

Benodige functies:
- Functie voor de API GET Request
- Functie voor het checken van de user input


#### Screen #3 (RecipeImageActivity)
Vervolgens gaat de gebruiker naar de RecipeImageActivity hierbij worden de afbeeldingen van recepten weergegeven die gebaseerd zijn op de ingevulde parameters van de gebruiker in de vorige activiteit. Door te klikken op een afbeelding van een recept zal de gebruiker door gestuurd worden naar scherm 4.

Benodige functies:
- Functie voor de API Get Request (na klikken op afbeelding).
- Functie voor het alleen weergeven van de afbeeldingen van recepten.

#### Screen #4 (RecipeInfoActivity)
Op scherm 4 wordt de informatie weergeven van het gekozen recept (op scherm 3). Dit is wederom met een GET Request op de Yummly API met de id als parameter van het gekozen recept. Verder is het mogelijk om het recept op te slaan naar je _Favorieten_ door te klikken op het plusje.

Benodige functies:
- Functie voor het inladen van de juiste id van een recept & afbeelding.
- Functie voor het recept kunnen toevoegen aan persoonlijke favorieten.

#### Screen #5 (ProfileActivity)
Scherm 5 is de profielpagina van de gebruiker. Hier kan hij/zij een profielfoto instellen en zijn username/email instellen of aanpassen. Verder kan de gebruiker naar zijn favoriete recepten die hij/zij heeft toegevoegd via het klikken op het plusje (op scherm 4).
Daarnaast kan de gebruiker naar een activiteit gaan waar alle gebruikers worden weergeven om vrienden toe te kunnen voegen.

Benodige functies:
- Functie voor het (eventueel) updaten van user info als username & email.
- Functie voor het uploaden van profiel foto.

#### Screen #6 (GroupRecipeActivity)
Via scherm 2 kan de gebruiker klikken het groepsicontje waardoor de gebruiker doorgestuurd wordt naar scherm 6. Hier worden de recepten van de bro's weergeven wat iedereen heeft gegeten de afgelopen week. Hier wordt gebruik gemaakt van een request op de _Firebase database_ om de opgeslagen recepten te weergeven.

Benodige functies:
- Functie voor het inladen van de juiste recepten van de database.

#### Screen #7 (AllUsersActivity)
Op Scherm 7 wordt er weergeven welke users de app gebruiken, hierbij kan de gebruiker filteren in de database en zoeken naar de juiste vrienden. Vervolgens kan een user toegevoegd worden als vriend.

Benodige functies:
- Functie voor het kunnen filteren van de zoekresultaten van users.
- Functie voor het kunnen inladen van alle users van de database.

#### Screen #8 (FriendsActivity)
Op dit scherm worden alle vrienden weergeven die op scherm 7 zijn toegevoegd. Dit zijn de vrienden die je kan bekijken of kan toevoegen aan je groep.

Benodige functies:
- Functie voor het inladen van de users die toegevoegd zijn als 'follow'/vrienden.
- Functie voor het kunnen toevoegen tot groep die men wilt volgen

#### Screen #9 (FavoriteRecipesActivity)
Dit scherm is voor de recepten die je persoonlijk zelf hebt opgeslagen als favoriet. Hier kan je vervolgens de recepten aan je groep toevoegen aan de groepsreceptenlijst. 

Benodige functies:
- Functie voor het inladen van de persoonlijke favoriete recepten van de database.
- Functie voor het kunnen toevoegen van de recepten tot de groepsreceptenlijst.


#### Classes:
- User
- Recipes

#### Database:
In de Firebase database worden de volgende dingen opgeslagen:
- Username 
- Email
- Password
- Recepten ( + alle info )
- Groepdeelname

Per username wordt er volgens opgeslagen welke recepten als favorieten zijn genoteerd en wat voor info die bevatten zodat de recepten makkelijk terug gevonden kunnen worden. Ook wordt er per username de groep waar hij of zij in zit opgeslagen zodat de juiste informatie van de juiste groep uit de database weergeven wordt.

Per groep wordt er opgeslagen welke recepten zijn opgeslagen. Dit is mogelijk doordat de favorieten recepten die op de FavoriteRecipeActivity per groepsdeelnemer toegevoegd kan worden aan de groepsrecepten.

