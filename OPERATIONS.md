# Betriebsanleitung für RocketVoIP

## RocketVoIP Backend

### Vorbedingungen

* Java 1.8
* PostgreSQL 9.x
* Maven 3
* Git

Um das Backend von RocketVoIP zu betreiben, muss zuerst Java in der Version 
1.8 installiert sein.
Des weiteren muss eine PostgreSQL-Datenbank eingerichtet und für den Zugriff
vom Server, auf welchem RocketVoIP laufen soll konfiguriert sein.

### Builden
Zuerst muss der Sourcecode von github.com ausgecheckt werden. Dies geschieht 
mit folgendem Befehl:
```
git clone https://github.com/rocketvoip/rocketvoip.git
```
Um dann ein ausführbares .jar-File zu erhalten, ist ein Maven-Build nötig.
Dieser wird mit folgendem Befehl (innerhalb des Sourcecode-Verzeichnisses) 
angestossen:
```
mvn clean package
```
Im Verzeichniss `target`, kann dann das benötigte .jar-File gefunden werden.

### Starten

RocketVoIP benötigt keine Installation, es kann einfach das .jar-File 
ausgeführt werden.

Dies muss folgendermassen geschehen:

```
DATABASE_URL="postgres://USERNAME:PASSWORD@HOST:PORT/DATABASE" java -jar ROCKETVOIP.jar
```

RocketVoIP ist dann auf dem entsprechenden System, unter dem Port 8080 erreichbar (`http://<SERVER>:8080/v1`)

## RocketVoIP-Frontend

### Vorbedingungen

* node.js (min. 4.x)
* npm v3
* git

Auch das Frontend muss zuerst von github.com ausgecheckt werden:

```
git clone https://github.com/rocketvoip/rocketvoip-frontend.git
```

Die restlichen Abhängigkeiten können dann via `npm` installiert werden:

```
npm install
```

### Starten
Die node.js Anwendung kann dann ohne weiteres mittels dem web.js gestartet 
werden. Dies geschieht durch Ausführen des folgenden Befehls, innerhalb 
des RocketVoIP-Frontend Verzeichnisses:

```
BACKEND_URL="https://<URL-OF-BACKEND>/" node web.js
```

Das Frontend höhrt standardmässig auf den Port 5000 -> `http://<SERVER>:5000/app/`

## Asterisk-Server

### Vorbedingungen

* Asterisk 13
* Cron
* cURL

Asterisk und Cron müssen auf dem System vorhanden sein, auf welchem der 
VoIP-Server, mit der durch RocketVoIP erstellten Konfiguration, betrieben 
werden soll.    

Folgende Konfigurationen von Asterisk sollten vor der ersten Verwendung 
des unten angegeben Scripts entfernt werden:

* `sip.conf`
* `extensions.conf`

### Cron
Folgendes Script kann verwendet werden, um durch einen Cronjob ausgeführt 
zu werden, damit die durch RocketVoIP erstellte Konfiguration automatisch 
übernommen wird:

```
# Vars
ASTERISK_CONFIG_PATH="/opt/asterisk/etc/asterisk"
DOWNLOAD_FOLDER="/opt/asterisk/etc/asterisk/downloads"
API_BASE_URL="https://rocketvoip-staging.herokuapp.com/v1/"
API_PATH="${API_BASE_URL}configuration/zip"
API_USER="<USERNAME>"
API_PASS="<PASSWORD>"
DATE=$(date +%Y%m%d)
EXPIRE_DAYS="10"

# Code

# Get Auth Token
AUTH=$(/usr/bin/curl ${API_BASE_URL}login -XPOST -d "{\"username\": \"${API_USER}\", \"password\": \"${API_PASS}\"}" -i -H "Content-Type: application/json;charset=UTF-8" | grep X-Auth-Token | sed 's/\r//')
# Download config
/usr/bin/curl -H "${AUTH}" ${API_PATH} -o "${DOWNLOAD_FOLDER}/$DATE.zip"
if [ "$?" -ne "0" ]; then
  exit 0;
fi

# Remove old configs
/usr/bin/find ${DOWNLOAD_FOLDER}* -mtime +${EXPIRE_DAYS} -exec rm {} \;

# Unpack config & write to asterisk config path
/usr/bin/unzip -o "${DOWNLOAD_FOLDER}/$DATE.zip" -d $ASTERISK_CONFIG_PATH

/opt/asterisk/sbin/asterisk -rx "reload"
```
