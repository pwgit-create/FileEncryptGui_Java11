# File Encrypt GUI U1 
##### (Java 11)
File Encrypt is an application that encrypts files with RSA/AES from a graphical user interface. 
The benefit of using this app is the ease of use while getting the safety of 4096 bits RSA encryption. 


![Logo](https://github.com/pwgit-create/FileEncryptGui/blob/main/img/fileEncrypt_small.jpg?raw=true)

## Java version: 11

## Project Libraries

* Java FX : https://docs.oracle.com/javase/8/javase-clienttechnologies.htm

* Apache Commons Codec 1.15 : https://mvnrepository.com/artifact/commons-codec/commons-codec/1.15

* Apache Commons IO 2.11 https://mvnrepository.com/artifact/commons-io/commons-io/2.11.0

* Zip4j 2.11.2 https://mvnrepository.com/artifact/net.lingala.zip4j/zip4j/2.9.1




## Instructions of use
    1. Generate a key pair by clicking on “New Keypair”.

    2. Set the key paths by clicking on the Set Key buttons.

    3. Keys.properties will contain the paths to your keys. This file must exist (and can’t be renamed) in order for the jar/project to run. You can edit this file manually as well 😊 



### Encrypt file

    1. Click on Encrypt to encrypt a file
    2. A new file that ends on.” zc” will be created. That is the file you decrypt or send to a friend.
    3. Remember that if you use a friend’s public key, you can’t decrypt the file yourself.


### Decrypt File
    1. Click on decrypt 
    2. Choose a file that has been encrypted using this app and has the file ending “.zc”. 
    3. If the file has been encrypted with your public key, you can decrypt it with your private key,

### How to Start the Project 

mvn javafx:run


##  How large files can I encrypt?
As big as your heap size in java lets you. 
There is a plan to split the encrypted files into chunks in the next pull request which would let you encrypt [GB+] files.

### What differs this project from the original [FileEncrypt GUI](https://github.com/pwgit-create/FileEncryptGui) Repository?
•	This repository uses Java 11 while the original repo uses Java 8

•	This Repository uses maven

•	The IV-Bytes are generated at random and is written into an encrypted archive 


