# Bro-Cook

### Sketches UI
![alt text](https://github.com/tomdekr/project-01/blob/master/doc/sketchui-final.png)

#### Screen #1 (LoginActivity)
Dit is het login scherm van de applicatie en eveneens de LoginActivity, waar naarmate de gebruiker een account heeft en is ingelogd doorgestuurd wordt naar de volgende activity.

Benodige functies:
- Login 
- Signup

#### Screen #2 (MainActivity)
Op de tweede schets zie je de MainActivity van de applicatie. Hier is het mogelijk om te zoeken naar recepten gebaseerd op ingredienten en allergien. De ingevulde tekst wordt gebruikt als parameters om een GET request te doen op de Yummly API. Op dit scherm/activiteit is het mogelijk om naar de gebruiker zijn eigen profiel of groep te gaan. Door te klikken op 'Create' wordt de GET request uitgevoerd en zal de user doorgestuurd worden naar scherm 3.

#### Screen #3 (RecipeImageActivity)
Vervolgens gaat de gebruiker naar de RecipeImageActivity hierbij worden de afbeeldingen van recepten weergegeven die gebaseerd zijn op de ingevulde parameters van de gebruiker in de vorige activiteit. Door te klikken op een afbeelding van een recept zal de gebruiker door gestuurd worden naar scherm 4.

#### Screen #4 (RecipeInfoActivity)
Op scherm 4 wordt de informatie weergeven van het gekozen recept (op scherm 3). Dit is wederom met een GET Request op de Yummly API met de id als parameter van het gekozen recept. Verder is het mogelijk om het recept op te slaan naar je _Favorieten_ door te klikken op het plusje.

#### Screen #5 (ProfileActivity)
Scherm 5 is de profielpagina van de gebruiker. Hier kan hij/zij een profielfoto instellen en zijn username/email instellen of aanpassen. Verder kan de gebruiker naar zijn favoriete recepten die hij/zij heeft toegevoegd via het klikken op het plusje (op scherm 4).
Daarnaast kan de gebruiker naar een activiteit gaan waar alle gebruikers worden weergeven om vrienden toe te kunnen voegen.

#### Screen #6 (GroupRecipeActivity)
Via scherm 2 kan de gebruiker klikken het groepsicontje waardoor de gebruiker doorgestuurd wordt naar scherm 6. Hier worden de recepten van de bro's weergeven wat iedereen heeft gegeten de afgelopen week. Hier wordt gebruik gemaakt van een request op de _Firebase database_ om de opgeslagen recepten te weergeven.

#### Screen #7 (AllUsersActivity)
Op Scherm 7 wordt er weergeven welke users de app gebruiken, hierbij kan de gebruiker filteren in de database en zoeken naar de juiste vrienden. Vervolgens kan een user toegevoegd worden als vriend.

#### Screen #8 (FriendsActivity)


#### Screen #9 (FavoriteRecipesActivity)


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

