RED='\e[1;97;41m'
YELLOW='\e[1;33m'
URL='http://10.100.52.30'
URL='http://local.android'
NC='\e[0m'
echo -e "${RED}ALARM MANAGER${NC}"
echo -e "${YELLOW}adb shell dumpsys alarm > alarm.txt{NC}"
./adb shell dumpsys alarm > alarm.txt