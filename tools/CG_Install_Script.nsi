# This installs two files, app.exe and logo.ico, creates a start menu shortcut, builds an uninstaller, and
# adds uninstall information to the registry for Add/Remove Programs

# To get started, put this script into a folder with the two files (app.exe, logo.ico, and license.rtf -
# You'll have to create these yourself) and run makensis on it

# If you change the names "app.exe", "logo.ico", or "license.rtf" you should do a search and replace - they
# show up in a few places.
# All the other settings can be tweaked by editing the !defines at the top of this script
!define APPNAME "Course Generator"
!define COMPANYNAME "Pierre Delore"
!define DESCRIPTION "Calcul de vos temps parcours / Calculate your track time"
# These three must be integers
!define VERSIONMAJOR 4
!define VERSIONMINOR 2
!define VERSIONBUILD 0
# These will be displayed by the "Click here for support information" link in "Add/Remove Programs"
# It is possible to use "mailto:" links in here to open the email client
!define HELPURL "https://techandrun.com/course-generator/" # "Support Information" link
!define UPDATEURL "https://techandrun.com/course-generator/" # "Product Updates" link
!define ABOUTURL "https://techandrun.com/course-generator/" # "Publisher" link
# This is the size (in kB) of all the files copied into "Program Files"
# !define INSTALLSIZE 7233

RequestExecutionLevel admin ;Require admin rights on NT6+ (When UAC is turned on)
#InstallDir "$LOCALAPPDATA\Course_Generator"
InstallDir "$PROGRAMFILES\Course Generator"

# rtf or txt file - remember if it is txt, it must be in the DOS text format (\r\n)
LicenseData "..\gpl-3.0.txt"
# This will be in the installer/uninstaller's title bar
Name "${APPNAME}"
Icon "..\build\cg.ico"
# outFile "Course_Generator_install_4_2_0.exe"
outFile "..\build\Course_Generator_install_${VERSIONMAJOR}_${VERSIONMINOR}_${VERSIONBUILD}.exe"

!include LogicLib.nsh

# Just three pages - license agreement, install location, and installation
page license
page directory
Page instfiles

/*
!macro VerifyUserIsAdmin
UserInfo::GetAccountType
pop $0
${If} $0 != "admin" ;Require admin rights on NT4+
        messageBox mb_iconstop "Administrator rights required!"
        setErrorLevel 740 ;ERROR_ELEVATION_REQUIRED
        quit
${EndIf}
!macroend
*/

function .onInit
	setShellVarContext all
	#!insertmacro VerifyUserIsAdmin
functionEnd

section "install"
	# Files for the install directory - to build the installer, these should be in the same directory as the install script (this file)
	setOutPath $INSTDIR
        SetOverwrite ifnewer
	# Files added here should be removed by the uninstaller (see section "uninstall")
	file "..\build\Course_Generator.exe"
	file "..\build\cg.ico"
	# Add any other files for the install directory (license files, app data, etc) here
	file "..\build\course_generator.jar"
	file "..\build\course_generator.sh"
	file "..\build\en_cg_doc_4.00.epub"
	file "..\build\en_cg_doc_4.00.pdf"
	file "..\build\fr_cg_doc_4.00.epub"
	file "..\build\fr_cg_doc_4.00.pdf"
	file "..\gpl-3.0.txt"
	file /r "..\build\help"

	# Uninstaller - See function un.onInit and section "uninstall" for configuration
	writeUninstaller "$INSTDIR\uninstall.exe"

	# Start Menu
	createDirectory "$SMPROGRAMS\${APPNAME}"
	createShortCut "$SMPROGRAMS\${APPNAME}\${APPNAME}.lnk" "$INSTDIR\Course_Generator.exe" "" "$INSTDIR\cg.ico"
	createShortCut "$SMPROGRAMS\${APPNAME}\CG documentation - FR.lnk" "$INSTDIR\fr_cg_doc_4.00.pdf"
	createShortCut "$SMPROGRAMS\${APPNAME}\CG documentation - EN.lnk" "$INSTDIR\en_cg_doc_4.00.pdf"
	
	# Desktop	
	CreateShortcut "$DESKTOP\Course Generator.lnk" "$INSTDIR\Course_Generator.exe" "" "$INSTDIR\cg.ico"

	# Registry information for add/remove programs
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "DisplayName" "${APPNAME}"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "UninstallString" "$\"$INSTDIR\uninstall.exe$\""
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "QuietUninstallString" "$\"$INSTDIR\uninstall.exe$\" /S"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "InstallLocation" "$\"$INSTDIR$\""
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "DisplayIcon" "$\"$INSTDIR\cg.ico$\""
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "Publisher" "$\"${COMPANYNAME}$\""
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "HelpLink" "$\"${HELPURL}$\""
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "URLUpdateInfo" "$\"${UPDATEURL}$\""
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "URLInfoAbout" "$\"${ABOUTURL}$\""
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "DisplayVersion" "$\"${VERSIONMAJOR}.${VERSIONMINOR}.${VERSIONBUILD}$\""
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "VersionMajor" ${VERSIONMAJOR}
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "VersionMinor" ${VERSIONMINOR}
	# There is no option for modifying or repairing the install
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "NoModify" 1
	WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}" "NoRepair" 1
	# Set the INSTALLSIZE constant (!defined at the top of this script) so Add/Remove Programs can accurately report the size
	# WriteRegDWORD HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${COMPANYNAME} ${APPNAME}" "EstimatedSize" ${INSTALLSIZE}
sectionEnd

# Uninstaller

function un.onInit
	SetShellVarContext all

	#Verify the uninstaller - last chance to back out
	MessageBox MB_OKCANCEL "Permanantly remove ${APPNAME}?" IDOK next
		Abort
	next:
	#!insertmacro VerifyUserIsAdmin
functionEnd

section "uninstall"

	# Remove Start Menu launcher
	delete "$SMPROGRAMS\${APPNAME}\${APPNAME}.lnk"
	delete "$SMPROGRAMS\${APPNAME}\CG documentation - FR.lnk"
	delete "$SMPROGRAMS\${APPNAME}\CG documentation - EN.lnk"

	# Remove Desktop link
	delete "$DESKTOP\Course Generator.lnk"

	# Try to remove the Start Menu folder - this will only happen if it is empty
	rmDir "$SMPROGRAMS\${APPNAME}"

	# Remove files
	delete $INSTDIR\Course_Generator.exe
	delete $INSTDIR\cg.ico
	delete $INSTDIR\course_generator.jar
	delete $INSTDIR\course_generator.sh
	delete $INSTDIR\en_cg_doc_4.00.epub
	delete $INSTDIR\en_cg_doc_4.00.pdf
	delete $INSTDIR\fr_cg_doc_4.00.epub
	delete $INSTDIR\fr_cg_doc_4.00.pdf
	delete $INSTDIR\gpl-3.0.txt
        rmdir /r $INSTDIR\help

	# Always delete uninstaller as the last action
	delete $INSTDIR\uninstall.exe

	# Try to remove the install directory - this will only happen if it is empty
	rmDir $INSTDIR

	# Remove uninstaller information from the registry
	DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${APPNAME}"
sectionEnd