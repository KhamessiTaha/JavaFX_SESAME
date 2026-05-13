@echo off
setlocal enabledelayedexpansion

set JAVA_HOME=C:\Program Files\JetBrains\IntelliJ IDEA 2026.1.1\jbr
set PROJECT_DIR=C:\Users\SETUP\IdeaProjects\JavaFX_Sesame

cd /d %PROJECT_DIR%

echo.
echo ========================================
echo Car Rental Management Application
echo ========================================
echo.
echo Launching application...
echo.

call "C:\Program Files\JetBrains\IntelliJ IDEA 2026.1.1\plugins\maven\lib\maven3\bin\mvn.cmd" clean javafx:run

echo.
echo Application closed.
echo.
pause

