| Metric |  Notes                             |
| ------ |  --------------------------------- |
| **AC** | How Many Classes depend on this class |
| **EC** |  How Many Classes does this class depend on |
| **I**  | Stability calculated by above metrics (EC / (EC + AC))  |


Main
| Metric | Value | Notes                             |
| ------ | ----- | --------------------------------- |
| **AC** | 0     | No other classes depend on `Main` |
| **EC** | 1     | `MainFrame`      |
| **I**  | 1.0   | Completely unstable  |

CommunicationChannel
| Metric | Value | Notes                             |
| ------ | ----- | --------------------------------- |
| **AC** | 4     | `Subscription` `EmailChannel` `SmsChannel` `Mainframe` |
| **EC** | 0     | Depends on no classes             |
| **I**  | 0   |  Completely Stable |

EmailChannel
| Metric | Value | Notes                             |
| ------ | ----- | --------------------------------- |
| **AC** | 1     |  `Mainframe`  |
| **EC** | 1    | `CommunicationChannel`           |
| **I**  | 0.5   |   |

SmsChannel
| Metric | Value | Notes                             |
| ------ | ----- | --------------------------------- |
| **AC** | 1     |  `Mainframe`  |
| **EC** | 1    | `CommunicationChannel`           |
| **I**  | 0.5   |   |

Notification
| Metric | Value | Notes                             |
| ------ | ----- | --------------------------------- |
| **AC** | 1     | `Subscription`  |
| **EC** | 0    |          |
| **I**  | 0   | Completely Stable  |


Subscription
| Metric | Value | Notes                             |
| ------ | ----- | --------------------------------- |
| **AC** | 2    | `Mainframe` `User`  |
| **EC** | 2    | `CommunicationChannel` `Notification` |
| **I**  | 0.5   |   |

User
| Metric | Value | Notes                             |
| ------ | ----- | --------------------------------- |
| **AC** | 1    | `Mainframe`  |
| **EC** | 1    | `Subscription` |
| **I**  | 0.5   |   |

Mainframe
| Metric | Value | Notes                             |
| ------ | ----- | --------------------------------- |
| **AC** | 1    | `Main`  |
| **EC** | 5    | `CommunicationChannel` `SmsChannel` `EmailChannel` `User` `Subscription` |
| **I**  | 0.83   | Somewhat Unstable  |
