@ECHO OFF

REM Simple batch script to enable bar code printing on Windows machines

REM Steps to use:
REM 1) Share the printer via Windows
REM 2) Run the following from the command line: net use LPT1: "\\[Computer_Name]\[Printer_name]" /Persistent:Yes

REM Then, when the web browser asked you what to use to open the downloaded .epl file, open it with this program

copy %1 LPT1

