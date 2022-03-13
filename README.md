# secure-mail-transfer-project

## Objective 
The objective of this project is to implement a secure way of transferring files when send emails with attachments.
This is done using the Identity Based Encrypton.

## Project tree
```bash
.
├── client
│   ├── controller # link with resources.fxml
│   ├── main # Main client App
│   └── model # All classes used
│       └── encryption # Classes used to encrypt attachments
├── resources
│   ├── fxml # Client layout   
│   └── img # images used in fxml
├── server # Server which generates keys
└── utilities
    ├── config # Config files
    └── curves # Curves used in jpbc

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

