compile:
	@echo "***Installing FileEncryptGui_U***"
	cd FileEncryptGui_U1 && mvn install
start:	compile
	@echo "***Starting FileEncryptGui_U***"
	cd FileEncryptGui_U1 && mvn javafx:run
fx:
	@echo "***Starting FileEncryptGui_U ***"
	cd FileEncryptGui_U1 && mvn javafx:run
