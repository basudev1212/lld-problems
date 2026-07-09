# ATM — Design

## Class Diagram

```mermaid
classDiagram
    class OperationType {
        <<enumeration>>
        DEPOSIT_CASH
        WITHDRAW_CASH
        CHECK_BALANCE
    }

    class Card {
        -String cardNumber
        -String expiryDate
        -String cvv
        +getCardNumber() String
    }

    class Account {
        -String accountNumber
        -String holderName
        -String pin
        -double balance
        +getBalance() double
        +setBalance(double) void
    }

    class ATM {
        <<singleton>>
        -static ATM instance
        -String atmId
        -String location
        -CashDispenser cashDispenser
        -BankingService bankingService
        +static getInstance(...) ATM
        +getCashDispenser() CashDispenser
        +getBankingService() BankingService
    }

    class CashDispenser {
        -Map~Integer,Integer~ denominationCounts
        -DenominationStrategy strategy
        +loadCash(int denomination, int count) void
        +dispense(int amount) boolean
        -buildChain() CashDispenseHandler
    }

    class BankingService {
        -Map~String,Account~ accountByCardNumber
        +linkCardToAccount(Card, Account) void
        +getAccount(Card) Account
        +validatePin(Account, String) boolean
        +getBalance(Account) double
        +debit(Account, double) void
        +credit(Account, double) void
    }

    class DenominationStrategy {
        <<interface>>
        +getDenominationOrder(Set~Integer~) List~Integer~
    }

    class LowerDenominationFirst {
        +getDenominationOrder(Set~Integer~) List~Integer~
    }

    class HigherDenominationFirst {
        +getDenominationOrder(Set~Integer~) List~Integer~
    }

    class CashDispenseHandler {
        <<interface>>
        +handleRequest(int amount) int
        +setNextLevel(CashDispenseHandler) void
    }

    class AbstractCashHandler {
        <<abstract>>
        -int denomination
        -Map~Integer,Integer~ denominationCounts
        -CashDispenseHandler next
        +handleRequest(int amount) int
        +setNextLevel(CashDispenseHandler) void
    }

    class FiveHundredCashHandler
    class TwoHundredCashHandler
    class HundredCashHandler

    class ATMState {
        <<interface>>
        +insertCard(ATMSystem, Card) void
        +enterPIN(ATMSystem, String) void
        +selectOperation(ATMSystem, OperationType) void
        +ejectCard(ATMSystem) void
    }

    class IdleState
    class CardInsertedState
    class AuthenticatedState

    class ATMSystem {
        -ATM atm
        -ATMState currentState
        -Card currentCard
        -Account currentAccount
        +insertCard(Card) void
        +enterPIN(String) void
        +selectOperation(OperationType) void
        +ejectCard() void
        +setState(ATMState) void
    }

    class ATMApplication {
        +main(String[] args) void
    }

    DenominationStrategy <|.. LowerDenominationFirst
    DenominationStrategy <|.. HigherDenominationFirst

    CashDispenseHandler <|.. AbstractCashHandler
    AbstractCashHandler <|-- FiveHundredCashHandler
    AbstractCashHandler <|-- TwoHundredCashHandler
    AbstractCashHandler <|-- HundredCashHandler
    AbstractCashHandler --> CashDispenseHandler : next

    CashDispenser --> DenominationStrategy : uses
    CashDispenser ..> CashDispenseHandler : builds chain
    CashDispenser --> ATM

    BankingService --> Account : reads/writes
    BankingService --> Card : looks up

    ATM --> CashDispenser
    ATM --> BankingService

    ATMState <|.. IdleState
    ATMState <|.. CardInsertedState
    ATMState <|.. AuthenticatedState
    IdleState ..> CardInsertedState : transitions to
    CardInsertedState ..> AuthenticatedState : transitions to
    CardInsertedState ..> IdleState : eject
    AuthenticatedState ..> IdleState : eject

    ATMSystem --> ATMState : currentState
    ATMSystem --> Card : currentCard
    ATMSystem --> Account : currentAccount
    ATMSystem --> ATM

    ATMApplication --> ATMSystem
    ATMApplication --> ATM : getInstance()
    ATMState ..> OperationType
```

