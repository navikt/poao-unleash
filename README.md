# poao-unleash

Felles tjeneste for å gjøre kall mot unleash med tilpassede strategier

## Strategier
Tjenesten støtter følgende strategier

### byCluster
byCluster-strategien kan brukes for å skru på funksjonalitet for et gitt cluster. Feks. `dev-gcp`

### userWithId
userWithId-strategien kan brukes for å gi en eller flere brukeridenter tilgang til funksjonalitet.

### byEnhet
byEnhet-strategien brukes for å skru på funksjonalitet for en gitt enhet med `fagområdet/tema = oppfølging (OPP)`. Feks vil verdien 0106 skru på funksjonalitet for alle i NAV-enheten Fredrikstad som har fagområdet `OPP` (oppfølging).