openssl pkcs12 -export -inkey gcp.kennie-ng.cc.key -in gcp.kennie-ng.cc.crt -out gcp.kennie-ng.cc.p12 -name gcp.kennie-ng.cc


keytool -importkeystore -destkeystore www.kennie-ng.cc.jks -srckeystore gcp.kennie-ng.cc.p12 -srcstoretype PKCS12


keytool -import -alias ca -file ca.crt -keystore aws.kennie-ng.cc.jks
