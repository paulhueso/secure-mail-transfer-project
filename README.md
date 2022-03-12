# secure-mail-transfer-project

## Objective 
The objective of this project is to implement a secure way of transferring files when send emails with attachments.
This is done using the Identity Based Encrypton.

## Project tree
```bash
.
├── README.md
├── jars 
│   ├── javax.mail-1.6.2.jar
│   └── jpbc-2.0.0
│  
├── secure-mail-transfer-project.iml
├── src
│   ├── client.controller # controllers to link JavaFX layout to functions
│   ├── client.main # client.main functions
│   ├── client.model # all classes
│   └── view # all .fxml files
└── static
    └── attachment.txt
```

## Jars recquired
- JPBC
- JavaFX
- activation-1.1.1.jar
- Javax
    
## TODO
- Date to date and not String
- Store emails in ArrayList to accelerate loading
- Handle attachments