## Session Flow (Sequence)

```mermaid
sequenceDiagram
    participant App as ATMApplication
    participant Sys as ATMSystem
    participant State as ATMState (Idle/CardInserted/Authenticated)
    participant Bank as BankingService
    participant Dispenser as CashDispenser

    App->>Sys: insertCard(card)
    Sys->>State: insertCard(sys, card)
    Note over State: IdleState -> CardInsertedState

    App->>Sys: enterPIN(pin)
    Sys->>State: enterPIN(sys, pin)
    State->>Bank: getAccount(card)
    State->>Bank: validatePin(account, pin)
    Bank-->>State: true / false
    Note over State: CardInsertedState -> AuthenticatedState (if valid)

    App->>Sys: selectOperation(CHECK_BALANCE)
    Sys->>State: selectOperation(sys, type)
    State->>Bank: getBalance(account)
    Bank-->>State: balance

    App->>Sys: ejectCard()
    Sys->>State: ejectCard(sys)
    Note over State: any state -> IdleState
```

## Key Design Decisions

1. **Singleton** — `ATM` represents the single physical machine the application
   models, so it exposes a static `getInstance(...)` instead of a public constructor.
   The first call wires in its `CashDispenser` and `BankingService`; every later call
   (including the no-arg `getInstance()`) returns that same instance.

2. **Service layer, separate from data models** — `CashDispenser` and `BankingService`
   live in `atm.service` because they hold *behavior* (dispensing cash, validating a
   PIN, debiting/crediting an account), not just data. `atm.model` is left with plain
   entities (`Card`, `Account`) plus `ATM`, which is really a composition root/facade
   over the two services rather than a data record itself.

3. **State Pattern** — `ATMSystem` is the context; `ATMState` implementations
   (`IdleState`, `CardInsertedState`, `AuthenticatedState`) each decide which of the
   four actions are legal and drive the transition to the next state. Invalid actions
   for a state (e.g. entering a PIN with no card inserted) throw immediately instead
   of silently no-oping.

4. **Strategy + Chain of Responsibility, split by responsibility** — `CashDispenser`
   asks its `DenominationStrategy` **which order** to try denominations in
   (`LowerDenominationFirst` vs `HigherDenominationFirst`), then links
   `CashDispenseHandler`s in that order. Each handler only knows **how to serve its
   own denomination** and pass the remainder down the chain — so the ordering policy
   and the per-denomination dispensing math stay independent and swappable.

5. **`AbstractCashHandler` removes boilerplate** — `FiveHundredCashHandler`,
   `TwoHundredCashHandler`, `HundredCashHandler` are now three-line subclasses that
   just fix a denomination; the shared notes-needed/available/remainder math and the
   `next` pointer live once in the abstract base.

6. **`BankingService` is a concrete class, not an interface** — it implements the
   basic steps directly (in-memory `Map<cardNumber, Account>`, PIN check, balance
   debit/credit) rather than being an abstraction over multiple backends. If a real
   backend integration is ever needed, this class is the natural seam to extract an
   interface from later — no need to design for it up front.

7. **Known gap, deliberately deferred** — `ATMState.selectOperation` doesn't carry an
   amount, so `DEPOSIT_CASH`/`WITHDRAW_CASH` currently throw
   `UnsupportedOperationException`. `CHECK_BALANCE` needs no amount, so it's fully
   wired. Also, `CashDispenser.dispense` mutates denomination counts as it walks the
   chain with no rollback if the full amount can't be served — worth a two-pass
   check (can-fulfill, then commit) before wiring up real withdrawals.

## How to Run

```bash
cd atm
javac -d out $(find . -name "*.java")
java -cp out atm.ATMApplication
```
