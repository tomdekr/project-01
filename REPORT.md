# Final Project Report
## Korte omschrijving

In mijn app is het mogelijk om recepten te zoeken gebaseerd op 1 of meerdere ingredienten en voedselallergieen. De gebruiker krijgt een resultaat van gevonden recepten te zien gebaseerd op de ingevoerde ingredienten. Van dit recept is de stappenplan te bekijken. De user kan het recept op slaan als persoonlijk favoriet. Verder is het mogelijk om als user een groepnaam aan tegeven, hierbij kunnen meerdere mensen in dezelfde groep (/huishouden) recepten toevoegen aan de groepslijst en dit vervolgens weergeven.

<img src="https://github.com/tomdekr/project-01/blob/master/doc/Naamloos-1.png" width="436" height="768" />

### Clearly describe the technical design: how is the functionality implemented in your code? This should be like your DESIGN.md but updated to reflect the final application. First, give a high level overview, which helps us navigate and understand the total of your code (which components are there?). Second, go into detail, and describe the modules/classes (apps) files/functions (data) and how they relate.

# Technical design
## Omschrijving:
- *LoginActivity.java :* De functionaliteit van de login activity klinkt vanzelfsprekend, de user kan op deze activity inloggen met zijn of haar gegevens. 
- *SignupActivity.java :* Op deze activity kan de user zich registreren mits hij of zij dat nog niet heeft gedaan.
- *MainActivity.java :* Deze class functioneert als de kern van de applicatie. Op de MainActivity kan de user ingredienten invullen op de editText om met een druk op de 'Search' button het uit te voeren. Als de parameters zijn ingevuld dan zal de samengestelde input van  parameters opgeslagen worden en worden ze meegegeven naar de RecipeImageActitvity om de JSON Response te parse'n. Daarnaast kan de user navigeren naar zijn of haar profiel, favorieten en groepsfavorieten. 
- *RecipeImageActivity.java :* Hiernaartoe worden de parameters voor de url vanuit MainActivity met een intent meegegeven. Met de methode ParseJSON wordt de url request aangemaakt en zal de benodigde data als; id, imageurl, name & rating opgehaald worden. 
Vervolgens worden de waardes per recept opgeslagen in een instance van de class Recipe. Deze instances van Recipe worden daarmee toegevoegd aan een Arraylist en vastgezet aan de Adapter. Hiermee wordt vervolgens de RecyclerView aangemaakt en worden de recepten weergegeven op de pagina met de benodigde data. Tot slot is er een onItemClickListener die ervoor zorgt dat de recepten clickable zijn en bij een onItemClick de juiste id wordt meegegeven voor de RecipeDetailActivity.
- *RecipeDetailActivity :* Op deze activity wordt er een pagina genereeerd op de meegegeven waarde van de id van een recept. Zo'n id kan ontvangen worden van verschillende activiteiten namelijk: RecipeImage, FavoriteList en GroupList. Eerst zal er dus gecheckt moeten worden waar de id vandaan komt door de juiste intent te gebruiken. Als dat gedaan is wordt de parseJSON method uitgevoerd en worden de benodige data als: name, preparation time, ingedrients, imageUrl & sourceUrl in de verschillende Views neergezet. Op de pagina zelf kan door de method addToFavorite() een recept toegevoegd worden tot de persoonlijke favorieten door op het zwarte hartje te klikken, als dit gebeurt verandert hij vervolgens in een rood hartje. Het recept wordt verwijderd door nog een keer op het hartje te klikken, wat het hartje weer zwart maakt. Ook kan een recept toegevoegd worden aan de groepslijst door de method addtoGroupFavorites(), dit wordt uitgevoerd door op de 'Group' button te klikken. Door een longClick uit te voeren kan een recept uit de groepslijst worden gehaald.
Verder is er de mogelijkheid om op de button 'Recipe' te klikken en kan een user hiermee naar de WebView activity gaan.
- *Recipe.java :*
- *Adapter.java :*  
- *ProfileActivity.java :*



[![BCH compliance](https://bettercodehub.com/edge/badge/tomdekr/project-01?branch=master)](https://bettercodehub.com/)
