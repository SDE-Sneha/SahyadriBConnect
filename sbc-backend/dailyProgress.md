                    REST API
                       │
                       ▼
              BusinessController
                       │
                       ▼
                BusinessService
                       │
          ┌────────────┼────────────┐
          ▼            ▼            ▼
       Create       Update       Status
                                    │
                              Transition Rules
                                    │
                                    ▼
                          IBusinessRepository
                                    │
                                    ▼
                       InMemoryBusinessRepository