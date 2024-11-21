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
set "JAR_FILE=junchuang-0.0.1-SNAPSHOT.jar"
REM 指定要运行的 JAR 文件

set "MACHINE_CODE=A000002"
REM 机器码

set "DATA_BEEF10=10"
REM 牛肉分量 10 克

set "DATA_BEEF15=15"
REM 牛肉分量 15 克

set "DATA_BEEF20=20"
REM 牛肉分量 20 克

set "DATA_CILANTRO=10"
REM 香菜克数

set "DATA_CHOPPED_GREEN_ONION=10"
REM 葱花克数

set "DATA_SOUP_EXTRACTION_TIME=8 "
REM 抽汤时间（秒）

set "DATA_VIBRATOR_TIME=5"
REM 振动器工作时间（秒）

set "DATA_SOUP_HEATING_TEMPERATURE=120"
REM 汤加热温度（℃）

set "DATA_SOUP_INSULATION_TEMPERATURE=80"
REM 汤保温温度（℃）

set "DATA_STEAM_ADDITION_TIME_SECONDS=10"
REM 加蒸汽时间（秒）

set "DATA_SOUP_EXHAUST_TIME=5"
REM 汤排气时间（秒）

set "DATA_LADLE_WALKING_DISTANCE_VALUE=1900"
REM 菜勺走动距离值（脉冲值）

set "DATA_LADLE_DUMPING_DISTANCE_PULSE=1320"
REM 菜勺倒菜距离（脉冲值）

set "DATA_LADLE_DUMPING_ROTATION=1300"
REM 菜勺倒菜转动值（脉冲值）

set "DATA_FAN_PUSH_ROD_THRUST=1000"
REM 粉丝推杆推动距离值（脉冲值）

echo 启动服务中...
java ^
  -Dfile.encoding=UTF-8 ^
  -jar %JAR_FILE% ^
  --machineCode=%MACHINE_CODE% ^
  --data.beef10=%DATA_BEEF10% ^
  --data.beef15=%DATA_BEEF15% ^
  --data.beef20=%DATA_BEEF20% ^
  --data.cilantro=%DATA_CILANTRO% ^
  --data.choppedGreenOnion=%DATA_CHOPPED_GREEN_ONION% ^
  --data.soupExtractionTime=%DATA_SOUP_EXTRACTION_TIME% ^
  --data.vibratorTime=%DATA_VIBRATOR_TIME% ^
  --data.soupHeatingTemperature=%DATA_SOUP_HEATING_TEMPERATURE% ^
  --data.soupInsulationTemperature=%DATA_SOUP_INSULATION_TEMPERATURE% ^
  --data.steamAdditionTimeSeconds=%DATA_STEAM_ADDITION_TIME_SECONDS% ^
  --data.soupExhaustTime=%DATA_SOUP_EXHAUST_TIME% ^
  --data.ladleWalkingDistanceValue=%DATA_LADLE_WALKING_DISTANCE_VALUE% ^
  --data.ladleDishDumpingDistancePulseValue=%DATA_LADLE_DUMPING_DISTANCE_PULSE% ^
  --data.ladleDishDumpingRotationValue=%DATA_LADLE_DUMPING_ROTATION% ^
  --data.fanPushRodThrustDistanceValue=%DATA_FAN_PUSH_ROD_THRUST%

if %ERRORLEVEL% neq 0 (
  echo 错误：服务启动失败！请检查相关配置和 JAR 文件。
  pause
  exit /b
)

echo ================================
echo 服务已成功启动！
echo ================================
pause

