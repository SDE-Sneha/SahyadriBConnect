| #  | Test                                                |
| -- | --------------------------------------------------- |
| 1  | Get all businesses                                  |
| 2  | Get business by ID                                  |
| 3  | Get nonexistent business → 404                      |
| 4  | Create business                                     |
| 5  | Missing business name → 400                         |
| 6  | Update business                                     |
| 7  | Update nonexistent business → 404                   |
| 8  | Delete business + verify 404                        |
| 9  | Delete nonexistent business → 404                   |
| 10 | Approve pending business                            |
| 11 | Reject pending business                             |
| 12 | Suspend approved business                           |
| 13 | Approve suspended business                          |
| 14 | **Invalid PENDING → SUSPENDED → 400**               |
| 15 | **Invalid APPROVED → REJECTED → 400** ← **MISSING** |
| 16 | Approve nonexistent business → 404                  |
| 17 | Missing category → 400                              |
| 18 | Missing city → 400                                  |
| 19 | Missing province → 400                              |
| 20 | Invalid email → 400                                 |
