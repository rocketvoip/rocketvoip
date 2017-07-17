#!/bin/bash
#################
# Script to get the asterisk config from an API endpoint
#
# Author: Martin Wittwer; wittwma1@students.zhaw.ch
#################
#
# Params
# -
#################

# Vars
ASTERISK_CONFIG_PATH="/opt/asterisk/etc/asterisk"
DOWNLOAD_FOLDER="/opt/asterisk/etc/asterisk/downloads"
API_BASE_URL="https://rocketvoip-staging.herokuapp.com/v1/"
API_PATH="${API_BASE_URL}configuration/zip"
API_USER="masteradmin@rocketvoip.local"
API_PASS="4h6rb42q7eoupbn185a4vodeot"
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
