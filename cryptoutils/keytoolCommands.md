# Keytool commands

keytool -list -v -keystore sever.jks

1. keytool -genkey -keyalg RSA -alias server -keystore server.p12 -storepass Teknos01. -validity 360 -keysize 2048
2. keytool -genkey -keyalg RSA -alias client1 -keystore client1.p12 -storepass Teknos01. -validity 360 -keysize 2048 
3. keytool -exportcert -alias client1 -keystore client1.p12 -file client1.cert
4. keytool -exportcert -alias server -keystore server.p12 -file server.cert
5. keytool -importcert -file client1.cert -keystore server.p12 -alias client1
6. keytool -importcert -file server.cert -keystore client1.p12 -alias server
