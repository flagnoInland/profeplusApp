RED='\e[1;97;41m'
YELLOW='\e[1;33m'
NC='\e[0m'
echo -e "${RED}UNINSTALL APP${NC}"
adb uninstall com.equipu.profeplus
echo -e "${RED}INSTALL APP${NC}"
adb install app-release.apk
#echo -e "${RED}START ACTIVITY${NC}"
#adb shell am start -n com.equipu.profeplus/com.equipu.profeplus.activities.StartActivity