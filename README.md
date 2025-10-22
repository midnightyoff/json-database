# JSON Database (Java)

A minimal client–server key–value store that saves data as JSON.  
It supports **nested keys** (JSON path as an array) and **arbitrary JSON values** (objects, arrays, numbers, booleans, null, strings).  
The server is concurrent, safely handles multiple clients, and persists data to disk.

## Features

- **JSON over TCP**: client sends one JSON request per connection; server replies with one JSON response and closes the socket.
- **Operations**:
  - `get` — read by key or path
  - `set` — create/update value at key/path
  - `delete` — remove key/path
  - `exit` — stop the server
- **JCommander** — parse `-t/-k/-v` and `-in <file>` modes
- **Nested keys**: `key` can be a string (`"name"`) **or** an array `["person","surname"]`.
- **Any JSON value**: store objects, arrays, numbers, booleans, `null`, or strings.
- **Persistence**: data is stored at `src/server/data/db.json`.
- **Concurrency**: multiple clients; `get` is concurrent, `set/delete` are serialized via a `ReadWriteLock`.
- **Predictable logs** (for tests/CI):
  - Server: `Server started!`
  - Client: `Client started!`, `Sent: ...`, `Received: ...`

---

## Arguments

The arguments will be passed to the client in the following format:

```
java Main -t <type> -k <key> [-v <value>]
```
- `-t` specifies the type of request (get, set, or delete).
- `-k `specifies the key.
- `-v` specifies the value (only needed for set requests).
- `-in` specifies the file name at `/client/data directory` which contains request 

**For example**: 
- `java Main -t set -k "text" -v "Here is some text to store on the server"`
- `java Main -in "userRequest.json"`

## Preview 

<img width="1210" height="161" alt="image" src="https://github.com/user-attachments/assets/51d643e9-b099-418d-9c6c-0c9f9128e352" />
