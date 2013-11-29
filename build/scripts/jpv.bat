@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  jpv startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

@rem Add default JVM options here. You can also use JAVA_OPTS and JPV_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windowz variants

if not "%OS%" == "Windows_NT" goto win9xME_args
if "%@eval[2+2]" == "4" goto 4NT_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*
goto execute

:4NT_args
@rem Get arguments from the 4NT Shell from JP Software
set CMD_LINE_ARGS=%$

:execute
@rem Setup the command line

set CLASSPATH=.;%APP_HOME%\lib\jpv-1.0.jar;%APP_HOME%\lib\ui-1.0.jar;%APP_HOME%\lib\joda-time-2.1.jar;%APP_HOME%\lib\ant-1.8.4.jar;%APP_HOME%\lib\commons-lang3-3.1.jar;%APP_HOME%\lib\slf4j-api-1.6.1.jar;%APP_HOME%\lib\jcl-over-slf4j-1.6.1.jar;%APP_HOME%\lib\slf4j-log4j12-1.6.1.jar;%APP_HOME%\lib\velocity-1.7.jar;%APP_HOME%\lib\apache-log4j-extras-1.1.jar;%APP_HOME%\lib\groovy-all-1.8.6.jar;%APP_HOME%\lib\ant-launcher-1.8.4.jar;%APP_HOME%\lib\log4j-1.2.16.jar;%APP_HOME%\lib\commons-collections-3.2.1.jar;%APP_HOME%\lib\commons-lang-2.4.jar;%APP_HOME%\lib\spring-asm-3.1.2.RELEASE.jar;%APP_HOME%\lib\commons-logging-1.1.1.jar;%APP_HOME%\lib\spring-core-3.1.2.RELEASE.jar;%APP_HOME%\lib\spring-beans-3.1.2.RELEASE.jar;%APP_HOME%\lib\aopalliance-1.0.jar;%APP_HOME%\lib\spring-aop-3.1.2.RELEASE.jar;%APP_HOME%\lib\spring-expression-3.1.2.RELEASE.jar;%APP_HOME%\lib\spring-context-3.1.2.RELEASE.jar;%APP_HOME%\lib\spring-context-support-3.1.2.RELEASE.jar;%APP_HOME%\lib\spring-tx-3.1.2.RELEASE.jar;%APP_HOME%\lib\spring-jdbc-3.1.2.RELEASE.jar;%APP_HOME%\lib\spring-orm-3.1.2.RELEASE.jar;%APP_HOME%\lib\spring-data-commons-core-1.3.2.RELEASE.jar;%APP_HOME%\lib\aspectjrt-1.6.12.jar;%APP_HOME%\lib\spring-data-jpa-1.1.1.RELEASE.jar;%APP_HOME%\lib\javassist-3.15.0-GA.jar;%APP_HOME%\lib\antlr-2.7.7.jar;%APP_HOME%\lib\jboss-transaction-api_1.1_spec-1.0.0.Final.jar;%APP_HOME%\lib\xml-apis-1.3.02.jar;%APP_HOME%\lib\dom4j-1.6.1.jar;%APP_HOME%\lib\hibernate-jpa-2.0-api-1.0.1.Final.jar;%APP_HOME%\lib\jboss-logging-3.1.0.CR2.jar;%APP_HOME%\lib\hibernate-commons-annotations-4.0.1.Final.jar;%APP_HOME%\lib\hibernate-core-4.0.1.Final.jar;%APP_HOME%\lib\hibernate-entitymanager-4.0.1.Final.jar;%APP_HOME%\lib\validation-api-1.0.0.GA.jar;%APP_HOME%\lib\hibernate-validator-4.3.0.Final.jar;%APP_HOME%\lib\c3p0-0.9.1.jar;%APP_HOME%\lib\hibernate-c3p0-4.0.1.Final.jar;%APP_HOME%\lib\ehcache-core-2.4.3.jar;%APP_HOME%\lib\hibernate-ehcache-4.0.1.Final.jar;%APP_HOME%\lib\jsr305-1.3.9.jar;%APP_HOME%\lib\guava-11.0.2.jar;%APP_HOME%\lib\mysema-commons-lang-0.2.2.jar;%APP_HOME%\lib\asm-3.1.jar;%APP_HOME%\lib\cglib-2.2.jar;%APP_HOME%\lib\querydsl-core-2.7.0.jar;%APP_HOME%\lib\codegen-0.5.1.jar;%APP_HOME%\lib\javax.inject-1.jar;%APP_HOME%\lib\querydsl-codegen-2.7.0.jar;%APP_HOME%\lib\querydsl-apt-2.7.0.jar;%APP_HOME%\lib\querydsl-jpa-2.7.0.jar;%APP_HOME%\lib\postgresql-9.1-901.jdbc4.jar;%APP_HOME%\lib\icu4j-49.1.jar;%APP_HOME%\lib\core-1.0.jar;%APP_HOME%\lib\commons-beanutils-1.8.3.jar;%APP_HOME%\lib\commons-digester-2.1.jar;%APP_HOME%\lib\bcmail-jdk14-138.jar;%APP_HOME%\lib\bcprov-jdk14-138.jar;%APP_HOME%\lib\bcprov-jdk14-1.38.jar;%APP_HOME%\lib\bcmail-jdk14-1.38.jar;%APP_HOME%\lib\bctsp-jdk14-1.38.jar;%APP_HOME%\lib\itext-2.1.7.jar;%APP_HOME%\lib\jcommon-1.0.15.jar;%APP_HOME%\lib\jfreechart-1.0.12.jar;%APP_HOME%\lib\jdtcore-3.1.0.jar;%APP_HOME%\lib\castor-1.2.jar;%APP_HOME%\lib\poi-3.7.jar;%APP_HOME%\lib\stax-api-1.0.1.jar;%APP_HOME%\lib\xmlbeans-2.3.0.jar;%APP_HOME%\lib\geronimo-stax-api_1.0_spec-1.0.jar;%APP_HOME%\lib\poi-ooxml-schemas-3.7.jar;%APP_HOME%\lib\poi-ooxml-3.7.jar;%APP_HOME%\lib\jackson-core-asl-1.9.4.jar;%APP_HOME%\lib\jackson-mapper-asl-1.9.4.jar;%APP_HOME%\lib\jasperreports-4.7.0.jar;%APP_HOME%\lib\jasperreports-fonts-4.0.0.jar;%APP_HOME%\lib\reports-1.0.jar;%APP_HOME%\lib\miglayout-core-4.2.jar;%APP_HOME%\lib\miglayout-swing-4.2.jar

@rem Execute jpv
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %JPV_OPTS%  -classpath "%CLASSPATH%" mx.lux.pos.ui.MainWindow %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable JPV_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%JPV_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
