@echo off  
rem Search...  
for /f "delims=" %%i in ('dir /b /a-d /s "*.pyc"') do del %%i
rem finish deleting  
pause 