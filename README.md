## What is Easy Web Server?
Easy Web Server is a simple web server written in Java, as a hobby project. Usage of Easy Web Server without permission is forbidden. Please contact atharvashah123 at gmail dot com for any inquiries regarding usage.

### Known issues
1. EWSO files aren't client independent; one EWSO file is generated which can cause conflicts when considering multiple clients.
2. File streaming may cause issues with limited memory/high filesize environments  
`int available = fis.available();`  
`byte[] bytes = new byte[available];`  
`fis.read(bytes);`  
`sos.write(bytes);`  

### Bucket List

 - [ ] EWSO file should be streamed over, never stored locally, EVER
 - [ ] Implement a permissions system (configuration file that allows the user to configure permissions on directories)
 - [ ] Stream files over in chunks
 - [ ] Include instructions on compilation/setup


#####  &copy;Atharva Shah 2019
