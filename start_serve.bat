@echo off
chcp 65001 >nul
echo ================================
echo 正在启动服务，请稍候...
echo ================================

echo 检查环境是否安装...
java -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
  echo 错误：未检测到 Java 环境！请检查是否已安装 Java 并配置环境变量。
  pause
  exit /b
)

echo 配置启动参数...
set "JAR_FILE=junchuang.jar"
REM 指定要运行的 JAR 文件——根据启动的文件名可以修改

set "MACHINE_CODE=A000001"
REM 机器码——每台机器都是固定的，不可更改

set "DATA_CILANTRO=5"
REM 香菜默认值 (克)——0~100

set "DATA_CHOPPED_GREEN_ONION=5"
REM 葱花默认值 (克)——0~100

set "DATA_INGREDIENT1_VALUE=10"
REM 配料1的值 (克)——0~100

set "DATA_INGREDIENT2_VALUE=10"
REM 配料2的值 (克)——0~100

set "DATA_SOUP_EXTRACTION_TIME=10"
REM 抽汤时间（秒）——0~100

set "DATA_SOUP_HEATING_TEMPERATURE=120"
REM 汤加热温度（℃）——0~150

set "DATA_SOUP_INSULATION_TEMPERATURE=80"
REM 汤保温温度（℃）——0~100

set "DATA_STEAM_ADDITION_TIME_SECONDS=15"
REM 加蒸汽时间（秒）——0~100

set "DATA_SOUP_EXHAUST_TIME=10"
REM 汤排气时间（秒）——0~100

set "DATA_LADLE_WALKING_DISTANCE_VALUE=1900"
REM 菜勺走动距离值（脉冲值）——不要随意改动，改了传感器位置一起动

set "LADLE_DISH_DUMPING_DISTANCE_PULSE_VALUE=1300"
REM 菜勺倒菜距离（脉冲值）

set "DATA_LADLE_DUMPING_DISTANCE_PULSE=1300"
REM 菜勺倒菜距离（脉冲值）——不要随意改动，改了传感器位置一起动

set "DATA_LADLE_DUMPING_ROTATION=1300"
REM 菜勺倒菜转动值（脉冲值）——不要随意改动，改了传感器位置一起动

set "DATA_FAN_PUSH_ROD_THRUST=1000"
REM 粉丝推杆推动距离值（脉冲值）——不要随意改动，改了传感器位置一起动

set "DATA_OPEN_FAN_TIME=90"
REM 风扇开启时间（秒）——0~999

set "DATA_IS_USE_WEIGHING=false"
REM 是否使用称重——使用称重模块为true,不使用为false

set "DATA_DEFAULT_WEIGHING_VALUE=10"
REM 称重默认值（克）——0~3000

set "FAN_PUSH_ROD_THRUST_DISTANCE_VALUE=1000"
REM  粉丝推杆推动距离值（脉冲值）——不要随意改动

set "OPEN_FAN_TIME=90"
REM 打开风扇时间(秒)——0~200

set "IS_USE_WEIGHING=false"
REM 是否使用称重——0~3000

set "DEFAULT_WEIGHING_VALUE=10"
REM 称重默认值(克)——0~200

set "DISPENSE_SOUP_BY_PULSE_COUNT=10"
REM 抽汤脉冲值(每个脉中约50ml) —0~200

REM 价格和份量配置
set "PORTION_SMALL_PRICE=10"
REM 小份价格

set "PORTION_SMALL_QUANTITY=4"
REM 小份分量

set "PORTION_MID_PRICE=15"
REM 中份价格

set "PORTION_MID_QUANTITY=6"
REM 中份分量

set "PORTION_LARGE_PRICE=20"
REM 大份价格

set "PORTION_LARGE_QUANTITY=8"
REM 大份分量

set "PORTION_ADD_MEAT_PRICE=30"
REM 加肉价格

set "PORTION_ADD_MEAT_QUANTITY=10"
REM 加肉分量


echo 启动服务中...
java ^
  -Dfile.encoding=UTF-8 ^
  -jar %JAR_FILE% ^
  --machineCode=%MACHINE_CODE% ^
  --data.cilantro=%DATA_CILANTRO% ^
  --data.choppedGreenOnion=%DATA_CHOPPED_GREEN_ONION% ^
  --data.ingredient1Value=%DATA_INGREDIENT1_VALUE% ^
  --data.ingredient2Value=%DATA_INGREDIENT2_VALUE% ^
  --data.soupExtractionTime=%DATA_SOUP_EXTRACTION_TIME% ^
  --data.soupHeatingTemperature=%DATA_SOUP_HEATING_TEMPERATURE% ^
  --data.soupInsulationTemperature=%DATA_SOUP_INSULATION_TEMPERATURE% ^
  --data.steamAdditionTimeSeconds=%DATA_STEAM_ADDITION_TIME_SECONDS% ^
  --data.soupExhaustTime=%DATA_SOUP_EXHAUST_TIME% ^
  --data.ladleWalkingDistanceValue=%DATA_LADLE_WALKING_DISTANCE_VALUE% ^
  --data.ladleDishDumpingDistancePulseValue=%LADLE_DISH_DUMPING_DISTANCE_PULSE_VALUE% ^
  --data.fanPushRodThrustDistanceValue=%FAN_PUSH_ROD_THRUST_DISTANCE_VALUE% ^
  --data.openFanTime=%OPEN_FAN_TIME% ^
  --data.isUseWeighing=%IS_USE_WEIGHING% ^
  --data.defaultWeighingValue=%DEFAULT_WEIGHING_VALUE% ^
  --data.dispenseSoupByPulseCount=%DISPENSE_SOUP_BY_PULSE_COUNT% ^
  --data.portionOptions.small.price=%PORTION_SMALL_PRICE% ^
  --data.portionOptions.small.quantity=%PORTION_SMALL_QUANTITY% ^
  --data.portionOptions.mid.price=%PORTION_MID_PRICE% ^
  --data.portionOptions.mid.quantity=%PORTION_MID_QUANTITY% ^
  --data.portionOptions.large.price=%PORTION_LARGE_PRICE% ^
  --data.portionOptions.large.quantity=%PORTION_LARGE_QUANTITY% ^
  --data.portionOptions.addMeat.price=%PORTION_ADD_MEAT_PRICE% ^
  --data.portionOptions.addMeat.quantity=%PORTION_ADD_MEAT_QUANTITY%