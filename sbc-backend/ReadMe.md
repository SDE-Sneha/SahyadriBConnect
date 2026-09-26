sahyadri-backend/
│
├── pom.xml
│
├── src/
│   ├── main/
│   │   ├── java/com/sbc/
│   │   │
│   │   ├── SbcBackendApplication.java
│   │   │
│   │   ├── api/
│   │   │   ├── BusinessController.java
│   │   │   ├── CategoryController.java
│   │   │   └── AdminController.java
│   │   │
│   │   ├── mcp/
│   │   │   ├── BusinessTools.java
│   │   │   ├── CategoryTools.java
│   │   │   └── AdminTools.java
│   │   │
│   │   ├── service/
│   │   │   ├── BusinessService.java
│   │   │   ├── CategoryService.java
│   │   │   └── ApprovalService.java
│   │   │
│   │   ├── repository/
│   │   │   ├── BusinessRepository.java
│   │   │   └── S3BusinessRepository.java
│   │   │
│   │   ├── model/
│   │   │   ├── Business.java
│   │   │   └── BusinessStatus.java
│   │   │
│   │   ├── dto/
│   │   │   ├── BusinessRequest.java
│   │   │   └── BusinessResponse.java
│   │   │
│   │   └── config/
│   │       ├── SecurityConfig.java
│   │       └── McpConfig.java
│   │
│   └── test/
│       └── java/com/sahyadri/
│           ├── service/
│           ├── api/
│           └── mcp/
│
└── README.md



FEature 
F1 - 
F2 - 
F3 - Transition from one state to another
F4 -  Controlled Business Status Workflow
┌───────────┐
│  PENDING  │
└─────┬─────┘
│
┌─────┴─────┐
↓           ↓
APPROVED      REJECTED
│
↓
SUSPENDED
│
↓
APPROVED

