#### Transformer
#### Version `1.0.0`
---

#### Description
Cette librairie permet de simplifier le parsing de JSON retournés par l'API REST. Son utilisation vous permet de gagner du temps dans la conversion d'un JSONObject ou JSONArray en un modèle défini.

### Installation

* Ouvrez le projet dans lequel vous souhaitez utiliser le librairie,
* Ouvrez le fichier build.gradle de votre projet (pas celui de l'application),
* Ajoutez-y (si ce n'est pas déjà fait) le code suivant :

```ruby
allprojects {
    repositories {
        jcenter()
        maven{ url "https://jitpack.io" }
    }
}
```

* Ouvrez ensuite le build.gradle de votre application
* Ajoutez la dépendance suivante :
```ruby
compile 'com.github.soyhuce:transformer:1.0.0'
```

### Utilisation

Une fois ajoutée au projet, pour utiliser cette librarie, vous devez créer une classe héritant de "Transformer" puis implémenter les méthodes requises :

Exemple avec une classe custom "User" : 

```java
public class UserTransformer extends Transformer<User> {
    
    @Override
    public User getItemFromJSONObject(JSONObject jsonObject) throws JSONException {
        User user = new User();
        user.setId(jsonObject.getInt("id"));
        user.setLastname(jsonObject.getString("last_name"));

        //!\\ firstname => optional --> getOptionalString()
        user.setFirstname(getOptionalString(jsonObject, "first_name"));

        user.setPremium(jsonObject.getBoolean("is_premium"));
        return user;    
    }
}
```
L'attribut "first_name" de l'exemple ci-dessus peut-être **null**, d'où l'utilisation de **getOptionalString()**

Vous pourrez ensuite l'utiliser de la manière suivante : 

```java
Transformer<User> transformer = new UserTransformer()
User user = transformer.getItemFromJSONObject(userJSONObject);
```
